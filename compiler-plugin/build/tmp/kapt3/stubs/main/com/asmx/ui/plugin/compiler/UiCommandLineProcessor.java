package com.asmx.ui.plugin.compiler;

import java.lang.System;

@com.google.auto.service.AutoService(value = {org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor.class})
@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/asmx/ui/plugin/compiler/UiCommandLineProcessor;", "Lorg/jetbrains/kotlin/compiler/plugin/CommandLineProcessor;", "()V", "pluginId", "", "getPluginId", "()Ljava/lang/String;", "pluginOptions", "", "Lorg/jetbrains/kotlin/compiler/plugin/CliOption;", "getPluginOptions", "()Ljava/util/List;", "compiler-plugin"})
public final class UiCommandLineProcessor implements org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<org.jetbrains.kotlin.compiler.plugin.CliOption> pluginOptions = null;
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String getPluginId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.util.List<org.jetbrains.kotlin.compiler.plugin.CliOption> getPluginOptions() {
        return null;
    }
    
    public UiCommandLineProcessor() {
        super();
    }
    
    public void processOption(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.compiler.plugin.AbstractCliOption option, @org.jetbrains.annotations.NotNull
    java.lang.String value, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.config.CompilerConfiguration configuration) {
    }
    
    @java.lang.Deprecated
    public void processOption(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.compiler.plugin.CliOption option, @org.jetbrains.annotations.NotNull
    java.lang.String value, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.config.CompilerConfiguration configuration) {
    }
    
    public <T extends java.lang.Object>void appendList(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.config.CompilerConfiguration $this$appendList, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.config.CompilerConfigurationKey<java.util.List<T>> option, T value) {
    }
    
    public <T extends java.lang.Object>void appendList(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.config.CompilerConfiguration $this$appendList, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.config.CompilerConfigurationKey<java.util.List<T>> option, @org.jetbrains.annotations.NotNull
    java.util.List<? extends T> values) {
    }
    
    public void applyOptionsFrom(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.config.CompilerConfiguration $this$applyOptionsFrom, @org.jetbrains.annotations.NotNull
    java.util.Map<java.lang.String, ? extends java.util.List<java.lang.String>> map, @org.jetbrains.annotations.NotNull
    java.util.Collection<? extends org.jetbrains.kotlin.compiler.plugin.AbstractCliOption> pluginOptions) {
    }
}