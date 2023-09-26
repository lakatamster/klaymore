# Klaymore: A Lightweight Dependency Injection Framework

Klaymore is a lightweight Dependency Injection (DI) framework for Kotlin applications. It simplifies the management of dependencies in your application, making it easier to create, maintain, and test your code.

[![Release](https://jitpack.io/v/lakatamster/klaymore.svg)](https://jitpack.io/#lakatamster/klaymore)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Features

- **DSL-based Configuration:** Configure your dependencies using modules and bindings, making it easy to wire up your classes.

- **Singletons, Custom Scopes and Factories:** Control the lifecycle of your objects by specifying whether they should be singletons, scoped or created fresh every time.

- **Value Classes:** Easily provide values using Kotlin value classes.

- **Subcomponents:** Create subcomponents with attached modules for more fine-grained control over your dependencies.

## Getting Started

### Installation

https://jitpack.io/#lakatamster/klaymore

### Usage

1. **Create Modules with Bindings:**

   Define your dependencies by creating modules with bindings. For example:

   ```kotlin
   val module = module {
       bind<SomeInterface>(Singleton) with ::SomeClass
       bind<SomeOtherInterface>(Factory) with ::SomeOtherClass
   }
   ```

   You can also provide values using Kotlin value classes:

   ```kotlin
   value class Ten(val value: Int)

   val valuesModule = module {
       bind<Ten>(Singleton) { -> Ten(10) }
   }
   ```

2. **Create Component with Modules:**

   Create a component by passing your modules:

   ```kotlin
   val component = Component {
       module()
       valuesModule()
   }
   ```

3. **Get Dependencies from the Component:**

   Obtain dependencies from your component using `withDI`:

   ```kotlin
   withDI(component) {
       val someInterface = component.provide<SomeInterface>()
       // ...
   }
   ```

   Alternatively, you can use property delegation:

   ```kotlin
   class Target(override val component: DIComponent) : DIComponentHolder {
       val someInterface by provider()
   }
   ```

4. **Create Subcomponents with Modules:**

   Define subcomponents with attached modules for more specific scopes:

   ```kotlin
   object ChildScope : Scope
   val subcomponent = component.createSubcomponent(ChildScope) {
       bind<ChildOtherInterface>(ChildScope) with ::ChildOtherClass
   }
   ```

5. **Get Dependencies from Subcomponents:**

   Fetch dependencies from subcomponents just like with components:

   ```kotlin
   withDI(subcomponent) {
       val childOtherInterface = subcomponent.provide<ChildOtherInterface>()
   }
   ```

## Documentation

For detailed information on how to use Klaymore and its advanced features, please refer to the [Documentation](https://github.com/lakatamster/klaymore/wiki).

## Contact

If you have any questions or need support, feel free to open an issue on the [GitHub repository](https://github.com/lakatamster/klaymore/issues).
```
