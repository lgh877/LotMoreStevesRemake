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

import java.util.Map;

public class SlimeBombProjectileHitsLivingEntity2Procedure {
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
		if (dependencies.get("sourceentity") == null) {
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		Entity entity = (Entity) dependencies.get("entity");
		Entity sourceentity = (Entity) dependencies.get("sourceentity");
		AxisAlignedBB attackRange = new AxisAlignedBB(x - (3 / 2d), y - (3 / 2), z - (3 / 2d), x + (3 / 2d), y + (3 / 2), z + (3 / 2d));
		for (LivingEntity livingentity : world.getEntitiesWithinAABB(LivingEntity.class, attackRange)) {
			if (livingentity != sourceentity & !sourceentity.isOnSameTeam(livingentity)) {
				livingentity.attackEntityFrom(DamageSource.causeMobDamage((LivingEntity) sourceentity), (float) 2);
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
