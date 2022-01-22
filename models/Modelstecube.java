// Made with Blockbench 4.1.3
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports

public static class Modelstecube extends EntityModel<Entity> {
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
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
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

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		this.right_arm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		this.left_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
		this.left_arm.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
		this.right_leg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
	}
}