# Widget Compiler Plugin v4


## Versions & Milestones
* v1  
  - The initial version. Didn't consider the return value.
  
* v2  
  - All widgets are skippable:
    
    - Widget without return value
      ```kotlin
      @Widget
      fun Hi(a: String) {
          MyWidget(123, a)
      }
      ```
      into
      ```kotlin
      @Widget
      fun Hi(a: String, `$buildScope`: BuildScope, `$idState`: Long) {
          val changed = `$buildScope`.start(`$idState`, arrayOf(a))
          if(changed != 0) {
              MyWidget(
                  123, a, `$buildScope`,
                  137 /* id */ or ((
                      /* 1st */(0b10 /* unchanged */ shl 0) or
                      /* 2nd */(0b10 or (changed and 0b1 shr 0 /* delegate state from a */) shl 1)
                  ) shl 32)
              )
          }
          end()
      }
      ```
    
    - Widget with return value
      ```kotlin
      @Widget
      fun HelloWorld() = 3
      ```
      into
      ```kotlin
      @Widget
      fun HelloWorld(`$buildScope`: BuildScope, `$idState`: Long) {
          val changed = `$buildScope`.start(`$idState`, arrayOf())
          return if(changed != 0) run {
              3
          }.also { `$buildScope`.end(it) } else `$buildScope`.endSkip()
      }
      ```
    > This compilation behavior may leak the return value and cause unexpected side effects.
      (fixed in v3)
  
  - Added special library functions: `currentScope()`, `bridgeCall(widget)`.
  
  - Added `@InlineWidget`
    
    Compilation of this was not complete, though.
  
  
  
* v3
  - Added special library function `nextId()`: treated specially
  - divide widgets into skippable ones and else:
    
    Widgets with return value(not `Unit`) are not skippable
    
    - Widget without return value
      ```kotlin
         @Widget
         fun Hi(a: String) {
             MyWidget(123, a)
         }
      ```
      into
      ```kotlin
         @Widget
         fun Hi(a: String, `$buildScope`: BuildScope, `$idState`: Long) {
             // the same: skippable
         }
       ```
    
    - Widget with return value
      ```kotlin
         @Widget
         fun HelloWorld() = 3
      ```
      into
      ```kotlin
         @Widget
         fun HelloWorld(`$buildScope`: BuildScope, `$id`: Long /* just for compatibility reason: 32 bits are discarded */) {
             `$buildScope`.startExpr(`$id`.toInt())
             val returnValue = run { // not skippable
                 3
             }
             `$buildScope`.endExpr()
             return returnValue
         }
      ```
  
  - The output of `@Widget inline fun ...` is more simplified:
    inline widget fun signifies that it has @InlineWidget
    (actually there is no actual annotation: just meaning behavior)
  
* v4 (not implemented yet)
	- flat slot table: Currently building depends on `children` of `Element`.
	- make `Element` more private api: Implementation of flat slot table will require this.
	- optimize branches: for example, in `if(...) Widget1() else Widget2()`, if the condition changes, locating the
	  element may be more expensive.

	  Solution: group them as 'replaceable group'
	- support referencing widget function like: `::MyWidget`
	- do not create Element instance for normal functional widgets
	- remove `$id` parameter: put in `$buildScope.startExpr(..)`

plus: Yah! Backend IR became beta stage!  
https://blog.jetbrains.com/kotlin/2021/02/the-jvm-backend-is-in-beta-let-s-make-it-stable-together/
