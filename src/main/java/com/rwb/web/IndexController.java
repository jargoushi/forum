package com.rwb.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rwb.entity.PageBean;
import com.rwb.entity.Post;
import com.rwb.entity.User;
import com.rwb.service.IPostService;
import com.rwb.service.IUserService;

/**
 * 
* @ClassName: IndexController
* @Description: 跳转首页Controller
* @author ruwenbo
* @date 2017年10月21日 下午8:53:16
*
 */
@Controller
public class IndexController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IPostService postService;

	/**
	 * 
	* @Title: showIndex
	* @Description: 跳转首页并携带数据
	* @param @param request
	* @param @param model
	* @param @return
	* @param @throws Exception    设定文件
	* @return String    返回类型
	* @throws
	 */
	@RequestMapping("/index.do")
	public String showIndex(HttpServletRequest request, Model model) throws Exception {
		// 获取客户端请求的完整路径
		String url = request.getRequestURL().toString();
		// 获取项目的根目录
		String contextPath = request.getContextPath();
		// 获取访问者的ip地址
		String ip = request.getRemoteAddr();
		// 记录来访者信息
		userService.record(url, contextPath, ip);
		// 分页查询所有帖子
		PageBean<Post> pageBean = postService.findPagingPost(1);
		// 获取最近加入的用户
		List<User> userList = userService.findUsersByTime();
		// 获取活跃的用户
		List<User> hotUserList = userService.findUsersByHot();
		// 向页面返回数据
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("userList", userList);
		model.addAttribute("hotUserList", hotUserList);
		
		return "index";
	}
}
