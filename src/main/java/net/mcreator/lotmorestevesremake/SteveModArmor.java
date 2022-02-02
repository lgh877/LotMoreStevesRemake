/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.lotmorestevesremake as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.lotmorestevesremake;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.extensions.IForgeItem;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ArmorItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.LivingEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SteveModArmor extends ArmorItem implements IForgeItem {
	public SteveModArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Item.Properties builderIn) {
		super(materialIn, slot, builderIn);
	}

	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return false;
	}

	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		return false;
	}

	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		return false;
	}

	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
		return false;
	}

	public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
		return false;
	}
}
