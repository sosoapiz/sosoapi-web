package com.dev.user.service;

import com.dev.user.vo.LoginParamInfo;
import com.dev.user.vo.UserInfo;

public interface LdapLoginService extends LoginService {

	UserInfo loginByUsername(LoginParamInfo loginParamInfo);
}
