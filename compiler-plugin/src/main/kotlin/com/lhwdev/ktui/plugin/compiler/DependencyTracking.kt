package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.builtins.isFunctionType
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.descriptors.IrBuiltinOperatorDescriptor
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.symbols.IrValueSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classifierOrNull
import org.jetbrains.kotlin.ir.util.kotlinPackageFqn
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import java.util.Stack


private val sStatelessStdlibCalls = arrayOf(
	// has 1 argument (though it can be vararg)
	"arrayOf", "listOf", "setOf",
	"byteArrayOf", "shortArrayOf", "intArrayOf", "longArrayOf", "floatArrayOf", "doubleArrayOf", "booleanArrayOf", "charArrayOf",
)

private val sStatelessStdlibFunctionalCalls = arrayOf(
	"run", "let", "with", "apply", "also"
)

private val sStatelessKotlinClasses = arrayOf("Number", "Byte", "Short", "Int", "Long", "Float", "Double", "Boolean", "Char", "String")

val IrType.isStable get() = true // TODO

val IrType.isFunctionType get() = isClassWithNamePrefix("Function", kotlinPackageFqn)

private fun IrType.isClassWithNamePrefix(name: String, packageFqName: FqName) =
	classifierOrNull?.descriptor?.fqNameSafe?.let {
		it.parent() == packageFqName && it.shortName().asString() == name
	}


fun IrElement.dependType() = accept(WidgetStaticObserver(), null)

fun IrElement.isStatic() = dependType() == DependType.static

enum class DependType { static, stateless, none }

infix fun DependType.and(other: DependType) = DependType.values()[maxOf(ordinal, other.ordinal)]
infix fun DependType.or(other: DependType) = DependType.values()[minOf(ordinal, other.ordinal)]


// WOW! such TODO! very bug!
class WidgetStaticObserver : IrElementVisitor<DependType, Nothing?> {
	private val functionScope = Stack<IrFunction>()
	val dependencies = mutableListOf<IrSymbol>()
	var stateless = true
	
	private fun IrElement.dependType() = accept(this@WidgetStaticObserver, null)
	
	fun dependsOn(target: IrSymbol): DependType {
		dependencies += target
		return DependType.stateless
	}
	
	override fun visitFunction(declaration: IrFunction, data: Nothing?) = try {
		functionScope.push(declaration)
		super.visitFunction(declaration, data)
	} finally {
		functionScope.pop()
	}
	
	override fun visitElement(element: IrElement, data: Nothing?) = DependType.none
	
	override fun <T> visitConst(expression: IrConst<T>, data: Nothing?) = DependType.static
	
	override fun visitGetValue(expression: IrGetValue, data: Nothing?) =
		with(expression.symbol.ownerOrNull) {
			when(this) {
				is IrVariable -> when {
					isConst -> DependType.static
					isVar -> DependType.none
					(initializer as? IrCall)?.let { // remember { .. } (without key) is always persistent during the lifecycle
						it.symbol.descriptor.fqNameSafe == UiLibrary.REMEMBER &&
							it.valueArgumentsCount == 1 // TODO: remembered value can be changed: only the type is stable then this is available
					} == true -> DependType.static
					else -> initializer?.dependType() ?: DependType.none // none?
				}
				is IrValueParameter -> when { // @Key param: Type is always persistent
					annotations.any { it.symbol.descriptor == UiLibraryDescriptors.key } -> DependType.static
					// since writeable valueParameter deprecated, don't need to check it (and if it is available, this plugin may be broken)
					else -> dependsOn(expression.symbol) // depends on the parameter
				}
				null -> DependType.none
				else -> error("unknown IrValueDeclaration $this")
			}
		}
	
	override fun visitGetObjectValue(expression: IrGetObjectValue, data: Nothing?) =
		DependType.static
	
	override fun visitGetEnumValue(expression: IrGetEnumValue, data: Nothing?) = DependType.static
	
	override fun visitGetField(expression: IrGetField, data: Nothing?) = with(expression) {
		if((symbol.ownerOrNull?.isFinal == true) && receiver?.isStatic() != false) DependType.static else DependType.none
	}
	
	override fun visitFunctionReference(expression: IrFunctionReference, data: Nothing?) =
		DependType.static
	
	override fun visitClassReference(expression: IrClassReference, data: Nothing?) =
		DependType.static
	
	override fun visitBlock(expression: IrBlock, data: Nothing?) =
		expression.statements.fold(DependType.static) { acc, statement -> acc and statement.dependType() }
	
	override fun visitCall(
		expression: IrCall, data: Nothing?
	) = with(expression.symbol.descriptor) {
		val allArgType by lazy {
			(expression.dispatchReceiver?.dependType() ?: DependType.static) and
				(expression.extensionReceiver?.dependType() ?: DependType.static) and
				expression.valueArguments.fold(DependType.static) { acc, argument ->
					acc and (argument?.dependType() ?: DependType.static)
				}
		}
		
		when {
			// builtin operators
			this is IrBuiltinOperatorDescriptor -> allArgType // all builtin operators are stateless
			
			// operators
			isOperator -> (if(dispatchReceiverParameter?.let {
					val fqName = it.containingDeclaration.fqNameSafe
					fqName.parent() == kotlinPackageFqn && fqName.shortName().asString() in sStatelessKotlinClasses
				} == true) DependType.stateless else DependType.none) and allArgType
			
			// property
			(expression.symbol.ownerOrNull as? IrSimpleFunction)?.correspondingPropertySymbol?.let {
				val prop = it.ownerOrNull
				when {
					prop == null -> false
					prop.isDelegated -> false
					prop.backingField.let { backing ->
						backing != null && backing.isFinal && backing.type.isStable
					} && prop.getter?.let { getter ->
						getter.origin == IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR ||
							getter.dependsOnlyOn(listOfNotNull(getter.dispatchReceiverParameter?.symbol, getter.extensionReceiverParameter?.symbol))
					} ?: true -> true
					prop.getter?.isStatic() ?: false -> true
					else -> false
				}
			} ?: false -> allArgType
			
			fqNameSafe.parent() == kotlinPackageFqn && (
				valueParameters.size == 1 && name.asString() in sStatelessStdlibCalls ||
					valueParameters.last().type.isFunctionType && name.asString() in sStatelessStdlibFunctionalCalls
				) -> (expression.extensionReceiver
				?: expression.valueArguments.singleOrNull())!!.dependType()
			
			else -> DependType.none
		}
	}
}


fun IrElement.dependsOnlyOn(allowedDependencies: List<IrValueSymbol>) =
	DependencyTracker().let { visitor ->
		accept(visitor, null)
		visitor.dependencies.all { it in allowedDependencies }
	}

class DependencyTracker : IrElementVisitorRecursive() {
	val dependencies = mutableSetOf<IrValueSymbol>()
	
	override fun visitGetValue(expression: IrGetValue) {
		dependencies += expression.symbol
		super.visitGetValue(expression)
	}
}
