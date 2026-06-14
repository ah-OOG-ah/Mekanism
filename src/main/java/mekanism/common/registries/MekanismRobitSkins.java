package mekanism.common.registries;

import java.util.Optional;
import mekanism.api.MekanismAPI;
import mekanism.api.robit.AdvancementBasedRobitSkin;
import mekanism.api.robit.RobitSkin;
import mekanism.api.robit.RobitSkinSerializationHelper;
import mekanism.common.Mekanism;
import mekanism.common.registration.DatapackDeferredRegister;
import mekanism.common.registration.DeferredMapCodecHolder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;

public class MekanismRobitSkins {

    private MekanismRobitSkins() {
    }

    private static final DatapackDeferredRegister<RobitSkin> ROBIT_SKINS = DatapackDeferredRegister.robitSkins(Mekanism.MODID);

    public static final DeferredMapCodecHolder<RobitSkin, RobitSkin> BASIC_SERIALIZER = ROBIT_SKINS.registerCodec("basic", () -> RobitSkinSerializationHelper.NETWORK_CODEC);
    public static final DeferredMapCodecHolder<RobitSkin, AdvancementBasedRobitSkin> ADVANCEMENT_BASED_SERIALIZER = ROBIT_SKINS.registerCodec("advancement_based", () -> RobitSkinSerializationHelper.ADVANCEMENT_BASED_ROBIT_SKIN_CODEC);

    public static final ResourceKey<RobitSkin> BASE = ROBIT_SKINS.dataKey("robit");
    public static final ResourceKey<RobitSkin> ALLAY = ROBIT_SKINS.dataKey("allay");

    public static void createAndRegisterDatapack(IEventBus modEventBus) {
        ROBIT_SKINS.createAndRegisterDatapack(modEventBus, RobitSkinSerializationHelper.DIRECT_CODEC, RobitSkinSerializationHelper.NETWORK_CODEC.codec(),
              registryBuilder -> registryBuilder.defaultKey(BASE));
    }

    public static SkinLookup lookup(RegistryAccess registryAccess, ResourceKey<RobitSkin> key) {
        Registry<RobitSkin> robitSkins = registryAccess.registryOrThrow(MekanismAPI.ROBIT_SKIN_REGISTRY_NAME);
        Optional<RobitSkin> skin = robitSkins.getOptional(key);
        //noinspection OptionalIsPresent - Capturing lambda
        if (skin.isEmpty()) {
            return new SkinLookup(BASE, robitSkins.getOrThrow(BASE));
        }
        return new SkinLookup(key, skin.get());
    }

    public record SkinLookup(ResourceKey<RobitSkin> name, RobitSkin skin) {

        public ResourceLocation location() {
            return name.location();
        }
    }
}