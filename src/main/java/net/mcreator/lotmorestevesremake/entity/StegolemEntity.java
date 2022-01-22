
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
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import net.mcreator.lotmorestevesremake.procedures.StegolemSpawnConditionProcedure;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.StegolemRenderer;
import net.mcreator.lotmorestevesremake.StevindicatorDetectBlockGoal;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;
import java.util.AbstractMap;

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
				(entityType, world, reason, pos, random) -> {
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();
					return StegolemSpawnConditionProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("world", world))
							.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
				});
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2);
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
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		public boolean isEntityInBox(LivingEntity entityIn, double sizeup) {
			for (LivingEntity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(sizeup))) {
				if (entity == entityIn) {
					return true;
				}
			}
			return false;
		}

		public AxisAlignedBB makeHorizontalAttackRange(LivingEntity entityIn, double height, double distTo, double sizeX, double sizeY,
				double sizeZ) {
			double x = entityIn.getPosX() - Math.sin((Math.toRadians((entityIn.getYaw(1))))) * distTo;
			double y = entityIn.getPosY() + height;
			double z = entityIn.getPosZ() + Math.cos((Math.toRadians((entityIn.getYaw(1))))) * distTo;
			AxisAlignedBB attackRange = new AxisAlignedBB(x - (sizeX / 2d), y - (sizeY / 2), z - (sizeZ / 2d), x + (sizeX / 2d), y + (sizeY / 2),
					z + (sizeZ / 2d));
			return attackRange;
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
					if (!this.attacker.isSwingInProgress && distToEnemySqr * 0.1f <= d0 * 2 && CustomEntity.this.isAttackSucceed) {
						CustomEntity.this.swingArm(Hand.MAIN_HAND);
						CustomEntity.this.isAttackSucceed = false;
					}
				}
			});
			this.goalSelector.addGoal(6, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
			this.applyEntityAI();
		}

		protected void applyEntityAI() {
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(Blocks.CRAFTING_TABLE, this, 1, (int) 6));
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(Blocks.FURNACE, this, 1, (int) 6));
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(Blocks.BLAST_FURNACE, this, 1, (int) 6));
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(Blocks.BOOKSHELF, this, 1, (int) 6));
		}

		public void livingTick() {
			super.livingTick();
			if (this.isAlive()) {
				if ((this.collidedHorizontally && this.onGround || this.getNavigator().getPath() != null && rand.nextInt(80) == 0)
						&& !this.isSwingInProgress && this.isAttackSucceed) {
					this.swingArm(Hand.MAIN_HAND);
					this.isAttackSucceed = false;
				}
				if (!this.onGround && !this.isLanded) {
					if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this) && rand.nextInt(5) == 0) {
						boolean flag = false;
						AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(1, 1, 1);
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
					for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(1, 1, 1))) {
						if (!this.isOnSameTeam(livingentity) && livingentity.hurtResistantTime == 0)
							this.attackEntityAsMob(livingentity);
					}
				}
				if (this.swingProgress > 0.8f && !this.isAttackSucceed) {
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
				return CustomEntity.this.isSwingInProgress && CustomEntity.this.getAttackTarget() != null;
			}

			public void tick() {
				if (CustomEntity.this.swingProgress < 0.5f) {
					CustomEntity.this.faceEntity(CustomEntity.this.getAttackTarget(), 30F, 30F);
				}
			}
		}

		protected void jump() {
			if (this.swingProgress > 0.8f && !this.isAttackSucceed) {
				this.isLanded = false;
				this.setMotion(this.getLookVec().x, this.getJumpFactor() * (rand.nextFloat() + 1), this.getLookVec().z);
			}
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:mechanical_steve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:mechanical_steve_hurt"));
		}
	}
}
