package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lcom/asmx/ui/plugin/compiler/IrScope;", "", "moduleFragment", "Lorg/jetbrains/kotlin/ir/declarations/IrModuleFragment;", "getModuleFragment", "()Lorg/jetbrains/kotlin/ir/declarations/IrModuleFragment;", "pluginContext", "Lorg/jetbrains/kotlin/backend/common/extensions/IrPluginContext;", "getPluginContext", "()Lorg/jetbrains/kotlin/backend/common/extensions/IrPluginContext;", "compiler-plugin"})
public abstract interface IrScope {
    
    @org.jetbrains.annotations.NotNull
    public abstract org.jetbrains.kotlin.backend.common.extensions.IrPluginContext getPluginContext();
    
    @org.jetbrains.annotations.NotNull
    public abstract org.jetbrains.kotlin.ir.declarations.IrModuleFragment getModuleFragment();
}