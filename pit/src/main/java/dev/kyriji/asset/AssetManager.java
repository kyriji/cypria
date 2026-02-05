package dev.kyriji.asset;

import com.hypixel.hytale.assetstore.event.LoadedAssetsEvent;
import com.hypixel.hytale.assetstore.map.IndexedLookupTableAssetMap;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.modules.entitystats.asset.condition.Condition;
import com.hypixel.hytale.server.core.modules.entitystats.asset.condition.SprintingCondition;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AssetManager {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private static final String STAMINA_STAT_ID = "Stamina";

    public static void onEntityStatsLoaded(@Nonnull LoadedAssetsEvent event) {
        try {
            LOGGER.atInfo().log("Beginning entity stats asset modifications");
            removeSprintStaminaCost();
            LOGGER.atInfo().log("Entity stats asset modifications complete");
        } catch (Exception e) {
            LOGGER.atSevere().log("Failed to perform asset modifications", e);
        }
    }

    private static void removeSprintStaminaCost() throws Exception {
        IndexedLookupTableAssetMap<String, EntityStatType> assetMap = EntityStatType.getAssetMap();
        EntityStatType staminaStat = assetMap.getAsset(STAMINA_STAT_ID);

        if (staminaStat == null) {
            LOGGER.atSevere().log("Stamina stat not found");
            return;
        }

        EntityStatType.Regenerating[] regeneratingArray = staminaStat.getRegenerating();
        if (regeneratingArray == null || regeneratingArray.length == 0) {
            LOGGER.atSevere().log("Stamina stat has no regenerating entries");
            return;
        }

        List<EntityStatType.Regenerating> sprintRegenStat = new ArrayList<>();
        boolean modified = false;

        for (EntityStatType.Regenerating regen : regeneratingArray) {
            if (isSprintStaminaConsumption(regen)) {
                LOGGER.atInfo().log("Found sprint stamina drain: interval=" + regen.getInterval() + ", amount=" + regen.getAmount());
                modified = true;
            } else {
                sprintRegenStat.add(regen);
            }
        }

        if (modified) {
            EntityStatType.Regenerating[] newArray = sprintRegenStat.toArray(
                new EntityStatType.Regenerating[0]
            );
            setRegeneratingArray(staminaStat, newArray);
            LOGGER.atInfo().log("Removed sprint stamina drain (" + regeneratingArray.length + " -> " + newArray.length + " entries)");
        }
    }

    private static boolean isSprintStaminaConsumption(@Nonnull EntityStatType.Regenerating regenStat) {
        if (regenStat.getAmount() >= 0) return false;
        Condition[] conditions = regenStat.getConditions();
        if (conditions == null) return false;
        for (Condition condition : conditions) if (condition instanceof SprintingCondition) return !isConditionInverse(condition);
        return false;
    }

    private static boolean isConditionInverse(@Nonnull Condition condition) {
        try {
            Field inverseField = Condition.class.getDeclaredField("inverse");
            inverseField.setAccessible(true);
            return inverseField.getBoolean(condition);
        } catch (Exception e) {
            return false;
        }
    }

    private static void setRegeneratingArray(
        @Nonnull EntityStatType statType,
        @Nonnull EntityStatType.Regenerating[] newArray
    ) throws Exception {
        Field regeneratingField = EntityStatType.class.getDeclaredField("regenerating");
        regeneratingField.setAccessible(true);
        regeneratingField.set(statType, newArray);
    }
}
