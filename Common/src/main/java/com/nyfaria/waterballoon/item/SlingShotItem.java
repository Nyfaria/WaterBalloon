package com.nyfaria.waterballoon.item;

import com.nyfaria.waterballoon.entity.ThrownBalloon;
import com.nyfaria.waterballoon.init.ItemInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class SlingShotItem extends ProjectileWeaponItem implements Vanishable {
    public static final int MAX_DRAW_DURATION = 20;
    public static final int DEFAULT_RANGE = 15;

    public SlingShotItem(Item.Properties $$0) {
        super($$0);
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int useTime) {
        if (livingEntity instanceof Player $$4) {
            boolean $$5 = $$4.getAbilities().instabuild;
            ItemStack $$6 = $$4.getProjectile(stack);
            if (!$$6.isEmpty() || $$5) {
                if ($$6.isEmpty()) {
                    $$6 = new ItemStack(ItemInit.WATER_BALLOON.get());
                }

                int $$7 = this.getUseDuration(stack) - useTime;
                float $$8 = getPowerForTime($$7);
                if (!((double) $$8 < 0.1)) {
                    boolean $$9 = $$5 && $$6.getItem() instanceof BalloonItem;
                    if (!level.isClientSide) {

                        ThrownBalloon $$11 = new ThrownBalloon(livingEntity, level);
                        $$11.setItem($$6);
                        $$11.shootFromRotation($$4, $$4.getXRot(), $$4.getYRot(), 0.0F, $$8 * 3.0F, 1.0F);

                        stack.hurtAndBreak(1, $$4, ($$1x) -> {
                            $$1x.broadcastBreakEvent($$4.getUsedItemHand());
                        });


                        level.addFreshEntity($$11);
                    }

                    level.playSound((Player) null, $$4.getX(), $$4.getY(), $$4.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + $$8 * 0.5F);
                    if (!$$9 && !$$4.getAbilities().instabuild) {
                        $$6.shrink(1);
                        if ($$6.isEmpty()) {
                            $$4.getInventory().removeItem($$6);
                        }
                    }

                    $$4.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public static float getPowerForTime(int $$0) {
        float $$1 = (float) $$0 / 20.0F;
        $$1 = ($$1 * $$1 + $$1 * 2.0F) / 3.0F;
        if ($$1 > 1.0F) {
            $$1 = 1.0F;
        }

        return $$1;
    }

    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }

    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        ItemStack stack = $$1.getProjectile($$3);
        boolean $$4 = !stack.isEmpty();
        if (!$$1.getAbilities().instabuild && !$$4) {
            return InteractionResultHolder.fail($$3);
        } else {
            if(stack.hasTag()) {
                $$3.getOrCreateTag().putInt("color", stack.getTag().getInt("color"));
            }
            $$1.startUsingItem($$2);
            return InteractionResultHolder.consume($$3);
        }
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() instanceof BalloonItem;
    }

    public int getDefaultProjectileRange() {
        return 15;
    }
}
