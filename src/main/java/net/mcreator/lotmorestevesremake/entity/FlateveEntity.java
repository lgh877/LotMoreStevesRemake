
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
import net.minecraft.util.DamageSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
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

import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.FlateveRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;
import net.mcreator.lotmorestevesremake.AggressiveSteveEntity;

import javax.annotation.Nullable;

@LotmorestevesremakeModElements.ModElement.Tag
public class FlateveEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.AMBIENT)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(1.5f, 0.75f)).build("flateve").setRegistryName("flateve");

	public FlateveEntity(LotmorestevesremakeModElements instance) {
		super(instance, 57);
		FMLJavaModLoadingContext.get().getModEventBus().register(new FlateveRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16764007, -16737793, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("flateve_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.AMBIENT).add(new MobSpawnInfo.Spawners(entity, 1, 1, 3));
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
			ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2);
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 50);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 2);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.2);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends StecubeEntity.CustomEntity {
		public boolean noRiders;

		public static boolean rarityForSpawn() {
			return Math.random() < 0.05;
		}

		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 15;
			setNoAI(false);
			this.setCanPickUpLoot(false);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
		}

		protected void applyEntityAI() {
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
				protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
					double d0 = this.getAttackReachSqr(enemy);
					if (distToEnemySqr <= d0 && !this.attacker.isSwingInProgress) {
						enemy.hurtResistantTime = 0;
						this.attacker.swingArm(Hand.MAIN_HAND);
						this.attacker.attackEntityAsMob(enemy);
					}
				}

				protected double getAttackReachSqr(LivingEntity attackTarget) {
					float f = CustomEntity.this.getWidth() - 0.1F;
					return (double) (f * 2.0F + attackTarget.getWidth() * 1.5f);
				}
			});
		}

		protected void updateMovementGoalFlags() {
			boolean flag = !(this.getControllingPassenger() instanceof MobEntity) || EntityTypeTags.getCollection()
					.getTagByID(new ResourceLocation("lotmorestevesremake:we_are_steves")).contains(this.getControllingPassenger().getType());
			boolean flag1 = !(this.getRidingEntity() instanceof BoatEntity);
			this.goalSelector.setFlag(Goal.Flag.MOVE, flag);
			this.goalSelector.setFlag(Goal.Flag.JUMP, flag && flag1);
			this.goalSelector.setFlag(Goal.Flag.LOOK, flag);
			this.goalSelector.setFlag(Goal.Flag.TARGET, flag);
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		protected float getSoundPitch() {
			return super.getSoundPitch() * 0.5f;
		}

		protected int getJumpChance() {
			return 80;
		}

		public boolean attackEntityAsMob(Entity entityIn) {
			if (entityIn instanceof MobEntity)
				((MobEntity) entityIn).setAttackTarget(this);
			return super.attackEntityAsMob(entityIn);
		}

		protected float getSoundVolume() {
			return 1f;
		}

		public double getMountedYOffset() {
			return super.getMountedYOffset() - 0.5;
		}

		@Nullable
		public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason,
				@Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
			if ((double) worldIn.getRandom().nextFloat() < 0.1 && !this.noRiders) {
				StevindicatorEntity.CustomEntity entityToSpawn = new StevindicatorEntity.CustomEntity(StevindicatorEntity.entity, this.world);
				entityToSpawn.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
				entityToSpawn.onInitialSpawn(worldIn, difficultyIn, reason, (ILivingEntityData) null, (CompoundNBT) null);
				entityToSpawn.startRiding(this);
				this.noWeapon = true;
			}
			spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
			return spawnDataIn;
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
