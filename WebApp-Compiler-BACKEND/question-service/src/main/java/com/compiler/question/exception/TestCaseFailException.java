package com.compiler.question.exception;

public class TestCaseFailException extends Exception {

	public TestCaseFailException(String message) {
		super(message);
	}

	public TestCaseFailException() {
		super();
	}

	public TestCaseFailException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TestCaseFailException(String message, Throwable cause) {
		super(message, cause);
	}

	public TestCaseFailException(Throwable cause) {
		super(cause);
	}

}
