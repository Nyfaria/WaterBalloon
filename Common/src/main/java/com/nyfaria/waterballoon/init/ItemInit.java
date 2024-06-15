package com.nyfaria.waterballoon.init;

import com.nyfaria.waterballoon.Constants;
import com.nyfaria.waterballoon.item.BalloonItem;
import com.nyfaria.waterballoon.item.BazookaItem;
import com.nyfaria.waterballoon.item.SlingShotItem;
import com.nyfaria.waterballoon.registration.RegistrationProvider;
import com.nyfaria.waterballoon.registration.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ItemInit {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(BuiltInRegistries.ITEM, Constants.MODID);

    public static final RegistryObject<Item> WATER_BALLOON = ITEMS.register("water_balloon", () -> new BalloonItem(getItemProperties()));
    public static final RegistryObject<Item> BAZOOKA = ITEMS.register("balloon_bazooka", () -> new BazookaItem(getItemProperties()));
    public static final RegistryObject<Item> SLING_SHOT = ITEMS.register("slingshot", () -> new SlingShotItem(getItemProperties()));

    public static Item.Properties getItemProperties() {
        return new Item.Properties();
    }

    public static void loadClass() {
    }
}
