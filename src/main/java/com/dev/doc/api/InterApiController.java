package com.dev.doc.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dev.base.controller.BaseController;
import com.dev.base.exception.code.ErrorCode;
import com.dev.base.json.JsonUtils;
import com.dev.base.util.Pager;
import com.dev.base.util.WebPaginate;
import com.dev.base.utils.ValidateUtils;
import com.dev.doc.entity.Inter;
import com.dev.doc.service.CopyService;
import com.dev.doc.service.InterService;
import com.dev.doc.service.RespSchemaService;
import com.dev.doc.vo.InterInfo;

@RestController
@RequestMapping("/api/doc/inter")
public class InterApiController extends BaseController {

	@Autowired
	private InterService interService;

	@Autowired
	private RespSchemaService respSchemaService;

	@Autowired
	private CopyService copyService;

	/**
	 * 
	 * @name 接口列表
	 * @Description
	 * @CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/list")
	public Pager list(HttpServletRequest request, Model model, Long docId, Long moduleId, String name,
			String description, Integer pageNumber, Integer pageSize) {
		ValidateUtils.notNull(docId, ErrorCode.SYS_001, "文档id不能为空");

		Pager pager = new Pager(pageNumber, pageSize);
		List<Inter> list = interService.listByDocId(docId, moduleId, name, description, pager);
		int count = interService.countByDocId(docId, moduleId, name, description);
		pager.setTotalCount(count);
		pager.setList(list);

		model.addAttribute("pager", pager);
		model.addAttribute("paginate", WebPaginate.build(request, pageNumber, pageSize, count));

		return pager;
	}

	/**
	 * 
	 * @name 接口基本信息
	 * @Description
	 * @CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/info")
	public InterInfo getInfo(HttpServletRequest request, Long docId, Long interId, Model model) {
		InterInfo interInfo = interService.getDetailByDocId(docId, interId);
		// return JsonUtils.createSuccess(interInfo);
		return interInfo;
	}

	/**
	 * 
	 * @name 新增项目信息
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/copy", method = RequestMethod.POST)
	public Map copy(HttpServletRequest request, Long docId, Long interId) {
		ValidateUtils.notNull(docId, ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(interId, ErrorCode.SYS_001, "接口id不能为空");

		Long userId = getUserId(request);
		copyService.copyInter(userId, docId, interId);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 新增接口
	 * @Description
	 * @CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/add")
	public Map add(HttpServletRequest request, Inter inter, Model model) {
		ValidateUtils.notNull(inter.getDocId(), ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(inter.getName(), ErrorCode.SYS_001, "接口名称不能为空");
		ValidateUtils.notNull(inter.getPath(), ErrorCode.SYS_001, "接口url不能为空");

		interService.add(inter);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 更新项目信息
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map update(HttpServletRequest request, Inter inter, Long interId) {
		ValidateUtils.notNull(interId, ErrorCode.SYS_001, "接口id不能为空");
		ValidateUtils.notNull(inter.getDocId(), ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(inter.getName(), ErrorCode.SYS_001, "接口名称不能为空");
		ValidateUtils.notNull(inter.getPath(), ErrorCode.SYS_001, "接口url不能为空");

		inter.setId(interId);
		interService.updateByDocId(inter);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 删除
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/del")
	public Map del(HttpServletRequest request, Long docId, Long interId) {
		ValidateUtils.notNull(docId, ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(interId, ErrorCode.SYS_001, "接口id不能为空");

		interService.deleteByDocId(docId, interId);

		return JsonUtils.createSuccess();
	}

}
