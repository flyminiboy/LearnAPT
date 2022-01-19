package com.gpf.processor;

import com.google.auto.service.AutoService;
import com.gpf.annotation.GPFBindLayout;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class GPFLayoutProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;

    private void info(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        info("GPFLayoutProcessor init");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        info("GPFLayoutProcessor getSupportedAnnotationTypes");
        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(GPFBindLayout.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        info("GPFLayoutProcessor process");

        // 找到含有 GPFBindLayout 注解的所有元素
        Set<? extends Element> elements =
                roundEnvironment.getElementsAnnotatedWith(GPFBindLayout.class);
        info("elements size = " + elements.size());
        for (Element e :
                elements) {
            info("element kind = " + e.getKind().name() + "/" +
                    e.getSimpleName());

            info("element type = " + e.asType().toString());

            GPFBindLayout bindLayout = e.getAnnotation(GPFBindLayout.class);

            String newClassName = e.getSimpleName() + "_ViewBindLayout";

            StringBuilder builder = new StringBuilder()
                    .append("package com.gpf.processor.auto;\n\n")
                    .append("public class ")
                    .append(newClassName)
                    .append(" {\n\n") // open class
                    .append("\tpublic void setContentView() {\n"); // open method

            // TODO 具体的方法实现

            builder.append("\t}\n") // close method
                    .append("} \n"); // end class

            try { // write the file
                JavaFileObject source = filer.createSourceFile("com.gpf.processor.auto." + newClassName);
                Writer writer = source.openWriter();
                writer.write(builder.toString());
                writer.flush();
                writer.close();
            } catch (IOException ioe) {
                // Note: calling e.printStackTrace() will print IO errors
                // that occur from the file already existing after its first run, this is normal
            }

        }

        return false;
    }

}