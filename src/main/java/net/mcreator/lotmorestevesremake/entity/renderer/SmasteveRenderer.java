package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.lotmorestevesremake.entity.SmasteveEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class SmasteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(SmasteveEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelSmasteve(), 1f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/smasteve.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 4.1.3
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelSmasteve extends EntityModel<Entity> {
		private final ModelRenderer whole;
		private final ModelRenderer lowerBody;
		private final ModelRenderer cube_r1;
		private final ModelRenderer upperBody;
		private final ModelRenderer neck;
		private final ModelRenderer head;
		private final ModelRenderer eye;
		private final ModelRenderer rightShoulder;
		private final ModelRenderer rightWrist;
		private final ModelRenderer rightFist;
		private final ModelRenderer leftShoulder;
		private final ModelRenderer leftWrist;
		private final ModelRenderer rightRearThigh;
		private final ModelRenderer cube_r2;
		private final ModelRenderer rightRearShank;
		private final ModelRenderer rightFrontThigh;
		private final ModelRenderer rightFrontShank;
		private final ModelRenderer leftThigh;
		private final ModelRenderer cube_r3;
		private final ModelRenderer leftShank;

		public ModelSmasteve() {
			textureWidth = 128;
			textureHeight = 128;
			whole = new ModelRenderer(this);
			whole.setRotationPoint(0.0F, 24.0F, 0.0F);
			lowerBody = new ModelRenderer(this);
			lowerBody.setRotationPoint(2.0F, -14.0F, 0.0F);
			whole.addChild(lowerBody);
			cube_r1 = new ModelRenderer(this);
			cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
			lowerBody.addChild(cube_r1);
			setRotationAngle(cube_r1, 0.0F, 0.0F, -0.4363F);
			cube_r1.setTextureOffset(48, 19).addBox(-6.0F, -11.0F, -5.0F, 12.0F, 11.0F, 10.0F, 0.0F, false);
			upperBody = new ModelRenderer(this);
			upperBody.setRotationPoint(-4.0F, -10.0F, 0.0F);
			lowerBody.addChild(upperBody);
			upperBody.setTextureOffset(0, 0).addBox(-8.0F, -13.0F, -7.0F, 15.0F, 15.0F, 14.0F, 0.0F, false);
			neck = new ModelRenderer(this);
			neck.setRotationPoint(0.0F, -11.0F, -2.0F);
			upperBody.addChild(neck);
			neck.setTextureOffset(88, 10).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, -8.0F, 0.0F);
			neck.addChild(head);
			head.setTextureOffset(0, 29).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, 0.0F, false);
			eye = new ModelRenderer(this);
			eye.setRotationPoint(3.0F, -5.0F, -6.0F);
			head.addChild(eye);
			eye.setTextureOffset(0, 0).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
			rightShoulder = new ModelRenderer(this);
			rightShoulder.setRotationPoint(-8.0F, -10.0F, 0.0F);
			upperBody.addChild(rightShoulder);
			setRotationAngle(rightShoulder, 0.3491F, 0.0F, 0.0F);
			rightShoulder.setTextureOffset(32, 66).addBox(-7.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
			rightShoulder.setTextureOffset(0, 53).addBox(-11.0F, 0.0F, -4.0F, 8.0F, 20.0F, 8.0F, 0.0F, false);
			rightWrist = new ModelRenderer(this);
			rightWrist.setRotationPoint(-7.0F, 20.0F, 0.0F);
			rightShoulder.addChild(rightWrist);
			setRotationAngle(rightWrist, 0.5236F, 0.0F, 0.0F);
			rightWrist.setTextureOffset(81, 40).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 18.0F, 6.0F, 0.0F, false);
			rightFist = new ModelRenderer(this);
			rightFist.setRotationPoint(0.0F, 18.0F, 0.0F);
			rightWrist.addChild(rightFist);
			setRotationAngle(rightFist, 0.6981F, 0.0F, 0.0F);
			rightFist.setTextureOffset(58, 0).addBox(-5.0F, 0.0F, -4.0F, 10.0F, 8.0F, 8.0F, 0.0F, false);
			leftShoulder = new ModelRenderer(this);
			leftShoulder.setRotationPoint(7.0F, -10.0F, 0.0F);
			upperBody.addChild(leftShoulder);
			leftShoulder.setTextureOffset(99, 35).addBox(0.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, false);
			leftWrist = new ModelRenderer(this);
			leftWrist.setRotationPoint(2.0F, 5.0F, 0.0F);
			leftShoulder.addChild(leftWrist);
			leftWrist.setTextureOffset(92, 24).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, -1.0F, false);
			rightRearThigh = new ModelRenderer(this);
			rightRearThigh.setRotationPoint(-4.0F, -13.0F, 3.0F);
			whole.addChild(rightRearThigh);
			setRotationAngle(rightRearThigh, 0.0F, 0.5236F, 0.0F);
			cube_r2 = new ModelRenderer(this);
			cube_r2.setRotationPoint(-5.0F, 4.0F, 0.0F);
			rightRearThigh.addChild(cube_r2);
			setRotationAngle(cube_r2, 0.0F, 0.0F, 0.7854F);
			cube_r2.setTextureOffset(32, 86).addBox(-3.0F, -9.5F, -3.0F, 7.0F, 11.0F, 7.0F, -1.5F, false);
			rightRearShank = new ModelRenderer(this);
			rightRearShank.setRotationPoint(-3.0F, 4.0F, 0.0F);
			rightRearThigh.addChild(rightRearShank);
			rightRearShank.setTextureOffset(37, 42).addBox(-7.0F, -2.0F, -5.0F, 11.0F, 13.0F, 11.0F, -2.0F, false);
			rightFrontThigh = new ModelRenderer(this);
			rightFrontThigh.setRotationPoint(-4.0F, -14.0F, -4.0F);
			whole.addChild(rightFrontThigh);
			rightFrontThigh.setTextureOffset(60, 86).addBox(-3.0F, -1.5F, -3.0F, 6.0F, 9.0F, 6.0F, -1.5F, false);
			rightFrontShank = new ModelRenderer(this);
			rightFrontShank.setRotationPoint(0.0F, 6.0F, 0.0F);
			rightFrontThigh.addChild(rightFrontShank);
			rightFrontShank.setTextureOffset(72, 66).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 12.0F, 8.0F, -2.0F, false);
			leftThigh = new ModelRenderer(this);
			leftThigh.setRotationPoint(9.0F, -21.0F, 0.0F);
			whole.addChild(leftThigh);
			cube_r3 = new ModelRenderer(this);
			cube_r3.setRotationPoint(0.0F, 9.0F, 0.0F);
			leftThigh.addChild(cube_r3);
			setRotationAngle(cube_r3, 0.0F, 0.0F, -0.2618F);
			cube_r3.setTextureOffset(84, 86).addBox(-3.0F, -9.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F, false);
			leftShank = new ModelRenderer(this);
			leftShank.setRotationPoint(0.0F, 9.0F, 0.0F);
			leftThigh.addChild(leftShank);
			leftShank.setTextureOffset(0, 81).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 12.0F, 8.0F, 0.0F, false);
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
			SmasteveEntity.CustomEntity entityM = (SmasteveEntity.CustomEntity) e;
			int attackstate = entityM.getAttackState();
			float speed = (float) entityM.getAttributeValue(Attributes.MOVEMENT_SPEED) * 4;
			float pi = (float) Math.PI;
			float pitch = f4 / (180F / (float) Math.PI);
			float yawhead = f3 / (180F / (float) Math.PI);
			float moving = MathHelper.sin(f * 0.2f * speed) * f1;
			{//basic settings
				this.head.rotateAngleY = yawhead * 0.3f;
				this.head.rotateAngleX = pitch * 0.3f - moving * 0.1f;
				eye.rotationPointX = 3;
				eye.rotationPointY = -5;
				this.neck.rotateAngleY = yawhead * 0.3f;
				this.neck.rotateAngleX = pitch * 0.3f - moving * 0.05f;
				this.upperBody.rotateAngleY = yawhead * 0.2f;
				this.upperBody.rotateAngleX = pitch * 0.2f - moving * 0.05f;
				upperBody.rotationPointY = -10;
				this.lowerBody.rotateAngleY = yawhead * 0.2f;
				this.lowerBody.rotateAngleX = pitch * 0.2f - moving * 0.05f;
				lowerBody.rotationPointY = -14;
				rightShoulder.rotateAngleX = pi / 9 - pitch * 0.2f - moving * 0.1f;
				rightWrist.rotateAngleX = pi / 6 - pitch * 0.2f + moving * 0.15f;
				rightWrist.rotationPointY = 20;
				rightFist.rotateAngleX = pi / 4.5f + pitch * 0.4f - moving * 0.2f;
				whole.rotateAngleX = moving * 0.1f;
				whole.rotateAngleZ = MathHelper.cos(f * 0.2f) * f1 * 0.1f;
				rightFrontThigh.rotationPointY = -14;
				rightRearThigh.rotationPointY = -13;
				leftThigh.rotationPointY = -21;
			}
			eye.rotationPointX += MathHelper.clamp(-yawhead * 2 + (float) (Math.random() - 0.5) * 0.4f, -1, 1);
			eye.rotationPointY += MathHelper.clamp(pitch * 1.4f + (float) (Math.random() - 0.5) * 0.4f, -1, 1);
			this.leftShoulder.rotateAngleX = (MathHelper.cos(f * 0.3662F) + 2) * f1 * 0.3f;
			this.leftWrist.rotateAngleX = (MathHelper.cos(f * 0.3662F) + 2) * f1 * 0.3f;
			this.rightRearThigh.rotateAngleY = MathHelper.cos(f * 0.4F * speed) * 0.5f * f1 * speed + pi / 6;
			this.rightRearThigh.rotateAngleZ = MathHelper.sin(f * 0.5F * speed) * 0.5F * f1 * speed;
			this.rightRearShank.rotateAngleX = MathHelper.cos(f * 0.3F * speed) * 0.8F * f1 * speed;
			this.rightFrontThigh.rotateAngleX = MathHelper.cos(f * 0.35F * speed + pi * 0.33f) * 1.0F * f1 * speed;
			this.rightFrontShank.rotateAngleX = Math.abs(MathHelper.sin(f * 0.2F * speed + pi * 0.33f)) * 1.0F * speed * f1;
			this.leftThigh.rotateAngleX = MathHelper.sin(f * 0.3F * speed + pi * 0.66f) * 0.3f * f1 * speed;
			this.leftThigh.rotateAngleY = MathHelper.cos(f * 0.4F * speed + pi * 0.66f) * 0.5f * f1 * speed;
			this.leftThigh.rotateAngleZ = MathHelper.sin(f * 0.3F * speed + pi * 0.66f) * 0.5f * f1 * speed;
			this.leftShank.rotateAngleX = MathHelper.cos(f * 0.5F * speed + pi * 0.66f) * 0.3f * f1 * speed;
			if (entityM.isSwingInProgress) {
				if (attackstate == 1) {
					if (this.swingProgress < 0.8f) {
						float a = this.swingProgress * 1.25f;
						a = (float) Math.pow((double) a, 18);
						lowerBody.rotateAngleX -= MathHelper.sin(a * pi * 1.5f) * pi * 0.1f;
						lowerBody.rotateAngleY -= MathHelper.sin(a * pi * 1.5f) * pi * 0.2f;
						upperBody.rotateAngleX -= MathHelper.sin(a * pi * 1.5f) * pi * 0.1f;
						upperBody.rotateAngleY -= MathHelper.sin(a * pi * 1.5f) * pi * 0.2f;
						this.rightShoulder.rotateAngleX *= (1 - MathHelper.sin(a * pi / 2));
						this.rightWrist.rotateAngleX *= (1 - MathHelper.sin(a * pi / 2));
						this.rightFist.rotateAngleX *= (1 - MathHelper.sin(a * pi / 2));
						this.rightShoulder.rotateAngleX += MathHelper.sin(a * pi / 2) * pi * 2;
						this.rightWrist.rotateAngleX -= MathHelper.sin(a * pi / 2) * pi / 3;
						this.rightFist.rotateAngleX -= MathHelper.sin(a * pi / 2) * pi / 3;
					} else {
						float a = (1f - this.swingProgress) * 5f;
						a = (float) Math.pow((double) a, 3);
						lowerBody.rotateAngleX += MathHelper.sin(a * pi / 2) * pi * 0.1f;
						lowerBody.rotateAngleY += MathHelper.sin(a * pi / 2) * pi * 0.2f;
						upperBody.rotateAngleX += MathHelper.sin(a * pi / 2) * pi * 0.1f;
						upperBody.rotateAngleY += MathHelper.sin(a * pi / 2) * pi * 0.2f;
						this.rightShoulder.rotateAngleX *= (1 - MathHelper.sin(a * pi / 2));
						this.rightWrist.rotateAngleX *= (1 - MathHelper.sin(a * pi / 2));
						this.rightFist.rotateAngleX *= (1 - MathHelper.sin(a * pi / 2));
						this.rightShoulder.rotateAngleX += MathHelper.sin(a * pi / 2) * pi / 4.5f;
						this.rightWrist.rotateAngleX -= MathHelper.sin(a * pi / 2) * pi / 3;
						this.rightFist.rotateAngleX -= MathHelper.sin(a * pi / 2) * pi / 3;
					}
				}
			}
		}
	}
}
