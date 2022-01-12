package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.entity.Entity;

import net.mcreator.lotmorestevesremake.particle.SteveFaceParticleParticle;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeMod;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

public class CursedDiversionOnEffectActiveTickProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency world for procedure CursedDiversionOnEffectActiveTick!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency x for procedure CursedDiversionOnEffectActiveTick!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency y for procedure CursedDiversionOnEffectActiveTick!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency z for procedure CursedDiversionOnEffectActiveTick!");
			return;
		}
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency entity for procedure CursedDiversionOnEffectActiveTick!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		Entity entity = (Entity) dependencies.get("entity");
		if (world instanceof ServerWorld) {
			((ServerWorld) world).spawnParticle(SteveFaceParticleParticle.particle, x, y, z,
					(int) (5 * Math.pow(entity.getWidth(), 2) * entity.getHeight()), Math.pow(entity.getWidth(), 0.5), (entity.getHeight() * 0.66),
					Math.pow(entity.getWidth(), 0.5), 0);
		}
		if (!entity.isAlive()) {
			InfectionEventProcedure.executeProcedure(Stream
					.of(new AbstractMap.SimpleEntry<>("world", world), new AbstractMap.SimpleEntry<>("entity", entity),
							new AbstractMap.SimpleEntry<>("x", x), new AbstractMap.SimpleEntry<>("y", y), new AbstractMap.SimpleEntry<>("z", z))
					.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
		}
	}
}
