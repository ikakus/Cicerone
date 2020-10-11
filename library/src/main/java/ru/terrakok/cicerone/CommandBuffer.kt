/*
 * Created by Konstantin Tskhovrebov (aka @terrakok)
 */
package ru.terrakok.cicerone

import ru.terrakok.cicerone.commands.Command
import java.util.*

/**
 * Passes navigation command to an active [Navigator]
 * or stores it in the pending commands queue to pass it later.
 */
open class CommandBuffer : NavigatorHolder {
    private var navigator: Navigator? = null
    private val pendingCommands: Queue<Array<Command>> = LinkedList()
    override fun setNavigator(navigator: Navigator) {
        this.navigator = navigator
        while (!pendingCommands.isEmpty()) {
            if (navigator != null) {
                executeCommands(pendingCommands.poll())
            } else break
        }
    }

    override fun removeNavigator() {
        navigator = null
    }

    /**
     * Passes `commands` to the [Navigator] if it available.
     * Else puts it to the pending commands queue to pass it later.
     * @param commands navigation command array
     */
    fun executeCommands(commands: Array<Command>) {
        if (navigator != null) {
            navigator!!.applyCommands(commands)
        } else {
            pendingCommands.add(commands)
        }
    }
}