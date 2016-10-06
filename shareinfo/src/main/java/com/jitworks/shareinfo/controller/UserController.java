/**
 * 
 */
package com.jitworks.shareinfo.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import com.jitworks.shareinfo.data.Blog;
import com.jitworks.shareinfo.data.QuizRecent;
import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.data.UserCustom;
import com.jitworks.shareinfo.exception.BadRequestException;
import com.jitworks.shareinfo.service.BlogService;
import com.jitworks.shareinfo.service.EmailService;
import com.jitworks.shareinfo.service.QuizService;
import com.jitworks.shareinfo.service.UserService;

/**
 * @author j.paidimarla
 * 
 */
@Controller
@SessionAttributes(types = User.class)
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private QuizService quizService;

	@Autowired
	private BlogService blogService;

	@Resource(name = "sessionRegistry")
	private SessionRegistryImpl sessionRegistry;
	
	@Autowired
	private EmailService emailService;

	private Logger logger = LoggerFactory.getLogger(UserController.class);

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

	
	@RequestMapping(value = { "/"}, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String forward() {
		logger.info("Land Page. . .");
		return "redirect:/welcome";
	}

	@RequestMapping(value = {"/welcome"}, method = RequestMethod.GET)
	public String getUsers() {
		logger.info("Landing Page. . .");
		//getUsersQuizsBlogs(model);
		return "/users/landingPage";
	}

	// Spring Security see this :
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {
		logger.info("Login Controller. . .");
		if (error != null) {
			model.addAttribute("error", "Invalid username and password!");
		}

		if (logout != null) {
			model.addAttribute("msg", "You've been logged out successfully.");
		}
		return "login";

	}

	/**
	 * @param model
	 */
	private void getUsersQuizsBlogs(Model model) {
		List<User> usersList = userService.getUsers();
		model.addAttribute("usersList", usersList);
		List<QuizRecent> recentQuizs = quizService.getRecentQuizs();
		List<Blog> recentBlogs = blogService.getRecentBlogs();
		model.addAttribute("recentQuizs", recentQuizs);
		model.addAttribute("recentBlogs", recentBlogs);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String getAllUsers(Model model) {
		logger.info("signup");
		// getUsersQuizsBlogs(model);
		UserCustom registerUser = new UserCustom();
		model.addAttribute("registerUser", registerUser);
		return "/users/signup";
	}

	
	@RequestMapping(value = "/signup/recoverPassword", method = RequestMethod.POST)
	public String emailPassword(Model model, @RequestParam(value = "emailId") String emailId){
		logger.info("User Email id : " + emailId.toLowerCase());
		
		UserCustom userCustom = userService.getUserByEmail(emailId.toLowerCase());
		if( userCustom != null){
			emailService.emailPassword(userCustom);
			model.addAttribute("message", " We Emailed your password successfully!");
		}else{
			model.addAttribute("messageError", " Cannot find your account, Please signup or contact administrator.!");
		}
		return "login";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String registerUser(Model model, @ModelAttribute("registerUser") UserCustom registerUser) {
		logger.info("new user" + registerUser.getFirstName());


		Boolean isExisting = userService.searchByUserName(registerUser.getUsername());

		if (!registerUser.getUsername().matches("^[a-zA-Z]{1,9}[\\.]?[a-zA-Z]{1,16}$")) {
			model.addAttribute("username_message", "Please enter valid Email ID. Only alphabets and '.' are allowed");
			return "/users/signup";
		} else if (isExisting) {
			model.addAttribute("username_message", "Username :  " + registerUser.getUsername() + " , Already existes");
			return "/users/signup";
		} else if (!registerUser.getFirstName().matches("^[a-zA-Z]{3,16}$")) {
			model.addAttribute("user_message", "First Name : only alphabets and minlingth 3 , max length 16 ");
			return "/users/signup";
		} else if (!registerUser.getLastName().matches("^[a-zA-Z]{1,16}$")) {
			model.addAttribute("user_message", "Last Name : only alphabets and minlingth 3 , max length 16 ");
			return "/users/signup";
		} else if (registerUser.getPassword() != registerUser.getPasswordUnEncrypted() && !registerUser.getPasswordUnEncrypted().matches("^.{8,}$")) {
			model.addAttribute("user_message", "Confirm Password Missmatched or min length should be 8 .");
			return "/users/signup";
		}

		userService.createUser(registerUser);
		// validateEmail(registerUser.getEmail());
		model.addAttribute("message", "Hello " + registerUser.getFirstName() + " Your Account is created successfully!");
		return "login";
	}

	@RequestMapping(value = "/users/{userId}/edit", method = RequestMethod.POST)
	String setUserStatus(@ModelAttribute("user") User user, @PathVariable("userId") int userId, @RequestParam(value = "file", required = false) MultipartFile multipartFile,
			@RequestParam(value = "status") String statusMessage) {
		logger.info("Update status for user ID : " + userId);
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}
		logger.info("file name: " + multipartFile.getOriginalFilename() + "  File name  : " + multipartFile.getName() + "File Size : " + multipartFile.getSize());

		// try {
		// if(! status.isEmpty()){
		// user.setStatusMessage(status);
		// }
		// //TODO:// Check the file size and format IE may not validate in UI.
		// && (multipartFile.getSize()/1024) < 1024
		// if(!multipartFile.isEmpty() && (multipartFile.getSize()/1024) < 1024
		// ){
		// user.setPhotoString("data:image/jpeg;base64,".concat(new
		// String(Base64.encode(multipartFile.getBytes()))));
		// }
		// this.userService.updateUser(user);
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }

		userService.updateUser(user, statusMessage, multipartFile);
		return "redirect:/users/" + userId + "/posts";

	}

	@RequestMapping(value = "/users/principals", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<String> getAuthenticatedUsers(@ModelAttribute("user") User user) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		List<Object> userObjects = sessionRegistry.getAllPrincipals();

		List<String> users = new ArrayList<String>();
		logger.info("Total logged-in users: " + sessionRegistry.getAllPrincipals().size());
		for (Object object : userObjects) {
			User userObj = (User) object;
			users.add(userObj.getFirstName());

			logger.info("Authenticated User: " + ((User) object).getFirstName());
		}

		return users;
	}
//TODO : Not used in view. check it
	@RequestMapping(value = "/signup/checkuser", method = RequestMethod.GET)
	public @ResponseBody
	Boolean checkUserAccount(@ModelAttribute("user") User user, Model model, @RequestParam(value = "username") String username) {
		logger.info("Check new User: " + username);
		Boolean isExisting = userService.searchByUserName(username);
		return isExisting;
	}

	@RequestMapping(value = "/users/chatRoom", method = RequestMethod.GET)
	public String getBlogs(@ModelAttribute("user") User user, Model model) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		return "/users/webRTCmultiConnect";
	}

	//Get user by Employer/userGroupID

	@RequestMapping(value = "/consultants", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<User> getUsersByUserGroupId(@ModelAttribute("user") User user) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		List<Object> userObjects = sessionRegistry.getAllPrincipals();
		List<User> consultants = null;
		consultants = userService.getUserByGroupCode(user.getUserGroup().getCode());
	/*	List<String> users = new ArrayList<String>();
		logger.info("Total logged-in users: " + sessionRegistry.getAllPrincipals().size());
		for (Object object : userObjects) {
			User userObj = (User) object;
			users.add(userObj.getFirstName());

			logger.info("Authenticated User: " + ((User) object).getFirstName());
		}
*/
		return consultants;

}
}