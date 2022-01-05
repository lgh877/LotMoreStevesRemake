package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.particles.ParticleTypes;

import net.mcreator.lotmorestevesremake.LotmorestevesremakeMod;

import java.util.Map;

public class SlimeBombWhileProjectileFlyingTickProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency world for procedure SlimeBombWhileProjectileFlyingTick!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency x for procedure SlimeBombWhileProjectileFlyingTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency y for procedure SlimeBombWhileProjectileFlyingTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency z for procedure SlimeBombWhileProjectileFlyingTick!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		if (world instanceof ServerWorld) {
			((ServerWorld) world).spawnParticle(ParticleTypes.ITEM_SLIME, x, y, z, (int) 3, 0.2, 0.2, 0.2, 0);
		}
	}
}
