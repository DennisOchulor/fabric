package net.fabricmc.fabric.test.networking.client.tracking;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.test.networking.tracking.NetworkingTrackingTest;

public class NetworkingTrackingClientTest implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(NetworkingTrackingTest.LevelTrackingPayload.TYPE, (payload, context) -> {
			ClientLevel level = context.client().level;

			if (level.dimension().identifier() != payload.level()) {
				throw new AssertionError("Received tracking packet for unknown level: " + payload.level());
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(NetworkingTrackingTest.ChunkTrackingPayload.TYPE, (payload, context) -> {
			ClientLevel level = context.client().level;

			if (level.getChunk(payload.chunkPos().x(), payload.chunkPos().z(), ChunkStatus.FULL, false) == null) {
				throw new AssertionError("Received tracking packet for unknown chunk: " + payload.chunkPos() + "in level " + level.dimension().identifier());
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(NetworkingTrackingTest.BlockEntityTrackingPayload.TYPE, (payload, context) -> {
			ClientLevel level = context.client().level;

			if (level.getBlockEntity(payload.blockPos()) == null) {
				throw new AssertionError("Received tracking packet for unknown block entity: " + payload.blockPos() + " in level " + level.dimension().identifier());
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(NetworkingTrackingTest.EntityTrackingPayload.TYPE, (payload, context) -> {
			ClientLevel level = context.client().level;

			if (level.getEntity(payload.id()) == null) {
				throw new AssertionError("Received tracking packet for unknown entity: " + payload.id());
			}
		});
	}
}
