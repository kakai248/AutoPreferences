package com.kakai.android.autopreferences.backup;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakai.android.autopreferences.annotations.Preference;
import com.kakai.android.autopreferences.annotations.PreferenceGetter;
import com.kakai.android.autopreferences.annotations.PreferenceSetter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BackupHelper {

    private Context context;

    private BackupHelper(Context context) {
        this.context = context;
    }

    public static BackupHelper with(Context context) {
        return new BackupHelper(context);
    }

    public Map<String, Object> read(Object obj, Integer... tags) {
        if(tags.length == 0) tags = new Integer[] {Preference.ALL};

        List<Method> methods = new ArrayList<>(Arrays.asList(obj.getClass().getDeclaredMethods()));
        HashSet<Integer> tagSet = getTagSet(tags);

        Map<String, Object> map = new HashMap<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(PreferenceGetter.class)) {
                PreferenceGetter annotation = method.getAnnotation(PreferenceGetter.class);

                if(tagSet.contains(Preference.ALL) || tagSet.contains(annotation.tag())) {
                    try {
                        map.put(context.getString(annotation.stringRes()), method.invoke(obj));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    public void write(Object obj, Map<String, Object> values) {
        List<Method> methods = new ArrayList<>(Arrays.asList(obj.getClass().getDeclaredMethods()));

        for (Method method : methods) {
            if (method.isAnnotationPresent(PreferenceSetter.class)) {
                PreferenceSetter annotation = method.getAnnotation(PreferenceSetter.class);

                String key = context.getString(annotation.stringRes());
                if(values.containsKey(key)) {
                    try {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if(parameterTypes.length < 1) continue;

                        Class<?> type = parameterTypes[0];

                        Object param;

                        // Special case to handle enums
                        if(Enum.class.isAssignableFrom(type)) {
                            // I'm sorry!
                            // http://stackoverflow.com/a/1901275
                            Class<Enum> tClass = (Class<Enum>) ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[0];
                            String value = (String) values.get(key);
                            param = value != null ? Enum.valueOf(tClass, value) : null;
                        }

                        // Special case to handle floats
                        else if(type == Float.class || type == float.class) {
                            param = ((Double) values.get(key)).floatValue();
                        }

                        // Special case to handle string sets
                        else if(type == Set.class) {
                            param = new HashSet<>((List<String>) values.get(key));
                        }

                        // Every other type
                        else {
                            param = values.get(key);
                        }

                        method.invoke(obj, param);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void backup(File f, Object obj, Integer... tags) throws IOException {
        String json = backup(obj, tags);
        FileUtils.writeToFile(f, json);
    }

    public String backup(Object obj, Integer... tags) throws JsonProcessingException {
        Map<String, Object> map = read(obj, tags);
        return new ObjectMapper().writeValueAsString(map);
    }

    public void restore(File f, Object obj) throws IOException {
        String json = FileUtils.readFromFile(f);
        restore(json, obj);
    }

    public void restore(String json, Object obj) throws IOException {
        Map<String, Object> map = new ObjectMapper().readValue(json, new TypeReference<HashMap<String, Object>>(){});
        write(obj, map);
    }

    private HashSet<Integer> getTagSet(Integer... tags) {
        return new HashSet<>(Arrays.asList(tags));
    }
}
