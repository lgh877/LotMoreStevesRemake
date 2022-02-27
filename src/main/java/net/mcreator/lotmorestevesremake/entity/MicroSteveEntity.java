
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.GroundPathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.pathfinding.GroundPathNavigator;
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
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.FollowMobGoal;
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

import net.mcreator.lotmorestevesremake.procedures.SpawnInOverworldOnlyProcedure;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.item.TntArmorSetItem;
import net.mcreator.lotmorestevesremake.entity.renderer.MicroSteveRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import javax.annotation.Nullable;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

@LotmorestevesremakeModElements.ModElement.Tag
public class MicroSteveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.3f, 0.9f)).build("micro_steve").setRegistryName("micro_steve");

	public MicroSteveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 27);
		FMLJavaModLoadingContext.get().getModEventBus().register(new MicroSteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16737793, -16711681, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("micro_steve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(entity, 10, 4, 4));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> {
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();
					return SpawnInOverworldOnlyProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("world", world))
							.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
				});
		DungeonHooks.addDungeonMob(entity, 180);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 10);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 32);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<? extends AggressiveSteveEntity> type, World world) {
			super(type, world);
			this.setCanPickUpLoot(true);
			experienceValue = 6;
			setNoAI(false);
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TntArmorSetItem.boots));
		}

		protected boolean shouldDrown() {
			return rand.nextInt(10) == 0;
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 1, 1));
			this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(6, new BreakDoorGoal(this, e -> true));
			this.goalSelector.addGoal(3, new FollowMobGoal(this, (float) 1, 10, 5));
			this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					double d0 = this.getAttackReachSqr(enemy);
					if (distToEnemySqr <= d0 && !this.attacker.isSwingInProgress) {
						enemy.hurtResistantTime = 0;
						this.attacker.swingArm(Hand.MAIN_HAND);
						this.attacker.attackEntityAsMob(enemy);
					}
				}
			});
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			return super.onLivingFall(distance * 0.75f, damageMultiplier);
		}

		public void livingTick() {
			super.livingTick();
			if (GroundPathHelper.isGroundNavigator(this))
				((GroundPathNavigator) this.getNavigator()).setBreakDoors(true);
			if (rand.nextInt(80) == 0) {
				this.setSprinting(!this.isSprinting());
				this.resetActiveHand();
			}
			if (rand.nextInt(40) == 0)
				this.setActiveHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof ShieldItem));
		}

		protected float getSoundVolume() {
			return 0.5f;
		}

		protected float getSoundPitch() {
			return 1.5f;
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

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return this.canBlockDamageSource(ds) && this.isHandActive()
					? (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.shield.block"))
					: (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
							.getValue(new ResourceLocation("lotmorestevesremake:hostile_steve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS
					.getValue(new ResourceLocation("lotmorestevesremake:hostile_steve_hurt"));
		}
	}
}
