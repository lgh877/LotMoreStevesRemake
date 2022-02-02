package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.world.IWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;

import net.mcreator.lotmorestevesremake.LotmorestevesremakeMod;

import java.util.Map;

public class GhosteveSpawningConditionProcedure {

	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency world for procedure GhosteveSpawningCondition!");
			return false;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency x for procedure GhosteveSpawningCondition!");
			return false;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency y for procedure GhosteveSpawningCondition!");
			return false;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency z for procedure GhosteveSpawningCondition!");
			return false;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		return world.func_241828_r().getRegistry(Registry.BIOME_KEY).getKey(world.getBiome(new BlockPos((int) x, (int) y, (int) z))) != null
				&& world.func_241828_r().getRegistry(Registry.BIOME_KEY).getKey(world.getBiome(new BlockPos((int) x, (int) y, (int) z)))
						.equals(new ResourceLocation("soul_sand_valley"))
				|| Math.random() < 0.05;
	}
}
