package com.gpf.processor;

import com.google.auto.service.AutoService;
import com.gpf.annotation.GPFBindLayout;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

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
import javax.lang.model.element.Modifier;
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
        for (Element e :
                elements) {

            GPFBindLayout bindLayout = e.getAnnotation(GPFBindLayout.class);

            String newClassName = e.getSimpleName() + "_ViewBindLayout";

            // 使用 Javapoet

            ClassName activityClassName = ClassName.get("android.app", "Activity");

            // 定义参数 Activity activity
            ParameterSpec parameterSpec = ParameterSpec
                    .builder(activityClassName, "activity").build();

            // 方法
            MethodSpec setContentView = MethodSpec.methodBuilder("setContentView")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(parameterSpec)
//                    .addStatement("activity.setContentView(" + bindLayout.value() + ")")
                    .addStatement("activity.setContentView($L)", bindLayout.value())
                    .build();

            // 生成Java源文件类
            TypeSpec viewBindLayout = TypeSpec.classBuilder(newClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(setContentView)
                    .build();

            // 输出Java源文件
            JavaFile javaFile = JavaFile
                    .builder("com.gpf.learnapt", viewBindLayout)
                    .build();

            try {
                javaFile.writeTo(filer);
                info(javaFile.toString());
                info("文件生成成功");
            } catch (IOException ioException) {
                info("文件生成失败");
            }

//            StringBuilder builder = new StringBuilder()
//                    .append("package com.gpf.learnapt;\n\n")
//                    .append("public class ")
//                    .append(newClassName)
//                    .append(" {\n\n") // open class
//                    .append("\tpublic void setContentView() {\n"); // open method
//
//            // TODO 具体的方法实现 方法内部调用 activity.setContentView(bindLayout.value())
//            // setContentView 需要一个参数 activity
//
//            builder.append("\t}\n") // close method
//                    .append("} \n"); // end class
//
//            try { // write the file
//                JavaFileObject source = filer.createSourceFile("com.gpf.processor.auto." + newClassName);
//                Writer writer = source.openWriter();
//                writer.write(builder.toString());
//                writer.flush();
//                writer.close();
//            } catch (IOException ioe) {
//                // Note: calling e.printStackTrace() will print IO errors
//                // that occur from the file already existing after its first run, this is normal
//            }

        }

        return false;
    }

}