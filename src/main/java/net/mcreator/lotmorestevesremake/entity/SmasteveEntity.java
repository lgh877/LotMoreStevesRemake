
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
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.Direction;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.EffectInstance;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import net.mcreator.lotmorestevesremake.procedures.LimpSteveSpawnConditionProcedure;
import net.mcreator.lotmorestevesremake.potion.CursedDiversionPotionEffect;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.SmasteveRenderer;
import net.mcreator.lotmorestevesremake.StevindicatorDetectBlockGoal;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.CustomMathHelper;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

@LotmorestevesremakeModElements.ModElement.Tag
public class SmasteveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(2.5f, 5.25f)).build("smasteve").setRegistryName("smasteve");

	public SmasteveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 36);
		FMLJavaModLoadingContext.get().getModEventBus().register(new SmasteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16764007, -10092289, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("smasteve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 2, 1, 1));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> {
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();
					return LimpSteveSpawnConditionProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("world", world))
							.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
				});
		DungeonHooks.addDungeonMob(entity, 180);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 384);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 25);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.8);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		private static final DataParameter<Integer> ATTACK_STATE = EntityDataManager.createKey(CustomEntity.class, DataSerializers.VARINT);
		public int attackProgress;

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
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

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			stepHeight = 1;
			experienceValue = 45;
			setNoAI(false);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					double d0 = this.getAttackReachSqr(enemy);
					if (distToEnemySqr <= d0 && !this.attacker.isSwingInProgress) {
						this.attacker.swingArm(Hand.MAIN_HAND);
						CustomEntity.this.setAttackState(1);
						CustomEntity.this.attackProgress = 0;
					}
				}
			});
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, (float) 6));
			this.goalSelector.addGoal(4, new LookAtGoal(this, ServerPlayerEntity.class, (float) 6));
			this.goalSelector.addGoal(4, new LookAtGoal(this, MobEntity.class, (float) 6));
			this.applyEntityAI();
		}

		protected void applyEntityAI() {
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(this, 1, (int) 5));
		}

		public float getAttackDamage() {
			return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
		}

		public float getKnockBackPower() {
			return (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
		}

		@Override
		protected void updateArmSwingProgress() {
			int i = 60;
			if (this.isSwingInProgress) {
				++this.swingProgressInt;
				if (this.swingProgressInt >= i) {
					this.swingProgressInt = 0;
					this.isSwingInProgress = false;
					this.setAttackState(0);
				}
			} else {
				this.swingProgressInt = 0;
				this.setAttackState(0);
			}
			this.swingProgress = (float) this.swingProgressInt / (float) i;
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			return super.onLivingFall(MathHelper.clamp(distance - 4, 0, Float.POSITIVE_INFINITY), damageMultiplier);
		}

		public void livingTick() {
			super.livingTick();
			this.attackProcedure();
			if ((this.collidedHorizontally || this.isMovementBlocked()) && !this.isSwingInProgress) {
				this.swingArm(Hand.MAIN_HAND);
				this.setAttackState(1);
				this.attackProgress = 0;
			}
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
						int i = (int) (this.getWidth() * 7);
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
								if (rand.nextInt(3) == 0)
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
								CustomMathHelper.makeHorizontalAttackRange(this, 0, 0, (double) this.getWidth() * 6, (double) this.getHeight() * 0.8f,
										(double) this.getWidth() * 6))) {
							if (!this.isOnSameTeam(livingentity) && this.getDistance(livingentity) < this.getWidth() * 4f) {
								livingentity.hurtResistantTime = 0;
								livingentity.addPotionEffect(new EffectInstance(CursedDiversionPotionEffect.potion, (int) 200, (int) 0));
								if (livingentity.attackEntityFrom(DamageSource.causeMobDamage(this),
										this.getAttackDamage() / (float) MathHelper.clamp(Math.pow(this.getDistanceSq(livingentity), 0.5), 1, 5))) {
									this.applyEnchantments(this, livingentity);
									livingentity.applyKnockback(
											this.getKnockBackPower()
													/ (float) MathHelper.clamp(Math.pow(this.getDistanceSq(livingentity), 0.5), 1, 5),
											-livingentity.getPosX() + this.getPosX(), -livingentity.getPosZ() + this.getPosZ());
								}
							}
						}
						this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, 1, 0.5f);
						this.attackProgress++;
					}
				}
			}
		}

		protected float getSoundPitch() {
			return 0.75f;
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			if (source == DamageSource.IN_WALL && !this.isSwingInProgress) {
				this.swingArm(Hand.MAIN_HAND);
				this.setAttackState(1);
				this.attackProgress = 0;
			}
			return super.attackEntityFrom(source, amount);
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
