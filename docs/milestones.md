# Kt-ui Milestones
## Api Design
- Declarative, Functional
  ```kotlin
  @Widget
  fun MyWidget(name: String) {
      Text("Hello, $name!")
  }
  ```

- Data flows only from outer to inner
  ```kotlin
  @Widget
  fun MyWidget(name: String) {
      // ...
  }
  
  MyWidget("Steve")
  ```

## Accessibility
- Full accessibility features support


## Performance
- Of course, should be very fast(??)
