package com.kakai.android.autopreferences.compiler.generator.type;

import com.kakai.android.autopreferences.compiler.AutoPreferencesAnnotatedClass;
import com.kakai.android.autopreferences.compiler.PreferenceAnnotatedField;

class LongTypeGenerator extends BaseTypeGenerator implements TypeGenerator {

    LongTypeGenerator(AutoPreferencesAnnotatedClass clazz, PreferenceAnnotatedField field) {
        super(clazz, field);
    }

    @Override
    protected String getterHelperMethodName() {
        return "getLong";
    }

    @Override
    protected String setterHelperMethodName() {
        return "putLong";
    }
}
