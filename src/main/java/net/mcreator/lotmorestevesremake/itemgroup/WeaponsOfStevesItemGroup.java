
package net.mcreator.lotmorestevesremake.itemgroup;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.block.Blocks;

import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;

@LotmorestevesremakeModElements.ModElement.Tag
public class WeaponsOfStevesItemGroup extends LotmorestevesremakeModElements.ModElement {
	public WeaponsOfStevesItemGroup(LotmorestevesremakeModElements instance) {
		super(instance, 5);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabweapons_of_steves") {
			@OnlyIn(Dist.CLIENT)
			@Override
			public ItemStack createIcon() {
				return new ItemStack(Blocks.PLAYER_HEAD);
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}

	public static ItemGroup tab;
}
