package exception;

public class ResourceUnavailableException extends Exception{
    // constructor
    public ResourceUnavailableException(String message){
        //call super class Exception and pass message to it
        super(message);
    }

}