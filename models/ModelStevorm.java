// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports

public static class ModelStevorm extends EntityModel<Entity> {
	private final ModelRenderer body1;
	private final ModelRenderer head;
	private final ModelRenderer body2;
	private final ModelRenderer body3;
	private final ModelRenderer body4;

	public ModelStevorm() {
		textureWidth = 64;
		textureHeight = 64;

		body1 = new ModelRenderer(this);
		body1.setRotationPoint(1.0F, 24.0F, -5.0F);
		body1.setTextureOffset(27, 7).addBox(-5.0F, -6.0F, -2.0F, 8.0F, 6.0F, 7.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(-1.0F, -4.0F, -2.0F);
		body1.addChild(head);
		head.setTextureOffset(0, 14).addBox(-4.0F, -3.5F, -7.5F, 8.0F, 8.0F, 8.0F, -0.5F, false);

		body2 = new ModelRenderer(this);
		body2.setRotationPoint(0.0F, 24.0F, 0.0F);
		body2.setTextureOffset(0, 0).addBox(-5.0F, -7.0F, -3.0F, 10.0F, 7.0F, 7.0F, 0.1F, false);

		body3 = new ModelRenderer(this);
		body3.setRotationPoint(0.0F, 24.0F, 6.0F);
		body3.setTextureOffset(22, 20).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 5.0F, 10.0F, 0.0F, false);

		body4 = new ModelRenderer(this);
		body4.setRotationPoint(0.0F, 24.0F, 13.0F);
		body4.setTextureOffset(0, 30).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 3.0F, 9.0F, -0.1F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		body1.render(matrixStack, buffer, packedLight, packedOverlay);
		body2.render(matrixStack, buffer, packedLight, packedOverlay);
		body3.render(matrixStack, buffer, packedLight, packedOverlay);
		body4.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
	}
}