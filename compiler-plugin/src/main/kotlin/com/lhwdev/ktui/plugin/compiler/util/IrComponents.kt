package com.lhwdev.ktui.plugin.compiler.util

import com.lhwdev.ktui.plugin.compiler.lower.checkNotNull
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.typeOrNull
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor


class IrInvoke(
	override val startOffset: Int,
	override val endOffset: Int,
	var functionLiteralType: IrFunctionLiteralType,
	valueArgumentsCount: Int,
	override var type: IrType,
	override var origin: IrStatementOrigin = IrStatementOrigin.LAMBDA,
	override var superQualifierSymbol: IrClassSymbol? = null
) : IrComponentExpression<IrCall>, IrCall(0, valueArgumentsCount) {
	constructor(
		startOffset: Int = UNDEFINED_OFFSET,
		endOffset: Int = UNDEFINED_OFFSET,
		receiver: IrExpression,
		functionLiteralType: IrFunctionLiteralType = kotlin.run {
			val arguments = (receiver.type as IrSimpleType).arguments.map { it.typeOrNull ?: irBuiltIns.anyType }
			IrFunctionLiteralType(arguments.dropLast(1), arguments.last())
		},
		valueArguments: List<IrExpression> = emptyList(),
		type: IrType = functionLiteralType.returnType,
		origin: IrStatementOrigin = IrStatementOrigin.LAMBDA,
		superQualifierSymbol: IrClassSymbol? = null
	) : this(
		startOffset, endOffset,
		functionLiteralType = functionLiteralType,
		valueArgumentsCount = valueArguments.size,
		type = type,
		origin = origin,
		superQualifierSymbol = superQualifierSymbol
	) {
		dispatchReceiver = receiver
		valueArguments.forEachIndexed { index, argument -> putValueArgument(index, argument) }
	}
	
	override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R = handleAccept(visitor, data) {
		visitor.visitCall(this, data)
	}
	
	override val symbol: IrSimpleFunctionSymbol get() = functionLiteralType.functionSymbol
	
	
	override fun deepCopyWithSymbols(transformer: IrDeepCopyComponentTransformer): IrInvoke = IrInvoke(
		startOffset, endOffset, functionLiteralType, typeArgumentsCount, type, origin, superQualifierSymbol
	)
	
	override fun IrComponentBuildScope.doBuild(): IrCall = IrCallImpl(
		startOffset, endOffset,
		type = type, symbol = symbol,
		typeArgumentsCount = 0, valueArgumentsCount = valueArgumentsCount,
		origin = origin, superQualifierSymbol = superQualifierSymbol
	).also {
		check(dispatchReceiver != null) { "function literal invocation must contain dispatchReceiver(= function literal instance)" }
		check(extensionReceiver == null) { "function literal invocation cannot contain extensionReceiver" }
		
		it.dispatchReceiver = dispatchReceiver
		for((index, argument) in valueArguments.withIndex())
			it.putValueArgument(
				index,
				argument ?: error("function literal invocation cannot contain default argument(null)")
			)
	}
	
	
	companion object {
		fun detect(call: IrCall): IrInvoke? = when {
			call.dispatchReceiver == null -> null
			call.extensionReceiver != null -> null
			call.valueArguments.any { it == null } -> null
			else -> IrInvoke(
				call.startOffset, call.endOffset,
				receiver = call.dispatchReceiver!!,
				valueArguments = call.valueArguments.checkNotNull(),
				type = call.type,
				origin = call.origin ?: IrStatementOrigin.LAMBDA,
				superQualifierSymbol = call.superQualifierSymbol
			)
		}
	}
}
