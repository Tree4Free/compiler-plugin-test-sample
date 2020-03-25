package org.example

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.Charset

class CompilerPluginTest {
    @Test
    fun `Can compile without call`() {
        val kotlinSource = SourceFile.kotlin("TestClass.kt", """
                @org.example.GenerateMethod
                class TestClass
            """.trimIndent()
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)

            compilerPlugins = listOf(CustomRegistrar())

            inheritClassPath = true
            messageOutputStream = System.out
        }.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        val clazz = result.classLoader.loadClass("TestClass")

        assertThat(clazz).hasDeclaredMethods("generatedMethod")

        // redirect System.out and assert println is correct
        val out = ByteArrayOutputStream()
        System.setOut(PrintStream(out))
        clazz.getDeclaredMethod("generatedMethod").invoke(clazz.newInstance())
        out.flush()
        assertThat(out.toByteArray().toString(Charset.defaultCharset())).isEqualTo("Hello World")
    }

    @Test
    fun `Can compile with call`() {
        val kotlinSource = SourceFile.kotlin("TestClass.kt", """
                @org.example.GenerateMethod
                class TestClass {
                    fun callGeneratedMethod() {
                        this.generatedMethod()
                    }
                }
            """.trimIndent()
        )

        val result = KotlinCompilation().apply {
            sources = listOf(kotlinSource)

            compilerPlugins = listOf(CustomRegistrar())

            inheritClassPath = true
            messageOutputStream = System.out
        }.compile()

        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        val clazz = result.classLoader.loadClass("TestClass")
        assertThat(clazz).hasDeclaredMethods("callGeneratedMethod", "generatedMethod")

        // redirect System.out and assert println is correct
        val out = ByteArrayOutputStream()
        System.setOut(PrintStream(out))
        clazz.getDeclaredMethod("callGeneratedMethod").invoke(clazz.newInstance())
        out.flush()
        assertThat(out.toByteArray().toString(Charset.defaultCharset())).isEqualTo("Hello World")
    }
}