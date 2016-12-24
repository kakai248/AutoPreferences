package com.kakai.android.autopreferences.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Preference {

    int ALL = -1;

    int stringRes() default 0;

    boolean remove() default false;

    boolean contains() default false;

    int tag() default ALL;
}
