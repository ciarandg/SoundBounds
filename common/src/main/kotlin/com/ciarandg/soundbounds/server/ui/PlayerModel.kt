package com.ciarandg.soundbounds.server.ui

import net.minecraft.util.math.BlockPos

data class PlayerModel(
    var curSongID: String? = null,
    var marker1: BlockPos? = null,
    var marker2: BlockPos? = null
)
