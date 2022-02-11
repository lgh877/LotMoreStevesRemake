package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import net.mcreator.lotmorestevesremake.potion.CursedDiversionPotionEffect;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeMod;

import java.util.Map;

public class CursedDiversionEffectExpiresProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency entity for procedure CursedDiversionEffectExpires!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).removePotionEffect(CursedDiversionPotionEffect.potion);
		}
	}
}
