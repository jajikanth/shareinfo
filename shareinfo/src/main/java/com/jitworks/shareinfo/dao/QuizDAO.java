/**
 * 
 */
package com.jitworks.shareinfo.dao;

import java.util.List;

import com.jitworks.shareinfo.data.Quiz;
import com.jitworks.shareinfo.data.QuizFile;
import com.jitworks.shareinfo.data.QuizRecent;
import com.jitworks.shareinfo.data.QuizTopic;

/**
 * @author j.paidimarla
 * 
 */
public interface QuizDAO {

	List<QuizTopic> getQuizTopics();

	List<Quiz> getQuizs(int quizTopicId);

	QuizTopic getQuizTopic(int quizTopicId);

	void deleteQuiz(int quizTopicId);

	void cerateQuizTopic(QuizTopic quizToipc);

	void updateQuizTopic(QuizTopic quizTopic, List<Quiz> newValidQuizs, QuizFile newQuizFile);

	List<QuizRecent> getRecentQuizs();

	List<QuizFile> getQuizFiles(int quizTopicId);

	QuizFile getQuizFile(int fileId);

	void deleteQuizFile(int fileId);

}
