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
import javax.persistence.Transient;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author j.paidimarla
 * 
 */
@Entity
@Table(name = "shareinfo_blog")
public class Blog {

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
	
	@JsonIgnore
	// , fetch = FetchType.EAGER
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "blog")
	@OrderBy("id DESC")
	private List<BlogFile> blogFiles;
	
	
	@JsonIgnore
	@Transient
	private MultipartFile file ;

	@Transient	
	private boolean sendEmail ; 
	
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

	/**
	 * @return the containsVideo
	 */
	public boolean isContainsVideo() {
		return containsVideo;
	}

	/**
	 * @param containsVideo the containsVideo to set
	 */
	public void setContainsVideo(boolean containsVideo) {
		this.containsVideo = containsVideo;
	}

	public List<BlogFile> getBlogFiles() {
		return blogFiles;
	}

	public void setBlogFiles(List<BlogFile> blogFiles) {
		this.blogFiles = blogFiles;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public boolean isSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}



}
