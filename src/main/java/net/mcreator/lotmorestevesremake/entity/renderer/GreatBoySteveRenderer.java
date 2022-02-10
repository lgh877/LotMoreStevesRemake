package net.mcreator.lotmorestevesremake.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.mcreator.lotmorestevesremake.entity.GreatBoySteveEntity;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class GreatBoySteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(GreatBoySteveEntity.entity, renderManager -> {
				return new MobRenderer(renderManager, new ModelHugeUndead(), 0.8f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/great_boy_steve.png");
					}
				};
			});
		}
	}

	// Made with Blockbench 3.8.4
	// Exported for Minecraft version 1.15 - 1.16
	// Paste this class into your mod and generate all required imports
	public static class ModelHugeUndead extends EntityModel<Entity> {
		private final ModelRenderer Whole;
		private final ModelRenderer LowerTorso;
		private final ModelRenderer UpperTorso;
		private final ModelRenderer Neck;
		private final ModelRenderer cube_r1;
		private final ModelRenderer Head;
		private final ModelRenderer Jaw;
		private final ModelRenderer RightShoulder;
		private final ModelRenderer cube_r2;
		private final ModelRenderer RightWrist;
		private final ModelRenderer cube_r3;
		private final ModelRenderer LeftShoulder;
		private final ModelRenderer cube_r4;
		private final ModelRenderer LeftWrist;
		private final ModelRenderer cube_r5;
		private final ModelRenderer RightThigh;
		private final ModelRenderer cube_r6;
		private final ModelRenderer RightShank;
		private final ModelRenderer LeftThigh;
		private final ModelRenderer cube_r7;
		private final ModelRenderer LeftShank;

		public ModelHugeUndead() {
			textureWidth = 128;
			textureHeight = 128;
			Whole = new ModelRenderer(this);
			Whole.setRotationPoint(0.0F, 24.0F, 0.0F);
			LowerTorso = new ModelRenderer(this);
			LowerTorso.setRotationPoint(0.0F, -17.0F, 6.0F);
			Whole.addChild(LowerTorso);
			setRotationAngle(LowerTorso, 0.2618F, 0.0F, 0.0F);
			LowerTorso.setTextureOffset(0, 19).addBox(-5.0F, -10.0F, -4.0F, 10.0F, 10.0F, 7.0F, 0.0F, false);
			UpperTorso = new ModelRenderer(this);
			UpperTorso.setRotationPoint(0.0F, -8.0F, 0.0F);
			LowerTorso.addChild(UpperTorso);
			setRotationAngle(UpperTorso, 0.2618F, 0.0F, 0.0F);
			UpperTorso.setTextureOffset(0, 0).addBox(-6.0F, -12.0F, -5.0F, 12.0F, 10.0F, 9.0F, 0.0F, false);
			Neck = new ModelRenderer(this);
			Neck.setRotationPoint(0.0F, -11.0F, -1.0F);
			UpperTorso.addChild(Neck);
			setRotationAngle(Neck, -0.5236F, 0.0F, 0.0F);
			cube_r1 = new ModelRenderer(this);
			cube_r1.setRotationPoint(0.0F, 0.0F, -3.0F);
			Neck.addChild(cube_r1);
			setRotationAngle(cube_r1, 0.6109F, 0.0F, 0.0F);
			cube_r1.setTextureOffset(42, 0).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 7.0F, 4.0F, 0.0F, false);
			Head = new ModelRenderer(this);
			Head.setRotationPoint(0.0F, -3.0F, -3.0F);
			Neck.addChild(Head);
			Head.setTextureOffset(34, 11).addBox(-4.0F, -7.0F, -6.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
			Jaw = new ModelRenderer(this);
			Jaw.setRotationPoint(0.0F, 1.0F, 2.0F);
			Head.addChild(Jaw);
			Jaw.setTextureOffset(26, 28).addBox(-4.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.01F, false);
			RightShoulder = new ModelRenderer(this);
			RightShoulder.setRotationPoint(-6.0F, -7.0F, 0.0F);
			UpperTorso.addChild(RightShoulder);
			cube_r2 = new ModelRenderer(this);
			cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
			RightShoulder.addChild(cube_r2);
			setRotationAngle(cube_r2, -0.7854F, 0.0F, 0.0F);
			cube_r2.setTextureOffset(48, 44).addBox(-5.0F, -1.0F, -3.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
			RightWrist = new ModelRenderer(this);
			RightWrist.setRotationPoint(-3.0F, 8.0F, -9.0F);
			RightShoulder.addChild(RightWrist);
			cube_r3 = new ModelRenderer(this);
			cube_r3.setRotationPoint(3.0F, 0.0F, 0.0F);
			RightWrist.addChild(cube_r3);
			setRotationAngle(cube_r3, -1.5708F, 0.0F, 0.0F);
			cube_r3.setTextureOffset(16, 44).addBox(-5.0F, -0.0208F, -2.2929F, 4.0F, 15.0F, 4.0F, 0.0F, false);
			LeftShoulder = new ModelRenderer(this);
			LeftShoulder.setRotationPoint(6.0F, -7.0F, 0.0F);
			UpperTorso.addChild(LeftShoulder);
			cube_r4 = new ModelRenderer(this);
			cube_r4.setRotationPoint(0.0F, 0.0F, 0.0F);
			LeftShoulder.addChild(cube_r4);
			setRotationAngle(cube_r4, -0.7854F, 0.0F, 0.0F);
			cube_r4.setTextureOffset(32, 44).addBox(1.0F, -1.0F, -3.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);
			LeftWrist = new ModelRenderer(this);
			LeftWrist.setRotationPoint(3.0F, 8.0F, -9.0F);
			LeftShoulder.addChild(LeftWrist);
			cube_r5 = new ModelRenderer(this);
			cube_r5.setRotationPoint(-3.0F, 0.0F, 0.0F);
			LeftWrist.addChild(cube_r5);
			setRotationAngle(cube_r5, -1.5708F, 0.0F, 0.0F);
			cube_r5.setTextureOffset(0, 36).addBox(1.0F, -0.0208F, -2.2929F, 4.0F, 15.0F, 4.0F, 0.0F, false);
			RightThigh = new ModelRenderer(this);
			RightThigh.setRotationPoint(-3.0F, -18.0F, 6.0F);
			Whole.addChild(RightThigh);
			cube_r6 = new ModelRenderer(this);
			cube_r6.setRotationPoint(0.0F, 0.0F, 0.0F);
			RightThigh.addChild(cube_r6);
			setRotationAngle(cube_r6, -0.5236F, 0.0F, 0.0F);
			cube_r6.setTextureOffset(60, 56).addBox(-3.0F, 1.0F, -2.0F, 4.0F, 8.0F, 4.0F, 1.0F, false);
			RightShank = new ModelRenderer(this);
			RightShank.setRotationPoint(-1.0F, 8.0F, -6.0F);
			RightThigh.addChild(RightShank);
			RightShank.setTextureOffset(58, 0).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
			LeftThigh = new ModelRenderer(this);
			LeftThigh.setRotationPoint(3.0F, -18.0F, 6.0F);
			Whole.addChild(LeftThigh);
			cube_r7 = new ModelRenderer(this);
			cube_r7.setRotationPoint(0.0F, 0.0F, 0.0F);
			LeftThigh.addChild(cube_r7);
			setRotationAngle(cube_r7, -0.5236F, 0.0F, 0.0F);
			cube_r7.setTextureOffset(28, 60).addBox(-1.0F, 1.0F, -2.0F, 4.0F, 8.0F, 4.0F, 1.0F, false);
			LeftShank = new ModelRenderer(this);
			LeftShank.setRotationPoint(1.0F, 8.0F, -6.0F);
			LeftThigh.addChild(LeftShank);
			LeftShank.setTextureOffset(0, 55).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
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
			MobEntity entityM = (MobEntity) e;
			float pi = (float) Math.PI;
			float pitch = f4 / (180F / pi);
			float headYaw = f3 / (180F / pi);
			this.Whole.rotateAngleX = f1 * pi * (1 + MathHelper.cos(f * 0.8F)) / 12;
			this.Whole.rotationPointY = 24;
			this.Whole.rotationPointZ = 0;
			this.Head.rotateAngleY = headYaw;
			this.Head.rotateAngleX = pitch * 0.25f;
			//this.Jaw.rotateAngleX = (MathHelper.cos(f * 0.5F) + 1) * f1 / 3f;
			this.Neck.rotateAngleX = pitch * 0.25f - pi / 12 + (1 - f1) * MathHelper.cos(f2 / 6) / 12;
			this.UpperTorso.rotateAngleX = pitch * 0.25f + pi / 12 + (1 - f1) * MathHelper.cos(f2 / 5) / 12;
			this.UpperTorso.rotateAngleY = 0;
			this.LowerTorso.rotateAngleX = pitch * 0.25f + pi / 12 + (1 - f1) * MathHelper.cos(f2 / 7) / 12
					- f1 * pi * (1 + MathHelper.cos(f * 0.8F)) / 18;
			this.LowerTorso.rotateAngleY = 0;
			{//arms
				this.LeftShoulder.rotateAngleX = MathHelper.cos(f * 0.6F) * f1 / 3 + (1 - f1) * MathHelper.cos(f2 / 4) / 12 + 0.4f;
				this.LeftShoulder.rotateAngleZ = 0;
				this.LeftWrist.rotateAngleX = MathHelper.cos(f * 0.6F) * f1 / 3;
				this.LeftWrist.rotateAngleY = 0;
				this.RightShoulder.rotateAngleX = MathHelper.cos(f * 0.6F + pi) * f1 / 3 + (1 - f1) * MathHelper.cos(f2 / 4) / 12 + 0.4f;
				this.RightShoulder.rotateAngleZ = 0;
				this.RightWrist.rotateAngleX = MathHelper.cos(f * 0.4F + pi) * f1 / 3;
				this.RightWrist.rotateAngleY = 0;
			}
			this.LeftThigh.rotateAngleX = MathHelper.cos(f * 0.4F) * -1.0F * f1 / 3;
			this.LeftShank.rotateAngleX = MathHelper.cos(f * 0.4F - pi * 0.4f) * -1.0F * f1 / 3;
			this.RightThigh.rotateAngleX = MathHelper.cos(f * 0.4F) * 1.0F * f1 / 2;
			this.RightShank.rotateAngleX = MathHelper.cos(f * 0.4F - pi * 0.4f) * 1.0F * f1 / 3;
		}
	}
}
