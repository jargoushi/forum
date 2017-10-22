package com.rwb.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rwb.entity.Post;
import com.rwb.entity.User;
import com.rwb.service.IPostService;
import com.rwb.service.IUserService;

/**
 * 
* @ClassName: UserController
* @Description: 个人信息Controller
* @author ruwenbo
* @date 2017年10月22日 下午1:34:03
*
 */
@Controller
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IPostService postService;
	
	/**
	 * 
	* @Title: toMyProfile
	* @Description: 查看我的个人主页
	* @param @param session
	* @param @param model
	* @param @return    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping("/toMyProfile.do")
	public String toMyProfile(HttpSession session, Model model) throws Exception{
		int sessionUid = (int) session.getAttribute("uid");
		User user = userService.getProfile(sessionUid, sessionUid);
		List<Post> postList = postService.findPostByUid(sessionUid);
		model.addAttribute("user",user);
        model.addAttribute("postList",postList);
        return "myProfile";
	}
	
	/**
	 * 
	* @Title: toEditProfile
	* @Description: 跳转至编辑个人主页页面
	* @param @return
	* @param @throws Exception    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping("/toEditProfile.do")
	public String toEditProfile(HttpSession session, Model model) throws Exception{
		// 获取session中的用户id
		int userId = (int) session.getAttribute("uid");
		// 通过主键id查询用户信息
		User user = userService.findUserById(userId);
		// 向页面回显用户信息
		model.addAttribute("user", user);
		return "editProfile";
	}
	
	/**
	 * 
	* @Title: editProfile
	* @Description: 编辑个人信息
	* @param @param user
	* @param @return
	* @param @throws Exception    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping(value = "/editProfile.do", method = RequestMethod.POST)
	public String editProfile(User user) throws Exception{
		// 编辑个人信息
		userService.updateProfile(user);
		return "redirect:/toMyProfile.do";
	}
	
	/**
	 * 
	* @Title: updatePassword
	* @Description: 修改用户密码
	* @param @param session
	* @param @param password
	* @param @param newpassword
	* @param @param repassword
	* @param @param model
	* @param @return
	* @param @throws Exception    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping("/updatePassword.do")
	public String updatePassword(HttpSession session, String password, String newpassword,
			String repassword, Model model) throws Exception {
		int userId = (int) session.getAttribute("uid");
		String result = userService.updatePassword(userId, password, newpassword, repassword);
		if ("ok".equals(result)) {
			return "redirect:logout.do";
        }else {
            model.addAttribute("passwordError",result);
            return "editProfile";
        }
	}
}
