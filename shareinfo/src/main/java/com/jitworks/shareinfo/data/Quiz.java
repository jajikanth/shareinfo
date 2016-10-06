/**
 * 
 */
package com.jitworks.shareinfo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * @author j.paidimarla
 * 
 */
@Entity
@Table(name = "shareinfo_quiz")
public class Quiz {

	@Id
	@Column(name = "quiz_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "quiz_question")
	private String question;

	@Column(name = "quiz_answer")
	private String answer;

/*	@Column(name = "creation_time")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime creationTime;*/

	@Column(name = "deleted")
	private boolean deleted;
	
/*	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;*/

	@ManyToOne
	@JoinColumn(name = "quiz_category_id")
	private QuizTopic quizTopic;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

/*	public DateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}*/

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

/*	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
*/
	public QuizTopic getQuizTopic() {
		return quizTopic;
	}

	public void setQuizTopic(QuizTopic quizTopic) {
		this.quizTopic = quizTopic;
	}

}
