package com.jitworks.shareinfo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.util.StreamUtils;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.jitworks.shareinfo.config.ApplicationConfig;
import com.jitworks.shareinfo.data.BlogFile;
import com.jitworks.shareinfo.data.QuizFile;

import com.jitworks.shareinfo.exception.BadRequestException;
import com.jitworks.shareinfo.exception.ResourceNotFoundException;
import com.jitworks.shareinfo.service.BlogService;
import com.jitworks.shareinfo.service.QuizService;
import com.jitworks.shareinfo.service.UserService;


@Controller
public class DownloadController {
	private static Logger logger = LoggerFactory.getLogger(DownloadController.class);

	@Autowired
	private ApplicationConfig applicationConfig;

	@Autowired
	private UserService userService;
	@Autowired
	private QuizService quizService;
	
	@Autowired
	private BlogService blogService;

	/**
	 * Streams the binary of the specified <code>updatePackageId</code>.
	 * 
	 * @param updatePackageId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/download/users/images/{userId}", method = RequestMethod.GET, produces = "image/jpg")
	public void getImageDownload(@PathVariable("userId") int userId, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getDownload, userId: " + userId);
		/*
		 * User user = userService.getUser(userId);
		 * 
		 * if (user == null) { throw new ResourceNotFoundException(); }
		 */

		String rootPath = applicationConfig.getFileBasePath();
		String filePath = rootPath + "userImages/" + userId + ".jpg";
		logger.info("file path : " + filePath);
		// response.setContentType("image/png");
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(filePath);
			out = response.getOutputStream();
			StreamUtils.copy(in, out);

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception localException2) {
				} finally {
					in = null;
				}
			}
			if (out != null)
				try {
					out.close();
				} catch (Exception localException3) {
				} finally {
					out = null;
				}
		}
	}

	@RequestMapping(value = "/download/quizFiles/{fileId}", method = RequestMethod.GET)
	public void getQuizFileDownload(@PathVariable("fileId") int fileId, HttpServletRequest request, HttpServletResponse response) {
		
		QuizFile quizFile = this.quizService.getQuizFile(fileId);
		if (quizFile == null) {
			throw new ResourceNotFoundException();
		}
		String rootPath = applicationConfig.getFileBasePath();

		String filePath = rootPath + quizFile.getFilePath() ; 
		long fileSize = quizFile.getSize();

		String fileName =  quizFile.getName();
		String fileContentType = quizFile.getContentType();
		/**************************************************************************************************************/
		String range = request.getHeader("Range");
		logger.debug("Range: " + range);
		long begin = 0;
		long end = fileSize - 1;
		if (range != null) {
			if (range.startsWith("bytes=") == false) {
				// error
				throw new BadRequestException();
			}
			
			int indexDash = range.indexOf("-");
			if (indexDash == -1) {
				throw new BadRequestException();
			} else if (indexDash > 6) {
				begin = Long.parseLong(range.substring(6, indexDash));
				if (begin > fileSize) {
					//throw new RequestedRangeNotSatisfiableException();
					throw new BadRequestException();
				}
			}
			
			if (indexDash + 1 < range.length()) {
				end = Long.parseLong(range.substring(indexDash + 1));
				if (end > fileSize) {
					//throw new RequestedRangeNotSatisfiableException();
					throw new BadRequestException();
				}
			}
			
			logger.debug("begin: " + begin + ", end: " + end);
			
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			response.setHeader("Content-Range", "bytes " + begin + "-" + end + "/" + fileSize);
		}
		response.setContentType(fileContentType);
		response.setHeader("Content-Disposition","attachment; filename=\"" + fileName +"\"");
		response.setContentLength((int)(end - begin + 1));
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1L);

		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(filePath);
			out = response.getOutputStream();
			if (range != null) {
				IOUtils.copyLarge(in, out, begin, (end - begin + 1));
			} else {
				StreamUtils.copy(in, out);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception localException2) {
				} finally {
					in = null;
				}
			}
			if (out != null)
				try {
					out.close();
				} catch (Exception localException3) {
				} finally {
					out = null;
				}
		}
	
	
	
		
		
		
	}

	@RequestMapping(value = "/download/blogFiles/{fileId}", method = RequestMethod.GET)
	public void getBlogFileDownload(@PathVariable("fileId") int fileId, HttpServletRequest request, HttpServletResponse response) {
		
		BlogFile blogFile = this.blogService.getBlogFileById(fileId);
		
		if (blogFile == null) {
			throw new ResourceNotFoundException();
		}
		String rootPath = applicationConfig.getFileBasePath();

		String filePath = rootPath + blogFile.getFilePath() ; 
		long fileSize = blogFile.getSize();

		String fileName =  blogFile.getName();
		String fileContentType = blogFile.getContentType();
		

		/******************************************************************************************/
		String range = request.getHeader("Range");
		logger.debug("Range: " + range);
		long begin = 0;
		long end = fileSize - 1;
		if (range != null) {
			if (range.startsWith("bytes=") == false) {
				// error
				throw new BadRequestException();
			}
			
			int indexDash = range.indexOf("-");
			if (indexDash == -1) {
				throw new BadRequestException();
			} else if (indexDash > 6) {
				begin = Long.parseLong(range.substring(6, indexDash));
				if (begin > fileSize) {
					//throw new RequestedRangeNotSatisfiableException();
					throw new BadRequestException();
				}
			}
			
			if (indexDash + 1 < range.length()) {
				end = Long.parseLong(range.substring(indexDash + 1));
				if (end > fileSize) {
					//throw new RequestedRangeNotSatisfiableException();
					throw new BadRequestException();
				}
			}
			
			logger.debug("begin: " + begin + ", end: " + end);
			
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			response.setHeader("Content-Range", "bytes " + begin + "-" + end + "/" + fileSize);
		}
		response.setContentType(fileContentType);
		response.setHeader("Content-Disposition","attachment; filename=\"" + fileName +"\"");
		response.setContentLength((int)(end - begin + 1));
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1L);

		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(filePath);
			out = response.getOutputStream();
			if (range != null) {
				IOUtils.copyLarge(in, out, begin, (end - begin + 1));
			} else {
				StreamUtils.copy(in, out);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception localException2) {
				} finally {
					in = null;
				}
			}
			if (out != null)
				try {
					out.close();
				} catch (Exception localException3) {
				} finally {
					out = null;
				}
		}
	}
	
	
}

