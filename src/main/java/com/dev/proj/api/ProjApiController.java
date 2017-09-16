package com.dev.proj.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.base.controller.BaseController;
import com.dev.base.enums.ProjectStatus;
import com.dev.base.enums.Role;
import com.dev.base.exception.ValidateException;
import com.dev.base.exception.code.ErrorCode;
import com.dev.base.json.JsonUtils;
import com.dev.base.util.Pager;
import com.dev.base.util.WebPaginate;
import com.dev.base.util.WebUtil;
import com.dev.base.utils.MapUtils;
import com.dev.base.utils.ValidateUtils;
import com.dev.doc.service.CopyService;
import com.dev.proj.entity.Project;
import com.dev.proj.service.ProjectMemberService;
import com.dev.proj.service.ProjectService;
import com.dev.proj.vo.ProjectInfo;
import com.dev.user.vo.UserInfo;

@RestController
@RequestMapping(value = "/api/proj")
public class ProjApiController extends BaseController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectMemberService projectMemberService;

	@Autowired
	private CopyService copyService;

	/**
	 * 
	 * @name 新增项目信息
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map add(HttpServletRequest request, Project project) {
		ValidateUtils.notNull(project.getCode(), ErrorCode.SYS_001, "项目编码不能为空");

		Long userId = getUserId(request);
		projectService.add(userId, project);

		// 重新加载项目权限
		reloadProjAuth(request);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 新增项目信息
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/copy", method = RequestMethod.POST)
	public Map copy(HttpServletRequest request, Long projId) {
		ValidateUtils.notNull(projId, ErrorCode.SYS_001, "项目id不能为空");

		UserInfo userInfo = getUserInfo(request);
		ProjectInfo projectInfo = copyService.copyProj(userInfo.getUserId(), projId);
		// 更新当前登陆用户缓存信息
		if (projectInfo != null) {
			userInfo.getProjMap().put(projectInfo.getProjId(), Role.admin);
			userInfo.getDocMap().put(projectInfo.getDocId(), Role.admin);
			saveUserInfo(request, userInfo);
		}

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 更新项目信息
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map update(HttpServletRequest request, Project project, Long projId) {
		ValidateUtils.notNull(projId, ErrorCode.SYS_001, "项目id不能为空");

		project.setId(projId);
		projectService.update(project);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 删除项目信息
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/del")
	public Map update(HttpServletRequest request, Long projId) {
		ValidateUtils.notNull(projId, ErrorCode.SYS_001, "项目id不能为空");

		projectService.deleteById(projId);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 退出项目
	 * @Description
	 * @CreateDate 2015年8月22日下午2:07:39
	 */
	@RequestMapping("/quit")
	public Map quit(HttpServletRequest request, Long projId) {
		ValidateUtils.notNull(projId, ErrorCode.SYS_001, "项目id不能为空");

		Long userId = getUserId(request);
		projectMemberService.quit(userId, projId);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 项目列表
	 * @Description
	 * @CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/list")
	public Pager list(HttpServletRequest request, Model model, String code, String name, String status,
			Integer pageNumber, Integer pageSize) {
		Long userId = getUserId(request);

		ProjectStatus projectStatus = null;
		if (!StringUtils.isEmpty(status)) {
			projectStatus = ProjectStatus.valueOf(status);
		}

		Pager pager = new Pager(pageNumber, pageSize);
		List<ProjectInfo> list = projectService.listByUserId(userId, code, name, projectStatus, pager);
		int count = projectService.countByUserId(userId, code, name, projectStatus);
		pager.setTotalCount(count);
		pager.setList(list);

		model.addAttribute("pager", pager);
		model.addAttribute("paginate", WebPaginate.build(request, pageNumber, pageSize, count));

		return pager;
	}

	/**
	 * 
	 * @name 项目基本信息
	 * @Description
	 * @CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/info")
	public ProjectInfo getInfo(HttpServletRequest request, Long projId, Model model) {
		ValidateUtils.notNull(projId, ErrorCode.SYS_001, "项目id不能为空");

		Long userId = getUserId(request);
		ProjectInfo projectInfo = projectService.getInfo(userId, projId);
		model.addAttribute("projInfo", projectInfo);

		Map<Long, ProjectInfo> sessionMap = MapUtils.newMap();
		sessionMap.put(projId, projectInfo);
		WebUtil.setSessionAttr(request, "projTempMap", sessionMap);

		return projectInfo;
	}

	/**
	 * 
	 * @name 导入swagger json创建新项目
	 * @Description
	 * @CreateDate 2015年10月15日下午11:03:06
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> upload(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file) {
		ValidateUtils.isTrue(file.getOriginalFilename().endsWith(".json"), ErrorCode.DOC_002);
		Map<String, Object> swaggerInfo = null;
		try {
			swaggerInfo = JsonUtils.toObject(new String(file.getBytes(), "UTF-8"), Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidateException(ErrorCode.DOC_002);
		}

		Long userId = getUserId(request);
		projectService.upload(userId, swaggerInfo);

		return JsonUtils.createSuccess();
	}
}
