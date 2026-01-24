package net.fabricmc.fabric.mixin.attachment;

import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.server.level.ChunkResult;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;

@Mixin(ServerChunkCache.class)
public interface ServerChunkCacheInvoker {
	@Invoker("getChunkFutureMainThread")
	CompletableFuture<ChunkResult<ChunkAccess>> fabric_getChunkFutureMainThread(
			final int x, final int z, final ChunkStatus targetStatus, final boolean loadOrGenerate
	);
}
