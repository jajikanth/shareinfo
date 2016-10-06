/**
 * 
 */
package com.jitworks.shareinfo.validator;

/**
 * @author kwanchul
 *
 */
public class ValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -763802879027677268L;

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
