package io.zemke.javalinchess.controller

import java.io.Serializable
import java.util.*

open class Entity(
        val id: String = UUID.randomUUID().toString()
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Entity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
