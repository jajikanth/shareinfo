/**
 * 
 */
package com.jitworks.shareinfo.service;

import java.util.List;

import com.jitworks.shareinfo.data.Folder;

/**
 * @author j.paidimarla
 *
 */
public interface FileAppService {

	/**
	 * @param userId
	 * @return
	 */
	List<Folder> getFoldersByUserId(int userId);

}
