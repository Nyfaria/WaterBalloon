package com.nyfaria.waterballoon.init;

import com.nyfaria.grinnersents.registration.RegistrationProvider;
import com.nyfaria.grinnersents.registration.RegistryObject;
import com.nyfaria.waterballoon.Constants;
import com.nyfaria.waterballoon.item.BalloonItem;
import com.nyfaria.waterballoon.item.BazookaItem;
import com.nyfaria.waterballoon.item.SlingShotItem;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ItemInit {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registry.ITEM, Constants.MODID);

    public static final RegistryObject<Item> WATER_BALLOON = ITEMS.register("water_balloon", () -> new BalloonItem(getItemProperties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> BAZOOKA = ITEMS.register("balloon_bazooka", () -> new BazookaItem(getItemProperties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> SLING_SHOT = ITEMS.register("sling_shot", () -> new SlingShotItem(getItemProperties().tab(CreativeModeTab.TAB_MISC)));

    public static Item.Properties getItemProperties() {
        return new Item.Properties();
    }

    public static void loadClass() {
    }
}
