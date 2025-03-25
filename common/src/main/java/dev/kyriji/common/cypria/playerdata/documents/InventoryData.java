package dev.kyriji.common.cypria.playerdata.documents;
import dev.kyriji.common.cypria.playerdata.models.PlayerDataDocument;

import java.util.List;
import java.util.UUID;

public class InventoryData extends PlayerDataDocument {

	private List<String> inventory = null;

	public InventoryData() {
		super();
	}

	public InventoryData(UUID uuid, List<String> inventory) {
		this.uuid = uuid.toString();
		this.inventory = inventory;
	}

	public List<String> getInventory() {
		return inventory;
	}

	public void setInventory(List<String> inventory) {
		this.inventory = inventory;
	}
}