package com.dev.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.base.constant.AppConstants;
import com.dev.base.enums.LoginType;
import com.dev.base.exception.ValidateException;
import com.dev.base.exception.code.ErrorCode;
import com.dev.base.ldap.entity.Person;
import com.dev.base.ldap.service.LdapService;
import com.dev.base.utils.CryptUtil;
import com.dev.user.entity.UserBasic;
import com.dev.user.service.LdapLoginService;
import com.dev.user.service.RegistService;
import com.dev.user.service.UserBasicService;
import com.dev.user.vo.LoginParamInfo;
import com.dev.user.vo.RegistParamInfo;
import com.dev.user.vo.UserInfo;

@Service
public class LdapLoginServiceImpl extends LoginServiceImpl implements LdapLoginService {
	
	@Autowired
	LdapService ldapService;
	
	@Autowired
	UserBasicService userBasicService;
	@Autowired
	RegistService registService;

	@Override
	public UserInfo loginByUsername(LoginParamInfo loginParamInfo) {
		try {
			Person person = ldapService.authenticate(loginParamInfo.getEmail(), loginParamInfo.getPassword());
			UserBasic userBasic = userBasicService.getByEmail(person.getEmail());
			
			if (null == userBasic) {
				regist(person, loginParamInfo);
				userBasic = userBasicService.getByEmail(person.getEmail());
			}
			loginParamInfo.setEmail(person.getEmail());
			return loginByEmail(loginParamInfo);
		} catch (Exception e) {
			throw new ValidateException(ErrorCode.LOGIN_004);
		}
	}
	
	private void regist(Person person, LoginParamInfo loginParamInfo) {
		RegistParamInfo registParamInfo = new RegistParamInfo();
		registParamInfo.setEmail(person.getEmail());
		registParamInfo.setLoginType(LoginType.email);
		registParamInfo.setNickName(person.getFirstName() + person.getLastName());
		registParamInfo.setPassword(loginParamInfo.getPassword());
		registParamInfo.setPhone(person.getMobile());
		registParamInfo.setRegistIp(loginParamInfo.getLoginIp());
		
		String code = CryptUtil.encryptAES(person.getEmail(), AppConstants.DEFAULT_SECRET_KEY);
		registService.registByEmail(registParamInfo);
		registService.activeByEmail(code);
	} 

}
