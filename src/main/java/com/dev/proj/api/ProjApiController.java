package com.dev.proj.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.base.controller.BaseController;
import com.dev.base.enums.ProjectStatus;
import com.dev.base.exception.code.ErrorCode;
import com.dev.base.util.Pager;
import com.dev.base.util.WebPaginate;
import com.dev.base.util.WebUtil;
import com.dev.base.utils.MapUtils;
import com.dev.base.utils.ValidateUtils;
import com.dev.doc.service.CopyService;
import com.dev.proj.service.ProjectMemberService;
import com.dev.proj.service.ProjectService;
import com.dev.proj.vo.ProjectInfo;

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
			*@name 项目列表
			*@Description  
			*@CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/list")
	public Pager list(HttpServletRequest request,Model model,String code,String name,
						String status,Integer pageNumber,Integer pageSize){
		Long userId = getUserId(request);
		
		ProjectStatus projectStatus = null;
		if (!StringUtils.isEmpty(status)) {
			projectStatus = ProjectStatus.valueOf(status);
		}
		
		Pager pager = new Pager(pageNumber, pageSize);
		List<ProjectInfo> list = projectService.listByUserId(userId,code,name, projectStatus, pager);
		int count = projectService.countByUserId(userId,code,name, projectStatus);
		pager.setTotalCount(count);
		pager.setList(list);
		
		model.addAttribute("pager", pager);
		model.addAttribute("paginate",WebPaginate.build(request, pageNumber, pageSize, count));
		
		return pager;
	}
	
	/**
	 * 
			*@name 项目基本信息
			*@Description  
			*@CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/info")
	public ProjectInfo getInfo(HttpServletRequest request,Long projId,Model model){
		ValidateUtils.notNull(projId, ErrorCode.SYS_001,"项目id不能为空");
		
		Long userId = getUserId(request);
		ProjectInfo projectInfo = projectService.getInfo(userId, projId);
		model.addAttribute("projInfo", projectInfo);
		
		Map<Long, ProjectInfo> sessionMap = MapUtils.newMap();
		sessionMap.put(projId, projectInfo);
		WebUtil.setSessionAttr(request, "projTempMap", sessionMap);
		
		return projectInfo;
	}
}
