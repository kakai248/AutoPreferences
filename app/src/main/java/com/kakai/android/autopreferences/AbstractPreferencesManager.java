package com.kakai.android.autopreferences;

import com.kakai.android.autopreferences.annotations.AutoPreferences;
import com.kakai.android.autopreferences.annotations.Preference;

import java.util.HashSet;
import java.util.Set;

@AutoPreferences(annotateMethods = true)
public abstract class AbstractPreferencesManager {

    public static final int GROUP1 = 1;
    public static final int GROUP2 = 2;

    @Preference(stringRes = R.string.test_boolean, omitGetterPrefix = true, tag = GROUP1)
    final boolean testBoolean = true;

    @Preference(stringRes = R.string.test_string)
    final String testString = "stuff";

    @Preference(stringRes = R.string.test_int, remove = true, contains = true, tag = GROUP1)
    final int testInt = 10;

    @Preference(stringRes = R.string.test_float, tag = GROUP2)
    final float testFloat = 5.0f;

    @Preference(stringRes = R.string.test_long, tag = GROUP1)
    final long testLong = 20;

    @Preference(stringRes = R.string.test_stringset)
    final Set<String> testStringSet;

    @Preference(stringRes = R.string.test_enum)
    final TestEnum testEnum = TestEnum.TEST_2;

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
