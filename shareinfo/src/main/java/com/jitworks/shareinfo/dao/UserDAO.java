/**
 * 
 */
package com.jitworks.shareinfo.dao;

import java.util.List;

import com.jitworks.shareinfo.data.User;
import com.jitworks.shareinfo.data.UserCustom;

/**
 * @author j.paidimarla
 * 
 */
public interface UserDAO {

	List<User> getUsers();

	User getUser(int userId);

	void updateUser(User user);

	void createUser(UserCustom registerUser);

	Boolean searchByUserName(String username);

	UserCustom getUserByEmail(String email);

	/**
	 * @param groupId
	 * @return
	 */
	List<User> getUserByGroupCode(String groupCode);

}
