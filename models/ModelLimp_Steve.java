// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports

public static class ModelLimp_Steve extends EntityModel<Entity> {
	private final ModelRenderer back1;
	private final ModelRenderer back2;
	private final ModelRenderer back3;
	private final ModelRenderer back4;
	private final ModelRenderer leftArm1;
	private final ModelRenderer leftArm2;
	private final ModelRenderer leftArm3;
	private final ModelRenderer leftArm4;
	private final ModelRenderer leftArm5;
	private final ModelRenderer rightArm1;
	private final ModelRenderer rightArm2;
	private final ModelRenderer rightArm3;
	private final ModelRenderer rightArm4;
	private final ModelRenderer rightArm5;
	private final ModelRenderer head;
	private final ModelRenderer leftThigh;
	private final ModelRenderer leftShank;
	private final ModelRenderer leftFoot;
	private final ModelRenderer rightThigh;
	private final ModelRenderer rightShank;
	private final ModelRenderer rightFoot;

	public ModelLimp_Steve() {
		textureWidth = 256;
		textureHeight = 256;

		back1 = new ModelRenderer(this);
		back1.setRotationPoint(0.0F, -4.0F, 0.0F);
		back1.setTextureOffset(94, 94).addBox(-8.0F, -20.0F, -5.0F, 16.0F, 20.0F, 10.0F, 0.0F, false);

		back2 = new ModelRenderer(this);
		back2.setRotationPoint(0.0F, -20.0F, 0.0F);
		back1.addChild(back2);
		back2.setTextureOffset(80, 0).addBox(-8.0F, -20.0F, -5.0F, 16.0F, 20.0F, 10.0F, 0.0F, false);

		back3 = new ModelRenderer(this);
		back3.setRotationPoint(0.0F, -20.0F, 0.0F);
		back2.addChild(back3);
		back3.setTextureOffset(52, 74).addBox(-8.0F, -20.0F, -5.0F, 16.0F, 20.0F, 10.0F, 0.0F, false);

		back4 = new ModelRenderer(this);
		back4.setRotationPoint(0.0F, -20.0F, 0.0F);
		back3.addChild(back4);
		back4.setTextureOffset(0, 74).addBox(-8.0F, -20.0F, -5.0F, 16.0F, 20.0F, 10.0F, 0.0F, false);

		leftArm1 = new ModelRenderer(this);
		leftArm1.setRotationPoint(14.0F, -11.0F, 0.0F);
		back4.addChild(leftArm1);
		leftArm1.setTextureOffset(52, 40).addBox(-6.0F, -4.0F, -7.0F, 12.0F, 20.0F, 14.0F, 0.0F, false);

		leftArm2 = new ModelRenderer(this);
		leftArm2.setRotationPoint(0.0F, 16.0F, 0.0F);
		leftArm1.addChild(leftArm2);
		leftArm2.setTextureOffset(0, 134).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);

		leftArm3 = new ModelRenderer(this);
		leftArm3.setRotationPoint(0.0F, 20.0F, 0.0F);
		leftArm2.addChild(leftArm3);
		leftArm3.setTextureOffset(132, 0).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);

		leftArm4 = new ModelRenderer(this);
		leftArm4.setRotationPoint(0.0F, 20.0F, 0.0F);
		leftArm3.addChild(leftArm4);
		leftArm4.setTextureOffset(98, 124).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);

		leftArm5 = new ModelRenderer(this);
		leftArm5.setRotationPoint(0.0F, 40.0F, 0.0F);
		leftArm3.addChild(leftArm5);
		leftArm5.setTextureOffset(0, 104).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);

		rightArm1 = new ModelRenderer(this);
		rightArm1.setRotationPoint(-14.0F, -11.0F, 0.0F);
		back4.addChild(rightArm1);
		rightArm1.setTextureOffset(0, 40).addBox(-6.0F, -4.0F, -7.0F, 12.0F, 20.0F, 14.0F, 0.0F, false);

		rightArm2 = new ModelRenderer(this);
		rightArm2.setRotationPoint(0.0F, 16.0F, 0.0F);
		rightArm1.addChild(rightArm2);
		rightArm2.setTextureOffset(62, 124).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);

		rightArm3 = new ModelRenderer(this);
		rightArm3.setRotationPoint(0.0F, 20.0F, 0.0F);
		rightArm2.addChild(rightArm3);
		rightArm3.setTextureOffset(104, 60).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);

		rightArm4 = new ModelRenderer(this);
		rightArm4.setRotationPoint(0.0F, 20.0F, 0.0F);
		rightArm3.addChild(rightArm4);
		rightArm4.setTextureOffset(36, 104).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);

		rightArm5 = new ModelRenderer(this);
		rightArm5.setRotationPoint(0.0F, 40.0F, 0.0F);
		rightArm3.addChild(rightArm5);
		rightArm5.setTextureOffset(104, 30).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 20.0F, 10.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -20.0F, 0.0F);
		back4.addChild(head);
		head.setTextureOffset(0, 0).addBox(-10.0F, -20.0F, -11.0F, 20.0F, 20.0F, 20.0F, 0.0F, false);

		leftThigh = new ModelRenderer(this);
		leftThigh.setRotationPoint(5.0F, -4.0F, 0.0F);
		leftThigh.setTextureOffset(134, 142).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);

		leftShank = new ModelRenderer(this);
		leftShank.setRotationPoint(0.0F, 12.0F, 0.0F);
		leftThigh.addChild(leftShank);
		leftShank.setTextureOffset(140, 30).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);

		leftFoot = new ModelRenderer(this);
		leftFoot.setRotationPoint(0.0F, 4.0F, 0.0F);
		leftShank.addChild(leftFoot);
		leftFoot.setTextureOffset(130, 49).addBox(-3.0F, 8.0F, -8.0F, 6.0F, 4.0F, 11.0F, 0.0F, false);

		rightThigh = new ModelRenderer(this);
		rightThigh.setRotationPoint(-5.0F, -4.0F, 0.0F);
		rightThigh.setTextureOffset(134, 124).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);

		rightShank = new ModelRenderer(this);
		rightShank.setRotationPoint(0.0F, 12.0F, 0.0F);
		rightThigh.addChild(rightShank);
		rightShank.setTextureOffset(36, 134).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, 0.0F, false);

		rightFoot = new ModelRenderer(this);
		rightFoot.setRotationPoint(0.0F, 4.0F, 0.0F);
		rightShank.addChild(rightFoot);
		rightFoot.setTextureOffset(129, 79).addBox(-3.0F, 8.0F, -8.0F, 6.0F, 4.0F, 11.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		back1.render(matrixStack, buffer, packedLight, packedOverlay);
		leftThigh.render(matrixStack, buffer, packedLight, packedOverlay);
		rightThigh.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		this.rightArm1.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.rightThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
		this.leftArm1.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
		this.leftThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
	}
}