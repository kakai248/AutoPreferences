package com.kakai.android.autopreferences.compiler;

import com.kakai.android.autopreferences.annotations.Preference;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;

public class PreferenceAnnotatedField {

    private VariableElement variable;
    private TypeName typeName;
    private Name name;
    private int stringRes;
    private boolean omitGetterPrefix;
    private boolean generateRemove;
    private boolean generateContains;
    private int tag;

    PreferenceAnnotatedField(VariableElement variable) {
        this.variable = variable;
        this.typeName = TypeName.get(variable.asType());
        this.name = variable.getSimpleName();

        Preference annotation = variable.getAnnotation(Preference.class);
        this.stringRes = annotation.stringRes();
        this.omitGetterPrefix = annotation.omitGetterPrefix();
        this.generateRemove = annotation.remove();
        this.generateContains = annotation.contains();
        this.tag = annotation.tag();
    }

    public VariableElement getVariable() {
        return variable;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public Name getName() {
        return name;
    }

    public int getStringRes() {
        return stringRes;
    }

    public boolean omitGetterPrefix() {
        return omitGetterPrefix;
    }

    public boolean generateRemove() {
        return generateRemove;
    }

    public boolean generateContains() {
        return generateContains;
    }

    public int getTag() {
        return tag;
    }
}
