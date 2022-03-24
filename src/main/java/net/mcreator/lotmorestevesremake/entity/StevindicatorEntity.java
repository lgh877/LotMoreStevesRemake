
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

import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.GroundPathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.BreakDoorGoal;
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
import net.minecraft.block.Blocks;

import net.mcreator.lotmorestevesremake.procedures.StevillagersSpawnConditionProcedure;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.StevindicatorRenderer;
import net.mcreator.lotmorestevesremake.StevindicatorDetectBlockGoal;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.CustomMathHelper;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import javax.annotation.Nullable;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;
import java.util.AbstractMap;

@LotmorestevesremakeModElements.ModElement.Tag
public class StevindicatorEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("stevindicator").setRegistryName("stevindicator");

	public StevindicatorEntity(LotmorestevesremakeModElements instance) {
		super(instance, 18);
		FMLJavaModLoadingContext.get().getModEventBus().register(new StevindicatorRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16750900, -10066330, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("stevindicator_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 10, 1, 2));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> {
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();
					return StevillagersSpawnConditionProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("world", world))
							.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
				});
		DungeonHooks.addDungeonMob(entity, 180);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 26);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		private static final DataParameter<Integer> RED = EntityDataManager.createKey(CustomEntity.class, DataSerializers.VARINT);
		private static final DataParameter<Integer> GREEN = EntityDataManager.createKey(CustomEntity.class, DataSerializers.VARINT);
		private static final DataParameter<Integer> BLUE = EntityDataManager.createKey(CustomEntity.class, DataSerializers.VARINT);
		public boolean attack;
		public int DancingTick;

		/*public int red;
		public int green;
		public int blue;
		*/
		protected void registerData() {
			super.registerData();
			this.dataManager.register(RED, 0);
			this.dataManager.register(GREEN, 0);
			this.dataManager.register(BLUE, 0);
		}

		public void readAdditional(CompoundNBT compound) {
			super.readAdditional(compound);
			this.DancingTick = compound.getInt("DancingTick");
			this.dataManager.set(RED, compound.getInt("red"));
			this.dataManager.set(GREEN, compound.getInt("green"));
			this.dataManager.set(BLUE, compound.getInt("blue"));
			/*this.red = compound.getInt("red");
			this.green = compound.getInt("green");
			this.blue = compound.getInt("blue");
			*/
		}

		public void writeAdditional(CompoundNBT compound) {
			super.writeAdditional(compound);
			compound.putInt("DancingTick", this.DancingTick);
			compound.putInt("red", (int) this.getRed());
			compound.putInt("green", (int) this.getGreen());
			compound.putInt("blue", (int) this.getBlue());
			/*compound.putInt("red", this.red);
			compound.putInt("green", this.green);
			compound.putInt("blue", this.blue);
			*/
		}

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public void awardKillScore(Entity killed, int scoreValue, DamageSource damageSource) {
			super.awardKillScore(killed, scoreValue, damageSource);
			this.DancingTick = 30;
		}

		public CustomEntity(EntityType<? extends AggressiveSteveEntity> type, World world) {
			super(type, world);
			experienceValue = 15;
			stepHeight = 1.5f;
			setNoAI(false);
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new BreakDoorGoal(this, e -> true));
			this.goalSelector.addGoal(1, new CustomEntity.LockAngle());
			this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, (float) 6));
			this.goalSelector.addGoal(4, new LookAtGoal(this, ServerPlayerEntity.class, (float) 6));
			this.goalSelector.addGoal(4, new LookAtGoal(this, MobEntity.class, (float) 6));
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(this, 1, (int) 5));
			this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					double d0 = this.getAttackReachSqr(enemy);
					if (distToEnemySqr * 0.1 <= d0 && !this.attacker.isSwingInProgress) {
						float multiplier = 1;
						if (attacker.getHealth() / attacker.getMaxHealth() < 0.5)
							multiplier = 1.8f;
						this.attacker.swingArm(Hand.MAIN_HAND);
						this.attacker.applyKnockback((float) Math.pow(distToEnemySqr, 0.5) * 0.17f * multiplier,
								-enemy.getPosX() + this.attacker.getPosX(), -enemy.getPosZ() + this.attacker.getPosZ());
						CustomEntity.this.attack = false;
					}
				}
			});
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
		}

		protected double getAttackReach(LivingEntity attackTarget) {
			return (double) (this.getWidth() * 2.0F * this.getWidth() * 2.0F + attackTarget.getWidth());
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			if (!this.isSwingInProgress) {
				this.swingArm(Hand.MAIN_HAND);
				this.attack = false;
			}
			return super.attackEntityFrom(source, amount);
		}

		public void swingArm(Hand hand) {
			super.swingArm(hand);
		}

		public void livingTick() {
			super.livingTick();
			if (rand.nextInt(240) == 0)
				this.DancingTick = 40;
			if (this.DancingTick > 0)
				this.DancingTick--;
			if (GroundPathHelper.isGroundNavigator(this))
				((GroundPathNavigator) this.getNavigator()).setBreakDoors(true);
			if (this.isAlive()) {
				this.attackProcedure();
				if (this.DancingTick > 0 && this.getNavigator().hasPath())
					this.setSprinting(true);
				else
					this.setSprinting(false);
			}
		}

		public void attackProcedure() {
			if (this.isSwingInProgress && !this.attack && this.swingProgress > 0.3f) {
				this.attack = true;
				this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
				if (this.getAttackTarget() != null) {
					this.getAttackTarget().hurtResistantTime = 0;
					if (this.getEntitySenses().canSee(this.getAttackTarget()) && CustomMathHelper.isEntityInBox(this.getAttackTarget(), this, 1.5)) {
						this.attackEntityAsMob(this.getAttackTarget());
					}
				}
			}
		}

		@Override
		protected void updateArmSwingProgress() {
			Item getArmor = this.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem();
			int i = 30;
			if (this.getHealth() / this.getMaxHealth() < 0.3f)
				i = 18;
			else if (getArmor == Blocks.CARVED_PUMPKIN.asItem())
				i = 10;
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

		public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason,
				@Nullable ILivingEntityData livingdata, @Nullable CompoundNBT tag) {
			ILivingEntityData retval = super.onInitialSpawn(world, difficulty, reason, livingdata, tag);
			float maxHP = (float) this.getAttributeValue(Attributes.MAX_HEALTH);
			float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
			float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
			this.setRed(rand.nextInt(11));
			this.setGreen(rand.nextInt(11));
			this.setBlue(rand.nextInt(11));
			/*this.red = rand.nextInt(11);
			this.green = rand.nextInt(11);
			this.blue = rand.nextInt(11);
			*/
			if (rand.nextInt(20) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.LEATHER_BOOTS));
			} else if (rand.nextInt(20) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
			} else if (rand.nextInt(20) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.DIAMOND_BOOTS));
			} else if (rand.nextInt(20) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.NETHERITE_HELMET));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.NETHERITE_BOOTS));
			} else if (rand.nextInt(20) == 0) {
				ItemStack _setstack = new ItemStack(Items.TOTEM_OF_UNDYING);
				_setstack.setCount(rand.nextInt(10));
				this.setHeldItem(Hand.OFF_HAND, _setstack);
			} else if (rand.nextInt(50) == 0) {
				int a = rand.nextInt(3);
				if (a == 0)
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Blocks.BLAST_FURNACE));
				else if (a == 1)
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Blocks.OBSERVER));
				else
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Blocks.DROPPER));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
				this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHP * 4);
				this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed * 0.75f);
				this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
				this.getAttribute(Attributes.ARMOR).setBaseValue(15);
				this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damage + 2);
			} else if (rand.nextInt(50) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Blocks.CARVED_PUMPKIN));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.LEATHER_BOOTS));
				this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHP * 2.5f);
				this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed * 1.5f);
				this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damage + 2);
			}
			this.setHealth(this.getMaxHealth());
			this.setEnchantmentBasedOnDifficulty(difficulty);
			return retval;
		}

		public void setRed(int value) {
			this.dataManager.set(RED, value);
		}

		public void setGreen(int value) {
			this.dataManager.set(GREEN, value);
		}

		public void setBlue(int value) {
			this.dataManager.set(BLUE, value);
		}

		public float getRed() {
			return this.dataManager.get(RED);
		}

		public float getGreen() {
			return this.dataManager.get(GREEN);
		}

		public float getBlue() {
			return this.dataManager.get(BLUE);
		}

		/*@OnlyIn(Dist.CLIENT)
		public float getRed() {
			return (float) this.red;
		}
		@OnlyIn(Dist.CLIENT)
		public float getGreen() {
			return (float) this.green;
		}
		@OnlyIn(Dist.CLIENT)
		public float getBlue() {
			return (float) this.blue;
		}
		*/
		public class LockAngle extends Goal {
			public LockAngle() {
				this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
			}

			public boolean shouldExecute() {
				LivingEntity livingentity = CustomEntity.this.getAttackTarget();
				if (livingentity instanceof PlayerEntity && ((PlayerEntity) livingentity).abilities.disableDamage)
					return false;
				return CustomEntity.this.isSwingInProgress && CustomEntity.this.getAttackTarget() != null;
			}

			public void tick() {
				CustomEntity.this.getNavigator().tryMoveToEntityLiving(CustomEntity.this.getAttackTarget(), 1.5);
			}
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:hostile_steve_hurt"));
		}

		@OnlyIn(Dist.CLIENT)
		public int getDancingTick() {
			return this.DancingTick;
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:hostile_steve_hurt"));
		}
	}
}
