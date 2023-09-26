package klaymore

import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class Component : DIComponent {

    private companion object {
        private fun reportError(message: String, req: Req): Nothing {
            throw CantProvideError(message, req)
        }
    }

    private class LazyProvider<T>(provide: () -> T) : Provider<T> {
        val value by lazy { provide() }

        override fun get(): T = value
    }

    private class Req(
        val klass: KClass<*>,
        val parent: Req? = null
    ) {
        override fun equals(other: Any?): Boolean =
            (other as? Req)?.klass == klass

        override fun hashCode(): Int =
            klass.hashCode()

        fun getErrorString() = buildString {
            append("\t$klass\n")
            var req = parent
            while (req != null) {
                val key = req.klass
                append("\trequested by $key\n")
                req = req.parent
            }
        }
    }

    private class CantProvideError(val title: String, val req: Req) :
        IllegalStateException("$title\n${req.getErrorString()}")

    private constructor(
        parent: Component?,
        scopes: Set<Scope>,
        modules: List<Module>
    ) {
        this.parent = parent
        this.scopes = scopes
        this.modules = modules
    }

    constructor(module: Module) : this(parent = null, scopes = setOf<Scope>(Singleton), modules = listOf(module))

    private val modules: List<Module>
    private val parent: Component?

    private val providers: MutableMap<KClass<*>, Provider<*>> = mutableMapOf()
    private val bindings: Map<KClass<*>, Function<*>> by lazy {
        initBindings()
    }

    private val scopes: Set<Scope>

    override fun <T : Any> getProvider(klass: KClass<T>): Provider<T> =
        getProvider(Req(klass))

    override fun createSubcomponent(scope: Scope, module: Module): DIComponent =
        Component(
            parent = this,
            scopes = this.scopes + scope.also {
                if (it in this.scopes) error("$scope is already exists in parent components, use new scope")
            },
            modules = this.modules + module,
        )

    fun verifyAll(onVerify: (klass: KClass<*>, provider: Provider<Any>) -> Unit) {
        buildSet {
            bindings.forEach {
                val scope = it.value.bindingKind as? Scope
                if (scope == null || scope in scopes) {
                    add(it.key)
                }
            }
            providers.keys.forEach {
                add(it)
            }
        }.forEach {
            onVerify(it, getProvider(Req(it)))
        }
    }

    private fun <T : Any> getProvider(req: Req): Provider<T> {
        getCachedProvider<T>(req.klass)?.let {
            return it
        }
        return construct(req)
    }

    private fun initBindings(): Map<KClass<*>, Function<*>> =
        BindingsBuilder().apply {
            modules.forEach { module ->
                module()
            }
        }.bindings

    private fun <T : Any> construct(req: Req): Provider<T> {
        val constructRequests = LinkedStack<Req>().apply { addFirst(req) }
        while (constructRequests.size > 0) {
            var hasChanges = false
            constructRequests.toList().forEach {
                hasChanges = hasChanges || tryConstruct<T>(it, constructRequests)
            }
            if (!hasChanges) reportError("Cycle dependency detected: ${
                constructRequests.toList().map { it.klass }
            }", req)
        }
        return requireNotNull(getCachedProvider(req.klass))
    }

    private fun <T : Any> getRef(req: Req): Function<T> {
        val type = req.klass
        val function = bindings[type] as Function<T>?
        if (function != null) {
            return function
        } else {
            reportError("Can't find binding for $type", req)
        }
    }

    private fun <T : Any> tryConstruct(req: Req, constructRequests: LinkedStack<Req>): Boolean {
        val key = req.klass
        val function = getRef<T>(req)
        val scope = function.bindingKind as? Scope
        if (parent != null && scope != null && scope in parent.scopes) {
            try {
                providers[key] = parent.getProvider<T>(req)
            } catch (e: CantProvideError) {
                reportError("$key should be provided by $scope scoped component\n${e.title}", e.req)
            }
            return constructRequests.removeAll(req)
        }
        if (scope != null && scope !in scopes) {
            reportError("Can't provide $key in $scopes scopes, because it's provided in $scope", req)
        }
        var hasChanges = false
        var success = true

        val providers = function.paramKlasses.map {
            getCachedProvider<Any>(it) ?: run {
                hasChanges = hasChanges || constructRequests.moveFirst(Req(it, req))
                success = false
                null
            }
        }
        return if (success) {
            fun provide(): T {
                val args = providers.map { provider ->
                    requireNotNull(provider).get()
                }
                return function.call(args)
            }

            val provider = when (scope) {
                in scopes ->
                    LazyProvider(::provide)

                else ->
                    Provider(::provide)
            }
            this.providers[key] = provider
            constructRequests.removeAll(req)
        } else {
            hasChanges
        }
    }

    private fun <T : Any> getCachedProvider(klass: KClass<*>): Provider<T>? =
        providers[klass] as Provider<T>?
}
