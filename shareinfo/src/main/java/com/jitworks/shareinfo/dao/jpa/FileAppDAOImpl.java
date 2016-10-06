/**
 * 
 */
package com.jitworks.shareinfo.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jitworks.shareinfo.dao.FileAppDAO;

import com.jitworks.shareinfo.data.Folder;

/**
 * @author j.paidimarla
 *
 */
@Repository
public class FileAppDAOImpl implements FileAppDAO{
	private static Logger logger = LoggerFactory.getLogger(FileAppDAOImpl.class);
	
	@PersistenceContext
	private EntityManager em;

	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.dao.FileAppDAO#getFolderByUserId(int)
	 */

	public List<Folder> getFolderByUserId(int userId) {
		TypedQuery<Folder> query;
		//TODO : Check for expiry when implemented
		query = em.createQuery("SELECT f FROM Folder f WHERE f.user.id = :userId AND f.deleted = false", Folder.class);
		query.setParameter("userId", userId);

		List <Folder> folder = null;
		try {
			folder = query.getResultList();
		} catch (NoResultException e) {
			logger.error("no Folder with user id :  " + userId);
		}
		return folder;
	}
	
	

}
