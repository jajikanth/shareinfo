/**
 * 
 */
package com.jitworks.shareinfo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jitworks.shareinfo.dao.FileAppDAO;
import com.jitworks.shareinfo.data.Folder;
import com.jitworks.shareinfo.service.FileAppService;

/**
 * @author j.paidimarla
 *
 */
@Service
public class FileAppServiceImpl implements FileAppService{

	@Autowired
	private FileAppDAO fileAppDAO;
	
	/* (non-Javadoc)
	 * @see com.jitworks.shareinfo.service.FileAppService#getFoldersByUserId(int)
	 */
	@Transactional(readOnly = true)
	public List<Folder> getFoldersByUserId(int userId) {
		// TODO Auto-generated method stub
		return fileAppDAO.getFolderByUserId(userId);
	}
	
	
	

}
