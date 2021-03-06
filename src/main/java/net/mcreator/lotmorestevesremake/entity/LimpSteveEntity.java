
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
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.LimpSteveRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.CustomMathHelper;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

@LotmorestevesremakeModElements.ModElement.Tag
public class LimpSteveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new).size(1.5f, 8f))
					.build("limp_steve").setRegistryName("limp_steve");

	public LimpSteveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 55);
		FMLJavaModLoadingContext.get().getModEventBus().register(new LimpSteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16737895, -6526615, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("limp_steve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 2, 1, 1));
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
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 250);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 10);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.7);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 5);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		public float[] prevSwingProgress2 = new float[5];
		public int attack;

		public static boolean whenToSpawn(IServerWorld worldIn) {
			return worldIn.getWorldInfo().getDayTime() > 24000 * 15;
		}

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		protected float getSoundPitch() {
			return super.getSoundPitch() * 0.7f;
		}

		protected float getSoundVolume() {
			return 2f;
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 40;
			setNoAI(false);
			this.stepHeight = 1.5f;
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					if (CustomMathHelper.isEntityInBox(enemy, this.attacker, CustomEntity.this.getWidth() * 3) && !this.attacker.isSwingInProgress) {
						this.attacker.swingArm(Hand.MAIN_HAND);
						CustomEntity.this.attack = 0;
					}
				}
			});
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(4, new SwimGoal(this));
		}

		@Override
		protected void updateArmSwingProgress() {
			int i = (int) (this.getWidth() * 10);
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

		public void attackProcedure() {
			if (this.isSwingInProgress && this.attack == 0 && this.swingProgress > 0.7f) {
				this.attack++;
				this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1, 0.5f);
				if (this.getAttackTarget() != null) {
					this.getAttackTarget().hurtResistantTime = 0;
					if (CustomMathHelper.isEntityInBox(this.getAttackTarget(), this, this.getWidth() * 3)) {
						this.attackEntityAsMob(this.getAttackTarget());
					}
				}
			}
		}

		public void livingTick() {
			super.livingTick();
			if (this.isAlive())
				attackProcedure();
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

		public void baseTick() {
			for (int i = 0; i < 4; i++) {
				prevSwingProgress2[4 - i] = prevSwingProgress2[3 - i];
			}
			prevSwingProgress2[0] = prevSwingProgress;
			super.baseTick();
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

		public float getSwingProgress(float partialTime, int value) {
			if (value == 0)
				return super.getSwingProgress(partialTime);
			else if (value == 1) {
				float f = this.prevSwingProgress - this.prevSwingProgress2[0];
				if (f < 0.0F) {
					++f;
				}
				return this.prevSwingProgress2[0] + f * partialTime;
			} else {
				value = MathHelper.clamp(value, 0, 4);
				float f = this.prevSwingProgress2[value - 2] - this.prevSwingProgress2[value - 1];
				if (f < 0.0F) {
					++f;
				}
				return this.prevSwingProgress2[value - 2] + f * partialTime;
			}
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:weird_steve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:weird_steve_hurt"));
		}
	}
}
