package Exceptions;

public class UserServiceException extends RuntimeException {

    public static final long SerialVersionUID = 1L;

    public UserServiceException(String message) {
        super(message);
    }
    
    
}
