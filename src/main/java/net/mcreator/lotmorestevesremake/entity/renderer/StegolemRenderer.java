package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.lotmorestevesremake.entity.StegolemEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class StegolemRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(StegolemEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new Modeliron_golem_with_joints(), 0.7f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/stefied_golem.png");
					}

					protected void applyRotations(LivingEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw,
							float partialTicks) {
						super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
						matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.clamp((float) entityLiving.getMotion().y, -1f, 0) * 180f));
					}
				};
			});
		}
	}

	// Made with Blockbench 3.9.3
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class Modeliron_golem_with_joints extends EntityModel<Entity> {
		private final ModelRenderer Whole;
		private final ModelRenderer lowerBody;
		private final ModelRenderer upperBody;
		private final ModelRenderer head;
		private final ModelRenderer leftShoulder;
		private final ModelRenderer leftWrist;
		private final ModelRenderer rightShoulder;
		private final ModelRenderer rightWrist;
		private final ModelRenderer leftThigh;
		private final ModelRenderer leftShank;
		private final ModelRenderer rightThigh;
		private final ModelRenderer rightShank;

		public Modeliron_golem_with_joints() {
			textureWidth = 128;
			textureHeight = 128;
			Whole = new ModelRenderer(this);
			Whole.setRotationPoint(0.0F, 24.0F, 0.0F);
			lowerBody = new ModelRenderer(this);
			lowerBody.setRotationPoint(0.0F, -16.0F, 0.0F);
			Whole.addChild(lowerBody);
			lowerBody.setTextureOffset(47, 0).addBox(-4.5F, -5.0F, -3.0F, 9.0F, 5.0F, 6.0F, 0.5F, false);
			upperBody = new ModelRenderer(this);
			upperBody.setRotationPoint(0.0F, -5.0F, 0.0F);
			lowerBody.addChild(upperBody);
			upperBody.setTextureOffset(0, 0).addBox(-9.0F, -12.0F, -6.0F, 18.0F, 12.0F, 11.0F, 0.0F, false);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, -10.0F, -2.0F);
			upperBody.addChild(head);
			head.setTextureOffset(0, 23).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, 0.0F, false);
			head.setTextureOffset(0, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F, 0.0F, false);
			leftShoulder = new ModelRenderer(this);
			leftShoulder.setRotationPoint(9.0F, -10.0F, 0.0F);
			upperBody.addChild(leftShoulder);
			leftShoulder.setTextureOffset(40, 44).addBox(0.0F, -2.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);
			leftWrist = new ModelRenderer(this);
			leftWrist.setRotationPoint(2.0F, 12.0F, 0.0F);
			leftShoulder.addChild(leftWrist);
			leftWrist.setTextureOffset(20, 44).addBox(-2.0F, 0.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);
			rightShoulder = new ModelRenderer(this);
			rightShoulder.setRotationPoint(-10.0F, -10.0F, 0.0F);
			upperBody.addChild(rightShoulder);
			rightShoulder.setTextureOffset(0, 41).addBox(-3.0F, -2.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);
			rightWrist = new ModelRenderer(this);
			rightWrist.setRotationPoint(-1.0F, 12.0F, 0.0F);
			rightShoulder.addChild(rightWrist);
			rightWrist.setTextureOffset(32, 23).addBox(-2.0F, 0.5F, -3.0F, 4.0F, 15.0F, 6.0F, 0.0F, false);
			leftThigh = new ModelRenderer(this);
			leftThigh.setRotationPoint(4.0F, -13.0F, 0.0F);
			Whole.addChild(leftThigh);
			leftThigh.setTextureOffset(0, 62).addBox(-2.5F, -3.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);
			leftShank = new ModelRenderer(this);
			leftShank.setRotationPoint(1.0F, 5.0F, 0.0F);
			leftThigh.addChild(leftShank);
			leftShank.setTextureOffset(60, 49).addBox(-3.5F, 0.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);
			rightThigh = new ModelRenderer(this);
			rightThigh.setRotationPoint(-4.0F, -13.0F, 0.0F);
			Whole.addChild(rightThigh);
			rightThigh.setTextureOffset(54, 36).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);
			rightShank = new ModelRenderer(this);
			rightShank.setRotationPoint(-1.0F, 5.0F, 0.0F);
			rightThigh.addChild(rightShank);
			rightShank.setTextureOffset(52, 23).addBox(-2.5F, 0.0F, -3.0F, 6.0F, 8.0F, 5.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			Whole.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4) {
			StegolemEntity.CustomEntity entityA = (StegolemEntity.CustomEntity) e;
			float bodyMove = (MathHelper.cos(f * 1.2f) / 10 + 0.2f) * f1;
			float a = this.swingProgress;
			a = (float) Math.pow(a, 4);
			this.Whole.rotateAngleX = bodyMove * 0.3f;
			this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
			this.head.rotateAngleX = f4 / (180F / (float) Math.PI) - bodyMove * 1.3f;
			upperBody.rotateAngleX = MathHelper.cos(f2 / 17) * (1 - f1) / 7 + bodyMove * f1;
			this.lowerBody.rotateAngleX = MathHelper.cos(f2 / 11) * (1 - f1) / 7 + bodyMove * f1;
			this.lowerBody.rotateAngleZ = MathHelper.cos(f2 / 13) / 7;
			this.rightThigh.rotateAngleX = MathHelper.cos(f * 0.5F) * f1;
			this.rightShank.rotateAngleX = (MathHelper.cos(f * 0.5F) + 1) * f1 * 0.8f;
			this.rightShoulder.rotateAngleX = MathHelper.cos(f2 / 5) * (1 - f1) / 5 + (MathHelper.cos(f * 0.6f) / 4 - (float) Math.PI / 2) * f1;
			this.rightWrist.rotateAngleX = MathHelper.cos(f2 / 7) * (1 - f1) / 5 + (MathHelper.cos(f * 0.6f) / 4 + (float) Math.PI / 5) * f1;
			this.leftThigh.rotateAngleX = MathHelper.cos(f * 0.5F) * -1.0F * f1;
			this.leftShank.rotateAngleX = (MathHelper.cos(f * 0.5f + (float) Math.PI) + 1) * f1 * 0.8f;
			this.leftShoulder.rotateAngleX = MathHelper.cos(f2 / 6) * (1 - f1) / 5
					+ (MathHelper.cos(f * 0.6f + (float) Math.PI / 2) / 4 - (float) Math.PI / 2) * f1;
			this.leftWrist.rotateAngleX = MathHelper.cos(f2 / 4.5f) * (1 - f1) / 5
					+ (MathHelper.cos(f * 0.6f + (float) Math.PI / 2) / 4 + (float) Math.PI / 5) * f1;
			if (entityA.isSwingInProgress) {
				head.rotateAngleX -= MathHelper.sin(a * (float) Math.PI) * (float) Math.PI * 0.4f;
				lowerBody.rotateAngleX += MathHelper.sin(a * (float) Math.PI) * (float) Math.PI / 5f;
				upperBody.rotateAngleX += MathHelper.sin(a * (float) Math.PI) * (float) Math.PI / 6;
				rightShoulder.rotateAngleX -= MathHelper.sin(a * (float) Math.PI) * (float) Math.PI / 3;
				rightWrist.rotateAngleX -= MathHelper.sin(a * (float) Math.PI) * (float) Math.PI / 3;
				leftShoulder.rotateAngleX -= MathHelper.sin(a * (float) Math.PI) * (float) Math.PI / 3;
				leftWrist.rotateAngleX -= MathHelper.sin(a * (float) Math.PI) * (float) Math.PI / 3;
			}
		}
	}
}
