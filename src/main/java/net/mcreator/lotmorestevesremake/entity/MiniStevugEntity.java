
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
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
import net.minecraft.block.BlockState;

import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.MiniStevugRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import javax.annotation.Nullable;

@LotmorestevesremakeModElements.ModElement.Tag
public class MiniStevugEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.AMBIENT)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.7f, 0.7f)).build("mini_stevug").setRegistryName("mini_stevug");

	public MiniStevugEntity(LotmorestevesremakeModElements instance) {
		super(instance, 61);
		FMLJavaModLoadingContext.get().getModEventBus().register(new MiniStevugRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16751002, -10027162, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("mini_stevug_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.AMBIENT).add(new MobSpawnInfo.Spawners(entity, 1, 1, 4));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS,
				Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AggressiveSteveEntity::customSpawningConditionInLight);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 10);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public static boolean rarityForSpawn() {
			return Math.random() < 0.2;
		}

		protected float getSoundVolume() {
			return super.getSoundVolume() * 0.5f;
		}

		protected float getSoundPitch() {
			return super.getSoundPitch() * 1.5f;
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 0;
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
						enemy.hurtResistantTime = 0;
						this.attacker.swingArm(Hand.MAIN_HAND);
						this.attacker.attackEntityAsMob(enemy);
					}
				}
			});
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(4, new SwimGoal(this));
		}

		public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason,
				@Nullable ILivingEntityData livingdata, @Nullable CompoundNBT tag) {
			ILivingEntityData retval = super.onInitialSpawn(world, difficulty, reason, livingdata, tag);
			this.setEquipmentBasedOnDifficulty(difficulty);
			this.setEnchantmentBasedOnDifficulty(difficulty);
			return retval;
		}

		protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
			if (this.rand.nextFloat() < 0.2f) {
				int i = this.rand.nextInt(10);
				if (i < 3) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.WOODEN_PICKAXE));
					this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.WOODEN_PICKAXE));
				} else if (i < 5) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
					this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.WOODEN_SWORD));
				} else if (i < 7) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
					this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.STONE_PICKAXE));
				} else if (i == 9) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
					this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.STONE_SWORD));
				}
			} else if (this.rand.nextFloat() < 0.03f) {
				int i = this.rand.nextInt(4);
				float maxHP = (float) this.getAttributeValue(Attributes.MAX_HEALTH);
				float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
				if (i == 0) {
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.FURNACE));
					this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHP * 4);
					this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed * 0.5f);
					this.experienceValue *= 2;
				} else if (i == 1) {
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.SMOKER));
					this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHP * 6);
					this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed * 0.5f);
					this.experienceValue *= 3;
				} else if (i == 2) {
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.BLAST_FURNACE));
					this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHP * 8);
					this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed * 0.5f);
					this.experienceValue *= 4;
				} else {
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.SLIME_BLOCK));
					this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHP * 3);
					this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed * 1.5f);
					this.experienceValue *= 3;
				}
			}
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			return false;
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.ARTHROPOD;
		}

		@Override
		public void playStepSound(BlockPos pos, BlockState blockIn) {
			this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15f, 2);
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
