/**
 * 
 */
package com.jitworks.shareinfo.service;

import com.jitworks.shareinfo.data.Blog;
import com.jitworks.shareinfo.data.MailParam;
import com.jitworks.shareinfo.data.MailTemplate;
import com.jitworks.shareinfo.data.UserCustom;

/**
 * @author j.paidimarla
 * 
 */
public interface EmailService {

	void sentEmail(MailParam mailParam);

	public MailTemplate getEmailTemplate(int emailTemplateId);

	void sendNewBlogMails(Blog blog);

	void emailPassword(UserCustom userCustom);

}
