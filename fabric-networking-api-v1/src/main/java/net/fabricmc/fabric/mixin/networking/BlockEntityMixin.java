package net.fabricmc.fabric.mixin.networking;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.world.level.block.entity.BlockEntity;

import net.fabricmc.fabric.impl.networking.BlockEntitySyncTracker;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements BlockEntitySyncTracker {
	@Unique
	private boolean fabric_hasSyncedToAnyClients = false;

	@Unique
	public boolean fabric_hasSyncedToAnyClients() {
		return fabric_hasSyncedToAnyClients;
	}

	@Unique
	public void fabric_setHasSyncedToAnyClients() {
		fabric_hasSyncedToAnyClients = true;
	}
}
