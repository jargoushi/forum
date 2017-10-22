package com.rwb.service;

import java.util.Map;

import com.rwb.entity.User;

public interface ILoginService {

	public String register(User user, String repassword) throws Exception;

	public void activate(String code) throws Exception;

	public Map<String, Object> login(User user) throws Exception;

}
