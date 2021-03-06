package com.kakai.android.autopreferences.compiler.generator.type;

import com.kakai.android.autopreferences.compiler.AutoPreferencesAnnotatedClass;
import com.kakai.android.autopreferences.compiler.PreferenceAnnotatedField;

class StringSetTypeGenerator extends BaseTypeGenerator implements TypeGenerator {

    StringSetTypeGenerator(AutoPreferencesAnnotatedClass clazz, PreferenceAnnotatedField field) {
        super(clazz, field);
    }

    @Override
    protected String getterHelperMethodName() {
        return "getStringSet";
    }

    @Override
    protected String setterHelperMethodName() {
        return "putStringSet";
    }
}
