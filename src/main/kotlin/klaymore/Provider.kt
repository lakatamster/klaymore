package klaymore

fun interface Provider<T> {
    fun get(): T
}
