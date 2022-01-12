
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
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
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
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import javax.annotation.Nullable;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
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
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 10, 1, 4));
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
		private boolean attack;
		private boolean isBreakDoorsTaskSet = true;

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
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
			this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp());
			this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, MobEntity.class, false, false));
			this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, PlayerEntity.class, false, false));
			this.goalSelector.addGoal(6, new BreakDoorGoal(this, e -> true));
			this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, ServerPlayerEntity.class, false, false));
			this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, (float) 6));
			this.goalSelector.addGoal(4, new LookAtGoal(this, ServerPlayerEntity.class, (float) 6));
			this.goalSelector.addGoal(4, new LookAtGoal(this, MobEntity.class, (float) 6));
			this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					double d0 = this.getAttackReachSqr(enemy);
					if (distToEnemySqr <= d0 && !this.attacker.isSwingInProgress) {
						this.attacker.swingArm(Hand.MAIN_HAND);
						CustomEntity.this.attack = false;
					}
				}
			});
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(0, new SwimGoal(this));
		}

		protected double getAttackReach(LivingEntity attackTarget) {
			return (double) (this.getWidth() * 2.0F * this.getWidth() * 2.0F + attackTarget.getWidth());
		}

		public boolean attackEntityFrom(DamageSource source, float amount) {
			double d0 = this.getDistanceSq(source.getTrueSource());
			if (source.getTrueSource() instanceof LivingEntity)
				if (!this.isSwingInProgress && this.getAttackReach((LivingEntity) source.getTrueSource()) > d0 * 0.3f) {
					this.swingArm(Hand.MAIN_HAND);
					this.attack = false;
				}
			return super.attackEntityFrom(source, amount);
		}

		public void livingTick() {
			super.livingTick();
			((GroundPathNavigator) this.getNavigator()).setBreakDoors(true);
			if (this.isAlive()) {
				if (this.getAttackTarget() != null && this.isSwingInProgress) {
					double d0 = this.getDistanceSq(this.getAttackTarget());
					if (!this.attack && this.swingProgress > 0.3f) {
						this.getAttackTarget().hurtResistantTime = 0;
						this.attack = true;
						this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
						if (d0 <= this.getAttackReach(this.getAttackTarget()))
							this.attackEntityAsMob(this.getAttackTarget());
					}
				}
			}
		}

		protected void collideWithEntity(Entity entityIn) {
			if (entityIn instanceof MobEntity && !this.isOnSameTeam(entityIn) && this.getRNG().nextInt(5) == 0) {
				this.setAttackTarget((LivingEntity) entityIn);
			}
			super.collideWithEntity(entityIn);
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
			if (rand.nextInt(10) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.LEATHER_BOOTS));
			} else if (rand.nextInt(10) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
			} else if (rand.nextInt(10) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.DIAMOND_HELMET));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.DIAMOND_BOOTS));
			} else if (rand.nextInt(10) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.NETHERITE_HELMET));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.NETHERITE_BOOTS));
			} else if (rand.nextInt(10) == 0) {
				ItemStack _setstack = new ItemStack(Items.TOTEM_OF_UNDYING);
				_setstack.setCount(rand.nextInt(10));
				this.setHeldItem(Hand.OFF_HAND, _setstack);
			} else if (rand.nextInt(50) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Blocks.BLAST_FURNACE));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
				this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHP + 80);
				this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
				this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
				this.getAttribute(Attributes.ARMOR).setBaseValue(15);
				this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damage + 2);
			} else if (rand.nextInt(70) == 0) {
				this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Blocks.CARVED_PUMPKIN));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.LEATHER_BOOTS));
				this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHP + 40);
				this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed + 0.24);
				this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damage + 2);
			}
			this.setHealth(this.getMaxHealth());
			this.setEnchantmentBasedOnDifficulty(difficulty);
			return retval;
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

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:hostile_steve_hurt"));
		}
	}
}
