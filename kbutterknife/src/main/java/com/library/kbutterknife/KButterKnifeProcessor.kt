package com.library.kbutterknife

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.StringBuilder
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

const val SUFFIX = "_ViewInjector"

class KButterKnifeProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        // The map of activity -> @BindView info
        val injectionsByClass = linkedMapOf<TypeElement, MutableSet<InjectionPoint>>()

        // Save all @BindView annotations to map
        roundEnv?.getElementsAnnotatedWith(BindView::class.java)?.forEach { element ->
            val enclosingElement = element.enclosingElement as TypeElement
            var injections = injectionsByClass[enclosingElement]
            if (injections == null) {
                injections = mutableSetOf()
                injectionsByClass[enclosingElement] = injections
            }
            val variableName = element.simpleName.toString()
            val value = element.getAnnotation(BindView::class.java).value
            injections.add(InjectionPoint(variableName, value))
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
                injections.appendLine(String.format(INJECTION, it.variableName, it.value))
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