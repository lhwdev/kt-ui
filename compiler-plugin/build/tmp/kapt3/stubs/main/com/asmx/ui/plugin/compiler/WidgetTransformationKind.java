package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2 = {"Lcom/asmx/ui/plugin/compiler/WidgetTransformationKind;", "", "(Ljava/lang/String;I)V", "isWidget", "", "()Z", "widgetUtil", "inlineWidget", "widget", "compiler-plugin"})
public enum WidgetTransformationKind {
    /*public static final*/ widgetUtil /* = new widgetUtil() */,
    /*public static final*/ inlineWidget /* = new inlineWidget() */,
    /*public static final*/ widget /* = new widget() */;
    
    public final boolean isWidget() {
        return false;
    }
    
    WidgetTransformationKind() {
    }
}