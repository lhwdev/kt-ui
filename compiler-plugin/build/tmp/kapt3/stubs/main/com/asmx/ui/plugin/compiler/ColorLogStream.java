package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/asmx/ui/plugin/compiler/ColorLogStream;", "Lcom/asmx/ui/plugin/compiler/LogStream;", "doLog", "", "content", "", "color", "", "compiler-plugin"})
public abstract interface ColorLogStream extends com.asmx.ui.plugin.compiler.LogStream {
    
    public abstract void doLog(@org.jetbrains.annotations.Nullable
    java.lang.Object content, @org.jetbrains.annotations.NotNull
    java.lang.String color);
}