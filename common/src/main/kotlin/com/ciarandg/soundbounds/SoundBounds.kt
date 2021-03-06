package com.ciarandg.soundbounds

import com.ciarandg.soundbounds.client.ClientEvents
import com.ciarandg.soundbounds.client.regions.ClientRegion
import com.ciarandg.soundbounds.common.CommonEvents
import com.ciarandg.soundbounds.common.regions.RegionData
import com.ciarandg.soundbounds.server.ServerEvents
import me.shedaniel.architectury.event.events.LifecycleEvent
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent
import me.shedaniel.architectury.platform.Platform
import net.fabricmc.api.EnvType
import net.minecraft.text.MutableText
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class SoundBounds {
    init {
        CommonEvents.register()
        if (Platform.getEnv() == EnvType.CLIENT)
            ClientLifecycleEvent.CLIENT_SETUP.register { ClientEvents.register() }
        LifecycleEvent.SERVER_STARTED.register { ServerEvents.register() }
    }

    companion object {
        const val MOD_ID = "soundbounds"
        val LOGGER: Logger = LogManager.getLogger(MOD_ID)

        val POS_MARKER_UPDATE_CHANNEL_S2C = Identifier(MOD_ID, "pos_marker_update_s2c")
        val POS_MARKER_UPDATE_CHANNEL_C2S = Identifier(MOD_ID, "pos_marker_update_c2s")
        val VISUALIZE_REGION_CHANNEL_S2C = Identifier(MOD_ID, "visualize_region_c2s")
        val META_HASH_CHECK_S2C = Identifier(MOD_ID, "meta_hash_check_s2c")
        val META_HASH_CHECK_C2S = Identifier(MOD_ID, "meta_hash_check_c2s")
        val NOW_PLAYING_CHANNEL_C2S = Identifier(MOD_ID, "now_playing_c2s")
        val NOW_PLAYING_CHANNEL_S2C = Identifier(MOD_ID, "now_playing_s2c")
        val CURRENT_REGION_CHANNEL_C2S = Identifier(MOD_ID, "current_region_c2s")
        val CURRENT_REGION_CHANNEL_S2C = Identifier(MOD_ID, "current_region_s2c")
        val SYNC_METADATA_CHANNEL_C2S = Identifier(MOD_ID, "sync_metadata_c2s")
        val SYNC_METADATA_CHANNEL_S2C = Identifier(MOD_ID, "sync_metadata_s2c")
        val UPDATE_REGIONS_CHANNEL_S2C = Identifier(MOD_ID, "update_regions")
        val DESTROY_REGION_CHANNEL_S2C = Identifier(MOD_ID, "destroy_region")
    }
}

typealias RegionEntry = Pair<String, RegionData>
typealias ClientRegionEntry = Pair<String, ClientRegion>
operator fun MutableText.plus(text: MutableText): MutableText = append(text)
