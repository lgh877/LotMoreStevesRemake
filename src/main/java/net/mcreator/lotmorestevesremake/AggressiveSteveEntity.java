/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.lotmorestevesremake as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.lotmorestevesremake;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.potion.EffectInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Entity;

import net.mcreator.lotmorestevesremake.potion.CursedDiversionPotionEffect;
import net.mcreator.lotmorestevesremake.entity.StevagerEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AggressiveSteveEntity extends MonsterEntity {
	public AggressiveSteveEntity(EntityType<? extends MonsterEntity> type, World world) {
		super(type, world);
		setNoAI(false);
	}

	@Override
	public boolean isOnSameTeam(Entity entityIn) {
		if (super.isOnSameTeam(entityIn))
			return true;
		else if (EntityTypeTags.getCollection().getTagByID(new ResourceLocation("lotmorestevesremake:we_are_steves")).contains(entityIn.getType()))
			return true;
		else
			return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float amount) {
		if (damagesource.getTrueSource() instanceof LivingEntity && this.isOnSameTeam(damagesource.getTrueSource()))
			return false;
		if (this.getRidingEntity() != null) {
			if (this.getRidingEntity() instanceof StevagerEntity.CustomEntity) {
				this.getRidingEntity().attackEntityFrom(damagesource, amount);
				return false;
			}
		}
		return super.attackEntityFrom(damagesource, amount);
	}

	public boolean attackEntityAsMob(Entity entityIn) {
		((LivingEntity) entityIn).addPotionEffect(new EffectInstance(CursedDiversionPotionEffect.potion, (int) 200, (int) 0));
		return super.attackEntityAsMob(entityIn);
	}
}
