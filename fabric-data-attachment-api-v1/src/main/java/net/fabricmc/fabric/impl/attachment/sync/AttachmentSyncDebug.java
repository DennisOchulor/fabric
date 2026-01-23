package net.fabricmc.fabric.impl.attachment.sync;

import java.util.Objects;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.commons.lang3.exception.ExceptionUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class AttachmentSyncDebug {
	public record AttSyncDebugInfo(String type, String stackTrace) {
		public AttSyncDebugInfo(String type) {
			this(type, ExceptionUtils.getStackTrace(new Exception("att sync debug stack trace")));
		}

		public static final StreamCodec<FriendlyByteBuf, AttSyncDebugInfo> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8, AttSyncDebugInfo::type,
				ByteBufCodecs.STRING_UTF8, AttSyncDebugInfo::stackTrace,
				AttSyncDebugInfo::new
		);
	}

	private static int id = 0;
	private static final Int2ObjectMap<AttSyncDebugInfo> debugInfos = new Int2ObjectOpenHashMap<>();

	public static int nextDebugInfo(String syncType) {
		debugInfos.put(id, new AttSyncDebugInfo(syncType));
		return id++;
	}

	public static AttSyncDebugInfo get(int id) {
		return Objects.requireNonNull(debugInfos.getOrDefault(id, null));
	}
}
