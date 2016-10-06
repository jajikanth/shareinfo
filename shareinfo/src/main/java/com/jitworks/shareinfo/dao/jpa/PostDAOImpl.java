/**
 * 
 */
package com.jitworks.shareinfo.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jitworks.shareinfo.dao.PostDAO;
import com.jitworks.shareinfo.data.Post;
import com.jitworks.shareinfo.data.PostComment;

@Repository
public class PostDAOImpl implements PostDAO {

	private static Logger logger = LoggerFactory.getLogger(PostDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	public void createPost(Post post) {
		logger.info("createPost");
		this.em.persist(post);
	}

	public List<Post> getPosts(int userId) {
		logger.debug("getPosts");
		TypedQuery<Post> query;

		query = this.em.createQuery("SELECT post FROM Post post WHERE post.deleted = false AND post.user.id = :userId ORDER BY post.id DESC", Post.class);

		query.setParameter("userId", userId);

		return query.getResultList();
	}

	public void updateDeleted(Integer postId) {

		logger.debug("Delete Post.");
		Query query = em.createQuery("UPDATE Post p SET p.deleted = true WHERE p.id = :postId");
		query.setParameter("postId", postId);
		query.executeUpdate();
	}

	public Post getPost(int postId) {

		TypedQuery<Post> query;
		query = em.createQuery("SELECT p FROM Post p WHERE p.id = :postId AND p.deleted = false", Post.class);
		query.setParameter("postId", postId);

		Post post = null;
		try {
			post = query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("no post with Id " + postId);
		}

		return post;
	}

	public void createPostComment(PostComment postComment) {
		logger.info("createPostComment");
		this.em.persist(postComment);

	}

}
