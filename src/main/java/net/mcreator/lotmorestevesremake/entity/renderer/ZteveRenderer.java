package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.RenderType;

import net.mcreator.lotmorestevesremake.entity.ZteveEntity;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class ZteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(ZteveEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new ZteveModel(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/zteve.png");
					}

					protected void preRenderCallback(LivingEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
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

	public static class ZteveModel<T extends LivingEntity> extends BipedModel<T> {
		protected ZteveModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
			super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
		}

		public ZteveModel(float modelSize) {
			this(RenderType::getEntityCutoutNoCull, modelSize, 0.0F, 64, 32);
		}

		public ZteveModel(Function<ResourceLocation, RenderType> renderTypeIn, float modelSizeIn, float yOffsetIn, int textureWidthIn,
				int textureHeightIn) {
			super(renderTypeIn, modelSizeIn, 0.0F, 64, 32);
		}

		public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			MobEntity entityM = (MobEntity) entityIn;
			float a = 1 - this.swingProgress;
			a = a * a * a;
			if (entityM.isSprinting()) {
				this.bipedHead.rotateAngleX -= (float) Math.PI / 6 * limbSwingAmount;
				this.bipedRightArm.rotateAngleX *= 1.5f;
				this.bipedLeftArm.rotateAngleX *= 1.5f;
				this.bipedRightLeg.rotateAngleX *= 1.5f;
				this.bipedLeftLeg.rotateAngleX *= 1.5f;
			} else if (!entityM.isSwingInProgress) {
				this.bipedRightArm.rotateAngleX *= 0.3f;
				this.bipedLeftArm.rotateAngleX *= 0.3f;
			}
			this.bipedHead.rotateAngleX -= MathHelper.sin(a * (float) Math.PI * 12) / 6;
			this.bipedRightArm.rotateAngleX -= (float) Math.PI * 0.4f + MathHelper.sin(a * (float) Math.PI * 2);
			this.bipedLeftArm.rotateAngleX -= (float) Math.PI * 0.4f + MathHelper.sin(a * (float) Math.PI * 2);
		}
	}
}
