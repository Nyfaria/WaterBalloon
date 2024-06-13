package com.nyfaria.nyfsmultiloader.init;

import com.nyfaria.nyfsmultiloader.Constants;
import com.nyfaria.nyfsmultiloader.registration.RegistrationProvider;
import com.nyfaria.nyfsmultiloader.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EntityInit {
    public static final RegistrationProvider<EntityType<?>> ENTITIES = RegistrationProvider.get(Registries.ENTITY_TYPE, Constants.MODID);
    public static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();



    protected static <T extends Entity> RegistryObject<EntityType<?>,EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(Constants.MODID + ":" + name));
    }

    protected static <T extends LivingEntity> RegistryObject<EntityType<?>,EntityType<T>> registerLivingEntity(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                                 Supplier<AttributeSupplier.Builder> attributeSupplier) {
        RegistryObject<EntityType<?>,EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(new AttributesRegister<>(entityTypeSupplier, attributeSupplier));
        return entityTypeSupplier;
    }

    protected static <T extends LivingEntity> RegistryObject<EntityType<?>,EntityType<T>> registerEntityWithEgg(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                                  Supplier<AttributeSupplier.Builder> attributeSupplier,
                                                                                                  int secondaryColor) {
        return registerEntityWithEgg(name, supplier, attributeSupplier, 0x392F24, secondaryColor);
    }

    protected static <T extends LivingEntity> RegistryObject<EntityType<?>,EntityType<T>> registerEntityWithEgg(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                                  Supplier<AttributeSupplier.Builder> attributeSupplier,
                                                                                                  int primaryColor, int secondaryColor) {
        RegistryObject<EntityType<?>,EntityType<T>> entityTypeSupplier = registerLivingEntity(name, supplier, attributeSupplier);
        return entityTypeSupplier;
    }

    public static void loadClass() {
    }


    public record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier,
                                                             Supplier<AttributeSupplier.Builder> factory) {
    }
}
