package com.ecoquelin.vaadin.utils.processors;

import com.ecoquelin.vaadin.utils.annotations.I18nConstants;
import com.ecoquelin.vaadin.utils.annotations.Key;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by Emmanuel on 24/04/2015.
 */
@SupportedAnnotationTypes("com.ecoquelin.vaadin.utils.annotations.I18nConstants")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class I18nConstantProcessor extends AbstractProcessor {


    public I18nConstantProcessor(){
        super();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Trying to process");
        for (Element element : roundEnv.getElementsAnnotatedWith(I18nConstants.class)){
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found element " + element.getSimpleName().toString());
            try {
            String packageName = null;
                String className = null;

            Map<String, MethodElement> methods = new HashMap<>();

            if(element.getKind() == ElementKind.INTERFACE){
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Element is a class");
                TypeElement typeElement = (TypeElement) element;
                PackageElement packageElement = (PackageElement) element.getEnclosingElement();
                packageName = packageElement.getQualifiedName().toString();
                className = typeElement.getSimpleName().toString();

                typeElement.getEnclosedElements().stream().filter(e -> e.getKind() == ElementKind.METHOD).forEach(new Consumer<Element>() {
                    @Override
                    public void accept(Element element) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Found method");
                        ExecutableElement method = (ExecutableElement) element;
                        String methodValue = method.getSimpleName().toString();
                        Key annotation = method.getAnnotation(Key.class);

                        if(annotation != null) {
                            methodValue = annotation.value();
                        }

                        methods.put(method.getSimpleName().toString(), new MethodElement(method, methodValue));
                    }
                });

            }

            if (className != null) {

                Properties props = new Properties();
                URL url = this.getClass().getClassLoader().getResource("velocity.properties");
                props.load(url.openStream());

                VelocityEngine ve = new VelocityEngine(props);
                ve.init();

                VelocityContext vc = new VelocityContext();

                vc.put("className", className);
                vc.put("packageName", packageName);
                vc.put("methods", methods);

                Template vt = ve.getTemplate("I18nConstantImpl.vm");

                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        className + "Impl");

                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE,
                        "creating source file: " + jfo.toUri());

                Writer writer = jfo.openWriter();

                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE,
                        "applying velocity template: " + vt.getName());

                vt.merge(vc, writer);
                writer.close();

            }
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        e.getLocalizedMessage());
            }
        }

        return true;
    }
}
