package com.jitworks.shareinfo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jitworks.shareinfo.dao.PostDAO;
import com.jitworks.shareinfo.data.Post;
import com.jitworks.shareinfo.data.PostComment;
import com.jitworks.shareinfo.service.PostService;


@Service
public class PostServiceImpl implements PostService {

	private Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	@Autowired
	private PostDAO postDAO;

	@Transactional
	public void createPost(Post post) throws DataAccessException {
		postDAO.createPost(post);
	}


	@Transactional(readOnly = true)
	public List<Post> getPosts(int userId)	throws DataAccessException {
		logger.info("getPosts");
		return postDAO.getPosts(userId);
	}

	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.service.PostService#deletePost(java.lang.Integer)
	 */
	@Transactional
	public void deletePost(Integer postId) throws DataAccessException {
		// TODO Auto-generated method stub
		postDAO.updateDeleted(postId);
	}


	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.service.PostService#getPost(int)
	 */
	@Transactional(readOnly = true)
	public Post getPost(int postId) {
		// TODO Auto-generated method stub
		return postDAO.getPost(postId);
	}


	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.service.PostService#createPostComment(com.jitworks.shareinfo.data.PostComment)
	 */
	@Transactional
	public void createPostComment(PostComment postComment) {
		// TODO Auto-generated method stub
		postDAO.createPostComment(postComment);
	}


}
