package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.lotmorestevesremake.entity.FlateveEntity;

import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class FlateveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(FlateveEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new StecubeRenderer.Modelstecube(), 1.2f) {
					{
						this.addLayer(new HeadLayer<LivingEntity, StecubeRenderer.Modelstecube<LivingEntity>>(this));
						this.addLayer(new HeldItemLayer<LivingEntity, StecubeRenderer.Modelstecube<LivingEntity>>(this) {
							public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
									LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
									float netHeadYaw, float headPitch) {
								matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90));
								matrixStackIn.translate(0, -1.8f, 0.9);
								matrixStackIn.scale(1, 1, 2);
								super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
										ageInTicks, netHeadYaw, headPitch);
							}
						});
					}

					protected void preRenderCallback(LivingEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
						matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entitylivingbaseIn.rotationPitch * 0.7f));
						matrixStackIn.scale(3, 1, 3);
					}

					protected void applyRotations(LivingEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw,
							float partialTicks) {
						super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
						if (!((double) entityLiving.limbSwingAmount < 0.01D)) {
							float f = 13.0F;
							float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
							float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
							matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(10.5F * f2));
						}
					}

					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/original_steve.png");
					}
				};
			});
		}
	}
}
