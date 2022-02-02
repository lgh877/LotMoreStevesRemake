
package net.mcreator.lotmorestevesremake.itemgroup;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.block.Blocks;

import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;

@LotmorestevesremakeModElements.ModElement.Tag
public class ArmorsOfStevesItemGroup extends LotmorestevesremakeModElements.ModElement {
	public ArmorsOfStevesItemGroup(LotmorestevesremakeModElements instance) {
		super(instance, 40);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabarmors_of_steves") {
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
