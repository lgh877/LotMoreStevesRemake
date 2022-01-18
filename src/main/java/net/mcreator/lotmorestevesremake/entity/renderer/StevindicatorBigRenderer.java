package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.mcreator.lotmorestevesremake.entity.StevindicatorBigEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class StevindicatorBigRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(StevindicatorBigEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new StevindicatorRenderer.Modelstev_illager(), 1f) {
					{
						this.addLayer(new GlowingLayer<>(this));
						this.addLayer(new HeldItemLayer<LivingEntity, StevindicatorRenderer.Modelstev_illager<LivingEntity>>(this) {
							public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
									LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
									float netHeadYaw, float headPitch) {
								if ((entitylivingbaseIn.isSwingInProgress || entitylivingbaseIn.limbSwingAmount > 0.13f)
										&& entitylivingbaseIn.isAlive()) {
									super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
											ageInTicks, netHeadYaw, headPitch);
								}
							}
						});
						this.addLayer(new HeadLayer<LivingEntity, StevindicatorRenderer.Modelstev_illager<LivingEntity>>(this));
					}

					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/stev_illager.png");
					}

					protected void preRenderCallback(LivingEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
						matrixStackIn.scale(2, 2, 2);
					}
				};
			});
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static class GlowingLayer<T extends Entity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
		public GlowingLayer(IEntityRenderer<T, M> er) {
			super(er);
		}

		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing,
				float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			ResourceLocation Texture = new ResourceLocation("lotmorestevesremake:textures/stev_illager_cloth1.png");
			MobEntity entityM = (MobEntity) entitylivingbaseIn;
			Item getArmor = entityM.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem();
			if (getArmor == Items.NETHERITE_CHESTPLATE)
				Texture = new ResourceLocation("lotmorestevesremake:textures/stev_illager_netherite.png");
			else if (getArmor == Items.DIAMOND_CHESTPLATE)
				Texture = new ResourceLocation("lotmorestevesremake:textures/stev_illager_diamond.png");
			else if (getArmor == Items.IRON_CHESTPLATE)
				Texture = new ResourceLocation("lotmorestevesremake:textures/stev_illager_iron.png");
			else if (getArmor == Items.LEATHER_CHESTPLATE)
				Texture = new ResourceLocation("lotmorestevesremake:textures/stev_illager_leather.png");
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(Texture));
			this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		}
	}
	// Made with Blockbench 4.1.1
	// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
	// Paste this class into your mod and generate all required imports
	/*public static class Modelstev_illager extends EntityModel<Entity> {
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
			left_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
			left_arm.setTextureOffset(40, 46).addBox(9.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
			right_arm = new ModelRenderer(this);
			right_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
			right_arm.setTextureOffset(40, 46).addBox(-13.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
			left_leg = new ModelRenderer(this);
			left_leg.setRotationPoint(-2.0F, 12.0F, 0.0F);
			left_leg.setTextureOffset(0, 22).addBox(2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
			right_leg = new ModelRenderer(this);
			right_leg.setRotationPoint(2.0F, 12.0F, 0.0F);
			right_leg.setTextureOffset(0, 22).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		}
		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			head.render(matrixStack, buffer, packedLight, packedOverlay);
			body.render(matrixStack, buffer, packedLight, packedOverlay);
			arms.render(matrixStack, buffer, packedLight, packedOverlay);
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
		public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4) {
			this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
			this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
			this.right_arm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
			this.left_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
			this.left_arm.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
			this.right_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
			this.arms.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		}
	}
	*/
}
