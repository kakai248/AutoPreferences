package com.kakai.android.autopreferences.compiler.generator.type;

import com.kakai.android.autopreferences.compiler.AutoPreferencesAnnotatedClass;
import com.kakai.android.autopreferences.compiler.PreferenceAnnotatedField;

class StringTypeGenerator extends BaseTypeGenerator implements TypeGenerator {

    StringTypeGenerator(AutoPreferencesAnnotatedClass clazz, PreferenceAnnotatedField field) {
        super(clazz, field);
    }

    @Override
    protected String getterMethodName() {
        return "getString";
    }

    @Override
    protected String setterMethodName() {
        return "putString";
    }
}
