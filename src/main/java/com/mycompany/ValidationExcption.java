package com.mycompany;

public class ValidationExcption extends RuntimeException {

	private static final long serialVersionUID = 5733667809238655138L;

	public ValidationExcption(String message) {
		super(message);
	}

}
