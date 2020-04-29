package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 2, xi = 2, d1 = {"\u0000\u00a6\u0001\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a&\u0010\u000f\u001a\u00020\u00012\u001b\u0010\u0010\u001a\u0017\u0012\b\u0012\u00060\u0012j\u0002`\u0013\u0012\u0004\u0012\u00020\u00140\u0011\u00a2\u0006\u0002\b\u0015H\u0082\b\u001aF\u0010\u0016\u001a\n\u0018\u00010\u0012j\u0004\u0018\u0001`\u00132\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00012\b\u0010\u001a\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\n\u0010\u001e\u001a\u00060\u0012j\u0002`\u0013H\u0002\u001a\u0010\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u0001H\u0002\u001a\u0016\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010!2\u0006\u0010\"\u001a\u00020\u0018H\u0002\u001a\u0010\u0010#\u001a\u00020\u00012\u0006\u0010$\u001a\u00020%H\u0002\u001a\u0016\u0010&\u001a\u00020\u00012\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020%0!H\u0002\u001a`\u0010(\u001a\u00020\u0014\"\u0004\b\u0000\u0010)*\u00060\u0012j\u0002`\u00132\f\u0010*\u001a\b\u0012\u0004\u0012\u0002H)0!2\u0006\u0010+\u001a\u00020\u00012\u0006\u0010,\u001a\u00020\u00012\u0006\u0010-\u001a\u00020\u00012!\u0010.\u001a\u001d\u0012\b\u0012\u00060\u0012j\u0002`\u0013\u0012\u0004\u0012\u0002H)\u0012\u0004\u0012\u00020\u00140/\u00a2\u0006\u0002\b\u0015H\u0082\b\u001a\n\u00100\u001a\u00020\u0001*\u000201\u001a\n\u00102\u001a\u00020\u0001*\u000201\u001a\u0014\u00103\u001a\u00020\u0001*\u0002042\b\b\u0002\u00105\u001a\u00020\u001c\u001a\u001c\u00106\u001a\u00020\u0001*\u0002072\u0006\u00108\u001a\u00020\u00182\b\b\u0002\u00105\u001a\u00020\u001c\u001a\u0012\u00109\u001a\b\u0012\u0004\u0012\u00020\u00010!*\u00020:H\u0002\u001a\u001e\u0010;\u001a\u00020\u0014*\u0002042\b\b\u0002\u0010<\u001a\u00020\u00012\b\b\u0002\u00105\u001a\u00020\u001c\u001a\f\u0010=\u001a\u00020\u0001*\u00020>H\u0002\u001a\f\u0010?\u001a\u00020\u0001*\u00020@H\u0002\u001a\u001a\u0010A\u001a\u00020\u0014*\u00060\u0012j\u0002`\u00132\b\u0010B\u001a\u0004\u0018\u000104H\u0002\u001a\f\u0010C\u001a\u00020\u0001*\u00020DH\u0002\u001a\f\u0010E\u001a\u00020\u0001*\u00020FH\u0002\u001a\f\u0010G\u001a\u00020\u0001*\u000204H\u0002\u001a\u0018\u0010H\u001a\u00020\u0014*\u00020>2\n\u0010I\u001a\u00060\u0012j\u0002`\u0013H\u0002\u001a\u0018\u0010J\u001a\u00020\u0014*\u00020>2\n\u0010I\u001a\u00060\u0012j\u0002`\u0013H\u0002\u001a\u0014\u0010K\u001a\u00020\u0001*\u00020L2\u0006\u0010M\u001a\u000201H\u0002\u001a\f\u0010N\u001a\u00020\u0001*\u00020>H\u0002\u001a\n\u0010O\u001a\u00020\u0001*\u00020P\u001a\f\u0010Q\u001a\u00020\u0001*\u00020RH\u0002\u001a\f\u0010S\u001a\u00020\u0001*\u00020TH\u0002\u001a\f\u0010U\u001a\u00020\u0001*\u00020@H\u0002\u001a\f\u0010V\u001a\u00020\u0001*\u00020PH\u0002\u001a\f\u0010W\u001a\u00020\u0001*\u00020XH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u000b\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\r\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u000e\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006Y"}, d2 = {"sDimmed", "", "sIdentifier", "sIdentifierDimmed", "sModality", "sName", "sNumber", "sProperty", "sProperty2", "sReference", "sReferenceColor", "sReset", "sType", "sUnboundType", "sVisibility", "buildTrimEnd", "fn", "Lkotlin/Function1;", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "", "Lkotlin/ExtensionFunctionType;", "escapeStringCharacters", "length", "", "str", "additionalChars", "escapeSlash", "", "escapeUnicode", "buffer", "s", "getPlaceholderParameterNames", "", "expectedCount", "renderAsAnnotation", "irAnnotation", "Lorg/jetbrains/kotlin/ir/expressions/IrConstructorCall;", "renderTypeAnnotations", "annotations", "appendListWith", "T", "list", "prefix", "postfix", "separator", "renderItem", "Lkotlin/Function2;", "dump", "Lorg/jetbrains/kotlin/descriptors/DeclarationDescriptor;", "dumpAsType", "dumpColored", "Lorg/jetbrains/kotlin/ir/IrElement;", "normalizeNames", "dumpTreesFromLineNumber", "Lorg/jetbrains/kotlin/ir/declarations/IrFile;", "lineNumber", "getValueParameterNamesForDebug", "Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;", "logDumpColored", "baseColor", "name", "Lorg/jetbrains/kotlin/ir/declarations/IrDeclaration;", "render", "Lorg/jetbrains/kotlin/ir/types/IrTypeArgument;", "renderAsAnnotationArgument", "irElement", "renderClassFqn", "Lorg/jetbrains/kotlin/ir/declarations/IrClass;", "renderClassifierFqn", "Lorg/jetbrains/kotlin/ir/symbols/IrClassifierSymbol;", "renderColored", "renderDeclarationFqn", "sb", "renderDeclarationParentFqn", "renderDescriptor", "Lorg/jetbrains/kotlin/renderer/DescriptorRenderer;", "descriptor", "renderOriginIfNonTrivial", "renderReadable", "Lorg/jetbrains/kotlin/ir/types/IrType;", "renderTypeAbbreviation", "Lorg/jetbrains/kotlin/ir/types/IrTypeAbbreviation;", "renderTypeAliasFqn", "Lorg/jetbrains/kotlin/ir/symbols/IrTypeAliasSymbol;", "renderTypeArgument", "renderTypeInner", "renderTypeParameterFqn", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeParameter;", "compiler-plugin"})
public final class DumpIrTreeWithColorKt {
    private static final java.lang.String sReset = "\u001b[0m";
    private static final java.lang.String sName = "\u001b[0m\u001b[1m";
    private static final java.lang.String sIdentifier = "\u001b[0;34m";
    private static final java.lang.String sIdentifierDimmed = "\u001b[0;94m";
    private static final java.lang.String sProperty = "\u001b[0;91m";
    private static final java.lang.String sProperty2 = "\u001b[0;91m";
    private static final java.lang.String sType = "\u001b[0;97m";
    private static final java.lang.String sUnboundType = "\u001b[0;97m";
    private static final java.lang.String sNumber = "\u001b[0;91m";
    private static final java.lang.String sModality = "\u001b[0;35m";
    private static final java.lang.String sVisibility = "\u001b[0;35m";
    private static final java.lang.String sReference = "\u001b[0;92m-> \u001b[0m";
    private static final java.lang.String sReferenceColor = "\u001b[0m";
    private static final java.lang.String sDimmed = "\u001b[0;37m";
    
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String renderReadable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$renderReadable) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String dump(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.DeclarationDescriptor $this$dump) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String dumpAsType(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.DeclarationDescriptor $this$dumpAsType) {
        return null;
    }
    
    private static final java.lang.String renderTypeInner(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$renderTypeInner) {
        return null;
    }
    
    private static final java.lang.String renderTypeArgument(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrTypeArgument $this$renderTypeArgument) {
        return null;
    }
    
    private static final java.lang.String renderTypeAbbreviation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrTypeAbbreviation $this$renderTypeAbbreviation) {
        return null;
    }
    
    private static final java.lang.String buildTrimEnd(kotlin.jvm.functions.Function1<? super java.lang.StringBuilder, kotlin.Unit> fn) {
        return null;
    }
    
    private static final java.lang.String renderTypeAnnotations(java.util.List<? extends org.jetbrains.kotlin.ir.expressions.IrConstructorCall> annotations) {
        return null;
    }
    
    private static final java.lang.String renderAsAnnotation(org.jetbrains.kotlin.ir.expressions.IrConstructorCall irAnnotation) {
        return null;
    }
    
    private static final void renderAsAnnotationArgument(@org.jetbrains.annotations.NotNull
    java.lang.StringBuilder $this$renderAsAnnotationArgument, org.jetbrains.kotlin.ir.IrElement irElement) {
    }
    
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String dumpColored(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement $this$dumpColored, boolean normalizeNames) {
        return null;
    }
    
    public static final void logDumpColored(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement $this$logDumpColored, @org.jetbrains.annotations.NotNull
    java.lang.String baseColor, boolean normalizeNames) {
    }
    
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String dumpTreesFromLineNumber(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFile $this$dumpTreesFromLineNumber, int lineNumber, boolean normalizeNames) {
        return null;
    }
    
    private static final java.util.List<java.lang.String> getValueParameterNamesForDebug(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression $this$getValueParameterNamesForDebug) {
        return null;
    }
    
    private static final java.util.List<java.lang.String> getPlaceholderParameterNames(int expectedCount) {
        return null;
    }
    
    private static final java.lang.String renderColored(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement $this$renderColored) {
        return null;
    }
    
    private static final java.lang.String name(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration $this$name) {
        return null;
    }
    
    private static final java.lang.String renderDescriptor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.renderer.DescriptorRenderer $this$renderDescriptor, org.jetbrains.kotlin.descriptors.DeclarationDescriptor descriptor) {
        return null;
    }
    
    private static final java.lang.String renderOriginIfNonTrivial(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration $this$renderOriginIfNonTrivial) {
        return null;
    }
    
    private static final java.lang.String renderClassifierFqn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol $this$renderClassifierFqn) {
        return null;
    }
    
    private static final java.lang.String renderTypeAliasFqn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol $this$renderTypeAliasFqn) {
        return null;
    }
    
    private static final java.lang.String renderClassFqn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrClass $this$renderClassFqn) {
        return null;
    }
    
    private static final java.lang.String renderTypeParameterFqn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeParameter $this$renderTypeParameterFqn) {
        return null;
    }
    
    private static final void renderDeclarationFqn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration $this$renderDeclarationFqn, java.lang.StringBuilder sb) {
    }
    
    private static final void renderDeclarationParentFqn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration $this$renderDeclarationParentFqn, java.lang.StringBuilder sb) {
    }
    
    private static final java.lang.String render(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrTypeArgument $this$render) {
        return null;
    }
    
    private static final <T extends java.lang.Object>void appendListWith(@org.jetbrains.annotations.NotNull
    java.lang.StringBuilder $this$appendListWith, java.util.List<? extends T> list, java.lang.String prefix, java.lang.String postfix, java.lang.String separator, kotlin.jvm.functions.Function2<? super java.lang.StringBuilder, ? super T, kotlin.Unit> renderItem) {
    }
    
    private static final java.lang.String escapeStringCharacters(java.lang.String s) {
        return null;
    }
    
    private static final java.lang.StringBuilder escapeStringCharacters(int length, java.lang.String str, java.lang.String additionalChars, boolean escapeSlash, boolean escapeUnicode, java.lang.StringBuilder buffer) {
        return null;
    }
}