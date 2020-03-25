package org.example

import org.apache.maven.plugin.MojoExecution
import org.apache.maven.project.MavenProject
import org.codehaus.plexus.component.annotations.Component
import org.codehaus.plexus.component.annotations.Requirement
import org.codehaus.plexus.logging.Logger
import org.jetbrains.kotlin.maven.KotlinMavenPluginExtension
import org.jetbrains.kotlin.maven.PluginOption

@Component(role = KotlinMavenPluginExtension::class, hint = "customExample")
class CustomMavenPlugin : KotlinMavenPluginExtension {
    override fun getCompilerPluginId(): String = "customExample"

    @Requirement
    lateinit var logger: Logger

    override fun isApplicable(project: MavenProject, execution: MojoExecution): Boolean = true

    override fun getPluginOptions(project: MavenProject, exectution: MojoExecution): List<PluginOption> {
        logger.debug("Loaded Maven plugin " + CustomMavenPlugin::class.simpleName)
        return emptyList()
    }
}