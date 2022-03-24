package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.HandSide;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.lotmorestevesremake.entity.HungryZteveEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class HungryZteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(HungryZteveEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelHungrySteve(), 0.7f) {
					{
						this.addLayer(new HeadLayer<HungryZteveEntity.CustomEntity, ModelHungrySteve<HungryZteveEntity.CustomEntity>>(this));
						this.addLayer(new HeldItemLayer<HungryZteveEntity.CustomEntity, ModelHungrySteve<HungryZteveEntity.CustomEntity>>(this) /*{
																																				public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
																																				HungryZteveEntity.CustomEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
																																				float ageInTicks, float netHeadYaw, float headPitch) {
																																				matrixStackIn.translate(0, -0.125, 0);
																																				}
																																				}*/);
					}

					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/hungrysteve.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 4.1.5
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelHungrySteve<T extends HungryZteveEntity.CustomEntity> extends EntityModel<T> implements IHasArm, IHasHead {
		private final ModelRenderer head;
		private final ModelRenderer leftArm;
		private final ModelRenderer rightArm;
		private final ModelRenderer upperBody;
		private final ModelRenderer lowerBody;
		private final ModelRenderer tongueRoot;
		private final ModelRenderer tongue1;
		private final ModelRenderer tongue2;
		private final ModelRenderer tongue3;
		private final ModelRenderer tongue4;
		private final ModelRenderer tongue5;
		private final ModelRenderer leftLeg;
		private final ModelRenderer rightLeg;

		public ModelHungrySteve() {
			textureWidth = 128;
			textureHeight = 128;
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, 7.0F, 0.0F);
			head.setTextureOffset(40, 14).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			leftArm = new ModelRenderer(this);
			leftArm.setRotationPoint(8.0F, 10.0F, 0.0F);
			leftArm.setTextureOffset(16, 44).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
			rightArm = new ModelRenderer(this);
			rightArm.setRotationPoint(-8.0F, 10.0F, 0.0F);
			rightArm.setTextureOffset(0, 44).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
			upperBody = new ModelRenderer(this);
			upperBody.setRotationPoint(0.0F, 17.0F, 6.0F);
			upperBody.setTextureOffset(0, 0).addBox(-6.0F, -10.0F, -12.0F, 12.0F, 10.0F, 12.0F, 0.01F, false);
			lowerBody = new ModelRenderer(this);
			lowerBody.setRotationPoint(0.0F, 17.0F, 0.0F);
			lowerBody.setTextureOffset(0, 22).addBox(-6.0F, -10.0F, -6.0F, 12.0F, 10.0F, 12.0F, 0.005F, false);
			tongueRoot = new ModelRenderer(this);
			tongueRoot.setRotationPoint(0.0F, -3.0F, 0.0F);
			lowerBody.addChild(tongueRoot);
			tongueRoot.setTextureOffset(48, 30).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
			tongue1 = new ModelRenderer(this);
			tongue1.setRotationPoint(0.0F, 0.0F, 0.0F);
			tongueRoot.addChild(tongue1);
			tongue1.setTextureOffset(48, 42).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			tongue2 = new ModelRenderer(this);
			tongue2.setRotationPoint(0.0F, 0.0F, 0.0F);
			tongue1.addChild(tongue2);
			tongue2.setTextureOffset(48, 42).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 12.0F, 4.0F, -0.25F, false);
			tongue3 = new ModelRenderer(this);
			tongue3.setRotationPoint(0.0F, 0.0F, 0.0F);
			tongue2.addChild(tongue3);
			tongue3.setTextureOffset(48, 42).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 12.0F, 4.0F, -0.5F, false);
			tongue4 = new ModelRenderer(this);
			tongue4.setRotationPoint(0.0F, 0.0F, 0.0F);
			tongue3.addChild(tongue4);
			tongue4.setTextureOffset(48, 42).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 12.0F, 4.0F, -0.75F, false);
			tongue5 = new ModelRenderer(this);
			tongue5.setRotationPoint(0.0F, 0.0F, 0.0F);
			tongue4.addChild(tongue5);
			tongue5.setTextureOffset(48, 42).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 12.0F, 4.0F, -1.0F, false);
			leftLeg = new ModelRenderer(this);
			leftLeg.setRotationPoint(3.0F, 17.0F, 0.0F);
			leftLeg.setTextureOffset(32, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, false);
			rightLeg = new ModelRenderer(this);
			rightLeg.setRotationPoint(-3.0F, 17.0F, 0.0F);
			rightLeg.setTextureOffset(36, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			head.render(matrixStack, buffer, packedLight, packedOverlay);
			leftArm.render(matrixStack, buffer, packedLight, packedOverlay);
			rightArm.render(matrixStack, buffer, packedLight, packedOverlay);
			upperBody.render(matrixStack, buffer, packedLight, packedOverlay);
			lowerBody.render(matrixStack, buffer, packedLight, packedOverlay);
			leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
			rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(T e, float f, float f1, float f2, float f3, float f4) {
			this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
			this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
			this.leftLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
			this.rightLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
			ModelHelper.func_239105_a_(this.leftArm, this.rightArm, e.isAggressive(), this.swingProgress, f2);
			ModelHelper.func_239101_a_(this.rightArm, this.leftArm, f2);
		}

		public ModelRenderer getModelHead() {
			return this.head;
		}

		public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
			this.getArmForSide(sideIn).translateRotate(matrixStackIn);
		}

		protected ModelRenderer getArmForSide(HandSide side) {
			return side == HandSide.LEFT ? this.leftArm : this.rightArm;
		}
	}
}
