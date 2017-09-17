package com.dev.doc.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.base.controller.BaseController;
import com.dev.base.exception.code.ErrorCode;
import com.dev.base.json.JsonUtils;
import com.dev.base.utils.ValidateUtils;
import com.dev.doc.entity.InterResp;
import com.dev.doc.service.InterRespService;

@RestController
@RequestMapping("/api/doc/inter/resp")
public class InterRespApiController extends BaseController {

	@Autowired
	private InterRespService interRespService;

	/**
	 * 
	 * @name 查询接口响应列表
	 * @Description
	 * @CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/list")
	public List<InterResp> list(HttpServletRequest request, Long docId, Long interId) {
		ValidateUtils.notNull(interId, ErrorCode.SYS_001, "接口id不能为空");

		List<InterResp> interRespList = interRespService.listAllByInterId(docId, interId);
		// return JsonUtils.createSuccess(interRespList);
		return interRespList;
	}

	/**
	 * 
	 * @name 新增
	 * @Description
	 * @CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map add(HttpServletRequest request, InterResp interResp) {
		ValidateUtils.notNull(interResp.getDocId(), ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(interResp.getInterId(), ErrorCode.SYS_001, "接口id不能为空");
		ValidateUtils.notNull(interResp.getCode(), ErrorCode.SYS_001, "响应编码不能为空");

		interRespService.add(interResp);
		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 编辑
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map update(HttpServletRequest request, InterResp interResp, Long respId) {
		ValidateUtils.notNull(interResp.getDocId(), ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(respId, ErrorCode.SYS_001, "响应id不能为空");
		ValidateUtils.notNull(interResp.getCode(), ErrorCode.SYS_001, "编码不能为空");

		interResp.setId(respId);
		interRespService.updateByDocId(interResp);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 删除
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/del")
	public Map del(HttpServletRequest request, Long docId, Long respId) {
		ValidateUtils.notNull(docId, ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(respId, ErrorCode.SYS_001, "响应id不能为空");

		interRespService.deleteByDocId(docId, respId);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 查询基本信息
	 * @Description
	 * @CreateDate 2015年7月11日下午2:05:24
	 */
	@RequestMapping("/info")
	public InterResp getInfo(Long docId, Long respId) {
		ValidateUtils.notNull(docId, ErrorCode.SYS_001, "文档id不能为空");
		ValidateUtils.notNull(respId, ErrorCode.SYS_001, "响应id不能为空");
		InterResp interResp = interRespService.getByDocId(docId, respId);

		// return JsonUtils.createSuccess(interResp);
		return interResp;
	}
}
