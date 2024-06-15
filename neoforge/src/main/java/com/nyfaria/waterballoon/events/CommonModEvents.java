package com.nyfaria.waterballoon.events;

import com.nyfaria.waterballoon.init.EntityInit;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(bus=EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void attribs(EntityAttributeCreationEvent e) {
        EntityInit.attributeSuppliers.forEach(p -> e.put(p.entityTypeSupplier().get(), p.factory().get().build()));
    }
}
