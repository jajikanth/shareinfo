/**
 * 
 */
package com.jitworks.shareinfo.service;

import java.util.List;

import com.jitworks.shareinfo.data.Blog;
import com.jitworks.shareinfo.data.BlogComment;
import com.jitworks.shareinfo.data.BlogFile;
import com.jitworks.shareinfo.data.BlogSummary;
import com.jitworks.shareinfo.data.BlogTopic;
import com.jitworks.shareinfo.data.User;

/**
 * @author j.paidimarla
 * 
 */
public interface BlogService {

	List<BlogTopic> getBlogTopics();

	//List<Blog> getBlogs(Integer blogUser);

	void createBlog(Blog blog, User user);

	Blog getBlog(int blogId);

	void updateBlog(Blog blog);

	void deleteBlog(int blogId);

	List<BlogSummary> getBlogsByTopic(Integer userId, Integer topicId, int pageNo);

	List<BlogComment> getBlogComments(int blogId);

	void createBlogComment(BlogComment blogComment);

	List<Blog> getRecentBlogs();

	List<BlogFile> getBlogFiles(int blogId);

	BlogFile getBlogFileById(int fileId);

	void deleteBlogFile(int blogId, int fileId);

}
