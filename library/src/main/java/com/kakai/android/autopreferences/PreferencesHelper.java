package com.kakai.android.autopreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;
import java.util.Set;

public class PreferencesHelper {

    private static PreferencesHelper instance;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private PreferencesHelper(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    private PreferencesHelper(Context context, String name, int mode) {
        sharedPreferences = context.getSharedPreferences(name, mode);
        editor = sharedPreferences.edit();
    }

    public static PreferencesHelper with(Context context) {
        if (instance == null) {
            instance = new Builder(context, null, -1).build();
        }
        return instance;
    }

    public static PreferencesHelper with(Context context, String name, int mode) {
        if (instance == null) {
            instance = new Builder(context, name, mode).build();
        }
        return instance;
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public void putString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value).apply();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value).apply();
    }

    public void putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value).apply();
    }

    public <T extends Enum<T>> void putEnum(String key, T value) {
        editor.putString(key, value != null ? value.name() : "").apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return sharedPreferences.getStringSet(key, defValue);
    }

    public <T extends Enum<T>> T getEnum(String key, T defValue, Class<T> type) {
        String name = sharedPreferences.getString(key, defValue != null ? defValue.name() : null);
        return name != null && !name.isEmpty() ? Enum.valueOf(type, name) : null;
    }

    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    public void remove(String key) {
        editor.remove(key).apply();
    }

    public void clear() {
        editor.clear().apply();
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    private static class Builder {
        private final Context context;
        private final int mode;
        private final String name;

        Builder(Context context, String name, int mode) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context;
            this.name = name;
            this.mode = mode;
        }

        PreferencesHelper build() {
            if (mode == -1 || name == null) {
                return new PreferencesHelper(context);
            }
            return new PreferencesHelper(context, name, mode);
        }
    }
}
