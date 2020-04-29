package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0096\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010%\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010 \u001a\u00020!J\u0010\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\u0005H\u0016J\u0010\u0010$\u001a\u00020\u00072\u0006\u0010#\u001a\u00020\u0007H\u0016J\u0010\u0010%\u001a\u00020\t2\u0006\u0010#\u001a\u00020\tH\u0016J\u0010\u0010&\u001a\u00020\u000b2\u0006\u0010#\u001a\u00020\u000bH\u0016J\u0010\u0010\'\u001a\u00020\r2\u0006\u0010#\u001a\u00020\rH\u0016J\u0010\u0010(\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u000fH\u0016J\u0010\u0010)\u001a\u00020\u00112\u0006\u0010#\u001a\u00020\u0011H\u0016J\u0010\u0010*\u001a\u00020\u00132\u0006\u0010#\u001a\u00020\u0013H\u0016J\u0010\u0010+\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\u0015H\u0016J\u0010\u0010,\u001a\u00020\u00192\u0006\u0010#\u001a\u00020\u0019H\u0016J\u0010\u0010-\u001a\u00020\u001b2\u0006\u0010#\u001a\u00020\u001bH\u0016J\u0010\u0010.\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020\u001dH\u0016J\u0010\u0010/\u001a\u00020\u001f2\u0006\u0010#\u001a\u00020\u001fH\u0016J\u0010\u00100\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\u0005H\u0016J\u0014\u00101\u001a\u0004\u0018\u00010\u00052\b\u0010#\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u00102\u001a\u0002032\u0006\u0010#\u001a\u000203H\u0016J\u0010\u00104\u001a\u00020\u00072\u0006\u0010#\u001a\u00020\u0007H\u0016J\u0010\u00105\u001a\u00020\t2\u0006\u0010#\u001a\u00020\tH\u0016J\u0010\u00106\u001a\u00020\r2\u0006\u0010#\u001a\u00020\rH\u0016J\u0010\u00107\u001a\u0002082\u0006\u0010#\u001a\u000208H\u0016J\u0010\u00109\u001a\u00020\u00132\u0006\u0010#\u001a\u00020\u0013H\u0016J\u0010\u0010:\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\u0015H\u0016J\u0010\u0010;\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u0017H\u0016J\u0010\u0010<\u001a\u00020\u00112\u0006\u0010#\u001a\u00020\u0011H\u0016J\u0010\u0010=\u001a\u00020\u00192\u0006\u0010#\u001a\u00020\u0019H\u0016J\u0010\u0010>\u001a\u00020?2\u0006\u0010#\u001a\u00020?H\u0016J\u0010\u0010@\u001a\u00020\u001f2\u0006\u0010#\u001a\u00020\u001fH\u0016J>\u0010A\u001a\u0002HB\"\b\b\u0000\u0010B*\u00020C*\u000e\u0012\u0004\u0012\u0002HB\u0012\u0004\u0012\u0002HB0D2\u0006\u0010#\u001a\u0002HB2\f\u0010E\u001a\b\u0012\u0004\u0012\u0002HB0FH\u0082\b\u00a2\u0006\u0002\u0010GJ/\u0010H\u001a\u0002HB\"\b\b\u0000\u0010B*\u00020C*\u000e\u0012\u0004\u0012\u0002HB\u0012\u0004\u0012\u0002HB0D2\u0006\u0010#\u001a\u0002HBH\u0002\u00a2\u0006\u0002\u0010IR\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\t0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\r0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00130\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00150\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00170\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00190\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u00020\u001b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001d0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u001f0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006J"}, d2 = {"Lcom/asmx/ui/plugin/compiler/LazySymbolDeepCopyRemapper;", "Lorg/jetbrains/kotlin/ir/util/SymbolRemapper;", "()V", "classes", "Ljava/util/HashMap;", "Lorg/jetbrains/kotlin/ir/symbols/IrClassSymbol;", "constructors", "Lorg/jetbrains/kotlin/ir/symbols/IrConstructorSymbol;", "enumEntries", "Lorg/jetbrains/kotlin/ir/symbols/IrEnumEntrySymbol;", "externalPackageFragments", "Lorg/jetbrains/kotlin/ir/symbols/IrExternalPackageFragmentSymbol;", "fields", "Lorg/jetbrains/kotlin/ir/symbols/IrFieldSymbol;", "files", "Lorg/jetbrains/kotlin/ir/symbols/IrFileSymbol;", "functions", "Lorg/jetbrains/kotlin/ir/symbols/IrSimpleFunctionSymbol;", "localDelegatedProperties", "Lorg/jetbrains/kotlin/ir/symbols/IrLocalDelegatedPropertySymbol;", "properties", "Lorg/jetbrains/kotlin/ir/symbols/IrPropertySymbol;", "returnableBlocks", "Lorg/jetbrains/kotlin/ir/symbols/IrReturnableBlockSymbol;", "typeAliases", "Lorg/jetbrains/kotlin/ir/symbols/IrTypeAliasSymbol;", "typeParameters", "Lorg/jetbrains/kotlin/ir/symbols/IrTypeParameterSymbol;", "valueParameters", "Lorg/jetbrains/kotlin/ir/symbols/IrValueParameterSymbol;", "variables", "Lorg/jetbrains/kotlin/ir/symbols/IrVariableSymbol;", "dump", "", "getDeclaredClass", "symbol", "getDeclaredConstructor", "getDeclaredEnumEntry", "getDeclaredExternalPackageFragment", "getDeclaredField", "getDeclaredFile", "getDeclaredFunction", "getDeclaredLocalDelegatedProperty", "getDeclaredProperty", "getDeclaredTypeAlias", "getDeclaredTypeParameter", "getDeclaredValueParameter", "getDeclaredVariable", "getReferencedClass", "getReferencedClassOrNull", "getReferencedClassifier", "Lorg/jetbrains/kotlin/ir/symbols/IrClassifierSymbol;", "getReferencedConstructor", "getReferencedEnumEntry", "getReferencedField", "getReferencedFunction", "Lorg/jetbrains/kotlin/ir/symbols/IrFunctionSymbol;", "getReferencedLocalDelegatedProperty", "getReferencedProperty", "getReferencedReturnableBlock", "getReferencedSimpleFunction", "getReferencedTypeAlias", "getReferencedValue", "Lorg/jetbrains/kotlin/ir/symbols/IrValueSymbol;", "getReferencedVariable", "getDeclared", "T", "Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;", "", "create", "Lkotlin/Function0;", "(Ljava/util/Map;Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;Lkotlin/jvm/functions/Function0;)Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;", "getReferenced", "(Ljava/util/Map;Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;)Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;", "compiler-plugin"})
public final class LazySymbolDeepCopyRemapper implements org.jetbrains.kotlin.ir.util.SymbolRemapper {
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrClassSymbol, org.jetbrains.kotlin.ir.symbols.IrClassSymbol> classes = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol, org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol> constructors = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol, org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol> enumEntries = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrExternalPackageFragmentSymbol, org.jetbrains.kotlin.ir.symbols.IrExternalPackageFragmentSymbol> externalPackageFragments = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrFieldSymbol, org.jetbrains.kotlin.ir.symbols.IrFieldSymbol> fields = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrFileSymbol, org.jetbrains.kotlin.ir.symbols.IrFileSymbol> files = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol, org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol> functions = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrPropertySymbol, org.jetbrains.kotlin.ir.symbols.IrPropertySymbol> properties = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrReturnableBlockSymbol, org.jetbrains.kotlin.ir.symbols.IrReturnableBlockSymbol> returnableBlocks = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol, org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol> typeParameters = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol, org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol> valueParameters = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrVariableSymbol, org.jetbrains.kotlin.ir.symbols.IrVariableSymbol> variables = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrLocalDelegatedPropertySymbol, org.jetbrains.kotlin.ir.symbols.IrLocalDelegatedPropertySymbol> localDelegatedProperties = null;
    private final java.util.HashMap<org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol, org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol> typeAliases = null;
    
    private final <T extends org.jetbrains.kotlin.ir.symbols.IrSymbol>T getDeclared(@org.jetbrains.annotations.NotNull
    java.util.Map<T, T> $this$getDeclared, T symbol, kotlin.jvm.functions.Function0<? extends T> create) {
        return null;
    }
    
    public final void dump() {
    }
    
    private final <T extends org.jetbrains.kotlin.ir.symbols.IrSymbol>T getReferenced(@org.jetbrains.annotations.NotNull
    java.util.Map<T, T> $this$getReferenced, T symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrClassSymbol getDeclaredClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrClassSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol getDeclaredFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrPropertySymbol getDeclaredProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrPropertySymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrFieldSymbol getDeclaredField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrFieldSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrFileSymbol getDeclaredFile(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrFileSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol getDeclaredConstructor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol getDeclaredEnumEntry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrExternalPackageFragmentSymbol getDeclaredExternalPackageFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrExternalPackageFragmentSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrVariableSymbol getDeclaredVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrVariableSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol getDeclaredTypeParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrTypeParameterSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol getDeclaredValueParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrValueParameterSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrLocalDelegatedPropertySymbol getDeclaredLocalDelegatedProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrLocalDelegatedPropertySymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol getDeclaredTypeAlias(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrClassSymbol getReferencedClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrClassSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrClassSymbol getReferencedClassOrNull(@org.jetbrains.annotations.Nullable
    org.jetbrains.kotlin.ir.symbols.IrClassSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol getReferencedEnumEntry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrEnumEntrySymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrVariableSymbol getReferencedVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrVariableSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrLocalDelegatedPropertySymbol getReferencedLocalDelegatedProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrLocalDelegatedPropertySymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrFieldSymbol getReferencedField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrFieldSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol getReferencedConstructor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol getReferencedSimpleFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrPropertySymbol getReferencedProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrPropertySymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrValueSymbol getReferencedValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrValueSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol getReferencedFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrReturnableBlockSymbol getReferencedReturnableBlock(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrReturnableBlockSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol getReferencedClassifier(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrClassifierSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol getReferencedTypeAlias(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrTypeAliasSymbol symbol) {
        return null;
    }
    
    public LazySymbolDeepCopyRemapper() {
        super();
    }
}