package service.model;

public class NoMatchTableHeaderException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public NoMatchTableHeaderException(String message) {
		super(message);
	}
}
