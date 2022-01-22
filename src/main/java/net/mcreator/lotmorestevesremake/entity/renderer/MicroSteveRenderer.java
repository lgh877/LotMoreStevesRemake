package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.RenderType;

import net.mcreator.lotmorestevesremake.entity.MicroSteveEntity;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class MicroSteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(MicroSteveEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new MicroSteveModel(0), 0.325f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/original_steve.png");
					}

					protected void preRenderCallback(LivingEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
						matrixStackIn.scale(0.5f, 0.5f, 0.5f);
						if (entitylivingbaseIn.isSprinting()) {
							matrixStackIn.rotate(Vector3f.XP.rotationDegrees(30 * entitylivingbaseIn.limbSwingAmount));
						}
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new BipedModel(0.5f), new BipedModel(1)));
				return customRender;
			});
		}
	}

	public static class MicroSteveModel<T extends LivingEntity> extends BipedModel<T> {
		protected MicroSteveModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
			super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
		}

		public MicroSteveModel(float modelSize) {
			this(RenderType::getEntityCutoutNoCull, modelSize, 0.0F, 64, 32);
		}

		public MicroSteveModel(Function<ResourceLocation, RenderType> renderTypeIn, float modelSizeIn, float yOffsetIn, int textureWidthIn,
				int textureHeightIn) {
			super(renderTypeIn, modelSizeIn, 0.0F, 64, 32);
		}

		public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			MobEntity entityM = (MobEntity) entityIn;
			if (entityM.isSprinting()) {
				this.bipedHead.rotateAngleX -= (float) Math.PI * 0.33f * limbSwingAmount;
				this.bipedRightArm.rotateAngleX -= (float) Math.PI * 0.8f * limbSwingAmount;
				this.bipedLeftArm.rotateAngleX -= (float) Math.PI * 0.8f * limbSwingAmount;
			}
		}
	}
}
