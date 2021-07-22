package com.library.kbutterknife

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.StringBuilder
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

const val SUFFIX = "_ViewInjector"

class KButterKnifeProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        // The map of activity -> @BindView info
        val injectionsByClass = linkedMapOf<TypeElement, MutableSet<InjectionPoint>>()

        // Save all @BindView annotations to map
        roundEnv?.getElementsAnnotatedWith(BindView::class.java)?.forEach { element ->
            val enclosingElement = element.enclosingElement as TypeElement
            var fieldInjections = injectionsByClass[enclosingElement]
            if (fieldInjections == null) {
                fieldInjections = mutableSetOf()
                injectionsByClass[enclosingElement] = fieldInjections
            }
            val fieldName = element.simpleName.toString()
            val id = element.getAnnotation(BindView::class.java).value
            fieldInjections.add(InjectionPoint(id, fieldName = fieldName))
        }

        // Save all @OnClick annotations to map
        roundEnv?.getElementsAnnotatedWith(OnClick::class.java)?.forEach { element ->
            val executableElement = element as ExecutableElement
            val enclosingElement = element.enclosingElement as TypeElement
            var methodInjections = injectionsByClass[enclosingElement]
            if (methodInjections == null) {
                methodInjections = mutableSetOf()
                injectionsByClass[enclosingElement] = methodInjections
            }
            val methodName = executableElement.simpleName.toString()
            val methodType = executableElement.parameters.firstOrNull()?.asType()?.toString()
            element.getAnnotation(OnClick::class.java).value.forEach { id ->
                methodInjections.add(InjectionPoint(id, methodName = methodName, methodType = methodType))
            }
        }

        // Default generated dir path: app\build\generated\source\kaptKotlin\debug
        val generatedDir = processingEnv.options["kapt.kotlin.generated"]
        val filePath = "$generatedDir/com/example/butterknife"

        // Generate every injection file
        for (injection in injectionsByClass) {
            val type = injection.key
            val targetClass = type.qualifiedName
            val lastDot = targetClass.lastIndexOf(".")
            val packageName = targetClass.substring(0, lastDot)
            val activityType = targetClass.substring(lastDot + 1)
            val className = activityType + SUFFIX

            val injections = StringBuilder()
            injection.value.forEach {
                if (it.fieldName != null) {
                    injections.appendLine(String.format(FIELD_INJECTION, it.fieldName, it.id))
                } else if (it.methodName != null) {
                    injections.appendLine(String.format(METHOD_INJECTION, it.id, it.methodName, if (it.methodType == null) "" else "it as ${it.methodType}"))
                }
            }

            val file = File(filePath, "$className.kt")
            file.parentFile.mkdirs()
            // Write file
            val writer = BufferedWriter(FileWriter(file))
            writer.use {
                it.write(String.format(TEMPLATE, packageName, className, activityType, injections.toString()))
            }
        }
        return false
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(BindView::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}