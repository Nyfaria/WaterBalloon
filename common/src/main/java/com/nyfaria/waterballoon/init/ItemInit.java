package com.nyfaria.waterballoon.init;

import com.nyfaria.waterballoon.Constants;
import com.nyfaria.waterballoon.item.BalloonItem;
import com.nyfaria.waterballoon.item.BazookaItem;
import com.nyfaria.waterballoon.item.SlingShotItem;
import com.nyfaria.waterballoon.registration.RegistrationProvider;
import com.nyfaria.waterballoon.registration.RegistryObject;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.Blocks;

public class ItemInit {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, Constants.MODID);

    public static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, Constants.MODID);
    public static final RegistryObject<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(Constants.MODID, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(Blocks.DIRT))
            .displayItems(
                    (itemDisplayParameters, output) -> {
                        ITEMS.getEntries().forEach(item -> output.accept(new ItemStack(item.get())));
                    }).title(Component.translatable("itemGroup." + Constants.MODID + ".tab"))
            .build());

    public static final RegistryObject<Item, BalloonItem> WATER_BALLOON = ITEMS.register("water_balloon", () -> new BalloonItem(getItemProperties().component(DataComponents.DYED_COLOR, new DyedItemColor(-1, true))));
    public static final RegistryObject<Item, BazookaItem> BAZOOKA = ITEMS.register("balloon_bazooka", () -> new BazookaItem(getItemProperties()));
    public static final RegistryObject<Item, SlingShotItem> SLING_SHOT = ITEMS.register("slingshot", () -> new SlingShotItem(getItemProperties().component(DataComponents.DYED_COLOR, new DyedItemColor(-1, true))));

    public static Item.Properties getItemProperties() {
        return new Item.Properties();
    }

    public static void loadClass() {
    }

}
