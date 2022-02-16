package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.world.IWorld;
import net.minecraft.particles.ParticleTypes;

import java.util.Map;

public class MinigunWhileProjectileFlyingTickProcedure {
	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			return;
		}
		if (dependencies.get("x") == null) {
			return;
		}
		if (dependencies.get("y") == null) {
			return;
		}
		if (dependencies.get("z") == null) {
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		world.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
	}
}
