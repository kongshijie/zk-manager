package com.dj.zk.manager.exceptions;

public class GeneralException extends BaseException {

	private static final long serialVersionUID = 1L;

	public GeneralException() {
		super();
	}

	public GeneralException(String message) {
		super(message);
	}

	public GeneralException(Throwable cause) {
		super(cause);
	}

	public GeneralException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneralException(String message, Throwable cause, String code,
			Object[] values) {
		super(message, cause, code, values);
	}
}
