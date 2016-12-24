package com.kakai.android.autopreferences.compiler;

import com.kakai.android.autopreferences.annotations.AutoPreferences;
import com.kakai.android.autopreferences.annotations.Preference;
import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class AutoPreferencesAnnotatedClass {

    private TypeElement clazz;
    private String packageName;
    private Name name;
    private List<PreferenceAnnotatedField> fields;
    private boolean annotate;

    public AutoPreferencesAnnotatedClass(Elements elementUtils, TypeElement clazz)
            throws IllegalArgumentException {

        this.clazz = clazz;

        // Package
        PackageElement pkg = elementUtils.getPackageOf(clazz);
        packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();

        // Class
        name = clazz.getSimpleName();

        // Annotate getters and setters
        AutoPreferences annotation = clazz.getAnnotation(AutoPreferences.class);
        annotate = annotation.annotate();

        // Fields
        fields = new ArrayList<>();

        clazz.getEnclosedElements()
                .stream()
                .filter(element -> element.getKind() == ElementKind.FIELD)
                .filter(element -> element.getAnnotation(Preference.class) != null)
                .forEach(element -> {
                    VariableElement variable = (VariableElement) element;
                    PreferenceAnnotatedField field = new PreferenceAnnotatedField(variable);
                    fields.add(field);
                });
    }

    public String getPackageName() {
        return packageName;
    }

    public Name getName() {
        return name;
    }

    public ClassName getSuperclass() {
        return ClassName.get(clazz);
    }

    public boolean useAnnotations() {
        return annotate;
    }

    public List<PreferenceAnnotatedField> getFields() {
        return fields;
    }
}