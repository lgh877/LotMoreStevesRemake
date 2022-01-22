
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import net.mcreator.lotmorestevesremake.procedures.StevillagersSpawnConditionProcedure;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.StevindicatorBigRenderer;
import net.mcreator.lotmorestevesremake.StevindicatorDetectBlockGoal;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

@LotmorestevesremakeModElements.ModElement.Tag
public class StevindicatorBigEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(1.2f, 3.6f)).build("stevindicator_big").setRegistryName("stevindicator_big");

	public StevindicatorBigEntity(LotmorestevesremakeModElements instance) {
		super(instance, 22);
		FMLJavaModLoadingContext.get().getModEventBus().register(new StevindicatorBigRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16751002, -16764109, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("stevindicator_big_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 5, 1, 1));
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
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 96);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.4);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends StevindicatorEntity.CustomEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		protected float getSoundPitch() {
			return 0.8f;
		}

		protected float getSoundVolume() {
			return 1.2f;
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			stepHeight = 3f;
			this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			return super.onLivingFall(distance * 0.5f, damageMultiplier);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.applyEntityAI();
		}

		protected void applyEntityAI() {
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(Blocks.CRAFTING_TABLE, this, 1, (int) 6));
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(Blocks.FURNACE, this, 1, (int) 6));
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(Blocks.BLAST_FURNACE, this, 1, (int) 6));
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(Blocks.BOOKSHELF, this, 1, (int) 6));
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		protected void updateArmSwingProgress() {
			Item getArmor = this.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem();
			int i = 45;
			if (this.getHealth() / this.getMaxHealth() < 0.3f)
				i = 27;
			else if (getArmor == Blocks.CARVED_PUMPKIN.asItem())
				i = 15;
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

		public void livingTick() {
			super.livingTick();
			if (this.collidedHorizontally && !this.isSwingInProgress) {
				this.swingArm(Hand.MAIN_HAND);
				this.attack = false;
			}
		}

		@Override
		public void attackProcedure() {
			if (this.isSwingInProgress && !this.attack && this.swingProgress > 0.3f) {
				this.attack = true;
				this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1, 0.5f);
				if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
					boolean flag = false;
					AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(1.2, 0, 1.2);
					for (BlockPos blockpos : BlockPos.getAllInBoxMutable(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY),
							MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY),
							MathHelper.floor(axisalignedbb.maxZ))) {
						BlockState blockstate = this.world.getBlockState(blockpos);
						Block block = blockstate.getBlock();
						if (rand.nextInt(3) == 0 && block.getExplosionResistance() < 1200 && !world.isRemote()) {
							flag = this.world.destroyBlock(blockpos, true, this) || flag;
						}
					}
				}
				if (this.getAttackTarget() != null) {
					this.getAttackTarget().hurtResistantTime = 0;
					double d0 = this.getDistanceSq(this.getAttackTarget());
					if (d0 <= this.getAttackReach(this.getAttackTarget())) {
						this.attackEntityAsMob(this.getAttackTarget());
					}
				}
			}
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
