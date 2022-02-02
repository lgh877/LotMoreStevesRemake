
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
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.block.Blocks;

import net.mcreator.lotmorestevesremake.procedures.ZteveSpawnConditionProcedure;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.ZteveRenderer;
import net.mcreator.lotmorestevesremake.StevindicatorDetectBlockGoal;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

@LotmorestevesremakeModElements.ModElement.Tag
public class ZteveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.6f, 1.8f)).build("zteve").setRegistryName("zteve");

	public ZteveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 29);
		FMLJavaModLoadingContext.get().getModEventBus().register(new ZteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16751002, -12996586, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("zteve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 100, 1, 2));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> {
					int x = pos.getX();
					int y = pos.getY();
					int z = pos.getZ();
					return ZteveSpawnConditionProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("world", world))
							.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
				});
		DungeonHooks.addDungeonMob(entity, 180);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 22);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends MicroSteveEntity.CustomEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		protected float getSoundVolume() {
			return 1f;
		}

		protected float getSoundPitch() {
			return this.isChild()
					? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F
					: (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
		}

		public boolean onLivingFall(float distance, float damageMultiplier) {
			return super.onLivingFall(distance, damageMultiplier);
		}

		public CustomEntity(EntityType<? extends AggressiveSteveEntity> type, World world) {
			super(type, world);
			experienceValue = 8;
			this.stepHeight = 1.5F;
			setNoAI(false);
		}

		public void livingTick() {
			super.livingTick();
			if (this.isAlive()) {
				boolean flag = this.shouldBurnInDay() && this.isInDaylight() && rand.nextInt(60) == 0;
				if (flag) {
					ItemStack itemstack = this.getItemStackFromSlot(EquipmentSlotType.HEAD);
					if (!itemstack.isEmpty()) {
						if (itemstack.isDamageable()) {
							itemstack.setDamage(itemstack.getDamage() + this.rand.nextInt(2));
							if (itemstack.getDamage() >= itemstack.getMaxDamage()) {
								this.sendBreakAnimation(EquipmentSlotType.HEAD);
								this.setItemStackToSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
							}
						}
						flag = false;
					}
					if (flag) {
						this.setFire(4);
					}
				}
			}
		}

		protected boolean shouldBurnInDay() {
			return true;
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.applyEntityAI();
		}

		protected void applyEntityAI() {
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(Blocks.CRAFTING_TABLE, this, 1, (int) 2));
			this.goalSelector.addGoal(6, new StevindicatorDetectBlockGoal(Blocks.FURNACE, this, 1, (int) 2));
			this.goalSelector.addGoal(8, new StevindicatorDetectBlockGoal(Blocks.BLAST_FURNACE, this, 1, (int) 2));
			this.goalSelector.addGoal(7, new StevindicatorDetectBlockGoal(Blocks.BOOKSHELF, this, 1, (int) 2));
		}

		public boolean attackEntityAsMob(Entity entityIn) {
			this.applyKnockback(1, -entityIn.getPosX() + this.getPosX(), -entityIn.getPosZ() + this.getPosZ());
			return super.attackEntityAsMob(entityIn);
		}

		@Override
		protected void updateArmSwingProgress() {
			int i = 10;
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

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEAD;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return this.canBlockDamageSource(ds) && this.isHandActive()
					? SoundEvents.ITEM_SHIELD_BLOCK
					: (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lotmorestevesremake:zteve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lotmorestevesremake:zteve_hurt"));
		}
	}
}
