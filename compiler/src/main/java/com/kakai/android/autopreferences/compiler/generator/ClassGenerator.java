package com.kakai.android.autopreferences.compiler.generator;

import android.content.Context;

import com.kakai.android.autopreferences.PreferencesHelper;
import com.kakai.android.autopreferences.compiler.AutoPreferencesAnnotatedClass;
import com.kakai.android.autopreferences.compiler.PreferenceAnnotatedField;
import com.kakai.android.autopreferences.compiler.generator.type.TypeGenerator;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class ClassGenerator {

    private static final String CLASS_NAME_PREFIX_ABSTRACT = "Abstract";
    private static final String CLASS_NAME_PREFIX_AUTO = "Auto";

    private static final String FIELD_CONTEXT_NAME = "context";
    private static final String FIELD_HELPER_NAME = "helper";

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;

    private AutoPreferencesAnnotatedClass clazz;
    private FieldSpec contextField;
    private FieldSpec helperField;

    public ClassGenerator(Types typeUtils, Elements elementUtils, Filer filer, AutoPreferencesAnnotatedClass clazz) {
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.filer = filer;
        this.clazz = clazz;
    }

    public String generate() {
        return generateClass();
    }

    private String generateClass() {
        TypeSpec autoPrefs = TypeSpec.classBuilder(className())
                .addModifiers(Modifier.PUBLIC)
                .superclass(clazz.getSuperclass())
                .addFields(generateFields())
                .addMethod(generateConstructor())
                .addMethods(generateMethods())
                .build();

        JavaFile javaFile = JavaFile.builder(clazz.getPackageName(), autoPrefs)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return javaFile.toString();
    }

    private List<FieldSpec> generateFields() {
        List<FieldSpec> fields = new ArrayList<>();

        contextField = FieldSpec.builder(Context.class, FIELD_CONTEXT_NAME)
                .addModifiers(Modifier.PRIVATE)
                .build();

        helperField = FieldSpec.builder(PreferencesHelper.class, FIELD_HELPER_NAME)
                .addModifiers(Modifier.PRIVATE)
                .build();

        fields.add(contextField);
        fields.add(helperField);

        return fields;
    }

    private MethodSpec generateConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Context.class, FIELD_CONTEXT_NAME)
                .addStatement("super()")
                .addStatement("this.$N = $L", contextField, FIELD_CONTEXT_NAME)
                .addStatement("this.$N = $T.with($L)", helperField, PreferencesHelper.class, FIELD_CONTEXT_NAME)
                .build();
    }

    private List<MethodSpec> generateMethods() {
        List<MethodSpec> methods = new ArrayList<>();

        for(PreferenceAnnotatedField field : clazz.getFields()) {
            methods.addAll(
                    TypeGenerator
                            .create(typeUtils, elementUtils, clazz, field)
                            .generateMethods(contextField, helperField)
            );
        }

        return methods;
    }

    private String className() {
        String className = clazz.getName().toString();

        if(className.startsWith(CLASS_NAME_PREFIX_ABSTRACT)) {
            return className.substring(CLASS_NAME_PREFIX_ABSTRACT.length());
        } else {
            return CLASS_NAME_PREFIX_AUTO + className;
        }
    }
}
