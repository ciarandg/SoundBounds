package com.ciarandg.soundbounds.common.ui.cli

import com.ciarandg.soundbounds.common.PlaylistType
import com.ciarandg.soundbounds.common.ui.cli.argument.ArgumentContainer
import com.ciarandg.soundbounds.common.ui.cli.command.SoundBoundsCommand.OP_PERM_LEVEL
import com.ciarandg.soundbounds.server.ui.controller.PlayerController
import com.ciarandg.soundbounds.server.ui.controller.WorldController
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.PosArgument
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.math.BlockPos

open class CommandNode(val data: NodeData, val children: List<CommandNode>, val permissionLevel: Int = OP_PERM_LEVEL)

interface NodeData {
    val work: ((CommandContext<ServerCommandSource>, WorldController, PlayerController?) -> Unit)?
}

class LiteralNodeData(
    val literal: String,
    val description: String?,
    override val work: ((CommandContext<ServerCommandSource>, WorldController, PlayerController?) -> Unit)?
) : NodeData

interface ArgNodeData<S, T> : NodeData {
    val arg: ArgumentContainer<S, T>
}

class BoolArgNodeData(
    override val arg: ArgumentContainer<Boolean, Boolean>,
    override val work: ((CommandContext<ServerCommandSource>, WorldController, PlayerController?) -> Unit)?
) : ArgNodeData<Boolean, Boolean>

class IntArgNodeData(
    override val arg: ArgumentContainer<Int, Int>,
    override val work: ((CommandContext<ServerCommandSource>, WorldController, PlayerController?) -> Unit)?
) : ArgNodeData<Int, Int>

class StringArgNodeData(
    override val arg: ArgumentContainer<String, String>,
    override val work: ((CommandContext<ServerCommandSource>, WorldController, PlayerController?) -> Unit)?
) : ArgNodeData<String, String>

class BlockPosArgNodeData(
    override val arg: ArgumentContainer<PosArgument, BlockPos>,
    override val work: ((CommandContext<ServerCommandSource>, WorldController, PlayerController?) -> Unit)?
) : ArgNodeData<PosArgument, BlockPos>

class PlaylistTypeArgData(
    override val arg: ArgumentContainer<PlaylistType, PlaylistType>,
    override val work: ((CommandContext<ServerCommandSource>, WorldController, PlayerController?) -> Unit)?
) : ArgNodeData<PlaylistType, PlaylistType>
