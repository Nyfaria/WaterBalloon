package com.nyfaria.nyfsmultiloader.datagen;

import com.nyfaria.nyfsmultiloader.Constants;
import com.nyfaria.nyfsmultiloader.registration.RegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {
    public ModSoundProvider(PackOutput generator, ExistingFileHelper helper) {
        super(generator, Constants.MODID, helper);
    }

    @Override
    public void registerSounds() {
//        SoundInit.SOUNDS.getEntries().forEach(this::addSound);
    }

    public void addSound(RegistryObject<SoundEvent,SoundEvent> entry) {
        add(entry, SoundDefinition.definition().with(sound(entry.getId())));
    }
}
