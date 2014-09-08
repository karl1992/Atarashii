package net.somethingdreadful.MAL.api.response;

import android.database.Cursor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class User {
    private String name;
    private String friend_since;
    private Profile profile;

    public static User fromCursor(Cursor c) {
        return fromCursor(c, false);
    }

    public static User fromCursor(Cursor c, boolean friendDetails) {
        User result = new User();

        List<String> columnNames = Arrays.asList(c.getColumnNames());
        result.setName(c.getString(columnNames.indexOf("username")));

        result.setProfile(Profile.fromCursor(c, friendDetails));
        return result;
    }

    public static boolean isDeveloperRecord(String name) {
        String[] developers = {
                "ratan12",
                "animasa",
                "motokochan",
                "apkawa",
                "d-sko"
        };
        return Arrays.asList(developers).contains(name.toLowerCase(Locale.US));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendSince() {
        return friend_since;
    }

    public void setFriendSince(String friend_since) {
        this.friend_since = friend_since;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
