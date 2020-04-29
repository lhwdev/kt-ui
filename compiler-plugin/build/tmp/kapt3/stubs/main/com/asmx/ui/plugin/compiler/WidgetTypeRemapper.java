package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u000eH\u0016J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016H\u0002J\u0010\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0019H\u0002J\f\u0010\u001a\u001a\u00020\u001b*\u00020\u001bH\u0002J\f\u0010\u001c\u001a\u00020\u0013*\u00020\u001dH\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/asmx/ui/plugin/compiler/WidgetTypeRemapper;", "Lorg/jetbrains/kotlin/ir/util/TypeRemapper;", "context", "Lorg/jetbrains/kotlin/backend/common/extensions/IrPluginContext;", "symbolRemapper", "Lorg/jetbrains/kotlin/ir/util/SymbolRemapper;", "typeTranslator", "Lorg/jetbrains/kotlin/ir/util/TypeTranslator;", "buildScopeTypeDescriptor", "Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;", "widgetAnnotation", "Lorg/jetbrains/kotlin/descriptors/annotations/AnnotationDescriptor;", "(Lorg/jetbrains/kotlin/backend/common/extensions/IrPluginContext;Lorg/jetbrains/kotlin/ir/util/SymbolRemapper;Lorg/jetbrains/kotlin/ir/util/TypeTranslator;Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;Lorg/jetbrains/kotlin/descriptors/annotations/AnnotationDescriptor;)V", "enterScope", "", "irTypeParametersContainer", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeParametersContainer;", "leaveScope", "remapType", "Lorg/jetbrains/kotlin/ir/types/IrType;", "type", "remapTypeArgument", "Lorg/jetbrains/kotlin/ir/types/IrTypeArgument;", "typeArgument", "underlyingRemapType", "Lorg/jetbrains/kotlin/ir/types/IrSimpleType;", "remapTypeAbbreviation", "Lorg/jetbrains/kotlin/ir/types/IrTypeAbbreviation;", "toIrType", "Lorg/jetbrains/kotlin/types/KotlinType;", "compiler-plugin"})
public final class WidgetTypeRemapper implements org.jetbrains.kotlin.ir.util.TypeRemapper {
    private final org.jetbrains.kotlin.backend.common.extensions.IrPluginContext context = null;
    private final org.jetbrains.kotlin.ir.util.SymbolRemapper symbolRemapper = null;
    private final org.jetbrains.kotlin.ir.util.TypeTranslator typeTranslator = null;
    private final org.jetbrains.kotlin.descriptors.ClassDescriptor buildScopeTypeDescriptor = null;
    private final org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor widgetAnnotation = null;
    
    @java.lang.Override
    public void enterScope(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeParametersContainer irTypeParametersContainer) {
    }
    
    @java.lang.Override
    public void leaveScope() {
    }
    
    private final org.jetbrains.kotlin.ir.types.IrType toIrType(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.KotlinType $this$toIrType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.types.IrType remapType(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType type) {
        return null;
    }
    
    private final org.jetbrains.kotlin.ir.types.IrType underlyingRemapType(org.jetbrains.kotlin.ir.types.IrSimpleType type) {
        return null;
    }
    
    private final org.jetbrains.kotlin.ir.types.IrTypeArgument remapTypeArgument(org.jetbrains.kotlin.ir.types.IrTypeArgument typeArgument) {
        return null;
    }
    
    private final org.jetbrains.kotlin.ir.types.IrTypeAbbreviation remapTypeAbbreviation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrTypeAbbreviation $this$remapTypeAbbreviation) {
        return null;
    }
    
    public WidgetTypeRemapper(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.backend.common.extensions.IrPluginContext context, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.SymbolRemapper symbolRemapper, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.TypeTranslator typeTranslator, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ClassDescriptor buildScopeTypeDescriptor, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor widgetAnnotation) {
        super();
    }
}