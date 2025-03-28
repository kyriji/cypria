package dev.kyriji.cypria.islands.base.building.models;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import dev.kyriji.commonmc.cypria.CypriaMinecraft;
import dev.kyriji.cypria.islands.base.building.enums.BuildingSchematic;
import dev.kyriji.cypria.islands.base.building.enums.BuildingType;
import dev.kyriji.cypria.islands.base.models.Base;
import dev.kyriji.cypria.islands.base.models.BasePos;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class Building implements Listener {
	private BuildingType type;
	private BasePos position;
	private int level = 1;

	public transient Base base;

	public Building() {
		Bukkit.getPluginManager().registerEvents(this, CypriaMinecraft.plugin());
	}

	public Building(BuildingType type) {
		this();
		this.type = type;

		initTransient(base);
	}

	// static
	public abstract BuildingType getType();
	public abstract BuildingSchematic getSchematic();

	// transient
	public abstract BuildingHealth getHealth();

	public void initTransient(Base base) {
		this.base = base;
	}

	public void setData(BasePos position) {
		this.position = position;
	}

	public void place() {
		File file = new File(CypriaMinecraft.plugin().getDataFolder() + "/../FastAsyncWorldEdit/schematics/" +
				getSchematic().getFileName() + ".schem");
		Clipboard clipboard;
		ClipboardFormat format = ClipboardFormats.findByFile(file);

		try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
			clipboard = reader.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try (EditSession editSession = WorldEdit.getInstance().newEditSession(base.getFAWEWorld())) {
			BlockVector3 minPoint = clipboard.getMinimumPoint();
			BlockVector3 origin = clipboard.getOrigin();
			BlockVector3 offset = minPoint.subtract(origin);

			BlockVector3 adjustedPosition = position.toFAWEBlockVector(base).subtract(offset);

			Operation operation = new ClipboardHolder(clipboard)
					.createPaste(editSession)
					.to(adjustedPosition)
					.build();
			Operations.complete(operation);
		}
	}

	public void destroy() {

	}
}
