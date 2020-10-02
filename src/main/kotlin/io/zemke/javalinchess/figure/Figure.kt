package io.zemke.javalinchess.figure

import io.zemke.javalinchess.controller.Entity

open class Figure(val name: String) : Entity() {
    override fun toString(): String {
        return name
    }
}
