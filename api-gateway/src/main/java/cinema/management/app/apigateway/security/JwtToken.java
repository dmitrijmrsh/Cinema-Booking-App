package cinema.management.app.apigateway.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class JwtToken extends AbstractAuthenticationToken {

    private final String token;
    private final UserDetails userDetails;

    private JwtToken(String token, UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.token = token;
        this.userDetails = userDetails;
    }

    public static JwtToken of(final String token, final UserDetails userDetails) {
        return new JwtToken(token, userDetails);
    }

    Authentication withAuthenticated(boolean isAuthenticated) {
        JwtToken cloned = JwtToken.of(token, userDetails);
        cloned.setAuthenticated(isAuthenticated);
        return cloned;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JwtToken t)) {
            return false;
        }

        if (this.getToken() == null && t.getToken() != null) {
            return false;
        }

        if (this.getToken() != null && !this.getToken().equals(t.getToken())) {
            return false;
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int code = super.hashCode();

        if (this.getToken() != null) {
            code ^= this.getToken().hashCode();
        }

        return code;
    }
}
