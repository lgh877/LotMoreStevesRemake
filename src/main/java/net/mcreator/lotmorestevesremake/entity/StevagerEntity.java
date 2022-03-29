
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
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

import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.StevagerRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import javax.annotation.Nullable;

@LotmorestevesremakeModElements.ModElement.Tag
public class StevagerEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new).size(1.5f, 2f))
					.build("stevager").setRegistryName("stevager");

	public StevagerEntity(LotmorestevesremakeModElements instance) {
		super(instance, 16);
		FMLJavaModLoadingContext.get().getModEventBus().register(new StevagerRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16751002, -10092493, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("stevager_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(entity, 6, 1, 1));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				AggressiveSteveEntity::customSpawningConditionInLight);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 100);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 6);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.75);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.5);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 20;
			setNoAI(false);
			stepHeight = 1;
		}

		public static boolean whenToSpawn(IServerWorld worldIn) {
			return worldIn.getWorldInfo().getDayTime() > 24000 * 8;
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		protected void updateMovementGoalFlags() {
			boolean flag = !(this.getControllingPassenger() instanceof MobEntity) || EntityTypeTags.getCollection()
					.getTagByID(new ResourceLocation("lotmorestevesremake:we_are_steves")).contains(this.getControllingPassenger().getType());
			boolean flag1 = !(this.getRidingEntity() instanceof BoatEntity);
			this.goalSelector.setFlag(Goal.Flag.MOVE, flag);
			this.goalSelector.setFlag(Goal.Flag.JUMP, flag && flag1);
			this.goalSelector.setFlag(Goal.Flag.LOOK, flag);
			this.goalSelector.setFlag(Goal.Flag.TARGET, flag);
		}

		public boolean canBeSteered() {
			return !this.isAIDisabled() && this.getControllingPassenger() instanceof LivingEntity;
		}

		@Nullable
		public Entity getControllingPassenger() {
			return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					double d0 = this.getAttackReachSqr(enemy);
					if (distToEnemySqr <= d0 && !this.attacker.isSwingInProgress) {
						this.attacker.swingArm(Hand.MAIN_HAND);
						this.attacker.attackEntityAsMob(enemy);
					}
				}

				protected double getAttackReachSqr(LivingEntity attackTarget) {
					float f = CustomEntity.this.getWidth() - 0.1F;
					return (double) (f * 2.0F * f * 2.0F + attackTarget.getWidth());
				}
			});
			this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, (float) 6));
			this.goalSelector.addGoal(4, new LookAtGoal(this, ServerPlayerEntity.class, (float) 6));
			this.goalSelector.addGoal(4, new LookAtGoal(this, MobEntity.class, (float) 6));
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
		}

		public double getMountedYOffset() {
			return 1.2;
		}

		@Override
		protected void updateArmSwingProgress() {
			int i = 10;
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

		public boolean attackEntityAsMob(Entity entityIn) {
			this.playSound(SoundEvents.BLOCK_PISTON_EXTEND, this.getSoundVolume(), this.getSoundPitch());
			return super.attackEntityAsMob(entityIn);
		}

		public void livingTick() {
			super.livingTick();
			if (this.collidedHorizontally && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
				boolean flag = false;
				AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(0.2D);
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

		@Nullable
		public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason,
				@Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
			spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
			if ((double) worldIn.getRandom().nextFloat() < 0.2D) {
				StevindicatorEntity.CustomEntity entityToSpawn = new StevindicatorEntity.CustomEntity(StevindicatorEntity.entity, this.world);
				entityToSpawn.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
				entityToSpawn.onInitialSpawn(worldIn, difficultyIn, reason, (ILivingEntityData) null, (CompoundNBT) null);
				entityToSpawn.startRiding(this);
			}
			return spawnDataIn;
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public void playStepSound(BlockPos pos, BlockState blockIn) {
			this.playSound((net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.ravager.step")), 0.15f,
					1);
		}

		protected float getSoundPitch() {
			return super.getSoundPitch() * 0.7f;
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
