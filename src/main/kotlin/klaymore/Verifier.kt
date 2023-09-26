package klaymore

object Verifier {

    /**
     * Verifies component and prints the result to the console.
     */
    fun verifyComponent(component: DIComponent) {
        component as Component
        component.verifyAll { key, provider ->
            println(key)
            var dep: Any = provider
            var gets = ""
            while (dep is Provider<*>) {
                gets += "==> "
                dep = dep.get()!!
                println("\t$gets $dep")
            }
        }
    }
}
