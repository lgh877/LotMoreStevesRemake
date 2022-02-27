package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.world.IWorld;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

public class LimpSteveSpawnConditionProcedure {
	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			return false;
		}
		IWorld world = (IWorld) dependencies.get("world");
		return world.getWorldInfo().getDayTime() > 240000
				&& SpawnInOverworldOnlyProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("world", world)).collect(HashMap::new,
						(_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
	}
}
