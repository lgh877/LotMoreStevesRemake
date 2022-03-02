// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports

public static class ModelStevager extends EntityModel<Entity> {
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
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		whole.render(matrixStack, buffer, packedLight, packedOverlay);
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