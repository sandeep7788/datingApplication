package com.love.loveme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.love.loveme.models.CountryRoot;
import com.love.loveme.models.SettingRoot;
import com.love.loveme.models.User;
import com.love.loveme.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        try {
            this.pref = context.getSharedPreferences(Const.PREF_NAME, MODE_PRIVATE);
            this.editor = this.pref.edit();
        }catch (Exception e) {

        }
    }

    public void saveStringValue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return pref.getString(key, "");
    }

    public void saveBooleanValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        return pref.getBoolean(key, false);
    }

    public void saveUser(User user) {
        editor.putString(Const.USER, new Gson().toJson(user));
        editor.apply();
    }

    public User getUser() {
        String userString = pref.getString(Const.USER, "");
        if (!userString.isEmpty()) {
            return new Gson().fromJson(userString, User.class);
        }
        return null;
    }

    public void saveSettings(SettingRoot.Data settings) {
        editor.putString(Const.SETTINGS, new Gson().toJson(settings));
        editor.apply();
    }

    public SettingRoot.Data getSettings() {
        String userString = pref.getString(Const.SETTINGS, "");
        if (!userString.isEmpty()) {
            return new Gson().fromJson(userString, SettingRoot.Data.class);
        }
        return null;
    }

    public String getFBInt() {
        SettingRoot.Data data = getSettings();
        if (data != null) {
            if (data.getFbInterestialId() != null && !data.getFbInterestialId().isEmpty()) {
                return data.getFbInterestialId();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getFBNative() {
        SettingRoot.Data data = getSettings();
        if (data != null) {
            if (data.getFbRewardedId() != null && !data.getFbRewardedId().isEmpty()) {
                return data.getFbRewardedId();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }


    public String getAdmobInt() {
        SettingRoot.Data data = getSettings();
        if (data != null) {
            if (data.getAdmobInterestialId() != null && !data.getAdmobInterestialId().isEmpty()) {
                return data.getAdmobInterestialId();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getAdmobBanner() {
        SettingRoot.Data data = getSettings();
        if (data != null) {
            if (data.getAdmobBannerId() != null && !data.getAdmobBannerId().isEmpty()) {
                return data.getAdmobBannerId();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getFbBanner() {
        SettingRoot.Data data = getSettings();
        if (data != null) {
            if (data.getFbBannerId() != null && !data.getFbBannerId().isEmpty()) {
                return data.getFbBannerId();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getAdmobNative() {
        SettingRoot.Data data = getSettings();
        if (data != null) {
            if (data.getAdmobPublisherId() != null && !data.getAdmobPublisherId().isEmpty()) {
                return data.getAdmobPublisherId();
            } else {
                return "ca-app-pub-3940256099942544/2247696110";
            }
        } else {
            return "ca-app-pub-3940256099942544/2247696110";
        }
    }

    public String getAdmobReward() {
        SettingRoot.Data data = getSettings();
        if (data != null) {
            if (data.getAdmobRewardedId() != null && !data.getAdmobRewardedId().isEmpty()) {
                return data.getAdmobRewardedId();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }


    public void saveCountry(List<CountryRoot.DataItem> list) {

        editor.putString(Const.COUNTRYLIST, new Gson().toJson(list));
        editor.apply();
    }

    public List<CountryRoot.DataItem> getCountry() {
        String countries = pref.getString(Const.COUNTRYLIST, "");
        if (!countries.isEmpty()) {
            return new Gson().fromJson(countries, new TypeToken<ArrayList<CountryRoot.DataItem>>() {
            }.getType());
        }
        return new ArrayList<>();
    }
}
