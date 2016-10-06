/**
 * 
 */
package com.jitworks.shareinfo.data;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author j.paidimarla
 * 
 */
public class QuizForm {

	private QuizTopic quizTopic;

	/**
	 * @return the quizTopic
	 */
	public QuizTopic getQuizTopic() {
		return quizTopic;
	}

	/**
	 * @param quizTopic the quizTopic to set
	 */
	public void setQuizTopic(QuizTopic quizTopic) {
		this.quizTopic = quizTopic;
	}

	private MultipartFile file;
	
	private List<Quiz> quizs;



/*	public String getQuizTopicName() {
		return quizTopicName;
	}

	public void setQuizTopicName(String quizTopicName) {
		this.quizTopicName = quizTopicName;
	}
*/

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public List<Quiz> getQuizs() {
		return quizs;
	}

	public void setQuizs(List<Quiz> quizs) {
		this.quizs = quizs;
	}
	

}
