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
import com.jitworks.shareinfo.dao.QuizDAO;
import com.jitworks.shareinfo.data.Quiz;
import com.jitworks.shareinfo.data.QuizFile;
import com.jitworks.shareinfo.data.QuizForm;
import com.jitworks.shareinfo.data.QuizRecent;
import com.jitworks.shareinfo.data.QuizTopic;
import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.exception.BadRequestException;
import com.jitworks.shareinfo.service.QuizService;

/**
 * @author j.paidimarla
 * 
 */

@Service
public class QuizServiceImpl implements QuizService {

	private Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

	@Autowired
	private QuizDAO quizDAO;

	@Autowired
	private ApplicationConfig applicationConfig;

	@Transactional(readOnly = true)
	public List<QuizTopic> getQuizTopics() {

		return quizDAO.getQuizTopics();
	}

	@Transactional(readOnly = true)
	public List<Quiz> getQuizs(int quizTopicId) {

		return quizDAO.getQuizs(quizTopicId);
	}

	@Transactional(readOnly = true)
	public QuizTopic getQuizTopic(int quizTopicId) {

		return quizDAO.getQuizTopic(quizTopicId);
	}

	@Transactional
	public void deleteQuiz(int quizTopicId) {
		List <QuizFile> quizFiles  = quizDAO.getQuizFiles(quizTopicId);
		for (QuizFile quizFile : quizFiles) {
			if(!quizFile.getFilePath().isEmpty()){
				deleteFile(quizFile.getFilePath());
			}
		}
		quizDAO.deleteQuiz(quizTopicId);

	}

	@Transactional
	public void createQuizTopic(User user, QuizForm quizForm) {
		// Create Quiz Topic with Quiz(Q&A)
		QuizTopic quizTopic = quizForm.getQuizTopic();
	//	quizTopic = creatingQuiz(quizTopic, user, quizForm);
		List<Quiz> quizFormValidData = new ArrayList<Quiz>();
		for (Quiz quiz : quizForm.getQuizs()) {
			if (quiz.getQuestion().length() > 0) {
				quiz.setQuizTopic(quizTopic);
				quizFormValidData.add(quiz);
			}
		}
		quizTopic.setCreationTime(new DateTime());
		quizTopic.setQuizs(quizFormValidData);
		quizTopic.setUser(user);
	//	creatingQuizFile(user, quizForm, quizTopic, quizFiles);
		MultipartFile multipartFile = quizForm.getFile();
		if (!multipartFile.isEmpty()) {
			List<QuizFile> quizFiles = new ArrayList<QuizFile>();
			
			QuizFile quizFile = createNewFileDirectory(user, quizTopic, multipartFile);
			quizFiles.add(quizFile);
			quizTopic.setQuizFiles(quizFiles);
		}
		quizDAO.cerateQuizTopic(quizTopic);
		
		
	}

	/**
	 * @param user
	 * @param quizTopic
	 * @param multipartFile
	 * @return
	 */
	private QuizFile createNewFileDirectory(User user, QuizTopic quizTopic, MultipartFile multipartFile) {
		String rootPath = applicationConfig.getFileBasePath();
		String filePath = "QuizFiles/" +  user.getFirstName() + "/" + quizTopic.getContent()  + "/"+System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
		logger.debug("file: " + rootPath + filePath);
		File dest = new File(rootPath + filePath);
		try {
			if (dest.mkdirs()) {
				multipartFile.transferTo(dest);
			} else {
				logger.error("mkdirs failed: " + dest);
			}
		} catch (IllegalStateException e) {
			logger.error("File upload failed", e);
			throw new BadRequestException();
		} catch (IOException e) {
			logger.error("File upload failed", e);
			throw new BadRequestException();
		}

		QuizFile quizFile = new QuizFile();
		quizFile.setCreationTime(new DateTime());
		quizFile.setFilePath(filePath);
		quizFile.setName(multipartFile.getOriginalFilename());
		
		quizFile.setQuizTopic(quizTopic);
		quizFile.setSize(multipartFile.getSize());
		quizFile.setContentType(multipartFile.getContentType());
		return quizFile;
	}

	//used to create and update quiz
/*	private QuizTopic creatingQuiz(QuizTopic quizTopic, User user, QuizForm quizForm) {

		List<Quiz> quizFormValidData = new ArrayList<Quiz>();
	
		for (Quiz quiz : quizForm.getQuizTopic().getQuizs()) {
			if (quiz.getQuestion().length() > 0) {

				quiz.setQuizTopic(quizTopic);
				quizFormValidData.add(quiz);

			}
		}
	//	quizTopic.setContent(quizForm.getQuizTopic().getContent());
		quizTopic.setCreationTime(new DateTime());
		quizTopic.setQuizs(quizFormValidData);
		quizTopic.setUser(user);
		return quizTopic;
	}*/

	@Transactional
	public void updateQuizTopic(User user, int quizTopicId, QuizForm quizForm) {
		QuizTopic quizTopic = quizForm.getQuizTopic();
		quizTopic.setUser(user);
		
		List<Quiz> newValidQuizs = new ArrayList<Quiz>();
		
		for (Quiz quiz : quizForm.getQuizs()) {
			if (quiz.getQuestion().length() > 0) {
				quiz.setQuizTopic(quizTopic);
				newValidQuizs.add(quiz);
			}
		}
		
		//QuizTopic quizTopic = quizDAO.getQuizTopic(quizTopicId);
	//	List<QuizFile> quizFiles = quizTopic.getQuizFiles();
		//	QuizTopic updatedQuizToipc = creatingQuiz(quizTopic, user, quizForm);
			
		//	creatingQuizFile(user, quizForm, quizTopic, quizFiles);
		MultipartFile multipartFile = quizForm.getFile();
		QuizFile newQuizFile = null;
		if (!multipartFile.isEmpty()) {
			 newQuizFile = createNewFileDirectory(user, quizTopic, multipartFile);
		}
				
		quizDAO.updateQuizTopic(quizTopic, newValidQuizs , newQuizFile);
	}

	@Transactional(readOnly = true)
	public List<QuizRecent> getRecentQuizs() {
		return quizDAO.getRecentQuizs();
	}

	@Transactional(readOnly = true)
	public List<QuizFile> getQuizFiles(int quizTopicId) {

		return quizDAO.getQuizFiles(quizTopicId);
	}

	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.service.QuizService#getQuizFile(int)
	 */
	@Transactional(readOnly = true)
	public QuizFile getQuizFile(int fileId) {
		return quizDAO.getQuizFile(fileId);
	}

	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.service.QuizService#deleteQuizFile(int)
	 */
	@Transactional
	public void deleteQuizFile(int fileId) {

		QuizFile quizFile = quizDAO.getQuizFile(fileId);
		deleteFile(quizFile.getFilePath());
		quizDAO.deleteQuizFile(fileId);
	}
	
	
	
	
	private void deleteFile(String filePath) {
		if (FileUtils.deleteQuietly(new File(applicationConfig.getFileBasePath() + filePath))) {
			logger.debug("file[" + filePath + "] deleted.");
		} else {
			logger.error("file[" + filePath + "] failed to delete.");
		}
	}
	
	
	

}
