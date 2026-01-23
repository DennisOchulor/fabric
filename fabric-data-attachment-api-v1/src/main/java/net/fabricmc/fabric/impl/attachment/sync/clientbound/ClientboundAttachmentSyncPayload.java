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

package net.fabricmc.fabric.impl.attachment.sync.clientbound;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.impl.attachment.sync.AttachmentChange;

public record ClientboundAttachmentSyncPayload(List<AttachmentChange> attachments, AttSyncDebugInfo debugInfo) implements CustomPacketPayload {
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

	public static final StreamCodec<FriendlyByteBuf, ClientboundAttachmentSyncPayload> CODEC = StreamCodec.composite(
			AttachmentChange.PACKET_CODEC.apply(ByteBufCodecs.list()), ClientboundAttachmentSyncPayload::attachments,
			AttSyncDebugInfo.STREAM_CODEC, ClientboundAttachmentSyncPayload::debugInfo,
			ClientboundAttachmentSyncPayload::new
	);
	public static final Identifier PACKET_ID = Identifier.fromNamespaceAndPath("fabric", "attachment_sync_v1");
	public static final Type<ClientboundAttachmentSyncPayload> ID = new Type<>(PACKET_ID);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
