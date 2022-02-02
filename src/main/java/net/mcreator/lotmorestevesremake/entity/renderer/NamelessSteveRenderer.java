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
import net.minecraft.client.renderer.entity.model.PiglinModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.BipedRenderer;

import net.mcreator.lotmorestevesremake.entity.NamelessSteveEntity;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class NamelessSteveRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(NamelessSteveEntity.entity, renderManager -> {
				BipedRenderer customRender = new BipedRenderer(renderManager, new NamelessSteveModel<>(0), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("lotmorestevesremake:textures/doppelganger-t-h-e-n-e-t-h-e-r-o-o-m-s-on-planetminecraft-com.png");
					}
				};
				customRender.addLayer(new BipedArmorLayer(customRender, new BipedModel(0.5f), new BipedModel(1)));
				return customRender;
			});
		}
	}

	public static class NamelessSteveModel<T extends MobEntity> extends PiglinModel<T> {
		public NamelessSteveModel(float modelSize) {
			super(modelSize, 64, 64);
		}

		public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			super.setRotationAngles(entityIn, limbSwing * 3, limbSwingAmount, ageInTicks * 3, netHeadYaw, headPitch);
			Random random = new Random();
			bipedHead.rotateAngleZ += (random.nextFloat() - 0.5f) * 0.3f;
			if (entityIn.hurtTime > 0) {
				float f3 = ageInTicks / 10.0F;
				this.field_239116_b_.rotateAngleZ += ((float) Math.PI / 6F) + ((float) Math.PI / 180F) * MathHelper.sin(f3 * 30.0F) * 10.0F;
				this.field_239115_a_.rotateAngleZ += (-(float) Math.PI / 6F) - ((float) Math.PI / 180F) * MathHelper.cos(f3 * 30.0F) * 10.0F;
				this.bipedHead.rotationPointX += MathHelper.sin(f3 * 10.0F);
				this.bipedHead.rotationPointY += MathHelper.sin(f3 * 40.0F) + 0.4F;
				this.bipedRightArm.rotateAngleZ += ((float) Math.PI / 180F) * (70.0F + MathHelper.cos(f3 * 40.0F) * 10.0F);
				this.bipedLeftArm.rotateAngleZ += this.bipedRightArm.rotateAngleZ * -1.0F;
				this.bipedRightArm.rotationPointY += MathHelper.sin(f3 * 40.0F) * 0.5F + 1.5F;
				this.bipedLeftArm.rotationPointY += MathHelper.sin(f3 * 40.0F) * 0.5F + 1.5F;
				this.bipedBody.rotationPointY += MathHelper.sin(f3 * 40.0F) * 0.35F;
			}
		}
	}
}
