package klaymore

import kotlin.reflect.KClass

class BindingsBuilder internal constructor() {
    internal val bindings = mutableMapOf<KClass<*>, Function<*>>()

    class Binder<T : Any>(
        private val bindingsBuilder: BindingsBuilder,
        private val bindingKind: BindingKind,
        private val klass: KClass<T>
    ) {
        fun withFunction(
            paramKlasses: List<KClass<*>>,
            call: (List<Any>) -> T
        ) {
            bindingsBuilder.bindings[klass] = Function(bindingKind, paramKlasses, call)
        }
    }

    inline fun <reified T : Any> bind(bindingKind: BindingKind): Binder<T> {
        return Binder(this, bindingKind, T::class)
    }
}
