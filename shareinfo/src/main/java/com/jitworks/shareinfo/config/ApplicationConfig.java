/**
 * 
 */
package com.jitworks.shareinfo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author j.paidimarla
 *
 */
@Component
public class ApplicationConfig {

	@Value("${application.fileBasePath}")
	private String fileBasePath;

	@Value("${application.uploadBasePath}")
	private String uploadBasePath;

	@Value("${application.uploadBasePathForOmadmQa}")
	private String uploadBasePathForOmadmQa;
	
	@Value("${application.downloadBaseURI}")
	private String downloadBaseURI;

	public String getFileBasePath() {
		return fileBasePath;
	}

	public void setFileBasePath(String fileBasePath) {
		this.fileBasePath = fileBasePath;
	}

	/**
	 * @return the uploadBasePath
	 */
	public String getUploadBasePath() {
		return uploadBasePath;
	}

	/**
	 * @param uploadBasePath the uploadBasePath to set
	 */
	public void setUploadBasePath(String uploadBasePath) {
		this.uploadBasePath = uploadBasePath;
	}

	public String getDownloadBaseURI() {
		return downloadBaseURI;
	}

	public void setDownloadBaseURI(String downloadBaseURI) {
		this.downloadBaseURI = downloadBaseURI;
	}

	public String getUploadBasePathForOmadmQa() {
		return uploadBasePathForOmadmQa;
	}

	public void setUploadBasePathForOmadmQa(String uploadBasePathForOmadmQa) {
		this.uploadBasePathForOmadmQa = uploadBasePathForOmadmQa;
	}
	
}
