/**
 * 
 */
package com.jitworks.shareinfo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jitworks.shareinfo.dao.EmailDAO;
import com.jitworks.shareinfo.dao.UserDAO;
import com.jitworks.shareinfo.data.Blog;
import com.jitworks.shareinfo.data.MailParam;
import com.jitworks.shareinfo.data.MailTemplate;
import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.data.UserCustom;
import com.jitworks.shareinfo.service.EmailService;

/**
 * @author j.paidimarla
 * 
 */
@Service
public class EmailServiceImpl implements EmailService {

	

//	private static final String EMAIL_REPLY_TO = "j.paidimarla@sta.samsung.com";
//	private static final String EMAIL_FROM_NAME = "shareinfo Notifications";
	
//	private static final String EMAIL_FROM = "seceas044@smail0702.net";
//	private static final String EMAIL_PASSWORD = "Exchange$10";
//	private static final String EMAIL_USERID = "seceas044";
//	private static final String HOST_NAME = "smail0702.net";
//	private static final int EMAIL_PORT = 25;
	
	private static final String EMAIL_REPLY_TO = "iamjaji@gmail.com";
	private static final String EMAIL_FROM_NAME = "shareinfo Notifications";
	
	private static final String EMAIL_FROM = "jaji2015@gmail.com";
	private static final String EMAIL_PASSWORD = "jajikanth88";
	private static final String EMAIL_USERID = "jaji2015";
	private static final String HOST_NAME = "smtp.googlemail.com";
	private static final int EMAIL_PORT = 465;
	
	private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Autowired
	private EmailDAO emailDAO;

	@Autowired
	private UserDAO userDAO;

	public void sentEmail(MailParam mailParam) {
		try {
			HtmlEmail email = new HtmlEmail();
			// TODO: Refactor all strings as constants
			email.setHostName(HOST_NAME);
			email.setSmtpPort(EMAIL_PORT);
			email.setAuthenticator(new DefaultAuthenticator(EMAIL_USERID, EMAIL_PASSWORD));
			email.setFrom(EMAIL_FROM, EMAIL_FROM_NAME);
			//for gmail
			email.setSSLOnConnect(true);
			email.setDebug(true);

			email.setSubject(mailParam.getSubject());
			email.setHtmlMsg(mailParam.getMessageBody());
			email.setTextMsg("Your email client does not support HTML messages");
			// TODO : Chage to the Blog user ID.
			email.addReplyTo(EMAIL_REPLY_TO);
			if (mailParam.getToEmailId() != null)
				email.addTo(mailParam.getToEmailId());
			if (mailParam.getCcEmailId() != null)
				email.addCc(mailParam.getCcEmailId());
			try {
				if (mailParam.getToEmailIds() != null && !mailParam.getToEmailIds().isEmpty())
					email.addBcc(mailParam.getToEmailIds().toArray(new String[mailParam.getToEmailIds().size()])); // Convert to array send the email
			} catch (Exception e) {

				logger.error("Error in adding to email IDs : " + e);
			}
			logger.info("HostName : " + email.getHostName() + " ; Port : " + email.getSmtpPort() + "");
			email.send();
			logger.info(">>>>>>>>>>>>>>>>>>>>Email Sent. . .");

		} catch (EmailException e) {
			logger.error(">>>>>>>>>>>>>>>>>>>Email Exception . . . ." + e);
		}
	}

	public MailTemplate getEmailTemplate(int emailTemplateId) {
		logger.debug("Get Email template for id" + emailTemplateId);
		return emailDAO.getEmailTemplate(emailTemplateId);
	}

	@Async
	public void sendNewBlogMails(Blog blog) {
		// Template id is used for new blog emails
		MailTemplate mailTemplate = emailDAO.getEmailTemplate(1);
		MailParam mailParam = new MailParam();
		List<User> users = userDAO.getUsers();
		List<String> userEmailIds = new ArrayList<String>();
		for (User user : users) {
			try {
				// remove auth user form this list
				if (!user.getEmail().isEmpty() && !user.getEmail().equals(blog.getUser().getEmail())) {
					userEmailIds.add(user.getEmail());
				}
			} catch (NullPointerException e) {
				logger.error("No Email ID found for user :  " + user.getFirstName());
			} catch (Exception e) {
				logger.error("Add user Email ID exception . . . ." + e);
			}
		}

		if (mailTemplate != null) {
			String updateTemplate = mailTemplate.getTemplate();
			if (mailTemplate.getTemplate().contains("$blogtitle$"))
				updateTemplate = updateTemplate.replace("$blogtitle$", blog.getTitle());
			if (mailTemplate.getTemplate().contains("$blogcontent$"))
				updateTemplate = updateTemplate.replace("$blogcontent$", blog.getContent());

			mailParam.setSubject(blog.getUser().getFirstName().concat(" Created/Updated a blog in shareinfo"));
			// mailParam.setToEmailId(EMAIL_REPLY_TO);
			mailParam.setCcEmailId(blog.getUser().getEmail());
			mailParam.setToEmailIds(userEmailIds);// send as bcc
			mailParam.setMessageBody(updateTemplate);
			sentEmail(mailParam);
			/*
			 * //check for async task try {
			 * logger.info("sleeping thread>>>>>>>>>>>>>>>");
			 * Thread.sleep(25000); logger.info("After delay>>>>>>>>>>>>>>>"); }
			 * catch (InterruptedException e) { 
			 * block e.printStackTrace(); logger.error("sleeping thread"); }
			 */

		}

	}

	@Async
	public void emailPassword(UserCustom userCustom) {
		// TODO Create new template if required
		MailTemplate mailTemplate = emailDAO.getEmailTemplate(1);
		MailParam mailParam = new MailParam();

		// List<String> userEmailIds = new ArrayList<String>();
		if (mailTemplate != null) {
			String updateTemplate = mailTemplate.getTemplate();
			if (mailTemplate.getTemplate().contains("$blogtitle$"))
				updateTemplate = updateTemplate.replace("$blogtitle$", "Your Password is : ".concat(userCustom.getPasswordUnEncrypted()));
			if (mailTemplate.getTemplate().contains("$blogcontent$"))
				updateTemplate = updateTemplate.replace("$blogcontent$", "If you have not requested for your password, Please inform administrator. ");
			mailParam.setSubject(userCustom.getFirstName().concat(",  Please find your password for shareinfo."));
			mailParam.setToEmailId(userCustom.getEmail());
			mailParam.setMessageBody(updateTemplate);
			sentEmail(mailParam);

		}

		
		
		
		
	}

}
