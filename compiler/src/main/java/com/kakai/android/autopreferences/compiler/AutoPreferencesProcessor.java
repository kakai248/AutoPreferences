package com.kakai.android.autopreferences.compiler;

import com.kakai.android.autopreferences.annotations.AutoPreferences;
import com.kakai.android.autopreferences.compiler.generator.ClassGenerator;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes({
        "com.kakai.android.autopreferences.annotations.AutoPreferences",
        "com.kakai.android.autopreferences.annotations.Preference"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public final class AutoPreferencesProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {

        for(Element annotatedElement : env.getElementsAnnotatedWith(AutoPreferences.class)) {

            // Check if a class has been annotated with @AutoPreferences
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement, "Only classes can be annotated with @%s",
                        AutoPreferences.class.getSimpleName());
                return true; // Exit processing
            }

            AutoPreferencesAnnotatedClass clazz =
                    new AutoPreferencesAnnotatedClass(elementUtils, (TypeElement) annotatedElement);

            ClassGenerator classGenerator = new ClassGenerator(typeUtils, elementUtils, filer, clazz);

            try {
                messager.printMessage(Diagnostic.Kind.NOTE, classGenerator.generate());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }
}