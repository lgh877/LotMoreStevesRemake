package net.mcreator.lotmorestevesremake.procedures;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;

import net.mcreator.lotmorestevesremake.LotmorestevesremakeMod;

import java.util.Map;

public class SlimeBombProjectileHitsLivingEntityProcedure {
	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency world for procedure SlimeBombProjectileHitsLivingEntity!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency x for procedure SlimeBombProjectileHitsLivingEntity!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency y for procedure SlimeBombProjectileHitsLivingEntity!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency z for procedure SlimeBombProjectileHitsLivingEntity!");
			return;
		}
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency entity for procedure SlimeBombProjectileHitsLivingEntity!");
			return;
		}
		if (dependencies.get("imediatesourceentity") == null) {
			if (!dependencies.containsKey("imediatesourceentity"))
				LotmorestevesremakeMod.LOGGER
						.warn("Failed to load dependency imediatesourceentity for procedure SlimeBombProjectileHitsLivingEntity!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		Entity entity = (Entity) dependencies.get("entity");
		Entity imediatesourceentity = (Entity) dependencies.get("imediatesourceentity");
		AxisAlignedBB attackRange = new AxisAlignedBB(x - (3 / 2d), y - (3 / 2), z - (3 / 2d), x + (3 / 2d), y + (3 / 2), z + (3 / 2d));
		for (LivingEntity livingentity : world.getEntitiesWithinAABB(LivingEntity.class, attackRange)) {
			if (livingentity != entity & !entity.isOnSameTeam(livingentity)) {
				livingentity.attackEntityFrom(DamageSource.causeMobDamage((LivingEntity) entity), (float) 2);
				livingentity.hurtResistantTime = 0;
				livingentity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, (int) 30, (int) 1, (true), (false)));
			}
		}
		world.playEvent(2001, new BlockPos(x, y, z), Block.getStateId(Blocks.SLIME_BLOCK.getDefaultState()));
		for (int i = 0; i < 4; i++)
			world.playEvent(2001, new BlockPos(x + (Math.random() - 0.5f) * 3, y + (Math.random() - 0.5f) * 3, z + (Math.random() - 0.5f) * 3),
					Block.getStateId(Blocks.SLIME_BLOCK.getDefaultState()));
	}
}
