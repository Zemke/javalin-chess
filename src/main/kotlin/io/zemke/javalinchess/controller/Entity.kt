package io.zemke.javalinchess.controller

import java.io.Serializable
import java.util.*

open class Entity(
        val id: String = UUID.randomUUID().toString()
) : Serializable
