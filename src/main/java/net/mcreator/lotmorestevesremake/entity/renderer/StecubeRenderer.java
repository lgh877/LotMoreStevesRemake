package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.HandSide;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.lotmorestevesremake.entity.StecubeEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class StecubeRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(StecubeEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new Modelstecube(), 0.4f) {
					{
						this.addLayer(new HeadLayer<LivingEntity, Modelstecube<LivingEntity>>(this));
						this.addLayer(new HeldItemLayer<LivingEntity, Modelstecube<LivingEntity>>(this) {
							public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
									LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
									float netHeadYaw, float headPitch) {
								matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90));
								matrixStackIn.translate(0, -1.8f, 0.9);
								super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
										ageInTicks, netHeadYaw, headPitch);
							}
						});
					}

					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/original_steve.png");
					}

					protected void preRenderCallback(LivingEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
						matrixStackIn.rotate(Vector3f.XP.rotationDegrees(entitylivingbaseIn.rotationPitch * 0.7f));
					}

					protected void applyRotations(LivingEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw,
							float partialTicks) {
						super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
						if (!((double) entityLiving.limbSwingAmount < 0.01D)) {
							float f = 13.0F;
							float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
							float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
							matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(10.5F * f2));
						}
					}
				};
			});
		}
	}

	// Made with Blockbench 4.1.3
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class Modelstecube<T extends LivingEntity> extends EntityModel<T> implements IHasArm, IHasHead {
		private final ModelRenderer head;
		private final ModelRenderer left_arm;
		private final ModelRenderer right_arm;
		private final ModelRenderer left_leg;
		private final ModelRenderer right_leg;

		public Modelstecube() {
			textureWidth = 64;
			textureHeight = 32;
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, 20.0F, 0.0F);
			head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			head.setTextureOffset(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);
			left_arm = new ModelRenderer(this);
			left_arm.setRotationPoint(5.0F, 17.0F, 0.0F);
			left_arm.setTextureOffset(40, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, -1.0F, true);
			right_arm = new ModelRenderer(this);
			right_arm.setRotationPoint(-5.0F, 17.0F, 0.0F);
			right_arm.setTextureOffset(40, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, -1.0F, false);
			left_leg = new ModelRenderer(this);
			left_leg.setRotationPoint(-1.9F, 20.0F, 0.0F);
			left_leg.setTextureOffset(0, 16).addBox(-2.1F, -1.0F, -2.0F, 4.0F, 6.0F, 4.0F, -1.0F, true);
			right_leg = new ModelRenderer(this);
			right_leg.setRotationPoint(1.9F, 20.0F, 0.0F);
			right_leg.setTextureOffset(0, 16).addBox(-1.9F, -1.0F, -2.0F, 4.0F, 6.0F, 4.0F, -1.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			head.render(matrixStack, buffer, packedLight, packedOverlay);
			left_arm.render(matrixStack, buffer, packedLight, packedOverlay);
			right_arm.render(matrixStack, buffer, packedLight, packedOverlay);
			left_leg.render(matrixStack, buffer, packedLight, packedOverlay);
			right_leg.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(T e, float f, float f1, float f2, float f3, float f4) {
			MobEntity entityM = (MobEntity) e;
			float a = 1 - this.swingProgress;
			a = a * a * a;
			this.head.rotateAngleY = f3 * 0.5f / (180F / (float) Math.PI);
			this.head.rotateAngleX = f4 * 0.3f / (180F / (float) Math.PI);
			this.right_arm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
			this.left_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
			this.left_arm.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
			this.right_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
			if (entityM.isSwingInProgress)
				this.head.rotateAngleX += MathHelper.sin(a * (float) Math.PI) * 1.2f;
		}

		public ModelRenderer getModelHead() {
			return this.head;
		}

		public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
			this.getArmForSide(sideIn).translateRotate(matrixStackIn);
		}

		protected ModelRenderer getArmForSide(HandSide side) {
			return side == HandSide.LEFT ? this.head : this.head;
		}
	}
}
