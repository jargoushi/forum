package com.rwb.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.rwb.contanst.ForumException;
import com.rwb.dao.UserMapper;
import com.rwb.entity.Info;
import com.rwb.entity.User;
import com.rwb.service.IUserService;

/**
 * 
* @ClassName: UserServiceImpl
* @Description: 用户管理Service
* @author ruwenbo
* @date 2017年10月21日 下午9:02:08
*
 */
@Service("userService")
public class UserServiceImpl implements IUserService{

	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * 记录来访者信息
	 * url : 访问的完整路径
	 * contextPath : 项目根目录
	 * ip : 来访者的ip地址
	 */
	@Override
	public void record(String url, String contextPath, String ip) throws Exception{
		Info info = new Info();
		info.setRequestUrl(url);
		info.setContextPath(contextPath);
		info.setRemoteAddr(ip);
		try {
			userMapper.insertInfo(info);
		} catch (Exception e) {
			log.error(ForumException.INSERT_EXCEPTION, e);
			throw new Exception();
		}
	}

	/**
	 * 获取最近加入的用户
	 */
	@Override
	public List<User> findUsersByTime() throws Exception {
		List<User> userList = null;
		try {
			userList = userMapper.findUserByTime();
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		return userList;
	}

	/**
	 * 获取活跃的用户
	 */
	@Override
	public List<User> findUsersByHot() throws Exception {
		List<User> userList = null;
		try {
			userList = userMapper.findUsersByHot();
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		return userList;
	}

	/**
	 * 获取个人主页信息
	 */
	@Override
	public User getProfile(int sessionUid, int uid) throws Exception {
		// 如果是浏览别人的主页，则浏览量加1
		if (sessionUid != uid) {
			userMapper.updateScanCount(uid);
		}
		// 从数据库得到user对象
		User user = null;
		try {
			user = userMapper.findUserByUid(uid);
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		
		//设置获赞数，关注数，粉丝数
        Jedis jedis = jedisPool.getResource();
        user.setFollowCount((int)(long)jedis.scard(uid+":follow"));
        user.setFollowerCount((int)(long)jedis.scard(uid+":fans"));
        String likeCount = jedis.hget("vote",uid+"");
        if(likeCount==null){
            user.setLikeCount(0);
        }else {
            user.setLikeCount(Integer.valueOf(likeCount));
        }

        if(jedis!=null){
            jedisPool.returnResource(jedis);
        }
		return user;
	}

	/**
	 * 通过用户id查询用户信息
	 */
	@Override
	public User findUserById(int userId) throws Exception{
		User user = null;
		try {
			user = userMapper.findUserByUid(userId);
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		return user;
	}

	/**
	 * 编辑用户个人信息
	 */
	@Override
	public void updateProfile(User user) throws Exception {

		try {
			userMapper.updateUserProfile(user);
		} catch (Exception e) {
			log.error(ForumException.UPDATE_EXCEPTION, e);
			throw new Exception();
		}
	}

	/**
	 * 修改用户密码
	 */
	@Override
	public String updatePassword(int userId, String password,
			String newpassword, String repassword) throws Exception {
		if (userId == 0) {
			log.error(ForumException.NULL_VALUE_EXCEPTION);
			throw new Exception();
		}
		// 通过用户id查询用户密码
		String oldPassword = userMapper.findPasswordByUserId(userId);
		if (StringUtils.isBlank(password) || !password.equals(oldPassword)) {
			 return "原密码输入错误~";
		}
		
		if(newpassword.length()<6 ||newpassword.length()>20){
            return "新密码长度要在6~20之间~";
        }

        if(!newpassword.equals(repassword)){
            return "新密码两次输入不一致~";
        }
        
        try {
			userMapper.updatePassword(userId, newpassword);
		} catch (Exception e) {
			log.error(ForumException.UPDATE_EXCEPTION, e);
			throw new Exception();
		}
		return "ok";
	}

	
}
