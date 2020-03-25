package org.example

import com.google.auto.service.AutoService
import com.intellij.mock.MockProject
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

@AutoService(ComponentRegistrar::class)
class CustomRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
        SyntheticResolveExtension.registerExtension(project, CustomSyntheticResolveExtension())
        ExpressionCodegenExtension.registerExtension(project, CustomCodegenExtension())
    }
}