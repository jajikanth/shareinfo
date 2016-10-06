/**
 * 
 */
package com.jitworks.shareinfo.data;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author j.paidimarla
 * 
 */
@Entity
@Table(name = "shareinfo_quiz_category")
public class QuizTopic {

	@Id
	@Column(name = "quiz_category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "quiz_category")
	private String content;

	@Column(name = "creation_time")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime creationTime;

	@Column(name = "deleted")
	private boolean deleted;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
//, fetch = FetchType.LAZY
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "quizTopic")
	@OrderBy("id DESC")
	private List<Quiz> quizs;

	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "quizTopic")
	@OrderBy("id DESC")
	private List<QuizFile> quizFiles;
	
	
	public List<Quiz> getQuizs() {
		return quizs;
	}

	public void setQuizs(List<Quiz> quizs) {
		this.quizs = quizs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public DateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the quizFiles
	 */
	public List<QuizFile> getQuizFiles() {
		return quizFiles;
	}

	/**
	 * @param quizFiles the quizFiles to set
	 */
	public void setQuizFiles(List<QuizFile> quizFiles) {
		this.quizFiles = quizFiles;
	}
	

}
