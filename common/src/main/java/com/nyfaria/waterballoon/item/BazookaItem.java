package com.nyfaria.waterballoon.item;

import com.google.common.collect.*;
import com.nyfaria.waterballoon.entity.*;
import net.minecraft.*;
import net.minecraft.advancements.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;
import org.joml.*;

import java.lang.Math;
import java.util.*;
import java.util.function.*;

public class BazookaItem extends ProjectileWeaponItem {
    private static final int MAX_CHARGE_DURATION = 25;
    public static final int DEFAULT_RANGE = 8;
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;
    private static final float START_SOUND_PERCENT = 0.2F;
    private static final float MID_SOUND_PERCENT = 0.5F;
    private static final float ARROW_POWER = 3.15F;
    private static final float FIREWORK_POWER = 1.6F;
    public static final float MOB_ARROW_POWER = 1.6F;
    private static final CrossbowItem.ChargingSounds DEFAULT_SOUNDS = new CrossbowItem.ChargingSounds(Optional.of(SoundEvents.CROSSBOW_LOADING_START), Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE), Optional.of(SoundEvents.CROSSBOW_LOADING_END));

    public BazookaItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return stack -> stack.getItem() instanceof BalloonItem;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() instanceof BalloonItem;
    }

    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        ChargedProjectiles chargedprojectiles = itemstack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
            this.performShooting(pLevel, pPlayer, pHand, itemstack, getShootingPower(chargedprojectiles), 1.0F, null);
            return InteractionResult.CONSUME;
        } else if (!pPlayer.getProjectile(itemstack).isEmpty()) {
            this.startSoundPlayed = false;
            this.midLoadSoundPlayed = false;
            pPlayer.startUsingItem(pHand);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.FAIL;
        }
    }

    private static float getShootingPower(ChargedProjectiles pProjectile) {
        return pProjectile.contains(Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level pLevel, LivingEntity entity, int pTimeLeft) {
        int timeHeld = this.getUseDuration(itemStack, entity) - pTimeLeft;
        return getPowerForTime(timeHeld, itemStack, entity) >= 1.0F && isCharged(itemStack);
    }

    private static boolean tryLoadProjectiles(LivingEntity pShooter, ItemStack pCrossbowStack) {
        List<ItemStack> list = draw(pCrossbowStack, pShooter.getProjectile(pCrossbowStack), pShooter);
        if (!list.isEmpty()) {
            pCrossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.ofNonEmpty(list));
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCharged(ItemStack pCrossbowStack) {
        ChargedProjectiles chargedprojectiles = pCrossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
        return !chargedprojectiles.isEmpty();
    }

    @Override
    protected void shootProjectile(
            LivingEntity pShooter, Projectile pProjectile, int pIndex, float pVelocity, float pInaccuracy, float pAngle, @Nullable LivingEntity pTarget
    ) {
        Vector3f vector3f;
        if (pTarget != null) {
            double d0 = pTarget.getX() - pShooter.getX();
            double d1 = pTarget.getZ() - pShooter.getZ();
            double d2 = Math.sqrt(d0 * d0 + d1 * d1);
            double d3 = pTarget.getY(0.3333333333333333) - pProjectile.getY() + d2 * 0.2F;
            vector3f = getProjectileShotVector(pShooter, new Vec3(d0, d3, d1), pAngle);
        } else {
            Vec3 vec3 = pShooter.getUpVector(1.0F);
            Quaternionf quaternionf = new Quaternionf()
                    .setAngleAxis((double) (pAngle * (float) (Math.PI / 180.0)), vec3.x, vec3.y, vec3.z);
            Vec3 vec31 = pShooter.getViewVector(1.0F);
            vector3f = vec31.toVector3f().rotate(quaternionf);
        }

        pProjectile.shoot((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), pVelocity, pInaccuracy);
        float f = getShotPitch(pShooter.getRandom(), pIndex);
        pShooter.level().playSound(null, pShooter.getX(), pShooter.getY(), pShooter.getZ(), SoundEvents.CROSSBOW_SHOOT, pShooter.getSoundSource(), 1.0F, f);
    }

    private static Vector3f getProjectileShotVector(LivingEntity pShooter, Vec3 pDistance, float pAngle) {
        Vector3f vector3f = pDistance.toVector3f().normalize();
        Vector3f vector3f1 = new Vector3f(vector3f).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double) vector3f1.lengthSquared() <= 1.0E-7) {
            Vec3 vec3 = pShooter.getUpVector(1.0F);
            vector3f1 = new Vector3f(vector3f).cross(vec3.toVector3f());
        }

        Vector3f vector3f2 = new Vector3f(vector3f).rotateAxis((float) (Math.PI / 2), vector3f1.x, vector3f1.y, vector3f1.z);
        return new Vector3f(vector3f).rotateAxis(pAngle * (float) (Math.PI / 180.0), vector3f2.x, vector3f2.y, vector3f2.z);
    }

    @Override
    protected Projectile createProjectile(Level pLevel, LivingEntity pShooter, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit) {
        ThrownBalloon projectile = new ThrownBalloon(pShooter, pLevel, pAmmo);
        projectile.setItem(pAmmo);
        return projectile;
    }

    @Override
    protected int getDurabilityUse(ItemStack pStack) {
        return pStack.is(Items.FIREWORK_ROCKET) ? 3 : 1;
    }

    public void performShooting(
            Level pLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pWeapon, float pVelocity, float pInaccuracy, @Nullable LivingEntity pTarget
    ) {
        if (!pLevel.isClientSide()) {
            ChargedProjectiles chargedprojectiles = pWeapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
                this.shoot(
                        (ServerLevel) pLevel, pShooter, pHand, pWeapon, chargedprojectiles.itemCopies(), pVelocity, pInaccuracy, pShooter instanceof Player, pTarget
                );
                if (pShooter instanceof ServerPlayer serverplayer) {
                    CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, pWeapon);
                    serverplayer.awardStat(Stats.ITEM_USED.get(pWeapon.getItem()));
                }
            }
        }
    }

    private static float getShotPitch(RandomSource pRandom, int pIndex) {
        return pIndex == 0 ? 1.0F : getRandomShotPitch((pIndex & 1) == 1, pRandom);
    }

    private static float getRandomShotPitch(boolean pIsHighPitched, RandomSource pRandom) {
        float f = pIsHighPitched ? 0.63F : 0.43F;
        return 1.0F / (pRandom.nextFloat() * 0.5F + 1.8F) + f;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int ticksRemaining) {
        if (!level.isClientSide()) {
            CrossbowItem.ChargingSounds sounds = this.getChargingSounds(itemStack);
            float tickPercent = (float)(itemStack.getUseDuration(entity) - ticksRemaining) / (float)getChargeDuration(itemStack, entity);
            if (tickPercent < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (tickPercent >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                sounds.start().ifPresent((sound) -> level.playSound((Entity)null, entity.getX(), entity.getY(), entity.getZ(), (SoundEvent)sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F));
            }

            if (tickPercent >= 0.5F && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                sounds.mid().ifPresent((sound) -> level.playSound((Entity)null, entity.getX(), entity.getY(), entity.getZ(), (SoundEvent)sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F));
            }

            if (tickPercent >= 1.0F && !isCharged(itemStack) && tryLoadProjectiles(entity, itemStack)) {
                sounds.end().ifPresent((sound) -> level.playSound((Entity)null, entity.getX(), entity.getY(), entity.getZ(), (SoundEvent)sound.value(), entity.getSoundSource(), 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F));
            }
        }

    }


    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return getChargeDuration(pStack,pEntity) + 3;
    }

    public static int getChargeDuration(ItemStack pCrossbowStack, LivingEntity user) {
        return 25;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        return ItemUseAnimation.CROSSBOW;
    }

    private SoundEvent getStartSound(int pEnchantmentLevel) {
        switch (pEnchantmentLevel) {
            case 1:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_1.value();
            case 2:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_2.value();
            case 3:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_3.value();
            default:
                return SoundEvents.CROSSBOW_LOADING_START.value();
        }
    }

    private static float getPowerForTime(int timeHeld, ItemStack itemStack, LivingEntity holder) {
        float pow = (float)timeHeld / (float)getChargeDuration(itemStack, holder);
        if (pow > 1.0F) {
            pow = 1.0F;
        }

        return pow;
    }

    CrossbowItem.ChargingSounds getChargingSounds(ItemStack itemStack) {
        return (CrossbowItem.ChargingSounds) EnchantmentHelper.pickHighestLevel(itemStack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS).orElse(DEFAULT_SOUNDS);
    }

    @Override
    public boolean useOnRelease(ItemStack pStack) {
        return pStack.is(this);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }
}