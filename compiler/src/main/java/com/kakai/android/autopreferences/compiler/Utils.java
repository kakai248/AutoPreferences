package com.kakai.android.autopreferences.compiler;

import com.google.common.base.CaseFormat;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class Utils {

    public static String lowerToUpperCamel(String name) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
    }

    public static DeclaredType getEnumType(Types typeUtils, Elements elementUtils) {
        return typeUtils.getDeclaredType(
                elementUtils.getTypeElement(Enum.class.getCanonicalName()),
                typeUtils.getWildcardType(null, null));
    }
}
