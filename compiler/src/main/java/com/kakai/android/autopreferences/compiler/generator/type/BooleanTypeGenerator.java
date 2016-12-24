package com.kakai.android.autopreferences.compiler.generator.type;

import com.kakai.android.autopreferences.compiler.AutoPreferencesAnnotatedClass;
import com.kakai.android.autopreferences.compiler.PreferenceAnnotatedField;

class BooleanTypeGenerator extends BaseTypeGenerator implements TypeGenerator {

    BooleanTypeGenerator(AutoPreferencesAnnotatedClass clazz, PreferenceAnnotatedField field) {
        super(clazz, field);
    }

    @Override
    protected String getterMethodName() {
        return "getBoolean";
    }

    @Override
    protected String setterMethodName() {
        return "putBoolean";
    }

    protected String getterPrefix() {
        return BOOLEAN_GETTER_PREFIX;
    }
}
