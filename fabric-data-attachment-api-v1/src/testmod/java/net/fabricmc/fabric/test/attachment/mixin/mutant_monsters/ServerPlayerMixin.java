package net.fabricmc.fabric.test.attachment.mixin.mutant_monsters;

import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import net.fabricmc.fabric.test.attachment.AttachmentTestMod;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
	// https://github.com/Fuzss/mutantmonsters/blob/main/1.21.10/Common/src/main/java/fuzs/mutantmonsters/mixin/ServerPlayerMixin.java

	public ServerPlayerMixin(Level level, GameProfile gameProfile) {
		super(level, gameProfile);
	}

	@Inject(method = "setShoulderEntityLeft", at = @At("TAIL"))
	protected void setShoulderEntityLeft(CompoundTag tag, CallbackInfo callback) {
		if (!AttachmentTestMod.DEBUG_MUTANT_MONSTERS) return;

		this.modifyAttached(AttachmentTestMod.SYNCED_WITH_ALL, bl -> bl == null || !bl);
	}

	@Inject(method = "setShoulderEntityRight", at = @At("TAIL"))
	protected void setShoulderEntityRight(CompoundTag tag, CallbackInfo callback) {
		if (!AttachmentTestMod.DEBUG_MUTANT_MONSTERS) return;

		this.modifyAttached(AttachmentTestMod.SYNCED_WITH_ALL, bl -> bl == null || !bl);
	}
}
