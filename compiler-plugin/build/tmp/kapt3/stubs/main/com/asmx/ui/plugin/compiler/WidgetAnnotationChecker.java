package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 /2\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0001/B\u0005\u00a2\u0006\u0002\u0010\u0005J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ \u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ \u0010\u0010\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0007H\u0002J \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\n\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J$\u0010\u0012\u001a\u00020\u00132\n\u0010\u0019\u001a\u0006\u0012\u0002\b\u00030\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\u001dH\u0016J,\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020\u000f2\n\u0010#\u001a\u0006\u0012\u0002\b\u00030$H\u0016J \u0010%\u001a\u00020\u00132\u0006\u0010&\u001a\u00020\'2\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+H\u0016J\u001a\u0010,\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\n\u0010\u0019\u001a\u0006\u0012\u0002\b\u00030\u001aJ\u0015\u0010-\u001a\u00020\u0007*\u00020\u00072\u0006\u0010.\u001a\u00020\u0007H\u0086\u0002\u00a8\u00060"}, d2 = {"Lcom/asmx/ui/plugin/compiler/WidgetAnnotationChecker;", "Lorg/jetbrains/kotlin/resolve/calls/checkers/CallChecker;", "Lorg/jetbrains/kotlin/resolve/checkers/DeclarationChecker;", "Lorg/jetbrains/kotlin/resolve/calls/checkers/AdditionalTypeChecker;", "Lorg/jetbrains/kotlin/extensions/StorageComponentContainerContributor;", "()V", "analyze", "Lcom/asmx/ui/plugin/compiler/WidgetKind;", "trace", "Lorg/jetbrains/kotlin/resolve/BindingTrace;", "descriptor", "Lorg/jetbrains/kotlin/descriptors/FunctionDescriptor;", "element", "Lorg/jetbrains/kotlin/psi/KtElement;", "type", "Lorg/jetbrains/kotlin/types/KotlinType;", "analyzeFunctionContents", "signatureWidgetability", "check", "", "declaration", "Lorg/jetbrains/kotlin/psi/KtDeclaration;", "Lorg/jetbrains/kotlin/descriptors/DeclarationDescriptor;", "context", "Lorg/jetbrains/kotlin/resolve/checkers/DeclarationCheckerContext;", "resolvedCall", "Lorg/jetbrains/kotlin/resolve/calls/model/ResolvedCall;", "reportOn", "Lorg/jetbrains/kotlin/com/intellij/psi/PsiElement;", "Lorg/jetbrains/kotlin/resolve/calls/checkers/CallCheckerContext;", "checkType", "expression", "Lorg/jetbrains/kotlin/psi/KtExpression;", "expressionType", "expressionTypeWithSmartCast", "c", "Lorg/jetbrains/kotlin/resolve/calls/context/ResolutionContext;", "registerModuleComponents", "container", "Lorg/jetbrains/kotlin/container/StorageComponentContainer;", "platform", "Lorg/jetbrains/kotlin/platform/TargetPlatform;", "moduleDescriptor", "Lorg/jetbrains/kotlin/descriptors/ModuleDescriptor;", "shouldInvokeAsTag", "plus", "rhs", "Companion", "compiler-plugin"})
public final class WidgetAnnotationChecker implements org.jetbrains.kotlin.resolve.calls.checkers.CallChecker, org.jetbrains.kotlin.resolve.checkers.DeclarationChecker, org.jetbrains.kotlin.resolve.calls.checkers.AdditionalTypeChecker, org.jetbrains.kotlin.extensions.StorageComponentContainerContributor {
    public static final com.asmx.ui.plugin.compiler.WidgetAnnotationChecker.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull
    public final com.asmx.ui.plugin.compiler.WidgetKind shouldInvokeAsTag(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.resolve.BindingTrace trace, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.resolve.calls.model.ResolvedCall<?> resolvedCall) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.asmx.ui.plugin.compiler.WidgetKind analyze(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.resolve.BindingTrace trace, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.FunctionDescriptor descriptor) {
        return null;
    }
    
    private final com.asmx.ui.plugin.compiler.WidgetKind analyzeFunctionContents(org.jetbrains.kotlin.resolve.BindingTrace trace, org.jetbrains.kotlin.psi.KtElement element, com.asmx.ui.plugin.compiler.WidgetKind signatureWidgetability) {
        return null;
    }
    
    /**
     * Analyze a KtElement
     * - Determine if it is @Widget or @WidgetUtil (eg. the element or inferred type has an @Widget annotation)
     * - Update the binding context to cache analysis results
     * - Report errors (eg. invocations of an widgetKind etc)
     * - Return the kind of the element
     */
    @org.jetbrains.annotations.NotNull
    public final com.asmx.ui.plugin.compiler.WidgetKind analyze(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.resolve.BindingTrace trace, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.psi.KtElement element, @org.jetbrains.annotations.Nullable
    org.jetbrains.kotlin.types.KotlinType type) {
        return null;
    }
    
    @java.lang.Override
    public void registerModuleComponents(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.container.StorageComponentContainer container, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.platform.TargetPlatform platform, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ModuleDescriptor moduleDescriptor) {
    }
    
    @java.lang.Override
    public void check(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.psi.KtDeclaration declaration, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.DeclarationDescriptor descriptor, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.resolve.checkers.DeclarationCheckerContext context) {
    }
    
    @java.lang.Override
    public void check(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.resolve.calls.model.ResolvedCall<?> resolvedCall, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.com.intellij.psi.PsiElement reportOn, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.resolve.calls.checkers.CallCheckerContext context) {
    }
    
    @java.lang.Override
    public void checkType(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.psi.KtExpression expression, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.KotlinType expressionType, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.types.KotlinType expressionTypeWithSmartCast, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.resolve.calls.context.ResolutionContext<?> c) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.asmx.ui.plugin.compiler.WidgetKind plus(@org.jetbrains.annotations.NotNull
    com.asmx.ui.plugin.compiler.WidgetKind $this$plus, @org.jetbrains.annotations.NotNull
    com.asmx.ui.plugin.compiler.WidgetKind rhs) {
        return null;
    }
    
    public WidgetAnnotationChecker() {
        super();
    }
    
    public void checkReceiver(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.descriptors.ReceiverParameterDescriptor receiverParameter, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.resolve.scopes.receivers.ReceiverValue receiverArgument, boolean safeAccess, @org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.resolve.calls.context.CallResolutionContext<?> c) {
    }
    
    @kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/asmx/ui/plugin/compiler/WidgetAnnotationChecker$Companion;", "", "()V", "get", "Lcom/asmx/ui/plugin/compiler/WidgetAnnotationChecker;", "project", "Lorg/jetbrains/kotlin/com/intellij/openapi/project/Project;", "compiler-plugin"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull
        public final com.asmx.ui.plugin.compiler.WidgetAnnotationChecker get(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.com.intellij.openapi.project.Project project) {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}