package net.fabricmc.fabric.mixin.networking;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.PlayerChunkSender;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.chunk.LevelChunk;

import net.fabricmc.fabric.impl.networking.BlockEntitySyncTracker;

@Mixin(PlayerChunkSender.class)
public class PlayerChunkSenderMixin {
	@Inject(method = "sendChunk", at = @At(value = "TAIL"))
	private static void markBlockEntitiesAsSynced(ServerGamePacketListenerImpl connection, ServerLevel level, LevelChunk chunk, CallbackInfo ci) {
		chunk.getBlockEntities().forEach((_, blockEntity) -> ((BlockEntitySyncTracker) blockEntity).fabric_setHasSyncedToAnyClients());
	}
}
