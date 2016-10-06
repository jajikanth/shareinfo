/**
 * 
 */
package com.jitworks.shareinfo.dao;

import java.util.List;

import com.jitworks.shareinfo.data.Folder;

/**
 * @author j.paidimarla
 *
 */
public interface FileAppDAO {

	/**
	 * @param userId
	 * @return
	 */
	List<Folder> getFolderByUserId(int userId);

}
