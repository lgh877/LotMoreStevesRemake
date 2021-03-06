package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.world.Explosion;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;

import java.util.Random;
import java.util.Map;

public class TntArmorSetBodyTickEventProcedure {
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
		if (dependencies.get("entity") == null) {
			return;
		}
		if (dependencies.get("itemstack") == null) {
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		Entity entity = (Entity) dependencies.get("entity");
		ItemStack itemstack = (ItemStack) dependencies.get("itemstack");
		Random random = new Random();
		if (random.nextInt(50) == 0 && random.nextInt(50) == 0) {
			(itemstack).shrink((int) 1);
			if (world instanceof World && !((World) world).isRemote) {
				((World) world).createExplosion(null, (int) x, (int) y, (int) z, (float) 4, Explosion.Mode.BREAK);
			}
		} else if (random.nextInt(10) == 0) {
			if (world instanceof ServerWorld) {
				((ServerWorld) world).spawnParticle(ParticleTypes.LAVA, x, (entity.getHeight() + y), z, (int) 2, 0, 0, 0, 1);
			}
		}
	}
}
