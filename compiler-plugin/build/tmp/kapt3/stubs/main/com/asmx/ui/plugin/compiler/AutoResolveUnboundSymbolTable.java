package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0090\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0003J \u0010\u000b\u001a\u0002H\f\"\b\b\u0000\u0010\f*\u00020\r2\u0006\u0010\u000e\u001a\u0002H\fH\u0086\b\u00a2\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0018\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0012\u001a\u00020\u0019H\u0016J\u0018\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u0012\u001a\u00020\u00192\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0012\u001a\u00020\u001dH\u0016J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0018\u0010 \u001a\u00020\u001f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010!\u001a\u00020\"2\u0006\u0010\u0012\u001a\u00020#H\u0016J\u0018\u0010$\u001a\u00020\"2\u0006\u0010\u0012\u001a\u00020#2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0018\u0010%\u001a\u00020&2\u0006\u0010\u0012\u001a\u00020#2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\'\u001a\u00020\u001c2\u0006\u0010\u0012\u001a\u00020\u001dH\u0016J\u0018\u0010(\u001a\u00020\u001c2\u0006\u0010\u0012\u001a\u00020\u001d2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010)\u001a\u00020*2\u0006\u0010\u0012\u001a\u00020+H\u0016J\u0018\u0010,\u001a\u00020*2\u0006\u0010\u0012\u001a\u00020+2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u000200H\u0016J\u0018\u00101\u001a\u00020.2\u0006\u0010/\u001a\u0002002\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u00102\u001a\u0002032\u0006\u0010\u0012\u001a\u000204H\u0016J\u0010\u00105\u001a\u0002062\u0006\u0010\u0012\u001a\u000207H\u0016R$\u0010\u0004\u001a\u00020\u00058\u0000@\u0000X\u0081\u000e\u00a2\u0006\u0014\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u001a\u0004\b\u0004\u0010\b\"\u0004\b\t\u0010\n\u00a8\u00068"}, d2 = {"Lcom/asmx/ui/plugin/compiler/AutoResolveUnboundSymbolTable;", "Lorg/jetbrains/kotlin/ir/util/SymbolTable;", "symbolTable", "(Lorg/jetbrains/kotlin/ir/util/SymbolTable;)V", "isNested", "", "isNested$annotations", "()V", "()Z", "setNested", "(Z)V", "bind", "S", "Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;", "referencedValue", "(Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;)Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;", "referenceClass", "Lorg/jetbrains/kotlin/ir/symbols/IrClassSymbol;", "descriptor", "Lorg/jetbrains/kotlin/descriptors/ClassDescriptor;", "referenceClassFromLinker", "sig", "Lorg/jetbrains/kotlin/ir/util/IdSignature;", "referenceConstructor", "Lorg/jetbrains/kotlin/ir/symbols/IrConstructorSymbol;", "Lorg/jetbrains/kotlin/descriptors/ClassConstructorDescriptor;", "referenceConstructorFromLinker", "referenceDeclaredFunction", "Lorg/jetbrains/kotlin/ir/symbols/IrSimpleFunctionSymbol;", "Lorg/jetbrains/kotlin/descriptors/FunctionDescriptor;", "referenceEnumEntry", "Lorg/jetbrains/kotlin/ir/symbols/IrEnumEntrySymbol;", "referenceEnumEntryFromLinker", "referenceField", "Lorg/jetbrains/kotlin/ir/symbols/IrFieldSymbol;", "Lorg/jetbrains/kotlin/descriptors/PropertyDescriptor;", "referenceFieldFromLinker", "referencePropertyFromLinker", "Lorg/jetbrains/kotlin/ir/symbols/IrPropertySymbol;", "referenceSimpleFunction", "referenceSimpleFunctionFromLinker", "referenceTypeAlias", "Lorg/jetbrains/kotlin/ir/symbols/IrTypeAliasSymbol;", "Lorg/jetbrains/kotlin/descriptors/TypeAliasDescriptor;", "referenceTypeAliasFromLinker", "referenceTypeParameter", "Lorg/jetbrains/kotlin/ir/symbols/IrTypeParameterSymbol;", "classifier", "Lorg/jetbrains/kotlin/descriptors/TypeParameterDescriptor;", "referenceTypeParameterFromLinker", "referenceValueParameter", "Lorg/jetbrains/kotlin/ir/symbols/IrValueParameterSymbol;", "Lorg/jetbrains/kotlin/descriptors/ParameterDescriptor;", "referenceVariable", "Lorg/jetbrains/kotlin/ir/symbols/IrVariableSymbol;", "Lorg/jetbrains/kotlin/descriptors/VariableDescriptor;", "compiler-plugin"})
public final class AutoResolveUnboundSymbolTable extends org.jetbrains.kotlin.ir.util.SymbolTable {
    private boolean isNested = false;
    
    @kotlin.PublishedApi
    @java.lang.Deprecated
    public static void isNested$annotations() {
    }
    
    public final boolean isNested() {
        return false;
    }
    
    public final void setNested(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    @kotlin.Suppress(names = {"NOTHING_TO_INLINE"})
    public final <S extends org.jetbrains.kotlin.ir.symbols.IrSymbol>S bind(@org.jetbrains.annotations.NotNull
    S referencedValue) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrClassSymbol referenceClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ClassDescriptor descriptor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrClassSymbol referenceClassFromLinker(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ClassDescriptor descriptor, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.IdSignature sig) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol referenceConstructor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor descriptor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol referenceConstructorFromLinker(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor descriptor, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.IdSignature sig) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol referenceDeclaredFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.FunctionDescriptor descriptor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol referenceEnumEntry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ClassDescriptor descriptor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol referenceEnumEntryFromLinker(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ClassDescriptor descriptor, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.IdSignature sig) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrFieldSymbol referenceField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.PropertyDescriptor descriptor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrFieldSymbol referenceFieldFromLinker(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.PropertyDescriptor descriptor, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.IdSignature sig) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrPropertySymbol referencePropertyFromLinker(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.PropertyDescriptor descriptor, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.IdSignature sig) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol referenceSimpleFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.FunctionDescriptor descriptor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol referenceSimpleFunctionFromLinker(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.FunctionDescriptor descriptor, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.IdSignature sig) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol referenceTypeAlias(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.TypeAliasDescriptor descriptor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol referenceTypeAliasFromLinker(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.TypeAliasDescriptor descriptor, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.IdSignature sig) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol referenceTypeParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.TypeParameterDescriptor classifier) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol referenceTypeParameterFromLinker(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.TypeParameterDescriptor classifier, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.IdSignature sig) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol referenceValueParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ParameterDescriptor descriptor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrVariableSymbol referenceVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.VariableDescriptor descriptor) {
        return null;
    }
    
    public AutoResolveUnboundSymbolTable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.util.SymbolTable symbolTable) {
        super(null);
    }
}