/**
 * 
 */
package com.jitworks.shareinfo.controller;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jitworks.shareinfo.data.Folder;
import com.jitworks.shareinfo.exception.BadRequestException;
import com.jitworks.shareinfo.service.FileAppService;
import com.jitworks.shareinfo.util.UserContextUtils;

/**
 * @author j.paidimarla
 *
 */
@Controller
@RequestMapping("/fileManager")
//@SessionAttributes(types = Filemanager.class)
public class FileAppController {
	private static Logger logger = LoggerFactory.getLogger(FileAppController.class);

	@Autowired
	FileAppService fileAppService;
	
/*	@ModelAttribute("blogTopics")
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

	*/

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getAllBlogs(Model model) {
		
		logger.info("Load File App");
		
//		List<BlogSummary> blogs = blogService.getBlogsByTopic(null, null, 0);
	//	model.addAttribute("blogs", blogs);
//		model.addAttribute("viewAllBlogs", viewAllBlogs);
		return "fileapp/index";
	}
	
	@RequestMapping(value = "/{userId}/folders", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<Folder> getFoldersByUserId(@PathVariable("userId") int userId) {

		if (UserContextUtils.getUserId() != userId) {
			logger.error("Folders request by Not authenticated user. ");
			throw new BadRequestException();
		}
		// List<Object> userObjects = sessionRegistry.getAllPrincipals();
		List<Folder> folders = null;
		folders = fileAppService.getFoldersByUserId(userId);

		return folders;

}
}
