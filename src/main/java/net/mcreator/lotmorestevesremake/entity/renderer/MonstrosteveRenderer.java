package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.lotmorestevesremake.entity.MonstrosteveEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class MonstrosteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(MonstrosteveEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelRedstone_Monstrosteve(), 3f) {
					//private ResourceLocation DECAY_TEXTURE = new ResourceLocation("lotmorestevesremake:textures/cloud.png");
					private final ModelRedstone_Monstrosteve model = new ModelRedstone_Monstrosteve();

					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/monstrosteve.png");
					}

					/*
										public void render(Entity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
												int packedLightIn) {
											/*
																	MonstrosteveEntity.CustomEntity entityM = (MonstrosteveEntity.CustomEntity) entityIn;
																	float deathTicks = entityM.getDeathAnimationScale(partialTicks);
																	float deathTicks2 = MathHelper.clamp((deathTicks - 160) / 40, 0, 1);
																	matrixStackIn.push();
																	matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
																	matrixStackIn.translate(0.0D, (double) -1.501F, 0.0D);
																	boolean flag = entityM.hurtTime > 0;
																	if (deathTicks > 160) {
																		//float f2 = (float) ((DgasfdaEntity.CustomEntity) entityIn).deathTicks / 40.0F;
																		this.model.setRotationAngles(entityIn, 0, 0, 0, 0, 0);
																		this.getEntityModel().copyModelAttributesTo(this.model);
																		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityAlpha(DECAY_TEXTURE, deathTicks2));
																		this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
																		IVertexBuilder ivertexbuilder1 = bufferIn.getBuffer(RenderType.getEntityDecal(this.getEntityTexture(entityIn)));
																		this.model.render(matrixStackIn, ivertexbuilder1, packedLightIn, OverlayTexture.getPackedUV(0.0F, flag), 1.0F, 1.0F, 1.0F,
																				1);
																	}
																	matrixStackIn.pop();
																	if (deathTicks < 160)
											
											super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
										}
					*/
					protected void preRenderCallback(LivingEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
						MonstrosteveEntity.CustomEntity entityM = (MonstrosteveEntity.CustomEntity) entitylivingbaseIn;
						float deathTicks = entityM.getDeathAnimationScale(partialTickTime);
						float deathTicks2 = MathHelper.clamp((deathTicks - 160) / 40, 0, 1);
						this.shadowSize = (1 - deathTicks2) * 3;
						matrixStackIn.scale((1 - deathTicks2) * 1.5f, (1 - deathTicks2) * 1.5f, (1 - deathTicks2) * 1.5f);
					}
				};
			});
		}
	}

	// Made with Blockbench 4.1.4
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class ModelRedstone_Monstrosteve extends EntityModel<Entity> {
		private final ModelRenderer whole;
		private final ModelRenderer body;
		private final ModelRenderer leftDispenser;
		private final ModelRenderer rightDispenser;
		private final ModelRenderer head;
		private final ModelRenderer jaw;
		private final ModelRenderer leftShoulder;
		private final ModelRenderer leftWrist;
		private final ModelRenderer rightShoulder;
		private final ModelRenderer rightWrist;
		private final ModelRenderer leftLeg;
		private final ModelRenderer rightLeg;

		public ModelRedstone_Monstrosteve() {
			textureWidth = 256;
			textureHeight = 256;
			whole = new ModelRenderer(this);
			whole.setRotationPoint(0.0F, 24.0F, 0.0F);
			whole.setTextureOffset(144, 84).addBox(-8.0F, -32.0F, -8.0F, 16.0F, 8.0F, 16.0F, 0.0F, false);
			body = new ModelRenderer(this);
			body.setRotationPoint(0.0F, -32.0F, 0.0F);
			whole.addChild(body);
			body.setTextureOffset(0, 0).addBox(-24.0F, -48.0F, -12.0F, 48.0F, 48.0F, 24.0F, 0.0F, false);
			leftDispenser = new ModelRenderer(this);
			leftDispenser.setRotationPoint(13.0F, -31.0F, 0.0F);
			body.addChild(leftDispenser);
			leftDispenser.setTextureOffset(168, 40).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
			rightDispenser = new ModelRenderer(this);
			rightDispenser.setRotationPoint(-13.0F, -31.0F, 0.0F);
			body.addChild(rightDispenser);
			rightDispenser.setTextureOffset(168, 40).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, true);
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, -35.0F, -11.0F);
			body.addChild(head);
			head.setTextureOffset(48, 143).addBox(-4.0F, -4.0F, -15.0F, 8.0F, 8.0F, 8.0F, 6.0F, false);
			jaw = new ModelRenderer(this);
			jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
			head.addChild(jaw);
			jaw.setTextureOffset(120, 0).addBox(-4.0F, -4.0F, -15.0F, 8.0F, 8.0F, 8.0F, 6.0F, false);
			jaw.setTextureOffset(168, 40).addBox(-8.0F, -8.0F, -19.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
			leftShoulder = new ModelRenderer(this);
			leftShoulder.setRotationPoint(36.0F, -37.0F, 0.0F);
			body.addChild(leftShoulder);
			leftShoulder.setTextureOffset(72, 84).addBox(-12.0F, -6.0F, -12.0F, 24.0F, 12.0F, 24.0F, 0.0F, false);
			leftShoulder.setTextureOffset(136, 131).addBox(-12.0F, -18.0F, -12.0F, 12.0F, 12.0F, 24.0F, 0.0F, false);
			leftShoulder.setTextureOffset(144, 0).addBox(-8.0F, 6.0F, -8.0F, 16.0F, 24.0F, 16.0F, 0.0F, false);
			leftWrist = new ModelRenderer(this);
			leftWrist.setRotationPoint(0.0F, 30.0F, 0.0F);
			leftShoulder.addChild(leftWrist);
			leftWrist.setTextureOffset(80, 120).addBox(-10.0F, 0.0F, -10.0F, 20.0F, 15.0F, 20.0F, 0.0F, false);
			rightShoulder = new ModelRenderer(this);
			rightShoulder.setRotationPoint(-36.0F, -37.0F, 0.0F);
			body.addChild(rightShoulder);
			rightShoulder.setTextureOffset(0, 72).addBox(-12.0F, -6.0F, -12.0F, 24.0F, 12.0F, 24.0F, 0.0F, false);
			rightShoulder.setTextureOffset(120, 48).addBox(0.0F, -18.0F, -12.0F, 12.0F, 12.0F, 24.0F, 0.0F, false);
			rightShoulder.setTextureOffset(0, 143).addBox(-8.0F, 6.0F, -8.0F, 16.0F, 24.0F, 16.0F, 0.0F, false);
			rightWrist = new ModelRenderer(this);
			rightWrist.setRotationPoint(0.0F, 30.0F, 0.0F);
			rightShoulder.addChild(rightWrist);
			rightWrist.setTextureOffset(0, 108).addBox(-10.0F, 0.0F, -10.0F, 20.0F, 15.0F, 20.0F, 0.0F, true);
			leftLeg = new ModelRenderer(this);
			leftLeg.setRotationPoint(16.0F, -24.0F, 0.0F);
			whole.addChild(leftLeg);
			leftLeg.setTextureOffset(128, 167).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 24.0F, 16.0F, 0.0F, false);
			rightLeg = new ModelRenderer(this);
			rightLeg.setRotationPoint(-16.0F, -24.0F, 0.0F);
			whole.addChild(rightLeg);
			rightLeg.setTextureOffset(64, 155).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 24.0F, 16.0F, 0.0F, false);
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
			MonstrosteveEntity.CustomEntity entityM = (MonstrosteveEntity.CustomEntity) e;
			float pi = (float) Math.PI;
			float headYaw = f3 / (180F / (float) Math.PI);
			float pitch = f4 / (180F / (float) Math.PI);
			float a1 = f2 - (float) entityM.ticksExisted;
			float a2 = entityM.getAnimationScale(a1, 0);
			float a3 = entityM.getAnimationScale(a1, 1);
			float deathTicks = entityM.getDeathAnimationScale(a1);
			float deathTicks2 = (float) Math.pow((double) MathHelper.clamp(deathTicks / 100, 0, 1), 18);
			{//basic settings
				this.leftLeg.setRotationPoint(16.0F, -24.0F, 0.0F);
				this.rightLeg.setRotationPoint(-16.0F, -24.0F, 0.0F);
				this.body.rotateAngleX = 0;
				this.body.rotationPointY = -32;
				this.head.rotationPointY = -35;
				this.leftDispenser.setRotationPoint(13.0F, -31.0F, 0.0F);
				this.rightDispenser.setRotationPoint(-13.0F, -31.0F, 0.0F);
			}
			this.leftDispenser.rotateAngleX = pitch * (a3 / 17);
			this.leftDispenser.rotateAngleY = headYaw * (a3 / 17);
			this.leftDispenser.rotationPointY -= a3;
			this.rightDispenser.rotateAngleX = pitch * (a3 / 17);
			this.rightDispenser.rotateAngleY = headYaw * (a3 / 17);
			this.rightDispenser.rotationPointY -= a3;
			this.jaw.rotationPointY = a2;
			this.whole.rotateAngleX = MathHelper.sin(deathTicks2 * pi / 2) * pi / 2;
			this.whole.rotateAngleZ = MathHelper.sin(f * 0.5f) * f1 * 0.1f;
			this.head.rotateAngleZ = MathHelper.clamp(deathTicks, 0, 40) * pi / 20;
			this.head.rotateAngleY = headYaw;
			this.head.rotateAngleX = pitch;
			if (e.isAlive()) {
				this.leftShoulder.rotateAngleX = (MathHelper.cos(f * 0.2f) - 2) * f1 * 0.2f;
				this.rightShoulder.rotateAngleX = (MathHelper.cos(f * 0.2f + pi) - 2) * f1 * 0.2f;
				this.body.rotateAngleY = 0;
			} else {
				this.leftShoulder.rotateAngleX = +MathHelper.clamp(deathTicks, 0, 40) * pi / 20 + MathHelper.sin(deathTicks2 * pi);
				this.rightShoulder.rotateAngleX = -MathHelper.clamp(deathTicks, 0, 40) * pi / 20 + MathHelper.sin(deathTicks2 * pi);
				this.body.rotateAngleY = MathHelper.clamp(deathTicks, 0, 40) * pi / 20;
			}
			this.leftShoulder.rotateAngleZ = -MathHelper.sin(deathTicks2 * pi / 2) * pi * 0.3f;
			this.rightShoulder.rotateAngleZ = MathHelper.sin(deathTicks2 * pi / 2) * pi * 0.3f;
			this.leftLeg.rotateAngleX = MathHelper.cos(f * 0.5f + pi * 1.4f) * f1 * 0.4f;
			this.leftLeg.rotationPointY += MathHelper.clamp(MathHelper.sin(f * 0.5f) * f1 * 5, -Float.POSITIVE_INFINITY, 0);
			this.leftLeg.rotationPointZ += MathHelper.cos(f * 0.5f + pi) * f1 * 5;
			this.rightLeg.rotateAngleX = MathHelper.cos(f * 0.5F + pi * 0.4f) * f1 * 0.4f;
			this.rightLeg.rotationPointY += MathHelper.clamp(MathHelper.sin(f * 0.5f + pi) * f1 * 5, -Float.POSITIVE_INFINITY, 0);
			this.rightLeg.rotationPointZ = MathHelper.cos(f * 0.5f) * f1 * 5;
			if (entityM.prevSwingProgress > 0) {
				if (entityM.getAttackState() == 1) {
					if (this.swingProgress < 0.8f) {
						float a = this.swingProgress * 1.25f;
						a = (float) Math.pow((double) a, 8);
						this.body.rotateAngleX += -MathHelper.sin(a * pi) * pi / 6 + MathHelper.sin(a * pi / 2) * pi / 2.4f;
						this.leftShoulder.rotateAngleX *= MathHelper.clamp((1 - MathHelper.sin(a * pi / 2) * 2), 0, 1);
						this.rightShoulder.rotateAngleX *= MathHelper.clamp((1 - MathHelper.sin(a * pi / 2) * 2), 0, 1);
						this.leftShoulder.rotateAngleX += -MathHelper.sin(a * pi) * 4 - MathHelper.sin(a * pi / 2) * pi * 0.6667f;
						this.rightShoulder.rotateAngleX += -MathHelper.sin(a * pi) * 4 - MathHelper.sin(a * pi / 2) * pi * 0.6667f;
					} else {
						float a = (1 - this.swingProgress) * 5f;
						a = (float) Math.pow((double) a, 3);
						this.body.rotateAngleX += MathHelper.sin(a * pi / 2) * pi / 2.4f;
						this.leftShoulder.rotateAngleX *= (1 - MathHelper.sin(a * pi / 2));
						this.rightShoulder.rotateAngleX *= (1 - MathHelper.sin(a * pi / 2));
						this.leftShoulder.rotateAngleX += -MathHelper.sin(a * pi / 2) * pi * 0.6667f;
						this.rightShoulder.rotateAngleX += this.leftShoulder.rotateAngleX;
					}
				} else if (entityM.getAttackState() == 2) {
					float a = (1 - this.swingProgress);
					a = (float) Math.pow((double) a, 2);
					this.body.rotationPointY += MathHelper.sin(a * pi) * 8;
					this.head.rotationPointY += MathHelper.sin(a * pi) * 5;
					this.leftShoulder.rotateAngleX += -MathHelper.sin(a * pi);
					this.leftShoulder.rotateAngleZ += -MathHelper.sin(a * pi);
					this.rightShoulder.rotateAngleX += -MathHelper.sin(a * pi);
					this.rightShoulder.rotateAngleZ += MathHelper.sin(a * pi);
				}
			}
		}
	}
}
