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

import net.mcreator.lotmorestevesremake.entity.StevagerEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class StevagerRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(StevagerEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelStevager(), 1.1f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/stevager.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 4.1.5
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelStevager extends EntityModel<StevagerEntity.CustomEntity> {
		private final ModelRenderer whole;
		private final ModelRenderer body;
		private final ModelRenderer neck;
		private final ModelRenderer head;
		private final ModelRenderer leftLeg;
		private final ModelRenderer rightLeg;
		private final ModelRenderer leftArm;
		private final ModelRenderer rightArm;

		public ModelStevager() {
			textureWidth = 128;
			textureHeight = 128;
			whole = new ModelRenderer(this);
			whole.setRotationPoint(0.0F, 24.0F, 0.0F);
			body = new ModelRenderer(this);
			body.setRotationPoint(0.0F, -24.0F, 4.0F);
			whole.addChild(body);
			body.setTextureOffset(0, 0).addBox(-5.0F, -5.0F, -11.0F, 10.0F, 12.0F, 17.0F, 0.0F, false);
			neck = new ModelRenderer(this);
			neck.setRotationPoint(0.0F, 1.0F, -1.0F);
			body.addChild(neck);
			neck.setTextureOffset(42, 17).addBox(-4.0F, -4.0F, -12.0F, 8.0F, 8.0F, 12.0F, 0.0F, false);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, 0.0F, -12.0F);
			neck.addChild(head);
			head.setTextureOffset(0, 29).addBox(-6.0F, -6.0F, -12.0F, 12.0F, 12.0F, 12.0F, 0.0F, false);
			leftLeg = new ModelRenderer(this);
			leftLeg.setRotationPoint(-3.0F, 4.0F, 8.0F);
			body.addChild(leftLeg);
			leftLeg.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			rightLeg = new ModelRenderer(this);
			rightLeg.setRotationPoint(3.0F, 4.0F, 8.0F);
			body.addChild(rightLeg);
			rightLeg.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
			leftArm = new ModelRenderer(this);
			leftArm.setRotationPoint(-9.0F, -24.0F, 4.0F);
			whole.addChild(leftArm);
			leftArm.setTextureOffset(40, 45).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 32.0F, 8.0F, 0.0F, false);
			rightArm = new ModelRenderer(this);
			rightArm.setRotationPoint(9.0F, -24.0F, 4.0F);
			whole.addChild(rightArm);
			rightArm.setTextureOffset(40, 45).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 32.0F, 8.0F, 0.0F, true);
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

		public void setRotationAngles(StevagerEntity.CustomEntity e, float f, float f1, float f2, float f3, float f4) {
			float pi = (float) Math.PI;
			float leg = MathHelper.cos(f * 0.6F) * f1;
			float legY = MathHelper.cos(f * 0.6F + pi * 0.4f) * f1;
			float partialTicks = f2 - e.ticksExisted;
			float headYaw = (f3 / (180F / pi));
			float pitch = f4 / (180F / pi);
			{//basic settings
				leftArm.setRotationPoint(-9.0F, -24.0F, 4.0F);
				rightArm.setRotationPoint(9.0F, -24.0F, 4.0F);
				neck.rotationPointZ = -1;
			}
			this.head.rotateAngleY = headYaw;
			this.head.rotateAngleX = pitch;
			this.neck.rotateAngleX = 0;
			this.neck.rotateAngleY = 0;
			this.leftLeg.rotateAngleX = (MathHelper.cos(f * 0.3662F) + 2) * f1 * 0.3f;
			this.rightLeg.rotateAngleX = (MathHelper.sin(f * 0.3662F) + 2) * f1 * 0.3f;
			this.leftArm.rotateAngleX = -leg / 2;
			this.leftArm.rotateAngleY = 0;
			this.leftArm.rotationPointY += MathHelper.clamp(-legY * 8, -7, 0);
			this.leftArm.rotationPointZ += MathHelper.clamp(-leg * 8, -3, 3);
			this.rightArm.rotateAngleX = leg / 2;
			this.rightArm.rotateAngleY = 0;
			this.rightArm.rotationPointY += MathHelper.clamp(legY * 8, -7, 0);
			this.rightArm.rotationPointZ += MathHelper.clamp(leg * 8, -3, 3);
			if (e.isSwingInProgress || e.prevSwingProgress > 0) {
				float a = 1 - e.getSwingProgress(partialTicks);
				a = a * a * a;
				float b = MathHelper.sin(a * pi);
				this.head.rotateAngleX *= 1 - b;
				this.head.rotateAngleY *= 1 - b;
				this.neck.rotateAngleX += pitch * b;
				this.neck.rotateAngleY += headYaw * b;
				this.neck.rotationPointZ += -b * 10;
			}
		}
	}
}
