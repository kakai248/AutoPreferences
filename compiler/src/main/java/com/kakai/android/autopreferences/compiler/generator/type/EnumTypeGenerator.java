package com.kakai.android.autopreferences.compiler.generator.type;
import com.kakai.android.autopreferences.annotations.PreferenceGetter;
import com.kakai.android.autopreferences.compiler.AutoPreferencesAnnotatedClass;
import com.kakai.android.autopreferences.compiler.PreferenceAnnotatedField;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

class EnumTypeGenerator extends BaseTypeGenerator implements TypeGenerator {

    EnumTypeGenerator(AutoPreferencesAnnotatedClass clazz, PreferenceAnnotatedField field) {
        super(clazz, field);
    }

    @Override
    public MethodSpec generateGetter(FieldSpec context, FieldSpec helper) {
        MethodSpec.Builder builder = baseGetterBuilder()
                .addStatement("return $N.$L($N.getString($L), $L, $T.class)",
                        helper, getterHelperMethodName(), context, field.getStringRes(), field.getVariable(), field.getTypeName());

        if(clazz.annotateMethods()) {
            builder = builder.addAnnotation(generateAnnotation(PreferenceGetter.class));
        }

        return builder.build();
    }

    @Override
    protected String getterHelperMethodName() {
        return "getEnum";
    }

    @Override
    protected String setterHelperMethodName() {
        return "putEnum";
    }
}
