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

import com.jitworks.shareinfo.dao.BlogDAO;
import com.jitworks.shareinfo.data.Blog;
import com.jitworks.shareinfo.data.BlogComment;
import com.jitworks.shareinfo.data.BlogFile;
import com.jitworks.shareinfo.data.BlogSummary;
import com.jitworks.shareinfo.data.BlogTopic;

/**
 * @author j.paidimarla
 * 
 */
@Repository
public class BlogDAOImpl implements BlogDAO {
	private static Logger logger = LoggerFactory.getLogger(QuizDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	public List<BlogTopic> getBlogTopics() {
		logger.debug("get Blog Topics");
		TypedQuery<BlogTopic> query;
		query = this.em.createQuery("SELECT b FROM BlogTopic b ORDER BY b.id DESC", BlogTopic.class);
		return query.getResultList();
	}

	/*
	 * public List<Blog> getBlogs(Integer blogUser) { logger.debug("get Blogs");
	 * TypedQuery<Blog> query; if (blogUser != null) { query =
	 * this.em.createQuery(
	 * "SELECT b FROM Blog b WHERE b.user.id = :blogUser AND b.deleted = false ORDER BY b.id DESC"
	 * , Blog.class); query.setParameter("blogUser", blogUser);
	 * 
	 * } else {
	 * 
	 * query = this.em.createQuery(
	 * "SELECT b FROM Blog b WHERE b.deleted = false ORDER BY b.id DESC",
	 * Blog.class);
	 * 
	 * }
	 * 
	 * return query.setMaxResults(4).getResultList(); }
	 */

	public void createBlog(Blog blog) {
		logger.debug("Create Blog");
		em.persist(blog);
	}

	public void createBlogComment(BlogComment blogComment) {
		logger.debug("Create Blog Comment");
		em.persist(blogComment);

	}

	public void updateBlog(Blog blog, BlogFile newBlogFile) {
		logger.debug("Update Blog for blog id : " + blog.getId());
		em.merge(blog);
		if (newBlogFile != null) {
			logger.debug("Create new File");
			em.persist(newBlogFile);
		}
	}

	public Blog getBlog(int blogId) {
		TypedQuery<Blog> query;
		query = em.createQuery("SELECT b FROM Blog b WHERE b.id = :blogId AND b.deleted = false", Blog.class);
		query.setParameter("blogId", blogId);

		Blog blog = null;
		try {
			blog = query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("no Blog with id :  " + blogId);
		}

		return blog;
	}

	public List<BlogSummary> getBlogsByTopic(Integer userId, Integer topicId, int pageNo) {
		logger.debug("get Blog Topics");
		int recordsPerPage = 4;
		int offset = (pageNo * recordsPerPage);
		TypedQuery<BlogSummary> query;
		if ((null != userId) && (null != topicId)) {
			query = this.em.createQuery("SELECT b FROM BlogSummary b WHERE b.user.id = :userId AND b.blogTopic.id = :topicId AND b.deleted = false ORDER BY b.id DESC",
					BlogSummary.class);
			query.setParameter("topicId", topicId);
			query.setParameter("userId", userId);
		} else if ((null != userId) && (null == topicId)) {
			query = this.em.createQuery("SELECT b FROM BlogSummary b WHERE b.user.id = :userId AND b.deleted = false ORDER BY b.id DESC", BlogSummary.class);
			query.setParameter("userId", userId);
		} else if ((null == userId) && (null != topicId)) {
			query = this.em.createQuery("SELECT b FROM BlogSummary b WHERE b.blogTopic.id = :topicId AND b.deleted = false ORDER BY b.id DESC", BlogSummary.class);
			query.setParameter("topicId", topicId);
		} else {
			query = this.em.createQuery("SELECT b FROM BlogSummary b WHERE b.deleted = false ORDER BY b.id DESC", BlogSummary.class);
		}

		return query.setFirstResult(offset).setMaxResults(recordsPerPage).getResultList();
	}

	public List<BlogComment> getBlogComments(int blogId) {

		TypedQuery<BlogComment> query;
		query = this.em.createQuery("SELECT b FROM BlogComment b WHERE b.blog.id = :blogId AND b.deleted = false ORDER BY b.id ASC", BlogComment.class);
		query.setParameter("blogId", blogId);

		return query.getResultList();
	}

	public List<Blog> getRecentBlogs() {
		TypedQuery<Blog> query = em.createQuery("SELECT b FROM Blog b WHERE b.deleted = false ORDER BY b.id DESC ", Blog.class);
		return query.setMaxResults(7).getResultList();
	}

	public void deleteBlog(int blogId) {
		logger.debug("Delete Blog for Blog ID : " + blogId);
		Query query1 = em.createQuery("UPDATE Blog b SET b.deleted = true WHERE b.id = :blogId");
		query1.setParameter("blogId", blogId);
		query1.executeUpdate();

		logger.debug("Delete BlogComments for Blog ID : " + blogId);
		Query query2 = em.createQuery("UPDATE BlogComment b SET b.deleted = true WHERE b.blog.id = :blogId");
		query2.setParameter("blogId", blogId);
		query2.executeUpdate();
		
		logger.debug("Delete BlogFile for Blog ID : " + blogId);
		Query query3 = em.createQuery("UPDATE BlogFile f SET f.deleted = true WHERE f.blog.id = :blogId");
		query3.setParameter("blogId", blogId);
		query3.executeUpdate();

	}

	public List<BlogFile> getBlogFiles(int blogId) {
		TypedQuery<BlogFile> query;
		query = this.em.createQuery("SELECT f FROM BlogFile f WHERE f.blog.id = :blogId AND f.deleted = false ORDER BY f.id ASC", BlogFile.class);
		query.setParameter("blogId", blogId);
		return query.getResultList();
	}

	public BlogFile getBlogFileById(int fileId) {
		TypedQuery<BlogFile> query;
		query = em.createQuery("SELECT f FROM BlogFile f WHERE f.id = :fileId AND f.deleted = false", BlogFile.class);
		query.setParameter("fileId", fileId);

		BlogFile blogFile = null;
		try {
			blogFile = query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("no BlogFiles with fileId :  " + fileId);
		}
		return blogFile;
	}


	public void deleteBlogFile(int fileId) {
		logger.debug("Delete BlogFile for File ID : " + fileId);
		Query query = em.createQuery("UPDATE BlogFile f SET f.deleted = true WHERE f.id = :fileId");
		query.setParameter("fileId", fileId);
		query.executeUpdate();
	}

}
