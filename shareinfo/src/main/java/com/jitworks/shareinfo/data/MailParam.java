/**
 * 
 */
package com.jitworks.shareinfo.data;

import java.util.List;

/**
 * @author j.paidimarla
 * 
 */
public class MailParam {

	private String toEmailId;// To whom mail send
	
	private List<String> toEmailIds;// To whom mail send
	
	//private String FromEmailId; // from email id, this is a constant for current implementation

	private String ccEmailId; // bcc emailid
	
	private String bccEmailId; // bcc emailid

	private String subject; // mail subject

	private String MessageBody; // mail message body

	private String mailattachment; // mail attachment path



/*	public String getFromEmailId() {
		return FromEmailId;
	}

	public void setFromEmailId(String fromEmailId) {
		FromEmailId = fromEmailId;
	}*/

	public String getBccEmailId() {
		return bccEmailId;
	}

	public void setBccEmailId(String bccEmailId) {
		this.bccEmailId = bccEmailId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessageBody() {
		return MessageBody;
	}

	public void setMessageBody(String messageBody) {
		MessageBody = messageBody;
	}

	public String getMailattachment() {
		return mailattachment;
	}

	public void setMailattachment(String mailattachment) {
		this.mailattachment = mailattachment;
	}

	public String getCcEmailId() {
		return ccEmailId;
	}

	public void setCcEmailId(String ccEmailId) {
		this.ccEmailId = ccEmailId;
	}

	public String getToEmailId() {
		return toEmailId;
	}

	public void setToEmailId(String toEmailId) {
		this.toEmailId = toEmailId;
	}

	public List<String> getToEmailIds() {
		return toEmailIds;
	}

	public void setToEmailIds(List<String> toEmailIds) {
		this.toEmailIds = toEmailIds;
	}

}
