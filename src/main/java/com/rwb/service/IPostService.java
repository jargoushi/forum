package com.rwb.service;

import java.util.List;

import com.rwb.entity.PageBean;
import com.rwb.entity.Post;

public interface IPostService {

	/**
	 * 
	* @Title: findPagingPost
	* @Description: 获取分页帖子列表，并返回当前页，每页显示条数
	* @param @param curPage
	* @param @return
	* @param @throws Exception    设定文件
	* @return PageBean<Post>    返回类型
	* @throws
	 */
	PageBean<Post> findPagingPost(int curPage) throws Exception;

	/**
	 * 
	* @Title: findPostByUid
	* @Description: 查询用户的所有帖子
	* @param @param sessionUid
	* @param @return    设定文件
	* @return List<Post>    返回类型
	* @throws
	 */
	List<Post> findPostByUid(int sessionUid) throws Exception;

}
