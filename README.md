# [WIP] kt-ui
> In fact, I'm writing this for fun(reinventing the wheel is always exciting!)  
> I acknowledge that lots of codes came from [Jetpack compose](https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-master-dev/compose/) (mostly the compiler plugin).  
> This project can be a great example for beginners to kotlin compiler plugin(IR backend) and contains some useful tools(irExpressions, colorful ir dumper, ir source printer which prints ir into almost kotlin code as-is(with highlighting))
>
> Disclaimer: This is not for use (so far) as it is poorly written

`kt-ui` is a declarative multiplatform UI framework for Kotlin.

```kotlin
// currently just pseudo code

@Widget
fun Main() {
    Text("Hello, world!")
}
```
