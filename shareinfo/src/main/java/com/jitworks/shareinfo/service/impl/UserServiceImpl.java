/**
 * 
 */
package com.jitworks.shareinfo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jitworks.shareinfo.config.ApplicationConfig;
import com.jitworks.shareinfo.dao.UserDAO;
import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.data.UserCustom;
import com.jitworks.shareinfo.exception.BadRequestException;
import com.jitworks.shareinfo.service.UserService;

/**
 * @author j.paidimarla
 * 
 */
@Service
public class UserServiceImpl implements UserService {

	 private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private ApplicationConfig applicationConfig;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	StandardPasswordEncoder encoder;

	@Transactional(readOnly = true)
	public List<User> getUsers() {

		return userDAO.getUsers();
	}

	@Transactional(readOnly = true)
	public User getUser(int userId) {

		return userDAO.getUser(userId);
	}

/*	@Transactional
	public void updateUser(User user) {

		userDAO.updateUser(user);
	}*/

	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.service.UserService#updateUser(com.jitworks.shareinfo.data.User, java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	@Transactional
	public void updateUser(User user, String statusMessage, MultipartFile multipartFile) {
		// TODO Auto-generated method stub
		
		if(! statusMessage.isEmpty()){
			user.setStatusMessage(statusMessage);
			
			}
		
		
		String rootPath = applicationConfig.getFileBasePath();
		String filePath = "userImages/" + user.getId() + ".jpg";
		logger.debug("file: " + rootPath + filePath);
		File dest = new File(rootPath + filePath);

		try {
			if(!multipartFile.isEmpty() && (multipartFile.getSize()/1024) < 1024 ){
				dest.mkdirs() ;
				multipartFile.transferTo(dest);
			user.setImageSize(Long.toString((multipartFile.getSize()/1024))+"KB");
			}
		} catch (IllegalStateException e) {
			logger.error("File upload failed", e);
			throw new BadRequestException();
		} catch (IOException e) {
			logger.error("File upload failed", e);
			throw new BadRequestException();
		}
		this.userDAO.updateUser(user);
		
	
		
	}
	
	@Transactional
	public void createUser(UserCustom registerUser) {
		//get complete emailif
		registerUser.setEmail(registerUser.getUsername().trim().toLowerCase()+"@gmail.com");
		//TODO: change to differenciate the groups
		registerUser.setRole("ROLE_USER");
		registerUser.setUserGroup(1);
		registerUser.setPassword(encoder.encode(registerUser.getPasswordUnEncrypted()));
		
		;
		userDAO.createUser(registerUser);
		
	}


	@Transactional(readOnly = true)
	public Boolean searchByUserName(String username) {
		
		return userDAO.searchByUserName(username);
	}

	@Transactional(readOnly = true)
	public UserCustom getUserByEmail(String email) {
		
		return userDAO.getUserByEmail(email);
	}

	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.service.UserService#getUserByGroupId(int)
	 */
	@Transactional(readOnly = true)
	public List<User> getUserByGroupCode(String groupCode) {
		
		return userDAO.getUserByGroupCode(groupCode);
	}


}
