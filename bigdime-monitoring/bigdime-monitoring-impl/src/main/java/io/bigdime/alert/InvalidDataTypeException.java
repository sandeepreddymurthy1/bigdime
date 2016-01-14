package io.bigdime.alert;

public class InvalidDataTypeException extends AlertException  {

	public InvalidDataTypeException(String message) {
		super(message);  
	}

	public InvalidDataTypeException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
