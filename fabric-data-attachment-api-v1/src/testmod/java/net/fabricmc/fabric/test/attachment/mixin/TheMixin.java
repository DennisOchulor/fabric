package net.fabricmc.fabric.test.attachment.mixin;

import net.fabricmc.fabric.test.attachment.AttachmentTestMod;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.FireChargeItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireChargeItem.class)
public abstract class TheMixin {

	@Inject(method = "asProjectile", at = @At(value = "RETURN"))
	private void onAsProjectile(Level level, Position position, ItemStack itemStack, Direction direction, CallbackInfoReturnable<Projectile> cir) {
		Projectile projectile = cir.getReturnValue();
		projectile.modifyAttached(AttachmentTestMod.SYNCED_WITH_ALL, bl -> bl==null || !bl); // ensure set value not the same
	}

}
