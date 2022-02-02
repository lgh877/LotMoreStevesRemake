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
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.RenderType;

import net.mcreator.lotmorestevesremake.entity.GhosteveEntity;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class GhosteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(GhosteveEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new GhosteveModel(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/original_steve_half_translucent.png");
					}

					protected void applyRotations(LivingEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw,
							float partialTicks) {
						super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
						matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-entityLiving.rotationPitch * 0.5f));
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new BipedModel(0.5f), new BipedModel(1)));
				return customRender;
			});
		}
	}

	public static class GhosteveModel<T extends LivingEntity> extends BipedModel<T> {
		protected GhosteveModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
			super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
		}

		public GhosteveModel(float modelSize) {
			this(RenderType::getEntityTranslucent, modelSize, 0.0F, 64, 32);
		}

		public GhosteveModel(Function<ResourceLocation, RenderType> renderTypeIn, float modelSizeIn, float yOffsetIn, int textureWidthIn,
				int textureHeightIn) {
			super(renderTypeIn, modelSizeIn, 0.0F, 64, 32);
		}

		public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			MobEntity entityM = (MobEntity) entityIn;
			this.bipedHead.rotateAngleX *= 0.5f;
			if (entityIn.isSprinting()) {
				this.bipedRightArm.rotateAngleX -= (float) Math.PI / 2;
				this.bipedLeftArm.rotateAngleX -= (float) Math.PI / 2;
			}
			if (entityIn.isSwingInProgress)
				ModelHelper.func_239105_a_(this.bipedLeftArm, this.bipedRightArm, true, this.swingProgress, ageInTicks);
		}
	}
}
