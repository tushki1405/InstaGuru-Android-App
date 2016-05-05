package itp341.gupta.tushar.finalproject;

/**
 * Created by Tushar on 5/3/2016.
 */
public class Constants {
    //url constants
    public static final String API_URL = "http://tushki1405.pythonanywhere.com/";
    public static final String API_IMAGE = "http://tushki1405.pythonanywhere.com/pics/";
    public static final String URL_ADDUSER = "adduser";
    public static final String URL_GETMATCHES = "getmatches/";
    public static final String URL_GETSEARCH = "getsearch/";

    //constants for API parameters to be used in request
    public static final String API_NAME = "name";
    public static final String API_DOB = "dob";
    public static final String API_EMAIL = "email";
    public static final String API_MENTOR = "mentorfor";
    public static final String API_LEARN = "learn";
    public static final String API_ABOUT = "about";
    public static final String API_USERID = "userid";
    public static final String API_ID = "id";
    public static final String API_PICTURES = "pictures";
    public static final String API_RESPONSECODE = "code";
    public static final String API_RESPONSEMSG = "msg";
    public static final String API_MATCHDATA = "result";

    //intent constants
    public static final int IMAGE_1 = 1;
    public static final int IMAGE_2 = 2;
    public static final int IMAGE_3 = 3;
    public static final int IMAGE_4 = 4;
    public static final int INTENT_INFO = 5;
    public static final String INTENT_VAR_PERSON = "FinalProjectPersonIntent";

    //shared preferences
    public static final String PREF_FILENAME = "InstaGuruPrefFileName";
    public static final String PREF_USERID = "InstaGuruUserID";
}
