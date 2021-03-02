package com.lhwdev.ktui.plugin.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContextImpl
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.util.*
import java.lang.reflect.Field
import java.util.Arrays
import java.util.Collections
import java.util.IdentityHashMap
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime


/*
* @Widget
* fun Main(num: Int) {}
*
* into (compilation phase - after the semantic analysis phase)
*
* fun Main($scope: BuildScope, num: Int) {}
*
*
* Main(3)
*
* into
*
* val _temp_num = 3
* call(13924, _temp_num) { // 13924: unique location hash
*     Main(_temp_num)
* }
*/


lateinit var symbolTable: DelegatingSymbolTable
	private set


private val symbolTableFields = SymbolTable::class.java.declaredFields.toList().onEach { it.isAccessible = true }

fun setSymbolTable(table: SymbolTable) {
	symbolTableFields.forEach { field ->
		field.set(symbolTable, field.get(table))
	}
	symbolTable.original = table
}

fun setSymbolTable(table: ReferenceSymbolTable) {
	symbolTable.original = table
}

class DelegatingSymbolTable(signaturer: IdSignatureComposer, var original: ReferenceSymbolTable) :
	SymbolTable(signaturer) {
	override fun enterScope(owner: DeclarationDescriptor) {
		original.enterScope(owner)
	}
	
	override fun leaveScope(owner: DeclarationDescriptor) {
		original.leaveScope(owner)
	}
	
	override fun referenceClass(descriptor: ClassDescriptor): IrClassSymbol {
		return original.referenceClass(descriptor)
	}
	
	override fun referenceClassFromLinker(descriptor: ClassDescriptor, sig: IdSignature): IrClassSymbol {
		return original.referenceClassFromLinker(descriptor, sig)
	}
	
	override fun referenceConstructor(descriptor: ClassConstructorDescriptor): IrConstructorSymbol {
		return original.referenceConstructor(descriptor)
	}
	
	override fun referenceConstructorFromLinker(
		descriptor: ClassConstructorDescriptor,
		sig: IdSignature
	): IrConstructorSymbol {
		return original.referenceConstructorFromLinker(descriptor, sig)
	}
	
	override fun referenceDeclaredFunction(descriptor: FunctionDescriptor): IrSimpleFunctionSymbol {
		return original.referenceDeclaredFunction(descriptor)
	}
	
	override fun referenceEnumEntry(descriptor: ClassDescriptor): IrEnumEntrySymbol {
		return original.referenceEnumEntry(descriptor)
	}
	
	override fun referenceEnumEntryFromLinker(descriptor: ClassDescriptor, sig: IdSignature): IrEnumEntrySymbol {
		return original.referenceEnumEntryFromLinker(descriptor, sig)
	}
	
	override fun referenceField(descriptor: PropertyDescriptor): IrFieldSymbol {
		return original.referenceField(descriptor)
	}
	
	override fun referenceFieldFromLinker(descriptor: PropertyDescriptor, sig: IdSignature): IrFieldSymbol {
		return original.referenceFieldFromLinker(descriptor, sig)
	}
	
	override fun referenceProperty(descriptor: PropertyDescriptor, generate: () -> IrProperty): IrProperty {
		return original.referenceProperty(descriptor, generate)
	}
	
	override fun referencePropertyFromLinker(descriptor: PropertyDescriptor, sig: IdSignature): IrPropertySymbol {
		return original.referencePropertyFromLinker(descriptor, sig)
	}
	
	override fun referenceSimpleFunction(descriptor: FunctionDescriptor): IrSimpleFunctionSymbol {
		return original.referenceSimpleFunction(descriptor)
	}
	
	override fun referenceSimpleFunctionFromLinker(
		descriptor: FunctionDescriptor,
		sig: IdSignature
	): IrSimpleFunctionSymbol {
		return original.referenceSimpleFunctionFromLinker(descriptor, sig)
	}
	
	override fun referenceTypeAlias(descriptor: TypeAliasDescriptor): IrTypeAliasSymbol {
		return original.referenceTypeAlias(descriptor)
	}
	
	override fun referenceTypeAliasFromLinker(descriptor: TypeAliasDescriptor, sig: IdSignature): IrTypeAliasSymbol {
		return original.referenceTypeAliasFromLinker(descriptor, sig)
	}
	
	override fun referenceTypeParameter(classifier: TypeParameterDescriptor): IrTypeParameterSymbol {
		return original.referenceTypeParameter(classifier)
	}
	
	override fun referenceTypeParameterFromLinker(
		classifier: TypeParameterDescriptor,
		sig: IdSignature
	): IrTypeParameterSymbol {
		return original.referenceTypeParameterFromLinker(classifier, sig)
	}
	
	override fun referenceValueParameter(descriptor: ParameterDescriptor): IrValueParameterSymbol {
		return original.referenceValueParameter(descriptor)
	}
	
	override fun referenceVariable(descriptor: VariableDescriptor): IrVariableSymbol {
		return original.referenceVariable(descriptor)
	}
}


class UiIrGenerationExtension : IrGenerationExtension {
	@OptIn(ExperimentalTime::class)
	override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
		pluginContext as IrPluginContextImpl
		@Suppress("DEPRECATION")
		val oldSymbolTable = pluginContext.symbolTable as SymbolTable
		symbolTable = DelegatingSymbolTable(oldSymbolTable.signaturer, oldSymbolTable)
		setSymbolTable(oldSymbolTable)
		
		val typeTranslator = TypeTranslator(symbolTable, pluginContext.languageVersionSettings, pluginContext.builtIns)
		
		@Suppress("DEPRECATION")
		val constantValueGenerator = ConstantValueGenerator(pluginContext.moduleDescriptor, symbolTable)
		typeTranslator.constantValueGenerator = constantValueGenerator
		constantValueGenerator.typeTranslator = typeTranslator
		
		@Suppress("NAME_SHADOWING", "DEPRECATION")
		val pluginContext = IrPluginContextImpl(
			pluginContext.moduleDescriptor,
			pluginContext.bindingContext,
			pluginContext.languageVersionSettings,
			symbolTable,
//			AutoResolveUnboundSymbolTable(pluginContext.symbolTable),
			typeTranslator,
			pluginContext.irBuiltIns,
			pluginContext.linker,
			pluginContext.symbols
		)
		
		logColor("Hello from UiIrGenerationExtension", ConsoleColors.GREEN)
		log("")
		
		UiIrContext(pluginContext, moduleFragment, moduleFragment).transformations("main") {
			initUnboundSymbolUtils(pluginContext)
			
			measureTimeMillis {
				bindAll()
			}.withLog { "bind took $it" }

//			log2(target.dumpSrc())
//		val target = moduleFragment.files.withLog { it.joinToString { files -> files.name } }.find { it.name == "Main.kt" }!!
//			target.deepCopyWithSymbols().logDumpColored()
			
			+WidgetTypeTransformer()
			+WidgetMarkerResolver()
			log2(uiTrace.getKeys(UiWritableSlices.IS_INLINE_FUNCTION).joinToString { it.descriptor.name.toString() })
			+WidgetFunctionParamTransformer()
			+WidgetCallTransformer()
			
			uiIrPhase("optimizations") {
//				+WidgetPureStateMarker()
			}
			target.patchDeclarationParents()
			val nT = target.deepCopyWithSymbols()
			target.logSrcColored(SourcePrintConfig.debug) // because the exception 'Unbound type parameters are forbidden'
			compare(nT, target)
//
//			+WidgetFunctionBodyTransformer()
////			target.logSrcColored(debug = true)
//			+UiIntrinsicsTransformer()
//			log("\nDUMP OVERALL TRANSFORMED IR TREE")
//			// this causes compilation to fail(I don't know why dump causes it but removing it worked;
//			// dump() in kotlin compiler library has the same error); so copy it then dump
////			target.deepCopyWithSymbols().logDumpColored()
//			log("")
//			target.logSrcColored(SourcePrintConfig.debug)
			bindAll()
		}
	}
}


private val sCompareTarget = listOf<Class<*>>()

fun compare(
	a: Any?,
	b: Any?,
	description: String? = null,
	compared: MutableSet<Any?> = Collections.newSetFromMap(IdentityHashMap(1024))
): Boolean {
	fun join(next: String, sep: String = ".") = if(description == null) next else "$description$sep$next"
	fun str(obj: Any?) = when(obj) {
		is IrElement -> obj.dumpSrcHeadColored(SourcePrintConfig.default.copy(allowNewLine = false))
		else -> obj.toString()
	}

//	log(description)
	if(a === b) return true
	val aC = a in compared
	val bC = b in compared
	
	val isEqual = if(a === null) false else {
		val clazz = a::class.java
		if(!clazz.isPrimitive)
			compared += a
		if(b !== null && !b::class.java.isPrimitive)
			compared += b
		
		when {
			clazz != b!!::class.java -> false
			aC || bC -> a == b
			clazz.isPrimitive -> a == b
			clazz.isArray -> {
				when(clazz.componentType) {
					Int::class.java -> Arrays.equals(a as IntArray, b as IntArray)
					Byte::class.java -> Arrays.equals(a as ByteArray, b as ByteArray)
					Short::class.java -> Arrays.equals(a as ShortArray, b as ShortArray)
					Long::class.java -> Arrays.equals(a as LongArray, b as LongArray)
					Float::class.java -> Arrays.equals(a as FloatArray, b as FloatArray)
					Double::class.java -> Arrays.equals(a as DoubleArray, b as DoubleArray)
					Boolean::class.java -> Arrays.equals(a as BooleanArray, b as BooleanArray)
					Char::class.java -> Arrays.equals(a as CharArray, b as CharArray)
					else -> {
						a as Array<*>
						b as Array<*>
						a.size == b.size && a.foldIndexed(true) { i, acc, el ->
							acc and compare(
								el,
								b[i],
								join("[${ConsoleColors.RED}$i${ConsoleColors.RESET}]", ""),
								compared
							)
						}
					}
				}
				true
			}
			a is List<*> -> {
				b as List<*>
				a.size == b.size && a.indices.fold(true) { acc, i ->
					acc and compare(
						a[i],
						b[i],
						join("[${ConsoleColors.RED}$i${ConsoleColors.RESET}]", ""),
						compared
					)
				}
				true
			}
			a is IrSymbol -> a.descriptor.toString() == (b as IrSymbol).descriptor.toString()
			a !is IrElement && sCompareTarget.none { it.isAssignableFrom(clazz) } -> true
			else -> {
				clazz.allDeclaredFields.fold(true) { acc, field ->
					field.isAccessible = true
					if("parent" in field.name) true // just temporary
					else acc and compare(
						field.get(a),
						field.get(b),
						join(field.name + ": " + ConsoleColors.GREEN + field.type.simpleName + ConsoleColors.RESET),
						compared
					)
				}
				true
			}
		}
	}
	if(!isEqual) log("$description(${str(a)} | ${str(b)})")
	return isEqual
}

val Class<*>.allDeclaredFields: Array<Field>
	get() = superclass?.let { it.allDeclaredFields + declaredFields } ?: declaredFields
