package io.zemke.javalinchess.aspectj.aspect

import io.zemke.javalinchess.aspectj.annotations.Inject
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

@Aspect
class ZemkeAspect {

    @Pointcut("execution((@io.zemke.javalinchess.aspectj.annotations.Zemke *).new())")
    fun pointcut() {
    }

    @After(value = "pointcut()", argNames = "jp")
    fun advice(jp: JoinPoint) {
        if (jp.target == null) return
        jp.target.javaClass.declaredFields.forEach {
            val inj = it.declaredAnnotations.toList().filterIsInstance<Inject>()
            if (inj.isNotEmpty()) {
                val isAccessible = it.isAccessible
                it.isAccessible = true
                if (it.get(jp.target!!) == null) {
                    // todo #di retrieve injectable from container
                    //  so that it's actually a singleton
                    it.set(jp.target!!, it.type.newInstance())
                }
                it.isAccessible = isAccessible
            }
        }
    }
}
