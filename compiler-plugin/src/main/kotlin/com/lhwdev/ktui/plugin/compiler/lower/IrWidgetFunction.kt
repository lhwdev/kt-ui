package com.lhwdev.ktui.plugin.compiler.lower

import com.lhwdev.ktui.plugin.compiler.UiLibraryFqNames
import com.lhwdev.ktui.plugin.compiler.dumpSrcHeadColored
import com.lhwdev.ktui.plugin.compiler.util.*
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.descriptors.WrappedSimpleFunctionDescriptor
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.hasAnnotation


typealias IrWidgetFunctionSymbol = IrComponentSymbol.SimpleFunction


class IrWidgetFunction private constructor(
	original: IrSimpleFunction,
	override val symbol: IrWidgetFunctionSymbol,
	var originalValueParameterCount: Int
) : IrComponentSymbolDeclaration<IrSimpleFunction, IrSimpleFunctionSymbol>, IrSimpleFunctionWrapper(original) {
	
	override var valueParameters: List<IrValueParameter>
		get() = super.valueParameters
		set(value) {
			if(value.size != super.valueParameters.size)
				error("IrWidgetFunction do not support adding/removing valueParameters")
			super.valueParameters = value
			validateValueParameters()
		}
	
	val originalValueParameters: List<IrValueParameter>
		get() = valueParameters.subList(0, originalValueParameterCount)
	val buildScopeParameter: IrValueParameter
		get() = valueParameters[originalValueParameterCount]
	val changedParameters: List<IrValueParameter>
		get() = valueParameters.subList(originalValueParameterCount + 1, valueParameters.size)
	val changed by cacheBy(by = { changedParameters }) { IrBitDataSetMapping(changedParameters, width = 2) }
	
	
	init {
		require(isWidget(original))
		symbol.bindComponent(this)
		validateValueParameters()
	}
	
	
	private fun validateValueParameters() {
		check(buildScopeParameter.type == UiLibrary.buildScopeType) {
			"buildScopeParameter is not BuildScope: ${buildScopeParameter.dumpSrcHeadColored()}"
		}
		changedParameters.forEachIndexed { i, param ->
			check(param.type == irBuiltIns.intType) { "changedParameters[$i] is not Int: ${param.dumpSrcHeadColored()}" }
		}
	}
	
	@OptIn(ObsoleteDescriptorBasedAPI::class)
	override fun remapSymbol(symbolsRemapper: DeepCopyComponentSymbolRemapper) {
		symbolsRemapper.remapSymbol(symbolsRemapper.functions, this) {
			val original = IrSimpleFunctionSymbolImpl(
				symbolsRemapper.descriptorsRemapper.remapDeclaredSimpleFunction(it.descriptor)
			)
			
			IrComponentSymbol.SimpleFunction(original)
		}
	}
	
	override fun deepCopyWithSymbols(
		transformer: IrDeepCopyComponentTransformer
	): IrWidgetFunction {
		transformChildren(transformer, null)
		return IrWidgetFunction(
			this,
			transformer.symbolRemapper.getDeclaredFunction(symbol) as IrWidgetFunctionSymbol,
			originalValueParameterCount
		)
	}
	
	override fun IrComponentBuildScope.doBuild(): IrSimpleFunction {
		validateValueParameters()
		val descriptor = WrappedSimpleFunctionDescriptor()
		val newSymbol = IrSimpleFunctionSymbolImpl(descriptor)
		val result = IrSimpleFunctionWrapperImpl(this@IrWidgetFunction, newSymbol)
		descriptor.bind(result)
		
		componentSymbolRemapper.addRemappedSymbol(componentSymbolRemapper.functions, result, newSymbol)
		
		return result
	}
	
	companion object {
		fun isWidget(function: IrSimpleFunction): Boolean = function.hasAnnotation(UiLibraryFqNames.Widget)
		
		fun transform(original: IrSimpleFunction): IrWidgetFunction {
			val originalValueParameters = original.valueParameters
			val descriptor = WrappedSimpleFunctionDescriptor()
			val symbol = IrComponentSymbol.SimpleFunction(IrSimpleFunctionSymbolImpl(descriptor))
			val newFunction = original.copyShallow(symbol)
			descriptor.bind(newFunction)
			
			newFunction.addValueParameter("\$buildScope", UiLibrary.buildScope.defaultType)
			for(i in 0 until changedParameterCount(originalValueParameters.size)) {
				newFunction.addValueParameter("\$changed${i + 1}", irBuiltIns.intType)
			}
			return IrWidgetFunction(
				newFunction, symbol = symbol, originalValueParameterCount = originalValueParameters.size
			)
		}
		
		fun fromCache(function: IrSimpleFunction): IrWidgetFunction? {
			val existingComponent = function.symbol.component
			if(existingComponent is IrWidgetFunction) return existingComponent
			
			return null
		}
		
		// when creating from external function symbol
		fun fromTransformed(function: IrSimpleFunction): IrWidgetFunction? {
			// fast path without creating
			fromCache(function)?.let { return it }
			
			if(!isWidget(function)) return null
			
			// TODO: is parameter name reliable? (compose uses it)
			// detect last parameter with BuildScope type
			val buildScopeParamIndex =
				function.valueParameters.indexOfLast { it.type == UiLibrary.buildScope.defaultType }
			check(buildScopeParamIndex != -1)
			
			return IrWidgetFunction(
				function,
				symbol = function.symbol as? IrComponentSymbol.SimpleFunction ?: IrComponentSymbol.SimpleFunction(
					function.symbol
				),
				originalValueParameterCount = buildScopeParamIndex
			)
		}
	}
}
