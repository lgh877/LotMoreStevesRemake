package net.mcreator.lotmorestevesremake.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.Entity;

import net.mcreator.lotmorestevesremake.LotmorestevesremakeMod;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import java.util.Map;
import java.util.HashMap;

public class SdasaadsfProcedure {
	@Mod.EventBusSubscriber
	private static class GlobalTrigger {
		@SubscribeEvent
		public static void onEntityJoin(EntityJoinWorldEvent event) {
			World world = event.getWorld();
			Entity entity = event.getEntity();
			double i = entity.getPosX();
			double j = entity.getPosY();
			double k = entity.getPosZ();
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
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				LotmorestevesremakeMod.LOGGER.warn("Failed to load dependency entity for procedure Sdasaadsf!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		if (entity instanceof AbstractIllagerEntity
				&& !EntityTypeTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:we_are_steves")).contains(entity.getType())) {
			if (!entity.world.isRemote())
				((AbstractIllagerEntity) entity).targetSelector.addGoal(2,
						new NearestAttackableTargetGoal<>(((AbstractIllagerEntity) entity), AggressiveSteveEntity.class, true));
		}
	}
}
