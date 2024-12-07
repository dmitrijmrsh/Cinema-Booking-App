package cinema.management.app.authservice.exception;

public class UnsupportedRoleException extends RuntimeException {

    private String message = "Role with name \"%s\" is not supported";

    public UnsupportedRoleException(final String unsupportedRoleInStr) {
        message = message.formatted(unsupportedRoleInStr);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
