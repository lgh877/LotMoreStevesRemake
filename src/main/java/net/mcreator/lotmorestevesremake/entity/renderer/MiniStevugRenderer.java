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
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.lotmorestevesremake.entity.MiniStevugEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class MiniStevugRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(MiniStevugEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelMini_Stevug(), 0.5f) {
					{
						this.addLayer(new HeadLayer<MiniStevugEntity.CustomEntity, ModelMini_Stevug<MiniStevugEntity.CustomEntity>>(this));
						this.addLayer(new HeldItemLayer<MiniStevugEntity.CustomEntity, ModelMini_Stevug<MiniStevugEntity.CustomEntity>>(this) {
							public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
									MiniStevugEntity.CustomEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
									float ageInTicks, float netHeadYaw, float headPitch) {
								matrixStackIn.translate(0, -0.2, 0.5);
								super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
										ageInTicks, netHeadYaw, headPitch);
							}
						});
					}

					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/mini_stevug.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 4.1.5
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelMini_Stevug<T extends MiniStevugEntity.CustomEntity> extends EntityModel<T> implements IHasHead, IHasArm {
		private final ModelRenderer head;
		private final ModelRenderer leftEye;
		private final ModelRenderer rightEye;
		private final ModelRenderer leftFirstThigh;
		private final ModelRenderer leftFirstShank;
		private final ModelRenderer leftSecondThigh;
		private final ModelRenderer leftSecondShank;
		private final ModelRenderer leftThirdThigh;
		private final ModelRenderer leftThirdShank;
		private final ModelRenderer rightFirstThigh;
		private final ModelRenderer rightFirstShank;
		private final ModelRenderer rightSecondThigh;
		private final ModelRenderer rightSecondShank;
		private final ModelRenderer rightThirdThigh;
		private final ModelRenderer rightThirdShank;
		private final ModelRenderer leftHand;
		private final ModelRenderer rightHand;

		public ModelMini_Stevug() {
			textureWidth = 64;
			textureHeight = 64;
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, 20.0F, -1.0F);
			head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 10.0F, 0.0F, false);
			leftEye = new ModelRenderer(this);
			leftEye.setRotationPoint(2.0F, -4.0F, -4.0F);
			head.addChild(leftEye);
			leftEye.setTextureOffset(0, 0).addBox(-0.5F, -0.5F, -0.1F, 1.0F, 1.0F, 1.0F, 0.0F, false);
			rightEye = new ModelRenderer(this);
			rightEye.setRotationPoint(-2.0F, -4.0F, -4.0F);
			head.addChild(rightEye);
			rightEye.setTextureOffset(0, 0).addBox(-0.5F, -0.5F, -0.1F, 1.0F, 1.0F, 1.0F, 0.0F, true);
			leftFirstThigh = new ModelRenderer(this);
			leftFirstThigh.setRotationPoint(3.0F, 0.0F, -2.0F);
			head.addChild(leftFirstThigh);
			leftFirstThigh.setTextureOffset(0, 34).addBox(-2.0F, -2.0F, -2.5F, 6.0F, 4.0F, 4.0F, -1.0F, false);
			leftFirstShank = new ModelRenderer(this);
			leftFirstShank.setRotationPoint(3.0F, 0.0F, 0.0F);
			leftFirstThigh.addChild(leftFirstShank);
			leftFirstShank.setTextureOffset(20, 26).addBox(-1.0F, -2.0F, -2.5F, 6.0F, 4.0F, 4.0F, -1.0F, false);
			leftSecondThigh = new ModelRenderer(this);
			leftSecondThigh.setRotationPoint(3.0F, 0.0F, 1.0F);
			head.addChild(leftSecondThigh);
			leftSecondThigh.setTextureOffset(26, 0).addBox(-2.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, -1.0F, false);
			leftSecondShank = new ModelRenderer(this);
			leftSecondShank.setRotationPoint(3.0F, 0.0F, 0.0F);
			leftSecondThigh.addChild(leftSecondShank);
			leftSecondShank.setTextureOffset(0, 26).addBox(-1.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, -1.0F, false);
			leftThirdThigh = new ModelRenderer(this);
			leftThirdThigh.setRotationPoint(3.0F, 0.0F, 4.0F);
			head.addChild(leftThirdThigh);
			leftThirdThigh.setTextureOffset(20, 18).addBox(-2.0F, -2.0F, -1.5F, 6.0F, 4.0F, 4.0F, -1.0F, false);
			leftThirdShank = new ModelRenderer(this);
			leftThirdShank.setRotationPoint(3.0F, 0.0F, 0.0F);
			leftThirdThigh.addChild(leftThirdShank);
			leftThirdShank.setTextureOffset(0, 18).addBox(-1.0F, -2.0F, -1.5F, 6.0F, 4.0F, 4.0F, -1.0F, false);
			rightFirstThigh = new ModelRenderer(this);
			rightFirstThigh.setRotationPoint(-3.0F, 0.0F, -2.0F);
			head.addChild(rightFirstThigh);
			rightFirstThigh.setTextureOffset(0, 34).addBox(-4.0F, -2.0F, -2.5F, 6.0F, 4.0F, 4.0F, -1.0F, true);
			rightFirstShank = new ModelRenderer(this);
			rightFirstShank.setRotationPoint(-3.0F, 0.0F, 0.0F);
			rightFirstThigh.addChild(rightFirstShank);
			rightFirstShank.setTextureOffset(20, 26).addBox(-5.0F, -2.0F, -2.5F, 6.0F, 4.0F, 4.0F, -1.0F, true);
			rightSecondThigh = new ModelRenderer(this);
			rightSecondThigh.setRotationPoint(-3.0F, 0.0F, 1.0F);
			head.addChild(rightSecondThigh);
			rightSecondThigh.setTextureOffset(26, 0).addBox(-4.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, -1.0F, true);
			rightSecondShank = new ModelRenderer(this);
			rightSecondShank.setRotationPoint(-3.0F, 0.0F, 0.0F);
			rightSecondThigh.addChild(rightSecondShank);
			rightSecondShank.setTextureOffset(0, 26).addBox(-5.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, -1.0F, true);
			rightThirdThigh = new ModelRenderer(this);
			rightThirdThigh.setRotationPoint(-3.0F, 0.0F, 4.0F);
			head.addChild(rightThirdThigh);
			rightThirdThigh.setTextureOffset(20, 18).addBox(-4.0F, -2.0F, -1.5F, 6.0F, 4.0F, 4.0F, -1.0F, true);
			rightThirdShank = new ModelRenderer(this);
			rightThirdShank.setRotationPoint(-3.0F, 0.0F, 0.0F);
			rightThirdThigh.addChild(rightThirdShank);
			rightThirdShank.setTextureOffset(0, 18).addBox(-5.0F, -2.0F, -1.5F, 6.0F, 4.0F, 4.0F, -1.0F, true);
			leftHand = new ModelRenderer(this);
			leftHand.setRotationPoint(1.0F, 12.0F, -3.0F);
			rightHand = new ModelRenderer(this);
			rightHand.setRotationPoint(-1.0F, 12.0F, -3.0F);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			head.render(matrixStack, buffer, packedLight, packedOverlay);
			leftHand.render(matrixStack, buffer, packedLight, packedOverlay);
			rightHand.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(T e, float f, float f1, float f2, float f3, float f4) {
			float pi = (float) Math.PI;
			float Tmove1 = MathHelper.sin(f * 0.6F) * f1;
			float Tmove2 = MathHelper.sin(f * 0.6F + pi * 0.33f) * f1;
			float Tmove3 = MathHelper.sin(f * 0.6F + pi * 0.66f) * f1;
			float Smove1 = MathHelper.cos(f * 0.6F) * f1;
			float Smove2 = MathHelper.cos(f * 0.6F * pi * 0.33f) * f1;
			float Smove3 = MathHelper.cos(f * 0.6F * pi * 0.66f) * f1;
			//
			this.head.rotationPointY = 20 + MathHelper.sin(f * 1.2F) * f1;
			//
			this.leftEye.rotationPointX = 2 + (float) MathHelper.clamp(-f3 / 30, -0.5, 0.5);
			this.leftEye.rotationPointY = -4 + (float) MathHelper.clamp(f4 / 30, -0.5, 0.5);
			//
			this.rightEye.rotationPointX = -2 + (float) MathHelper.clamp(-f3 / 30, -0.5, 0.5);
			this.rightEye.rotationPointY = -4 + (float) MathHelper.clamp(f4 / 30, -0.5, 0.5);
			//
			this.leftHand.rotateAngleX = -pi / 4 + MathHelper.sin(f2 * 0.04f) * 0.2f;
			this.rightHand.rotateAngleX = -pi / 4 + MathHelper.cos(f2 * 0.04f) * 0.2f;
			if (this.swingProgress > 0) {
				float a = 1 - this.swingProgress;
				a = a * a * a;
				this.leftHand.rotateAngleX += MathHelper.sin(a * pi);
				this.rightHand.rotateAngleX += MathHelper.sin(a * pi);
			}
			//
			this.leftFirstThigh.rotateAngleY = Tmove1;
			this.leftFirstThigh.rotateAngleZ = Smove1;
			this.leftFirstShank.rotateAngleZ = pi / 2;
			//
			this.leftSecondThigh.rotateAngleY = -Tmove2;
			this.leftSecondThigh.rotateAngleZ = -Smove2;
			this.leftSecondShank.rotateAngleZ = pi / 2;
			//
			this.leftThirdThigh.rotateAngleY = Tmove3;
			this.leftThirdThigh.rotateAngleZ = Smove3;
			this.leftThirdShank.rotateAngleZ = pi / 2;
			//
			this.rightFirstThigh.rotateAngleY = -Smove1;
			this.rightFirstThigh.rotateAngleZ = -Tmove1;
			this.rightFirstShank.rotateAngleZ = -pi / 2;
			//
			this.rightSecondThigh.rotateAngleY = Smove2;
			this.rightSecondThigh.rotateAngleZ = Tmove2;
			this.rightSecondShank.rotateAngleZ = -pi / 2;
			//
			this.rightThirdThigh.rotateAngleY = -Smove3;
			this.rightThirdThigh.rotateAngleZ = -Tmove3;
			this.rightThirdShank.rotateAngleZ = -pi / 2;
		}

		public ModelRenderer getModelHead() {
			return this.head;
		}

		public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
			this.getArmForSide(sideIn).translateRotate(matrixStackIn);
		}

		protected ModelRenderer getArmForSide(HandSide side) {
			return side == HandSide.LEFT ? this.leftHand : this.rightHand;
		}
	}
}
