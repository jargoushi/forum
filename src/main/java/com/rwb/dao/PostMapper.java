package com.rwb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rwb.entity.Post;

public interface PostMapper {

	public long findPostCount();

	public List<Post> findPagingPost(@Param("offset") int curPage,@Param("pageSize") int pageSize);

	public List<Post> findPostListByUid(int sessionUid);

}
