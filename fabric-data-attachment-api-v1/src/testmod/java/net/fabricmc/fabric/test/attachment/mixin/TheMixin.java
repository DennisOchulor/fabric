package net.fabricmc.fabric.test.attachment.mixin;

import com.mojang.authlib.GameProfile;

import net.fabricmc.fabric.test.attachment.AttachmentTestMod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class TheMixin extends Player {

	public TheMixin(Level level, GameProfile gameProfile) {
		super(level, gameProfile);
	}

	@Inject(method = "setShoulderEntityLeft", at = @At("TAIL"))
	protected void setShoulderEntityLeft(CompoundTag tag, CallbackInfo callback) {
		this.modifyAttached(AttachmentTestMod.SYNCED_WITH_ALL, bl -> bl==null || !bl);
	}

	@Inject(method = "setShoulderEntityRight", at = @At("TAIL"))
	protected void setShoulderEntityRight(CompoundTag tag, CallbackInfo callback) {
		this.modifyAttached(AttachmentTestMod.SYNCED_WITH_ALL, bl -> bl==null || !bl);
	}

}
