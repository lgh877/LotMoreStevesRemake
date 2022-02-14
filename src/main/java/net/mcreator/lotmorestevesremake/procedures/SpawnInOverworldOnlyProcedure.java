package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;

import java.util.Map;

public class SpawnInOverworldOnlyProcedure {
	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			return false;
		}
		IWorld world = (IWorld) dependencies.get("world");
		return (World.OVERWORLD) == (world instanceof World ? (((World) world).getDimensionKey()) : World.OVERWORLD);
	}
}
