package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.lotmorestevesremake.entity.QuadropedalSteveEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class QuadropedalSteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(QuadropedalSteveEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelQuadropedal_Steve(), 1f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/the_legs.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 4.1.5
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelQuadropedal_Steve extends EntityModel<Entity> {
		private final ModelRenderer whole;
		private final ModelRenderer lowerBody;
		private final ModelRenderer upperBody;
		private final ModelRenderer head;
		private final ModelRenderer leftArm;
		private final ModelRenderer rightArm;
		private final ModelRenderer leftFrontThigh;
		private final ModelRenderer leftFrontShank;
		private final ModelRenderer rightFrontThigh;
		private final ModelRenderer rightFrontShank;
		private final ModelRenderer leftBackThigh;
		private final ModelRenderer leftBackShank;
		private final ModelRenderer rightBackThigh;
		private final ModelRenderer rightBackShank;

		public ModelQuadropedal_Steve() {
			textureWidth = 128;
			textureHeight = 128;
			whole = new ModelRenderer(this);
			whole.setRotationPoint(0.0F, 17.0F, 0.0F);
			lowerBody = new ModelRenderer(this);
			lowerBody.setRotationPoint(0.0F, 0.0F, 0.0F);
			whole.addChild(lowerBody);
			lowerBody.setTextureOffset(66, 0).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, 1.0F, false);
			upperBody = new ModelRenderer(this);
			upperBody.setRotationPoint(0.0F, -13.0F, 0.0F);
			lowerBody.addChild(upperBody);
			upperBody.setTextureOffset(24, 60).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, -12.0F, 0.0F);
			upperBody.addChild(head);
			head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			leftArm = new ModelRenderer(this);
			leftArm.setRotationPoint(6.0F, -10.0F, 0.0F);
			upperBody.addChild(leftArm);
			leftArm.setTextureOffset(52, 66).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);
			rightArm = new ModelRenderer(this);
			rightArm.setRotationPoint(-6.0F, -10.0F, 0.0F);
			upperBody.addChild(rightArm);
			rightArm.setTextureOffset(66, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, true);
			leftFrontThigh = new ModelRenderer(this);
			leftFrontThigh.setRotationPoint(3.0F, 0.0F, -2.0F);
			whole.addChild(leftFrontThigh);
			setRotationAngle(leftFrontThigh, -2.0944F, -0.7854F, 0.0F);
			leftFrontThigh.setTextureOffset(0, 60).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 16.0F, 6.0F, 0.0F, false);
			leftFrontShank = new ModelRenderer(this);
			leftFrontShank.setRotationPoint(0.0F, 16.0F, -1.0F);
			leftFrontThigh.addChild(leftFrontShank);
			setRotationAngle(leftFrontShank, -1.0472F, 0.0F, 0.0F);
			leftFrontShank.setTextureOffset(48, 44).addBox(-3.0F, -16.0F, -3.0F, 6.0F, 16.0F, 6.0F, 0.1F, false);
			rightFrontThigh = new ModelRenderer(this);
			rightFrontThigh.setRotationPoint(-3.0F, 0.0F, -2.0F);
			whole.addChild(rightFrontThigh);
			setRotationAngle(rightFrontThigh, -2.0944F, 0.7854F, 0.0F);
			rightFrontThigh.setTextureOffset(48, 22).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 16.0F, 6.0F, 0.0F, false);
			rightFrontShank = new ModelRenderer(this);
			rightFrontShank.setRotationPoint(0.0F, 16.0F, -1.0F);
			rightFrontThigh.addChild(rightFrontShank);
			setRotationAngle(rightFrontShank, -1.0472F, 0.0F, 0.0F);
			rightFrontShank.setTextureOffset(42, 0).addBox(-3.0F, -16.0F, -3.0F, 6.0F, 16.0F, 6.0F, 0.1F, false);
			leftBackThigh = new ModelRenderer(this);
			leftBackThigh.setRotationPoint(3.0F, 0.0F, 2.0F);
			whole.addChild(leftBackThigh);
			setRotationAngle(leftBackThigh, 2.0944F, 0.7854F, 0.0F);
			leftBackThigh.setTextureOffset(24, 38).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 16.0F, 6.0F, 0.0F, false);
			leftBackShank = new ModelRenderer(this);
			leftBackShank.setRotationPoint(0.0F, 16.0F, 1.0F);
			leftBackThigh.addChild(leftBackShank);
			setRotationAngle(leftBackShank, 1.0472F, 0.0F, 0.0F);
			leftBackShank.setTextureOffset(0, 38).addBox(-3.0F, -16.0F, -3.0F, 6.0F, 16.0F, 6.0F, 0.1F, false);
			rightBackThigh = new ModelRenderer(this);
			rightBackThigh.setRotationPoint(-3.0F, 0.0F, 2.0F);
			whole.addChild(rightBackThigh);
			setRotationAngle(rightBackThigh, 2.0944F, -0.7854F, 0.0F);
			rightBackThigh.setTextureOffset(24, 16).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 16.0F, 6.0F, 0.0F, false);
			rightBackShank = new ModelRenderer(this);
			rightBackShank.setRotationPoint(0.0F, 16.0F, 1.0F);
			rightBackThigh.addChild(rightBackShank);
			setRotationAngle(rightBackShank, 1.0472F, 0.0F, 0.0F);
			rightBackShank.setTextureOffset(0, 16).addBox(-3.0F, -16.0F, -3.0F, 6.0F, 16.0F, 6.0F, 0.1F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			whole.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4) {
			float pi = (float) Math.PI;
			this.rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
			this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
			this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
			this.leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
			this.leftFrontThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1 - pi * 0.667f;
			this.rightFrontThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1 - pi * 0.667f;
			this.leftBackThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1 + pi * 0.667f;
			this.rightBackThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1 + pi * 0.667f;
		}
	}
}
