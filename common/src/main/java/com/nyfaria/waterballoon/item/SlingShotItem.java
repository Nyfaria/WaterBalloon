package com.nyfaria.waterballoon.item;

import com.nyfaria.waterballoon.entity.ThrownBalloon;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class SlingShotItem extends ProjectileWeaponItem {
    public static final int MAX_DRAW_DURATION = 20;
    public static final int DEFAULT_RANGE = 15;

    public SlingShotItem(Item.Properties properties) {
        super(properties);
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player) {
            ItemStack itemStack = player.getProjectile(stack);
            if (!itemStack.isEmpty()) {
                int i = this.getUseDuration(stack) - timeCharged;
                float f = getPowerForTime(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(stack, itemStack, player);
                    if (!level.isClientSide() && !list.isEmpty()) {
                        this.shoot(level, player, player.getUsedItemHand(), stack, list, f * 3.0F, 1.0F, f == 1.0F, (LivingEntity)null);
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    protected Projectile createProjectile(Level pLevel, LivingEntity pShooter, ItemStack pWeapon, ItemStack pAmmo, boolean pIsCrit) {
        ThrownBalloon balloon = new ThrownBalloon(pShooter,pLevel);
        balloon.setItem(pAmmo);
        return balloon;
    }

    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, velocity, inaccuracy);
    }

    public static float getPowerForTime(int charge) {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        boolean bl = !player.getProjectile(itemStack).isEmpty();
        if (!player.hasInfiniteMaterials() && !bl) {
            return InteractionResultHolder.fail(itemStack);
        } else {
            player.startUsingItem(usedHand);
            return InteractionResultHolder.consume(itemStack);
        }
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack->stack.getItem() instanceof BalloonItem;
    }

    public int getDefaultProjectileRange() {
        return 15;
    }
}
