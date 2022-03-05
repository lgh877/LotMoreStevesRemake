
package net.mcreator.lotmorestevesremake.itemgroup;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;

import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;

@LotmorestevesremakeModElements.ModElement.Tag
public class OtherMobsItemGroup extends LotmorestevesremakeModElements.ModElement {
	public OtherMobsItemGroup(LotmorestevesremakeModElements instance) {
		super(instance, 58);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabother_mobs") {
			@OnlyIn(Dist.CLIENT)
			@Override
			public ItemStack createIcon() {
				return new ItemStack(Items.PLAYER_HEAD);
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}

	public static ItemGroup tab;
}
