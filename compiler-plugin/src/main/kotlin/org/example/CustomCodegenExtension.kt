package org.example

import org.jetbrains.kotlin.codegen.ExpressionCodegen
import org.jetbrains.kotlin.codegen.FunctionGenerationStrategy
import org.jetbrains.kotlin.codegen.ImplementationBodyCodegen
import org.jetbrains.kotlin.codegen.OwnerKind
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi2ir.findFirstFunction
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOrigin
import org.jetbrains.kotlin.resolve.jvm.jvmSignature.JvmMethodSignature
import org.jetbrains.org.objectweb.asm.Opcodes

class CustomCodegenExtension : ExpressionCodegenExtension {
    override fun generateClassSyntheticParts(codegen: ImplementationBodyCodegen) {
        val targetClass = codegen.descriptor

        if (!targetClass.annotations.hasAnnotation(FqName("org.example.GenerateMethod")))
            return

        val fn = targetClass.findFirstFunction("generatedMethod") { true }

//        val fn = SimpleFunctionDescriptorImpl.create(
//            targetClass,
//            Annotations.EMPTY,
//            Name.identifier("generatedMethod"),
//            CallableMemberDescriptor.Kind.DECLARATION,
//            SourceElement.NO_SOURCE
//        )
//
//        fn.initialize(
//            null, // extensionReceiverParameter
//            targetClass.thisAsReceiverParameter, // dispatchReceiverParameter
//            mutableListOf(), // typeParameters
//            mutableListOf(), // unsubstitutedValueParameters
//            DefaultBuiltIns.Instance.unitType, // unsubstitutedReturnType
//            Modality.FINAL, // modality
//            Visibilities.PUBLIC // visibility
//        )

        codegen.context.intoClass(targetClass, OwnerKind.IMPLEMENTATION, codegen.state)

        codegen.functionCodegen.generateMethod(JvmDeclarationOrigin.NO_ORIGIN, fn, CodeGenStrategy(codegen))
    }

    class CodeGenStrategy(
        implCodegen: ImplementationBodyCodegen
    ) : FunctionGenerationStrategy.CodegenBased(implCodegen.state) {
        override fun doGenerateBody(codegen: ExpressionCodegen, signature: JvmMethodSignature) {
            codegen.v.apply {
                getstatic("java/lang/System", "out", "Ljava/io/PrintStream;")
                aconst("Hello World")
                invokevirtual("java/io/PrintStream", "print", "(Ljava/lang/Object;)V", false)
                visitInsn(Opcodes.RETURN)

            }
        }
    }
}