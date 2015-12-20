package in.ngrail.NGERail;

/**
 * Created by kiran on 15-12-2015.
 */
public class Config {


    // CONSTANTS
    static final String YOUR_SERVER_URL =
            "http://api.ngrail.in/gcm/register.php";
    static final String YOUR_SERVER_URL1 =
            "http://api.ngrail.in/gcm/unregister.php";

    // Google project id
    static final String GOOGLE_SENDER_ID = "625044450121";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "GCM Android Example";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.androidexample.gcm.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

}
