package com.rwb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.rwb.contanst.ForumException;
import com.rwb.dao.PostMapper;
import com.rwb.entity.PageBean;
import com.rwb.entity.Post;
import com.rwb.service.IPostService;

/**
 * 
* @ClassName: PostServiceImpl
* @Description: 帖子相关Service
* @author ruwenbo
* @date 2017年10月21日 下午9:24:13
*
 */
@Service("postService")
public class PostServiceImpl implements IPostService{

	private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * 分页查询帖子列表
	 */
	@Override
	public PageBean<Post> findPagingPost(int curPage) throws Exception{
		if (curPage == 0) {
			curPage = 1;
		}
		// 每页显示条数
		int pageSize = 8;
		// 查询帖子总条数
		long postCount = 0;
		try {
			postCount = postMapper.findPostCount();
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		// 总页数
		int allPage = 0;
		if (postCount <= pageSize) {
			allPage = 1;
		} else if (postCount%pageSize == 0) {
			allPage = (int)postCount/pageSize;
		} else {
			allPage = (int)postCount/pageSize + 1;
		}
		// 分页查询帖子
		List<Post> postList = null;
		try {
			postList = postMapper.findPagingPost(curPage, pageSize);
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		Jedis jedis = jedisPool.getResource();
        for(Post post : postList){
            post.setLikeCount((int)(long)jedis.scard(post.getPid()+":like"));
        }
		// 返回对象
		PageBean<Post> pageBean = new PageBean<>(allPage, curPage);
		pageBean.setList(postList);
		
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
		return pageBean;
	}

	/**
	 * 获取用户的帖子
	 */
	@Override
	public List<Post> findPostByUid(int sessionUid) throws Exception{
		List<Post> postList = null;
		try {
			postList = postMapper.findPostListByUid(sessionUid);
		} catch (Exception e) {
			log.error(ForumException.FIND_EXCEPTION, e);
			throw new Exception();
		}
		if (postList != null && postList.size() > 0) {
			return postList;
		}
		return new ArrayList<>();
	}

	
}
