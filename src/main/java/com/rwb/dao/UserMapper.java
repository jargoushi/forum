package com.rwb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rwb.entity.Info;
import com.rwb.entity.User;

public interface UserMapper {

	/**
	 * 
	* @Title: insertInfo
	* @Description: 记录来访者信息
	* @param @param info    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void insertInfo(Info info);

	/**
	 * 
	* @Title: findUserByTime
	* @Description: 查询近期加入的用户
	* @param @return    设定文件
	* @return List<User>    返回类型
	* @throws
	 */
	public List<User> findUserByTime();

	/**
	 * 
	* @Title: findUsersByHot
	* @Description: 查询活跃的用户
	* @param @return    设定文件
	* @return List<User>    返回类型
	* @throws
	 */
	public List<User> findUsersByHot();

	/**
	 * 
	* @Title: findEmailCount
	* @Description: 查询注册邮箱是否存在
	* @param @return    设定文件
	* @return List<User>    返回类型
	* @throws
	 */
	public int findEmailCount(String email);

	/**
	 * 
	* @Title: insertUser
	* @Description: 新增注册用户信息
	* @param @return    设定文件
	* @return List<User>    返回类型
	* @throws
	 */
	public void insertUser(User user);

	/**
	 * 
	* @Title: updataActivate
	* @Description: 修改用户激活状态
	* @param @return    设定文件
	* @return List<User>    返回类型
	* @throws
	 */
	public void updataActivate(String code);

	/**
	 * 
	* @Title: findUidByEmailAndPassword
	* @Description: 通过用户邮箱与密码查询用户id
	* @param @param user
	* @param @return    设定文件
	* @return Integer    返回类型
	* @throws
	 */
	public Integer findUidByEmailAndPassword(User user);

	/**
	 * 
	* @Title: findActivatedByUid
	* @Description: 通过用户主键查询用户是否激活
	* @param @param uid
	* @param @return    设定文件
	* @return int    返回类型
	* @throws
	 */
	public int findActivatedByUid(Integer uid);

	/**
	 * 
	* @Title: findHeadUrlByUid
	* @Description: 通过用户主键查询headUrl
	* @param @param uid
	* @param @return    设定文件
	* @return String    返回类型
	* @throws
	 */
	public String findHeadUrlByUid(Integer uid);

	/**
	 * 
	* @Title: updateScanCount
	* @Description: 浏览量加1
	* @param @param uid    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void updateScanCount(int uid);

	/**
	 * 
	* @Title: findUserByUid
	* @Description: 通过用户id获取用户信息
	* @param @param uid
	* @param @return    设定文件
	* @return User    返回类型
	* @throws
	 */
	public User findUserByUid(int uid);

	/**
	 * 
	* @Title: updateUserProfile
	* @Description: 修改用户个人信息
	* @param @param user    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void updateUserProfile(User user);

	/**
	 * 
	* @Title: findPasswordByUserId
	* @Description: 通过用户id查询用户密码
	* @param @param userId
	* @param @return    设定文件
	* @return String    返回类型
	* @throws
	 */
	public String findPasswordByUserId(int userId);

	/**
	 * 
	* @Title: updatePassword
	* @Description: 修改用户密码
	* @param @param userId
	* @param @param newpassword    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void updatePassword(@Param("userId") int userId,@Param("newPassword") String newpassword);


}
