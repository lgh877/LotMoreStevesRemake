package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.world.IWorld;

import net.mcreator.lotmorestevesremake.LotmorestevesremakeMod;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

public class StevillagersSpawnConditionProcedure {

	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency world for procedure StevillagersSpawnCondition!");
			return false;
		}
		IWorld world = (IWorld) dependencies.get("world");
		return world.getWorldInfo().getDayTime() > 240000
				&& SpawnInOverworldOnlyProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("world", world)).collect(HashMap::new,
						(_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
	}
}
