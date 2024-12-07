package cinema.management.app.authservice.constant;

import cinema.management.app.authservice.exception.UnsupportedRoleException;

public enum Roles {

    ADMIN,
    MANAGER,
    USER;

    public String getRoleInString() {
        return name();
    }

    public static Roles of(final String roleInString) {
        return switch(roleInString) {
            case "USER", "ROLE_USER" -> USER;
            case "MANAGER", "ROLE_MANAGER" -> MANAGER;
            case "ADMIN", "ROLE_ADMIN" -> ADMIN;
            default -> throw new UnsupportedRoleException(roleInString);
        };
    }

}
