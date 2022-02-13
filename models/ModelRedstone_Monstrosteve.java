// Made with Blockbench 4.1.4
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports

public static class ModelRedstone_Monstrosteve extends EntityModel<Entity> {
	private final ModelRenderer whole;
	private final ModelRenderer body;
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
		leftLeg.setRotationPoint(16.0F, 0.0F, 0.0F);
		whole.addChild(leftLeg);
		leftLeg.setTextureOffset(128, 167).addBox(-8.0F, -24.0F, -8.0F, 16.0F, 24.0F, 16.0F, 0.0F, false);

		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-16.0F, 0.0F, 0.0F);
		whole.addChild(rightLeg);
		rightLeg.setTextureOffset(64, 155).addBox(-8.0F, -24.0F, -8.0F, 16.0F, 24.0F, 16.0F, 0.0F, false);
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
		this.rightShoulder.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		this.rightLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
		this.leftLeg.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
		this.leftShoulder.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
	}
}