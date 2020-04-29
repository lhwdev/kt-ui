package com.lhwdev.ktui.plugin.compiler

import com.lhwdev.ktui.plugin.compiler.UiWritableSlices.FCS_RESOLVEDCALL_WIDGET
import com.lhwdev.ktui.plugin.compiler.UiWritableSlices.WIDGET_ANALYSIS
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.container.useInstance
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.impl.AnonymousFunctionDescriptor
import org.jetbrains.kotlin.descriptors.impl.LocalVariableDescriptor
import org.jetbrains.kotlin.diagnostics.Errors
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.load.java.descriptors.JavaMethodDescriptor
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.platform.jvm.isJvm
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.checkers.AdditionalTypeChecker
import org.jetbrains.kotlin.resolve.calls.checkers.CallChecker
import org.jetbrains.kotlin.resolve.calls.checkers.CallCheckerContext
import org.jetbrains.kotlin.resolve.calls.context.ResolutionContext
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.calls.model.VariableAsFunctionResolvedCall
import org.jetbrains.kotlin.resolve.checkers.DeclarationChecker
import org.jetbrains.kotlin.resolve.checkers.DeclarationCheckerContext
import org.jetbrains.kotlin.resolve.inline.InlineUtil.isInlinedArgument
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeUtils
import org.jetbrains.kotlin.types.lowerIfFlexible
import org.jetbrains.kotlin.types.typeUtil.builtIns
import org.jetbrains.kotlin.types.upperIfFlexible
import org.jetbrains.kotlin.util.OperatorNameConventions


class WidgetAnnotationChecker : CallChecker, DeclarationChecker,
	AdditionalTypeChecker, /*AdditionalAnnotationChecker,*/ StorageComponentContainerContributor {
	
	companion object {
		fun get(project: Project): WidgetAnnotationChecker {
			return StorageComponentContainerContributor.getInstances(project).single {
				it is WidgetAnnotationChecker
			} as WidgetAnnotationChecker
		}
	}
	
	fun shouldInvokeAsTag(trace: BindingTrace, resolvedCall: ResolvedCall<*>): WidgetKind {
		if(resolvedCall is VariableAsFunctionResolvedCall) {
			resolvedCall.variableCall.candidateDescriptor.type.anyWidgetKind?.let { return it }
			resolvedCall.functionCall.resultingDescriptor.anyWidgetKind?.let { return it }
		}
		val candidateDescriptor = resolvedCall.candidateDescriptor
		if(candidateDescriptor is FunctionDescriptor) {
			if(candidateDescriptor.isOperator &&
				candidateDescriptor.name == OperatorNameConventions.INVOKE) {
				resolvedCall.dispatchReceiver?.type?.anyWidgetKind?.let { return it }
			}
		}
		if(candidateDescriptor is FunctionDescriptor) {
			return analyze(trace, candidateDescriptor)
		}
		if(candidateDescriptor is ValueParameterDescriptor) {
			return candidateDescriptor.type.widgetKind
		}
		if(candidateDescriptor is LocalVariableDescriptor) {
			return candidateDescriptor.type.widgetKind
		}
		if(candidateDescriptor is PropertyDescriptor) {
			return candidateDescriptor.widgetKind
		}
		return candidateDescriptor.widgetKind
	}
	
	fun analyze(trace: BindingTrace, descriptor: FunctionDescriptor): WidgetKind {
		val unwrappedDescriptor = when(descriptor) {
//			is WidgetFunctionDescriptor -> descriptor.underlyingDescriptor // TODO
			else -> descriptor
		}
		val psi = unwrappedDescriptor.findPsi() as? KtElement
		psi?.let { element -> trace.bindingContext.get(WIDGET_ANALYSIS, element)?.let { return it } }
//		if (unwrappedDescriptor.name == Name.identifier("compose") &&
//			unwrappedDescriptor.containingDeclaration is ClassDescriptor &&
//			isWidgetComponent(unwrappedDescriptor.containingDeclaration)
//		) return WidgetKind.marked
//		if (trace.bindingContext.get(
//				INFERRED_COMPOSABLE_DESCRIPTOR,
//				unwrappedDescriptor
//			) ?: false) {
//			widgetKind = WidgetKind.marked
//		} else {
		var widgetKind = when(unwrappedDescriptor) {
//				is VariableDescriptor ->
//					if (unwrappedDescriptor.hasWidgetAnnotation() ||
//						unwrappedDescriptor.type.hasWidgetAnnotation()
//					)
//						WidgetKind =
//							WidgetKind.marked
			is ConstructorDescriptor -> unwrappedDescriptor.widgetKind
			is JavaMethodDescriptor -> unwrappedDescriptor.widgetKind
			is AnonymousFunctionDescriptor -> {
//				if(unwrappedDescriptor.hasWidgetAnnotation()) WidgetKind =
//					widgetKind.Marked
				unwrappedDescriptor.widgetKind
//				if(psi is KtFunctionLiteral && psi.isEmitInline(trace.bindingContext)) {
//					widgetKind = WidgetKind.Marked
//				}
			}
			is PropertyGetterDescriptor -> unwrappedDescriptor.widgetKind
			
			else -> unwrappedDescriptor.widgetKind
		}
		(unwrappedDescriptor.findPsi() as? KtElement)?.let { element ->
			widgetKind = analyzeFunctionContents(trace, element, widgetKind)
		}
		psi?.let { trace.record(WIDGET_ANALYSIS, it, widgetKind) }
		return widgetKind
	}
	
	private fun analyzeFunctionContents(
		trace: BindingTrace,
		element: KtElement,
		signatureWidgetability: WidgetKind
	): WidgetKind {
		var widgetKind = signatureWidgetability
		var localFcs = false
		var isInlineLambda = false
		
		element.accept(object : KtTreeVisitorVoid() {
			override fun visitNamedFunction(function: KtNamedFunction) {
				if(function == element) {
					super.visitNamedFunction(function)
				}
			}
			
			override fun visitPropertyAccessor(accessor: KtPropertyAccessor) {
				// this is basically a function, so unless it is the function we are analyzing, we
				// stop here
				if(accessor == element) {
					super.visitPropertyAccessor(accessor)
				}
			}
			
			override fun visitClass(klass: KtClass) {
				// never traverse a class
			}
			
			override fun visitLambdaExpression(lambdaExpression: KtLambdaExpression) {
				val isInlineable = isInlinedArgument(
					lambdaExpression.functionLiteral,
					trace.bindingContext,
					true
				)
				if(isInlineable && lambdaExpression == element) isInlineLambda = true
				if(isInlineable || lambdaExpression == element)
					super.visitLambdaExpression(lambdaExpression)
			}
			
			override fun visitCallExpression(expression: KtCallExpression) {
				val resolvedCall = expression.getResolvedCall(trace.bindingContext)
				checkResolvedCall(
					resolvedCall,
					trace.get(FCS_RESOLVEDCALL_WIDGET, expression),
					expression.calleeExpression ?: expression
				)
				super.visitCallExpression(expression)
			}
			
			@Suppress("NON_EXHAUSTIVE_WHEN")
			fun checkWidgetCall(from: WidgetKind, to: WidgetKind, reportElement: PsiElement) {
				log5("checkWidgetCall $from -> $to of ${reportElement.text}")
				when(from) {
					WidgetKind.none -> when(to) {
						WidgetKind.widget -> trace.report(UiErrors.widgetInvocationInNonWidget.on(reportElement))
						WidgetKind.widgetUtil -> trace.report(UiErrors.widgetUtilInvocationInNonWidget.on(reportElement))
					}
					WidgetKind.widgetUtil -> if(to == WidgetKind.widget)
						trace.report(UiErrors.widgetInvocationInWidgetUtil.on(reportElement))
				}
				
			}
			
			private fun checkResolvedCall(
				resolvedCall: ResolvedCall<*>?,
				callKind: WidgetKind?,
				reportElement: KtExpression
			) {
				log3("checkResolvedCall")
				resolvedCall?.candidateDescriptor?.widgetKind?.let { kind ->
					if(kind != WidgetKind.none) {
						localFcs = true
						if(!isInlineLambda)
							checkWidgetCall(from = widgetKind, to = kind, reportElement)
					}
				}
				
				// Can be null in cases where the call isn't resolvable (eg. due to a bug/typo in the user's code)
				if(callKind != null && callKind != WidgetKind.none) {
					localFcs = true
					
					if(!isInlineLambda)
						checkWidgetCall(widgetKind, callKind, reportElement)
				}
			}
		}, null)
		if(localFcs && !isInlineLambda && widgetKind == WidgetKind.none) {
			// TODO: is this branch needed?
			val reportElement = when(element) {
				is KtNamedFunction -> element.nameIdentifier ?: element
				else -> element
			}
//			TODO("wow! this branch is needed")
		}
//		if(localFcs && widgetKind == widgetKind.NotWidget) // TODO
//			widgetKind =
//				widgetKind.Inferred
		return widgetKind
	}
	
	/**
	 * Analyze a KtElement
	 *  - Determine if it is @Widget or @WidgetUtil (eg. the element or inferred type has an @Widget annotation)
	 *  - Update the binding context to cache analysis results
	 *  - Report errors (eg. invocations of an widgetKind etc)
	 *  - Return the kind of the element
	 */
	fun analyze(trace: BindingTrace, element: KtElement, type: KotlinType?): WidgetKind {
		trace.bindingContext.get(WIDGET_ANALYSIS, element)?.let { return it }
		
		var widgetKind = WidgetKind.none
		
		if(element is KtClass) {
//			val descriptor = trace.bindingContext.get(BindingContext.CLASS, element)
//				?: error("Element class context not found")
//			val annotationEntry = element.annotationEntries.singleOrNull {
//				trace.bindingContext.get(BindingContext.ANNOTATION, it)?.isComposableAnnotation
//					?: false
//			}
//			if(annotationEntry != null && !ComposeUtils.isComposeComponent(descriptor)) {
//				trace.report(
//					Errors.WRONG_ANNOTATION_TARGET.on(
//						annotationEntry,
//						"class which does not extend androidx.compose.Component"
//					)
//				)
//			}
//			if(ComposeUtils.isComposeComponent(descriptor)) {
//				widgetKind += WidgetKind.Marked
//			}
		}
		if(element is KtParameter) {
			element.typeReference?.annotationEntries
				?.mapNotNull { trace.bindingContext.get(BindingContext.ANNOTATION, it) }
				?.let {
					widgetKind += it.widgetKind
				}
			
		}
		if(element is KtParameter) {
			element.typeReference?.annotationEntries
				?.mapNotNull { trace.bindingContext.get(BindingContext.ANNOTATION, it) }
				?.let {
					widgetKind = it.widgetKind
				}
		}
		
		// if (candidateDescriptor.type.arguments.size != 1 || !candidateDescriptor.type.arguments[0].type.isUnit()) return false
		if(type != null && type !== TypeUtils.NO_EXPECTED_TYPE)
			widgetKind += type.widgetKind
		
		val parent = element.parent
		val annotations = when {
			element is KtNamedFunction -> element.annotationEntries
			parent is KtAnnotatedExpression -> parent.annotationEntries
//			element is KtProperty -> element.annotationEntries
			element is KtParameter -> element.typeReference?.annotationEntries ?: emptyList()
			else -> emptyList()
		}
		
		widgetKind += annotations.mapNotNull { trace.bindingContext.get(BindingContext.ANNOTATION, it) }.widgetKind
		
		
		if(element is KtLambdaExpression || element is KtFunction) {
			val associatedCall = parent?.parent as? KtCallExpression
			
			if(associatedCall != null && parent is KtLambdaArgument) {
				val resolvedCall = associatedCall.getResolvedCall(trace.bindingContext)
//				resolvedCall?.candidateDescriptor?.widgetKind?.let { widgetKind += it } // TODO: original code checks if is ComposableEmitDescriptor
			}
			
			widgetKind = analyzeFunctionContents(trace, element, widgetKind)
		}
		
		trace.record(WIDGET_ANALYSIS, element, widgetKind)
		return widgetKind
	}
	
	override fun registerModuleComponents(
		container: StorageComponentContainer,
		platform: TargetPlatform,
		moduleDescriptor: ModuleDescriptor
	) {
		if(!platform.isJvm()) return
		container.useInstance(this)
	}
	
	override fun check(
		declaration: KtDeclaration,
		descriptor: DeclarationDescriptor,
		context: DeclarationCheckerContext
	) {
		when(descriptor) {
			is ClassDescriptor -> {
				val trace = context.trace
				val element = descriptor.findPsi()
				if(element is KtClass) {
//					val classDescriptor =
//						trace.bindingContext.get(
//							BindingContext.CLASS,
//							element
//						) ?: error("Element class context not found")
//					val composableAnnotationEntry = element.annotationEntries.mapNotNull {
//						trace.bindingContext.get(
//							BindingContext.ANNOTATION,
//							it
//						)
//					}.widgetKind
//					if(composableAnnotationEntry != null &&
//						!ComposeUtils.isComposeComponent(classDescriptor)) {
//						trace.report(
//							Errors.WRONG_ANNOTATION_TARGET.on(
//								composableAnnotationEntry,
//								"class which does not extend androidx.compose.Component"
//							)
//						)
//					}
				}
			}
			is PropertyDescriptor -> {
			}
			is LocalVariableDescriptor -> {
			}
			is TypeAliasDescriptor -> {
			}
			is FunctionDescriptor -> analyze(context.trace, descriptor)
			else -> error("currently unsupported " + descriptor.javaClass)
		}
	}
	
	override fun check(
		resolvedCall: ResolvedCall<*>,
		reportOn: PsiElement,
		context: CallCheckerContext
	) {
		val shouldBeTag = shouldInvokeAsTag(context.trace, resolvedCall)
		context.trace.record(
			FCS_RESOLVEDCALL_WIDGET,
			resolvedCall.call.callElement,
			shouldBeTag
		)
	}
	
	override fun checkType(
		expression: KtExpression,
		expressionType: KotlinType,
		expressionTypeWithSmartCast: KotlinType,
		c: ResolutionContext<*>
	) {
		if(expression is KtLambdaExpression) {
			val expectedType = c.expectedType
			if(expectedType === TypeUtils.NO_EXPECTED_TYPE) return
			val expectedKind = expectedType.widgetKind
			val widgetKind = analyze(c.trace, expression, c.expectedType)
			if(expectedKind != widgetKind) {
				TODO("differs: expected $expectedKind analyzed $widgetKind")
				val isInlineable =
					isInlinedArgument(
						expression.functionLiteral,
						c.trace.bindingContext,
						true
					)
				if(isInlineable) return

//				if(expression.parent is KtLambdaArgument) {
//					val callDescriptor = expression
//						.parent
//						?.parent
//						?.cast<KtCallExpression>()
//						?.getResolvedCall(c.trace.bindingContext)
//						?.candidateDescriptor
//
//					if(callDescriptor is ComposableEmitDescriptor) {
//						return
//					}
//				}
				
				val reportOn =
					if(expression.parent is KtAnnotatedExpression)
						expression.parent as KtExpression
					else expression
				c.trace.report(
					Errors.TYPE_MISMATCH.on(
						reportOn,
						expectedType,
						expressionTypeWithSmartCast
					)
				)
			}
			return
		} else {
			val expectedType = c.expectedType
			
			if(expectedType === TypeUtils.NO_EXPECTED_TYPE) return
			if(expectedType === TypeUtils.UNIT_EXPECTED_TYPE) return
			
			val nullableAnyType = expectedType.builtIns.nullableAnyType
			val anyType = expectedType.builtIns.anyType
			
			if(anyType == expectedType.lowerIfFlexible() &&
				nullableAnyType == expectedType.upperIfFlexible()) return
			
			val nullableNothingType = expectedType.builtIns.nullableNothingType
			
			// Handle assigning null to a nullable widget type
			if(expectedType.isMarkedNullable &&
				expressionTypeWithSmartCast == nullableNothingType) return
			
			val expectedKind = expectedType.widgetKind
			val kind = expressionType.widgetKind
			
			if(expectedKind != kind) {
				val reportOn =
					if(expression.parent is KtAnnotatedExpression)
						expression.parent as KtExpression
					else expression
				c.trace.report(
					Errors.TYPE_MISMATCH.on(
						reportOn,
						expectedType,
						expressionTypeWithSmartCast
					)
				)
			}
			return
		}
	}

//	override fun checkEntries(
//		entries: List<KtAnnotationEntry>,
//		actualTargets: List<KotlinTarget>,
//		trace: BindingTrace
//	) {
//		val entry = entries.singleOrNull {
//			trace.bindingContext.get(BindingContext.ANNOTATION, it)?.isComposableAnnotation
//				?: false
//		}
//		if((entry?.parent as? KtAnnotatedExpression)?.baseExpression is
//				KtObjectLiteralExpression) {
//			trace.report(
//				Errors.WRONG_ANNOTATION_TARGET.on(
//					entry,
//					"class which does not extend androidx.compose.Component"
//				)
//			)
//		}
//	}
	
	operator fun WidgetKind.plus(rhs: WidgetKind): WidgetKind =
		if(this > rhs) this else rhs
}
