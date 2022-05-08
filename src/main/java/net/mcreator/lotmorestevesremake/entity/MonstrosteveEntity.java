
package net.mcreator.lotmorestevesremake.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.BossInfo;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.Direction;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import net.mcreator.lotmorestevesremake.potion.CursedDiversionPotionEffect;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.item.MinigunItem;
import net.mcreator.lotmorestevesremake.entity.renderer.MonstrosteveRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.CustomMathHelper;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import java.util.Random;
import java.util.EnumSet;

@LotmorestevesremakeModElements.ModElement.Tag
public class MonstrosteveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(4.5f, 7.5f)).build("monstrosteve").setRegistryName("monstrosteve");

	public MonstrosteveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 52);
		FMLJavaModLoadingContext.get().getModEventBus().register(new MonstrosteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16751002, -16724737, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("monstrosteve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 1, 1, 1));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				AggressiveSteveEntity::customSpawningConditionInLight);
		DungeonHooks.addDungeonMob(entity, 180);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 400);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 15);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 15);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 3);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.7);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 8);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		private static final DataParameter<Integer> ATTACK_STATE = EntityDataManager.createKey(CustomEntity.class, DataSerializers.VARINT);
		private static final DataParameter<Boolean> SHOOT_STATE = EntityDataManager.createKey(CustomEntity.class, DataSerializers.BOOLEAN);
		private static final DataParameter<Boolean> SUMMON_STATE = EntityDataManager.createKey(CustomEntity.class, DataSerializers.BOOLEAN);
		private static final DataParameter<Integer> DEATH_TICKS = EntityDataManager.createKey(CustomEntity.class, DataSerializers.VARINT);
		public int prevDeathTicks;
		public int attackProgress;
		private float[] clientSideStandAnimation0 = new float[2];
		private float[] clientSideStandAnimation = new float[2];
		public boolean shootActive;
		public boolean summonActive;

		@OnlyIn(Dist.CLIENT)
		public boolean isInRangeToRenderDist(double distance) {
			return true;
		}

		public static boolean whenToSpawn(IServerWorld worldIn) {
			return worldIn.getWorldInfo().getDayTime() > 24000 * 30;
		}

		protected void registerData() {
			super.registerData();
			this.dataManager.register(ATTACK_STATE, 0);
			this.dataManager.register(DEATH_TICKS, 0);
			this.dataManager.register(SHOOT_STATE, false);
			this.dataManager.register(SUMMON_STATE, false);
		}

		public void readAdditional(CompoundNBT compound) {
			super.readAdditional(compound);
			this.dataManager.set(DEATH_TICKS, compound.getInt("deathTicks"));
		}

		public void writeAdditional(CompoundNBT compound) {
			super.writeAdditional(compound);
			compound.putInt("deathTicks", this.getDeathTicks());
		}

		public void setDeathTicks(int value) {
			this.dataManager.set(DEATH_TICKS, value);
		}

		public int getDeathTicks() {
			return this.dataManager.get(DEATH_TICKS);
		}

		public void setAttackState(int value) {
			this.dataManager.set(ATTACK_STATE, value);
		}

		public int getAttackState() {
			return this.dataManager.get(ATTACK_STATE);
		}

		public void setShootState(boolean value) {
			this.dataManager.set(SHOOT_STATE, value);
		}

		public boolean getShootState() {
			return this.dataManager.get(SHOOT_STATE);
		}

		public void setSummonState(boolean value) {
			this.dataManager.set(SUMMON_STATE, value);
		}

		public boolean getSummonState() {
			return this.dataManager.get(SUMMON_STATE);
		}

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 200;
			setNoAI(false);
			stepHeight = 2;
			enablePersistence();
			this.moveForward = 3;
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(0, new CustomEntity.LockAngle());
			this.goalSelector.addGoal(1, new CustomEntity.ShootArrowGoal(this));
			this.goalSelector.addGoal(1, new CustomEntity.SummonGoal(this));
			this.goalSelector.addGoal(1, new CustomEntity.MoveAttackGoal(this, 1.2, 0, 48));
			/*this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					if (!CustomEntity.this.getShootState()) {
						if (CustomMathHelper.isEntityInBox(this.attacker.getAttackTarget(), this.attacker, 4) && !this.attacker.isSwingInProgress
								&& CustomEntity.this.getAttackState() == 0) {
							this.attacker.swingArm(Hand.MAIN_HAND);
							CustomEntity.this.setAttackState(1);
							CustomEntity.this.attackProgress = 0;
						} else if (!this.attacker.isSwingInProgress && CustomEntity.this.getAttackState() == 0
								&& CustomEntity.this.rand.nextInt(40) == 0) {
							this.attacker.swingArm(Hand.MAIN_HAND);
							CustomEntity.this.setAttackState(2);
							CustomEntity.this.attackProgress = 0;
						}
					}
				}
			});
			*/
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
		}

		public class MoveAttackGoal<T extends MonsterEntity> extends Goal {
			private final T entity;
			private final double moveSpeedAmp;
			private int attackTime = -1;
			private final float attackRadius;
			private int attackCooldown;
			private final float maxAttackDistance;
			private int seeTime;
			private boolean strafingClockwise;
			private boolean strafingBackwards;
			private int strafingTime = -1;

			public MoveAttackGoal(T mob, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
				this.entity = mob;
				this.moveSpeedAmp = moveSpeedAmpIn;
				this.attackCooldown = attackCooldownIn;
				this.attackRadius = maxAttackDistanceIn;
				this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
				this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
			}

			public void setAttackCooldown(int attackCooldownIn) {
				this.attackCooldown = attackCooldownIn;
			}

			/**
			 * Returns whether execution should begin. You can also read and cache any state
			 * necessary for execution in this method as well.
			 */
			public boolean shouldExecute() {
				return this.entity.getAttackTarget() == null ? false : true;
			}

			/**
			 * Returns whether an in-progress EntityAIBase should continue executing
			 */
			public boolean shouldContinueExecuting() {
				return (this.shouldExecute() || !this.entity.getNavigator().noPath());
			}

			/**
			 * Execute a one shot task or start executing a continuous task
			 */
			public void startExecuting() {
				super.startExecuting();
				this.entity.setAggroed(true);
			}

			/**
			 * Reset the task's internal state. Called when this task is interrupted by
			 * another one
			 */
			public void resetTask() {
				super.resetTask();
				this.entity.setAggroed(true);
				this.seeTime = 0;
				this.attackTime = -1;
			}

			/**
			 * Keep ticking a continuous task that has already been started
			 */
			public void tick() {
				LivingEntity livingentity = this.entity.getAttackTarget();
				if (livingentity != null) {
					double d0 = this.entity.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
					boolean flag = this.entity.getEntitySenses().canSee(livingentity);
					boolean flag1 = this.seeTime > 0;
					if (flag != flag1) {
						this.seeTime = 0;
					}
					if (flag) {
						++this.seeTime;
					} else {
						--this.seeTime;
					}
					if (!(d0 > (double) this.maxAttackDistance) && this.seeTime >= 20) {
						this.entity.getNavigator().clearPath();
						++this.strafingTime;
					} else {
						this.entity.getNavigator().tryMoveToEntityLiving(livingentity, this.moveSpeedAmp);
						this.strafingTime = -1;
					}
					if (this.strafingTime >= 20) {
						if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
							this.strafingClockwise = !this.strafingClockwise;
						}
						if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
							this.strafingBackwards = !this.strafingBackwards;
						}
						this.strafingTime = 0;
					}
					if (this.strafingTime > -1) {
						if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
							this.strafingBackwards = false;
						} else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
							this.strafingBackwards = true;
						}
						//this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
						this.entity.setMoveForward(this.strafingBackwards ? -1F : 1F);
						this.entity.setMoveStrafing(this.strafingClockwise ? 0.5F : -0.5F);
						this.entity.faceEntity(livingentity, 180.0F, 180.0F);
					} else {
						this.entity.getLookController().setLookPositionWithEntity(livingentity, 180.0F, 180.0F);
					}
					//this is not needed. I put this only for making the mob use melee attacks.
					this.checkAndPerformAttack(livingentity, d0);
				}
			}

			//this is not needed. I put this only for making the mob use melee attacks.
			protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
				if (!CustomEntity.this.getShootState()) {
					if (CustomMathHelper.isEntityInBox(this.entity.getAttackTarget(), this.entity, 4) && !this.entity.isSwingInProgress
							&& CustomEntity.this.getAttackState() == 0) {
						this.entity.swingArm(Hand.MAIN_HAND);
						CustomEntity.this.setAttackState(1);
						CustomEntity.this.attackProgress = 0;
					} else if (!this.entity.isSwingInProgress && CustomEntity.this.getAttackState() == 0 && CustomEntity.this.rand.nextInt(40) == 0) {
						this.entity.swingArm(Hand.MAIN_HAND);
						CustomEntity.this.setAttackState(2);
						CustomEntity.this.attackProgress = 0;
					}
				}
			}
		}

		public class LockAngle extends Goal {
			public LockAngle() {
				this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
			}

			public boolean shouldExecute() {
				return CustomEntity.this.getAttackTarget() != null && CustomEntity.this.getShootState();
			}

			public void tick() {
				CustomEntity.this.getLookController().setLookPositionWithEntity(CustomEntity.this.getAttackTarget(), 180, 180);
				CustomEntity.this.rotationYaw = CustomEntity.this.getYaw(1);
			}
		}

		public class SummonGoal extends Goal {
			private Random random = new Random();
			private int bullet;
			private int shootCooltime = 350;
			private CustomEntity entityIn;

			public SummonGoal(CustomEntity mob) {
				entityIn = mob;
			}

			public boolean shouldExecute() {
				return true;
			}

			public void tick() {
				if (this.bullet > 0) {
					if (entityIn.summonActive) {
						this.bullet--;
						entityIn.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 2, 0.5f);
						if (this.bullet == 0)
							this.shootCooltime = 200 + random.nextInt(200);
						entityIn.rotationYaw = entityIn.getYaw(1);
						entityIn.prevRotationYaw = entityIn.rotationYaw;
						if (this.bullet % 3 == 0)
							if (world instanceof ServerWorld) {
								float f = (entityIn.renderYawOffset + (float) (180 * (random.nextInt(3) - 1))) * ((float) Math.PI / 180F);
								float f1 = MathHelper.cos(f);
								float f2 = MathHelper.sin(f);
								double x = entityIn.getPosX() + f1 * 1.5;
								double y = entityIn.getPosY() + 8;
								double z = entityIn.getPosZ() + f2 * 1.5;
								Entity entityToSpawn = new StecubeEntity.CustomEntity(StecubeEntity.entity, (World) world);
								entityToSpawn.setLocationAndAngles(x, y, z, 0, 0);
								entityIn.shoot(entityIn.getLookVec().x, entityIn.getLookVec().y, entityIn.getLookVec().z, 2, 15, entityToSpawn);
								world.addEntity(entityToSpawn);
							}
					}
					if (this.bullet == 0)
						entityIn.playSound(SoundEvents.BLOCK_PISTON_CONTRACT, 2, 0.5f);
				} else {
					entityIn.setSummonState(false);
					if (this.shootCooltime > 0)
						this.shootCooltime--;
					else if (entityIn.getAttackTarget() != null) {
						this.bullet = 10 + random.nextInt(10);
						entityIn.playSound(SoundEvents.BLOCK_PISTON_EXTEND, 2, 0.5f);
						entityIn.setSummonState(true);
					}
				}
			}
		}

		public class ShootArrowGoal extends Goal {
			private Random random = new Random();
			private int bullet;
			private int shootCooltime = 200;
			private CustomEntity entityIn;

			public ShootArrowGoal(CustomEntity mob) {
				entityIn = mob;
			}

			public boolean shouldExecute() {
				return true;
			}

			public void tick() {
				if (this.bullet > 0) {
					entityIn.addPotionEffect(new EffectInstance(Effects.SLOWNESS, (int) 5, (int) 5));
					if (entityIn.shootActive) {
						this.bullet--;
						entityIn.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 2, 1f);
						if (this.bullet == 0)
							this.shootCooltime = 120 + random.nextInt(120);
						entityIn.rotationYaw = entityIn.getYaw(1);
						entityIn.prevRotationYaw = entityIn.rotationYaw;
						for (int i = 0; i < 2; i++)
							if (world instanceof ServerWorld) {
								double x = entityIn.getPosX();
								double y = entityIn.getPosY() + entityIn.getEyeHeight();
								double z = entityIn.getPosZ();
								AbstractArrowEntity entityToSpawn = new MinigunItem.ArrowCustomEntity(MinigunItem.arrow, entityIn, (World) world);
								entityToSpawn.setLocationAndAngles(x, y, z, 0, 0);
								entityToSpawn.setDamage(2);
								entityToSpawn.setKnockbackStrength(1);
								entityIn.shoot(entityIn.getLookVec().x, entityIn.getLookVec().y, entityIn.getLookVec().z, 5, 15, entityToSpawn);
								world.addEntity(entityToSpawn);
							}
					}
					if (this.bullet == 0)
						entityIn.playSound(SoundEvents.BLOCK_PISTON_CONTRACT, 2, 0.5f);
				} else {
					entityIn.setShootState(false);
					if (this.shootCooltime > 0)
						this.shootCooltime--;
					else if (entityIn.getAttackTarget() != null) {
						this.bullet = 20 + random.nextInt(31);
						entityIn.playSound(SoundEvents.BLOCK_PISTON_EXTEND, 2, 0.5f);
						entityIn.setShootState(true);
					}
				}
			}
		}

		protected void onDeathUpdate() {
			this.prevDeathTicks = this.getDeathTicks();
			this.setDeathTicks(this.getDeathTicks() + 1);
			if (this.getDeathTicks() > 200)
				this.remove();
		}

		protected void collideWithEntity(Entity entityIn) {
			if (this.isAlive())
				super.collideWithEntity(entityIn);
		}

		@OnlyIn(Dist.CLIENT)
		public float getDeathAnimationScale(float p_189795_1_) {
			return MathHelper.lerp(p_189795_1_, this.prevDeathTicks, this.getDeathTicks());
		}

		public void livingTick() {
			super.livingTick();
			if (this.isAlive()) {
				this.attackProcedure();
				this.clientSideStandAnimation0[0] = this.clientSideStandAnimation[0];
				this.clientSideStandAnimation0[1] = this.clientSideStandAnimation[1];
				if (this.getShootState()) {
					this.clientSideStandAnimation[0] = MathHelper.clamp(this.clientSideStandAnimation[0] + 1.0F, 0.0F, 6.0F);
					if (this.clientSideStandAnimation[0] == 6)
						this.shootActive = true;
				} else {
					this.clientSideStandAnimation[0] = MathHelper.clamp(this.clientSideStandAnimation[0] - 1.0F, 0.0F, 6.0F);
					this.shootActive = false;
				}
				if (this.getSummonState()) {
					this.clientSideStandAnimation[1] = MathHelper.clamp(this.clientSideStandAnimation[1] + 1.0F, 0.0F, 17.0F);
					if (this.clientSideStandAnimation[1] == 17)
						this.summonActive = true;
				} else {
					this.clientSideStandAnimation[1] = MathHelper.clamp(this.clientSideStandAnimation[1] - 1.0F, 0.0F, 17.0F);
					this.summonActive = false;
				}
			}
			if (this.collidedHorizontally && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
				boolean flag = false;
				AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(0.6D);
				for (BlockPos blockpos : BlockPos.getAllInBoxMutable(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY),
						MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY),
						MathHelper.floor(axisalignedbb.maxZ))) {
					BlockState blockstate = this.world.getBlockState(blockpos);
					Block block = blockstate.getBlock();
					if (block instanceof LeavesBlock) {
						flag = this.world.destroyBlock(blockpos, true, this) || flag;
					}
				}
				if (!flag && this.onGround) {
					this.jump();
				}
			}
		}

		@OnlyIn(Dist.CLIENT)
		public float getAnimationScale(float p_189795_1_, int i) {
			return MathHelper.lerp(p_189795_1_, this.clientSideStandAnimation0[i], this.clientSideStandAnimation[i]);
		}

		protected PathNavigator createNavigator(World worldIn) {
			return new CustomEntity.Navigator(this, worldIn);
		}

		static class Navigator extends GroundPathNavigator {
			public Navigator(MobEntity p_i50754_1_, World p_i50754_2_) {
				super(p_i50754_1_, p_i50754_2_);
			}

			protected PathFinder getPathFinder(int p_179679_1_) {
				this.nodeProcessor = new CustomEntity.Processor();
				return new PathFinder(this.nodeProcessor, p_179679_1_);
			}
		}

		static class Processor extends WalkNodeProcessor {
			private Processor() {
			}

			protected PathNodeType func_215744_a(IBlockReader p_215744_1_, boolean p_215744_2_, boolean p_215744_3_, BlockPos p_215744_4_,
					PathNodeType p_215744_5_) {
				return p_215744_5_ == PathNodeType.LEAVES
						? PathNodeType.OPEN
						: super.func_215744_a(p_215744_1_, p_215744_2_, p_215744_3_, p_215744_4_, p_215744_5_);
			}
		}

		@Override
		protected void updateArmSwingProgress() {
			int i = 40;
			if (this.getAttackState() == 2)
				i = 10;
			if (this.isSwingInProgress) {
				++this.swingProgressInt;
				if (this.swingProgressInt >= i) {
					this.swingProgressInt = 0;
					this.isSwingInProgress = false;
				}
			} else {
				this.swingProgressInt = 0;
				this.setAttackState(0);
			}
			this.swingProgress = (float) this.swingProgressInt / (float) i;
		}

		public int getHorizontalFaceSpeed() {
			return 20;
		}

		public void attackProcedure() {
			if (this.isSwingInProgress) {
				if (this.getAttackState() == 1) {
					if (this.swingProgress > 0.7 && this.attackProgress == 0) {
						for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class,
								CustomMathHelper.makeHorizontalAttackRange(this, (double) this.getHeight(), 0, (double) this.getWidth(),
										(double) this.getHeight() * 0.8f, (double) this.getWidth()))) {
							if (!this.isOnSameTeam(livingentity)) {
								livingentity.hurtResistantTime = 0;
								livingentity.addPotionEffect(new EffectInstance(CursedDiversionPotionEffect.potion, (int) 200, (int) 0));
								if (livingentity.attackEntityFrom(DamageSource.causeMobDamage(this),
										(float) (this.getAttackDamage() / MathHelper.clamp(Math.pow(this.getDistanceSq(livingentity), 0.5), 1, 5))
												* 0.6f)) {
									this.applyEnchantments(this, livingentity);
									livingentity.setMotion(livingentity.getMotion().add(0, -1, 0));
								}
							}
						}
						this.attackProgress++;
					}
					if (this.swingProgress > 0.8 && this.attackProgress == 1) {
						int i = (int) (this.getWidth() * 5);
						for (int j = 0; j < i * 8; ++j) {
							float f = this.rand.nextFloat() * ((float) Math.PI * 2F);
							float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
							float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
							float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
							double a = this.getPosX() + (double) f2;
							double t = this.getPosY();
							double k = this.getPosZ() + (double) f3;
							BlockPos pos = new BlockPos((int) a, (int) t, (int) k);
							boolean flag = false;
							double d0 = 0.0D;
							do {
								BlockPos blockpos1 = pos.down();
								BlockState blockstate = this.world.getBlockState(blockpos1);
								if (blockstate.isSolidSide(this.world, blockpos1, Direction.UP)) {
									if (!this.world.isAirBlock(pos)) {
										BlockState blockstate1 = this.world.getBlockState(pos);
										VoxelShape voxelshape = blockstate1.getCollisionShape(this.world, pos);
										if (!voxelshape.isEmpty()) {
											d0 = voxelshape.getEnd(Direction.Axis.Y);
										}
									}
									flag = true;
									break;
								}
								pos = pos.down();
							} while (pos.getY() >= MathHelper.floor(this.getHeight() * 1.5f) - 1);
							if (flag) {
								pos = new BlockPos((int) a, (int) t + d0 - 1, (int) k);
								BlockState blockstate = this.world.getBlockState(pos);
								if (rand.nextInt(6) == 0)
									world.playEvent(2001, new BlockPos(a, t + d0, k), Block.getStateId(blockstate));
							}
						}
						if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
							boolean flag = false;
							AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(2, 0.2, 2);
							for (BlockPos blockpos : BlockPos.getAllInBoxMutable(MathHelper.floor(axisalignedbb.minX),
									MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX),
									MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
								BlockState blockstate = this.world.getBlockState(blockpos);
								Block block = blockstate.getBlock();
								if (rand.nextInt(3) == 0 && block.getExplosionResistance() < 1200 && !world.isRemote()) {
									flag = this.world.destroyBlock(blockpos, true, this) || flag;
								}
							}
						}
						for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class,
								CustomMathHelper.makeHorizontalAttackRange(this, 0, 0, (double) this.getWidth() * 5, (double) this.getHeight() * 0.8f,
										(double) this.getWidth() * 5))) {
							if (!this.isOnSameTeam(livingentity) && this.getDistance(livingentity) < this.getWidth() * 2.5f) {
								livingentity.hurtResistantTime = 0;
								livingentity.addPotionEffect(new EffectInstance(CursedDiversionPotionEffect.potion, (int) 200, (int) 0));
								if (livingentity.attackEntityFrom(DamageSource.causeMobDamage(this), 3 * this.getAttackDamage()
										/ (float) MathHelper.clamp(Math.pow(this.getDistanceSq(livingentity), 0.2), 1, 5))) {
									Vector3d posVector = new Vector3d(livingentity.getPosX() - this.getPosX(), 0,
											livingentity.getPosZ() - this.getPosZ());
									posVector.normalize();
									this.applyEnchantments(this, livingentity);
									livingentity.setMotion(posVector.scale(Math.pow(this.getKnockBackPower(), 0.5) * 0.1));
									livingentity.setMotion(livingentity.getMotion().add(0, 1, 0));
									//this.getKnockBackPower() / (float) MathHelper.clamp(Math.pow(this.getDistanceSq(livingentity), 0.5))
									/*livingentity.applyKnockback(
											this.getKnockBackPower()
													/ (float) MathHelper.clamp(Math.pow(this.getDistanceSq(livingentity), 0.5), 1, 5),
											-livingentity.getPosX() + this.getPosX(), -livingentity.getPosZ() + this.getPosZ());*/
								}
							}
						}
						this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, 1, 0.5f);
						this.attackProgress++;
					}
				} else if (this.getAttackState() == 2) {
					if (this.attackProgress == 0) {
						if (this.getAttackTarget() != null) {
							this.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 2, 0.5f);
							if (world instanceof ServerWorld) {
								float f = (this.renderYawOffset + (float) (180 * (this.rand.nextInt(3) - 1))) * ((float) Math.PI / 180F);
								float f1 = MathHelper.cos(f);
								float f2 = MathHelper.sin(f);
								double x = this.getPosX() + f1 * 3.5;
								double y = this.getPosY() + 5;
								double z = this.getPosZ() + f2 * 3.5;
								Entity entityToSpawn = new SpinningSteveEntity.CustomEntity(SpinningSteveEntity.entity, (World) world);
								entityToSpawn.setLocationAndAngles(x, y, z, world.getRandom().nextFloat() * 360F, 0);
								((SpinningSteveEntity.CustomEntity) entityToSpawn).mustExplode = true;
								this.shoot(this.getAttackTarget().getPosX() - this.getPosX(), this.getAttackTarget().getPosY() - this.getPosY(),
										this.getAttackTarget().getPosZ() - this.getPosZ(),
										(float) Math.pow(this.getDistance(this.getAttackTarget()) - 2, 0.8) * 0.2f, 1, entityToSpawn);
								entityToSpawn.setMotion(entityToSpawn.getMotion().add(0, 2, 0));
								if (entityToSpawn instanceof MobEntity)
									((MobEntity) entityToSpawn).onInitialSpawn((ServerWorld) world,
											world.getDifficultyForLocation(entityToSpawn.getPosition()), SpawnReason.MOB_SUMMONED,
											(ILivingEntityData) null, (CompoundNBT) null);
								world.addEntity(entityToSpawn);
							}
						}
						this.attackProgress++;
					}
				}
			}
		}

		public void shoot(double x, double y, double z, float velocity, float inaccuracy, Entity entityIn) {
			Vector3d vector3d = (new Vector3d(x, y, z)).normalize()
					.add(this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
							this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
							this.rand.nextGaussian() * (double) 0.0075F * (double) inaccuracy)
					.scale((double) velocity);
			entityIn.setMotion(vector3d);
			float f = MathHelper.sqrt(horizontalMag(vector3d));
			entityIn.rotationYaw = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI));
			entityIn.rotationPitch = (float) (MathHelper.atan2(vector3d.y, (double) f) * (double) (180F / (float) Math.PI));
			entityIn.prevRotationYaw = entityIn.rotationYaw;
			entityIn.prevRotationPitch = entityIn.rotationPitch;
		}

		public float getAttackDamage() {
			return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
		}

		public float getKnockBackPower() {
			return (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public boolean canDespawn(double distanceToClosestPlayer) {
			return false;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:strange_steve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:strange_steve_hurt"));
		}

		@Override
		public boolean isNonBoss() {
			return false;
		}

		protected float getSoundPitch() {
			return 0.5f;
		}

		protected float getSoundVolume() {
			return 1.5f;
		}

		private final ServerBossInfo bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS);

		@Override
		public void addTrackingPlayer(ServerPlayerEntity player) {
			super.addTrackingPlayer(player);
			this.bossInfo.addPlayer(player);
		}

		@Override
		public void removeTrackingPlayer(ServerPlayerEntity player) {
			super.removeTrackingPlayer(player);
			this.bossInfo.removePlayer(player);
		}

		@Override
		public void updateAITasks() {
			super.updateAITasks();
			if (this.isAggressive())
				this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
		}
	}
}
