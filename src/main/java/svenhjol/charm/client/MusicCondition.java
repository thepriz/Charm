package svenhjol.charm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.util.SoundEvent;

import java.util.function.Predicate;

public class MusicCondition {
    private SoundEvent sound;
    private int minDelay;
    private int maxDelay;
    private Predicate<Minecraft> condition;

    public MusicCondition(SoundEvent sound, int minDelay, int maxDelay, Predicate<Minecraft> condition) {
        this.sound = sound;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.condition = condition;
    }

    public MusicCondition(BackgroundMusicSelector music) {
        this.sound = music.func_232661_a_();
        this.minDelay = music.func_232664_b_();
        this.maxDelay = music.func_232666_c_();
    }

    public boolean handle() {
        if (condition == null) return false;
        return condition.test(Minecraft.getInstance());
    }

    public SoundEvent getSound() {
        return sound;
    }

    public int getMaxDelay() {
        return maxDelay;
    }

    public int getMinDelay() {
        return minDelay;
    }
}
