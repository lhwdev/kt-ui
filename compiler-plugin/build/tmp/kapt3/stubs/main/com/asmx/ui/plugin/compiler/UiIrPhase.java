package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0006\u001a\u00020\u0007H&R\u0016\u0010\u0002\u001a\u0004\u0018\u00010\u00038VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\b"}, d2 = {"Lcom/asmx/ui/plugin/compiler/UiIrPhase;", "", "phaseName", "", "getPhaseName", "()Ljava/lang/String;", "lower", "", "compiler-plugin"})
public abstract interface UiIrPhase {
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.String getPhaseName();
    
    public abstract void lower();
    
    @kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 3, xi = 2)
    public final class DefaultImpls {
        
        @org.jetbrains.annotations.Nullable
        public static java.lang.String getPhaseName(com.asmx.ui.plugin.compiler.UiIrPhase $this) {
            return null;
        }
    }
}