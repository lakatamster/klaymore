package klaymore

import kotlin.properties.ReadOnlyProperty

fun <T> withDI(component: DIComponent, block: DIComponentHolder.() -> T): T =
    object : DIComponentHolder {
        override val component: DIComponent = component
    }.block()

inline fun <reified T : Any> DIComponentHolder.getProvider(): Provider<T> =
    component.getProvider(T::class)

inline fun <reified T : Any> DIComponentHolder.provide(): T =
    getProvider<T>().get()

inline fun <reified T : Any> DIComponentHolder.provider(): ReadOnlyProperty<Any, T> =
    ReadOnlyProperty { _, _ -> provide() }

inline fun <reified T : Any,
    reified P1 : Any> DIComponentHolder.call(
    function: (P1) -> T
): T =
    function(provide())

inline fun <reified T : Any,
    reified P1 : Any,
    reified P2 : Any> DIComponentHolder.call(
    function: (P1, P2) -> T
): T =
    function(provide(), provide())

inline fun <reified T : Any,
    reified P1 : Any,
    reified P2 : Any,
    reified P3 : Any> DIComponentHolder.call(
    function: (P1, P2, P3) -> T
): T =
    function(provide(), provide(), provide())

inline fun <reified T : Any,
    reified P1 : Any,
    reified P2 : Any,
    reified P3 : Any,
    reified P4 : Any> DIComponentHolder.call(
    function: (P1, P2, P3, P4) -> T
): T =
    function(provide(), provide(), provide(), provide())

inline fun <reified T : Any,
    reified P1 : Any,
    reified P2 : Any,
    reified P3 : Any,
    reified P4 : Any,
    reified P5 : Any> DIComponentHolder.call(
    function: (P1, P2, P3, P4, P5) -> T
): T =
    function(provide(), provide(), provide(), provide(), provide())

inline fun <reified T : Any,
    reified P1 : Any,
    reified P2 : Any,
    reified P3 : Any,
    reified P4 : Any,
    reified P5 : Any,
    reified P6 : Any> DIComponentHolder.call(
    function: (P1, P2, P3, P4, P5, P6) -> T
): T =
    function(provide(), provide(), provide(), provide(), provide(), provide())

inline fun <reified T : Any,
    reified P1 : Any,
    reified P2 : Any,
    reified P3 : Any,
    reified P4 : Any,
    reified P5 : Any,
    reified P6 : Any,
    reified P7 : Any> DIComponentHolder.call(
    function: (P1, P2, P3, P4, P5, P6, P7) -> T
): T =
    function(provide(), provide(), provide(), provide(), provide(), provide(), provide())
