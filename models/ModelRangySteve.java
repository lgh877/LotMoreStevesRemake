// Made with Blockbench 4.2.4
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports

public static class ModelRangySteve extends EntityModel<Entity> {
	private final ModelRenderer head;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightArm;
	private final ModelRenderer body;
	private final ModelRenderer leftLeg;
	private final ModelRenderer rightLeg;

	public ModelRangySteve() {
		textureWidth = 64;
		textureHeight = 64;

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -9.0F, -5.0F);
		head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		leftArm = new ModelRenderer(this);
		leftArm.setRotationPoint(6.0F, -8.0F, -3.0F);
		leftArm.setTextureOffset(24, 16).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 24.0F, 4.0F, 0.0F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setRotationPoint(-6.0F, -8.0F, -3.0F);
		rightArm.setTextureOffset(24, 16).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 24.0F, 4.0F, 0.0F, true);

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, -10.0F, -4.0F);
		setRotationAngle(body, 0.5236F, 0.0F, 0.0F);
		body.setTextureOffset(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(3.0F, 0.0F, 2.0F);
		leftLeg.setTextureOffset(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 24.0F, 4.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-3.0F, 0.0F, 2.0F);
		rightLeg.setTextureOffset(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 24.0F, 4.0F, 0.0F, true);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		leftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		rightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		leftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		rightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.rightLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
		this.rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		this.leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
		this.leftLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
	}
}