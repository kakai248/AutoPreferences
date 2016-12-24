package com.kakai.android.autopreferences;

import com.kakai.android.autopreferences.annotations.AutoPreferences;
import com.kakai.android.autopreferences.annotations.Preference;

import java.util.HashSet;
import java.util.Set;

@AutoPreferences(annotate = true)
public abstract class AbstractPreferencesManager {

    public static final int GROUP1 = 1;
    public static final int GROUP2 = 2;

    @Preference(stringRes = R.string.test_boolean, tag = GROUP1)
    boolean testBoolean = true;

    @Preference(stringRes = R.string.test_string)
    String testString = "stuff";

    @Preference(stringRes = R.string.test_int, remove = true, contains = true, tag = GROUP1)
    int testInt = 10;

    @Preference(stringRes = R.string.test_float, tag = GROUP2)
    float testFloat = 5.0f;

    @Preference(stringRes = R.string.test_long, tag = GROUP1)
    long testLong = 20;

    @Preference(stringRes = R.string.test_stringset)
    Set<String> testStringSet;

    @Preference(stringRes = R.string.test_enum)
    TestEnum testEnum = TestEnum.TEST_2;

    int shouldNotShow = 0;

    public AbstractPreferencesManager() {
        testStringSet = new HashSet<>();
        testStringSet.add("Test 1");
        testStringSet.add("Test 2");
    }

    enum TestEnum {
        TEST_1,
        TEST_2,
        TEST_3
    }
}
