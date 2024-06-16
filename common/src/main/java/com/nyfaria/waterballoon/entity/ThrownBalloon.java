package com.nyfaria.waterballoon.entity;

import com.nyfaria.waterballoon.init.EntityInit;
import com.nyfaria.waterballoon.init.ItemInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ThrownBalloon extends ThrowableItemProjectile {

    public ThrownBalloon(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public ThrownBalloon(double $$1, double $$2, double $$3, Level $$4) {
        super(EntityInit.THROWN_BALLOON.get(), $$1, $$2, $$3, $$4);
    }

    public ThrownBalloon(LivingEntity $$1, Level $$2) {
        super(EntityInit.THROWN_BALLOON.get(), $$1, $$2);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.WATER_BALLOON.get();
    }

    @Override
    protected void onHit(HitResult $$0) {
        super.onHit($$0);
        this.discard();
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return (LivingEntity) super.getOwner();
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if(!level().isClientSide){
            AreaEffectCloud cloud = new AreaEffectCloud(level(), getX(), getY(), getZ());
            cloud.setRadius(2.0F);
            cloud.setDuration(20);
            cloud.setParticle(ParticleTypes.SPLASH);
            cloud.setWaitTime(0);
            cloud.setRadiusPerTick(-0.2f);
            cloud.setOwner(getOwner());
            PotionContents potionContents = new PotionContents(Optional.of(Potions.SLOWNESS), Optional.of(0x0000FF), List.of());
            cloud.setPotionContents(potionContents);
            level().addFreshEntity(cloud);
            level().playSound(null, blockPosition(), SoundEvents.DOLPHIN_SPLASH, getSoundSource(), 1.0F, 1.0F);

        }

        super.onHitBlock($$0);
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if(!level().isClientSide){
            if($$0.getEntity() instanceof LivingEntity le){
                le.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
                if(le instanceof EnderMan){
                    le.hurt(level().damageSources().magic(),5.0f);
                }
                level().playSound(null, blockPosition(), SoundEvents.DOLPHIN_SPLASH, getSoundSource(), 1.0F, 1.0F);
            }
        }

        super.onHitEntity($$0);
    }

    @Override
    public double getDefaultGravity() {
        return 0.1;
    }
}
