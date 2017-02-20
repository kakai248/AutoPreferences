package com.kakai.android.autopreferences.compiler.generator.type;

import android.annotation.SuppressLint;

import com.kakai.android.autopreferences.annotations.PreferenceGetter;
import com.kakai.android.autopreferences.annotations.PreferenceSetter;
import com.kakai.android.autopreferences.compiler.AutoPreferencesAnnotatedClass;
import com.kakai.android.autopreferences.compiler.PreferenceAnnotatedField;
import com.kakai.android.autopreferences.compiler.Utils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

abstract class BaseTypeGenerator implements TypeGenerator {

    static final String GETTER_PREFIX = "get";
    static final String SETTER_PREFIX = "set";
    static final String REMOVE_PREFIX = "remove";
    static final String CONTAINS_PREFIX = "has";
    static final String BOOLEAN_GETTER_PREFIX = "is";

    private static final String REMOVE_METHOD_NAME = "remove";
    private static final String CONTAINS_METHOD_NAME = "contains";

    AutoPreferencesAnnotatedClass clazz;
    PreferenceAnnotatedField field;

    BaseTypeGenerator(AutoPreferencesAnnotatedClass clazz, PreferenceAnnotatedField field) {
        this.clazz = clazz;
        this.field = field;
    }

    @Override
    public MethodSpec generateGetter(FieldSpec context, FieldSpec helper) {
        MethodSpec.Builder builder = baseGetterBuilder()
                .addStatement("return $N.$L($N.getString($L), $L)",
                        helper, getterHelperMethodName(), context, field.getStringRes(), field.getVariable());

        if (clazz.annotateMethods()) {
            builder = builder.addAnnotation(generateAnnotation(PreferenceGetter.class));
        }

        return builder.build();
    }

    @Override
    public MethodSpec generateSetter(FieldSpec context, FieldSpec helper) {
        MethodSpec.Builder builder = baseSetterBuilder()
                .addStatement("$N.$L($N.getString($L), $L)",
                        helper, setterHelperMethodName(), context, field.getStringRes(), field.getVariable());

        if (clazz.annotateMethods()) {
            builder = builder.addAnnotation(generateAnnotation(PreferenceSetter.class));
        }

        return builder.build();
    }

    @Override
    public MethodSpec generateRemove(FieldSpec context, FieldSpec helper) {
        return baseRemoveBuilder()
                .addStatement("$N.$L($N.getString($L))",
                        helper, removeHelperMethodName(), context, field.getStringRes())
                .build();
    }

    @Override
    public MethodSpec generateContains(FieldSpec context, FieldSpec helper) {
        return baseContainsBuilder()
                .addStatement("return $N.$L($N.getString($L))",
                        helper, containsHelperMethodName(), context, field.getStringRes())
                .build();
    }

    @Override
    public List<MethodSpec> generateMethods(FieldSpec context, FieldSpec helper) {
        List<MethodSpec> methods = new ArrayList<>(2);
        methods.add(generateGetter(context, helper));
        methods.add(generateSetter(context, helper));

        if (field.generateRemove()) {
            methods.add(generateRemove(context, helper));
        }

        if (field.generateContains()) {
            methods.add(generateContains(context, helper));
        }

        return methods;
    }

    protected abstract String getterHelperMethodName();

    protected abstract String setterHelperMethodName();

    protected String removeHelperMethodName() {
        return REMOVE_METHOD_NAME;
    }

    protected String containsHelperMethodName() {
        return CONTAINS_METHOD_NAME;
    }

    MethodSpec.Builder baseGetterBuilder() {
        return MethodSpec.methodBuilder(getterMethodName())
                .addModifiers(Modifier.PUBLIC)
                .returns(field.getTypeName());
    }

    MethodSpec.Builder baseSetterBuilder() {
        return MethodSpec.methodBuilder(setterMethodName())
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(field.getTypeName(), field.getName().toString());
    }

    MethodSpec.Builder baseRemoveBuilder() {
        return MethodSpec.methodBuilder(removeMethodName())
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
    }

    MethodSpec.Builder baseContainsBuilder() {
        return MethodSpec.methodBuilder(containsMethodName())
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class);
    }

    AnnotationSpec generateAnnotation(Class<?> clazz) {
        return AnnotationSpec.builder(clazz)
                .addMember("stringRes", "$L", field.getStringRes())
                .addMember("tag", "$L", field.getTag())
                .build();
    }

    private String getterMethodName() {
        return methodName(field.omitGetterPrefix() ? "" : getterPrefix());
    }

    private String setterMethodName() {
        return methodName(setterPrefix());
    }

    private String removeMethodName() {
        return methodName(removePrefix());
    }

    private String containsMethodName() {
        return methodName(containsPrefix());
    }

    @SuppressLint("NewApi")
    private String methodName(String prefix) {
        String name = field.getName().toString();
        return !prefix.isEmpty() ? prefix + Utils.lowerToUpperCamel(name) : name;
    }

    protected String getterPrefix() {
        return GETTER_PREFIX;
    }

    protected String setterPrefix() {
        return SETTER_PREFIX;
    }

    protected String removePrefix() {
        return REMOVE_PREFIX;
    }

    protected String containsPrefix() {
        return CONTAINS_PREFIX;
    }
}
