
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

import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.block.BlockState;

import net.mcreator.lotmorestevesremake.procedures.GhosteveSpawningConditionProcedure;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.GhosteveRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import javax.annotation.Nullable;

import java.util.stream.Stream;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;
import java.util.AbstractMap;

@LotmorestevesremakeModElements.ModElement.Tag
public class GhosteveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER).immuneToFire()
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("ghosteve").setRegistryName("ghosteve");

	public GhosteveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 45);
		FMLJavaModLoadingContext.get().getModEventBus().register(new GhosteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -9865851, -8346677, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("ghosteve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 100, 1, 1));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS,
				Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (entityType, world, reason, pos, random) -> {
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();
					return GhosteveSpawningConditionProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("world", world))
							.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
				});
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 20);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 2);
			ammma = ammma.createMutableAttribute(Attributes.FLYING_SPEED, 0.3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		private static final DataParameter<Integer> AVOID = EntityDataManager.createKey(CustomEntity.class, DataSerializers.VARINT);

		protected void registerData() {
			super.registerData();
			this.dataManager.register(AVOID, 0);
		}

		public void setAvoidState(int value) {
			this.dataManager.set(AVOID, value);
		}

		public int getAvoidState() {
			return this.dataManager.get(AVOID);
		}

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 6;
			setNoAI(false);
			stepHeight = 5;
			jumpMovementFactor = 0.2f;
			this.moveController = new FlyingMovementController(this, 180, true);
			this.navigator = new FlyingPathNavigator(this, this.world);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 0.8, 20) {
				@Override
				protected Vector3d getPosition() {
					Random random = CustomEntity.this.getRNG();
					double dir_x = CustomEntity.this.getPosX() + ((random.nextFloat() * 2 - 1) * 32);
					double dir_y = CustomEntity.this.getPosY() + ((random.nextFloat() * 2 - 1) * 16);
					double dir_z = CustomEntity.this.getPosZ() + ((random.nextFloat() * 2 - 1) * 32);
					return new Vector3d(dir_x, dir_y, dir_z);
				}
			});
			this.goalSelector.addGoal(1, new AvoidEntityGoal(this, LivingEntity.class, (float) 6, 1.5, 2) {
				public boolean shouldExecute() {
					if (this.entity.getAttackTarget() != null && CustomEntity.this.getAvoidState() > 0) {
						this.avoidTarget = this.entity.getAttackTarget();
						if (this.avoidTarget == null) {
							return false;
						} else {
							Vector3d vector3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7,
									this.avoidTarget.getPositionVec());
							if (vector3d == null) {
								return false;
							} else if (this.avoidTarget.getDistanceSq(vector3d.x, vector3d.y, vector3d.z) < this.avoidTarget
									.getDistanceSq(this.entity)) {
								return false;
							} else {
								this.path = this.navigation.getPathToPos(vector3d.x, vector3d.y, vector3d.z, 0);
								return this.path != null;
							}
						}
					} else
						return false;
				}

				public void startExecuting() {
					super.startExecuting();
					this.entity.jumpMovementFactor = 0.4f;
					this.entity.setSprinting(false);
				}

				public void resetTask() {
					super.resetTask();
					this.entity.jumpMovementFactor = 0.1f;
				}
			});
			this.goalSelector.addGoal(2, new Goal() {
				{
					this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
				}

				public boolean shouldExecute() {
					if (CustomEntity.this.getAttackTarget() != null && !CustomEntity.this.getMoveHelper().isUpdating()
							&& CustomEntity.this.getAvoidState() == 0) {
						return true;
					} else {
						return false;
					}
				}

				@Override
				public boolean shouldContinueExecuting() {
					return CustomEntity.this.getMoveHelper().isUpdating() && CustomEntity.this.getAttackTarget() != null
							&& CustomEntity.this.getAttackTarget().isAlive();
				}

				@Override
				public void startExecuting() {
					LivingEntity livingentity = CustomEntity.this.getAttackTarget();
					Random random = new Random();
					CustomEntity.this.setSprinting(true);
					Vector3d vec3d = livingentity.getEyePosition(1);
					CustomEntity.this.moveController.setMoveTo(vec3d.x, vec3d.y - livingentity.getEyeHeight() - random.nextFloat() - 0.2f, vec3d.z,
							1);
				}

				public void resetTask() {
					super.resetTask();
					CustomEntity.this.setSprinting(false);
				}

				@Override
				public void tick() {
					LivingEntity livingentity = CustomEntity.this.getAttackTarget();
					CustomEntity.this.getLookController().setLookPositionWithEntity(CustomEntity.this.getAttackTarget(), 180, 180);
					if (CustomEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
						CustomEntity.this.attackEntityAsMob(livingentity);
						CustomEntity.this.swingArm(Hand.MAIN_HAND);
					} else {
						double d0 = CustomEntity.this.getDistanceSq(livingentity);
						Random random = new Random();
						if (d0 < 32) {
							Vector3d vec3d = livingentity.getEyePosition(1);
							CustomEntity.this.moveController.setMoveTo(vec3d.x, vec3d.y - livingentity.getEyeHeight() - random.nextFloat() - 0.2f,
									vec3d.z, 1);
						}
					}
				}
			});

			this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
			this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, PlayerEntity.class, false, false));
			this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, ServerPlayerEntity.class, false, false));
			this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, MobEntity.class, false, false));
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			if (source == DamageSource.IN_WALL)
				return false;
			if (this.rand.nextInt(2) == 0) {
				this.setAvoidState(MathHelper.clamp(this.getAvoidState() - 10, 0, 30));
				return super.attackEntityFrom(source, amount);
			} else {
				this.playSound(SoundEvents.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 1, 2);
				return false;
			}
		}

		public boolean attackEntityAsMob(Entity entityIn) {
			this.setAvoidState(60);
			return super.attackEntityAsMob(entityIn);
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return this.canBlockDamageSource(ds) && this.isHandActive()
					? (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.block"))
					: (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
							.getValue(new ResourceLocation("lotmorestevesremake:ghosteve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lotmorestevesremake:ghosteve_hurt"));
		}

		@Override
		public boolean onLivingFall(float l, float d) {
			return false;
		}

		@Override
		protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
		}

		@Override
		public void setNoGravity(boolean ignored) {
			super.setNoGravity(true);
		}

		public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason,
				@Nullable ILivingEntityData livingdata, @Nullable CompoundNBT tag) {
			ILivingEntityData retval = super.onInitialSpawn(world, difficulty, reason, livingdata, tag);
			this.setEquipmentBasedOnDifficulty(difficulty);
			this.setEnchantmentBasedOnDifficulty(difficulty);
			return retval;
		}

		protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
			super.setEquipmentBasedOnDifficulty(difficulty);
			if (this.rand.nextFloat() < 0.2f) {
				int i = this.rand.nextInt(3);
				if (i == 0) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
				} else if (i == 1) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
				} else if (i == 2) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.WOODEN_PICKAXE));
					this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SHIELD));
				}
			}
		}

		protected boolean canBlockDamageSource(DamageSource damageSourceIn) {
			Entity entity = damageSourceIn.getImmediateSource();
			boolean flag = false;
			if (entity instanceof AbstractArrowEntity) {
				AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity) entity;
				if (abstractarrowentity.getPierceLevel() > 0) {
					flag = true;
				}
			}
			if (!damageSourceIn.isUnblockable() && this.isActiveItemStackBlocking() && !flag) {
				Vector3d vector3d2 = damageSourceIn.getDamageLocation();
				if (vector3d2 != null) {
					Vector3d vector3d = this.getLook(1.0F);
					Vector3d vector3d1 = vector3d2.subtractReverse(this.getPositionVec()).normalize();
					vector3d1 = new Vector3d(vector3d1.x, 0.0D, vector3d1.z);
					if (vector3d1.dotProduct(vector3d) < 0.0D) {
						return true;
					}
				}
			}
			return false;
		}

		public void livingTick() {
			this.noClip = true;
			super.livingTick();
			if (this.getAvoidState() > 0) {
				this.setAvoidState(this.getAvoidState() - 1);
			}
			if (rand.nextInt(80) == 0) {
				this.resetActiveHand();
			}
			if (rand.nextInt(40) == 0)
				this.setActiveHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof ShieldItem));
		}
	}
}
