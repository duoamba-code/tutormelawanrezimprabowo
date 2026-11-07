package net.kirin.voxelbridge;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KirinVoxelBridge implements ClientModInitializer {
    public static final String MOD_ID = "kirinvx";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
    public static final VoxelWorld WORLD = new VoxelWorld();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(WORLD::tick);
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> KirinCommands.register(dispatcher));
        LOG.info("Kirin Voxel Bridge (tahap 1) dimuat: grid {}^3, tanpa render.", VoxelWorld.SIZE);
    }
}
