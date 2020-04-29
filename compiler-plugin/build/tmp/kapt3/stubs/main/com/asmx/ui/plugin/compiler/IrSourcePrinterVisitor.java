package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u00da\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004\u00a2\u0006\u0002\u0010\u0005J\u0017\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u0012H\u0082\bJ&\u0010\u0013\u001a\u00020\u000b2\u001b\u0010\u0014\u001a\u0017\u0012\b\u0012\u00060\u0016j\u0002`\u0017\u0012\u0004\u0012\u00020\u00100\u0015\u00a2\u0006\u0002\b\u0018H\u0082\bJ\u0016\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0017\u0010\u001d\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u0012H\u0082\bJ\u0010\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u001f\u001a\u00020\u001cH\u0002J\u0012\u0010 \u001a\u00020\u000e2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0002J\b\u0010#\u001a\u00020\u000eH\u0002J\u0012\u0010#\u001a\u00020\u000e2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0002J\u0010\u0010$\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020&H\u0002J\u0016\u0010\'\u001a\u00020\u000b2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020&0\u001aH\u0002J\u0010\u0010)\u001a\u00020\u00102\u0006\u0010*\u001a\u00020+H\u0016J\u0010\u0010,\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020-H\u0016J\u0010\u0010.\u001a\u00020\u00102\u0006\u0010/\u001a\u000200H\u0016J\u0010\u00101\u001a\u00020\u00102\u0006\u00102\u001a\u000203H\u0016J\u0010\u00104\u001a\u00020\u00102\u0006\u00102\u001a\u000205H\u0016J\u0010\u00106\u001a\u00020\u00102\u0006\u0010*\u001a\u000207H\u0016J\u0010\u00108\u001a\u00020\u00102\u0006\u00109\u001a\u00020:H\u0016J\u0010\u0010;\u001a\u00020\u00102\u0006\u0010<\u001a\u00020=H\u0016J\u0010\u0010>\u001a\u00020\u00102\u0006\u0010*\u001a\u00020?H\u0016J\u001c\u0010@\u001a\u00020\u0010\"\u0004\b\u0000\u0010A2\f\u0010*\u001a\b\u0012\u0004\u0012\u0002HA0BH\u0016J\u0010\u0010C\u001a\u00020\u00102\u0006\u0010<\u001a\u00020DH\u0016J\u0010\u0010E\u001a\u00020\u00102\u0006\u0010*\u001a\u00020&H\u0016J\u0010\u0010F\u001a\u00020\u00102\u0006\u0010*\u001a\u00020GH\u0016J\u0010\u0010H\u001a\u00020\u00102\u0006\u00102\u001a\u00020IH\u0016J\u0010\u0010J\u001a\u00020\u00102\u0006\u0010*\u001a\u00020KH\u0016J\u0010\u0010L\u001a\u00020\u00102\u0006\u0010M\u001a\u00020NH\u0016J\u0010\u0010O\u001a\u00020\u00102\u0006\u0010P\u001a\u00020QH\u0016J\u0010\u0010R\u001a\u00020\u00102\u0006\u0010/\u001a\u00020SH\u0016J\u0010\u0010T\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020UH\u0016J\u0010\u0010V\u001a\u00020\u00102\u0006\u0010<\u001a\u00020WH\u0016J\u0010\u0010X\u001a\u00020\u00102\u0006\u0010*\u001a\u00020YH\u0016J\u0010\u0010Z\u001a\u00020\u00102\u0006\u0010<\u001a\u00020\nH\u0016J\u0010\u0010[\u001a\u00020\u00102\u0006\u0010*\u001a\u00020\\H\u0016J\u0010\u0010]\u001a\u00020\u00102\u0006\u0010*\u001a\u00020^H\u0016J\u0010\u0010_\u001a\u00020\u00102\u0006\u0010*\u001a\u00020`H\u0016J\u0010\u0010a\u001a\u00020\u00102\u0006\u0010*\u001a\u00020bH\u0016J\u0010\u0010c\u001a\u00020\u00102\u0006\u0010*\u001a\u00020dH\u0016J\u0010\u0010e\u001a\u00020\u00102\u0006\u0010<\u001a\u00020fH\u0016J\u0010\u0010g\u001a\u00020\u00102\u0006\u0010*\u001a\u00020hH\u0016J\u0010\u0010i\u001a\u00020\u00102\u0006\u0010M\u001a\u00020jH\u0016J\u0010\u0010k\u001a\u00020\u00102\u0006\u0010<\u001a\u00020lH\u0016J\u0010\u0010m\u001a\u00020\u00102\u0006\u0010<\u001a\u00020nH\u0016J\u0010\u0010o\u001a\u00020\u00102\u0006\u0010*\u001a\u00020pH\u0016J\u0010\u0010q\u001a\u00020\u00102\u0006\u0010*\u001a\u00020rH\u0016J\u0010\u0010s\u001a\u00020\u00102\u0006\u0010*\u001a\u00020tH\u0016J\u0010\u0010u\u001a\u00020\u00102\u0006\u0010<\u001a\u00020vH\u0016J\u0010\u0010w\u001a\u00020\u00102\u0006\u0010x\u001a\u00020yH\u0016J\u0010\u0010z\u001a\u00020\u00102\u0006\u0010*\u001a\u00020{H\u0016J\u0010\u0010|\u001a\u00020\u00102\u0006\u0010*\u001a\u00020}H\u0016J\u0011\u0010~\u001a\u00020\u00102\u0007\u0010\u007f\u001a\u00030\u0080\u0001H\u0016J\u0012\u0010\u0081\u0001\u001a\u00020\u00102\u0007\u0010<\u001a\u00030\u0082\u0001H\u0016J\u0012\u0010\u0083\u0001\u001a\u00020\u00102\u0007\u0010*\u001a\u00030\u0084\u0001H\u0016J\u0012\u0010\u0085\u0001\u001a\u00020\u00102\u0007\u0010<\u001a\u00030\u0086\u0001H\u0016J\u0012\u0010\u0087\u0001\u001a\u00020\u00102\u0007\u0010<\u001a\u00030\u0088\u0001H\u0016J\u0012\u0010\u0089\u0001\u001a\u00020\u00102\u0007\u0010*\u001a\u00030\u008a\u0001H\u0016J\u0012\u0010\u008b\u0001\u001a\u00020\u00102\u0007\u0010<\u001a\u00030\u008c\u0001H\u0016J\u0012\u0010\u008d\u0001\u001a\u00020\u00102\u0007\u0010*\u001a\u00030\u008e\u0001H\u0016J\u0012\u0010\u008f\u0001\u001a\u00020\u00102\u0007\u0010*\u001a\u00030\u0090\u0001H\u0016J\u0012\u0010\u0091\u0001\u001a\u00020\u00102\u0007\u0010M\u001a\u00030\u0092\u0001H\u0016J\"\u0010\u0093\u0001\u001a\u0002HA\"\u0004\b\u0000\u0010A2\r\u0010\u0094\u0001\u001a\b\u0012\u0004\u0012\u0002HA0\u0012\u00a2\u0006\u0003\u0010\u0095\u0001J\u0019\u0010\u0096\u0001\u001a\u0004\u0018\u00010n*\u00020=2\b\u0010\u0097\u0001\u001a\u00030\u0088\u0001H\u0002J\u0014\u0010\u0098\u0001\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001a*\u00030\u0099\u0001H\u0002J\f\u0010 \u001a\u00020\u0010*\u00020QH\u0002J\u0019\u0010\u009a\u0001\u001a\u00020\u0010*\u00030\u009b\u00012\t\b\u0002\u0010\u009c\u0001\u001a\u00020\u0007H\u0002J\u000e\u0010\u009d\u0001\u001a\u00020\u0010*\u00030\u009e\u0001H\u0002J\u000b\u0010\u009f\u0001\u001a\u00020\u0010*\u00020\nJ\u000b\u0010\u00a0\u0001\u001a\u00020\u0010*\u00020=J\u000b\u0010\u00a1\u0001\u001a\u00020\u0010*\u00020\nJ\u001e\u0010\u00a2\u0001\u001a\u00020\u0010*\b\u0012\u0004\u0012\u00020Q0\u001a2\t\b\u0002\u0010\u00a3\u0001\u001a\u00020\u000bH\u0002J\u0018\u0010$\u001a\u00020\u0010*\u00060\u0016j\u0002`\u00172\u0006\u0010%\u001a\u00020&H\u0002J\u001c\u0010\u00a4\u0001\u001a\u00020\u0010*\u00060\u0016j\u0002`\u00172\t\u0010\u00a5\u0001\u001a\u0004\u0018\u00010QH\u0002J\u001b\u0010\u00a6\u0001\u001a\u00020\u0010*\u00030\u00a7\u00012\u000b\u0010\u00a8\u0001\u001a\u00060\u0016j\u0002`\u0017H\u0002J\u001b\u0010\u00a9\u0001\u001a\u00020\u0010*\u00030\u00a7\u00012\u000b\u0010\u00a8\u0001\u001a\u00060\u0016j\u0002`\u0017H\u0002J\u000e\u0010\u00aa\u0001\u001a\u00020\u000b*\u00030\u00ab\u0001H\u0002J\u000e\u0010\u00ac\u0001\u001a\u00020\u000b*\u00030\u00ad\u0001H\u0002J\u000e\u0010\u00ae\u0001\u001a\u00020\u000b*\u00030\u00af\u0001H\u0002J\u000e\u0010\u00b0\u0001\u001a\u00020\u000b*\u00030\u00b1\u0001H\u0002J\u000e\u0010\u00b2\u0001\u001a\u00020\u000b*\u00030\u00ab\u0001H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u00b3\u0001"}, d2 = {"Lcom/asmx/ui/plugin/compiler/IrSourcePrinterVisitor;", "Lorg/jetbrains/kotlin/backend/common/IrElementVisitorVoidWithContext;", "out", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "(Ljava/lang/Appendable;)V", "isInNotCall", "", "labels", "Ljava/util/IdentityHashMap;", "Lorg/jetbrains/kotlin/ir/declarations/IrFunction;", "", "printIntsAsBinary", "printer", "Lorg/jetbrains/kotlin/utils/Printer;", "bracedBlock", "", "body", "Lkotlin/Function0;", "buildTrimEnd", "fn", "Lkotlin/Function1;", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "Lkotlin/ExtensionFunctionType;", "getPlaceholderParameterNames", "", "expectedCount", "", "indented", "intAsBinaryString", "value", "print", "obj", "", "println", "renderAsAnnotation", "irAnnotation", "Lorg/jetbrains/kotlin/ir/expressions/IrConstructorCall;", "renderTypeAnnotations", "annotations", "visitBlock", "expression", "Lorg/jetbrains/kotlin/ir/expressions/IrBlock;", "visitBlockBody", "Lorg/jetbrains/kotlin/ir/expressions/IrBlockBody;", "visitBranch", "branch", "Lorg/jetbrains/kotlin/ir/expressions/IrBranch;", "visitBreak", "jump", "Lorg/jetbrains/kotlin/ir/expressions/IrBreak;", "visitBreakContinue", "Lorg/jetbrains/kotlin/ir/expressions/IrBreakContinue;", "visitCall", "Lorg/jetbrains/kotlin/ir/expressions/IrCall;", "visitCatch", "aCatch", "Lorg/jetbrains/kotlin/ir/expressions/IrCatch;", "visitClassNew", "declaration", "Lorg/jetbrains/kotlin/ir/declarations/IrClass;", "visitComposite", "Lorg/jetbrains/kotlin/ir/expressions/IrComposite;", "visitConst", "T", "Lorg/jetbrains/kotlin/ir/expressions/IrConst;", "visitConstructor", "Lorg/jetbrains/kotlin/ir/declarations/IrConstructor;", "visitConstructorCall", "visitContainerExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrContainerExpression;", "visitContinue", "Lorg/jetbrains/kotlin/ir/expressions/IrContinue;", "visitDelegatingConstructorCall", "Lorg/jetbrains/kotlin/ir/expressions/IrDelegatingConstructorCall;", "visitDoWhileLoop", "loop", "Lorg/jetbrains/kotlin/ir/expressions/IrDoWhileLoop;", "visitElement", "element", "Lorg/jetbrains/kotlin/ir/IrElement;", "visitElseBranch", "Lorg/jetbrains/kotlin/ir/expressions/IrElseBranch;", "visitExpressionBody", "Lorg/jetbrains/kotlin/ir/expressions/IrExpressionBody;", "visitFileNew", "Lorg/jetbrains/kotlin/ir/declarations/IrFile;", "visitFunctionExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrFunctionExpression;", "visitFunctionNew", "visitFunctionReference", "Lorg/jetbrains/kotlin/ir/expressions/IrFunctionReference;", "visitGetEnumValue", "Lorg/jetbrains/kotlin/ir/expressions/IrGetEnumValue;", "visitGetObjectValue", "Lorg/jetbrains/kotlin/ir/expressions/IrGetObjectValue;", "visitGetValue", "Lorg/jetbrains/kotlin/ir/expressions/IrGetValue;", "visitInstanceInitializerCall", "Lorg/jetbrains/kotlin/ir/expressions/IrInstanceInitializerCall;", "visitLocalDelegatedProperty", "Lorg/jetbrains/kotlin/ir/declarations/IrLocalDelegatedProperty;", "visitLocalDelegatedPropertyReference", "Lorg/jetbrains/kotlin/ir/expressions/IrLocalDelegatedPropertyReference;", "visitLoop", "Lorg/jetbrains/kotlin/ir/expressions/IrLoop;", "visitModuleFragment", "Lorg/jetbrains/kotlin/ir/declarations/IrModuleFragment;", "visitPropertyNew", "Lorg/jetbrains/kotlin/ir/declarations/IrProperty;", "visitPropertyReference", "Lorg/jetbrains/kotlin/ir/expressions/IrPropertyReference;", "visitReturn", "Lorg/jetbrains/kotlin/ir/expressions/IrReturn;", "visitSetVariable", "Lorg/jetbrains/kotlin/ir/expressions/IrSetVariable;", "visitSimpleFunction", "Lorg/jetbrains/kotlin/ir/declarations/IrSimpleFunction;", "visitSpreadElement", "spread", "Lorg/jetbrains/kotlin/ir/expressions/IrSpreadElement;", "visitStringConcatenation", "Lorg/jetbrains/kotlin/ir/expressions/IrStringConcatenation;", "visitThrow", "Lorg/jetbrains/kotlin/ir/expressions/IrThrow;", "visitTry", "aTry", "Lorg/jetbrains/kotlin/ir/expressions/IrTry;", "visitTypeAlias", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeAlias;", "visitTypeOperator", "Lorg/jetbrains/kotlin/ir/expressions/IrTypeOperatorCall;", "visitTypeParameter", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeParameter;", "visitValueParameterNew", "Lorg/jetbrains/kotlin/ir/declarations/IrValueParameter;", "visitVararg", "Lorg/jetbrains/kotlin/ir/expressions/IrVararg;", "visitVariable", "Lorg/jetbrains/kotlin/ir/declarations/IrVariable;", "visitVariableAccess", "Lorg/jetbrains/kotlin/ir/expressions/IrValueAccessExpression;", "visitWhen", "Lorg/jetbrains/kotlin/ir/expressions/IrWhen;", "visitWhileLoop", "Lorg/jetbrains/kotlin/ir/expressions/IrWhileLoop;", "withIntsAsBinaryLiterals", "block", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "correspondingProperty", "param", "getValueParameterNamesForDebug", "Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;", "printAnnotations", "Lorg/jetbrains/kotlin/ir/declarations/IrAnnotationContainer;", "onePerLine", "printArgumentList", "Lorg/jetbrains/kotlin/ir/expressions/IrFunctionAccessExpression;", "printAsLambda", "printAsObject", "printBody", "printJoin", "separator", "renderAsAnnotationArgument", "irElement", "renderDeclarationFqn", "Lorg/jetbrains/kotlin/ir/declarations/IrDeclaration;", "sb", "renderDeclarationParentFqn", "renderSrc", "Lorg/jetbrains/kotlin/ir/types/IrType;", "renderTypeAbbreviation", "Lorg/jetbrains/kotlin/ir/types/IrTypeAbbreviation;", "renderTypeAliasFqn", "Lorg/jetbrains/kotlin/ir/symbols/IrTypeAliasSymbol;", "renderTypeArgument", "Lorg/jetbrains/kotlin/ir/types/IrTypeArgument;", "renderTypeInner", "compiler-plugin"})
final class IrSourcePrinterVisitor extends org.jetbrains.kotlin.backend.common.IrElementVisitorVoidWithContext {
    private final java.util.IdentityHashMap<org.jetbrains.kotlin.ir.declarations.IrFunction, java.lang.String> labels = null;
    private final org.jetbrains.kotlin.utils.Printer printer = null;
    private boolean isInNotCall = false;
    private boolean printIntsAsBinary = false;
    
    private final void print(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement $this$print) {
    }
    
    private final org.jetbrains.kotlin.utils.Printer print(java.lang.Object obj) {
        return null;
    }
    
    private final org.jetbrains.kotlin.utils.Printer println(java.lang.Object obj) {
        return null;
    }
    
    private final org.jetbrains.kotlin.utils.Printer println() {
        return null;
    }
    
    private final void indented(kotlin.jvm.functions.Function0<kotlin.Unit> body) {
    }
    
    private final void bracedBlock(kotlin.jvm.functions.Function0<kotlin.Unit> body) {
    }
    
    private final void printJoin(@org.jetbrains.annotations.NotNull
    java.util.List<? extends org.jetbrains.kotlin.ir.IrElement> $this$printJoin, java.lang.String separator) {
    }
    
    @java.lang.Override
    public void visitModuleFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrModuleFragment declaration) {
    }
    
    @java.lang.Override
    public void visitFileNew(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFile declaration) {
    }
    
    @java.lang.Override
    public void visitValueParameterNew(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrValueParameter declaration) {
    }
    
    @java.lang.Override
    public void visitSimpleFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSimpleFunction declaration) {
    }
    
    public final void printBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction $this$printBody) {
    }
    
    @java.lang.Override
    public void visitConstructor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrConstructor declaration) {
    }
    
    @java.lang.Override
    public void visitCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCall expression) {
    }
    
    private final void printAnnotations(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrAnnotationContainer $this$printAnnotations, boolean onePerLine) {
    }
    
    private final void printArgumentList(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression $this$printArgumentList) {
    }
    
    @java.lang.Override
    public void visitFunctionExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionExpression expression) {
    }
    
    public final void printAsLambda(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction $this$printAsLambda) {
    }
    
    @java.lang.Override
    public void visitTypeOperator(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall expression) {
    }
    
    @java.lang.Override
    public void visitComposite(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrComposite expression) {
    }
    
    @java.lang.Override
    public void visitDoWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop loop) {
    }
    
    @java.lang.Override
    public void visitConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConstructorCall expression) {
    }
    
    @java.lang.Override
    public void visitStringConcatenation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrStringConcatenation expression) {
    }
    
    @java.lang.Override
    public void visitVararg(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrVararg expression) {
    }
    
    @java.lang.Override
    public void visitWhen(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhen expression) {
    }
    
    @java.lang.Override
    public void visitWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhileLoop loop) {
    }
    
    @java.lang.Override
    public void visitReturn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrReturn expression) {
    }
    
    @java.lang.Override
    public void visitBlock(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlock expression) {
    }
    
    @java.lang.Override
    public void visitVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrVariable declaration) {
    }
    
    @java.lang.Override
    public void visitGetObjectValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetObjectValue expression) {
    }
    
    @java.lang.Override
    public void visitGetValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetValue expression) {
    }
    
    @java.lang.Override
    public void visitGetEnumValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetEnumValue expression) {
    }
    
    @java.lang.Override
    public void visitSetVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetVariable expression) {
    }
    
    @java.lang.Override
    public void visitExpressionBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpressionBody body) {
    }
    
    @java.lang.Override
    public void visitPropertyNew(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrProperty declaration) {
    }
    
    public final <T extends java.lang.Object>T withIntsAsBinaryLiterals(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<? extends T> block) {
        return null;
    }
    
    private final java.lang.String intAsBinaryString(int value) {
        return null;
    }
    
    @java.lang.Override
    public <T extends java.lang.Object>void visitConst(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConst<T> expression) {
    }
    
    @java.lang.Override
    public void visitBlockBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlockBody body) {
    }
    
    private final org.jetbrains.kotlin.ir.declarations.IrProperty correspondingProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrClass $this$correspondingProperty, org.jetbrains.kotlin.ir.declarations.IrValueParameter param) {
        return null;
    }
    
    @java.lang.Override
    public void visitClassNew(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrClass declaration) {
    }
    
    public final void printAsObject(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrClass $this$printAsObject) {
    }
    
    @java.lang.Override
    public void visitBreak(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreak jump) {
    }
    
    @java.lang.Override
    public void visitContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContinue jump) {
    }
    
    @java.lang.Override
    public void visitTypeParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeParameter declaration) {
    }
    
    @java.lang.Override
    public void visitThrow(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrThrow expression) {
    }
    
    @java.lang.Override
    public void visitBranch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBranch branch) {
    }
    
    @java.lang.Override
    public void visitBreakContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreakContinue jump) {
    }
    
    @java.lang.Override
    public void visitCatch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCatch aCatch) {
    }
    
    @java.lang.Override
    public void visitContainerExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContainerExpression expression) {
    }
    
    @java.lang.Override
    public void visitDelegatingConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall expression) {
    }
    
    @java.lang.Override
    public void visitElseBranch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrElseBranch branch) {
    }
    
    @java.lang.Override
    public void visitFunctionNew(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction declaration) {
    }
    
    @java.lang.Override
    public void visitFunctionReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionReference expression) {
    }
    
    @java.lang.Override
    public void visitInstanceInitializerCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrInstanceInitializerCall expression) {
    }
    
    @java.lang.Override
    public void visitLocalDelegatedProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrLocalDelegatedProperty declaration) {
    }
    
    @java.lang.Override
    public void visitLocalDelegatedPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLocalDelegatedPropertyReference expression) {
    }
    
    @java.lang.Override
    public void visitLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLoop loop) {
    }
    
    @java.lang.Override
    public void visitPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrPropertyReference expression) {
    }
    
    @java.lang.Override
    public void visitSpreadElement(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSpreadElement spread) {
    }
    
    @java.lang.Override
    public void visitVariableAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrValueAccessExpression expression) {
    }
    
    @java.lang.Override
    public void visitTry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTry aTry) {
    }
    
    @java.lang.Override
    public void visitTypeAlias(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeAlias declaration) {
    }
    
    private final java.lang.String renderSrc(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$renderSrc) {
        return null;
    }
    
    private final java.lang.String renderTypeInner(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$renderTypeInner) {
        return null;
    }
    
    private final java.lang.String buildTrimEnd(kotlin.jvm.functions.Function1<? super java.lang.StringBuilder, kotlin.Unit> fn) {
        return null;
    }
    
    private final java.lang.String renderTypeAbbreviation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrTypeAbbreviation $this$renderTypeAbbreviation) {
        return null;
    }
    
    private final java.lang.String renderTypeArgument(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrTypeArgument $this$renderTypeArgument) {
        return null;
    }
    
    private final java.lang.String renderTypeAnnotations(java.util.List<? extends org.jetbrains.kotlin.ir.expressions.IrConstructorCall> annotations) {
        return null;
    }
    
    private final java.lang.String renderAsAnnotation(org.jetbrains.kotlin.ir.expressions.IrConstructorCall irAnnotation) {
        return null;
    }
    
    private final void renderAsAnnotation(@org.jetbrains.annotations.NotNull
    java.lang.StringBuilder $this$renderAsAnnotation, org.jetbrains.kotlin.ir.expressions.IrConstructorCall irAnnotation) {
    }
    
    private final java.lang.String renderTypeAliasFqn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol $this$renderTypeAliasFqn) {
        return null;
    }
    
    private final void renderDeclarationFqn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration $this$renderDeclarationFqn, java.lang.StringBuilder sb) {
    }
    
    private final void renderDeclarationParentFqn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration $this$renderDeclarationParentFqn, java.lang.StringBuilder sb) {
    }
    
    private final java.util.List<java.lang.String> getValueParameterNamesForDebug(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression $this$getValueParameterNamesForDebug) {
        return null;
    }
    
    private final java.util.List<java.lang.String> getPlaceholderParameterNames(int expectedCount) {
        return null;
    }
    
    private final void renderAsAnnotationArgument(@org.jetbrains.annotations.NotNull
    java.lang.StringBuilder $this$renderAsAnnotationArgument, org.jetbrains.kotlin.ir.IrElement irElement) {
    }
    
    @java.lang.Override
    public void visitElement(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement element) {
    }
    
    public IrSourcePrinterVisitor(@org.jetbrains.annotations.NotNull
    java.lang.Appendable out) {
        super();
    }
}