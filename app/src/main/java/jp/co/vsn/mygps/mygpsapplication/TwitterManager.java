package jp.co.vsn.mygps.mygpsapplication;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

public class TwitterManager {
    private static final String CONSUMER_KEY = "取得したConcumer keyを指定";
    private static final String CONSUMER_SECRET = "取得したConcumer secretを指定";
    public static final Uri CALLBACK_URL = Uri.parse("gpstwitter://OAuthCallBack");
    private static final String PREF_TWITTER_TOKEN = "TWITTER_TOKEN";
    private static final String PREF_TWITTER_SEACRET_TOKEN = "TWITTER_SEACRET_TOKEN";
    private static final String PREF_TAG = "GpsStateList";
    private Context mContext;
    private Twitter mTwitter;
    private RequestToken mRequestToken;
    private SharedPreferences mPreferences;

    public TwitterManager(Context context) {
        this.mContext = context;
        this.mTwitter = new TwitterFactory().getInstance();
        this.mTwitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        this.mPreferences = context.getSharedPreferences(PREF_TAG, Context.MODE_PRIVATE);
    }

    public String getOAuthUrl() {
        try {
            mRequestToken = mTwitter.getOAuthRequestToken(CALLBACK_URL.toString());
            return mRequestToken.getAuthorizationURL();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isLogin() {
        String token = getPreferences(PREF_TWITTER_TOKEN, null);
        String secretToken = getPreferences(PREF_TWITTER_SEACRET_TOKEN, null);
        return token != null && secretToken != null;
    }

    public void setAuthKey() {
        String token = getPreferences(PREF_TWITTER_TOKEN, null);
        String secretToken = getPreferences(PREF_TWITTER_SEACRET_TOKEN, null);
        mTwitter.setOAuthAccessToken(new AccessToken(token, secretToken));
    }

    public boolean sendTweet(String tweetMsg) {
        try {
            setAuthKey();
            mTwitter.updateStatus(tweetMsg);
            return true;
        } catch (TwitterException e) {
            return false;
        }
    }

    public void authrize(Intent data) {
        Uri uri = data.getData();
        if (uri != null && CALLBACK_URL.getScheme().equals(uri.getScheme())) {
            String oauthVerifier = uri.getQueryParameter("oauth_verifier");
            if (oauthVerifier != null) {
                try {
                    AccessToken accessToken = mTwitter.getOAuthAccessToken(mRequestToken, oauthVerifier);
                    setPreferences(PREF_TWITTER_TOKEN, accessToken.getToken());
                    setPreferences(PREF_TWITTER_SEACRET_TOKEN, accessToken.getTokenSecret());
                    Toast.makeText(mContext, R.string.twitter_login_success, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(mContext, R.string.twitter_login_fail, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void setPreferences(String key, String value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreferences(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }
}
