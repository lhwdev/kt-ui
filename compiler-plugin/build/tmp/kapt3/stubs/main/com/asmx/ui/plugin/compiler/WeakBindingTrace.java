package com.asmx.ui.plugin.compiler;

import java.lang.System;

/**
 * This class is meant to have the shape of a BindingTrace object that could exist and flow
 * through the Psi2Ir -> Ir phase, but doesn't currently exist. Ideally, this gets replaced in
 * the future by a trace that handles this use case in upstream. For now, we are okay with this
 * because the combination of IrAttributeContainer and WeakHashMap makes this relatively safe.
 */
@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J<\u0010\u0006\u001a\u0004\u0018\u0001H\u0007\"\b\b\u0000\u0010\b*\u00020\t\"\u0004\b\u0001\u0010\u00072\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\u00070\u000b2\u0006\u0010\f\u001a\u0002H\bH\u0086\u0002\u00a2\u0006\u0002\u0010\rJ?\u0010\u000e\u001a\u00020\u000f\"\b\b\u0000\u0010\b*\u00020\t\"\u0004\b\u0001\u0010\u00072\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\u00070\u00102\u0006\u0010\f\u001a\u0002H\b2\u0006\u0010\u0011\u001a\u0002H\u0007\u00a2\u0006\u0002\u0010\u0012R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/asmx/ui/plugin/compiler/WeakBindingTrace;", "", "()V", "map", "Ljava/util/WeakHashMap;", "Lorg/jetbrains/kotlin/com/intellij/util/keyFMap/KeyFMap;", "get", "V", "K", "Lorg/jetbrains/kotlin/ir/declarations/IrAttributeContainer;", "slice", "Lorg/jetbrains/kotlin/util/slicedMap/ReadOnlySlice;", "key", "(Lorg/jetbrains/kotlin/util/slicedMap/ReadOnlySlice;Lorg/jetbrains/kotlin/ir/declarations/IrAttributeContainer;)Ljava/lang/Object;", "record", "", "Lorg/jetbrains/kotlin/util/slicedMap/WritableSlice;", "value", "(Lorg/jetbrains/kotlin/util/slicedMap/WritableSlice;Lorg/jetbrains/kotlin/ir/declarations/IrAttributeContainer;Ljava/lang/Object;)V", "compiler-plugin"})
public final class WeakBindingTrace {
    private final java.util.WeakHashMap<java.lang.Object, org.jetbrains.kotlin.com.intellij.util.keyFMap.KeyFMap> map = null;
    
    public final <K extends org.jetbrains.kotlin.ir.declarations.IrAttributeContainer, V extends java.lang.Object>void record(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.util.slicedMap.WritableSlice<K, V> slice, @org.jetbrains.annotations.NotNull
    K key, V value) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final <K extends org.jetbrains.kotlin.ir.declarations.IrAttributeContainer, V extends java.lang.Object>V get(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.util.slicedMap.ReadOnlySlice<K, V> slice, @org.jetbrains.annotations.NotNull
    K key) {
        return null;
    }
    
    public WeakBindingTrace() {
        super();
    }
}