package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0098\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u0002J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0010\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0016J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0017H\u0016J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0012\u001a\u00020\u001aH\u0016J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0018\u001a\u00020\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0018\u001a\u00020\u001eH\u0016J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010\u0018\u001a\u00020!H\u0016J\u0010\u0010\"\u001a\u00020#2\u0006\u0010\u0018\u001a\u00020#H\u0016J\u0010\u0010$\u001a\u00020%2\u0006\u0010\u0018\u001a\u00020%H\u0016J\u0014\u0010&\u001a\u00020\'*\u00020(2\u0006\u0010)\u001a\u00020*H\u0002J\u0014\u0010+\u001a\u00020\'*\u00020,2\u0006\u0010-\u001a\u00020,H\u0002J\f\u0010.\u001a\u00020/*\u00020/H\u0002J\f\u00100\u001a\u00020/*\u000201H\u0002J\u001e\u00102\u001a\u0002H3\"\n\b\u0000\u00103\u0018\u0001*\u00020(*\u0002H3H\u0082\b\u00a2\u0006\u0002\u00104J#\u00105\u001a\u0002H3\"\b\b\u0000\u00103*\u00020,*\u0002H32\u0006\u00106\u001a\u0002H3H\u0002\u00a2\u0006\u0002\u00107J#\u00108\u001a\u00020\'\"\b\b\u0000\u00103*\u00020,*\u0002H32\u0006\u00106\u001a\u0002H3H\u0002\u00a2\u0006\u0002\u00109R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006:"}, d2 = {"Lcom/asmx/ui/plugin/compiler/DeepCopyIrTreeWithSymbolsPreservingMetadata;", "Lorg/jetbrains/kotlin/ir/util/DeepCopyIrTreeWithSymbols;", "context", "Lorg/jetbrains/kotlin/backend/common/extensions/IrPluginContext;", "symbolRemapper", "Lorg/jetbrains/kotlin/ir/util/DeepCopySymbolRemapper;", "typeRemapper", "Lorg/jetbrains/kotlin/ir/util/TypeRemapper;", "typeTranslator", "Lorg/jetbrains/kotlin/ir/util/TypeTranslator;", "symbolRenamer", "Lorg/jetbrains/kotlin/ir/util/SymbolRenamer;", "(Lorg/jetbrains/kotlin/backend/common/extensions/IrPluginContext;Lorg/jetbrains/kotlin/ir/util/DeepCopySymbolRemapper;Lorg/jetbrains/kotlin/ir/util/TypeRemapper;Lorg/jetbrains/kotlin/ir/util/TypeTranslator;Lorg/jetbrains/kotlin/ir/util/SymbolRenamer;)V", "mapStatementOrigin", "Lorg/jetbrains/kotlin/ir/expressions/IrStatementOrigin;", "origin", "shallowCopyCall", "Lorg/jetbrains/kotlin/ir/expressions/IrCall;", "expression", "newCallee", "Lorg/jetbrains/kotlin/ir/symbols/IrSimpleFunctionSymbol;", "visitCall", "visitClass", "Lorg/jetbrains/kotlin/ir/declarations/IrClass;", "declaration", "visitConstructorCall", "Lorg/jetbrains/kotlin/ir/expressions/IrConstructorCall;", "visitField", "Lorg/jetbrains/kotlin/ir/declarations/IrField;", "visitFile", "Lorg/jetbrains/kotlin/ir/declarations/IrFile;", "visitFunction", "Lorg/jetbrains/kotlin/ir/IrStatement;", "Lorg/jetbrains/kotlin/ir/declarations/IrFunction;", "visitProperty", "Lorg/jetbrains/kotlin/ir/declarations/IrProperty;", "visitSimpleFunction", "Lorg/jetbrains/kotlin/ir/declarations/IrSimpleFunction;", "copyMetadataFrom", "", "Lorg/jetbrains/kotlin/ir/IrElement;", "owner", "Lorg/jetbrains/kotlin/ir/declarations/IrMetadataSourceOwner;", "copyRemappedTypeArgumentsFrom", "Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;", "other", "remapType", "Lorg/jetbrains/kotlin/ir/types/IrType;", "toIrType", "Lorg/jetbrains/kotlin/types/KotlinType;", "transform", "T", "(Lorg/jetbrains/kotlin/ir/IrElement;)Lorg/jetbrains/kotlin/ir/IrElement;", "transformReceiverArguments", "original", "(Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;)Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;", "transformValueArguments", "(Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;)V", "compiler-plugin"})
public final class DeepCopyIrTreeWithSymbolsPreservingMetadata extends org.jetbrains.kotlin.ir.util.DeepCopyIrTreeWithSymbols {
    private final org.jetbrains.kotlin.backend.common.extensions.IrPluginContext context = null;
    private final org.jetbrains.kotlin.ir.util.DeepCopySymbolRemapper symbolRemapper = null;
    private final org.jetbrains.kotlin.ir.util.TypeRemapper typeRemapper = null;
    private final org.jetbrains.kotlin.ir.util.TypeTranslator typeTranslator = null;
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.declarations.IrClass visitClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrClass declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.IrStatement visitFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.declarations.IrSimpleFunction visitSimpleFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSimpleFunction declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.declarations.IrField visitField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrField declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.declarations.IrProperty visitProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrProperty declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.declarations.IrFile visitFile(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFile declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrConstructorCall visitConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConstructorCall expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrCall visitCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCall expression) {
        return null;
    }
    
    private final org.jetbrains.kotlin.ir.expressions.IrCall shallowCopyCall(org.jetbrains.kotlin.ir.expressions.IrCall expression, org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol newCallee) {
        return null;
    }
    
    private final void copyRemappedTypeArgumentsFrom(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression $this$copyRemappedTypeArgumentsFrom, org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression other) {
    }
    
    private final <T extends org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression>void transformValueArguments(@org.jetbrains.annotations.NotNull
    T $this$transformValueArguments, T original) {
    }
    
    private final <T extends org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression>T transformReceiverArguments(@org.jetbrains.annotations.NotNull
    T $this$transformReceiverArguments, T original) {
        return null;
    }
    
    private final org.jetbrains.kotlin.ir.types.IrType remapType(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$remapType) {
        return null;
    }
    
    private final org.jetbrains.kotlin.ir.expressions.IrStatementOrigin mapStatementOrigin(org.jetbrains.kotlin.ir.expressions.IrStatementOrigin origin) {
        return null;
    }
    
    private final void copyMetadataFrom(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement $this$copyMetadataFrom, org.jetbrains.kotlin.ir.declarations.IrMetadataSourceOwner owner) {
    }
    
    private final org.jetbrains.kotlin.ir.types.IrType toIrType(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.KotlinType $this$toIrType) {
        return null;
    }
    
    public DeepCopyIrTreeWithSymbolsPreservingMetadata(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.backend.common.extensions.IrPluginContext context, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.DeepCopySymbolRemapper symbolRemapper, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.TypeRemapper typeRemapper, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.TypeTranslator typeTranslator, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.SymbolRenamer symbolRenamer) {
        super(null, null);
    }
}