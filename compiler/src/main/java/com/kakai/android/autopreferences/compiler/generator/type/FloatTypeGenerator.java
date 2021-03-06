package com.kakai.android.autopreferences.compiler.generator.type;

import com.kakai.android.autopreferences.compiler.AutoPreferencesAnnotatedClass;
import com.kakai.android.autopreferences.compiler.PreferenceAnnotatedField;

class FloatTypeGenerator extends BaseTypeGenerator implements TypeGenerator {

    FloatTypeGenerator(AutoPreferencesAnnotatedClass clazz, PreferenceAnnotatedField field) {
        super(clazz, field);
    }

    @Override
    protected String getterHelperMethodName() {
        return "getFloat";
    }

    @Override
    protected String setterHelperMethodName() {
        return "putFloat";
    }
}
