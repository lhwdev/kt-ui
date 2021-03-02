# Widget Architecture
## `@Widget` annotation
`@Widget` can be used to mark a function / function literal / functional type as a widget.
- on functions:  
  ```kotlin
  @Widget
  fun Main() {
      val myState by state { 3 }
      MyWidget(myState)
      
      onClick {
          myState++
      }
  }
  ```
- on function literals:
  ```kotlin
  val widget: @Widget () -> Unit = { MyWidget(12345) }
  ```
  
