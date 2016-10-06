/**
 * 
 */
package com.jitworks.shareinfo.dao;

import java.util.List;

import com.jitworks.shareinfo.data.Blog;
import com.jitworks.shareinfo.data.BlogComment;
import com.jitworks.shareinfo.data.BlogFile;
import com.jitworks.shareinfo.data.BlogSummary;
import com.jitworks.shareinfo.data.BlogTopic;

/**
 * @author j.paidimarla
 * 
 */
public interface BlogDAO {

	List<BlogTopic> getBlogTopics();

	//List<Blog> getBlogs(Integer blogUser);

	void createBlog(Blog blog);

	Blog getBlog(int blogId);

	void updateBlog(Blog blog, BlogFile newBlogFile);

	List<BlogSummary> getBlogsByTopic(Integer userId, Integer topicId, int pageNo);

	List<BlogComment> getBlogComments(int blogId);

	void createBlogComment(BlogComment blogComment);

	List<Blog> getRecentBlogs();

	void deleteBlog(int blogId);

	List<BlogFile> getBlogFiles(int blogId);

	BlogFile getBlogFileById(int fileId);

	void deleteBlogFile(int fileId);


}
