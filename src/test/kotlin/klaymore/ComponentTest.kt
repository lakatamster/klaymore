package klaymore

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.Test
import java.util.*

object ChildScope : Scope

object GrandSonScope : Scope

@JvmInline
value class Ten(val value: Int)

@JvmInline
value class Three(val value: Int)

@JvmInline
value class HelloProvider(val provider: Provider<String>)

@Suppress("UNUSED_PARAMETER", "unused")
class ComponentTest {

    @Test
    fun provide() {
        open class IC

        class A

        class B(val a: A)

        class C(val a: A, val b: B) : IC()

        val module = module {
            bind<A>(Singleton) with ::A
            bind<B>(Factory) with ::B
            bind<C>(Factory) with ::C
            bind<IC>(Singleton) with { c: C -> c }
        }

        val component = Component(module)

        class Target(override val component: DIComponent) : DIComponentHolder {
            val a: A by provider()
            val b: B by provider()
            val c: C by provider()
            val ic: IC by provider()
        }

        withDI(component) {
            val b: B = provide()
            val a: A = provide()
            val c: C = provide()
            b.a shouldBeSameInstanceAs a
            b shouldNotBeSameInstanceAs provide<B>()
            c.a shouldBeSameInstanceAs a
            c.b shouldNotBeSameInstanceAs b

            val cProvider: Provider<C> = getProvider()
            cProvider.get()

            provide<IC>() shouldBeSameInstanceAs provide<IC>()

            val target = Target(component)
            target.a shouldBeSameInstanceAs a

            Verifier.verifyComponent(component)
        }
    }

    @Test
    fun cycleDependency() {

        class A
        class B

        fun provideA(b: B): A = A()
        fun provideB(a: A): B = B()

        val component = Component {
            bind<A>(Factory) to ::provideA
            bind<B>(Factory) to ::provideB
        }
        shouldThrow<IllegalStateException> {
            runCatching { component.getProvider(A::class) }.onFailure {
                println(it.message)
            }.getOrThrow()
        }
    }

    @Test
    fun callFunction() {

        class A
        class B(val a: A)
        class C {
            var executed = false
            fun check(a: A, b: B) {
                b.a shouldBeSameInstanceAs a
                executed = true
            }
        }

        val module = module {
            bind<A>(Singleton) with ::A
            bind<B>(Factory) with ::B
        }

        withDI(Component(module)) {

            provide<B>()

            val c = C()

            call(c::check)
            c.executed shouldBe true
        }
    }

    @Test
    fun perComponentProviders() {

        val rootModule = module {
            val uuid = UUID.randomUUID()
            bind<UUID>(Factory) with { -> uuid }
        }

        val rootComponent = Component(rootModule)
        val childComponent = rootComponent.createSubcomponent(ChildScope) {}

        rootComponent.getProvider(UUID::class).get() shouldBe rootComponent.getProvider(UUID::class).get()
        childComponent.getProvider(UUID::class).get() shouldBe childComponent.getProvider(UUID::class).get()
        childComponent.getProvider(UUID::class).get() shouldNotBe rootComponent.getProvider(UUID::class).get()
    }

    @Test
    fun singleton() {

        class A

        val rootModule = module {
            bind<A>(Singleton) with ::A
        }

        val component = Component { rootModule() }
        val a1 = component.getProvider(A::class).get()
        val childComponent = component.createSubcomponent(ChildScope) {}
        val a2 = childComponent.getProvider(A::class).get()
        val grandSonComponent = childComponent.createSubcomponent(GrandSonScope) {}
        val a3 = grandSonComponent.getProvider(A::class).get()
        a1 shouldBeSameInstanceAs a2
        a2 shouldBeSameInstanceAs a3
    }

    @Test
    fun valueClass() {
        val module = module {
            bind<Ten>(Factory) with { -> Ten(10) }
            bind<Three>(Factory) with { -> Three(3) }
            bind<HelloProvider>(Factory) with { -> HelloProvider { "Hello" } }
        }
        val component = Component(module)
        withDI(component) {
            provide<Ten>().value shouldBe 10
            provide<Three>().value shouldBe 3
            provide<HelloProvider>().provider.get() shouldBe "Hello"
        }
    }
}
