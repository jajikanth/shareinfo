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
@Table(name = "shareinfo_blog_view")
public class BlogSummary {

	@Id
	@Column(name = "blog_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "blog_name")
	private String title;

	@Column(name = "blog_content")
	@Type(type = "org.hibernate.type.StringClobType")
	private String content;

	@Column(name = "creation_time")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime creationTime;

	@Column(name = "deleted")
	private boolean deleted;

	@Column(name = "external_document")
	private boolean externalDocument;
	
	@Column(name = "contains_video")
	private boolean containsVideo;
	

	@ManyToOne
	@JoinColumn(name = "user_id_creator")
	private User user;

	@ManyToOne
	@JoinColumn(name = "blog_category_id")
	private BlogTopic blogTopic;

	@Column(name = "comment_count")
	private int commentCount;
	

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public BlogTopic getBlogTopic() {
		return blogTopic;
	}

	public void setBlogTopic(BlogTopic blogTopic) {
		this.blogTopic = blogTopic;
	}

	public boolean isExternalDocument() {
		return externalDocument;
	}

	public void setExternalDocument(boolean externalDocument) {
		this.externalDocument = externalDocument;
	}

	public boolean isContainsVideo() {
		return containsVideo;
	}


	public void setContainsVideo(boolean containsVideo) {
		this.containsVideo = containsVideo;
	}


}
