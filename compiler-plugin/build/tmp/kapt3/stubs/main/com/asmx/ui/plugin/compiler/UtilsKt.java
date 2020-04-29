package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 2, xi = 2, d1 = {"\u0000\u0080\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0004\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0010\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020>H\u0002\u001a\u0010\u0010?\u001a\b\u0012\u0004\u0012\u00020\u001b0@*\u00020A\u001a\u001e\u0010B\u001a\u00020C*\u0002072\b\b\u0002\u0010D\u001a\u00020E2\b\b\u0002\u0010F\u001a\u00020G\u001a\u0092\u0001\u0010H\u001a\u00020I*\u00020 2\u000e\b\u0002\u0010J\u001a\b\u0012\u0004\u0012\u00020K0$2\n\b\u0002\u0010L\u001a\u0004\u0018\u00010M2\n\b\u0002\u0010N\u001a\u0004\u0018\u00010M2\u000e\b\u0002\u0010O\u001a\b\u0012\u0004\u0012\u00020P0$2\b\b\u0002\u0010Q\u001a\u00020R2\b\b\u0002\u0010F\u001a\u00020G2\b\b\u0002\u0010S\u001a\u00020T2\b\b\u0002\u0010U\u001a\u00020V2\b\b\u0002\u0010W\u001a\u00020X2\b\b\u0002\u0010Y\u001a\u00020Z2\b\b\u0002\u0010[\u001a\u00020\\2\b\b\u0002\u0010]\u001a\u00020E\u001a\u001c\u0010^\u001a\u00020I*\u00020 2\u0006\u0010_\u001a\u00020R2\b\b\u0002\u0010U\u001a\u00020V\u001a\n\u0010`\u001a\u00020a*\u000207\u001aJ\u0010b\u001a\u00020a*\u00020&28\u0010c\u001a4\u0012\u0013\u0012\u00110e\u00a2\u0006\f\bf\u0012\b\bg\u0012\u0004\b\b(h\u0012\u0015\u0012\u0013\u0018\u00010%\u00a2\u0006\f\bf\u0012\b\bg\u0012\u0004\b\b(i\u0012\u0004\u0012\u00020a0dH\u0086\b\u00f8\u0001\u0000\u001a\u001a\u0010j\u001a\u00020%*\u00020 2\u0006\u0010k\u001a\u00020%2\u0006\u0010l\u001a\u00020%\u001a \u0010m\u001a\u00020n*\u00020 2\u0006\u0010_\u001a\u0002092\f\u0010o\u001a\b\u0012\u0004\u0012\u00020%0$\u001a4\u0010p\u001a\u00020%*\u00020 2\u0006\u0010g\u001a\u00020+2\u0006\u0010k\u001a\u00020%2\u0006\u0010l\u001a\u00020%2\u0006\u0010q\u001a\u00020R2\b\b\u0002\u0010r\u001a\u00020R\u001a\u0016\u0010s\u001a\u00020t*\u00020\u00112\n\u0010u\u001a\u0006\u0012\u0002\b\u00030v\u001a:\u0010s\u001a\u0002Hw\"\u0004\b\u0000\u0010w*\u00020\u00112\n\u0010u\u001a\u0006\u0012\u0002\b\u00030v2\u0017\u0010c\u001a\u0013\u0012\u0004\u0012\u00020t\u0012\u0004\u0012\u0002Hw0x\u00a2\u0006\u0002\by\u00a2\u0006\u0002\u0010z\u001a&\u0010s\u001a\u00020t*\u00020\u00112\u0006\u0010{\u001a\u00020\u001b2\b\b\u0002\u0010|\u001a\u00020e2\b\b\u0002\u0010}\u001a\u00020e\u001aJ\u0010s\u001a\u0002Hw\"\u0004\b\u0000\u0010w*\u00020\u00112\u0006\u0010{\u001a\u00020\u001b2\b\b\u0002\u0010|\u001a\u00020e2\b\b\u0002\u0010}\u001a\u00020e2\u0017\u0010c\u001a\u0013\u0012\u0004\u0012\u00020t\u0012\u0004\u0012\u0002Hw0x\u00a2\u0006\u0002\by\u00a2\u0006\u0002\u0010~\u001a\u0014\u0010\u007f\u001a\u00020n*\u00020 2\b\u0010\u0080\u0001\u001a\u00030\u0081\u0001\u001a\u0013\u0010\u007f\u001a\u00020n*\u00020 2\u0007\u0010\u0080\u0001\u001a\u00020C\u001a\u0014\u0010\u0082\u0001\u001a\u00020n*\u00020 2\u0007\u0010\u0080\u0001\u001a\u000207\u001a\u0015\u0010\u0082\u0001\u001a\u00020n*\u00020 2\b\u0010\u0080\u0001\u001a\u00030\u0083\u0001\u001a\u001c\u0010\u0084\u0001\u001a\t\u0012\u0004\u0012\u00020E0\u0085\u0001*\u00030\u0086\u00012\u0007\u0010\u0087\u0001\u001a\u00020E\u001a\u001c\u0010\u0084\u0001\u001a\t\u0012\u0004\u0012\u00020e0\u0085\u0001*\u00030\u0086\u00012\u0007\u0010\u0087\u0001\u001a\u00020e\u001a\u001e\u0010\u0084\u0001\u001a\n\u0012\u0005\u0012\u00030\u0088\u00010\u0085\u0001*\u00030\u0086\u00012\b\u0010\u0087\u0001\u001a\u00030\u0088\u0001\u001aP\u0010\u0089\u0001\u001a\u00020%*\u00020 2\b\b\u0002\u0010|\u001a\u00020e2\b\b\u0002\u0010}\u001a\u00020e2\u0006\u0010=\u001a\u00020>2\u0006\u0010_\u001a\u0002092\u001f\u0010\u008a\u0001\u001a\u001a\u0012\u0005\u0012\u00030\u008b\u0001\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020a0d\u00a2\u0006\u0002\by\u001a\u001b\u0010\u008c\u0001\u001a\u00020%*\u00020 2\u0006\u0010k\u001a\u00020%2\u0006\u0010l\u001a\u00020%\u001a\u001c\u0010\u008d\u0001\u001a\u00020%*\u00020 2\u0007\u0010\u008e\u0001\u001a\u00020%2\u0006\u0010_\u001a\u000209\u001aB\u0010\u008f\u0001\u001a\u00020%*\u00020 2\b\b\u0002\u0010|\u001a\u00020e2\b\b\u0002\u0010}\u001a\u00020e2\u0006\u0010_\u001a\u0002092\u0019\u0010\u008a\u0001\u001a\u0014\u0012\u0005\u0012\u00030\u008b\u0001\u0012\u0004\u0012\u00020a0x\u00a2\u0006\u0002\by\u001a\u001d\u0010\u0090\u0001\u001a\u00020%*\u00020 2\u0007\u0010\u0087\u0001\u001a\u00020%2\u0007\u0010\u0091\u0001\u001a\u00020e\u001a\u001b\u0010\u0092\u0001\u001a\u00020%*\u00020 2\u0006\u0010k\u001a\u00020%2\u0006\u0010l\u001a\u00020%\u001a\u001b\u0010\u0093\u0001\u001a\u00020%*\u00020 2\u0006\u0010k\u001a\u00020%2\u0006\u0010l\u001a\u00020%\u001a%\u0010\u0094\u0001\u001a\u00030\u0095\u0001*\u00030\u0086\u00012\u0006\u0010_\u001a\u0002092\u000e\u0010\u0096\u0001\u001a\t\u0012\u0005\u0012\u00030\u0097\u00010$\u001a\u000b\u0010\u0098\u0001\u001a\u00020E*\u000209\u001a\u000b\u0010\u0099\u0001\u001a\u00020E*\u00020>\u001a\u0016\u0010\u009a\u0001\u001a\u00020E*\u00030\u009b\u00012\b\u0010\u009c\u0001\u001a\u00030\u009d\u0001\u001a\u0015\u0010\u009a\u0001\u001a\u00020E*\u0002092\b\u0010\u009e\u0001\u001a\u00030\u009d\u0001\u001a\u000b\u0010\u009f\u0001\u001a\u00020E*\u00020\u0016\u001a\u000b\u0010\u009f\u0001\u001a\u00020E*\u000207\u001a\u000b\u0010\u009f\u0001\u001a\u00020E*\u000209\u001a\u000b\u0010\u00a0\u0001\u001a\u00020E*\u00020\u0016\u001a\u000b\u0010\u00a0\u0001\u001a\u00020E*\u000207\u001a\u000b\u0010\u00a0\u0001\u001a\u00020E*\u000209\u001a\f\u0010\u00a1\u0001\u001a\u00020E*\u00030\u00a2\u0001\u001a\u001e\u0010\u00a3\u0001\u001a\u00020a*\u00020,2\u0007\u0010g\u001a\u00030\u00a4\u00012\b\u0010i\u001a\u0004\u0018\u00010%\u001a\u0012\u0010\u00a5\u0001\u001a\b\u0012\u0004\u0012\u0002090$*\u00030\u009b\u0001\u001a\u000b\u0010\u00a6\u0001\u001a\u000209*\u00020R\u001a\r\u0010\u00a7\u0001\u001a\u00030\u00a8\u0001*\u00030\u00a9\u0001\u001a\"\u0010\u00aa\u0001\u001a\u00030\u00a8\u0001*\u00030\u00a9\u00012\b\u0010\u00ab\u0001\u001a\u00030\u00a4\u00012\t\b\u0002\u0010\u00ac\u0001\u001a\u00020e\"\u0012\u0010\u0000\u001a\u00020\u00018\u00c6\u0002\u00a2\u0006\u0006\u001a\u0004\b\u0002\u0010\u0003\"\u0012\u0010\u0004\u001a\u00020\u00058\u00c6\u0002\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007\"\u0012\u0010\b\u001a\u00020\u00018\u00c6\u0002\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0003\"\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\r\"\u0012\u0010\u0010\u001a\u00020\u00118\u00c6\u0002\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013\"\u0017\u0010\u0014\u001a\u0004\u0018\u00010\u0015*\u00020\u00168F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018\"\u0017\u0010\u0019\u001a\u0004\u0018\u00010\u001a*\u00020\u001b8F\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u001d\"\u0015\u0010\u001e\u001a\u00020\u001f*\u00020 8F\u00a2\u0006\u0006\u001a\u0004\b!\u0010\"\"\u001d\u0010#\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010%0$*\u00020&8F\u00a2\u0006\u0006\u001a\u0004\b\'\u0010(\"#\u0010)\u001a\u0010\u0012\u0004\u0012\u00020+\u0012\u0006\u0012\u0004\u0018\u00010%0**\u00020,8F\u00a2\u0006\u0006\u001a\u0004\b-\u0010.\"\u001b\u0010/\u001a\u00020\u0015*\b\u0012\u0004\u0012\u0002000$8F\u00a2\u0006\u0006\u001a\u0004\b1\u00102\"\u0015\u0010/\u001a\u00020\u0015*\u00020\u00168F\u00a2\u0006\u0006\u001a\u0004\b1\u0010\u0018\"\u0017\u00103\u001a\u0004\u0018\u000104*\u00020\u00168F\u00a2\u0006\u0006\u001a\u0004\b5\u00106\"\u0017\u00103\u001a\u0004\u0018\u000104*\u0002078F\u00a2\u0006\u0006\u001a\u0004\b5\u00108\"\u0017\u00103\u001a\u0004\u0018\u000104*\u0002098F\u00a2\u0006\u0006\u001a\u0004\b5\u0010:\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u00ad\u0001"}, d2 = {"context", "Lorg/jetbrains/kotlin/backend/common/extensions/IrPluginContext;", "getContext", "()Lorg/jetbrains/kotlin/backend/common/extensions/IrPluginContext;", "module", "Lorg/jetbrains/kotlin/descriptors/ModuleDescriptor;", "getModule", "()Lorg/jetbrains/kotlin/descriptors/ModuleDescriptor;", "pluginContext", "getPluginContext", "sRunSymbol", "Lorg/jetbrains/kotlin/ir/symbols/IrSimpleFunctionSymbol;", "getSRunSymbol", "()Lorg/jetbrains/kotlin/ir/symbols/IrSimpleFunctionSymbol;", "sRunSymbol$delegate", "Lkotlin/Lazy;", "scope", "Lcom/asmx/ui/plugin/compiler/IrScope;", "getScope", "()Lcom/asmx/ui/plugin/compiler/IrScope;", "anyWidgetKind", "Lcom/asmx/ui/plugin/compiler/WidgetKind;", "Lorg/jetbrains/kotlin/descriptors/annotations/Annotated;", "getAnyWidgetKind", "(Lorg/jetbrains/kotlin/descriptors/annotations/Annotated;)Lcom/asmx/ui/plugin/compiler/WidgetKind;", "ownerOrNull", "Lorg/jetbrains/kotlin/ir/declarations/IrSymbolOwner;", "Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;", "getOwnerOrNull", "(Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;)Lorg/jetbrains/kotlin/ir/declarations/IrSymbolOwner;", "returnTargetSymbol", "Lorg/jetbrains/kotlin/ir/symbols/IrReturnTargetSymbol;", "Lorg/jetbrains/kotlin/ir/builders/IrBuilderWithScope;", "getReturnTargetSymbol", "(Lorg/jetbrains/kotlin/ir/builders/IrBuilderWithScope;)Lorg/jetbrains/kotlin/ir/symbols/IrReturnTargetSymbol;", "valueArguments", "", "Lorg/jetbrains/kotlin/ir/expressions/IrExpression;", "Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;", "getValueArguments", "(Lorg/jetbrains/kotlin/ir/expressions/IrMemberAccessExpression;)Ljava/util/List;", "valueArgumentsMap", "", "Lorg/jetbrains/kotlin/name/Name;", "Lorg/jetbrains/kotlin/ir/expressions/IrFunctionAccessExpression;", "getValueArgumentsMap", "(Lorg/jetbrains/kotlin/ir/expressions/IrFunctionAccessExpression;)Ljava/util/Map;", "widgetKind", "Lorg/jetbrains/kotlin/descriptors/annotations/AnnotationDescriptor;", "getWidgetKind", "(Ljava/util/List;)Lcom/asmx/ui/plugin/compiler/WidgetKind;", "widgetTransformationKind", "Lcom/asmx/ui/plugin/compiler/WidgetTransformationKind;", "getWidgetTransformationKind", "(Lorg/jetbrains/kotlin/descriptors/annotations/Annotated;)Lcom/asmx/ui/plugin/compiler/WidgetTransformationKind;", "Lorg/jetbrains/kotlin/ir/declarations/IrFunction;", "(Lorg/jetbrains/kotlin/ir/declarations/IrFunction;)Lcom/asmx/ui/plugin/compiler/WidgetTransformationKind;", "Lorg/jetbrains/kotlin/ir/types/IrType;", "(Lorg/jetbrains/kotlin/ir/types/IrType;)Lcom/asmx/ui/plugin/compiler/WidgetTransformationKind;", "wrapDescriptor", "Lorg/jetbrains/kotlin/ir/descriptors/WrappedSimpleFunctionDescriptor;", "descriptor", "Lorg/jetbrains/kotlin/descriptors/FunctionDescriptor;", "allUnbound", "", "Lorg/jetbrains/kotlin/ir/util/SymbolTable;", "copyLight", "Lorg/jetbrains/kotlin/ir/declarations/IrSimpleFunction;", "isInline", "", "modality", "Lorg/jetbrains/kotlin/descriptors/Modality;", "createFunctionDescriptor", "Lorg/jetbrains/kotlin/descriptors/impl/AnonymousFunctionDescriptor;", "valueParameters", "Lorg/jetbrains/kotlin/descriptors/ValueParameterDescriptor;", "extensionReceiverParameter", "Lorg/jetbrains/kotlin/descriptors/ReceiverParameterDescriptor;", "dispatchReceiverParameter", "typeParameters", "Lorg/jetbrains/kotlin/descriptors/TypeParameterDescriptor;", "returnType", "Lorg/jetbrains/kotlin/types/KotlinType;", "visibility", "Lorg/jetbrains/kotlin/descriptors/Visibility;", "owner", "Lorg/jetbrains/kotlin/descriptors/DeclarationDescriptor;", "annotations", "Lorg/jetbrains/kotlin/descriptors/annotations/Annotations;", "kind", "Lorg/jetbrains/kotlin/descriptors/CallableMemberDescriptor$Kind;", "sourceElement", "Lorg/jetbrains/kotlin/descriptors/SourceElement;", "isSuspend", "createFunctionDescriptorFromFunctionType", "type", "createParameterDeclarations", "", "forEachValueArgument", "block", "Lkotlin/Function2;", "", "Lkotlin/ParameterName;", "name", "index", "valueArgument", "irAnd", "left", "right", "irArrayOf", "Lorg/jetbrains/kotlin/ir/expressions/IrCall;", "values", "irBinaryOperator", "typeLeft", "typeRight", "irBuilder", "Lorg/jetbrains/kotlin/backend/common/lower/DeclarationIrBuilder;", "declaration", "Lorg/jetbrains/kotlin/ir/declarations/IrSymbolDeclaration;", "R", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Lcom/asmx/ui/plugin/compiler/IrScope;Lorg/jetbrains/kotlin/ir/declarations/IrSymbolDeclaration;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "symbol", "startOffset", "endOffset", "(Lcom/asmx/ui/plugin/compiler/IrScope;Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;IILkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "irCall", "callee", "Lorg/jetbrains/kotlin/descriptors/SimpleFunctionDescriptor;", "irCallDefault", "Lorg/jetbrains/kotlin/ir/symbols/IrFunctionSymbol;", "irConst", "Lorg/jetbrains/kotlin/ir/expressions/impl/IrConstImpl;", "Lorg/jetbrains/kotlin/ir/builders/IrBuilder;", "value", "", "irLambdaExpression", "body", "Lorg/jetbrains/kotlin/ir/builders/IrBlockBodyBuilder;", "irOr", "irPrimitiveCast", "expression", "irRun", "irShiftBits", "bitsToShiftLeft", "irShl", "irShr", "irVararg", "Lorg/jetbrains/kotlin/ir/expressions/impl/IrVarargImpl;", "elements", "Lorg/jetbrains/kotlin/ir/expressions/IrVarargElement;", "isFunction", "isFunctionInvoke", "isSubtypeOfClass", "Lorg/jetbrains/kotlin/ir/symbols/IrClassifierSymbol;", "superClass", "Lorg/jetbrains/kotlin/ir/symbols/IrClassSymbol;", "parent", "isWidget", "isWidgetUtil", "isZero", "", "putValueArgument", "", "superTypes", "toIrType", "toReceiverParameter", "Lorg/jetbrains/kotlin/ir/declarations/IrValueParameter;", "Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;", "toValueParameter", "parameterName", "parameterIndex", "compiler-plugin"})
public final class UtilsKt {
    private static final kotlin.Lazy sRunSymbol$delegate = null;
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.descriptors.ModuleDescriptor getModule() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final com.asmx.ui.plugin.compiler.IrScope getScope() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.backend.common.extensions.IrPluginContext getContext() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.backend.common.extensions.IrPluginContext getPluginContext() {
        return null;
    }
    
    public static final boolean isWidget(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$isWidget) {
        return false;
    }
    
    public static final boolean isWidgetUtil(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$isWidgetUtil) {
        return false;
    }
    
    public static final boolean isWidget(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.annotations.Annotated $this$isWidget) {
        return false;
    }
    
    public static final boolean isWidgetUtil(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.annotations.Annotated $this$isWidgetUtil) {
        return false;
    }
    
    public static final boolean isWidget(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction $this$isWidget) {
        return false;
    }
    
    public static final boolean isWidgetUtil(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction $this$isWidgetUtil) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    public static final org.jetbrains.kotlin.ir.declarations.IrSymbolOwner getOwnerOrNull(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrSymbol $this$ownerOrNull) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final com.asmx.ui.plugin.compiler.WidgetKind getWidgetKind(@org.jetbrains.annotations.NotNull
    java.util.List<? extends org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor> $this$widgetKind) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public static final com.asmx.ui.plugin.compiler.WidgetTransformationKind getWidgetTransformationKind(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$widgetTransformationKind) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final com.asmx.ui.plugin.compiler.WidgetKind getWidgetKind(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.annotations.Annotated $this$widgetKind) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public static final com.asmx.ui.plugin.compiler.WidgetKind getAnyWidgetKind(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.annotations.Annotated $this$anyWidgetKind) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public static final com.asmx.ui.plugin.compiler.WidgetTransformationKind getWidgetTransformationKind(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.annotations.Annotated $this$widgetTransformationKind) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public static final com.asmx.ui.plugin.compiler.WidgetTransformationKind getWidgetTransformationKind(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction $this$widgetTransformationKind) {
        return null;
    }
    
    public static final boolean isFunctionInvoke(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.FunctionDescriptor $this$isFunctionInvoke) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrCall irCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irCall, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor callee) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrCall irCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irCall, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSimpleFunction callee) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrCall irCallDefault(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irCallDefault, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol callee) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrCall irCallDefault(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irCallDefault, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction callee) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final java.util.Set<org.jetbrains.kotlin.ir.symbols.IrSymbol> allUnbound(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.SymbolTable $this$allUnbound) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder irBuilder(@org.jetbrains.annotations.NotNull
    com.asmx.ui.plugin.compiler.IrScope $this$irBuilder, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSymbolDeclaration<?> declaration) {
        return null;
    }
    
    public static final <R extends java.lang.Object>R irBuilder(@org.jetbrains.annotations.NotNull
    com.asmx.ui.plugin.compiler.IrScope $this$irBuilder, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSymbolDeclaration<?> declaration, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder, ? extends R> block) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder irBuilder(@org.jetbrains.annotations.NotNull
    com.asmx.ui.plugin.compiler.IrScope $this$irBuilder, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrSymbol symbol, int startOffset, int endOffset) {
        return null;
    }
    
    public static final <R extends java.lang.Object>R irBuilder(@org.jetbrains.annotations.NotNull
    com.asmx.ui.plugin.compiler.IrScope $this$irBuilder, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrSymbol symbol, int startOffset, int endOffset, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder, ? extends R> block) {
        return null;
    }
    
    public static final void forEachValueArgument(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression $this$forEachValueArgument, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super org.jetbrains.kotlin.ir.expressions.IrExpression, kotlin.Unit> block) {
    }
    
    @org.jetbrains.annotations.NotNull
    public static final java.util.List<org.jetbrains.kotlin.ir.expressions.IrExpression> getValueArguments(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression $this$valueArguments) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final java.util.Map<org.jetbrains.kotlin.name.Name, org.jetbrains.kotlin.ir.expressions.IrExpression> getValueArgumentsMap(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression $this$valueArgumentsMap) {
        return null;
    }
    
    public static final void putValueArgument(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression $this$putValueArgument, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.Nullable
    org.jetbrains.kotlin.ir.expressions.IrExpression valueArgument) {
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrCall irArrayOf(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irArrayOf, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType type, @org.jetbrains.annotations.NotNull
    java.util.List<? extends org.jetbrains.kotlin.ir.expressions.IrExpression> values) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrExpression irPrimitiveCast(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irPrimitiveCast, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression expression, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType type) {
        return null;
    }
    
    private static final org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol getSRunSymbol() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrExpression irRun(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irRun, int startOffset, int endOffset, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType type, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder, kotlin.Unit> body) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.symbols.IrReturnTargetSymbol getReturnTargetSymbol(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$returnTargetSymbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrExpression irLambdaExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irLambdaExpression, int startOffset, int endOffset, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.FunctionDescriptor descriptor, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType type, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder, ? super org.jetbrains.kotlin.ir.declarations.IrFunction, kotlin.Unit> body) {
        return null;
    }
    
    public static final void createParameterDeclarations(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction $this$createParameterDeclarations) {
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.descriptors.impl.AnonymousFunctionDescriptor createFunctionDescriptor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$createFunctionDescriptor, @org.jetbrains.annotations.NotNull
    java.util.List<? extends org.jetbrains.kotlin.descriptors.ValueParameterDescriptor> valueParameters, @org.jetbrains.annotations.Nullable
    org.jetbrains.kotlin.descriptors.ReceiverParameterDescriptor extensionReceiverParameter, @org.jetbrains.annotations.Nullable
    org.jetbrains.kotlin.descriptors.ReceiverParameterDescriptor dispatchReceiverParameter, @org.jetbrains.annotations.NotNull
    java.util.List<? extends org.jetbrains.kotlin.descriptors.TypeParameterDescriptor> typeParameters, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.KotlinType returnType, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.Modality modality, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.Visibility visibility, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.DeclarationDescriptor owner, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.annotations.Annotations annotations, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.CallableMemberDescriptor.Kind kind, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.SourceElement sourceElement, boolean isSuspend) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.descriptors.impl.AnonymousFunctionDescriptor createFunctionDescriptorFromFunctionType(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$createFunctionDescriptorFromFunctionType, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.KotlinType type, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.DeclarationDescriptor owner) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl irVararg(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilder $this$irVararg, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType type, @org.jetbrains.annotations.NotNull
    java.util.List<? extends org.jetbrains.kotlin.ir.expressions.IrVarargElement> elements) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrExpression irBinaryOperator(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irBinaryOperator, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.name.Name name, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression left, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression right, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.KotlinType typeLeft, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.KotlinType typeRight) {
        return null;
    }
    
    public static final boolean isZero(@org.jetbrains.annotations.NotNull
    java.lang.Number $this$isZero) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    @kotlin.Suppress(names = {"UNCHECKED_CAST"})
    public static final org.jetbrains.kotlin.ir.expressions.IrExpression irOr(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irOr, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression left, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression right) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @kotlin.Suppress(names = {"UNCHECKED_CAST"})
    public static final org.jetbrains.kotlin.ir.expressions.IrExpression irAnd(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irAnd, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression left, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression right) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrExpression irShl(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irShl, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression left, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression right) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrExpression irShr(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irShr, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression left, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression right) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.IrExpression irShiftBits(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilderWithScope $this$irShiftBits, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression value, int bitsToShiftLeft) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl<java.lang.Integer> irConst(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilder $this$irConst, int value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl<java.lang.Long> irConst(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilder $this$irConst, long value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl<java.lang.Boolean> irConst(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.builders.IrBuilder $this$irConst, boolean value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final java.util.List<org.jetbrains.kotlin.ir.types.IrType> superTypes(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol $this$superTypes) {
        return null;
    }
    
    public static final boolean isSubtypeOfClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol $this$isSubtypeOfClass, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrClassSymbol superClass) {
        return false;
    }
    
    public static final boolean isSubtypeOfClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$isSubtypeOfClass, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrClassSymbol parent) {
        return false;
    }
    
    public static final boolean isFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$isFunction) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.types.IrType toIrType(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.KotlinType $this$toIrType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.declarations.IrValueParameter toValueParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ClassDescriptor $this$toValueParameter, @org.jetbrains.annotations.NotNull
    java.lang.String parameterName, int parameterIndex) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.declarations.IrValueParameter toReceiverParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ClassDescriptor $this$toReceiverParameter) {
        return null;
    }
    
    private static final org.jetbrains.kotlin.ir.descriptors.WrappedSimpleFunctionDescriptor wrapDescriptor(org.jetbrains.kotlin.descriptors.FunctionDescriptor descriptor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public static final org.jetbrains.kotlin.ir.declarations.IrSimpleFunction copyLight(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction $this$copyLight, boolean isInline, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.Modality modality) {
        return null;
    }
}