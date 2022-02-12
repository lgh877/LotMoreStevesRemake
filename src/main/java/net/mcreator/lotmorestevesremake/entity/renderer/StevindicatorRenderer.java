package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.HandSide;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.lotmorestevesremake.entity.StevindicatorEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class StevindicatorRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(StevindicatorEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new Modelstev_illager(), 0.5f) {
					{
						this.addLayer(new GlowingLayer<>(this));
						this.addLayer(new HeldItemLayer<StevindicatorEntity.CustomEntity, Modelstev_illager<StevindicatorEntity.CustomEntity>>(this) {
							public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
									StevindicatorEntity.CustomEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
									float ageInTicks, float netHeadYaw, float headPitch) {
								//StevindicatorEntity.CustomEntity entityS = (StevindicatorEntity.CustomEntity) entitylivingbaseIn;
								if ((entitylivingbaseIn.isSwingInProgress || entitylivingbaseIn.limbSwingAmount > 0.13f)
										&& entitylivingbaseIn.isAlive() || entitylivingbaseIn.getDancingTick() > 0) {
									super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
											ageInTicks, netHeadYaw, headPitch);
								}
							}
						});
						this.addLayer(new HeadLayer<StevindicatorEntity.CustomEntity, Modelstev_illager<StevindicatorEntity.CustomEntity>>(this));
					}

					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/stev_illager.png");
					}
				};
			});
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class GlowingLayer<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public GlowingLayer(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			ResourceLocation Texture = new ResourceLocation("lotmorestevesremake:textures/stev_illager_cloth1.png");
			StevindicatorEntity.CustomEntity entityM = (StevindicatorEntity.CustomEntity) entitylivingbaseIn;
			Item getArmor = entityM.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem();
			float color[] = {1, 1, 1};
			if (getArmor == Items.NETHERITE_CHESTPLATE)
				Texture = new ResourceLocation("lotmorestevesremake:textures/stev_illager_netherite.png");
			else if (getArmor == Items.DIAMOND_CHESTPLATE)
				Texture = new ResourceLocation("lotmorestevesremake:textures/stev_illager_diamond.png");
			else if (getArmor == Items.IRON_CHESTPLATE)
				Texture = new ResourceLocation("lotmorestevesremake:textures/stev_illager_iron.png");
			else if (getArmor == Items.LEATHER_CHESTPLATE)
				Texture = new ResourceLocation("lotmorestevesremake:textures/stev_illager_leather.png");
			else {
				color[0] = entityM.getRed() * 0.1f;
				color[1] = entityM.getGreen() * 0.1f;
				color[2] = entityM.getBlue() * 0.1f;
			}
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(Texture));
			this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color[0], color[1], color[2], 1);
		}
	}

	// Made with Blockbench 4.1.1
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	public static class Modelstev_illager<T extends StevindicatorEntity.CustomEntity> extends EntityModel<T> implements IHasArm, IHasHead {
		private final ModelRenderer head;
		private final ModelRenderer body;
		private final ModelRenderer arms;
		private final ModelRenderer arms_rotation;
		private final ModelRenderer arms_flipped;
		private final ModelRenderer left_arm;
		private final ModelRenderer right_arm;
		private final ModelRenderer left_leg;
		private final ModelRenderer right_leg;

		public Modelstev_illager() {
			textureWidth = 64;
			textureHeight = 64;
			head = new ModelRenderer(this);
			head.setRotationPoint(0.0F, 0.0F, 0.0F);
			head.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);
			body = new ModelRenderer(this);
			body.setRotationPoint(0.0F, 0.0F, 0.0F);
			body.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
			body.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F, false);
			arms = new ModelRenderer(this);
			arms.setRotationPoint(0.0F, 3.5F, 0.3F);
			arms_rotation = new ModelRenderer(this);
			arms_rotation.setRotationPoint(0.0F, -2.0F, 0.05F);
			arms.addChild(arms_rotation);
			setRotationAngle(arms_rotation, -0.7505F, 0.0F, 0.0F);
			arms_rotation.setTextureOffset(44, 22).addBox(-8.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
			arms_rotation.setTextureOffset(40, 38).addBox(-4.0F, 4.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);
			arms_flipped = new ModelRenderer(this);
			arms_flipped.setRotationPoint(0.0F, 24.0F, 0.0F);
			arms_rotation.addChild(arms_flipped);
			arms_flipped.setTextureOffset(44, 22).addBox(4.0F, -24.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, true);
			left_arm = new ModelRenderer(this);
			left_arm.setRotationPoint(6.0F, 2.0F, 0.0F);
			left_arm.setTextureOffset(40, 46).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
			right_arm = new ModelRenderer(this);
			right_arm.setRotationPoint(-6.0F, 2.0F, 0.0F);
			right_arm.setTextureOffset(40, 46).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			left_leg = new ModelRenderer(this);
			left_leg.setRotationPoint(2.0F, 12.0F, 0.0F);
			left_leg.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
			right_leg = new ModelRenderer(this);
			right_leg.setRotationPoint(-2.0F, 12.0F, 0.0F);
			right_leg.setTextureOffset(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			arms.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			left_arm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			right_arm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			left_leg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			right_leg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}

		public void setRotationAngles(T entityM, float f, float f1, float f2, float f3, float f4) {
			// entityM = (StevindicatorEntity.CustomEntity) e;
			float armSwing = MathHelper.cos(f * 0.6662F) * f1;
			boolean flag = entityM.getPrimaryHand() == HandSide.RIGHT;
			this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
			this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
			this.head.rotationPointX = 0;
			this.head.rotationPointY = 0;
			this.right_arm.rotateAngleZ = 0;
			this.left_arm.rotateAngleZ = 0;
			this.right_arm.rotationPointY = 2;
			this.left_arm.rotationPointY = 2;
			this.body.rotationPointY = 0;
			if ((f1 > 0.13f || entityM.isSwingInProgress || entityM.isPassenger()) && entityM.isAlive() || entityM.getDancingTick() > 0) {
				this.arms.showModel = false;
				this.right_arm.showModel = true;
				this.left_arm.showModel = true;
			} else {
				this.arms.showModel = true;
				this.right_arm.showModel = false;
				this.left_arm.showModel = false;
			}
			if (entityM.isSwingInProgress) {
				if (flag) {
					if (this.swingProgress < 0.375f) {
						float a = this.swingProgress * 2.667f;
						a = a * a * a;
						this.right_arm.rotateAngleX = -armSwing * MathHelper.clamp(1 - MathHelper.sin(a * (float) Math.PI) * 2, 0, 1)
								- MathHelper.clamp(MathHelper.sin(a * (float) Math.PI) * 3, -1.5f, 3);
						this.left_arm.rotateAngleX = armSwing;
					} else {
						float a = (0.625f - this.swingProgress) * 1.6f;
						a = a * a * a;
						this.right_arm.rotateAngleX = -armSwing * (1 - MathHelper.sin(a * (float) Math.PI / 2))
								+ 1.5f * MathHelper.sin(a * (float) Math.PI / 2);
						this.left_arm.rotateAngleX = armSwing;
					}
				} else {
					if (this.swingProgress < 0.375f) {
						float a = this.swingProgress * 2.667f;
						a = a * a * a;
						this.left_arm.rotateAngleX = armSwing * MathHelper.clamp(1 - MathHelper.sin(a * (float) Math.PI) * 2, 0, 1)
								- MathHelper.clamp(MathHelper.sin(a * (float) Math.PI) * 3, -1.5f, 3);
						this.right_arm.rotateAngleX = -armSwing;
					} else {
						float a = (0.625f - this.swingProgress) * 1.6f;
						a = a * a * a;
						this.left_arm.rotateAngleX = armSwing * (1 - MathHelper.sin(a * (float) Math.PI / 2))
								+ 1.5f * MathHelper.sin(a * (float) Math.PI / 2);
						this.right_arm.rotateAngleX = -armSwing;
					}
				}
			} else {
				this.left_arm.rotateAngleX = armSwing;
				this.right_arm.rotateAngleX = -armSwing;
				if (this.isSitting) {
					this.right_arm.rotateAngleX += (-(float) Math.PI / 5F);
					this.left_arm.rotateAngleX += (-(float) Math.PI / 5F);
				}
			}
			if (entityM.getDancingTick() > 0) {
				float d3 = f2 / 20.0F;
				this.head.rotationPointX += MathHelper.sin(d3 * 10.0F);
				this.head.rotationPointY += MathHelper.sin(d3 * 40.0F) + 0.4F;
				this.right_arm.rotateAngleZ += ((float) Math.PI / 180F) * (70.0F + MathHelper.cos(d3 * 40.0F) * 10.0F);
				this.left_arm.rotateAngleZ += this.right_arm.rotateAngleZ * -1.0F;
				this.right_arm.rotationPointY += MathHelper.sin(d3 * 40.0F) * 0.5F + 1.5F;
				this.left_arm.rotationPointY += MathHelper.sin(d3 * 40.0F) * 0.5F + 1.5F;
				this.body.rotationPointY += MathHelper.sin(d3 * 40.0F) * 0.35F;
			}
			if (this.isSitting) {
				this.right_leg.rotateAngleX = -1.4137167F;
				this.right_leg.rotateAngleY = ((float) Math.PI / 10F);
				this.right_leg.rotateAngleZ = 0.07853982F;
				this.left_leg.rotateAngleX = -1.4137167F;
				this.left_leg.rotateAngleY = (-(float) Math.PI / 10F);
				this.left_leg.rotateAngleZ = -0.07853982F;
			} else {
				this.left_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
				left_leg.rotateAngleY = 0;
				left_leg.rotateAngleZ = 0;
				this.right_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
				right_leg.rotateAngleY = 0;
				right_leg.rotateAngleZ = 0;
			}
			this.arms.rotateAngleX = 0;
			ModelHelper.func_239101_a_(this.right_arm, this.left_arm, f2);
		}

		public ModelRenderer getModelHead() {
			return this.head;
		}

		public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
			this.getArmForSide(sideIn).translateRotate(matrixStackIn);
		}

		protected ModelRenderer getArmForSide(HandSide side) {
			return side == HandSide.LEFT ? this.left_arm : this.right_arm;
		}
	}
}
