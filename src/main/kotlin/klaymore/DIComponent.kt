package klaymore

import kotlin.reflect.KClass

/**
 * Interface for dependency injection component.
 * How to use:
 *  1. Create module with bindings.
 *      Example:
 *      ```
 *      val module = module {
 *          bind<SomeInterface>(Singleton) with ::SomeClass
 *          bind<SomeOtherInterface>(Factory) with ::SomeOtherClass
 *      }
 *      ```
 *      Provide some value by value class:
 *      ```
 *      value class Ten(val value: Int)
 *
 *      val valuesModule = module {
 *          bind<Ten>(Singleton) { -> Ten(10) }
 *      }
 * 2. Create component with module.
 *      Example:
 *      ```
 *      val component = Component(module)
 *      ```
 * 3. Get dependency from component.
 *      Example:
 *      ```
 *      withDI(component) {
 *          val someInterface: SomeInterface = provide()
 *          ...
 *      }
 *      // or provide property
 *      class Target(override val component: DIComponent): DIComponentHolder {
 *          val someInterface by provider()
 *      }
 *      ```
 * 4. Create subcomponent with attached module.
 *      Example:
 *      ```
 *      object ChildScope : Scope
 *      val subcomponent = component.createSubcomponent(ChildScope) {
 *          bind<ChildOtherInterface>(ChildScope) with ::ChildOtherClass
 *      }
 *      ```
 * 5. Get dependency from subcomponent.
 *      Example:
 *      ```
 *      withDI(subcomponent) {
 *          val childOtherInterface: ChildOtherInterface = provide()
 *      }
 *      ```
 */

interface DIComponent {

    /**
     * Get dependency provider for type.
     */
    fun <T : Any> getProvider(klass: KClass<T>): Provider<T>

    /**
     * Create subcomponent with attached module.
     */
    fun createSubcomponent(scope: Scope, module: Module): DIComponent
}
