package dev.kyriji.commonmc.cypria.item.models;

import dev.kyriji.commonmc.cypria.item.ability.models.ItemAbility;
import dev.kyriji.commonmc.cypria.item.enums.ItemType;

public abstract class CypriaWeapon extends CypriaItem {
	public CypriaWeapon(ItemType itemType) {
		super(itemType);
		itemAbilities(getLeftAbility(), getRightAbility());
	}

	public abstract ItemAbility getLeftAbility();
	public abstract ItemAbility getRightAbility();

	@Override
	public ItemProperties createFullProperties(ItemProperties staticProperties, ItemProperties dynamicProperties) {
		ItemProperties itemProperties = super.createFullProperties(staticProperties, dynamicProperties);
		return new ItemProperties.Builder(itemProperties)
				.addData(new CustomData()
						.set(CustomProperty.ABILITY_LEFT, getLeftAbility() != null ? getLeftAbility().getAbilityType().getID() : null)
						.set(CustomProperty.ABILITY_RIGHT, getRightAbility() != null ? getRightAbility().getAbilityType().getID() : null)
				)
				.build();
	}
}
