package com.alvastudio.election2.Classes;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Utils {
    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static String getUniDeviceId() {
        long currentTime = System.currentTimeMillis();
        Random random = new Random();
        int randomNumber = random.nextInt(1000);
        String uniqueId = currentTime + "_" + randomNumber;
        return uniqueId;
    }

    public static String getDeviceName() {
        String deviceName = Build.MANUFACTURER + " " + Build.MODEL;
        return deviceName;
    }
}
