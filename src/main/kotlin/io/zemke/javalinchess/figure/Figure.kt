package io.zemke.javalinchess.figure

import java.io.Serializable

open class Figure(val name: String) : Serializable {
    override fun toString(): String {
        return name
    }
}
