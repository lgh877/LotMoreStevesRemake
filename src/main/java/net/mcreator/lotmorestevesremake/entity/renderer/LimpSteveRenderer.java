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
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.lotmorestevesremake.entity.LimpSteveEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class LimpSteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(LimpSteveEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelLimp_Steve(), 0.8f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/limp_steve.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 4.1.5
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelLimp_Steve extends EntityModel<LimpSteveEntity.CustomEntity> {
		private final ModelRenderer whole;
		private final ModelRenderer[] back = new ModelRenderer[4];
		private final ModelRenderer[] leftArm = new ModelRenderer[5];
		private final ModelRenderer[] rightArm = new ModelRenderer[5];
		private final ModelRenderer head;
		private final ModelRenderer leftThigh;
		private final ModelRenderer leftShank;
		private final ModelRenderer leftFoot;
		private final ModelRenderer rightThigh;
		private final ModelRenderer rightShank;
		private final ModelRenderer rightFoot;

		public ModelLimp_Steve() {
			textureWidth = 256;
			textureHeight = 256;
			whole = new ModelRenderer(this);
			whole.setRotationPoint(0.0F, 24.0F, 0.0F);
			back[0] = new ModelRenderer(this);
			back[0].setRotationPoint(0.0F, -28.0F, 0.0F);
			whole.addChild(back[0]);
			back[0].setTextureOffset(94, 94).addBox(-8.0F, -20.0F, -5.0F, 16.0F, 20.0F, 10.0F, 0.0F, false);
			back[1] = new ModelRenderer(this);
			back[1].setRotationPoint(0.0F, -20.0F, 0.0F);
			back[0].addChild(back[1]);
			back[1].setTextureOffset(80, 0).addBox(-8.0F, -20.0F, -5.0F, 16.0F, 20.0F, 10.0F, 0.0F, false);
			back[2] = new ModelRenderer(this);
			back[2].setRotationPoint(0.0F, -20.0F, 0.0F);
			back[1].addChild(back[2]);
			back[2].setTextureOffset(52, 74).addBox(-8.0F, -20.0F, -5.0F, 16.0F, 20.0F, 10.0F, 0.0F, false);
			back[3] = new ModelRenderer(this);
			back[3].setRotationPoint(0.0F, -20.0F, 0.0F);
			back[2].addChild(back[3]);
			back[3].setTextureOffset(0, 74).addBox(-8.0F, -20.0F, -5.0F, 16.0F, 20.0F, 10.0F, 0.0F, false);
			leftArm[0] = new ModelRenderer(this);
			leftArm[0].setRotationPoint(14.0F, -9.0F, 0.0F);
			back[3].addChild(leftArm[0]);
			leftArm[0].setTextureOffset(52, 40).addBox(-6.0F, -6.0F, -7.0F, 12.0F, 20.0F, 14.0F, 0.0F, false);
			leftArm[1] = new ModelRenderer(this);
			leftArm[1].setRotationPoint(0.0F, 14.0F, 0.0F);
			leftArm[0].addChild(leftArm[1]);
			leftArm[1].setTextureOffset(0, 134).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);
			leftArm[2] = new ModelRenderer(this);
			leftArm[2].setRotationPoint(0.0F, 20.0F, 0.0F);
			leftArm[1].addChild(leftArm[2]);
			leftArm[2].setTextureOffset(132, 0).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);
			leftArm[3] = new ModelRenderer(this);
			leftArm[3].setRotationPoint(0.0F, 20.0F, 0.0F);
			leftArm[2].addChild(leftArm[3]);
			leftArm[3].setTextureOffset(98, 124).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);
			leftArm[4] = new ModelRenderer(this);
			leftArm[4].setRotationPoint(0.0F, 20.0F, 0.0F);
			leftArm[3].addChild(leftArm[4]);
			leftArm[4].setTextureOffset(0, 104).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);
			rightArm[0] = new ModelRenderer(this);
			rightArm[0].setRotationPoint(-14.0F, -9.0F, 0.0F);
			back[3].addChild(rightArm[0]);
			rightArm[0].setTextureOffset(0, 40).addBox(-6.0F, -6.0F, -7.0F, 12.0F, 20.0F, 14.0F, 0.0F, false);
			rightArm[1] = new ModelRenderer(this);
			rightArm[1].setRotationPoint(0.0F, 14.0F, 0.0F);
			rightArm[0].addChild(rightArm[1]);
			rightArm[1].setTextureOffset(62, 124).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);
			rightArm[2] = new ModelRenderer(this);
			rightArm[2].setRotationPoint(0.0F, 20.0F, 0.0F);
			rightArm[1].addChild(rightArm[2]);
			rightArm[2].setTextureOffset(104, 60).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);
			rightArm[3] = new ModelRenderer(this);
			rightArm[3].setRotationPoint(0.0F, 20.0F, 0.0F);
			rightArm[2].addChild(rightArm[3]);
			rightArm[3].setTextureOffset(36, 104).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);
			rightArm[4] = new ModelRenderer(this);
			rightArm[4].setRotationPoint(0.0F, 20.0F, 0.0F);
			rightArm[3].addChild(rightArm[4]);
			rightArm[4].setTextureOffset(104, 30).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, -20.0F, 0.0F);
			back[3].addChild(head);
			head.setTextureOffset(0, 0).addBox(-10.0F, -20.0F, -11.0F, 20.0F, 20.0F, 20.0F, 0.0F, false);
			leftThigh = new ModelRenderer(this);
			leftThigh.setRotationPoint(5.0F, -28.0F, 0.0F);
			whole.addChild(leftThigh);
			leftThigh.setTextureOffset(134, 142).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);
			leftShank = new ModelRenderer(this);
			leftShank.setRotationPoint(0.0F, 12.0F, 0.0F);
			leftThigh.addChild(leftShank);
			leftShank.setTextureOffset(140, 30).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);
			leftFoot = new ModelRenderer(this);
			leftFoot.setRotationPoint(0.0F, 4.0F, 0.0F);
			leftShank.addChild(leftFoot);
			leftFoot.setTextureOffset(130, 49).addBox(-3.0F, 8.0F, -8.0F, 6.0F, 4.0F, 11.0F, 0.0F, false);
			rightThigh = new ModelRenderer(this);
			rightThigh.setRotationPoint(-5.0F, -28.0F, 0.0F);
			whole.addChild(rightThigh);
			rightThigh.setTextureOffset(134, 124).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);
			rightShank = new ModelRenderer(this);
			rightShank.setRotationPoint(0.0F, 12.0F, 0.0F);
			rightThigh.addChild(rightShank);
			rightShank.setTextureOffset(36, 134).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);
			rightFoot = new ModelRenderer(this);
			rightFoot.setRotationPoint(0.0F, 4.0F, 0.0F);
			rightShank.addChild(rightFoot);
			rightFoot.setTextureOffset(129, 79).addBox(-3.0F, 8.0F, -8.0F, 6.0F, 4.0F, 11.0F, 0.0F, false);
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

		public void setRotationAngles(LimpSteveEntity.CustomEntity e, float f, float f1, float f2, float f3, float f4) {
			float partialTicks = f2 - (float) e.ticksExisted;
			float pi = (float) Math.PI;
			float degToRad = 180F / pi;
			this.whole.rotateAngleX = (MathHelper.cos(f) + 1) * f1 * 0.1f;
			this.head.rotateAngleX = (f4 / degToRad);
			this.head.rotateAngleY = (f3 / degToRad) * 0.2f;
			for (int i = 0; i < 4; i++) {
				this.back[i].rotateAngleX = MathHelper.clamp((f4 / degToRad) * 0.5f, 0, pi);
				//(e.getPitch(partialTicks, i) / degToRad) * 0.5f;
				this.back[i].rotateAngleY = (f3 / degToRad) * 0.2f;
				//-(e.getHeadYaw(partialTicks, i) / degToRad) * 0.1f;
			}
			this.leftArm[0].rotateAngleX = MathHelper.clamp(f4 / degToRad, -pi, 0);
			this.rightArm[0].rotateAngleX = MathHelper.clamp(f4 / degToRad, -pi, 0);
			for (int i = 0; i < 4; i++) {
				this.leftArm[i + 1].rotateAngleX = -Math.abs((f4 / degToRad) * 0.6f);
				this.rightArm[i + 1].rotateAngleX = -Math.abs((f4 / degToRad) * 0.6f);
			}
			if (e.swingProgress > 0 || e.prevSwingProgress2[4] > 0) {
				for (int i = 0; i < 5; i++) {
					float a = e.getSwingProgress(partialTicks, i);
					//
					a = a * a * a;
					this.leftArm[i].rotateAngleX += MathHelper.sin(a * pi * 2);
					this.leftArm[i].rotateAngleZ = -MathHelper.sin(a * pi * 2) * 0.5f;
					this.rightArm[i].rotateAngleX += MathHelper.sin(a * pi * 2);
					this.rightArm[i].rotateAngleZ = MathHelper.sin(a * pi * 2) * 0.5f;
					if (i > 0)
						this.back[i - 1].rotateAngleX += -MathHelper.sin(a * pi * 2) * 0.4f;
				}
			} else {
				for (int i = 0; i < 5; i++) {
					this.leftArm[i].rotateAngleZ = 0;
					this.rightArm[i].rotateAngleZ = 0;
				}
			}
			for (int i = 0; i < 5; i++) {
				ModelHelper.func_239101_a_(this.rightArm[i], this.leftArm[i], f2 * 2);
				if (i > 0) {
					this.back[i - 1].rotateAngleX = MathHelper.sin(f2 * 0.03f + pi * (i - 1) * 0.2f) * 0.1f;
					this.back[i - 1].rotateAngleY = MathHelper.sin(f2 * 0.01f + pi * (i - 1) * 0.2f) * 0.1f;
					this.back[i - 1].rotateAngleZ = MathHelper.cos(f2 * 0.05f + pi * (i - 1) * 0.2f) * 0.1f;
				}
			}
			this.leftThigh.rotateAngleX = MathHelper.cos(f * 0.5F) * f1 * 0.6f;
			this.leftShank.rotateAngleX = (MathHelper.cos(f * 0.5f + pi * 0.4f) + 1) * f1 * 0.2f;
			this.rightThigh.rotateAngleX = MathHelper.cos(f * 0.5F + pi) * f1 * 0.6f;
			this.rightShank.rotateAngleX = (MathHelper.cos(f * 0.5F - pi * 0.4f) + 1) * f1 * 0.2f;
		}
	}
}
