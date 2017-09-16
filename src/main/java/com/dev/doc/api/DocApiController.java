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
import com.dev.doc.entity.ApiDoc;
import com.dev.doc.service.ApiDocService;
import com.dev.doc.vo.ApiDocInfo;

@RestController
@RequestMapping("/api/doc")
public class DocApiController extends BaseController {
	@Autowired
	private ApiDocService apiDocService;

	/**
	 * 
	 * @api文档列表
	 * @Description
	 * @CreateDate 2015年7月27日下午6:03:42
	 */
	@RequestMapping("list")
	public Pager list(HttpServletRequest request, Model model, String title, Integer pageNumber, Integer pageSize) {
		Long userId = getUserId(request);

		Pager pager = new Pager(pageNumber, pageSize);
		List<ApiDocInfo> list = apiDocService.listByUserId(userId, title, pager);
		int count = apiDocService.countByUserId(userId, title);
		pager.setTotalCount(count);
		pager.setList(list);

		model.addAttribute("pager", pager);
		model.addAttribute("paginate", WebPaginate.build(request, pageNumber, pageSize, count));

		return pager;
	}

	/**
	 * 
	 * @name 更新项目信息
	 * @Description
	 * @CreateDate 2015年8月6日下午5:14:18
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map update(HttpServletRequest request, ApiDoc apiDoc, Long docId) {
		ValidateUtils.notNull(docId, ErrorCode.SYS_001, "文档id不能为空");

		apiDoc.setId(docId);
		apiDocService.update(apiDoc);

		return JsonUtils.createSuccess();
	}

	/**
	 * 
	 * @name 文档信息
	 * @Description
	 * @CreateDate 2015年7月27日下午6:04:52
	 */
	@RequestMapping("info")
	public ApiDoc getInfo(HttpServletRequest request, Long docId, Model model) {
		ValidateUtils.notNull(docId, ErrorCode.SYS_001, "文档id不能为空");

		ApiDoc docInfo = apiDocService.getById(docId);
		model.addAttribute("docInfo", docInfo);

		return docInfo;
	}

}
