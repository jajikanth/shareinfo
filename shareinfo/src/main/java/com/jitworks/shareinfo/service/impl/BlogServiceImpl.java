/**
 * 
 */
package com.jitworks.shareinfo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jitworks.shareinfo.config.ApplicationConfig;
import com.jitworks.shareinfo.dao.BlogDAO;
import com.jitworks.shareinfo.dao.EmailDAO;
import com.jitworks.shareinfo.data.Blog;
import com.jitworks.shareinfo.data.BlogComment;
import com.jitworks.shareinfo.data.BlogFile;
import com.jitworks.shareinfo.data.BlogSummary;
import com.jitworks.shareinfo.data.BlogTopic;
import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.exception.BadRequestException;
import com.jitworks.shareinfo.service.BlogService;
import com.jitworks.shareinfo.service.EmailService;
import com.jitworks.shareinfo.service.UserService;

/**
 * @author j.paidimarla
 * 
 */

@Service
public class BlogServiceImpl implements BlogService {

	private Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

	@Autowired
	private BlogDAO blogDAO;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailDAO emailDAO;

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationConfig applicationConfig;

	@Transactional(readOnly = true)
	public List<BlogTopic> getBlogTopics() {

		return blogDAO.getBlogTopics();
	}

/*	
	  @Transactional(readOnly = true)
	 public List<Blog> getBlogs(Integer blogUser) {
	  return blogDAO.getBlogs(blogUser); }
	*/ 

	@Transactional
	public void createBlog(Blog blog, User user) {
		blog.setCreationTime(new DateTime());
		blog.setUser(user);
		if (blog.getContent().contains("<p><iframe") || blog.getContent().contains("www.youtube.com")) {
			logger.info("New Blog: contains external document or youtube videos");
			blog.setContainsVideo(true);
		}

		MultipartFile multipartFile = blog.getFile();

		if (!multipartFile.isEmpty()) {
			List<BlogFile> blogFiles = new ArrayList<BlogFile>();

			BlogFile blogFile = createNewFileDirectory(user, blog, multipartFile);
			blogFiles.add(blogFile);
			blog.setExternalDocument(true);
			blog.setBlogFiles(blogFiles);
		}

		blogDAO.createBlog(blog);
		logger.info("User requested to sent email : " + blog.isSendEmail());
		if (blog.isSendEmail()) {

			// Sent Async email
			logger.info("Before Async email call for new blog<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			emailService.sendNewBlogMails(blog);
			logger.info("After Email Call>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		}
	}

	/**
	 * @param user
	 * @param blog
	 * @param multipartFile
	 * @return
	 */

	private BlogFile createNewFileDirectory(User user, Blog blog, MultipartFile multipartFile) {
		String rootPath = applicationConfig.getFileBasePath();
		String filePath = "BlogFiles/" + user.getFirstName() + "/" + blog.getTitle() + "/" + System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
		logger.debug("Blog file : " + rootPath + filePath);
		File dest = new File(rootPath + filePath);
		try {
			if (dest.mkdirs()) {
				multipartFile.transferTo(dest);
			} else {
				logger.error("Blog : mkdirs failed: " + dest);
			}
		} catch (IllegalStateException e) {
			logger.error("Blog : File upload failed", e);
			throw new BadRequestException();
		} catch (IOException e) {
			logger.error("Blog : File upload failed", e);
			throw new BadRequestException();
		}

		BlogFile blogFile = new BlogFile();
		blogFile.setCreationTime(new DateTime());
		blogFile.setFilePath(filePath);
		blogFile.setName(multipartFile.getOriginalFilename());

		blogFile.setBlog(blog);
		blogFile.setSize(multipartFile.getSize());
		blogFile.setContentType(multipartFile.getContentType());
		return blogFile;
	}

	@Transactional(readOnly = true)
	public Blog getBlog(int blogId) {
		return blogDAO.getBlog(blogId);
	}

	@Transactional
	public void updateBlog(Blog blog) {

		if (blog.getContent().contains("<p><iframe") || blog.getContent().contains("www.youtube.com")) {
			logger.info("Edit Blog: contains external document or youtube videos");
			blog.setContainsVideo(true);
		} else {
			blog.setContainsVideo(false);
		}

		List<BlogFile> blogFiles = blogDAO.getBlogFiles(blog.getId());
		if (blogFiles.isEmpty()) {
			blog.setExternalDocument(false);
		}
		MultipartFile multipartFile = blog.getFile();
		BlogFile newBlogFile = null;
		if (!multipartFile.isEmpty()) {
			logger.info("Edit Blog: creating file. .");
			newBlogFile = createNewFileDirectory(blog.getUser(), blog, multipartFile);
			blog.setExternalDocument(true);
		}

		blogDAO.updateBlog(blog, newBlogFile);
		logger.info("User requested to sent email : " + blog.isSendEmail());
		if (blog.isSendEmail()) {
			logger.info("Before Asyn email call for update Blog<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			emailService.sendNewBlogMails(blog);
			logger.info("After Email Call>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	@Transactional
	public void deleteBlog(int blogId) {

		List<BlogFile> blogFiles = blogDAO.getBlogFiles(blogId);
		for (BlogFile blogFile : blogFiles) {
			deleteFile(blogFile.getFilePath());
		}

		blogDAO.deleteBlog(blogId);
	}

	@Transactional(readOnly = true)
	public List<BlogSummary> getBlogsByTopic(Integer userId, Integer topicId, int pageNo) {
		return blogDAO.getBlogsByTopic(userId, topicId, pageNo);
	}

	@Transactional(readOnly = true)
	public List<BlogComment> getBlogComments(int blogId) {
		return blogDAO.getBlogComments(blogId);
	}

	@Transactional
	public void createBlogComment(BlogComment blogComment) {
		blogDAO.createBlogComment(blogComment);

	}

	@Transactional(readOnly = true)
	public List<Blog> getRecentBlogs() {
		return blogDAO.getRecentBlogs();
	}

	@Transactional(readOnly = true)
	public List<BlogFile> getBlogFiles(int blogId) {
		return blogDAO.getBlogFiles(blogId);
	}

	@Transactional(readOnly = true)
	public BlogFile getBlogFileById(int fileId) {
		return blogDAO.getBlogFileById(fileId);
	}

	@Transactional
	public void deleteBlogFile(int blogId, int fileId) {
		BlogFile blogFile = blogDAO.getBlogFileById(fileId);

		deleteFile(blogFile.getFilePath());
		blogDAO.deleteBlogFile(fileId);

		List<BlogFile> blogFiles = blogDAO.getBlogFiles(blogId);
		if (blogFiles.isEmpty()) {
			Blog blog = blogDAO.getBlog(blogId);
			blog.setExternalDocument(false);
			blogDAO.updateBlog(blog, null);
		}
	}

	private void deleteFile(String filePath) {
		if (FileUtils.deleteQuietly(new File(applicationConfig.getFileBasePath() + filePath))) {
			logger.debug("file[" + filePath + "] deleted.");
		} else {
			logger.error("file[" + filePath + "] failed to delete.");
		}
	}

}
