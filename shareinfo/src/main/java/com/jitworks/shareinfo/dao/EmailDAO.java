/**
 * 
 */
package com.jitworks.shareinfo.dao;

import com.jitworks.shareinfo.data.MailTemplate;

/**
 * @author j.paidimarla
 *
 */
public interface EmailDAO {

	/**
	 * @param id
	 * @return
	 */
	MailTemplate getEmailTemplate(int emailTemplateId);
	

	
}
