package org.example

fun main(args: Array<String>) {
    println("Hello, World")
}

@GenerateMethod
class TestClass {
    fun test() {
        this.generatedMethod()
    }
}