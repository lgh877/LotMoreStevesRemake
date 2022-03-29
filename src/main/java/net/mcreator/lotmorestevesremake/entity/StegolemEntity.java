
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

import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.world.IServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.StegolemRenderer;
import net.mcreator.lotmorestevesremake.StevindicatorDetectBlockGoal;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import java.util.Random;
import java.util.EnumSet;

@LotmorestevesremakeModElements.ModElement.Tag
public class StegolemEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(1.4f, 2.7f)).build("stegolem").setRegistryName("stegolem");

	public StegolemEntity(LotmorestevesremakeModElements instance) {
		super(instance, 25);
		FMLJavaModLoadingContext.get().getModEventBus().register(new StegolemRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -6710887, -16737844, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("stegolem_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(entity, 6, 1, 1));
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
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 150);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 10);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		private static final DataParameter<Integer> ATTACK_STATE = EntityDataManager.createKey(CustomEntity.class, DataSerializers.VARINT);
		public boolean isAttackSucceed = true;
		public boolean isLanded;

		public static boolean whenToSpawn(IServerWorld worldIn) {
			return worldIn.getWorldInfo().getDayTime() > 24000 * 10;
		}

		protected void registerData() {
			super.registerData();
			this.dataManager.register(ATTACK_STATE, 0);
		}

		public void setAttackState(int value) {
			this.dataManager.set(ATTACK_STATE, value);
		}

		public int getAttackState() {
			return this.dataManager.get(ATTACK_STATE);
		}

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 25;
			setNoAI(false);
			this.stepHeight = 1.5f;
			this.isLanded = true;
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		public float getAttackDamage() {
			return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
		}

		public boolean onLivingFall(float a, float b) {
			this.isLanded = true;
			return false;
		}

		@Override
		protected void updateArmSwingProgress() {
			int i = 20;
			if (this.isSwingInProgress) {
				++this.swingProgressInt;
				if (this.swingProgressInt >= i) {
					this.swingProgressInt = 0;
					this.isSwingInProgress = false;
					this.setAttackState(0);
				}
			} else {
				this.swingProgressInt = 0;
			}
			this.swingProgress = (float) this.swingProgressInt / (float) i;
		}

		@Override
		public boolean startRiding(Entity entity, boolean force) {
			return false;
		}

		protected float getSoundPitch() {
			return 0.8f;
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new CustomEntity.LockAngle());
			this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					double d0 = this.getAttackReachSqr(enemy);
					Random random = new Random();
					if (!(attacker.isInWater() || attacker.isInLava()) && random.nextInt(15) == 0) {
						if (!this.attacker.isSwingInProgress && distToEnemySqr * 0.03f <= d0 * 2 && CustomEntity.this.isAttackSucceed) {
							CustomEntity.this.swingArm(Hand.MAIN_HAND);
							CustomEntity.this.isAttackSucceed = false;
							CustomEntity.this.setAttackState(1);
						}
					} else if (!this.attacker.isSwingInProgress && distToEnemySqr <= d0) {
						CustomEntity.this.swingArm(Hand.MAIN_HAND);
						CustomEntity.this.attackEntityAsMob(enemy);
						CustomEntity.this.setAttackState(2);
						CustomEntity.this.isAttackSucceed = true;
					}
				}
			});
			this.goalSelector.addGoal(6, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
			this.applyEntityAI();
		}

		protected void applyEntityAI() {
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(this, 1, (int) 5));
		}

		public void livingTick() {
			super.livingTick();
			if (this.isAlive()) {
				if (!this.onGround && !this.isLanded) {
					if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
						boolean flag = false;
						AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(1, 1, 1);
						for (BlockPos blockpos : BlockPos.getAllInBoxMutable(MathHelper.floor(axisalignedbb.minX),
								MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX),
								MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
							BlockState blockstate = this.world.getBlockState(blockpos);
							Block block = blockstate.getBlock();
							if (rand.nextInt(9) == 0 && block.getExplosionResistance() < 100 && !world.isRemote()) {
								flag = this.world.destroyBlock(blockpos, true, this) || flag;
							}
						}
					}
					for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(1, 1, 1))) {
						if (!this.isOnSameTeam(livingentity) && livingentity.hurtResistantTime == 0)
							this.attackEntityAsMob(livingentity);
					}
				}
				if (this.isInWater() || this.isInLava() || this.onGround)
					this.isLanded = true;
				else if (((!this.getNavigator().noPath() && rand.nextInt(80) == 0) || this.collidedHorizontally && this.onGround)
						&& !this.isSwingInProgress && this.isAttackSucceed) {
					this.swingArm(Hand.MAIN_HAND);
					this.setAttackState(1);
					this.isAttackSucceed = false;
				}
				if (this.getAttackState() == 1 && this.swingProgress > 0.8f && !this.isAttackSucceed) {
					if (this.onGround)
						this.jump();
					this.isAttackSucceed = true;
				}
			}
		}

		public boolean attackEntityAsMob(Entity entityIn) {
			return super.attackEntityAsMob(entityIn);
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			return super.attackEntityFrom(source, amount);
		}

		public class LockAngle extends Goal {
			public LockAngle() {
				this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
			}

			public boolean shouldExecute() {
				return CustomEntity.this.isSwingInProgress && CustomEntity.this.getAttackTarget() != null && CustomEntity.this.getAttackState() == 1;
			}

			public void tick() {
				if (CustomEntity.this.swingProgress < 0.5f) {
					CustomEntity.this.faceEntity(CustomEntity.this.getAttackTarget(), 30F, 30F);
				}
			}
		}

		protected void jump() {
			if (this.swingProgress > 0.8f && !this.isAttackSucceed && this.getAttackState() == 1) {
				float multiplier = 1;
				if (this.getAttackTarget() != null) {
					multiplier = (float) Math.pow(this.getDistance(this.getAttackTarget()), 0.5f) * 0.5f;
				}
				this.isLanded = false;
				this.setMotion(this.getLookVec().x * multiplier, this.getJumpFactor() * (rand.nextFloat() + 1), this.getLookVec().z * multiplier);
			}
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:great_boy_steve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:great_boy_steve_hurt"));
		}
	}
}
