/**
 * 
 */
package com.jitworks.shareinfo.controller;

import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jitworks.shareinfo.data.Blog;
import com.jitworks.shareinfo.data.BlogComment;
import com.jitworks.shareinfo.data.BlogFile;
import com.jitworks.shareinfo.data.BlogSummary;
import com.jitworks.shareinfo.data.BlogTopic;
import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.exception.BadRequestException;
import com.jitworks.shareinfo.service.BlogService;
import com.jitworks.shareinfo.service.UserService;

/**
 * @author j.paidimarla
 * 
 */
@Controller
@RequestMapping("/users/{userId}/blogs")
@SessionAttributes(types = Blog.class)
public class BlogController {

	private Logger logger = LoggerFactory.getLogger(BlogController.class);

	@Autowired
	BlogService blogService;

	@Autowired
	private UserService userService;

	@ModelAttribute("user")
	public User getUser() {
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (user != null) {
				return user;
			}
		} catch (Exception e) {
			logger.debug("getUser failed: " + e.getMessage());
		}
		return null;
	}

	@ModelAttribute("blogTopics")
	public Collection<BlogTopic> populateCarriers() {
		return this.blogService.getBlogTopics();
	}

	@ModelAttribute("blogUser")
	public User populateBlogUser(@PathVariable("userId") int userId) {
		return this.userService.getUser(userId);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getBlogs(@ModelAttribute("user") User user, @PathVariable("userId") int userId, Model model) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}

		logger.debug("get Blog Topics for Authenticated userID : " + user.getId());
		logger.info("get Blog Topics for  user ID  : " + userId);
		// User blogUser = userService.getUser(userId);
		List<BlogSummary> blogs = blogService.getBlogsByTopic(userId, null, 0);
		model.addAttribute("blogs", blogs);
		// model.addAttribute("blogUser", blogUser);
		return "blogs/userBlogsView";
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String getAllBlogs(@ModelAttribute("user") User user, @PathVariable("userId") int userId, Model model) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}

		logger.debug("get Blog Topics of all users for Authenticated userID : " + user.getFirstName());
		boolean viewAllBlogs = true;
		List<BlogSummary> blogs = blogService.getBlogsByTopic(null, null, 0);
		model.addAttribute("blogs", blogs);
		model.addAttribute("viewAllBlogs", viewAllBlogs);
		return "blogs/userBlogsView";
	}

	@RequestMapping(value = "/category/{topicId}", method = RequestMethod.GET)
	public String getBlogsInCategory(@ModelAttribute("user") User user, @PathVariable("userId") Integer userId, @PathVariable("topicId") Integer topicId, Model model) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}

		logger.debug("get Blogs of a Topic  for Authenticated User : " + user.getFirstName() + " Blogs User ID : " + userId + " Topic ID : " + topicId);

		List<BlogSummary> blogs = blogService.getBlogsByTopic(userId, topicId, 0);

		model.addAttribute("blogs", blogs);
		model.addAttribute("currentTopicId", topicId);
		return "blogs/userBlogsView";
	}

	@RequestMapping(value = "/all/category/{topicId}", method = RequestMethod.GET)
	public String getAllBlogsInCategory(@ModelAttribute("user") User user, @PathVariable("userId") Integer userId, @PathVariable("topicId") Integer topicId, Model model) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}

		logger.debug("get All Blogs of a Topic  for Authenticated User : " + user.getFirstName() + " Blogs User ID : " + userId + " Topic ID : " + topicId);

		List<BlogSummary> blogs = blogService.getBlogsByTopic(null, topicId, 0);
		boolean viewAllBlogs = true;
		model.addAttribute("blogs", blogs);
		model.addAttribute("viewAllBlogs", viewAllBlogs);
		model.addAttribute("currentTopicId", topicId);
		
		
		return "blogs/userBlogsView";
	}

	@RequestMapping(value = "/load/{pageNo}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<BlogSummary> getBlogs(@ModelAttribute("user") User user, @PathVariable("userId") int userId, @PathVariable(value = "pageNo") int pageNo,
			@RequestParam(value = "topicId") Integer topicId, @RequestParam(value = "allUsers", required = false) Integer allUsers) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		logger.info(" Get blogs for user : " + user.getFirstName() + "  pageNo : " + pageNo + "  topicId : " + topicId + " allUsers : " + allUsers);
		Integer topic = (topicId == 0) ? null : topicId;
		List<BlogSummary> blogs = null;
		if (allUsers == -1) {
			blogs = blogService.getBlogsByTopic(userId, topic, pageNo);
		} else {
			blogs = blogService.getBlogsByTopic(null, topic, pageNo);
		}

		return blogs;
	}

	@RequestMapping(value = "/loadBlogComments/{blogId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<BlogComment> getBlogComments(@ModelAttribute("user") User user, @PathVariable("userId") int userId, @PathVariable(value = "blogId") int blogId) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		logger.info(" Get blog Comments for Auth user : " + user.getFirstName() + "blogId : " + blogId + "  userId : " + userId);

		List<BlogComment> blogComments = null;
		blogComments = blogService.getBlogComments(blogId);
		return blogComments;
	}
	
	@RequestMapping(value = "/loadBlogFiles/{blogId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<BlogFile> getBlogFiles(@ModelAttribute("user") User user, @PathVariable("userId") int userId, @PathVariable(value = "blogId") int blogId) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		logger.info(" Get blog Files for Auth user : " + user.getFirstName() + "blogId : " + blogId + "  userId : " + userId);

		List<BlogFile> blogFiles = null;
		blogFiles = blogService.getBlogFiles(blogId);
		return blogFiles;
	}
	
	

	// TODO: CREATE NEW BLOG cmnt

	@RequestMapping(value = "/newBlogComment/{blogId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	BlogComment createBlogComment(@ModelAttribute("user") User user, @PathVariable(value = "blogId") int blogId, @RequestBody String userComment) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		logger.info(" New blog Comments by Auth user : " + user.getFirstName() + "blogId : " + blogId + "userComment" + userComment);
		BlogComment blogComment = new BlogComment();
		blogComment.setBlog(blogService.getBlog(blogId));
		blogComment.setCreationTime(new DateTime());
		blogComment.setComment(userComment);
		blogComment.setUser(user);
		blogService.createBlogComment(blogComment);
		return blogComment;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String initNewForm(@ModelAttribute("user") User user, Model model) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}
		logger.debug("initCreationForm for blog");
		Blog blog = new Blog();
		model.addAttribute("blog", blog);
		return "/blogs/createBlogForm";
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String createBlog(@ModelAttribute("user") User user, @ModelAttribute("blog") Blog blog) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}
		logger.debug("create blog by Auth user : " + user.getFirstName());
		this.blogService.createBlog(blog, user);
		return "redirect:/users/{userId}/blogs";
	}

	@RequestMapping(value = "/{blogId}/edit", method = RequestMethod.GET)
	public String initEditForm(@ModelAttribute("user") User user, @PathVariable("userId") int userId, @PathVariable("blogId") int blogId, Model model) {
		if (user == null || user.getId() != userId) {
			logger.error("user is null");
			return "redirect:/users";
		}
		logger.debug("initEditForm");
		Blog blog = blogService.getBlog(blogId);
		if (blog == null) {
			return "redirect:/users/{userId}/blogs";
		}
		List<BlogFile> blogFiles = blogService.getBlogFiles(blogId);
 		model.addAttribute("blog", blog);
 		model.addAttribute("blogFiles", blogFiles);
		
		return "/blogs/editBlogForm";
	}

	@RequestMapping(value = "/{blogId}/edit", method = RequestMethod.POST)
	public String updateBlog(@ModelAttribute("user") User user, @PathVariable("userId") int userId, @ModelAttribute("blog") Blog blog) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}
		logger.debug("Update blog by user : " + user.getFirstName());
		blogService.updateBlog(blog);
		return "redirect:/users/{userId}/blogs";
	}

	@RequestMapping(value = "/{blogId}/delete", method = RequestMethod.DELETE)
	public @ResponseBody
	String deleteBlog(@ModelAttribute("user") User user, @PathVariable(value = "blogId") int blogId) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		logger.debug("delete blog ID: " + blogId);
		blogService.deleteBlog(blogId);

		return "deleted";
	}

	@RequestMapping(value = "/file/delete/{blogId}/{fileId}", method = RequestMethod.DELETE)
	public @ResponseBody
	String deleteBlogFile(@ModelAttribute("user") User user,  @PathVariable(value = "blogId") int blogId,  @PathVariable(value = "fileId") int fileId) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		logger.debug("delete blogFile ID: " + fileId +"Blog ID" + blogId);
		blogService.deleteBlogFile(blogId, fileId);

		return "deleted";
	}
	
}
