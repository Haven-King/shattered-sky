package dev.hephaestus.shatteredsky;

import dev.hephaestus.shatteredsky.util.SyncedChunkData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.util.Identifier;

public class Networking {
    public static final Identifier SYNC_CHUNK_DATA = ShatteredSky.id("chunk_data", "sync");

    public static void init() {

    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ClientSidePacketRegistry.INSTANCE.register(SYNC_CHUNK_DATA, SyncedChunkData::receive);
    }
}
