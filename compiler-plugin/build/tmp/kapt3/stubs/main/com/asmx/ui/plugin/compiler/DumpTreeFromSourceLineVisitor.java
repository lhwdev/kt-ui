package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\u0010\u0006\u001a\u00060\u0007j\u0002`\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/asmx/ui/plugin/compiler/DumpTreeFromSourceLineVisitor;", "Lorg/jetbrains/kotlin/ir/visitors/IrElementVisitorVoid;", "fileEntry", "Lorg/jetbrains/kotlin/ir/SourceManager$FileEntry;", "lineNumber", "", "out", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "normalizeNames", "", "(Lorg/jetbrains/kotlin/ir/SourceManager$FileEntry;ILjava/lang/Appendable;Z)V", "dumper", "Lcom/asmx/ui/plugin/compiler/DumpIrTreeVisitor;", "getFileEntry", "()Lorg/jetbrains/kotlin/ir/SourceManager$FileEntry;", "visitElement", "", "element", "Lorg/jetbrains/kotlin/ir/IrElement;", "compiler-plugin"})
final class DumpTreeFromSourceLineVisitor implements org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid {
    private final com.asmx.ui.plugin.compiler.DumpIrTreeVisitor dumper = null;
    @org.jetbrains.annotations.NotNull
    private final org.jetbrains.kotlin.ir.SourceManager.FileEntry fileEntry = null;
    private final int lineNumber = 0;
    
    @java.lang.Override
    public void visitElement(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement element) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final org.jetbrains.kotlin.ir.SourceManager.FileEntry getFileEntry() {
        return null;
    }
    
    public DumpTreeFromSourceLineVisitor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.SourceManager.FileEntry fileEntry, int lineNumber, @org.jetbrains.annotations.NotNull
    java.lang.Appendable out, boolean normalizeNames) {
        super();
    }
    
    @java.lang.Override
    public void visitElement(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement element, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitAnonymousInitializer(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrAnonymousInitializer declaration) {
    }
    
    @java.lang.Override
    public void visitAnonymousInitializer(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrAnonymousInitializer declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitBlock(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlock expression) {
    }
    
    @java.lang.Override
    public void visitBlock(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlock expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitBlockBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlockBody body) {
    }
    
    @java.lang.Override
    public void visitBlockBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlockBody body, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBody body) {
    }
    
    @java.lang.Override
    public void visitBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBody body, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitBranch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBranch branch) {
    }
    
    @java.lang.Override
    public void visitBranch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBranch branch, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitBreak(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreak jump) {
    }
    
    @java.lang.Override
    public void visitBreak(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreak jump, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitBreakContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreakContinue jump) {
    }
    
    @java.lang.Override
    public void visitBreakContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreakContinue jump, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCall expression) {
    }
    
    @java.lang.Override
    public void visitCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitCallableReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCallableReference expression) {
    }
    
    @java.lang.Override
    public void visitCallableReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCallableReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitCatch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCatch aCatch) {
    }
    
    @java.lang.Override
    public void visitCatch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCatch aCatch, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrClass declaration) {
    }
    
    @java.lang.Override
    public void visitClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrClass declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitClassReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrClassReference expression) {
    }
    
    @java.lang.Override
    public void visitClassReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrClassReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitComposite(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrComposite expression) {
    }
    
    @java.lang.Override
    public void visitComposite(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrComposite expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public <T extends java.lang.Object>void visitConst(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConst<T> expression) {
    }
    
    @java.lang.Override
    public <T extends java.lang.Object>void visitConst(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConst<T> expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitConstructor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrConstructor declaration) {
    }
    
    @java.lang.Override
    public void visitConstructor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrConstructor declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConstructorCall expression) {
    }
    
    @java.lang.Override
    public void visitConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConstructorCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitContainerExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContainerExpression expression) {
    }
    
    @java.lang.Override
    public void visitContainerExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContainerExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContinue jump) {
    }
    
    @java.lang.Override
    public void visitContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContinue jump, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitDeclaration(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration declaration) {
    }
    
    @java.lang.Override
    public void visitDeclaration(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitDeclarationReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDeclarationReference expression) {
    }
    
    @java.lang.Override
    public void visitDeclarationReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDeclarationReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitDelegatingConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall expression) {
    }
    
    @java.lang.Override
    public void visitDelegatingConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitDoWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop loop) {
    }
    
    @java.lang.Override
    public void visitDoWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop loop, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitDynamicExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicExpression expression) {
    }
    
    @java.lang.Override
    public void visitDynamicExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitDynamicMemberExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicMemberExpression expression) {
    }
    
    @java.lang.Override
    public void visitDynamicMemberExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicMemberExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitDynamicOperatorExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicOperatorExpression expression) {
    }
    
    @java.lang.Override
    public void visitDynamicOperatorExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicOperatorExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitElseBranch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrElseBranch branch) {
    }
    
    @java.lang.Override
    public void visitElseBranch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrElseBranch branch, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitEnumConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrEnumConstructorCall expression) {
    }
    
    @java.lang.Override
    public void visitEnumConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrEnumConstructorCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitEnumEntry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrEnumEntry declaration) {
    }
    
    @java.lang.Override
    public void visitEnumEntry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrEnumEntry declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitErrorCallExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrErrorCallExpression expression) {
    }
    
    @java.lang.Override
    public void visitErrorCallExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrErrorCallExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitErrorDeclaration(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrErrorDeclaration declaration) {
    }
    
    @java.lang.Override
    public void visitErrorDeclaration(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrErrorDeclaration declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitErrorExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrErrorExpression expression) {
    }
    
    @java.lang.Override
    public void visitErrorExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrErrorExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression expression) {
    }
    
    @java.lang.Override
    public void visitExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitExpressionBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpressionBody body) {
    }
    
    @java.lang.Override
    public void visitExpressionBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpressionBody body, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitExternalPackageFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrExternalPackageFragment declaration) {
    }
    
    @java.lang.Override
    public void visitExternalPackageFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrExternalPackageFragment declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrField declaration) {
    }
    
    @java.lang.Override
    public void visitField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrField declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitFieldAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFieldAccessExpression expression) {
    }
    
    @java.lang.Override
    public void visitFieldAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFieldAccessExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitFile(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFile declaration) {
    }
    
    @java.lang.Override
    public void visitFile(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFile declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction declaration) {
    }
    
    @java.lang.Override
    public void visitFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitFunctionAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression expression) {
    }
    
    @java.lang.Override
    public void visitFunctionAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitFunctionExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionExpression expression) {
    }
    
    @java.lang.Override
    public void visitFunctionExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitFunctionReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionReference expression) {
    }
    
    @java.lang.Override
    public void visitFunctionReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitGetClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetClass expression) {
    }
    
    @java.lang.Override
    public void visitGetClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetClass expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitGetEnumValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetEnumValue expression) {
    }
    
    @java.lang.Override
    public void visitGetEnumValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetEnumValue expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitGetField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetField expression) {
    }
    
    @java.lang.Override
    public void visitGetField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetField expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitGetObjectValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetObjectValue expression) {
    }
    
    @java.lang.Override
    public void visitGetObjectValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetObjectValue expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitGetValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetValue expression) {
    }
    
    @java.lang.Override
    public void visitGetValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetValue expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitInstanceInitializerCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrInstanceInitializerCall expression) {
    }
    
    @java.lang.Override
    public void visitInstanceInitializerCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrInstanceInitializerCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitLocalDelegatedProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrLocalDelegatedProperty declaration) {
    }
    
    @java.lang.Override
    public void visitLocalDelegatedProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrLocalDelegatedProperty declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitLocalDelegatedPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLocalDelegatedPropertyReference expression) {
    }
    
    @java.lang.Override
    public void visitLocalDelegatedPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLocalDelegatedPropertyReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLoop loop) {
    }
    
    @java.lang.Override
    public void visitLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLoop loop, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitMemberAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression expression) {
    }
    
    @java.lang.Override
    public void visitMemberAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitModuleFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrModuleFragment declaration) {
    }
    
    @java.lang.Override
    public void visitModuleFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrModuleFragment declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitPackageFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrPackageFragment declaration) {
    }
    
    @java.lang.Override
    public void visitPackageFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrPackageFragment declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrProperty declaration) {
    }
    
    @java.lang.Override
    public void visitProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrProperty declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrPropertyReference expression) {
    }
    
    @java.lang.Override
    public void visitPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrPropertyReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitReturn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrReturn expression) {
    }
    
    @java.lang.Override
    public void visitReturn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrReturn expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitScript(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrScript declaration) {
    }
    
    @java.lang.Override
    public void visitScript(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrScript declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitSetField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetField expression) {
    }
    
    @java.lang.Override
    public void visitSetField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetField expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitSetVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetVariable expression) {
    }
    
    @java.lang.Override
    public void visitSetVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetVariable expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitSimpleFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSimpleFunction declaration) {
    }
    
    @java.lang.Override
    public void visitSimpleFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSimpleFunction declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitSingletonReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetSingletonValue expression) {
    }
    
    @java.lang.Override
    public void visitSingletonReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetSingletonValue expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitSpreadElement(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSpreadElement spread) {
    }
    
    @java.lang.Override
    public void visitSpreadElement(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSpreadElement spread, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitStringConcatenation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrStringConcatenation expression) {
    }
    
    @java.lang.Override
    public void visitStringConcatenation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrStringConcatenation expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitSuspendableExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSuspendableExpression expression) {
    }
    
    @java.lang.Override
    public void visitSuspendableExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSuspendableExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitSuspensionPoint(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSuspensionPoint expression) {
    }
    
    @java.lang.Override
    public void visitSuspensionPoint(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSuspensionPoint expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitSyntheticBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSyntheticBody body) {
    }
    
    @java.lang.Override
    public void visitSyntheticBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSyntheticBody body, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitThrow(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrThrow expression) {
    }
    
    @java.lang.Override
    public void visitThrow(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrThrow expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitTry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTry aTry) {
    }
    
    @java.lang.Override
    public void visitTry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTry aTry, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitTypeAlias(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeAlias declaration) {
    }
    
    @java.lang.Override
    public void visitTypeAlias(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeAlias declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitTypeOperator(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall expression) {
    }
    
    @java.lang.Override
    public void visitTypeOperator(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitTypeParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeParameter declaration) {
    }
    
    @java.lang.Override
    public void visitTypeParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeParameter declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    @java.lang.Override
    public void visitValueAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrValueAccessExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitValueParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrValueParameter declaration) {
    }
    
    @java.lang.Override
    public void visitValueParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrValueParameter declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitVararg(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrVararg expression) {
    }
    
    @java.lang.Override
    public void visitVararg(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrVararg expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrVariable declaration) {
    }
    
    @java.lang.Override
    public void visitVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrVariable declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitVariableAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrValueAccessExpression expression) {
    }
    
    public void visitWhen(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhen expression) {
    }
    
    @java.lang.Override
    public void visitWhen(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhen expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
    
    public void visitWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhileLoop loop) {
    }
    
    @java.lang.Override
    public void visitWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhileLoop loop, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
    }
}