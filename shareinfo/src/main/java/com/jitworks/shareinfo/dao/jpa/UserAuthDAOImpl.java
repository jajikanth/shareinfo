package com.jitworks.shareinfo.dao.jpa;

import com.jitworks.shareinfo.data.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class UserAuthDAOImpl implements UserDetailsService {
	private static Logger logger = LoggerFactory.getLogger(UserAuthDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		logger.debug("loadUserByUsername: " + username);
		User user = null;
		try {
			TypedQuery<User> query = this.em.createQuery(
					"SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username) OR LOWER(u.email) = LOWER(:username) ",
					User.class);
			query.setParameter("username", username);
			user = query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UsernameNotFoundException("Not found: " + username, e);
		}
		logger.debug("user: " + user);
		if (user == null) {
			throw new UsernameNotFoundException("Not found: " + username);
		}
		logger.debug("username: " + user.getUsername());
		logger.debug("password: " + user.getPassword());
		logger.debug("authorities: " + user.getAuthorities());
		return user;
	}
	
	
	
	
	
}
