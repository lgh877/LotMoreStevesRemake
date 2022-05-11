
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.loot.LootTables;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
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
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;

import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.SpigRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import javax.annotation.Nullable;

@LotmorestevesremakeModElements.ModElement.Tag
public class SpigEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.9f, 0.9f)).build("spig").setRegistryName("spig");

	public SpigEntity(LotmorestevesremakeModElements instance) {
		super(instance, 62);
		FMLJavaModLoadingContext.get().getModEventBus().register(new SpigRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16751002, -26215, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("spig_spawn_egg"));
	}

	//create("ghost", "ghost", 64, true, false, 128)
	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		/*Random random = new Random();
		if (random.nextFloat() < 0.5f)
			return;
		*/
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 5, 1, 4));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos,
						random) -> (world.getBlockState(pos.down()).getMaterial() == Material.ORGANIC && world.getLightSubtracted(pos, 0) > 8));
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 20);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			this.swingArm(Hand.MAIN_HAND);
			setNoAI(false);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
			return 0.0F;
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
				@Override
				protected double getAttackReachSqr(LivingEntity entity) {
					return (double) (4.0 + entity.getWidth() * entity.getWidth());
				}
			});
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
			this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(5, new SwimGoal(this));
		}

		public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason,
				@Nullable ILivingEntityData livingdata, @Nullable CompoundNBT tag) {
			ILivingEntityData retval = super.onInitialSpawn(world, difficulty, reason, livingdata, tag);
			this.setEquipmentBasedOnDifficulty(difficulty);
			this.setEnchantmentBasedOnDifficulty(difficulty);
			return retval;
		}

		protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
			if (this.rand.nextFloat() < 0.45F) {
				int i = this.rand.nextInt(2);
				if (this.rand.nextFloat() < 0.095F) {
					++i;
				}
				if (this.rand.nextFloat() < 0.095F) {
					++i;
				}
				if (this.rand.nextFloat() < 0.095F) {
					++i;
				}
				boolean flag = true;
				for (EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
					if (equipmentslottype.getSlotType() == EquipmentSlotType.Group.ARMOR) {
						ItemStack itemstack = this.getItemStackFromSlot(equipmentslottype);
						if (this.rand.nextFloat() < 0.07f) {
							break;
						}
						flag = false;
						if (itemstack.isEmpty()) {
							Item item = getArmorByChance(equipmentslottype, i);
							if (item != null) {
								this.setItemStackToSlot(equipmentslottype, new ItemStack(item));
							}
						}
					}
				}
			}
			if (this.rand.nextFloat() < 0.1f) {
				int i = this.rand.nextInt(6);
				if (i == 0) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
				} else if (i == 1) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
				} else if (i == 2) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.WOODEN_PICKAXE));
				} else if (i == 3) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STICK));
				} else if (i == 4) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.WOODEN_AXE));
				} else if (i == 5) {
					this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_HOE));
				}
				if (rand.nextInt(50) == 0 && rand.nextInt(50) == 0) {
					this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.NETHERITE_HELMET));
					this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
					this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
					this.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.NETHERITE_BOOTS));
				}
			}
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public void playStepSound(BlockPos pos, BlockState blockIn) {
			this.playSound((net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.pig.step")), 0.15f, 1);
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
