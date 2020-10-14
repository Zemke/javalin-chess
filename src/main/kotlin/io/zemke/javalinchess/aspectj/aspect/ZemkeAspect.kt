package io.zemke.javalinchess.aspectj.aspect

import io.zemke.javalinchess.aspectj.annotations.Inject
import io.zemke.javalinchess.aspectj.annotations.Zemke
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory

@Aspect
class ZemkeAspect {

    @Pointcut("@annotation(zemke)")
    fun callAt(zemke: Zemke?) {
    }

    @After(value = "callAt(zemke)", argNames = "jp,zemke")
    fun after(jp: JoinPoint, zemke: Zemke?) {
        logger.info("---------------------- ZemkeAspect ----------------------")
        if (jp.target != null) {
            logger.info("jp.target ${jp.target}")
            jp.target.javaClass.declaredFields.forEach {
                try {
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
                } catch (e: Exception) {
                    println("OMG")
                }
            }
        }
        logger.info("---------------------- ZemkeAspect ----------------------")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
