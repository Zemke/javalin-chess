package io.zemke.javalinchess.figure

import io.zemke.javalinchess.controller.Entity
import io.zemke.javalinchess.controller.Player

abstract class Figure(val name: String, val player: Player, val color: Color, val position: Position) : Entity() {

    override fun toString(): String {
        return name
    }
}
