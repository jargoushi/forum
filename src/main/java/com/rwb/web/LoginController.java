package com.rwb.web;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rwb.entity.User;
import com.rwb.service.ILoginService;
import com.rwb.util.MyConstant;

/**
 * 
* @ClassName: LoginController
* @Description: 登录注册Controller
* @author ruwenbo
* @date 2017年10月21日 下午10:25:49
*
 */
@Controller
public class LoginController {

	@Autowired
	private ILoginService loginService;
	
	/**
	 * 
	* @Title: toLogin
	* @Description: 跳转登录页面
	* @param @return    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping("/toLogin.do")
	public String toLogin() {
		return "login";
	}
	
	/**
	 * 
	* @Title: register
	* @Description: 用户注册并发送邮件
	* @param @param user
	* @param @param repassword
	* @param @param model
	* @param @return
	* @param @throws Exception    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	public String register(User user, String repassword, Model model) throws Exception{
		if (StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getEmail()) ||
				user.getPassword().length() <=6 || repassword.length() <=6) {
			model.addAttribute("error", "输入参数有误，请重新输入哟");
			return "login";
		}
		// 注册
		String result = loginService.register(user, repassword);
		if ("ok".equals(result)) {
			model.addAttribute("info","系统已经向你的邮箱发送了一封邮件哦，验证后就可以登录啦~");
			return "prompt/promptInfo";
		} else {
			model.addAttribute("register","yes");
            model.addAttribute("email",user.getEmail());
            model.addAttribute("error",result);
            return "login";
		}
	}
	
	/**
	 * 
	* @Title: activate
	* @Description: 激活注册用户信息
	* @param @param code
	* @param @param model
	* @param @return
	* @param @throws Exception    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping(value = "/activate.do")
	public String activate(String code, Model model) throws Exception{
		if (StringUtils.isBlank(code)) {
			model.addAttribute("info","注册码都被你搞丢了,一边玩去~");
			return "prompt/promptInfo";
		}
		// 激活
		loginService.activate(code);
	    model.addAttribute("info","您的账户已经激活成功，可以去登录啦~");
		return "prompt/promptInfo";
	}
	
	/**
	 * 
	* @Title: login
	* @Description: 登录
	* @param @param user
	* @param @param model
	* @param @param session
	* @param @return
	* @param @throws Exception    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String login(User user, Model model, HttpSession session) throws Exception {
		// 校验邮箱密码是否激活
		Map<String, Object> resultMap = loginService.login(user);
		if ("yes".equals(resultMap.get("status"))) {
			session.setAttribute("uid", resultMap.get("uid"));
			session.setAttribute("headUrl", resultMap.get("headUrl"));
			return "redirect:/toMyProfile.do";
		} else {
			model.addAttribute("error", resultMap.get("error"));
			model.addAttribute("email",user.getEmail());
			return "login";
		}
	}
	
	/**
	 * 
	* @Title: logout
	* @Description: 注销
	* @param @param session
	* @param @return    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {
		// 清空session
		session.removeAttribute("uid");
		// 重定向到论坛首页
		return "redirect:/index.do";
	}
}
