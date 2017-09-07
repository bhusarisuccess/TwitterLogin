package Module;

import play.libs.oauth.OAuth;
import play.libs.oauth.OAuth.ConsumerKey;
import play.libs.oauth.OAuth.RequestToken;
import play.libs.oauth.OAuth.ServiceInfo;
import play.mvc.Http.Request;
/**
 * The class which do the initial authentication.
 */

public class TwitterAuthenticator {
    private static final String CACHE_TOKEN = "token.";

    public static final class Constants {
        private Constants() {
        }

        public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";

        public static final String OAUTH_TOKEN = "oauth_token";

        public static final String OAUTH_VERIFIER = "oauth_verifier";
    }

    /**
     * Authenticate the twitter signup.
     *
     * @return the string
     */
    public String authenticate() {
        final OAuth service = getOauthService();
        final String callbackURL = ApplicationUtil.getTwitterConfiguration(SettingKeys.REDIRECT_URI_HOST);
        final RequestToken response = service.retrieveRequestToken(callbackURL);
        putTokenInCache(response.token, response);
        // All good, we have the request token
        final String token = response.token;
        return service.redirectUrl(token);
    }

    /**
     * Retrieve access token.
     *
     * @param request the request
     * @return the request token
     */
    public RequestToken retrieveAccessToken(final Request request) {
        final OAuth service = getOauthService();
        final String verifier = request.getQueryString(Constants.OAUTH_VERIFIER);
        final String token = request.getQueryString(Constants.OAUTH_TOKEN);
        final RequestToken rtoken = (RequestToken) getTokenFromCache(token);
        return service.retrieveAccessToken(rtoken, verifier);
    }

    /**
     * Gets the oauth service for twitter.
     *
     * @return the oauth service
     */
    private OAuth getOauthService() {
        final ConsumerKey key = new ConsumerKey(ApplicationUtil.getTwitterConfiguration(SettingKeys.CONSUMER_KEY),
                ApplicationUtil.getTwitterConfiguration(SettingKeys.CONSUMER_SECRET));
        final String requestTokenURL = ApplicationUtil.getTwitterConfiguration(SettingKeys.REQUEST_TOKEN_URL);
        final String accessTokenURL = ApplicationUtil.getTwitterConfiguration(SettingKeys.ACCESS_TOKEN_URL);
        final String authorizationURL = ApplicationUtil.getTwitterConfiguration(SettingKeys.AUTHORIZATION_URL);
        final ServiceInfo info = new ServiceInfo(requestTokenURL, accessTokenURL, authorizationURL, key);
        return new OAuth(info, true);
    }

    @SuppressWarnings("unchecked")
    private <T> T getTokenFromCache(final String appender) {
        return (T) play.cache.Cache.get(CACHE_TOKEN + appender);
    }

    private <T> void putTokenInCache(final String appender, final T key) {
        play.cache.Cache.set(CACHE_TOKEN + appender, key);
    }
}
