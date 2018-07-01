package org.dreamcafe.sljc.exception;

public class PropertiesException extends RuntimeException {
    private PropertiesException(String message) {
        super(message);
    }

    public static PropertiesException asBlankValuePropertiesException(String propertyName) {
        StringBuilder message = new StringBuilder(100);
        message.append("Missing value or the value is blank: ").append(propertyName);
        message.trimToSize();
        return new PropertiesException(message.toString());
    }

    public static PropertiesException asNumericValuePropertiesException(String propertyName) {
        StringBuilder message = new StringBuilder(100);
        message.append("The value is not a numeric: ").append(propertyName);
        message.trimToSize();
        return new PropertiesException(message.toString());
    }
}
