package pl.seb.czech.library.service.exceptions;

public class DataNotFoundException extends RuntimeException {
   

    public DataNotFoundException(String whatWasLookingFor, String ... parameters) {
        super(createErrorMessage(whatWasLookingFor, parameters));
    }

    private static String createErrorMessage(String whatWasLookingFor, String[] parameters) {
        StringBuilder message = new StringBuilder();
        message.append("No ").append(whatWasLookingFor).append(" was found with ");
        if(parameters.length > 1) {
            message.append(" these parameters: ");
        } else {
            message.append(" this parameter: ");
        }
        
        for (String parameter : parameters) {
            message.append(parameter).append(" ");
        }
        return message.toString();
    }
}
