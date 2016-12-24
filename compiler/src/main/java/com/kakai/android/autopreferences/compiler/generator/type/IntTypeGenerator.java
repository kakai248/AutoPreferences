package com.kakai.android.autopreferences.compiler.generator.type;

import com.kakai.android.autopreferences.compiler.AutoPreferencesAnnotatedClass;
import com.kakai.android.autopreferences.compiler.PreferenceAnnotatedField;

class IntTypeGenerator extends BaseTypeGenerator implements TypeGenerator {

    IntTypeGenerator(AutoPreferencesAnnotatedClass clazz, PreferenceAnnotatedField field) {
        super(clazz, field);
    }

    @Override
    protected String getterMethodName() {
        return "getInt";
    }

    @Override
    protected String setterMethodName() {
        return "putInt";
    }
}
