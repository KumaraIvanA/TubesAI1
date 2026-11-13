package com.firestationga.cli;

/**
 * Exception yang menandakan adanya argumen CLI yang ilegal.
 *
 * @author Kumara Ivan Anuraga
 */
public class InvalidArgumentException extends Exception {
	public InvalidArgumentException(String message) {
		super(message);
	}
}
