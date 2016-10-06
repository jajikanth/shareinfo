package com.jitworks.shareinfo.controller;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.jitworks.shareinfo.data.Post;
import com.jitworks.shareinfo.data.PostComment;
import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.exception.BadRequestException;
import com.jitworks.shareinfo.service.PostService;
import com.jitworks.shareinfo.service.UserService;

/**
 * @author kwanchul.k
 * 
 */
@Controller
@RequestMapping("/users/{userId}/posts")
@SessionAttributes(types = Post.class)
public class PostController {

	private Logger logger = LoggerFactory.getLogger(PostController.class);

	@Autowired
	private PostService postService;

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

	/*
	 * public User getUser(@PathVariable("userId") int userId) { try { User
	 * authUser = (User)
	 * SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	 * 
	 * logger.info("Populate details of userId : " + userId +
	 * " for Authenticated user:" + authUser.getFirstName()); User user =
	 * userService.getUser(userId); if (authUser.getUserGroup().getId() !=
	 * user.getUserGroup().getId()) { return null; }
	 * 
	 * return user; } catch (Exception e) {
	 * logger.error("get Authenticated user failed: " + e.getMessage()); return
	 * null; }
	 * 
	 * }
	 */

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getPosts(@ModelAttribute("user") User user, @PathVariable("userId") int userId, Model model) {

		logger.debug("getPosts for userID : " + userId);
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}
		List<Post> allPosts = postService.getPosts(userId);
		Set<Post> postContainer1 = new LinkedHashSet<Post>();
		Set<Post> postContainer2 = new LinkedHashSet<Post>();
		Set<Post> postContainer3 = new LinkedHashSet<Post>();

		int size = allPosts.size();
		for (int i = 0; i < size; ++i) {
			postContainer1.add(allPosts.get(i));
			if (i + 1 == size)
				break;
			postContainer2.add(allPosts.get(++i));
			if (i + 1 < size)
				postContainer3.add(allPosts.get(++i));
		}
User postUser = userService.getUser(userId);
		model.addAttribute("postUser", postUser);
		model.addAttribute("posts1", postContainer1);
		model.addAttribute("posts2", postContainer2);
		model.addAttribute("posts3", postContainer3);
		return "/posts/postsView";
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public @ResponseBody
	String createNewPost(@ModelAttribute("user") User user, @RequestBody String newPost) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		logger.debug(" Careate Post for user : " + user.getFirstName());
		Post post = new Post();
		post.setContent(newPost);
		post.setCreationTime(new DateTime());
		post.setUser(user);
		this.postService.createPost(post);
		return newPost;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	String deletePost(@ModelAttribute("user") User user, @PathVariable("userId") int userId, @RequestBody int postId) {
		if (user == null || user.getId() != userId) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		logger.debug(" Delete Post for user : " + user.getFirstName() + "PostId: " + postId);

		this.postService.deletePost(postId);
		return "Deleted";
	}

	@RequestMapping(value = "/newComment", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody
	PostComment newPostComment(@ModelAttribute("user") User user, @PathVariable("userId") int userId, @RequestBody Map<String, String> map) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		logger.info(" add post comment for PostId : " + map.get("postId") + "Comment name : " + map.get("comment"));
		Post post = postService.getPost(Integer.parseInt(map.get("postId")));
		PostComment postComment = new PostComment();
		postComment.setComment(map.get("comment"));
		postComment.setCreationTime(new DateTime());
		postComment.setUser(user);
		postComment.setPost(post);
		this.postService.createPostComment(postComment);
		return postComment;
	}

}
