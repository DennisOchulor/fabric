/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

			if (level == null || !level.dimension().identifier().equals(payload.level())) {
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
