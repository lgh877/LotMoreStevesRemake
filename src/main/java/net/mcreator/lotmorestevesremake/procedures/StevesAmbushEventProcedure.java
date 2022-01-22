package net.mcreator.lotmorestevesremake.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.village.VillageSiegeEvent;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.Entity;

import net.mcreator.lotmorestevesremake.entity.StevindicatorEntity;
import net.mcreator.lotmorestevesremake.entity.StevindicatorBigEntity;
import net.mcreator.lotmorestevesremake.entity.MicroSteveEntity;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeMod;

import java.util.Map;
import java.util.HashMap;

public class StevesAmbushEventProcedure {
	@Mod.EventBusSubscriber
	private static class GlobalTrigger {
		@SubscribeEvent
		public static void onVillageSiege(VillageSiegeEvent event) {
			PlayerEntity entity = event.getPlayer();
			double i = event.getAttemptedSpawnPos().x;
			double j = event.getAttemptedSpawnPos().y;
			double k = event.getAttemptedSpawnPos().z;
			IWorld world = event.getWorld();
			Map<String, Object> dependencies = new HashMap<>();
			dependencies.put("x", i);
			dependencies.put("y", j);
			dependencies.put("z", k);
			dependencies.put("world", world);
			dependencies.put("entity", entity);
			dependencies.put("event", event);
			executeProcedure(dependencies);
		}
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency world for procedure StevesAmbushEvent!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency x for procedure StevesAmbushEvent!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency y for procedure StevesAmbushEvent!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency z for procedure StevesAmbushEvent!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		for (int index0 = 0; index0 < (int) (0 + Math.random() * 3); index0++) {
			if (world instanceof ServerWorld) {
				Entity entityToSpawn = new StevindicatorBigEntity.CustomEntity(StevindicatorBigEntity.entity, (World) world);
				entityToSpawn.setLocationAndAngles(x, y, z, world.getRandom().nextFloat() * 360F, 0);
				if (entityToSpawn instanceof MobEntity)
					((MobEntity) entityToSpawn).onInitialSpawn((ServerWorld) world, world.getDifficultyForLocation(entityToSpawn.getPosition()),
							SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
				world.addEntity(entityToSpawn);
			}
		}
		for (int index1 = 0; index1 < (int) (2 + Math.random() * 5); index1++) {
			if (world instanceof ServerWorld) {
				Entity entityToSpawn = new StevindicatorEntity.CustomEntity(StevindicatorEntity.entity, (World) world);
				entityToSpawn.setLocationAndAngles(x, y, z, world.getRandom().nextFloat() * 360F, 0);
				if (entityToSpawn instanceof MobEntity)
					((MobEntity) entityToSpawn).onInitialSpawn((ServerWorld) world, world.getDifficultyForLocation(entityToSpawn.getPosition()),
							SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
				world.addEntity(entityToSpawn);
			}
		}
		for (int index2 = 0; index2 < (int) (4 + Math.random() * 10); index2++) {
			if (world instanceof ServerWorld) {
				Entity entityToSpawn = new MicroSteveEntity.CustomEntity(MicroSteveEntity.entity, (World) world);
				entityToSpawn.setLocationAndAngles(x, y, z, world.getRandom().nextFloat() * 360F, 0);
				if (entityToSpawn instanceof MobEntity)
					((MobEntity) entityToSpawn).onInitialSpawn((ServerWorld) world, world.getDifficultyForLocation(entityToSpawn.getPosition()),
							SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
				world.addEntity(entityToSpawn);
			}
		}
	}
}
