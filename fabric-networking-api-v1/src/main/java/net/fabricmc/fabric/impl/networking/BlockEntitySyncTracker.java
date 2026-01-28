package net.fabricmc.fabric.impl.networking;

public interface BlockEntitySyncTracker {
	boolean fabric_hasSyncedToAnyClients();

	void fabric_setHasSyncedToAnyClients();
}
