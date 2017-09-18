package com.dev.user.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.base.constant.AppConstants;
import com.dev.base.constant.CfgConstants;
import com.dev.base.controller.BaseController;
import com.dev.base.enums.LoginType;
import com.dev.base.exception.TipException;
import com.dev.base.exception.ValidateException;
import com.dev.base.exception.code.ErrorCode;
import com.dev.base.util.WebUtil;
import com.dev.base.utils.ValidateUtils;
import com.dev.user.service.LdapLoginService;
import com.dev.user.service.UserTokenService;
import com.dev.user.vo.LoginParamInfo;
import com.dev.user.vo.UserInfo;

@RestController
@RequestMapping("/api/ldap")
public class LdapLoginApiController extends BaseController {

	@Autowired
	LdapLoginService ldapLoginService;
	
	@Autowired
	private UserTokenService userTokenService;

	/**
	 * 登录
	 */
	@RequestMapping("/login")
	public UserInfo login(HttpServletRequest request, HttpServletResponse response, String loginName, String passwd,
			String validCode, boolean autoLogin) {
		ValidateUtils.notNull(loginName, ErrorCode.SYS_001, "登陆名不能为空");
		ValidateUtils.notNull(passwd, ErrorCode.SYS_001, "登陆密码不能为空");

		// 目前默认使用邮箱登陆
		LoginParamInfo paramInfo = new LoginParamInfo();
		paramInfo.setEmail(loginName);
		paramInfo.setPassword(passwd);
		paramInfo.setLoginIp(WebUtil.getClientIp(request));
		paramInfo.setLoginType(LoginType.email);
		paramInfo.setAutoLogin(autoLogin);

		UserInfo userInfo = null;
		try {
			// ValidateUtils.isTrue(validCode.equals(WebUtil.getSessionAttr(request, AppConstants.CAPTCHA_LOGIN)),
			//		ErrorCode.LOGIN_003);
			userInfo = ldapLoginService.loginByUsername(paramInfo);
		} catch (ValidateException e) {
			throw new TipException("forward:/forwardLogin.htm", e);
		}

		// 保存登陆用户信息
		WebUtil.setSessionAttr(request, AppConstants.SESSION_KEY_USER, userInfo);

		// 处理token，保存到cookie中
		if (autoLogin) {
			WebUtil.addCookie(response, CfgConstants.COOKIE_TOKEN_NAME, userInfo.getToken(),
					CfgConstants.COOKIE_TOKEN_EXPIRE);
		}

		// 设置登陆跳转页面
		return userInfo;
	}
}
