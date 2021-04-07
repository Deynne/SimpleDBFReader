package io.github.deynne.dbf.exceptions;

public class LeituraIncorretaDeCampoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2607692859132548986L;

	public LeituraIncorretaDeCampoException(String message) {
		super(message);
	}
	
	public LeituraIncorretaDeCampoException(String message, Throwable e) {
		super(message,e);
	}
}
