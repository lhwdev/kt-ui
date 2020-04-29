package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u00b2\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\u0010\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0004J\u0092\u0001\u0010\u000b\u001a\n \r*\u0004\u0018\u0001H\fH\f\"\u0010\b\u0000\u0010\f*\n \r*\u0004\u0018\u00010\u000e0\u000e\"\u0010\b\u0001\u0010\u000f*\n \r*\u0004\u0018\u00010\u000e0\u000e2F\u0010\u0010\u001aB\u0012\f\u0012\n \r*\u0004\u0018\u0001H\fH\f\u0012\f\u0012\n \r*\u0004\u0018\u0001H\u000fH\u000f \r* \u0012\f\u0012\n \r*\u0004\u0018\u0001H\fH\f\u0012\f\u0012\n \r*\u0004\u0018\u0001H\u000fH\u000f\u0018\u00010\u00110\u00112\u000e\u0010\u0012\u001a\n \r*\u0004\u0018\u0001H\u000fH\u000fH\u0096\u0001\u00a2\u0006\u0002\u0010\u0013JQ\u0010\u0014\u001a\u00020\u00152F\u0010\u0010\u001aB\u0012\f\u0012\n \r*\u0004\u0018\u00010\u00160\u0016\u0012\f\u0012\n \r*\u0004\u0018\u00010\u00160\u0016 \r* \u0012\f\u0012\n \r*\u0004\u0018\u00010\u00160\u0016\u0012\f\u0012\n \r*\u0004\u0018\u00010\u00160\u0016\u0018\u00010\u00110\u0011H\u0096\u0001JQ\u0010\u0017\u001a\u00020\u00012\u000e\u0010\u0010\u001a\n \r*\u0004\u0018\u00010\u00180\u00182\u000e\u0010\u0012\u001a\n \r*\u0004\u0018\u00010\u00190\u00192\u000e\u0010\u001a\u001a\n \r*\u0004\u0018\u00010\u001b0\u001b2\u000e\u0010\u001c\u001a\n \r*\u0004\u0018\u00010\u001d0\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0097\u0001J\t\u0010 \u001a\u00020\u0018H\u0097\u0001J\u000b\u0010!\u001a\u0004\u0018\u00010\"H\u0097\u0001J\u000b\u0010#\u001a\u0004\u0018\u00010\"H\u0097\u0001J\u000b\u0010$\u001a\u0004\u0018\u00010%H\u0097\u0001J\t\u0010&\u001a\u00020\u001dH\u0097\u0001J\t\u0010\'\u001a\u00020\u0019H\u0097\u0001J\t\u0010(\u001a\u00020)H\u0097\u0001J\t\u0010*\u001a\u00020\u0001H\u0097\u0001J-\u0010+\u001a&\u0012\u000e\b\u0001\u0012\n \r*\u0004\u0018\u00010%0% \r*\u0010\u0012\f\u0012\n \r*\u0004\u0018\u00010%0%0-0,H\u0097\u0001J\u000b\u0010.\u001a\u0004\u0018\u00010/H\u0097\u0001J\t\u00100\u001a\u000201H\u0097\u0001J\u0017\u00102\u001a\u0010\u0012\f\u0012\n \r*\u0004\u0018\u0001040403H\u0097\u0001JN\u00105\u001a\u0004\u0018\u0001H6\"\u0010\b\u0000\u00106*\n \r*\u0004\u0018\u00010\u000e0\u000e2*\u0010\u0010\u001a&\u0012\f\u0012\n \r*\u0004\u0018\u0001H6H6 \r*\u0012\u0012\f\u0012\n \r*\u0004\u0018\u0001H6H6\u0018\u00010707H\u0097\u0001\u00a2\u0006\u0002\u00108J+\u00109\u001a$\u0012\f\u0012\n \r*\u0004\u0018\u00010;0; \r*\u0010\u0012\f\u0012\n \r*\u0004\u0018\u00010;0;030:H\u0097\u0001J\t\u0010<\u001a\u00020\u001bH\u0097\u0001J\t\u0010=\u001a\u00020\u001fH\u0096\u0001J\t\u0010>\u001a\u00020\u001fH\u0096\u0001J\t\u0010?\u001a\u00020\u001fH\u0096\u0001J\t\u0010@\u001a\u00020\u001fH\u0096\u0001J\t\u0010A\u001a\u00020\u001fH\u0096\u0001J\t\u0010B\u001a\u00020\u001fH\u0096\u0001J\t\u0010C\u001a\u00020\u001fH\u0096\u0001J\t\u0010D\u001a\u00020\u001fH\u0096\u0001J\t\u0010E\u001a\u00020\u001fH\u0096\u0001J\t\u0010F\u001a\u00020\u001fH\u0096\u0001J\t\u0010G\u001a\u00020\u001fH\u0096\u0001J\t\u0010H\u001a\u00020\u001fH\u0096\u0001J\u0019\u0010I\u001a\u0012\u0012\u000e\b\u0001\u0012\n \r*\u0004\u0018\u00010\u00010\u00010JH\u0097\u0001J7\u0010K\u001a\u00020\u00152,\b\u0001\u0010\u0010\u001a&\u0012\u000e\b\u0001\u0012\n \r*\u0004\u0018\u00010L0L \r*\u0010\u0012\f\u0012\n \r*\u0004\u0018\u00010L0L0-0,H\u0096\u0001J\u0012\u0010M\u001a\u0004\u0018\u00010%2\u0006\u0010N\u001a\u00020OH\u0016R\u0012\u0010\u0005\u001a\u00020\u0006X\u0096\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u0003\u001a\u00020\u0001X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006P"}, d2 = {"Lcom/asmx/ui/plugin/compiler/WidgetSimpleFunctionDescriptorImpl;", "Lorg/jetbrains/kotlin/descriptors/SimpleFunctionDescriptor;", "Lcom/asmx/ui/plugin/compiler/WidgetFunctionDescriptor;", "underlyingDescriptor", "(Lorg/jetbrains/kotlin/descriptors/SimpleFunctionDescriptor;)V", "annotations", "Lorg/jetbrains/kotlin/descriptors/annotations/Annotations;", "getAnnotations", "()Lorg/jetbrains/kotlin/descriptors/annotations/Annotations;", "getUnderlyingDescriptor", "()Lorg/jetbrains/kotlin/descriptors/SimpleFunctionDescriptor;", "accept", "R", "kotlin.jvm.PlatformType", "", "D", "p0", "Lorg/jetbrains/kotlin/descriptors/DeclarationDescriptorVisitor;", "p1", "(Lorg/jetbrains/kotlin/descriptors/DeclarationDescriptorVisitor;Ljava/lang/Object;)Ljava/lang/Object;", "acceptVoid", "", "Ljava/lang/Void;", "copy", "Lorg/jetbrains/kotlin/descriptors/DeclarationDescriptor;", "Lorg/jetbrains/kotlin/descriptors/Modality;", "p2", "Lorg/jetbrains/kotlin/descriptors/Visibility;", "p3", "Lorg/jetbrains/kotlin/descriptors/CallableMemberDescriptor$Kind;", "p4", "", "getContainingDeclaration", "getDispatchReceiverParameter", "Lorg/jetbrains/kotlin/descriptors/ReceiverParameterDescriptor;", "getExtensionReceiverParameter", "getInitialSignatureDescriptor", "Lorg/jetbrains/kotlin/descriptors/FunctionDescriptor;", "getKind", "getModality", "getName", "Lorg/jetbrains/kotlin/name/Name;", "getOriginal", "getOverriddenDescriptors", "", "", "getReturnType", "Lorg/jetbrains/kotlin/types/KotlinType;", "getSource", "Lorg/jetbrains/kotlin/descriptors/SourceElement;", "getTypeParameters", "", "Lorg/jetbrains/kotlin/descriptors/TypeParameterDescriptor;", "getUserData", "V", "Lorg/jetbrains/kotlin/descriptors/CallableDescriptor$UserDataKey;", "(Lorg/jetbrains/kotlin/descriptors/CallableDescriptor$UserDataKey;)Ljava/lang/Object;", "getValueParameters", "", "Lorg/jetbrains/kotlin/descriptors/ValueParameterDescriptor;", "getVisibility", "hasStableParameterNames", "hasSynthesizedParameterNames", "isActual", "isExpect", "isExternal", "isHiddenForResolutionEverywhereBesideSupercalls", "isHiddenToOvercomeSignatureClash", "isInfix", "isInline", "isOperator", "isSuspend", "isTailrec", "newCopyBuilder", "Lorg/jetbrains/kotlin/descriptors/FunctionDescriptor$CopyBuilder;", "setOverriddenDescriptors", "Lorg/jetbrains/kotlin/descriptors/CallableMemberDescriptor;", "substitute", "substitutor", "Lorg/jetbrains/kotlin/types/TypeSubstitutor;", "compiler-plugin"})
public final class WidgetSimpleFunctionDescriptorImpl implements org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor, com.asmx.ui.plugin.compiler.WidgetFunctionDescriptor {
    @org.jetbrains.annotations.NotNull
    private final org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor underlyingDescriptor = null;
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.FunctionDescriptor substitute(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.TypeSubstitutor substitutor) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor getUnderlyingDescriptor() {
        return null;
    }
    
    public WidgetSimpleFunctionDescriptorImpl(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor underlyingDescriptor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.annotations.Annotations getAnnotations() {
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
    public org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor copy(org.jetbrains.kotlin.descriptors.DeclarationDescriptor p0, org.jetbrains.kotlin.descriptors.Modality p1, org.jetbrains.kotlin.descriptors.Visibility p2, org.jetbrains.kotlin.descriptors.CallableMemberDescriptor.Kind p3, boolean p4) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.DeclarationDescriptor getContainingDeclaration() {
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
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.FunctionDescriptor getInitialSignatureDescriptor() {
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
    public org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor getOriginal() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.util.Collection<? extends org.jetbrains.kotlin.descriptors.FunctionDescriptor> getOverriddenDescriptors() {
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
    public boolean isExpect() {
        return false;
    }
    
    @java.lang.Override
    public boolean isExternal() {
        return false;
    }
    
    @java.lang.Override
    public boolean isHiddenForResolutionEverywhereBesideSupercalls() {
        return false;
    }
    
    @java.lang.Override
    public boolean isHiddenToOvercomeSignatureClash() {
        return false;
    }
    
    @java.lang.Override
    public boolean isInfix() {
        return false;
    }
    
    @java.lang.Override
    public boolean isInline() {
        return false;
    }
    
    @java.lang.Override
    public boolean isOperator() {
        return false;
    }
    
    @java.lang.Override
    public boolean isSuspend() {
        return false;
    }
    
    @java.lang.Override
    public boolean isTailrec() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public org.jetbrains.kotlin.descriptors.FunctionDescriptor.CopyBuilder<? extends org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor> newCopyBuilder() {
        return null;
    }
    
    @java.lang.Override
    public void setOverriddenDescriptors(@org.jetbrains.annotations.NotNull
    java.util.Collection<? extends org.jetbrains.kotlin.descriptors.CallableMemberDescriptor> p0) {
    }
}