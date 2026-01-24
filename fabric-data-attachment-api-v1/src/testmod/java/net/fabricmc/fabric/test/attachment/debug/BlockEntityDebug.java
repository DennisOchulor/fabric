package net.fabricmc.fabric.test.attachment.debug;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.test.attachment.AttachmentTestMod;

public class BlockEntityDebug {
	public static void debug() {
		if (!AttachmentTestMod.DEBUG_BLOCK_ENTITY) return;

		ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, level) -> {
			blockEntity.modifyAttached(AttachmentTestMod.SYNCED_WITH_ALL, bl -> bl == null || !bl);
		});
	}
}
