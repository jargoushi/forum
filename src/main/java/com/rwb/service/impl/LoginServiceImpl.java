package com.rwb.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.rwb.asyc.MailTask;
import com.rwb.contanst.ForumException;
import com.rwb.dao.UserMapper;
import com.rwb.entity.User;
import com.rwb.service.ILoginService;
import com.rwb.util.MyConstant;
import com.rwb.util.MyUtil;

/**
 * 
* @ClassName: LoginServiceImpl
* @Description: 注册Service
* @author ruwenbo
* @date 2017年10月21日 下午10:40:34
*
 */
@Service("loginService")
public class LoginServiceImpl implements ILoginService{
	
	private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
	private TaskExecutor taskExecutor;

	@Override
	public String register(User user, String repassword) throws Exception{
		
		//校验邮箱格式
        Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$");
        Matcher m = p.matcher(user.getEmail());
        if(!m.matches()){
            return "邮箱格式有问题啊~";
        }

        //校验密码长度
        p = Pattern.compile("/^[a-zA-Z0-9]{6,10}$/");
        m = p.matcher(user.getPassword());
        if(m.matches()){
            return "密码长度要在6到20之间~";
        }

        //检查密码
        if(!user.getPassword().equals(repassword)){
            return "两次密码输入不一致~";
        }
        
        //检查邮箱是否被注册
		int emailCount = 0;
		try {
			emailCount = userMapper.findEmailCount(user.getEmail());
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		
		if (emailCount >= 1) {
			return "该邮箱已被注册~";
		}
		
		// 插入user，设置为未激活
		user.setActived(0);
		String activeCode = MyUtil.createActivateCode();
		user.setActivateCode(activeCode);
        user.setJoinTime(MyUtil.formatDate(new Date()));
        user.setUsername("DF"+new Random().nextInt(10000)+"号");
        user.setHeadUrl(MyConstant.QINIU_IMAGE_URL + "head.jpg");
        
        //发送邮件
        taskExecutor.execute(new MailTask(activeCode,user.getEmail(),javaMailSender,1));
        
        // 保存注册用户信息
        try {
			userMapper.insertUser(user);
		} catch (Exception e) {
			log.error(ForumException.INSERT_EXCEPTION, e);
			throw new Exception();
		}
		
		return "ok";
	}

	/**
	 * 激活注册用户信息
	 */
	@Override
	public void activate(String code) throws Exception{
		
		try {
			userMapper.updataActivate(code);
		} catch (Exception e) {
			log.error(ForumException.UPDATE_EXCEPTION, e);
			throw new Exception();
		}
	}

	/**
	 * 校验邮箱密码及是否激活
	 */
	@Override
	public Map<String, Object> login(User user) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 通过邮箱与密码查询用户id
		Integer uid = null;
		try {
			uid = userMapper.findUidByEmailAndPassword(user);
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		if (null == uid) {
			map.put("error", "邮箱或密码不正确");
			map.put("status", "no");
			return map;
		}
		
		// 通过用户id查询用户是否激活
		int activated = 0;
		try {
			activated = userMapper.findActivatedByUid(uid);
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		if (activated == 0) {
			map.put("status", "no");
			map.put("error", "您还没有激活账户哦，请前往邮箱激活~");
			return map;
		}
		
		// 通过用户主键查询headUrl
		String headUrl = "";
		try {
			headUrl = userMapper.findHeadUrlByUid(uid);
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		
		map.put("status", "yes");
		map.put("uid",uid);
        map.put("headUrl",headUrl);
		return map;
	}

}
