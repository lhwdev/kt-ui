package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u00012\u00020\u0002R\u0012\u0010\u0003\u001a\u00020\u0001X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/asmx/ui/plugin/compiler/WidgetPropertyDescriptor;", "Lorg/jetbrains/kotlin/descriptors/PropertyDescriptor;", "Lcom/asmx/ui/plugin/compiler/WidgetCallableDescriptor;", "underlyingDescriptor", "getUnderlyingDescriptor", "()Lorg/jetbrains/kotlin/descriptors/PropertyDescriptor;", "compiler-plugin"})
public abstract interface WidgetPropertyDescriptor extends org.jetbrains.kotlin.descriptors.PropertyDescriptor, com.asmx.ui.plugin.compiler.WidgetCallableDescriptor {
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public abstract org.jetbrains.kotlin.descriptors.PropertyDescriptor getUnderlyingDescriptor();
}