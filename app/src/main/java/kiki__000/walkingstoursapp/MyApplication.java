package kiki__000.walkingstoursapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kiki__000 on 16-Jun-15.
 */
public class MyApplication {

    public static String language;
    private static Pattern pattern;
    private static Matcher matcher;
    //Email Pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    /**
     * handles the language
     * */
    public static void updateLanguage(Context context){

        //get the language of sharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("language", Context.MODE_PRIVATE);
        String lang = prefs.getString("lang", "");
        Log.i("langSP", lang);


        if (TextUtils.isEmpty(lang)) {
            //set the default language
            Locale current = context.getResources().getConfiguration().locale;
            Log.i("localeHERE", current.toString());
            if (current.equals("el")){
                Locale locale = new Locale("el");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, null);
                DBController.walks = "walksG";
                DBController.stations = "stationsG";
                language = "el";
            }
            else{
                Locale locale = new Locale("en_US");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, null);
                DBController.walks = "walksE";
                DBController.stations = "stationsE";
                language = "en";
            }

        }else{
            if (lang.equals("en_US")) {
                Log.i("here1", "edw1");
                Locale locale = new Locale("en_US");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, null);
                DBController.walks = "walksE";
                DBController.stations = "stationsE";
                language = "en";
            } else if (lang.equals("el")) {
                Log.i("here2", "edw2");
                Locale locale = new Locale("el");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, null);
                DBController.walks = "walksG";
                DBController.stations = "stationsG";
                language = "el";
            }

        }
    }

    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
