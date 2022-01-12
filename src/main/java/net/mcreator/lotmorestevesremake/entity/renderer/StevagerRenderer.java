package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.model.RavagerModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.lotmorestevesremake.entity.StevagerEntity;

@OnlyIn(Dist.CLIENT)
public class StevagerRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(StevagerEntity.entity,
					renderManager -> new MobRenderer(renderManager, new RavagerModel(), 1.1f) {
						@Override
						public ResourceLocation getEntityTexture(Entity entity) {
							return new ResourceLocation("lotmorestevesremake:textures/stevager.png");
						}
					});
		}
	}
}
