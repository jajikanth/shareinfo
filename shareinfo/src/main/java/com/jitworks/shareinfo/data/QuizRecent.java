/**
 * 
 */
package com.jitworks.shareinfo.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author j.paidimarla
 * 
 */

@Entity
@Table(name = "shareinfo_quiz")
public class QuizRecent {

	@Id
	@Column(name = "quiz_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "quiz_question")
	private String question;

	@Column(name = "quiz_answer")
	private String answer;

	@Column(name = "deleted")
	private boolean deleted;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

}
