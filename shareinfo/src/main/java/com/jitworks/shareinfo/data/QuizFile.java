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

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author j.paidimarla
 *
 */
@Entity
@Table(name = "shareinfo_quiz_file")
public class QuizFile {

		@Id
		@Column(name = "quiz_file_id")
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int id;
	  
		@Column(name = "name")
		private String name;

		@Column(name = "file_path")
		private String filePath;
		
	  
		@Column(name = "creation_time")
		@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
		private DateTime creationTime;

		@Column(name = "deleted")
		private boolean deleted;
		
		@Column(name = "size")
		private long size;
	  
		@Column(name = "type")
		private String contentType;
		
/*	@Column(name = "quiz_category_id")
		private int quizCategoryId;*/

		@ManyToOne
		@JoinColumn(name = "quiz_category_id")
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


		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}


		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getFilePath() {
			return filePath;
		}


		public void setFilePath(String filePath) {
			this.filePath = filePath;
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


		/**
		 * @return the size
		 */
		public long getSize() {
			return size;
		}


		/**
		 * @param size the size to set
		 */
		public void setSize(long size) {
			this.size = size;
		}


		/**
		 * @return the contentType
		 */
		public String getContentType() {
			return contentType;
		}


		/**
		 * @param contentType the contentType to set
		 */
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}



		

}
