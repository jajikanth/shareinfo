package com.jitworks.shareinfo.dao;

import java.util.List;

import com.jitworks.shareinfo.data.Post;
import com.jitworks.shareinfo.data.PostComment;


public interface PostDAO {

	
	public void createPost(Post post);
	
	public List<Post> getPosts(int userId);

	/**
	 * @param postId
	 */
	public void updateDeleted(Integer postId);

	/**
	 * @param postId
	 * @return
	 */
	public Post getPost(int postId);

	/**
	 * @param postComment
	 */
	public void createPostComment(PostComment postComment);
	

	
}