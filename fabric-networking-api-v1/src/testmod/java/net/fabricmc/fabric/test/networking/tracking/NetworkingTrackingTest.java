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

package net.fabricmc.fabric.test.networking.tracking;

import io.netty.buffer.ByteBuf;

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ChunkPos;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.test.networking.NetworkingTestmods;

public class NetworkingTrackingTest implements ModInitializer {
	@Override
	public void onInitialize() {
		PayloadTypeRegistry.clientboundPlay().register(LevelTrackingPayload.TYPE, LevelTrackingPayload.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(ChunkTrackingPayload.TYPE, ChunkTrackingPayload.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(BlockEntityTrackingPayload.TYPE, BlockEntityTrackingPayload.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(EntityTrackingPayload.TYPE, EntityTrackingPayload.STREAM_CODEC);

		ServerLevelEvents.LOAD.register((_, level) -> {
			PlayerLookup.level(level).forEach(player -> ServerPlayNetworking.send(player, new LevelTrackingPayload(level.dimension().identifier())));
		});

		ServerChunkEvents.CHUNK_LOAD.register((level, chunk, _) -> {
			//PlayerLookup.tracking(level, chunk.getPos()).forEach(player -> ServerPlayNetworking.send(player, new ChunkTrackingPayload(chunk.getPos())));
		});

		ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, _) -> {
			PlayerLookup.tracking(blockEntity).forEach(player -> ServerPlayNetworking.send(player, new BlockEntityTrackingPayload(blockEntity.getBlockPos())));
		});

		ServerEntityEvents.ENTITY_LOAD.register((entity, _) -> {
			PlayerLookup.tracking(entity).forEach(player -> ServerPlayNetworking.send(player, new EntityTrackingPayload(entity.getId())));
		});

		ServerTickEvents.END_LEVEL_TICK.register(level -> {
			if (level.getServer().getTickCount() % 100 != 0) {
				return;
			}

			PlayerLookup.level(level).forEach(player -> ServerPlayNetworking.send(player, new LevelTrackingPayload(level.dimension().identifier())));

			level.getChunkSource().chunkMap.forEachReadyToSendChunk(chunk -> {
				PlayerLookup.tracking(level, chunk.getPos()).forEach(player -> {
					ServerPlayNetworking.send(player, new ChunkTrackingPayload(chunk.getPos()));

					chunk.getBlockEntities().forEach((_, blockEntity) -> {
						PlayerLookup.tracking(blockEntity).forEach(player1 -> ServerPlayNetworking.send(player1, new BlockEntityTrackingPayload(blockEntity.getBlockPos())));
					});
				});
			});

			level.getAllEntities().forEach(entity -> {
				PlayerLookup.tracking(entity).forEach(player -> ServerPlayNetworking.send(player, new EntityTrackingPayload(entity.getId())));
			});
		});
	}

	public record LevelTrackingPayload(Identifier level) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<LevelTrackingPayload> TYPE = new CustomPacketPayload.Type<>(NetworkingTestmods.id("tracking_level"));
		public static final StreamCodec<ByteBuf, LevelTrackingPayload> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, LevelTrackingPayload::level, LevelTrackingPayload::new);

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record ChunkTrackingPayload(ChunkPos chunkPos) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<ChunkTrackingPayload> TYPE = new CustomPacketPayload.Type<>(NetworkingTestmods.id("tracking_chunk"));
		public static final StreamCodec<ByteBuf, ChunkTrackingPayload> STREAM_CODEC = StreamCodec.composite(ChunkPos.STREAM_CODEC, ChunkTrackingPayload::chunkPos, ChunkTrackingPayload::new);

		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record BlockEntityTrackingPayload(BlockPos blockPos) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<BlockEntityTrackingPayload> TYPE = new CustomPacketPayload.Type<>(NetworkingTestmods.id("tracking_block_entity"));
		public static final StreamCodec<ByteBuf, BlockEntityTrackingPayload> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, BlockEntityTrackingPayload::blockPos, BlockEntityTrackingPayload::new);

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record EntityTrackingPayload(int id) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<EntityTrackingPayload> TYPE = new CustomPacketPayload.Type<>(NetworkingTestmods.id("tracking_entity"));
		public static final StreamCodec<ByteBuf, EntityTrackingPayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, EntityTrackingPayload::id, EntityTrackingPayload::new);

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
}
