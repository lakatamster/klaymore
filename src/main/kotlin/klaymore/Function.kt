package klaymore

import kotlin.reflect.KClass

class Function<T : Any> (val bindingKind: BindingKind, val paramKlasses: List<KClass<*>>, val call: (List<Any>) -> T)
