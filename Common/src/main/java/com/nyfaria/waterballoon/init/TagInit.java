package com.nyfaria.waterballoon.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagInit {
    public static TagKey<Item> DYES = TagKey.create(BuiltInRegistries.ITEM.key(), new ResourceLocation("c","dyes"));

    public static void loadClass() {
    }
}
