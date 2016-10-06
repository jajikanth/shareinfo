/**
 * 
 */
package com.jitworks.shareinfo.service;

import java.util.List;

import com.jitworks.shareinfo.data.Quiz;
import com.jitworks.shareinfo.data.QuizFile;
import com.jitworks.shareinfo.data.QuizForm;
import com.jitworks.shareinfo.data.QuizRecent;
import com.jitworks.shareinfo.data.QuizTopic;
import com.jitworks.shareinfo.data.User;

/**
 * @author j.paidimarla
 *
 */
public interface QuizService {

	List<QuizTopic> getQuizTopics();

	List<Quiz> getQuizs(int quizTopicId);

	QuizTopic getQuizTopic(int quizTopicId);

	void deleteQuiz(int quizTopicId);
	
	void createQuizTopic(User user, QuizForm quizForm);

	void updateQuizTopic(User user, int quizTopicId, QuizForm quizForm);

	List<QuizRecent> getRecentQuizs();

	List<QuizFile> getQuizFiles(int quizTopicId);

	/**
	 * @param fileId
	 * @return
	 */
	QuizFile getQuizFile(int fileId);

	/**
	 * @param fileId
	 */
	void deleteQuizFile(int fileId);

}
