package com.dev.doc.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dev.base.controller.BaseController;
import com.dev.base.exception.code.ErrorCode;
import com.dev.base.json.JsonUtils;
import com.dev.base.utils.ValidateUtils;
import com.dev.base.vo.SelectInfo;
import com.dev.doc.entity.Module;
import com.dev.doc.service.ModuleService;

@RestController
@RequestMapping("/api/doc/module")
public class ModuleApiController extends BaseController {

	@Autowired
	private ModuleService moduleService;

	/**
	 * 
	 * @name 模块列表
	 * @Description 用于填充下拉框
	 * @CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/list")
	public List<Module> listJson(HttpServletRequest request, Long docId) {
		ValidateUtils.notNull(docId, ErrorCode.SYS_001, "文档id不能为空");

		List<Module> list = moduleService.listAllByDocId(docId);

		return list;
	}

	/**
	 * 
	 * @name 新增模块信息
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map add(HttpServletRequest request, Module module) {
		ValidateUtils.notNull(module.getDocId(), ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(module.getName(), ErrorCode.SYS_001, "模块名称不能为空");

		moduleService.add(module);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 编辑模块信息
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map update(HttpServletRequest request, Module module, Long moduleId) {
		ValidateUtils.notNull(module.getDocId(), ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(moduleId, ErrorCode.SYS_001, "模块id不能为空");
		ValidateUtils.notNull(module.getName(), ErrorCode.SYS_001, "模块名称不能为空");

		module.setId(moduleId);
		moduleService.updateByDocId(module);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 删除模块信息
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/del")
	public Map del(HttpServletRequest request, Long docId, Long moduleId) {
		ValidateUtils.notNull(docId, ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(moduleId, ErrorCode.SYS_001, "模块id不能为空");

		moduleService.deleteByDocId(docId, moduleId);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 模块基本信息
	 * @Description
	 * @CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/info")
	public Module getInfo(Long moduleId) {
		ValidateUtils.notNull(moduleId, ErrorCode.SYS_001, "模块id不能为空");
		Module module = moduleService.getById(moduleId);

		return module;
	}
}
