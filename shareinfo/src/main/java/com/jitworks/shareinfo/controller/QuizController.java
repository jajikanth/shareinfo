/**
 * 
 */
package com.jitworks.shareinfo.controller;

import java.util.ArrayList;
import java.util.List;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jitworks.shareinfo.data.Quiz;
import com.jitworks.shareinfo.data.QuizFile;
import com.jitworks.shareinfo.data.QuizForm;
import com.jitworks.shareinfo.data.QuizTopic;
import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.exception.BadRequestException;
import com.jitworks.shareinfo.service.QuizService;


/**
 * @author j.paidimarla
 * 
 */

@Controller
@RequestMapping("/quiz")
@SessionAttributes(types = QuizForm.class)
public class QuizController {

	private Logger logger = LoggerFactory.getLogger(QuizController.class);

	@Autowired
	QuizService quizService;

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

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getQuizTopics(@ModelAttribute("user") User user, Model model, @RequestParam(value = "delete", defaultValue = "false", required = false) Boolean delete) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}

		logger.debug("getQuiz Topics for userID : " + user.getId());

		List<QuizTopic> quizTopics = quizService.getQuizTopics();
		
	if(!quizTopics.isEmpty()){
		
		QuizTopic selectedQuizTopic = quizTopics.get(0);
		List<QuizFile> quizFiles = quizService.getQuizFiles(selectedQuizTopic.getId());
		List<Quiz> quizDetails = quizService.getQuizs(selectedQuizTopic.getId());
		model.addAttribute("selectedQuizTopic", selectedQuizTopic);
		model.addAttribute("quizFiles", quizFiles);
		model.addAttribute("quizDetails", quizDetails);
		
	}
		
		
		
		if (delete) {
			model.addAttribute("Deleted", delete);
		}
		model.addAttribute("quizTopics", quizTopics);
		return "quiz/quizView";
	}

	@RequestMapping(value = "/{quizTopicId}", method = RequestMethod.GET)
	public String getQuizDetails(@ModelAttribute("user") User user, @PathVariable("quizTopicId") int quizTopicId, Model model) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}

		logger.debug("getQuizs for userID : " + user.getId() + " Topic ID : " + quizTopicId);

		List<QuizTopic> quizTopics = quizService.getQuizTopics();

		QuizTopic selectedQuizTopic = quizService.getQuizTopic(quizTopicId);

		List<QuizFile> quizFiles = quizService.getQuizFiles(quizTopicId);
		// TODO: Change implementation if req
		List<Quiz> quizDetails = quizService.getQuizs(quizTopicId);

		model.addAttribute("quizTopics", quizTopics);
		model.addAttribute("quizFiles", quizFiles);
		model.addAttribute("selectedQuizTopic", selectedQuizTopic);
		model.addAttribute("quizDetails", quizDetails);

		return "quiz/quizView";
	}

	@RequestMapping(value = "/{quizTopicId}/delete", method = RequestMethod.GET)
	public String deleteQuizDetails(@ModelAttribute("user") User user, @PathVariable("quizTopicId") int quizTopicId, Model model) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}

		logger.debug("delete Quiz by userID : " + user.getId() + " Topic ID : " + quizTopicId);
		quizService.deleteQuiz(quizTopicId);
		boolean delete = true;
		model.addAttribute("delete", delete);
		return "redirect:/quiz";
	}

	@RequestMapping(value = "/file/delete/{fileId}", method = RequestMethod.DELETE)
	public @ResponseBody
	String deleteBlog(@ModelAttribute("user") User user, @PathVariable(value = "fileId") int fileId) {
		if (user == null) {
			logger.error("user is null");
			throw new BadRequestException();
		}
		logger.debug("delete blog ID: " + fileId);
		quizService.deleteQuizFile(fileId);

		return "deleted";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String initNewForm(Model model) {

		List<Quiz> allQuizs = new ArrayList<Quiz>();
		QuizForm quizForm = new QuizForm();
		for (int i = 0; i < 10; i++) {
			allQuizs.add(new Quiz());
		}

		quizForm.setQuizs(allQuizs);
	quizForm.setQuizTopic(new QuizTopic());

		model.addAttribute("quizForm", quizForm);
		return "quiz/createQuizForm";
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String createQuizTopicsPost(@ModelAttribute("user") User user, @ModelAttribute("quizForm") QuizForm quizForm) {
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}
		logger.info("Submitting quiz : " + quizForm.getQuizTopic().getContent());


		//logger.info("Valid Quiz Questions count : " + quizFormValidData.size());
		quizService.createQuizTopic(user, quizForm);
		// status.setComplete();
		return "redirect:/quiz";
	}



	@RequestMapping(value = "/{quizTopicId}/edit", method = RequestMethod.GET)
	public String initEditForm(@ModelAttribute("user") User user, @PathVariable("quizTopicId") int quizTopicId, Model model) {
		logger.debug("initEditForm");

		QuizForm quizForm = new QuizForm();
		QuizTopic quizTopic = quizService.getQuizTopic(quizTopicId);
		List<Quiz> existingQuizFields = quizService.getQuizs(quizTopicId);
		List<QuizFile> files = quizService.getQuizFiles(quizTopicId);
		//get due to lazy initialization
		quizTopic.setQuizs(existingQuizFields);
		quizTopic.setQuizFiles(files);
		List<Quiz> newQuizFields = new ArrayList<Quiz>();

	
		for(int i= quizTopic.getQuizs().size() ; i < 10 ; i++){
			
			newQuizFields.add(new Quiz());
		}
		quizForm.setQuizTopic(quizTopic);
		quizForm.setQuizs(newQuizFields);
	
		//	List<QuizFile> quizFiles = quizService.getQuizFiles(quizTopicId);
		//	model.addAttribute("quizFiles", quizFiles);
			model.addAttribute("quizForm", quizForm);
		return "quiz/editQuizForm";
	}

	@RequestMapping(value = "/{quizTopicId}/edit", method = RequestMethod.POST)
	public String updateProduct(@ModelAttribute("user") User user, @PathVariable("quizTopicId") int quizTopicId,  @ModelAttribute("quizForm") QuizForm quizForm) {
		logger.debug("update Quiz Topic");
		if (user == null) {
			logger.error("user is null");
			return "redirect:/users";
		}
		quizService.updateQuizTopic(user, quizTopicId, quizForm);
		return "redirect:/quiz";
	}

}
