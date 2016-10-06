package com.jitworks.shareinfo.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

public class ValidationUtils {
	public static final int ANY = 0;
	public static final int ALPHANUMERIC = 1;
	public static final int NUMERIC = 2;
	public static final int HEXADECIMAL = 3;
	public static final int VERSION_NAME = 4;

	public static String validate(Errors errors, String field, int minLength,
			int maxLength, boolean autoTrim) {
		return validate(errors, field, minLength, maxLength, autoTrim, 0);
	}

	public static String validate(Errors errors, String field, int minLength,
			int maxLength, boolean autoTrim, int stringType) {
		String fieldValue = (String) errors.getFieldValue(field);
		if (autoTrim) {
			fieldValue = StringUtils.trim(fieldValue);
		}

		if (StringUtils.isEmpty(fieldValue)) {
			if (minLength == 0) {
				return fieldValue;
			}
			errors.rejectValue(field, "field.required", "required");
			return fieldValue;
		}

		if (fieldValue.length() < minLength) {
			errors.rejectValue(field, "field.minLength", "min length: " + minLength);
			return fieldValue;
		}
		if (fieldValue.length() > maxLength) {
			errors.rejectValue(field, "field.maxLength", "max length: " + maxLength);
			return fieldValue;
		}

		switch (stringType) {
		case ALPHANUMERIC:
			if (!StringUtils.isAlphanumeric(fieldValue)) {
				errors.rejectValue(field, "field.alphanumeric", "alphanumeric only");
				return fieldValue;
			}
			break;
		case NUMERIC:
			if (!StringUtils.isNumeric(fieldValue)) {
				errors.rejectValue(field, "field.numeric", "numeric only");
				return fieldValue;
			}
			break;
		case HEXADECIMAL:
			if (!fieldValue.matches("[0-9a-fA-F]+")) {
				errors.rejectValue(field, "field.hexadecimal",
						"hexadecimal digit only");
				return fieldValue;
			}
			break;
		case VERSION_NAME:
			if (!fieldValue.matches("[0-9a-zA-Z_]+")) {
				errors.rejectValue(field, "field.invalid",
						"alphanumeric and _ only");
				return fieldValue;
			}
			break;
		}

		return fieldValue;
	}

	public static String validate(String fieldValue, String field, int minLength,
			int maxLength, boolean autoTrim, int stringType) throws ValidationException {

		if (autoTrim) {
			fieldValue = StringUtils.trim(fieldValue);
		}

		if (StringUtils.isEmpty(fieldValue)) {
			if (minLength == 0) {
				return fieldValue;
			}
			throw new ValidationException(field + " - required");
		}

		if (fieldValue.length() < minLength) {
			throw new ValidationException(field + " - min length: " + minLength);
		}
		if (fieldValue.length() > maxLength) {
			throw new ValidationException(field + " - max length: " + maxLength);
		}

		switch (stringType) {
		case ALPHANUMERIC:
			if (!StringUtils.isAlphanumeric(fieldValue)) {
				throw new ValidationException(field + " - alphanumeric only");
			}
			break;
		case NUMERIC:
			if (!StringUtils.isNumeric(fieldValue)) {
				throw new ValidationException(field + " - numeric only");
			}
			break;
		case HEXADECIMAL:
			if (!fieldValue.matches("[0-9a-fA-F]+")) {
				throw new ValidationException(field + " - hexadecimal digit only");
			}
			break;
		}

		return fieldValue;
	}

}
