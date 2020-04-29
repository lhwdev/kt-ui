package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u00f2\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0016\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0014J\u0010\u0010\f\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020\u000eH\u0016J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u0010\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020\u001eH\u0016J\u0010\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020 H\u0016J\u0010\u0010!\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\"H\u0016J\u0010\u0010#\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020$H\u0016J\u0010\u0010%\u001a\u00020\u00112\u0006\u0010&\u001a\u00020\'H\u0016J\u0010\u0010(\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020)H\u0016J\u0010\u0010*\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020+H\u0016J\u0010\u0010,\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020-H\u0016J\u0010\u0010.\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020/H\u0016J\u0010\u00100\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u000201H\u0016J\u0010\u00102\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u000203H\u0016J\u0010\u00104\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u000205H\u0016J\u0010\u00106\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u000207H\u0016J\u0010\u00108\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u000209H\u0016J\u0010\u0010:\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020;H\u0016J\u0010\u0010<\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020=H\u0016J\u0010\u0010>\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020?H\u0016J\u0010\u0010@\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020AH\u0016J\u0010\u0010B\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020CH\u0016J\u0010\u0010D\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020EH\u0016J\u0010\u0010F\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020GH\u0016J\u0010\u0010H\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020IH\u0016J\u0010\u0010J\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020KH\u0016J\u0010\u0010L\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020MH\u0016J\u0010\u0010N\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020OH\u0016J\u0010\u0010P\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020QH\u0016J\u0010\u0010R\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020SH\u0016J\u0010\u0010T\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020UH\u0016J\u0010\u0010V\u001a\u00020\u00112\u0006\u0010W\u001a\u00020XH\u0016J\u0010\u0010Y\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020ZH\u0016J\u0010\u0010[\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020\\H\u0016J\u0010\u0010]\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020^H\u0016J\u0010\u0010_\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020`H\u0016J\u0010\u0010a\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020bH\u0016J\u0010\u0010c\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020dH\u0016J\u0010\u0010e\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020fH\u0016J\u0010\u0010g\u001a\u00020\u00112\u0006\u0010&\u001a\u00020hH\u0016J\u0014\u0010i\u001a\u00020j*\u00020k2\u0006\u0010l\u001a\u00020kH\u0002J\u0014\u0010m\u001a\u00020j*\u00020n2\u0006\u0010l\u001a\u00020nH\u0002J\u0014\u0010o\u001a\u00020p*\u00020\u00032\u0006\u0010q\u001a\u00020pH\u0002J\f\u0010r\u001a\u00020s*\u00020sH\u0002J\u001e\u0010t\u001a\u0002Hu\"\n\b\u0000\u0010u\u0018\u0001*\u00020v*\u0002HuH\u0082\b\u00a2\u0006\u0002\u0010wJ%\u0010t\u001a\b\u0012\u0004\u0012\u0002Hu0x\"\n\b\u0000\u0010u\u0018\u0001*\u00020v*\b\u0012\u0004\u0012\u0002Hu0xH\u0082\bJ)\u0010y\u001a\b\u0012\u0004\u0012\u00020{0z\"\b\b\u0000\u0010u*\u00020|*\u0002Hu2\u0006\u0010}\u001a\u0002HuH\u0002\u00a2\u0006\u0002\u0010~J%\u0010\u007f\u001a\u0002Hu\"\b\b\u0000\u0010u*\u00020k*\u0002Hu2\u0007\u0010\u0080\u0001\u001a\u0002HuH\u0002\u00a2\u0006\u0003\u0010\u0081\u0001J4\u0010\u0082\u0001\u001a\b\u0012\u0004\u0012\u0002Hu0z\"\n\b\u0000\u0010u\u0018\u0001*\u00020v*\b\u0012\u0004\u0012\u0002Hu0x2\f\u0010}\u001a\b\u0012\u0004\u0012\u0002Hu0zH\u0082\bJ&\u0010\u0083\u0001\u001a\u00020j\"\b\b\u0000\u0010u*\u00020k*\u0002Hu2\u0007\u0010\u0080\u0001\u001a\u0002HuH\u0002\u00a2\u0006\u0003\u0010\u0084\u0001R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0085\u0001"}, d2 = {"Lcom/asmx/ui/plugin/compiler/IrTreeTypeTransformerPreservingMetadata;", "Lorg/jetbrains/kotlin/ir/visitors/IrElementTransformerVoid;", "symbolRemapper", "Lorg/jetbrains/kotlin/ir/util/SymbolRemapper;", "typeRemapper", "Lorg/jetbrains/kotlin/ir/util/TypeRemapper;", "(Lorg/jetbrains/kotlin/ir/util/SymbolRemapper;Lorg/jetbrains/kotlin/ir/util/TypeRemapper;)V", "transformedLoops", "Ljava/util/HashMap;", "Lorg/jetbrains/kotlin/ir/expressions/IrLoop;", "getNonTransformedLoop", "irLoop", "getTransformedLoop", "shallowCopyCall", "Lorg/jetbrains/kotlin/ir/expressions/IrCall;", "expression", "visitBlock", "Lorg/jetbrains/kotlin/ir/expressions/IrExpression;", "Lorg/jetbrains/kotlin/ir/expressions/IrBlock;", "visitBreak", "jump", "Lorg/jetbrains/kotlin/ir/expressions/IrBreak;", "visitCall", "visitClass", "Lorg/jetbrains/kotlin/ir/IrStatement;", "declaration", "Lorg/jetbrains/kotlin/ir/declarations/IrClass;", "visitClassReference", "Lorg/jetbrains/kotlin/ir/expressions/IrClassReference;", "visitComposite", "Lorg/jetbrains/kotlin/ir/expressions/IrComposite;", "visitConstructorCall", "Lorg/jetbrains/kotlin/ir/expressions/IrConstructorCall;", "visitContinue", "Lorg/jetbrains/kotlin/ir/expressions/IrContinue;", "visitDelegatingConstructorCall", "Lorg/jetbrains/kotlin/ir/expressions/IrDelegatingConstructorCall;", "visitDoWhileLoop", "loop", "Lorg/jetbrains/kotlin/ir/expressions/IrDoWhileLoop;", "visitDynamicMemberExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrDynamicMemberExpression;", "visitDynamicOperatorExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrDynamicOperatorExpression;", "visitEnumConstructorCall", "Lorg/jetbrains/kotlin/ir/expressions/IrEnumConstructorCall;", "visitErrorCallExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrErrorCallExpression;", "visitErrorExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrErrorExpression;", "visitField", "Lorg/jetbrains/kotlin/ir/declarations/IrField;", "visitFunction", "Lorg/jetbrains/kotlin/ir/declarations/IrFunction;", "visitFunctionExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrFunctionExpression;", "visitFunctionReference", "Lorg/jetbrains/kotlin/ir/expressions/IrFunctionReference;", "visitGetClass", "Lorg/jetbrains/kotlin/ir/expressions/IrGetClass;", "visitGetEnumValue", "Lorg/jetbrains/kotlin/ir/expressions/IrGetEnumValue;", "visitGetField", "Lorg/jetbrains/kotlin/ir/expressions/IrGetField;", "visitGetObjectValue", "Lorg/jetbrains/kotlin/ir/expressions/IrGetObjectValue;", "visitGetValue", "Lorg/jetbrains/kotlin/ir/expressions/IrGetValue;", "visitInstanceInitializerCall", "Lorg/jetbrains/kotlin/ir/expressions/IrInstanceInitializerCall;", "visitLocalDelegatedProperty", "Lorg/jetbrains/kotlin/ir/declarations/IrLocalDelegatedProperty;", "visitLocalDelegatedPropertyReference", "Lorg/jetbrains/kotlin/ir/expressions/IrLocalDelegatedPropertyReference;", "visitPropertyReference", "Lorg/jetbrains/kotlin/ir/expressions/IrPropertyReference;", "visitReturn", "Lorg/jetbrains/kotlin/ir/expressions/IrReturn;", "visitSetField", "Lorg/jetbrains/kotlin/ir/expressions/IrSetField;", "visitSetVariable", "Lorg/jetbrains/kotlin/ir/expressions/IrSetVariable;", "visitStringConcatenation", "Lorg/jetbrains/kotlin/ir/expressions/IrStringConcatenation;", "visitThrow", "Lorg/jetbrains/kotlin/ir/expressions/IrThrow;", "visitTry", "aTry", "Lorg/jetbrains/kotlin/ir/expressions/IrTry;", "visitTypeAlias", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeAlias;", "visitTypeOperator", "Lorg/jetbrains/kotlin/ir/expressions/IrTypeOperatorCall;", "visitTypeParameter", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeParameter;", "visitValueParameter", "Lorg/jetbrains/kotlin/ir/declarations/IrValueParameter;", "visitVararg", "Lorg/jetbrains/kotlin/ir/expressions/IrVararg;", "visitVariable", "Lorg/jetbrains/kotlin/ir/declarations/IrVariable;", "visitWhen", "Lorg/jetbrains/kotlin/ir/expressions/IrWhen;", "visitWhileLoop", "Lorg/jetbrains/kotlin/ir/expressions/IrWhileLoop;", "copyRemappedTypeArgumentsFrom", "", "Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;", "other", "copyTypeParametersFrom", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeParametersContainer;", "getReferencedReturnTarget", "Lorg/jetbrains/kotlin/ir/symbols/IrReturnTargetSymbol;", "returnTarget", "remapType", "Lorg/jetbrains/kotlin/ir/types/IrType;", "transform", "T", "Lorg/jetbrains/kotlin/ir/IrElement;", "(Lorg/jetbrains/kotlin/ir/IrElement;)Lorg/jetbrains/kotlin/ir/IrElement;", "", "transformDeclarationsTo", "", "Lorg/jetbrains/kotlin/ir/declarations/IrDeclaration;", "Lorg/jetbrains/kotlin/ir/declarations/IrDeclarationContainer;", "destination", "(Lorg/jetbrains/kotlin/ir/declarations/IrDeclarationContainer;Lorg/jetbrains/kotlin/ir/declarations/IrDeclarationContainer;)Ljava/util/List;", "transformReceiverArguments", "original", "(Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;)Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;", "transformTo", "transformValueArguments", "(Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;)V", "compiler-plugin"})
public class IrTreeTypeTransformerPreservingMetadata extends org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid {
    private final java.util.HashMap<org.jetbrains.kotlin.ir.expressions.IrLoop, org.jetbrains.kotlin.ir.expressions.IrLoop> transformedLoops = null;
    private final org.jetbrains.kotlin.ir.util.SymbolRemapper symbolRemapper = null;
    private final org.jetbrains.kotlin.ir.util.TypeRemapper typeRemapper = null;
    
    private final org.jetbrains.kotlin.ir.types.IrType remapType(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$remapType) {
        return null;
    }
    
    private final <T extends org.jetbrains.kotlin.ir.declarations.IrDeclarationContainer>java.util.List<org.jetbrains.kotlin.ir.declarations.IrDeclaration> transformDeclarationsTo(@org.jetbrains.annotations.NotNull
    T $this$transformDeclarationsTo, T destination) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.IrStatement visitClass(@org.jetbrains.annotations.NotNull
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
    public org.jetbrains.kotlin.ir.IrStatement visitField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrField declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.IrStatement visitLocalDelegatedProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrLocalDelegatedProperty declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.IrStatement visitVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrVariable declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.IrStatement visitTypeParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeParameter declaration) {
        return null;
    }
    
    private final void copyTypeParametersFrom(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeParametersContainer $this$copyTypeParametersFrom, org.jetbrains.kotlin.ir.declarations.IrTypeParametersContainer other) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.IrStatement visitValueParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrValueParameter declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.IrStatement visitTypeAlias(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeAlias declaration) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitVararg(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrVararg expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitBlock(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlock expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitComposite(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrComposite expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitStringConcatenation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrStringConcatenation expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitGetObjectValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetObjectValue expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitGetEnumValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetEnumValue expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitGetValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetValue expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitSetVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetVariable expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitGetField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetField expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitSetField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetField expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCall expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConstructorCall expression) {
        return null;
    }
    
    private final void copyRemappedTypeArgumentsFrom(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression $this$copyRemappedTypeArgumentsFrom, org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression other) {
    }
    
    private final org.jetbrains.kotlin.ir.expressions.IrCall shallowCopyCall(org.jetbrains.kotlin.ir.expressions.IrCall expression) {
        return null;
    }
    
    private final <T extends org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression>T transformReceiverArguments(@org.jetbrains.annotations.NotNull
    T $this$transformReceiverArguments, T original) {
        return null;
    }
    
    private final <T extends org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression>void transformValueArguments(@org.jetbrains.annotations.NotNull
    T $this$transformValueArguments, T original) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitDelegatingConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitEnumConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrEnumConstructorCall expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitGetClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetClass expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitFunctionReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionReference expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrPropertyReference expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitLocalDelegatedPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLocalDelegatedPropertyReference expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitFunctionExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionExpression expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitClassReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrClassReference expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitInstanceInitializerCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrInstanceInitializerCall expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitTypeOperator(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitWhen(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhen expression) {
        return null;
    }
    
    private final org.jetbrains.kotlin.ir.expressions.IrLoop getTransformedLoop(org.jetbrains.kotlin.ir.expressions.IrLoop irLoop) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    protected org.jetbrains.kotlin.ir.expressions.IrLoop getNonTransformedLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLoop irLoop) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhileLoop loop) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitDoWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop loop) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitBreak(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreak jump) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContinue jump) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitTry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTry aTry) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitReturn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrReturn expression) {
        return null;
    }
    
    private final org.jetbrains.kotlin.ir.symbols.IrReturnTargetSymbol getReferencedReturnTarget(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.SymbolRemapper $this$getReferencedReturnTarget, org.jetbrains.kotlin.ir.symbols.IrReturnTargetSymbol returnTarget) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitThrow(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrThrow expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitDynamicOperatorExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicOperatorExpression expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitDynamicMemberExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicMemberExpression expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitErrorExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrErrorExpression expression) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.expressions.IrExpression visitErrorCallExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrErrorCallExpression expression) {
        return null;
    }
    
    public IrTreeTypeTransformerPreservingMetadata(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.SymbolRemapper symbolRemapper, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.TypeRemapper typeRemapper) {
        super();
    }
}