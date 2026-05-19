package ebusiness.ejb;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class OrderException extends Exception {

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
