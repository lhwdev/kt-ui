package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u00d0\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\u0010\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0004J\u0092\u0001\u0010\u0016\u001a\n \u0018*\u0004\u0018\u0001H\u0017H\u0017\"\u0010\b\u0000\u0010\u0017*\n \u0018*\u0004\u0018\u00010\u00190\u0019\"\u0010\b\u0001\u0010\u001a*\n \u0018*\u0004\u0018\u00010\u00190\u00192F\u0010\u001b\u001aB\u0012\f\u0012\n \u0018*\u0004\u0018\u0001H\u0017H\u0017\u0012\f\u0012\n \u0018*\u0004\u0018\u0001H\u001aH\u001a \u0018* \u0012\f\u0012\n \u0018*\u0004\u0018\u0001H\u0017H\u0017\u0012\f\u0012\n \u0018*\u0004\u0018\u0001H\u001aH\u001a\u0018\u00010\u001c0\u001c2\u000e\u0010\u001d\u001a\n \u0018*\u0004\u0018\u0001H\u001aH\u001aH\u0096\u0001\u00a2\u0006\u0002\u0010\u001eJQ\u0010\u001f\u001a\u00020 2F\u0010\u001b\u001aB\u0012\f\u0012\n \u0018*\u0004\u0018\u00010!0!\u0012\f\u0012\n \u0018*\u0004\u0018\u00010!0! \u0018* \u0012\f\u0012\n \u0018*\u0004\u0018\u00010!0!\u0012\f\u0012\n \u0018*\u0004\u0018\u00010!0!\u0018\u00010\u001c0\u001cH\u0096\u0001JQ\u0010\"\u001a\u00020#2\u000e\u0010\u001b\u001a\n \u0018*\u0004\u0018\u00010$0$2\u000e\u0010\u001d\u001a\n \u0018*\u0004\u0018\u00010%0%2\u000e\u0010&\u001a\n \u0018*\u0004\u0018\u00010\'0\'2\u000e\u0010(\u001a\n \u0018*\u0004\u0018\u00010)0)2\u0006\u0010*\u001a\u00020\u000eH\u0097\u0001J+\u0010+\u001a$\u0012\f\u0012\n \u0018*\u0004\u0018\u00010-0- \u0018*\u0010\u0012\f\u0012\n \u0018*\u0004\u0018\u00010-0-0.0,H\u0097\u0001J\u000b\u0010/\u001a\u0004\u0018\u000100H\u0097\u0001J\u000f\u00101\u001a\b\u0012\u0002\b\u0003\u0018\u000102H\u0097\u0001J\t\u00103\u001a\u00020$H\u0097\u0001J\u000b\u00104\u001a\u0004\u0018\u000100H\u0097\u0001J\u000b\u00105\u001a\u0004\u0018\u000106H\u0097\u0001J\u000b\u00107\u001a\u0004\u0018\u000106H\u0097\u0001J\t\u00108\u001a\u00020)H\u0097\u0001J\t\u00109\u001a\u00020%H\u0097\u0001J\t\u0010:\u001a\u00020;H\u0097\u0001J\t\u0010<\u001a\u00020\u0001H\u0097\u0001J-\u0010=\u001a&\u0012\u000e\b\u0001\u0012\n \u0018*\u0004\u0018\u00010\u00010\u0001 \u0018*\u0010\u0012\f\u0012\n \u0018*\u0004\u0018\u00010\u00010\u00010?0>H\u0097\u0001J\u000b\u0010@\u001a\u0004\u0018\u00010AH\u0097\u0001J\t\u0010B\u001a\u00020CH\u0097\u0001J\t\u0010D\u001a\u00020AH\u0097\u0001J\u0017\u0010E\u001a\u0010\u0012\f\u0012\n \u0018*\u0004\u0018\u00010F0F0.H\u0097\u0001JN\u0010G\u001a\u0004\u0018\u0001HH\"\u0010\b\u0000\u0010H*\n \u0018*\u0004\u0018\u00010\u00190\u00192*\u0010\u001b\u001a&\u0012\f\u0012\n \u0018*\u0004\u0018\u0001HHHH \u0018*\u0012\u0012\f\u0012\n \u0018*\u0004\u0018\u0001HHHH\u0018\u00010I0IH\u0097\u0001\u00a2\u0006\u0002\u0010JJ+\u0010K\u001a$\u0012\f\u0012\n \u0018*\u0004\u0018\u00010L0L \u0018*\u0010\u0012\f\u0012\n \u0018*\u0004\u0018\u00010L0L0.0,H\u0097\u0001J\t\u0010M\u001a\u00020\'H\u0097\u0001J\t\u0010N\u001a\u00020\u000eH\u0096\u0001J\t\u0010O\u001a\u00020\u000eH\u0096\u0001J\t\u0010P\u001a\u00020\u000eH\u0096\u0001J\t\u0010Q\u001a\u00020\u000eH\u0096\u0001J\t\u0010R\u001a\u00020\u000eH\u0096\u0001J\t\u0010S\u001a\u00020\u000eH\u0096\u0001J\t\u0010T\u001a\u00020\u000eH\u0096\u0001J\t\u0010U\u001a\u00020\u000eH\u0096\u0001J\t\u0010V\u001a\u00020\u000eH\u0096\u0001J\u0019\u0010W\u001a\u0012\u0012\u000e\b\u0001\u0012\n \u0018*\u0004\u0018\u00010\u00010\u00010XH\u0097\u0001J7\u0010Y\u001a\u00020 2,\b\u0001\u0010\u001b\u001a&\u0012\u000e\b\u0001\u0012\n \u0018*\u0004\u0018\u00010#0# \u0018*\u0010\u0012\f\u0012\n \u0018*\u0004\u0018\u00010#0#0?0>H\u0096\u0001J\u0012\u0010Z\u001a\u0004\u0018\u00010\u00012\u0006\u0010[\u001a\u00020\\H\u0016R\u0012\u0010\u0005\u001a\u00020\u0006X\u0096\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0016\u0010\t\u001a\u0004\u0018\u00010\n8WX\u0096\u0005\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u000e8\u0016X\u0097\u0005\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000fR\u0016\u0010\u0010\u001a\u0004\u0018\u00010\u00118WX\u0096\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0003\u001a\u00020\u0001X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006]"}, d2 = {"Lcom/asmx/ui/plugin/compiler/WidgetPropertyDescriptorImpl;", "Lorg/jetbrains/kotlin/descriptors/PropertyDescriptor;", "Lcom/asmx/ui/plugin/compiler/WidgetPropertyDescriptor;", "underlyingDescriptor", "(Lorg/jetbrains/kotlin/descriptors/PropertyDescriptor;)V", "annotations", "Lorg/jetbrains/kotlin/descriptors/annotations/Annotations;", "getAnnotations", "()Lorg/jetbrains/kotlin/descriptors/annotations/Annotations;", "getter", "Lorg/jetbrains/kotlin/descriptors/PropertyGetterDescriptor;", "getGetter", "()Lorg/jetbrains/kotlin/descriptors/PropertyGetterDescriptor;", "isDelegated", "", "()Z", "setter", "Lorg/jetbrains/kotlin/descriptors/PropertySetterDescriptor;", "getSetter", "()Lorg/jetbrains/kotlin/descriptors/PropertySetterDescriptor;", "getUnderlyingDescriptor", "()Lorg/jetbrains/kotlin/descriptors/PropertyDescriptor;", "accept", "R", "kotlin.jvm.PlatformType", "", "D", "p0", "Lorg/jetbrains/kotlin/descriptors/DeclarationDescriptorVisitor;", "p1", "(Lorg/jetbrains/kotlin/descriptors/DeclarationDescriptorVisitor;Ljava/lang/Object;)Ljava/lang/Object;", "acceptVoid", "", "Ljava/lang/Void;", "copy", "Lorg/jetbrains/kotlin/descriptors/CallableMemberDescriptor;", "Lorg/jetbrains/kotlin/descriptors/DeclarationDescriptor;", "Lorg/jetbrains/kotlin/descriptors/Modality;", "p2", "Lorg/jetbrains/kotlin/descriptors/Visibility;", "p3", "Lorg/jetbrains/kotlin/descriptors/CallableMemberDescriptor$Kind;", "p4", "getAccessors", "", "Lorg/jetbrains/kotlin/descriptors/PropertyAccessorDescriptor;", "", "getBackingField", "Lorg/jetbrains/kotlin/descriptors/FieldDescriptor;", "getCompileTimeInitializer", "Lorg/jetbrains/kotlin/resolve/constants/ConstantValue;", "getContainingDeclaration", "getDelegateField", "getDispatchReceiverParameter", "Lorg/jetbrains/kotlin/descriptors/ReceiverParameterDescriptor;", "getExtensionReceiverParameter", "getKind", "getModality", "getName", "Lorg/jetbrains/kotlin/name/Name;", "getOriginal", "getOverriddenDescriptors", "", "", "getReturnType", "Lorg/jetbrains/kotlin/types/KotlinType;", "getSource", "Lorg/jetbrains/kotlin/descriptors/SourceElement;", "getType", "getTypeParameters", "Lorg/jetbrains/kotlin/descriptors/TypeParameterDescriptor;", "getUserData", "V", "Lorg/jetbrains/kotlin/descriptors/CallableDescriptor$UserDataKey;", "(Lorg/jetbrains/kotlin/descriptors/CallableDescriptor$UserDataKey;)Ljava/lang/Object;", "getValueParameters", "Lorg/jetbrains/kotlin/descriptors/ValueParameterDescriptor;", "getVisibility", "hasStableParameterNames", "hasSynthesizedParameterNames", "isActual", "isConst", "isExpect", "isExternal", "isLateInit", "isSetterProjectedOut", "isVar", "newCopyBuilder", "Lorg/jetbrains/kotlin/descriptors/CallableMemberDescriptor$CopyBuilder;", "setOverriddenDescriptors", "substitute", "substitutor", "Lorg/jetbrains/kotlin/types/TypeSubstitutor;", "compiler-plugin"})
public final class WidgetPropertyDescriptorImpl implements org.jetbrains.kotlin.descriptors.PropertyDescriptor, com.asmx.ui.plugin.compiler.WidgetPropertyDescriptor {
    @org.jetbrains.annotations.NotNull
    private final org.jetbrains.kotlin.descriptors.PropertyDescriptor underlyingDescriptor = null;
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.PropertyDescriptor substitute(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.TypeSubstitutor substitutor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.PropertyDescriptor getUnderlyingDescriptor() {
        return null;
    }
    
    public WidgetPropertyDescriptorImpl(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.PropertyDescriptor underlyingDescriptor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.annotations.Annotations getAnnotations() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.PropertyGetterDescriptor getGetter() {
        return null;
    }
    
    @java.lang.Override
    @java.lang.Deprecated
    public boolean isDelegated() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.PropertySetterDescriptor getSetter() {
        return null;
    }
    
    @java.lang.Override
    public <R extends java.lang.Object, D extends java.lang.Object>R accept(org.jetbrains.kotlin.descriptors.DeclarationDescriptorVisitor<R, D> p0, D p1) {
        return null;
    }
    
    @java.lang.Override
    public void acceptVoid(org.jetbrains.kotlin.descriptors.DeclarationDescriptorVisitor<java.lang.Void, java.lang.Void> p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.CallableMemberDescriptor copy(org.jetbrains.kotlin.descriptors.DeclarationDescriptor p0, org.jetbrains.kotlin.descriptors.Modality p1, org.jetbrains.kotlin.descriptors.Visibility p2, org.jetbrains.kotlin.descriptors.CallableMemberDescriptor.Kind p3, boolean p4) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.util.List<org.jetbrains.kotlin.descriptors.PropertyAccessorDescriptor> getAccessors() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.FieldDescriptor getBackingField() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.resolve.constants.ConstantValue<?> getCompileTimeInitializer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.DeclarationDescriptor getContainingDeclaration() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.FieldDescriptor getDelegateField() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.ReceiverParameterDescriptor getDispatchReceiverParameter() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.ReceiverParameterDescriptor getExtensionReceiverParameter() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.CallableMemberDescriptor.Kind getKind() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.Modality getModality() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.name.Name getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.PropertyDescriptor getOriginal() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.util.Collection<? extends org.jetbrains.kotlin.descriptors.PropertyDescriptor> getOverriddenDescriptors() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.types.KotlinType getReturnType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.SourceElement getSource() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.types.KotlinType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    @kotlin.annotations.jvm.ReadOnly
    public java.util.List<org.jetbrains.kotlin.descriptors.TypeParameterDescriptor> getTypeParameters() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public <V extends java.lang.Object>V getUserData(org.jetbrains.kotlin.descriptors.CallableDescriptor.UserDataKey<V> p0) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.util.List<org.jetbrains.kotlin.descriptors.ValueParameterDescriptor> getValueParameters() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.Visibility getVisibility() {
        return null;
    }
    
    @java.lang.Override
    public boolean hasStableParameterNames() {
        return false;
    }
    
    @java.lang.Override
    public boolean hasSynthesizedParameterNames() {
        return false;
    }
    
    @java.lang.Override
    public boolean isActual() {
        return false;
    }
    
    @java.lang.Override
    public boolean isConst() {
        return false;
    }
    
    @java.lang.Override
    public boolean isExpect() {
        return false;
    }
    
    @java.lang.Override
    public boolean isExternal() {
        return false;
    }
    
    @java.lang.Override
    public boolean isLateInit() {
        return false;
    }
    
    @java.lang.Override
    public boolean isSetterProjectedOut() {
        return false;
    }
    
    @java.lang.Override
    public boolean isVar() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.CallableMemberDescriptor.CopyBuilder<? extends org.jetbrains.kotlin.descriptors.PropertyDescriptor> newCopyBuilder() {
        return null;
    }
    
    @java.lang.Override
    public void setOverriddenDescriptors(@org.jetbrains.annotations.NotNull
    java.util.Collection<? extends org.jetbrains.kotlin.descriptors.CallableMemberDescriptor> p0) {
    }
}