package svenhjol.meson.helper;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.meson.mixin.SoundEngineAccessor;
import svenhjol.meson.mixin.SoundHandlerAccessor;

public class SoundHelper {
    public static SoundHandler getSoundHandler() {
        return Minecraft.getInstance().getSoundHandler();
    }

    @OnlyIn(Dist.CLIENT)
    public static Multimap<SoundCategory, ISound> getPlayingSounds() {
        SoundEngine sndManager = ((SoundHandlerAccessor) getSoundHandler()).getSndManager();
        return ((SoundEngineAccessor)sndManager).getCategorySounds();
    }
}