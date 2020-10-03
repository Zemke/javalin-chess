package io.zemke.javalinchess.figure

import io.zemke.javalinchess.controller.Entity
import io.zemke.javalinchess.controller.Player

open class Figure(val name: String, val player: Player) : Entity() {

    override fun toString(): String {
        return name
    }
}
