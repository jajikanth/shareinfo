/**
 * 
 */
package com.jitworks.shareinfo.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jitworks.shareinfo.dao.EmailDAO;
import com.jitworks.shareinfo.data.MailTemplate;

/**
 * @author j.paidimarla
 *
 */
@Repository
public class EmailDAOImpl implements EmailDAO{
	private static Logger logger = LoggerFactory.getLogger(EmailDAOImpl.class);
	
	@PersistenceContext
	private EntityManager em;


	public MailTemplate getEmailTemplate(int emailTemplateId) {
		TypedQuery<MailTemplate> query;
		query = em.createQuery("SELECT m FROM MailTemplate m WHERE m.id = :emailTemplateId AND m.deleted = false", MailTemplate.class);
		query.setParameter("emailTemplateId", emailTemplateId);

		MailTemplate mailTemplate = null;
		try {
			mailTemplate = query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("no Email Template with id :  " + emailTemplateId);
		}
return mailTemplate;
	}
	
	
}
