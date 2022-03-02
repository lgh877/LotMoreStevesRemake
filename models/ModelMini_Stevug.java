// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports

public static class ModelMini_Stevug extends EntityModel<Entity> {
	private final ModelRenderer head;
	private final ModelRenderer leftEye;
	private final ModelRenderer rightEye;
	private final ModelRenderer leftFirstThigh;
	private final ModelRenderer leftFirstShank;
	private final ModelRenderer leftSecondThigh;
	private final ModelRenderer leftSecondShank;
	private final ModelRenderer leftThirdThigh;
	private final ModelRenderer leftThirdShank;
	private final ModelRenderer rightFirstThigh;
	private final ModelRenderer rightFirstShank;
	private final ModelRenderer rightSecondThigh;
	private final ModelRenderer rightSecondShank;
	private final ModelRenderer rightThirdThigh;
	private final ModelRenderer rightThirdShank;
	private final ModelRenderer leftHand;
	private final ModelRenderer rightHand;

	public ModelMini_Stevug() {
		textureWidth = 64;
		textureHeight = 64;

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 20.0F, -1.0F);
		head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 10.0F, 0.0F, false);

		leftEye = new ModelRenderer(this);
		leftEye.setRotationPoint(2.0F, -4.0F, -4.0F);
		head.addChild(leftEye);
		leftEye.setTextureOffset(0, 0).addBox(-0.5F, -0.5F, -0.1F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		rightEye = new ModelRenderer(this);
		rightEye.setRotationPoint(-2.0F, -4.0F, -4.0F);
		head.addChild(rightEye);
		rightEye.setTextureOffset(0, 0).addBox(-0.5F, -0.5F, -0.1F, 1.0F, 1.0F, 1.0F, 0.0F, true);

		leftFirstThigh = new ModelRenderer(this);
		leftFirstThigh.setRotationPoint(3.0F, 0.0F, -2.0F);
		head.addChild(leftFirstThigh);
		leftFirstThigh.setTextureOffset(0, 34).addBox(-2.0F, -2.0F, -2.5F, 6.0F, 4.0F, 4.0F, -1.0F, false);

		leftFirstShank = new ModelRenderer(this);
		leftFirstShank.setRotationPoint(3.0F, 0.0F, 0.0F);
		leftFirstThigh.addChild(leftFirstShank);
		leftFirstShank.setTextureOffset(20, 26).addBox(-1.0F, -2.0F, -2.5F, 6.0F, 4.0F, 4.0F, -1.0F, false);

		leftSecondThigh = new ModelRenderer(this);
		leftSecondThigh.setRotationPoint(3.0F, 0.0F, 1.0F);
		head.addChild(leftSecondThigh);
		leftSecondThigh.setTextureOffset(26, 0).addBox(-2.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, -1.0F, false);

		leftSecondShank = new ModelRenderer(this);
		leftSecondShank.setRotationPoint(3.0F, 0.0F, 0.0F);
		leftSecondThigh.addChild(leftSecondShank);
		leftSecondShank.setTextureOffset(0, 26).addBox(-1.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, -1.0F, false);

		leftThirdThigh = new ModelRenderer(this);
		leftThirdThigh.setRotationPoint(3.0F, 0.0F, 4.0F);
		head.addChild(leftThirdThigh);
		leftThirdThigh.setTextureOffset(20, 18).addBox(-2.0F, -2.0F, -1.5F, 6.0F, 4.0F, 4.0F, -1.0F, false);

		leftThirdShank = new ModelRenderer(this);
		leftThirdShank.setRotationPoint(3.0F, 0.0F, 0.0F);
		leftThirdThigh.addChild(leftThirdShank);
		leftThirdShank.setTextureOffset(0, 18).addBox(-1.0F, -2.0F, -1.5F, 6.0F, 4.0F, 4.0F, -1.0F, false);

		rightFirstThigh = new ModelRenderer(this);
		rightFirstThigh.setRotationPoint(-3.0F, 0.0F, -2.0F);
		head.addChild(rightFirstThigh);
		rightFirstThigh.setTextureOffset(0, 34).addBox(-4.0F, -2.0F, -2.5F, 6.0F, 4.0F, 4.0F, -1.0F, true);

		rightFirstShank = new ModelRenderer(this);
		rightFirstShank.setRotationPoint(-3.0F, 0.0F, 0.0F);
		rightFirstThigh.addChild(rightFirstShank);
		rightFirstShank.setTextureOffset(20, 26).addBox(-5.0F, -2.0F, -2.5F, 6.0F, 4.0F, 4.0F, -1.0F, true);

		rightSecondThigh = new ModelRenderer(this);
		rightSecondThigh.setRotationPoint(-3.0F, 0.0F, 1.0F);
		head.addChild(rightSecondThigh);
		rightSecondThigh.setTextureOffset(26, 0).addBox(-4.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, -1.0F, true);

		rightSecondShank = new ModelRenderer(this);
		rightSecondShank.setRotationPoint(-3.0F, 0.0F, 0.0F);
		rightSecondThigh.addChild(rightSecondShank);
		rightSecondShank.setTextureOffset(0, 26).addBox(-5.0F, -2.0F, -2.0F, 6.0F, 4.0F, 4.0F, -1.0F, true);

		rightThirdThigh = new ModelRenderer(this);
		rightThirdThigh.setRotationPoint(-3.0F, 0.0F, 4.0F);
		head.addChild(rightThirdThigh);
		rightThirdThigh.setTextureOffset(20, 18).addBox(-4.0F, -2.0F, -1.5F, 6.0F, 4.0F, 4.0F, -1.0F, true);

		rightThirdShank = new ModelRenderer(this);
		rightThirdShank.setRotationPoint(-3.0F, 0.0F, 0.0F);
		rightThirdThigh.addChild(rightThirdShank);
		rightThirdShank.setTextureOffset(0, 18).addBox(-5.0F, -2.0F, -1.5F, 6.0F, 4.0F, 4.0F, -1.0F, true);

		leftHand = new ModelRenderer(this);
		leftHand.setRotationPoint(3.0F, 12.0F, -4.0F);

		rightHand = new ModelRenderer(this);
		rightHand.setRotationPoint(-3.0F, 12.0F, -4.0F);

	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		head.render(matrixStack, buffer, packedLight, packedOverlay);
		leftHand.render(matrixStack, buffer, packedLight, packedOverlay);
		rightHand.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		this.leftFirstThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
		this.leftSecondThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
		this.leftThirdThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
	}
}