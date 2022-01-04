package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.lotmorestevesremake.entity.BouncySteveEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class BouncySteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(BouncySteveEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new BipedModel(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/original_steve.png");
					}

					protected void preRenderCallback(LivingEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
						if (!entitylivingbaseIn.isAlive()) {
							float getDeathTime = 1 - MathHelper
									.clamp((float) ((BouncySteveEntity.CustomEntity) entitylivingbaseIn).getDeathTicksState() / 80f, 0, 0.6f);
							getDeathTime = getDeathTime * getDeathTime * getDeathTime;
							float size = this.getEntityModel().isChild ? 0.5f : 1f;
							this.shadowSize = 0.5f * size / MathHelper.clamp(getDeathTime, 0.3f, 1);
							matrixStackIn.scale(size * 0.7f / MathHelper.clamp(getDeathTime, 0.3f, 1), size * 1.4f * getDeathTime,
									size * 0.7f / MathHelper.clamp(getDeathTime, 0.3f, 1));
						} else {
							float getHurtTime = 0;
							float getSwingProgress = 1;
							float f3 = 1;
							float size = this.getEntityModel().isChild ? 0.5f : 1f;
							if (entitylivingbaseIn.isSwingInProgress) {
								getSwingProgress = 1 - this.getEntityModel().swingProgress;
								getSwingProgress = getSwingProgress * getSwingProgress * getSwingProgress;
								getSwingProgress = MathHelper.sin(getSwingProgress * (float) Math.PI) * 2f + 1;
							}
							if (entitylivingbaseIn.hurtTime > 0) {
								getHurtTime = ((float) entitylivingbaseIn.hurtTime / entitylivingbaseIn.maxHurtTime);
								getHurtTime = getHurtTime * getHurtTime * getHurtTime;
								float f2 = MathHelper.lerp(partialTickTime, getHurtTime, getHurtTime);
								f3 = 1.0F / (f2 + 1.0F);
							}
							this.shadowSize = 0.6f * size * getSwingProgress / f3;
							matrixStackIn.scale(size * getSwingProgress / f3, f3 * size * (1 / getSwingProgress), size * getSwingProgress / f3);
						}
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new BipedModel(0.5f), new BipedModel(1)));
				customRender.addLayer(new SteveGelLayer<>(customRender));
				return customRender;
			});
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class SteveGelLayer<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		private final EntityModel<T> steveModel = new BipedModel(0.4f);

		public SteveGelLayer(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!entitylivingbaseIn.isInvisible()) {
				this.getEntityModel().copyModelAttributesTo(this.steveModel);
				this.steveModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
				this.steveModel.setRotationAngles(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				IVertexBuilder ivertexbuilder = bufferIn
						.getBuffer(RenderType.getEntityTranslucent(new ResourceLocation("lotmorestevesremake:textures/steve_gel.png")));
				this.steveModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}
		}
	}
}
