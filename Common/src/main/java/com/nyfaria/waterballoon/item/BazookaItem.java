package com.nyfaria.waterballoon.item;

import com.google.common.collect.Lists;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.nyfaria.waterballoon.entity.ThrownBalloon;
import com.nyfaria.waterballoon.init.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class BazookaItem extends ProjectileWeaponItem {
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;

    public BazookaItem(Properties $$0) {
        super($$0);
    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (isCharged(itemStack)) {
            performShooting(level, player, usedHand, itemStack, getShootingPower(itemStack), 1.0F);
            setCharged(itemStack, false);
            return InteractionResultHolder.consume(itemStack);
        } else if (!player.getProjectile(itemStack).isEmpty()) {
            if (!isCharged(itemStack)) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
                player.startUsingItem(usedHand);
            }

            return InteractionResultHolder.consume(itemStack);
        } else {
            return InteractionResultHolder.fail(itemStack);
        }
    }
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (itemStack) -> {
            return itemStack.is(ItemInit.WATER_BALLOON.get());
        };
    }
    private static float[] getShotPitches(RandomSource random) {
        boolean bl = random.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(bl, random), getRandomShotPitch(!bl, random)};
    }
    public static void performShooting(Level level, LivingEntity shooter, InteractionHand usedHand, ItemStack crossbowStack, float velocity, float inaccuracy) {
        List<ItemStack> list = getChargedProjectiles(crossbowStack);
        float[] fs = getShotPitches(shooter.getRandom());

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemStack = (ItemStack)list.get(i);
            boolean bl = shooter instanceof Player && ((Player)shooter).getAbilities().instabuild;
            if (!itemStack.isEmpty()) {
                if (i == 0) {
                    shootProjectile(level, shooter, usedHand, crossbowStack, itemStack, fs[i], bl, velocity, inaccuracy, 0.0F);
                } else if (i == 1) {
                    shootProjectile(level, shooter, usedHand, crossbowStack, itemStack, fs[i], bl, velocity, inaccuracy, -10.0F);
                } else if (i == 2) {
                    shootProjectile(level, shooter, usedHand, crossbowStack, itemStack, fs[i], bl, velocity, inaccuracy, 10.0F);
                }
            }
        }

        onCrossbowShot(level, shooter, crossbowStack);
    }
    private static float getRandomShotPitch(boolean isHighPitched, RandomSource random) {
        float f = isHighPitched ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static void onCrossbowShot(Level level, LivingEntity shooter, ItemStack crossbowStack) {
        if (shooter instanceof ServerPlayer serverPlayer) {
            if (!level.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverPlayer, crossbowStack);
            }

            serverPlayer.awardStat(Stats.ITEM_USED.get(crossbowStack.getItem()));
        }

        clearChargedProjectiles(crossbowStack);
    }
    private static float getShootingPower(ItemStack crossbowStack) {
        return containsChargedProjectile(crossbowStack, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        int i = this.getUseDuration(stack) - timeCharged;
        float f = getPowerForTime(i, stack);
        if (f >= 1.0F && !isCharged(stack) && tryLoadProjectiles(livingEntity, stack)) {
            setCharged(stack, true);
            SoundSource soundSource = livingEntity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            level.playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundSource, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    private static boolean tryLoadProjectiles(LivingEntity shooter, ItemStack crossbowStack) {
        int j = 1;
        boolean bl = shooter instanceof Player && ((Player) shooter).getAbilities().instabuild;
        ItemStack itemStack = shooter.getProjectile(crossbowStack);

        for (int k = 0; k < j; ++k) {

            if (itemStack.isEmpty() && bl) {
                itemStack = new ItemStack(ItemInit.WATER_BALLOON.get());
            }

            if (!loadProjectile(shooter, crossbowStack, itemStack, false, bl)) {
                return false;
            }
        }

        return true;
    }

    private static boolean loadProjectile(LivingEntity shooter, ItemStack crossbowStack, ItemStack ammoStack, boolean hasAmmo, boolean isCreative) {
        if (ammoStack.isEmpty()) {
            return false;
        } else {
            boolean bl = isCreative && ammoStack.getItem() instanceof BalloonItem;
            ItemStack itemStack;
            if (!bl && !isCreative && !hasAmmo) {
                itemStack = ammoStack.split(1);
                if (ammoStack.isEmpty() && shooter instanceof Player) {
                    ((Player) shooter).getInventory().removeItem(ammoStack);
                }
            } else {
                itemStack = ammoStack.copy();
            }

            addChargedProjectile(crossbowStack, itemStack);
            return true;
        }
    }

    public static boolean isCharged(ItemStack crossbowStack) {
        CompoundTag compoundTag = crossbowStack.getTag();
        return compoundTag != null && compoundTag.getBoolean("Charged");
    }

    public static void setCharged(ItemStack crossbowStack, boolean isCharged) {
        CompoundTag compoundTag = crossbowStack.getOrCreateTag();
        compoundTag.putBoolean("Charged", isCharged);
    }

    private static void addChargedProjectile(ItemStack crossbowStack, ItemStack ammoStack) {
        CompoundTag compoundTag = crossbowStack.getOrCreateTag();
        ListTag listTag;
        if (compoundTag.contains("ChargedProjectiles", 9)) {
            listTag = compoundTag.getList("ChargedProjectiles", 10);
        } else {
            listTag = new ListTag();
        }

        CompoundTag compoundTag2 = new CompoundTag();
        ammoStack.save(compoundTag2);
        listTag.add(compoundTag2);
        compoundTag.put("ChargedProjectiles", listTag);
    }

    private static List<ItemStack> getChargedProjectiles(ItemStack crossbowStack) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundTag = crossbowStack.getTag();
        if (compoundTag != null && compoundTag.contains("ChargedProjectiles", 9)) {
            ListTag listTag = compoundTag.getList("ChargedProjectiles", 10);
            if (listTag != null) {
                for (int i = 0; i < listTag.size(); ++i) {
                    CompoundTag compoundTag2 = listTag.getCompound(i);
                    list.add(ItemStack.of(compoundTag2));
                }
            }
        }

        return list;
    }

    private static void clearChargedProjectiles(ItemStack crossbowStack) {
        CompoundTag compoundTag = crossbowStack.getTag();
        if (compoundTag != null) {
            ListTag listTag = compoundTag.getList("ChargedProjectiles", 9);
            listTag.clear();
            compoundTag.put("ChargedProjectiles", listTag);
        }

    }

    public static boolean containsChargedProjectile(ItemStack crossbowStack, Item ammoItem) {
        return getChargedProjectiles(crossbowStack).stream().anyMatch((itemStack) -> {
            return itemStack.is(ammoItem);
        });
    }

    private static void shootProjectile(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbowStack, ItemStack ammoStack, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
        if (!level.isClientSide) {
            ThrownBalloon projectile;
            projectile = getArrow(level, shooter, crossbowStack, ammoStack);

            Vec3 vec3 = shooter.getUpVector(1.0F);
            Quaternion quaternion = new Quaternion(new Vector3f(vec3), projectileAngle, true);
            Vec3 vec32 = shooter.getViewVector(1.0F);
            Vector3f vector3f = new Vector3f(vec32);
            vector3f.transform(quaternion);
            projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, inaccuracy);

            crossbowStack.hurtAndBreak(1, shooter, (livingEntity) -> {
                livingEntity.broadcastBreakEvent(hand);
            });
            level.addFreshEntity((Entity) projectile);
            level.playSound((Player) null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, soundPitch);
        }
    }

    private static ThrownBalloon getArrow(Level level, LivingEntity livingEntity, ItemStack crossbowStack, ItemStack ammoStack) {
        ThrownBalloon arrowItem = new ThrownBalloon(livingEntity, level);
        arrowItem.setItem(ammoStack);
        return arrowItem;
    }


    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundEvent = this.getStartSound(i);
            SoundEvent soundEvent2 = i == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float f = (float) (stack.getUseDuration() - remainingUseDuration) / (float) getChargeDuration(stack);
            if (f < 0.2F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.2F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                level.playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && soundEvent2 != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                level.playSound((Player) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent2, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }

    }

    public int getUseDuration(ItemStack stack) {
        return getChargeDuration(stack) + 3;
    }

    public static int getChargeDuration(ItemStack crossbowStack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, crossbowStack);
        return i == 0 ? 25 : 25 - 5 * i;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    private SoundEvent getStartSound(int enchantmentLevel) {
        switch (enchantmentLevel) {
            case 1 -> {
                return SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            }
            case 2 -> {
                return SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            }
            case 3 -> {
                return SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            }
            default -> {
                return SoundEvents.CROSSBOW_LOADING_START;
            }
        }
    }

    private static float getPowerForTime(int useTime, ItemStack crossbowStack) {
        float f = (float) useTime / (float) getChargeDuration(crossbowStack);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        List<ItemStack> list = getChargedProjectiles(stack);
        if (isCharged(stack) && !list.isEmpty()) {
            ItemStack itemStack = (ItemStack) list.get(0);
            tooltipComponents.add(Component.translatable("item.minecraft.crossbow.projectile").append(" ").append(itemStack.getDisplayName()));
            if (isAdvanced.isAdvanced() && itemStack.is(Items.FIREWORK_ROCKET)) {
                List<Component> list2 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendHoverText(itemStack, level, list2, isAdvanced);
                if (!list2.isEmpty()) {
                    for (int i = 0; i < list2.size(); ++i) {
                        list2.set(i, Component.literal("  ").append((Component) list2.get(i)).withStyle(ChatFormatting.GRAY));
                    }

                    tooltipComponents.addAll(list2);
                }
            }

        }
    }

    public boolean useOnRelease(ItemStack stack) {
        return stack.is(this);
    }

    public int getDefaultProjectileRange() {
        return 8;
    }
}
