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

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.ModList;

public class ModCompat {
	public static void initCommon(FMLCommonSetupEvent e) {
		if (ModList.get().isLoaded("aoa")) {
			LotmorestevesremakeMod.LOGGER.info("Time to infect the gods!");
		}
	}
}
