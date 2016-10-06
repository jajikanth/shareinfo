/**
 * 
 */
package com.jitworks.shareinfo.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jitworks.shareinfo.dao.UserDAO;
import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.data.UserCustom;

/**
 * @author j.paidimarla
 * 
 */

@Repository
public class UserDAOImpl implements UserDAO {
	private static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	public void createUser(UserCustom registerUser) {
		logger.info("Create New User.");
		em.persist(registerUser);

	}

	public List<User> getUsers() {
		logger.debug("getUsers");
		TypedQuery<User> query;
		query = this.em.createQuery("SELECT user FROM User user WHERE user.userGroup.code = 'OMADM' ORDER BY user.firstName", User.class);
		return query.getResultList();
	}

	public User getUser(int userId) {
		logger.debug("getUser with ID:" + userId);
		TypedQuery<User> query;
		query = this.em.createQuery("SELECT user FROM User user WHERE user.id = :userId", User.class);
		query.setParameter("userId", userId);
		User user = null;
		try {
			user = query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("No user with ID:" + userId);
		}

		return user;
	}

	public void updateUser(User user) {
		this.em.merge(user);
	}

	public Boolean searchByUserName(String username) {
		// TODO Auto-generated method stub
		logger.debug("getUser with username : " + username);
		TypedQuery<User> query;
		query = this.em.createQuery("SELECT user FROM User user WHERE user.username = :username", User.class);
		query.setParameter("username", username);
		User user = null;
		Boolean isExisting = false;
		try {
			user = query.getSingleResult();
		} catch (NoResultException e) {
			logger.debug("No user with ID:" + username);
		}catch (NonUniqueResultException e) {
			logger.error("contains multiple users with same id:" + username);
			isExisting = true;
		}
		//TODO:refactor
		if (user != null) {
			isExisting = true;
		}
		return isExisting;
	}

	public UserCustom getUserByEmail(String email) {
		//as the username and email address are same
		logger.debug("getUser with username : " + email);
		TypedQuery<UserCustom> query;
		query = this.em.createQuery("SELECT user FROM UserCustom user WHERE user.username = :username", UserCustom.class);
		query.setParameter("username", email);
		UserCustom user = null;
	
		try {
			user = query.getSingleResult();
		} catch (NoResultException e) {
			logger.debug("No user with ID:" + email);
		}catch (NonUniqueResultException e) {
			logger.error("contains multiple users with same id:" + email);
		}
		return user;
	}

	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.dao.UserDAO#getUserByGroupId(int)
	 */
	@Override
	public List<User> getUserByGroupCode(String groupCode) {
		logger.debug("getUsers");
		TypedQuery<User> query;
		query = this.em.createQuery("SELECT user FROM User user WHERE user.userGroup.code = :groupCode ORDER BY user.firstName", User.class);
		query.setParameter("groupCode", groupCode);
		return query.getResultList();
		//return null;
	}

}
