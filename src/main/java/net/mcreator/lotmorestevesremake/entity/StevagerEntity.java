
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.network.IPacket;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Item;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;

import net.mcreator.lotmorestevesremake.procedures.StevillagersSpawnConditionProcedure;
import net.mcreator.lotmorestevesremake.itemgroup.MeetTheStevesItemGroup;
import net.mcreator.lotmorestevesremake.entity.renderer.StevagerRenderer;
import net.mcreator.lotmorestevesremake.LotmorestevesremakeModElements;

import java.util.stream.Stream;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.AbstractMap;

@LotmorestevesremakeModElements.ModElement.Tag
public class StevagerEntity extends LotmorestevesremakeModElements.ModElement {
	public static EntityType entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
			.size(1.95f, 2.2f)).build("stevager").setRegistryName("stevager");

	public StevagerEntity(LotmorestevesremakeModElements instance) {
		super(instance, 16);
		FMLJavaModLoadingContext.get().getModEventBus().register(new StevagerRenderer.ModelRegisterHandler());
		FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -16751002, -10092493, new Item.Properties().group(MeetTheStevesItemGroup.tab))
				.setRegistryName("stevager_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(entity, 3, 1, 1));
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
			ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 100);
			ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 64);
			ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 12);
			ammma = ammma.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.75);
			ammma = ammma.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.5);
			event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends RavagerEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public boolean isOnSameTeam(Entity entityIn) {
			if (super.isOnSameTeam(entityIn))
				return true;
			else if (EntityTypeTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:we_are_steves"))
					.contains(entityIn.getType()))
				return true;
			else
				return false;
		}

		protected float getSoundPitch() {
			return 0.6f;
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 20;
			setNoAI(false);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			this.goalSelector.addGoal(0, new SwimGoal(this));
			this.goalSelector.addGoal(4, new CustomEntity.AttackGoal2());
			this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.4D));
			this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp());
			this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, MobEntity.class, false, false));
			this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, PlayerEntity.class, false, false));
			this.targetSelector.addGoal(7, new NearestAttackableTargetGoal(this, ServerPlayerEntity.class, false, false));
		}

		public LivingEntity getSteveInBox(double sizeup) {
			for (LivingEntity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(sizeup))) {
				if (EntityTypeTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:we_are_steves")).contains(entity.getType())
						&& !entity.isPassenger() && !entity.isBeingRidden() && entity.isAlive()) {
					return entity;
				}
			}
			return null;
		}

		public void livingTick() {
			super.livingTick();
			if (this.isAlive()) {
				if (this.isBeingRidden()) {
					if ((this.getControllingPassenger() instanceof MobEntity)
							&& ((MobEntity) this.getControllingPassenger()).getAttackTarget() != null)
						this.setAttackTarget(((MobEntity) this.getControllingPassenger()).getAttackTarget());
					this.healRiders(this.getPassengers());
				} else {
					if (rand.nextInt(40) == 0) {
						LivingEntity steveH = this.getSteveInBox(5);
						if (steveH != null && steveH.getHealth() / steveH.getMaxHealth() < 0.5f) {
							steveH.startRiding(this, true);
						}
					}
				}
			}
		}

		private void healRiders(List<Entity> entities) {
			LivingEntity entityL;
			for (Entity entity : entities) {
				if (entity instanceof LivingEntity) {
					entityL = (LivingEntity) entity;
					if (EntityTypeTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:we_are_steves"))
							.contains(entityL.getType()) && entityL.isAlive() && rand.nextInt(10) == 0) {
						entityL.setHealth(entityL.getHealth() + 1);
					} else if (rand.nextInt(70) == 0) {
						if (!this.world.isRemote)
							entity.dismount();
						if (!EntityTypeTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:we_are_steves"))
								.contains(entity.getType()))
							entity.setMotion(entity.getMotion().add((rand.nextFloat() - 0.5f) * 2, 1, (rand.nextFloat() - 0.5f) * 2));
					}
				} else if (rand.nextInt(70) == 0) {
					if (!this.world.isRemote)
						entity.dismount();
					entity.setMotion(entity.getMotion().add((rand.nextFloat() - 0.5f) * 2, 1, (rand.nextFloat() - 0.5f) * 2));
				}
			}
		}

		class AttackGoal2 extends MeleeAttackGoal {
			public AttackGoal2() {
				super(CustomEntity.this, 1.0D, true);
			}

			protected double getAttackReachSqr(LivingEntity attackTarget) {
				float f = CustomEntity.this.getWidth() - 0.1F;
				return (double) (f * 2.0F * f * 2.0F + attackTarget.getWidth());
			}
		}

		public boolean attackEntityFrom(DamageSource damagesource, float amount) {
			if (damagesource.getTrueSource() instanceof LivingEntity && this.isOnSameTeam(damagesource.getTrueSource()))
				return false;
			return super.attackEntityFrom(damagesource, amount);
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public double getMountedYOffset() {
			return 1.7;
		}

		@Override
		public net.minecraft.util.SoundEvent getAmbientSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(""));
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
