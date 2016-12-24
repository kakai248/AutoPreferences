package com.kakai.android.autopreferences.compiler.generator.type;

import com.kakai.android.autopreferences.compiler.AutoPreferencesAnnotatedClass;
import com.kakai.android.autopreferences.compiler.PreferenceAnnotatedField;
import com.kakai.android.autopreferences.compiler.Utils;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;
import java.util.Set;

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public interface TypeGenerator {

    static TypeGenerator create(Types typeUtils, Elements elementUtils,
                                AutoPreferencesAnnotatedClass clazz,
                                PreferenceAnnotatedField field) {

        TypeName typeName = field.getTypeName();

        if (TypeName.BOOLEAN.equals(typeName)) {
            return new BooleanTypeGenerator(clazz, field);

        } else if (TypeName.get(String.class).equals(typeName)) {
            return new StringTypeGenerator(clazz, field);

        } else if (TypeName.INT.equals(typeName)) {
            return new IntTypeGenerator(clazz, field);

        } else if (TypeName.FLOAT.equals(typeName)) {
            return new FloatTypeGenerator(clazz, field);

        } else if (TypeName.LONG.equals(typeName)) {
            return new LongTypeGenerator(clazz, field);

        } else if (ParameterizedTypeName.get(Set.class, String.class).equals(typeName)) {
            return new StringSetTypeGenerator(clazz, field);

        } else if (typeUtils.isSubtype(field.getVariable().asType(), Utils.getEnumType(typeUtils, elementUtils))) {
            return new EnumTypeGenerator(clazz, field);

        } else {
            throw new IllegalArgumentException(
                    String.format("TypeName: %s is not allowed", typeName.toString()));

        }
    }

    MethodSpec generateGetter(FieldSpec context, FieldSpec helper);

    MethodSpec generateSetter(FieldSpec context, FieldSpec helper);

    MethodSpec generateRemove(FieldSpec context, FieldSpec helper);

    MethodSpec generateContains(FieldSpec context, FieldSpec helper);

    List<MethodSpec> generateMethods(FieldSpec context, FieldSpec helper);
}
