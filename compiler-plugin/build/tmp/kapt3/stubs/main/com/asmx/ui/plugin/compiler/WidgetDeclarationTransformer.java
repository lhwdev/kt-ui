package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Suppress(names = {"MemberVisibilityCanBePrivate"})
@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\fH\u0016J\u001a\u0010\r\u001a\u00020\u0004*\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u0012\u0010\u0012\u001a\u00020\u0013*\u00020\f2\u0006\u0010\u0014\u001a\u00020\u0015\u00a8\u0006\u0016"}, d2 = {"Lcom/asmx/ui/plugin/compiler/WidgetDeclarationTransformer;", "Lcom/asmx/ui/plugin/compiler/UiTransformerBase;", "()V", "visitCall", "Lorg/jetbrains/kotlin/ir/expressions/IrExpression;", "expression", "Lorg/jetbrains/kotlin/ir/expressions/IrCall;", "visitDeclaration", "Lorg/jetbrains/kotlin/ir/IrStatement;", "declaration", "Lorg/jetbrains/kotlin/ir/declarations/IrDeclaration;", "visitFunction", "Lorg/jetbrains/kotlin/ir/declarations/IrFunction;", "transformWidgetLambdaCallIfNecessary", "valueParameter", "Lorg/jetbrains/kotlin/ir/declarations/IrValueParameter;", "valueArgument", "Lorg/jetbrains/kotlin/ir/expressions/IrFunctionExpression;", "withWidgetCall", "Lcom/asmx/ui/plugin/compiler/WidgetTransformationInfo;", "kind", "Lcom/asmx/ui/plugin/compiler/WidgetTransformationKind;", "compiler-plugin"})
public final class WidgetDeclarationTransformer extends com.asmx.ui.plugin.compiler.UiTransformerBase {
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.IrStatement visitDeclaration(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.IrStatement visitFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCall expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.asmx.ui.plugin.compiler.WidgetTransformationInfo withWidgetCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction $this$withWidgetCall, @org.jetbrains.annotations.NotNull
    com.asmx.ui.plugin.compiler.WidgetTransformationKind kind) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final org.jetbrains.kotlin.ir.expressions.IrExpression transformWidgetLambdaCallIfNecessary(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCall $this$transformWidgetLambdaCallIfNecessary, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrValueParameter valueParameter, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionExpression valueArgument) {
        return null;
    }
    
    public WidgetDeclarationTransformer() {
        super();
    }
}