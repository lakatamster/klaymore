package klaymore

infix fun <T : Any> BindingsBuilder.Binder<T>.with(call: () -> T) {
    withFunction(emptyList()) { call() }
}

inline infix fun <T : Any,
    reified P1 : Any>
    BindingsBuilder.Binder<T>.with(noinline call: (P1) -> T) {
    withFunction(listOf(P1::class)) { call(it[0] as P1) }
}

inline infix fun <T : Any,
    reified P1 : Any,
    reified P2 : Any>
    BindingsBuilder.Binder<T>.with(noinline call: (P1, P2) -> T) {
    withFunction(listOf(P1::class, P2::class)) { call(it[0] as P1, it[1] as P2) }
}

inline infix fun <T : Any,
    reified P1 : Any,
    reified P2 : Any,
    reified P3 : Any>
    BindingsBuilder.Binder<T>.with(noinline call: (P1, P2, P3) -> T) {
    withFunction(listOf(P1::class, P2::class, P3::class)) { call(it[0] as P1, it[1] as P2, it[2] as P3) }
}

inline infix fun <T : Any,
    reified P1 : Any,
    reified P2 : Any,
    reified P3 : Any,
    reified P4 : Any>
    BindingsBuilder.Binder<T>.with(noinline call: (P1, P2, P3, P4) -> T) {
    withFunction(listOf(P1::class, P2::class, P3::class, P4::class)) { call(it[0] as P1, it[1] as P2, it[2] as P3, it[3] as P4) }
}

inline infix fun <T : Any,
    reified P1 : Any,
    reified P2 : Any,
    reified P3 : Any,
    reified P4 : Any,
    reified P5 : Any>
    BindingsBuilder.Binder<T>.with(noinline call: (P1, P2, P3, P4, P5) -> T) {
    withFunction(listOf(P1::class, P2::class, P3::class, P4::class, P5::class)) {
        call(it[0] as P1, it[1] as P2, it[2] as P3, it[3] as P4, it[4] as P5)
    }
}

inline infix fun <T : Any,
    reified P1 : Any,
    reified P2 : Any,
    reified P3 : Any,
    reified P4 : Any,
    reified P5 : Any,
    reified P6 : Any>
    BindingsBuilder.Binder<T>.with(noinline call: (P1, P2, P3, P4, P5, P6) -> T) {
    withFunction(listOf(P1::class, P2::class, P3::class, P4::class, P5::class, P6::class)) {
        call(it[0] as P1, it[1] as P2, it[2] as P3, it[3] as P4, it[4] as P5, it[5] as P6)
    }
}

inline infix fun <T : Any,
    reified P1 : Any,
    reified P2 : Any,
    reified P3 : Any,
    reified P4 : Any,
    reified P5 : Any,
    reified P6 : Any,
    reified P7 : Any>
    BindingsBuilder.Binder<T>.with(noinline call: (P1, P2, P3, P4, P5, P6, P7) -> T) {
    withFunction(listOf(P1::class, P2::class, P3::class, P4::class, P5::class, P6::class, P7::class)) {
        call(it[0] as P1, it[1] as P2, it[2] as P3, it[3] as P4, it[4] as P5, it[5] as P6, it[6] as P7)
    }
}
