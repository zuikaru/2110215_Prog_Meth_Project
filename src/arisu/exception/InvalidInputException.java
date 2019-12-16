package arisu.exception;

import java.util.Map;

import javafx.scene.control.TextInputControl;

public class InvalidInputException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> messages;
	
	public InvalidInputException(Map<String, String> messages) {
		this.messages = messages;
	}
	
	public Map<String, String> getValidationMessages(){
		return messages;
	}

}
