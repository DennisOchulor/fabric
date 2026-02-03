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
