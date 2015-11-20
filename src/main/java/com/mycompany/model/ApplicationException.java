package com.mycompany.model;

public class ApplicationException extends RuntimeException {

	public ApplicationException(String message) {
		super(message);
	}
	public ApplicationException(){
		super("Unknown Application Exception!");
	}
	private static final long serialVersionUID = -1390149202370021447L;


}
