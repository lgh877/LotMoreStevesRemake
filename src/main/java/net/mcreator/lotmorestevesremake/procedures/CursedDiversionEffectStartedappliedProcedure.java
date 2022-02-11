package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.world.IWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import net.mcreator.lotmorestevesremake.world.CanCauseInfectionGameRule;
import net.mcreator.lotmorestevesremake.potion.CursedDiversionPotionEffect;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeMod;

import java.util.Map;

public class CursedDiversionEffectStartedappliedProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency world for procedure CursedDiversionEffectStartedapplied!");
			return;
		}
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency entity for procedure CursedDiversionEffectStartedapplied!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		Entity entity = (Entity) dependencies.get("entity");
		if (!world.getWorldInfo().getGameRulesInstance().getBoolean(CanCauseInfectionGameRule.gamerule)) {
			if (entity instanceof LivingEntity) {
				((LivingEntity) entity).removePotionEffect(CursedDiversionPotionEffect.potion);
			}
		}
	}
}
