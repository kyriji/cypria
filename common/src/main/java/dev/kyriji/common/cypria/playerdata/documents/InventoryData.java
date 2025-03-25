package dev.kyriji.common.cypria.playerdata.documents;
import dev.kyriji.common.cypria.playerdata.models.PlayerDataDocument;
import java.util.UUID;

public class InventoryData extends PlayerDataDocument {

	private String[] inventory = null;

	public InventoryData() {
		super();
	}

	public InventoryData(UUID uuid, String[] inventory) {
		this.uuid = uuid.toString();
		this.inventory = inventory;
	}

	public String[] getInventory() {
		return inventory;
	}
}