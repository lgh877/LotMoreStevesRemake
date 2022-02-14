
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.GroundPathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
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
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.block.Blocks;

import net.mcreator.lotmorestevesremake.procedures.SpawnInOverworldOnlyProcedure;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.StecubeRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import javax.annotation.Nullable;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

@LotmorestevesremakeModElements.ModElement.Tag
public class StecubeEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.AMBIENT)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.5f, 0.75f)).build("stecube").setRegistryName("stecube");

	public StecubeEntity(LotmorestevesremakeModElements instance) {
		super(instance, 30);
		FMLJavaModLoadingContext.get().getModEventBus().register(new StecubeRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16763956, -16777012, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("stecube_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.AMBIENT).add(new MobSpawnInfo.Spawners(entity, 1, 1, 3));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS,
				Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (entityType, world, reason, pos, random) -> {
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();
					return SpawnInOverworldOnlyProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("world", world))
							.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll)) && Math.random() < 0.05;
				});
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 15);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.2);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 10;
			setNoAI(false);
		}

		protected float getSoundVolume() {
			return 0.5f;
		}

		protected float getSoundPitch() {
			return 1.5f;
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(6, new BreakDoorGoal(this, e -> true));
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					double d0 = this.getAttackReachSqr(enemy);
					if (distToEnemySqr <= d0 && !this.attacker.isSwingInProgress) {
						enemy.hurtResistantTime = 0;
						this.attacker.swingArm(Hand.MAIN_HAND);
						this.attacker.attackEntityAsMob(enemy);
					}
				}
			});
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			return super.onLivingFall(distance * 0.75f, damageMultiplier);
		}

		public void livingTick() {
			super.livingTick();
			if (GroundPathHelper.isGroundNavigator(this))
				((GroundPathNavigator) this.getNavigator()).setBreakDoors(true);
			this.setLeftHanded(false);
			if ((this.onGround || this.collidedHorizontally) && this.rand.nextInt(40) == 0 && this.getAttackTarget() != null) {
				this.applyKnockback(2, -this.getAttackTarget().getPosX() + this.getPosX(), -this.getAttackTarget().getPosZ() + this.getPosZ());
			}
		}

		public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason,
				@Nullable ILivingEntityData livingdata, @Nullable CompoundNBT tag) {
			ILivingEntityData retval = super.onInitialSpawn(world, difficulty, reason, livingdata, tag);
			float maxHP = (float) this.getAttributeValue(Attributes.MAX_HEALTH);
			float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
			float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
			if (this.rand.nextFloat() < 0.2f) {
				int i = this.rand.nextInt(5);
				if (i == 0) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
				} else if (i == 1) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
				} else if (i == 2) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SHOVEL));
				} else if (i == 3)
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.WOODEN_PICKAXE));
				else
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
			}
			if (rand.nextInt(50) == 0) {
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
				this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed * 0.65f);
				this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
				this.getAttribute(Attributes.ARMOR).setBaseValue(15);
				this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damage + 2);
			} else if (rand.nextInt(50) == 0) {
				int a = rand.nextInt(3);
				if (a == 0)
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Blocks.CARVED_PUMPKIN));
				else if (a == 1)
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.BONE));
				else
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEAD));
				this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.LEATHER_BOOTS));
				this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHP * 2.5f);
				this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed * 1.25f);
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
