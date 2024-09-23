package com.nyfaria.waterballoon.init;

import com.nyfaria.grinnersents.registration.RegistrationProvider;
import com.nyfaria.grinnersents.registration.RegistryObject;
import com.nyfaria.waterballoon.Constants;
import com.nyfaria.waterballoon.entity.ThrownBalloon;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EntityInit {
    public static final RegistrationProvider<EntityType<?>> ENTITIES = RegistrationProvider.get(Registry.ENTITY_TYPE, Constants.MODID);
    public static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();

    public static final RegistryObject<EntityType<ThrownBalloon>> THROWN_BALLOON = registerEntity("thrown_balloon", ()-> EntityType.Builder.of(ThrownBalloon::new, MobCategory.MISC));

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(Constants.MODID + ":" + name));
    }

    private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                         Supplier<AttributeSupplier.Builder> attributeSupplier) {
        RegistryObject<EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(new AttributesRegister<>(entityTypeSupplier, attributeSupplier));
        return entityTypeSupplier;
    }

    public static void loadClass() {
    }


    public record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier, Supplier<AttributeSupplier.Builder> factory) {}
}