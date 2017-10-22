package com.rwb.service;

import java.util.List;

import com.rwb.entity.User;

public interface IUserService {

	public void record(String url, String contextPath, String ip) throws Exception;

	public List<User> findUsersByTime() throws Exception;

	public List<User> findUsersByHot() throws Exception;

	public User getProfile(int sessionUid, int uid) throws Exception;

	public User findUserById(int userId) throws Exception;

	public void updateProfile(User user) throws Exception;

	public String updatePassword(int userId, String password,
			String newpassword, String repassword) throws Exception;

}
