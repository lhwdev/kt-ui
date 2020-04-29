package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0082\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0002\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\u001b\u0012\n\u0010\u0004\u001a\u00060\u0005j\u0002`\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0010\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0015H\u0002J\u0017\u0010\u0016\u001a\u00020\u00022\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00020\u0018H\u0082\bJ\u001f\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u00032\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00020\u0018H\u0082\bJ\u0018\u0010\u001a\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010\u001e\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010!\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\"2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010#\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010$\u001a\u00020\u00022\u0006\u0010%\u001a\u00020&2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010\'\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020(2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010)\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010+\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020,2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010-\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020.2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010/\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u0002002\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u00101\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u0002022\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u00103\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u0002042\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u00105\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u00106\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u0002072\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u00108\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u0002092\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010:\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020;2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010<\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020=2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010>\u001a\u00020\u00022\u0006\u0010?\u001a\u00020@2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010A\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020B2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010C\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020D2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010E\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020F2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010G\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020H2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010I\u001a\u00020\u00022\u0006\u0010%\u001a\u00020J2\u0006\u0010\u001d\u001a\u00020\u0003H\u0016J\u0018\u0010K\u001a\u00020\r*\u00020L2\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u0002J\u0012\u0010M\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020*0NH\u0002J\u0018\u0010O\u001a\u00020\u0002*\u00020*2\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u0002J\u0018\u0010O\u001a\u00020\u0002*\u00020L2\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u0002J5\u0010P\u001a\u00020\u0002\"\u0004\b\u0000\u0010Q*\b\u0012\u0004\u0012\u0002HQ0R2\u0006\u0010S\u001a\u00020\u00032\u0012\u0010T\u001a\u000e\u0012\u0004\u0012\u0002HQ\u0012\u0004\u0012\u00020\u00020UH\u0082\bJ#\u0010V\u001a\u00020\u0002*\u00020*2\u0006\u0010\u0019\u001a\u00020\u00032\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00020\u0018H\u0082\bJ\u0012\u0010W\u001a\b\u0012\u0004\u0012\u00020F0N*\u00020\"H\u0002J\u001a\u0010X\u001a\b\u0012\u0004\u0012\u00020\u00030N*\u00020Y2\u0006\u0010Z\u001a\u00020[H\u0002J\u001a\u0010X\u001a\b\u0012\u0004\u0012\u00020\u00030N*\u00020\u00152\u0006\u0010Z\u001a\u00020[H\u0002J\u0014\u0010\\\u001a\u00020\u0003*\u00020\u00152\u0006\u0010]\u001a\u00020[H\u0002J\u0014\u0010^\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0003H\u0002R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006_"}, d2 = {"Lcom/asmx/ui/plugin/compiler/DumpIrTreeVisitor;", "Lorg/jetbrains/kotlin/ir/visitors/IrElementVisitor;", "", "", "out", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "normalizeNames", "", "(Ljava/lang/Appendable;Z)V", "elementRenderer", "Lcom/asmx/ui/plugin/compiler/RenderIrElementVisitor;", "printer", "Lorg/jetbrains/kotlin/utils/Printer;", "dumpAnnotations", "element", "Lorg/jetbrains/kotlin/ir/declarations/IrAnnotationContainer;", "dumpConstructorValueArguments", "expression", "Lorg/jetbrains/kotlin/ir/expressions/IrConstructorCall;", "dumpTypeArguments", "Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;", "indented", "body", "Lkotlin/Function0;", "label", "visitBranch", "branch", "Lorg/jetbrains/kotlin/ir/expressions/IrBranch;", "data", "visitClass", "declaration", "Lorg/jetbrains/kotlin/ir/declarations/IrClass;", "visitConstructor", "Lorg/jetbrains/kotlin/ir/declarations/IrConstructor;", "visitConstructorCall", "visitDoWhileLoop", "loop", "Lorg/jetbrains/kotlin/ir/expressions/IrDoWhileLoop;", "visitDynamicOperatorExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrDynamicOperatorExpression;", "visitElement", "Lorg/jetbrains/kotlin/ir/IrElement;", "visitEnumEntry", "Lorg/jetbrains/kotlin/ir/declarations/IrEnumEntry;", "visitErrorCallExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrErrorCallExpression;", "visitField", "Lorg/jetbrains/kotlin/ir/declarations/IrField;", "visitFile", "Lorg/jetbrains/kotlin/ir/declarations/IrFile;", "visitGetField", "Lorg/jetbrains/kotlin/ir/expressions/IrGetField;", "visitMemberAccess", "visitModuleFragment", "Lorg/jetbrains/kotlin/ir/declarations/IrModuleFragment;", "visitProperty", "Lorg/jetbrains/kotlin/ir/declarations/IrProperty;", "visitSetField", "Lorg/jetbrains/kotlin/ir/expressions/IrSetField;", "visitSimpleFunction", "Lorg/jetbrains/kotlin/ir/declarations/IrSimpleFunction;", "visitTry", "aTry", "Lorg/jetbrains/kotlin/ir/expressions/IrTry;", "visitTypeAlias", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeAlias;", "visitTypeOperator", "Lorg/jetbrains/kotlin/ir/expressions/IrTypeOperatorCall;", "visitTypeParameter", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeParameter;", "visitWhen", "Lorg/jetbrains/kotlin/ir/expressions/IrWhen;", "visitWhileLoop", "Lorg/jetbrains/kotlin/ir/expressions/IrWhileLoop;", "dump", "Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;", "dumpElements", "", "dumpInternal", "dumpItems", "T", "", "caption", "renderElement", "Lkotlin/Function1;", "dumpLabeledElementWith", "getFullTypeParametersList", "getTypeParameterNames", "Lorg/jetbrains/kotlin/ir/declarations/IrSymbolOwner;", "expectedCount", "", "renderTypeArgument", "index", "withLabel", "compiler-plugin"})
final class DumpIrTreeVisitor implements org.jetbrains.kotlin.ir.visitors.IrElementVisitor<kotlin.Unit, java.lang.String> {
    private final org.jetbrains.kotlin.utils.Printer printer = null;
    private final com.asmx.ui.plugin.compiler.RenderIrElementVisitor elementRenderer = null;
    
    @java.lang.Override
    public void visitElement(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement element, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitModuleFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrModuleFragment declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitFile(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFile declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrClass declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitTypeAlias(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeAlias declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitTypeParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeParameter declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitSimpleFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSimpleFunction declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    private final void dumpAnnotations(org.jetbrains.kotlin.ir.declarations.IrAnnotationContainer element) {
    }
    
    private final org.jetbrains.kotlin.utils.Printer dump(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrSymbol $this$dump, java.lang.String label) {
        return null;
    }
    
    @java.lang.Override
    public void visitConstructor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrConstructor declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrProperty declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrField declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    private final void dumpElements(@org.jetbrains.annotations.NotNull
    java.util.List<? extends org.jetbrains.kotlin.ir.IrElement> $this$dumpElements) {
    }
    
    @java.lang.Override
    public void visitErrorCallExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrErrorCallExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitEnumEntry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrEnumEntry declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitMemberAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConstructorCall expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    private final void dumpConstructorValueArguments(org.jetbrains.kotlin.ir.expressions.IrConstructorCall expression) {
    }
    
    private final void dumpTypeArguments(org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression expression) {
    }
    
    private final void dumpTypeArguments(org.jetbrains.kotlin.ir.expressions.IrConstructorCall expression) {
    }
    
    private final java.util.List<java.lang.String> getTypeParameterNames(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression $this$getTypeParameterNames, int expectedCount) {
        return null;
    }
    
    private final java.util.List<java.lang.String> getTypeParameterNames(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSymbolOwner $this$getTypeParameterNames, int expectedCount) {
        return null;
    }
    
    private final java.util.List<org.jetbrains.kotlin.ir.declarations.IrTypeParameter> getFullTypeParametersList(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrConstructor $this$getFullTypeParametersList) {
        return null;
    }
    
    private final java.lang.String renderTypeArgument(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression $this$renderTypeArgument, int index) {
        return null;
    }
    
    @java.lang.Override
    public void visitGetField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetField expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitSetField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetField expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitWhen(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhen expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitBranch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBranch branch, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhileLoop loop, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitDoWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop loop, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitTry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTry aTry, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitTypeOperator(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    @java.lang.Override
    public void visitDynamicOperatorExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicOperatorExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    private final void dumpLabeledElementWith(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement $this$dumpLabeledElementWith, java.lang.String label, kotlin.jvm.functions.Function0<kotlin.Unit> body) {
    }
    
    private final <T extends java.lang.Object>void dumpItems(@org.jetbrains.annotations.NotNull
    java.util.Collection<? extends T> $this$dumpItems, java.lang.String caption, kotlin.jvm.functions.Function1<? super T, kotlin.Unit> renderElement) {
    }
    
    private final void dumpInternal(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrSymbol $this$dumpInternal, java.lang.String label) {
    }
    
    private final void dumpInternal(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement $this$dumpInternal, java.lang.String label) {
    }
    
    private final void indented(java.lang.String label, kotlin.jvm.functions.Function0<kotlin.Unit> body) {
    }
    
    private final void indented(kotlin.jvm.functions.Function0<kotlin.Unit> body) {
    }
    
    private final java.lang.String withLabel(@org.jetbrains.annotations.NotNull
    java.lang.String $this$withLabel, java.lang.String label) {
        return null;
    }
    
    public DumpIrTreeVisitor(@org.jetbrains.annotations.NotNull
    java.lang.Appendable out, boolean normalizeNames) {
        super();
    }
    
    public void visitAnonymousInitializer(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrAnonymousInitializer declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitBlock(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlock expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitBlockBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlockBody body, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBody body, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitBreak(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreak jump, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitBreakContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreakContinue jump, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCall expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitCallableReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCallableReference expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitCatch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCatch aCatch, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitClassReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrClassReference expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitComposite(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrComposite expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public <T extends java.lang.Object>void visitConst(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConst<T> expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitContainerExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContainerExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContinue jump, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitDeclaration(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitDeclarationReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDeclarationReference expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitDelegatingConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitDynamicExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitDynamicMemberExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicMemberExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitElseBranch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrElseBranch branch, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitEnumConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrEnumConstructorCall expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitErrorDeclaration(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrErrorDeclaration declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitErrorExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrErrorExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitExpressionBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpressionBody body, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitExternalPackageFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrExternalPackageFragment declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitFieldAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFieldAccessExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitFunctionAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitFunctionExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitFunctionReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionReference expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitGetClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetClass expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitGetEnumValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetEnumValue expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitGetObjectValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetObjectValue expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitGetValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetValue expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitInstanceInitializerCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrInstanceInitializerCall expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitLocalDelegatedProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrLocalDelegatedProperty declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitLocalDelegatedPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLocalDelegatedPropertyReference expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLoop loop, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitPackageFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrPackageFragment declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrPropertyReference expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitReturn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrReturn expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitScript(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrScript declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitSetVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetVariable expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitSingletonReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetSingletonValue expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitSpreadElement(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSpreadElement spread, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitStringConcatenation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrStringConcatenation expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitSuspendableExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSuspendableExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitSuspensionPoint(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSuspensionPoint expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitSyntheticBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSyntheticBody body, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitThrow(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrThrow expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitValueAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrValueAccessExpression expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitValueParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrValueParameter declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitVararg(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrVararg expression, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
    
    public void visitVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrVariable declaration, @org.jetbrains.annotations.NotNull
    java.lang.String data) {
    }
}