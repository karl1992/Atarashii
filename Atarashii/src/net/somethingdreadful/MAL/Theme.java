package net.somethingdreadful.MAL;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import net.somethingdreadful.MAL.account.AccountService;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class Theme extends Application {

    public static boolean darkTheme;
    Locale locale;
    Configuration config;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        PrefManager.create(getApplicationContext());
        AccountService.create(getApplicationContext());

        locale = PrefManager.getLocale();
        darkTheme = PrefManager.getDarkTheme();
        config = new Configuration();
        config.locale = locale;
        setLanguage(); //Change language when it is started
        Crashlytics.setString("Language", locale.toString());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLanguage(); //Change language after orientation.
    }

    public void setLanguage() {
        Resources res = getBaseContext().getResources();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    /**
     * This will apply the right theme and background.
     *
     * @param activity The activity which should be themed
     * @param view     The main view id
     * @param card     If the contents contains a card
     */
    public static void setTheme(Activity activity, int view, boolean card) {
        if (view != 0)
            activity.setContentView(view);
        if (darkTheme) {
            activity.setTheme(R.style.AtarashiiDarkBg);
            activity.getWindow().getDecorView().setBackgroundColor(activity.getResources().getColor(card ? R.color.bg_dark_card : R.color.bg_dark));
        } else {
            activity.getWindow().getDecorView().setBackgroundColor(activity.getResources().getColor(card ? R.color.bg_light_card : R.color.bg_light));
        }
    }

    /**
     * Set a background with the default card theme.
     *
     * @param c    The context
     * @param view The view which should use this drawable
     */
    public static void setBackground(Context c, View view) {
        setBackground(c, view, getDrawable());
    }

    /**
     * Set the background of a view.
     *
     * @param c    The context
     * @param view The view which should use this drawable
     * @param id   The drawable/color id of the wanted color/drawable
     */
    public static void setBackground(Context c, View view, int id) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(c.getResources().getDrawable(id));
        } else {
            view.setBackground(c.getResources().getDrawable(id));
        }
    }

    /**
     * Get the default drawable.
     *
     * @return int The id of the default card drawable
     */
    private static int getDrawable() {
        return darkTheme ? R.drawable.highlite_details_dark : R.drawable.highlite_details;
    }

    /**
     * Create a snackbar which handles the queue.
     *
     * @param activity    The activity where the snackbar should be shown
     * @param stringResID The string resource ID
     */
    public static void Snackbar(Activity activity, int stringResID) {
        SnackbarManager.show(Snackbar.with(activity).text(activity.getString(stringResID)));
    }

    /**
     * Converts a score to the display format
     *
     * @param score The score that should be converted
     * @return The string to display
     */
    public static String getDisplayScore(float score) {
        switch (PrefManager.getScoreType()) {
            case 1:
                Double score1 = AccountService.isMAL() ? score : Math.floor(score / 10);
                return score1 > 0.0 ? String.format("%.0f", score1) : "?";
            case 2:
                return score > 0 ? Integer.toString((int) score) : "?";
            case 3:
                if (score <= 0)
                    return "?";
                else if (score <= 29)
                    return "1";
                else if (score <= 49)
                    return "2";
                else if (score <= 69)
                    return "3";
                else if (score <= 89)
                    return "4";
                else
                    return "5";
            case 4:
                if (score <= 0)
                    return "?";
                else if (score <= 30)
                    return ":(";
                else if (score <= 60)
                    return ":|";
                else
                    return ":)";
            case 5:
                Double score5 = Math.floor(score / 10);
                return score5 > 0.0 ? String.format("%.1f", score5) : "?";
            default:
                return "?";
        }
    }

    /**
     * Converts a score to the raw format
     *
     * @param score The score that should be converted
     * @return The raw integer
     */
    public static int getRawScore(String score) {
        if (score.equals(""))
            return 0;
        switch (PrefManager.getScoreType()) {
            case 1:
                return TextUtils.isDigitsOnly(score) ? (int) (Double.parseDouble(score) * 10) : 0;
            case 2:
                return TextUtils.isDigitsOnly(score) ? Integer.parseInt(score) : 0;
            case 3:
                return TextUtils.isDigitsOnly(score) ? Integer.parseInt(score) * 20 : 0;
            case 4:
                switch (score) {
                    case ":(":
                        return 33;
                    case ":|":
                        return 67;
                    case ":)":
                        return 100;
                    default:
                        return 0;
                }
            case 5:
                return TextUtils.isDigitsOnly(score) ? (int) (Double.parseDouble(score) * 10) : 0;
            default:
                return TextUtils.isDigitsOnly(score) ? (int) (Double.parseDouble(score) * 10) : 0;
        }
    }
}
