package com.jitworks.shareinfo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.jitworks.shareinfo.data.User;

/**
 * @author j.paidimarla
 *
 */
public class UserContextUtils {
	private static Logger logger = LoggerFactory.getLogger(UserContextUtils.class);

	public static String getUsername() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				return (String)authentication.getName();
			}
		} catch (Exception e) {
			logger.debug("getUsername fail: " + e.getMessage());
		}
		return null;
	}

	public static User getUser() {
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (user != null) {
				return user;
			}
			logger.debug("user is null");
		} catch (Exception e) {
			logger.debug("getUser failed: " + e.getMessage());
		}
		return null;
	}

	public static int getUserId() {
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (user != null) {
				logger.debug("userId: " + user.getId());
				return user.getId();
			}
			logger.debug("user is null");
		} catch (Exception e) {
			logger.debug("getUserId failed: " + e.getMessage());
		}
		return 0;
	}
}
