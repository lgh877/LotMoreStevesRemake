
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.block.BlockState;

import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.HungryZteveRenderer;
import net.mcreator.lotmorestevesremake.StevindicatorDetectBlockGoal;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.CustomMathHelper;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

@LotmorestevesremakeModElements.ModElement.Tag
public class HungryZteveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(0.9f, 1.5f)).build("hungry_zteve").setRegistryName("hungry_zteve");

	public HungryZteveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 60);
		FMLJavaModLoadingContext.get().getModEventBus().register(new HungryZteveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16751002, -16751053, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("hungry_zteve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 20, 1, 1));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				MonsterEntity::canMonsterSpawn);
	}

	private static class EntityAttributesRegisterHandler {
		@SubscribeEvent
		public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
			AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 60);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 5);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.2);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AggressiveSteveEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 18;
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
					if (CustomMathHelper.isEntityInBox(enemy, this.attacker, 1) && !this.attacker.isSwingInProgress) {
						this.attacker.swingArm(Hand.MAIN_HAND);
						this.attacker.attackEntityAsMob(enemy);
					}
				}
			});
			this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1));
			this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
			this.goalSelector.addGoal(4, new SwimGoal(this));
			this.applyEntityAI();
		}

		@Override
		protected void updateArmSwingProgress() {
			int i = 12;
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

		protected void applyEntityAI() {
			this.goalSelector.addGoal(5, new StevindicatorDetectBlockGoal(this, 1, (int) 5));
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

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEAD;
		}

		protected boolean shouldBurnInDay() {
			return true;
		}

		@Override
		public void playStepSound(BlockPos pos, BlockState blockIn) {
			this.playSound((net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.zombie.step")), 0.15f,
					1);
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lotmorestevesremake:zteve_hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lotmorestevesremake:zteve_hurt"));
		}
	}
}
