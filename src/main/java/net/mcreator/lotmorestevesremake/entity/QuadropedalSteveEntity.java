
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.monster.MonsterEntity;
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
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.QuadropedalSteveRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import java.util.EnumSet;

@LotmorestevesremakeModElements.ModElement.Tag
public class QuadropedalSteveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(1.5f, 2.5f)).build("quadropedal_steve").setRegistryName("quadropedal_steve");

	public QuadropedalSteveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 59);
		FMLJavaModLoadingContext.get().getModEventBus().register(new QuadropedalSteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16750900, -16724839, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("quadropedal_steve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 5, 1, 1));
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
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 80);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 7);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public static boolean whenToSpawn(IServerWorld worldIn) {
			return worldIn.getWorldInfo().getDayTime() > 24000 * 3;
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 25;
			setNoAI(false);
			stepHeight = 2;
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4, false) {
				private int meleeAttackCoolTime;

				public boolean shouldContinueExecuting() {
					if (this.meleeAttackCoolTime > 0) {
						this.meleeAttackCoolTime--;
						return false;
					}
					return super.shouldContinueExecuting();
				}

				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					double d0 = this.getAttackReachSqr(enemy);
					this.meleeAttackCoolTime = 60;
					if (distToEnemySqr <= d0 && !this.attacker.isSwingInProgress) {
						this.attacker.swingArm(Hand.MAIN_HAND);
						this.attacker.attackEntityAsMob(enemy);
					}
				}
			});
			this.goalSelector.addGoal(2, new CustomEntity.MoveAttackGoal(this, 1, 0, 48));
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1));
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
			private boolean attack = true;

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
				}
			}
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
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
	}
}
