package klaymore

typealias Module = BindingsBuilder.() -> Unit

fun module(module: Module) = module
