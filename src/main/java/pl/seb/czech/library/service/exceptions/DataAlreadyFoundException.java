package pl.seb.czech.library.service.exceptions;

public class DataAlreadyFoundException extends RuntimeException {

    public DataAlreadyFoundException() {
    }

    public DataAlreadyFoundException(String whatWasLookingFor, String ... parameters) {
        super(createErrorMessage(whatWasLookingFor, parameters));
    }
    

    private static String createErrorMessage(String whatWasLookingFor, String[] parameters) {
        StringBuilder message = new StringBuilder();
        message.append("The ").append(whatWasLookingFor).append(" with ");
        if(parameters.length > 1) {
            message.append("these parameters: ");
        } else {
            message.append("this parameter: ");
        }

        for (String parameter : parameters) {
            message.append(parameter).append(" ");
        }
        
        
        message.append("is already in database");
        return message.toString();
    }
    
    
}
