package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Suppress(names = {"NOTHING_TO_INLINE"})
@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 2, xi = 2, d1 = {"\u0000r\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a+\u0010\u0017\u001a\u00020\u00182\u000e\u0010\u0019\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u001a2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aH\u0002\u00a2\u0006\u0002\u0010\u001d\u001a\u0019\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u001b2\u0006\u0010 \u001a\u00020\u001bH\u0082\b\u001a\u0010\u0010!\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010#\u001a.\u0010!\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010#2\b\b\u0002\u0010$\u001a\u00020%2\b\b\u0002\u0010&\u001a\u00020%2\b\b\u0002\u0010\'\u001a\u00020%\u001a\u0010\u0010(\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010#\u001a\u0010\u0010)\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010#\u001a\u0010\u0010*\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010#\u001a\u0010\u0010+\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010#\u001a\u0010\u0010,\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010#\u001a\u0018\u0010-\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010$\u001a\u00020%\u001a,\u0010.\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020%2\b\b\u0002\u0010$\u001a\u00020%2\b\b\u0002\u0010&\u001a\u00020%2\b\b\u0002\u0010\'\u001a\u00020%\u001a\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010$\u001a\u00020%\u001a%\u0010/\u001a\u0002H0\"\u0004\b\u0000\u001002\f\u00101\u001a\b\u0012\u0004\u0012\u0002H002H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u00103\u001a\u0010\u00104\u001a\u00020\u00032\u0006\u00105\u001a\u00020%H\u0002\u001a\u001a\u00106\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010#2\b\b\u0002\u00107\u001a\u00020%\u001a\u0014\u00108\u001a\u00020%*\u0002092\u0006\u0010:\u001a\u000209H\u0002\u001a\u0018\u0010;\u001a\u0002H<\"\u0004\b\u0000\u0010<*\u0002H<H\u0086\b\u00a2\u0006\u0002\u0010=\u001a1\u0010;\u001a\u0002H<\"\u0004\b\u0000\u0010<*\u0002H<2\u0014\u0010>\u001a\u0010\u0012\u0004\u0012\u0002H<\u0012\u0006\u0012\u0004\u0018\u00010#0?H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010@\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u0010\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0004\"\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000\"\u0011\u0010\b\u001a\u00020\t8F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000b\"\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u000e\u0010\u0014\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006A"}, d2 = {"doIndentAutomatic", "", "dummyInit", "", "Lkotlin/Unit;", "err", "Ljava/io/PrintStream;", "isLastNewline", "logConfig", "Lcom/asmx/ui/plugin/compiler/LogConfig;", "getLogConfig", "()Lcom/asmx/ui/plugin/compiler/LogConfig;", "logFile", "Ljava/io/File;", "logFileWriter", "Ljava/io/Writer;", "logStream", "Lcom/asmx/ui/plugin/compiler/LogStream;", "getLogStream", "()Lcom/asmx/ui/plugin/compiler/LogStream;", "out", "sDateFormat", "Ljava/text/SimpleDateFormat;", "compareStackTrace", "Lcom/asmx/ui/plugin/compiler/CompareStackTraceResult;", "last", "", "Ljava/lang/StackTraceElement;", "current", "([Ljava/lang/StackTraceElement;[Ljava/lang/StackTraceElement;)Lcom/asmx/ui/plugin/compiler/CompareStackTraceResult;", "isSameFunction", "a", "b", "log", "content", "", "color", "", "prefix", "suffix", "log2", "log3", "log4", "log5", "log6", "logColor", "logInternalWithoutNewline", "muteLog", "R", "block", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "printOut", "text", "printlnWithColor", "defaultColor", "toStringFilling", "", "i", "withLog", "T", "(Ljava/lang/Object;)Ljava/lang/Object;", "toString", "Lkotlin/Function1;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "compiler-plugin"})
public final class LogKt {
    private static final java.io.File logFile = null;
    private static final java.io.Writer logFileWriter = null;
    private static final boolean doIndentAutomatic = true;
    private static final java.text.SimpleDateFormat sDateFormat = null;
    private static final java.io.PrintStream out = null;
    private static final java.io.PrintStream err = null;
    private static final kotlin.Unit dummyInit = null;
    @org.jetbrains.annotations.NotNull
    private static final com.asmx.ui.plugin.compiler.LogStream logStream = null;
    private static boolean isLastNewline = true;
    
    @kotlin.Suppress(names = {"NOTHING_TO_INLINE"})
    private static final boolean isSameFunction(java.lang.StackTraceElement a, java.lang.StackTraceElement b) {
        return false;
    }
    
    private static final com.asmx.ui.plugin.compiler.CompareStackTraceResult compareStackTrace(java.lang.StackTraceElement[] last, java.lang.StackTraceElement[] current) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final com.asmx.ui.plugin.compiler.LogConfig getLogConfig() {
        return null;
    }
    
    public static final <R extends java.lang.Object>R muteLog(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<? extends R> block) {
        return null;
    }
    
    public static final void log(@org.jetbrains.annotations.Nullable
    java.lang.Object content) {
    }
    
    public static final void log(@org.jetbrains.annotations.Nullable
    java.lang.Object content, @org.jetbrains.annotations.NotNull
    java.lang.String color, @org.jetbrains.annotations.NotNull
    java.lang.String prefix, @org.jetbrains.annotations.NotNull
    java.lang.String suffix) {
    }
    
    public static final void logColor(@org.jetbrains.annotations.Nullable
    java.lang.Object content, @org.jetbrains.annotations.NotNull
    java.lang.String color) {
    }
    
    public static final void log2(@org.jetbrains.annotations.Nullable
    java.lang.Object content) {
    }
    
    public static final void log3(@org.jetbrains.annotations.Nullable
    java.lang.Object content) {
    }
    
    public static final void log4(@org.jetbrains.annotations.Nullable
    java.lang.Object content) {
    }
    
    public static final void log5(@org.jetbrains.annotations.Nullable
    java.lang.Object content) {
    }
    
    public static final void log6(@org.jetbrains.annotations.Nullable
    java.lang.Object content) {
    }
    
    @org.jetbrains.annotations.NotNull
    public static final com.asmx.ui.plugin.compiler.LogStream getLogStream() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final com.asmx.ui.plugin.compiler.LogStream logStream(@org.jetbrains.annotations.NotNull
    java.lang.String color) {
        return null;
    }
    
    public static final <T extends java.lang.Object>T withLog(T $this$withLog) {
        return null;
    }
    
    public static final <T extends java.lang.Object>T withLog(T $this$withLog, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super T, ? extends java.lang.Object> toString) {
        return null;
    }
    
    public static final void logInternalWithoutNewline(@org.jetbrains.annotations.NotNull
    java.lang.String content, @org.jetbrains.annotations.NotNull
    java.lang.String color, @org.jetbrains.annotations.NotNull
    java.lang.String prefix, @org.jetbrains.annotations.NotNull
    java.lang.String suffix) {
    }
    
    private static final void printOut(java.lang.String text) {
    }
    
    private static final java.lang.String toStringFilling(int $this$toStringFilling, int i) {
        return null;
    }
    
    public static final void printlnWithColor(@org.jetbrains.annotations.Nullable
    java.lang.Object content, @org.jetbrains.annotations.NotNull
    java.lang.String defaultColor) {
    }
}