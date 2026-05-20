package ebusiness.ejb;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ProductException extends Exception {
    public ProductException(String message) {
        super(message);
    }

    public ProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
