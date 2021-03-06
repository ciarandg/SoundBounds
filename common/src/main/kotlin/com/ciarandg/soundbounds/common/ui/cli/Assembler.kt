package com.ciarandg.soundbounds.common.ui.cli

import com.ciarandg.soundbounds.server.ui.controller.PlayerController
import com.ciarandg.soundbounds.server.ui.controller.PlayerControllers
import com.ciarandg.soundbounds.server.ui.controller.WorldController
import com.ciarandg.soundbounds.server.ui.controller.WorldControllers
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.TranslatableText

internal object Assembler {
    fun assembleLiteral(root: CommandNode): LiteralArgumentBuilder<ServerCommandSource> {
        if (root.data !is LiteralNodeData) throw IllegalArgumentException("Must be a literal node")
        val c = CommandManager.literal(root.data.literal).requires { it.hasPermissionLevel(root.permissionLevel) }
        assemblyHelper(c, root)
        return c
    }

    fun assembleArg(root: CommandNode): RequiredArgumentBuilder<ServerCommandSource, out Any?> {
        if (root.data !is ArgNodeData<*, *>) throw IllegalArgumentException("Must be an argument node")
        val c = CommandManager.argument(root.data.arg.name, root.data.arg.supply())
        assemblyHelper(c, root)
        return c
    }

    private fun <T : ArgumentBuilder<ServerCommandSource, T>?> assemblyHelper(
        c: ArgumentBuilder<ServerCommandSource, T>,
        root: CommandNode
    ) {
        val work = root.data.work
        if (work != null) c.executes { ctx -> runCommand(ctx, work) }
        for (n in root.children) {
            if (n.data is LiteralNodeData) c.then(assembleLiteral(n))
            else if (n.data is ArgNodeData<*, *>) c.then(assembleArg(n))
        }
    }

    private fun runCommand(
        ctx: CommandContext<ServerCommandSource>,
        command: (CommandContext<ServerCommandSource>, WorldController, PlayerController?) -> Unit
    ): Int {
        val source = ctx.source
        when (val entity = source?.entity) {
            is ServerPlayerEntity -> command(ctx, WorldControllers[source.world], PlayerControllers[entity])
            null -> command(ctx, WorldControllers[source.world], null)
            else -> source.sendError(TranslatableText("Invalid command source. Please run in-game as a player"))
        }
        return 1
    }
}
