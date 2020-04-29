package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u0012\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\'\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\rB\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\u000eB)\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\u0011B\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u00a2\u0006\u0002\u0010\u0012B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0013B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u00a2\u0006\u0002\u0010\u0014J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u001aH\u0016J\b\u0010\u001c\u001a\u00020\u001aH\u0002J\u0010\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\fH\u0002J\u0010\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020!H\u0016J \u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\n2\u0006\u0010#\u001a\u00020\nH\u0016J\u0010\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020\nH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/asmx/ui/plugin/compiler/WriterOutputStream;", "Ljava/io/OutputStream;", "writer", "Ljava/io/Writer;", "decoder", "Ljava/nio/charset/CharsetDecoder;", "(Ljava/io/Writer;Ljava/nio/charset/CharsetDecoder;)V", "charset", "Ljava/nio/charset/Charset;", "bufferSize", "", "writeImmediately", "", "(Ljava/io/Writer;Ljava/nio/charset/Charset;IZ)V", "(Ljava/io/Writer;Ljava/nio/charset/Charset;)V", "charsetName", "", "(Ljava/io/Writer;Ljava/lang/String;IZ)V", "(Ljava/io/Writer;Ljava/lang/String;)V", "(Ljava/io/Writer;)V", "(Ljava/io/Writer;Ljava/nio/charset/CharsetDecoder;IZ)V", "decoderIn", "Ljava/nio/ByteBuffer;", "decoderOut", "Ljava/nio/CharBuffer;", "close", "", "flush", "flushOutput", "processInput", "endOfInput", "write", "b", "", "off", "len", "compiler-plugin"})
public final class WriterOutputStream extends java.io.OutputStream {
    private final java.io.Writer writer = null;
    private final java.nio.charset.CharsetDecoder decoder = null;
    private final boolean writeImmediately = false;
    private final java.nio.ByteBuffer decoderIn = null;
    private final java.nio.CharBuffer decoderOut = null;
    
    @java.lang.Override
    public void write(@org.jetbrains.annotations.NotNull
    byte[] b, int off, int len) throws java.io.IOException {
    }
    
    @java.lang.Override
    public void write(@org.jetbrains.annotations.NotNull
    byte[] b) throws java.io.IOException {
    }
    
    @java.lang.Override
    public void write(int b) throws java.io.IOException {
    }
    
    @java.lang.Override
    public void flush() throws java.io.IOException {
    }
    
    @java.lang.Override
    public void close() throws java.io.IOException {
    }
    
    private final void processInput(boolean endOfInput) throws java.io.IOException {
    }
    
    private final void flushOutput() throws java.io.IOException {
    }
    
    public WriterOutputStream(@org.jetbrains.annotations.NotNull
    java.io.Writer writer, @org.jetbrains.annotations.NotNull
    java.nio.charset.CharsetDecoder decoder, int bufferSize, boolean writeImmediately) {
        super();
    }
    
    public WriterOutputStream(@org.jetbrains.annotations.NotNull
    java.io.Writer writer, @org.jetbrains.annotations.NotNull
    java.nio.charset.CharsetDecoder decoder) {
        super();
    }
    
    public WriterOutputStream(@org.jetbrains.annotations.NotNull
    java.io.Writer writer, @org.jetbrains.annotations.NotNull
    java.nio.charset.Charset charset, int bufferSize, boolean writeImmediately) {
        super();
    }
    
    public WriterOutputStream(@org.jetbrains.annotations.NotNull
    java.io.Writer writer, @org.jetbrains.annotations.NotNull
    java.nio.charset.Charset charset) {
        super();
    }
    
    public WriterOutputStream(@org.jetbrains.annotations.NotNull
    java.io.Writer writer, @org.jetbrains.annotations.Nullable
    java.lang.String charsetName, int bufferSize, boolean writeImmediately) {
        super();
    }
    
    public WriterOutputStream(@org.jetbrains.annotations.NotNull
    java.io.Writer writer, @org.jetbrains.annotations.Nullable
    java.lang.String charsetName) {
        super();
    }
    
    public WriterOutputStream(@org.jetbrains.annotations.NotNull
    java.io.Writer writer) {
        super();
    }
}