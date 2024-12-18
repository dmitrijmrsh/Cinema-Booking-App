package cinema.management.app.webclient.util;

public class SecurityUtil {

    private static String accessToken;
    private static String refreshToken;

    public synchronized static void setAccessToken(final String newAccessToken) {
        accessToken = newAccessToken;
    }

    public synchronized static void setRefreshToken(final String newRefreshToken) {
        refreshToken = newRefreshToken;
    }

    public synchronized static String getAccessToken() {
        return accessToken;
    }

    public synchronized static String getRefreshToken() {
        return refreshToken;
    }

}
