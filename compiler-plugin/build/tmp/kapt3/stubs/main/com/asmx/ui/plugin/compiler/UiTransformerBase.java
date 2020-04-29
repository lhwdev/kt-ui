package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b&\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u0014\u0010\u0005\u001a\u00020\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\nX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/asmx/ui/plugin/compiler/UiTransformerBase;", "Lorg/jetbrains/kotlin/ir/visitors/IrElementTransformerVoid;", "Lcom/asmx/ui/plugin/compiler/UiIrPhase;", "Lcom/asmx/ui/plugin/compiler/IrScope;", "()V", "moduleFragment", "Lorg/jetbrains/kotlin/ir/declarations/IrModuleFragment;", "getModuleFragment", "()Lorg/jetbrains/kotlin/ir/declarations/IrModuleFragment;", "pluginContext", "Lorg/jetbrains/kotlin/backend/common/extensions/IrPluginContext;", "getPluginContext", "()Lorg/jetbrains/kotlin/backend/common/extensions/IrPluginContext;", "target", "Lorg/jetbrains/kotlin/ir/IrElement;", "getTarget", "()Lorg/jetbrains/kotlin/ir/IrElement;", "thisLowering", "Lcom/asmx/ui/plugin/compiler/UiIrContext;", "lower", "", "compiler-plugin"})
public abstract class UiTransformerBase extends org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid implements com.asmx.ui.plugin.compiler.UiIrPhase, com.asmx.ui.plugin.compiler.IrScope {
    private final com.asmx.ui.plugin.compiler.UiIrContext thisLowering = null;
    @org.jetbrains.annotations.NotNull
    private final org.jetbrains.kotlin.backend.common.extensions.IrPluginContext pluginContext = null;
    @org.jetbrains.annotations.NotNull
    private final org.jetbrains.kotlin.ir.declarations.IrModuleFragment moduleFragment = null;
    @org.jetbrains.annotations.NotNull
    private final org.jetbrains.kotlin.ir.IrElement target = null;
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.backend.common.extensions.IrPluginContext getPluginContext() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.declarations.IrModuleFragment getModuleFragment() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final org.jetbrains.kotlin.ir.IrElement getTarget() {
        return null;
    }
    
    @java.lang.Override
    public void lower() {
    }
    
    public UiTransformerBase() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public java.lang.String getPhaseName() {
        return null;
    }
}