package exceptions;

public class QuantityLimitException extends RuntimeException{
    public QuantityLimitException(String message){
        super(message);
    }
}
