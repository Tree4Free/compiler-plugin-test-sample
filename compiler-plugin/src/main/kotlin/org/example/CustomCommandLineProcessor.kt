package org.example

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor

@AutoService(CommandLineProcessor::class)
class CustomCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = "customExample"
    override val pluginOptions: Collection<AbstractCliOption> = emptyList()
}