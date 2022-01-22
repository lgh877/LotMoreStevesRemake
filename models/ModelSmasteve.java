// Made with Blockbench 4.1.3
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports

public static class ModelSmasteve extends EntityModel<Entity> {
	private final ModelRenderer whole;
	private final ModelRenderer lowerBody;
	private final ModelRenderer cube_r1;
	private final ModelRenderer upperBody;
	private final ModelRenderer neck;
	private final ModelRenderer head;
	private final ModelRenderer rightShoulder;
	private final ModelRenderer rightWrist;
	private final ModelRenderer rightFist;
	private final ModelRenderer leftShoulder;
	private final ModelRenderer leftWrist;
	private final ModelRenderer rightRearThigh;
	private final ModelRenderer cube_r2;
	private final ModelRenderer rightRearShank;
	private final ModelRenderer rightFrontThigh;
	private final ModelRenderer rightFrontShank;
	private final ModelRenderer leftThigh;
	private final ModelRenderer cube_r3;
	private final ModelRenderer leftShank;

	public ModelSmasteve() {
		textureWidth = 128;
		textureHeight = 128;

		whole = new ModelRenderer(this);
		whole.setRotationPoint(0.0F, 24.0F, 0.0F);

		lowerBody = new ModelRenderer(this);
		lowerBody.setRotationPoint(2.0F, -14.0F, 0.0F);
		whole.addChild(lowerBody);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		lowerBody.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, -0.4363F);
		cube_r1.setTextureOffset(48, 19).addBox(-6.0F, -11.0F, -5.0F, 12.0F, 11.0F, 10.0F, 0.0F, false);

		upperBody = new ModelRenderer(this);
		upperBody.setRotationPoint(-4.0F, -10.0F, 0.0F);
		lowerBody.addChild(upperBody);
		upperBody.setTextureOffset(0, 0).addBox(-8.0F, -13.0F, -7.0F, 15.0F, 15.0F, 14.0F, 0.0F, false);

		neck = new ModelRenderer(this);
		neck.setRotationPoint(0.0F, -11.0F, -2.0F);
		upperBody.addChild(neck);
		neck.setTextureOffset(88, 10).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -8.0F, 0.0F);
		neck.addChild(head);
		head.setTextureOffset(0, 29).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, 0.0F, false);
		head.setTextureOffset(0, 0).addBox(2.0F, -6.0F, -7.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		rightShoulder = new ModelRenderer(this);
		rightShoulder.setRotationPoint(-8.0F, -10.0F, 0.0F);
		upperBody.addChild(rightShoulder);
		setRotationAngle(rightShoulder, 0.3491F, 0.0F, 0.0F);
		rightShoulder.setTextureOffset(32, 66).addBox(-7.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
		rightShoulder.setTextureOffset(0, 53).addBox(-11.0F, 0.0F, -4.0F, 8.0F, 20.0F, 8.0F, 0.0F, false);

		rightWrist = new ModelRenderer(this);
		rightWrist.setRotationPoint(-7.0F, 20.0F, 0.0F);
		rightShoulder.addChild(rightWrist);
		setRotationAngle(rightWrist, 0.5236F, 0.0F, 0.0F);
		rightWrist.setTextureOffset(81, 40).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 18.0F, 6.0F, 0.0F, false);

		rightFist = new ModelRenderer(this);
		rightFist.setRotationPoint(0.0F, 18.0F, 0.0F);
		rightWrist.addChild(rightFist);
		setRotationAngle(rightFist, 0.6981F, 0.0F, 0.0F);
		rightFist.setTextureOffset(58, 0).addBox(-5.0F, 0.0F, -4.0F, 10.0F, 8.0F, 8.0F, 0.0F, false);

		leftShoulder = new ModelRenderer(this);
		leftShoulder.setRotationPoint(7.0F, -10.0F, 0.0F);
		upperBody.addChild(leftShoulder);
		leftShoulder.setTextureOffset(99, 35).addBox(0.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, 0.0F, false);

		leftWrist = new ModelRenderer(this);
		leftWrist.setRotationPoint(2.0F, 5.0F, 0.0F);
		leftShoulder.addChild(leftWrist);
		leftWrist.setTextureOffset(92, 24).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, -1.0F, false);

		rightRearThigh = new ModelRenderer(this);
		rightRearThigh.setRotationPoint(-4.0F, -13.0F, 3.0F);
		whole.addChild(rightRearThigh);
		setRotationAngle(rightRearThigh, 0.0F, 0.5236F, 0.0F);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-5.0F, 4.0F, 0.0F);
		rightRearThigh.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.7854F);
		cube_r2.setTextureOffset(32, 86).addBox(-3.0F, -9.5F, -3.0F, 7.0F, 11.0F, 7.0F, -1.5F, false);

		rightRearShank = new ModelRenderer(this);
		rightRearShank.setRotationPoint(-3.0F, 4.0F, 0.0F);
		rightRearThigh.addChild(rightRearShank);
		rightRearShank.setTextureOffset(37, 42).addBox(-7.0F, -2.0F, -5.0F, 11.0F, 13.0F, 11.0F, -2.0F, false);

		rightFrontThigh = new ModelRenderer(this);
		rightFrontThigh.setRotationPoint(-4.0F, -14.0F, -4.0F);
		whole.addChild(rightFrontThigh);
		rightFrontThigh.setTextureOffset(60, 86).addBox(-3.0F, -1.5F, -3.0F, 6.0F, 9.0F, 6.0F, -1.5F, false);

		rightFrontShank = new ModelRenderer(this);
		rightFrontShank.setRotationPoint(0.0F, 6.0F, 0.0F);
		rightFrontThigh.addChild(rightFrontShank);
		rightFrontShank.setTextureOffset(72, 66).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 12.0F, 8.0F, -2.0F, false);

		leftThigh = new ModelRenderer(this);
		leftThigh.setRotationPoint(9.0F, -21.0F, 0.0F);
		whole.addChild(leftThigh);

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, 9.0F, 0.0F);
		leftThigh.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.0F, -0.2618F);
		cube_r3.setTextureOffset(84, 86).addBox(-3.0F, -9.0F, -3.0F, 6.0F, 9.0F, 6.0F, 0.0F, false);

		leftShank = new ModelRenderer(this);
		leftShank.setRotationPoint(0.0F, 9.0F, 0.0F);
		leftThigh.addChild(leftShank);
		leftShank.setTextureOffset(0, 81).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 12.0F, 8.0F, 0.0F, false);
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
		this.lowerBody.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.lowerBody.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.upperBody.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.upperBody.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.leftShank.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
		this.leftWrist.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
		this.rightWrist.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		this.neck.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.neck.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.leftShoulder.rotateAngleX = MathHelper.cos(f * 0.6662F) * f1;
		this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.rightFist.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		this.rightShoulder.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * f1;
		this.rightRearThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
		this.rightFrontThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
		this.leftThigh.rotateAngleX = MathHelper.cos(f * 1.0F) * -1.0F * f1;
		this.rightRearShank.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
		this.rightFrontShank.rotateAngleX = MathHelper.cos(f * 1.0F) * 1.0F * f1;
	}
}