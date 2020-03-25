package org.example

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.builtins.DefaultBuiltIns
import org.jetbrains.kotlin.codegen.extensions.ExpressionCodegenExtension
//import com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

@AutoService(ComponentRegistrar::class)
class CustomRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
        SyntheticResolveExtension.registerExtension(project, CustomSyntheticResolveExtension())
        ExpressionCodegenExtension.registerExtension(project, CustomCodegenExtension())
    }

    class CustomSyntheticResolveExtension : SyntheticResolveExtension {
        override fun generateSyntheticMethods(
            thisDescriptor: ClassDescriptor,
            name: Name,
            bindingContext: BindingContext,
            fromSupertypes: List<SimpleFunctionDescriptor>,
            result: MutableCollection<SimpleFunctionDescriptor>
        ) {
            if (!thisDescriptor.annotations.hasAnnotation(FqName("org.example.GenerateMethod")))
                return

            val fn = SimpleFunctionDescriptorImpl.create(
                thisDescriptor,
                Annotations.EMPTY,
                Name.identifier("generatedMethod"),
                CallableMemberDescriptor.Kind.SYNTHESIZED,
                SourceElement.NO_SOURCE
            )

            val fnDesc = fn.initialize(
                null, // extensionReceiverParameter
                thisDescriptor.thisAsReceiverParameter, // dispatchReceiverParameter
                mutableListOf(), // typeParameters
                mutableListOf(), // unsubstitutedValueParameters
                DefaultBuiltIns.Instance.unitType, // unsubstitutedReturnType
                Modality.FINAL, // modality
                Visibilities.PUBLIC // visibility
            )

            result.add(fnDesc)
        }
    }

}