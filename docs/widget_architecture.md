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
  
  
  Currently, kt-ui uses `Kotlin 1.4-M1` due to compiler implementation.
  Until Kotlin 1.4 is released and kt-ui 1.0.0 is released, kt-ui will stay experimental.
