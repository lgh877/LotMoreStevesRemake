
package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.HandSide;
import net.minecraft.util.Hand;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.lotmorestevesremake.entity.SpigEntity;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

import com.google.common.collect.ImmutableList;

@OnlyIn(Dist.CLIENT)
public class SpigRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(SpigEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new SpigModel(0), 0.5f) {
					{
						this.addLayer(new BipedArmorLayer(this, new SpigModel(0.1f), new SpigModel(0.5f)));
						this.addLayer(new ElytraLayer<>(this));
						this.addLayer(new HeldItemLayer<LivingEntity, SpigModel<LivingEntity>>(this) {
							public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
									LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
									float netHeadYaw, float headPitch) {
								matrixStackIn.translate(0, -0.2, 0);
								super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
										ageInTicks, netHeadYaw, headPitch);
							}
						});
					}

					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/original_steve_2.png");
					}
				};
				/*BipedRenderer customRender = new BipedRenderer(renderManager, new SpigModel(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/original_steve_2.png");
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new SpigModel(0.1f), new SpigModel(0.5f)));
				return customRender;
				*/
			});
		}
	}

	public static class SpigModel<T extends LivingEntity> extends BipedModel<T> {
		protected SpigModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
			super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
		}

		public SpigModel(float modelSize) {
			this(RenderType::getEntityCutoutNoCull, modelSize, 0.0F, 64, 32);
		}

		public SpigModel(Function<ResourceLocation, RenderType> renderTypeIn, float modelSizeIn, float yOffsetIn, int textureWidthIn,
				int textureHeightIn) {
			super(renderTypeIn, modelSizeIn, 0.0F, 64, 32);
			this.textureWidth = textureWidthIn;
			this.textureHeight = textureHeightIn;
			bipedHead = new ModelRenderer(this, 0, 0);
			bipedHead.setRotationPoint(0.0F, 16.0F + yOffsetIn, -7.0F);
			bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSizeIn);
			bipedHeadwear = new ModelRenderer(this, 32, 0);
			bipedHeadwear.setRotationPoint(0.0F, 16.0F + yOffsetIn, -7.0F);
			bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSizeIn + 0.5f);
			bipedBody = new ModelRenderer(this, 16, 16);
			bipedBody.setRotationPoint(0.0F, 15.0F + yOffsetIn, -5.0F);
			setRotationAngle(bipedBody, 1.5708F, 0.0F, 0.0F);
			bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, modelSizeIn);
			bipedRightArm = new ModelRenderer(this, 40, 16);
			bipedRightArm.setRotationPoint(-3.0F, 17.0F + yOffsetIn, -5.0F);
			bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSizeIn);
			bipedLeftArm = new ModelRenderer(this, 40, 16);
			bipedLeftArm.mirror = true;
			bipedLeftArm.setRotationPoint(3.0F, 17.0F + yOffsetIn, -5.0F);
			bipedLeftArm.addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSizeIn);
			bipedRightLeg = new ModelRenderer(this, 0, 16);
			bipedRightLeg.setRotationPoint(-3F, 17.0F + yOffsetIn, 7.0F);
			bipedRightLeg.setTextureOffset(0, 16).addBox(-2F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSizeIn);
			bipedLeftLeg = new ModelRenderer(this, 0, 16);
			bipedLeftLeg.mirror = true;
			bipedLeftLeg.setRotationPoint(3F, 17.0F + yOffsetIn, 7.0F);
			bipedLeftLeg.setTextureOffset(0, 16).addBox(-2F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSizeIn);
		}

		protected Iterable<ModelRenderer> getArmsAndLegs() {
			return ImmutableList.of(this.bipedRightArm, this.bipedLeftArm, this.bipedRightLeg, this.bipedLeftLeg);
		}

		protected Iterable<ModelRenderer> getHeadParts() {
			return ImmutableList.of(this.bipedHead, this.bipedHeadwear);
		}

		protected Iterable<ModelRenderer> getBodyParts() {
			return ImmutableList.of(this.bipedBody);
		}

		public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green,
				float blue, float alpha) {
			matrixStackIn.push();
			matrixStackIn.scale(1, 0.5f, 1);
			matrixStackIn.translate(0, 1.3, 0.02);
			this.getArmsAndLegs().forEach((p_228230_8_) -> {
				p_228230_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			});
			matrixStackIn.pop();
			matrixStackIn.push();
			matrixStackIn.scale(1, 1, 1);
			this.getHeadParts().forEach((p_228230_8_) -> {
				p_228230_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			});
			matrixStackIn.pop();
			matrixStackIn.push();
			matrixStackIn.scale(1.25f, 2, 1.33f);
			matrixStackIn.translate(0, -0.5, 0);
			this.getBodyParts().forEach((p_228230_8_) -> {
				p_228230_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
			});
			matrixStackIn.pop();
		}

		public void setRotationAngles(T e, float f, float f1, float f2, float f3, float f4) {
			float armSwing = MathHelper.cos(f * 0.6662F) * f1;
			ModelRenderer mainArm = this.getArmForSide(this.getMainHand(e));
			ModelRenderer offArm = this.getArmForSide(this.getOffHand(e));
			this.bipedHead.rotateAngleY = f3 / (180F / (float) Math.PI);
			this.bipedHead.rotateAngleX = f4 / (180F / (float) Math.PI);
			this.bipedLeftArm.rotateAngleX = armSwing;
			this.bipedRightArm.rotateAngleX = -armSwing;
			this.bipedLeftLeg.rotateAngleX = -armSwing;
			this.bipedRightLeg.rotateAngleX = armSwing;
			if (this.swingProgress > 0) {
				float a = 1 - this.swingProgress;
				a = a * a * a;
				mainArm.rotateAngleX = -MathHelper.sin(a * (float) Math.PI) * 2;
			}
			if (!e.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
				mainArm.rotateAngleX -= (float) Math.PI / 4;
			}
			if (!e.getHeldItem(Hand.OFF_HAND).isEmpty()) {
				offArm.rotateAngleX -= (float) Math.PI / 4;
			}
		}

		protected HandSide getOffHand(T entityIn) {
			HandSide handside = entityIn.getPrimaryHand();
			return entityIn.swingingHand == Hand.MAIN_HAND ? handside.opposite() : handside;
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}
	}
}
