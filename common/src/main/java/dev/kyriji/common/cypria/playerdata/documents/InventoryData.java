package dev.kyriji.common.cypria.playerdata.documents;
import dev.kyriji.common.cypria.playerdata.models.PlayerDataDocument;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class InventoryData extends PlayerDataDocument {

	private List<String> inventoryStrings = null;

	public InventoryData() {
		super();
	}

	public InventoryData(UUID uuid, List<byte[]> inventory) {
		this.uuid = uuid.toString();
		this.inventoryStrings = convertToBase64Strings(inventory);
	}

	public List<byte[]> getInventory() {
		if (inventoryStrings == null) return new ArrayList<>();

		List<byte[]> inventory = new ArrayList<>();
		for (String base64String : inventoryStrings) {
			if (base64String == null) {
				inventory.add(null);
			} else {
				inventory.add(Base64.getDecoder().decode(base64String));
			}
		}
		return inventory;
	}

	public void setInventory(List<byte[]> inventory) {
		this.inventoryStrings = convertToBase64Strings(inventory);
	}

	private List<String> convertToBase64Strings(List<byte[]> byteArrays) {
		if (byteArrays == null) return null;
		List<String> base64Strings = new ArrayList<>();
		for (byte[] bytes : byteArrays) {
			if (bytes == null) {
				base64Strings.add(null);
			} else {
				base64Strings.add(Base64.getEncoder().encodeToString(bytes));
			}
		}
		return base64Strings;
	}
}