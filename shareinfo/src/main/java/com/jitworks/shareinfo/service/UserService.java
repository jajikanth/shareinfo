/**
 * 
 */
package com.jitworks.shareinfo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.data.UserCustom;

/**
 * @author j.paidimarla
 * 
 */
public interface UserService {

	List<User> getUsers();

	User getUser(int userId);

	void updateUser(User user, String statusMessage, MultipartFile multipartFile);

	void createUser(UserCustom registerUser);

	Boolean searchByUserName(String username);

	UserCustom getUserByEmail(String email);

	/**
	 * @param id
	 * @return
	 */
	List<User> getUserByGroupCode(String groupCode);

}
