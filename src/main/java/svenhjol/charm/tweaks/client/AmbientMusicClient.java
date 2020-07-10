package svenhjol.charm.tweaks.client;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.tweaks.module.AmbientMusicImprovements;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.SoundHelper;
import svenhjol.meson.helper.WorldHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class AmbientMusicClient {
    public static List<AmbientMusicCondition> conditions = new ArrayList<>();
    public static ISound currentMusic;
    public static int timeUntilNextMusic = 100;
    private final static Random random;
    private final static Minecraft mc;
    private int ticksBeforeStop;
    private ISound musicToStop = null;
    public static boolean isEnabled = true;
    public static ResourceLocation dim = null;

    public void setupClient(FMLClientSetupEvent event) {
        isEnabled = Meson.isModuleEnabled("charm:ambient_music_improvements");

        if (AmbientMusicImprovements.playCreativeMusic) {
            conditions.add(new AmbientMusicCondition(SoundEvents.MUSIC_CREATIVE, 1200, 3600, mc -> mc.player != null
                && (!mc.player.isCreative() || !mc.player.isSpectator())
                && WorldHelper.isDimension(mc.player.world, new ResourceLocation("overworld"))
                && random.nextFloat() < 0.25F
            ));
        }
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) {
            BlockState state = event.getWorld().getBlockState(event.getPos());
            if (event.getEntity() instanceof PlayerEntity
                && event.getItemStack().getItem() instanceof MusicDiscItem
                && state.getBlock() == Blocks.JUKEBOX
                && !state.get(JukeboxBlock.HAS_RECORD)
            ) {
                SoundHelper.getSoundHandler().stop(null, SoundCategory.MUSIC);
            }
        }
    }

    @SubscribeEvent
    public void onSoundPlay(SoundEvent.SoundSourceEvent event) {
        ISound triggered = event.getSound();
        if (triggered.getCategory() == SoundCategory.MUSIC) {
            // check if there are any records playing
            SoundHelper.getPlayingSounds().forEach((category, sound) -> {
                if (category == SoundCategory.RECORDS) {
                    musicToStop = triggered;
                    Meson.LOG.debug("Triggered background music while record playing");
                }
            });
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END
            && musicToStop != null
            && ++ticksBeforeStop % 10 == 0
        ) {
            SoundHelper.getSoundHandler().stop(musicToStop);
            ticksBeforeStop = 0;
            musicToStop = null;
        }
    }

    /**
     * @see net.minecraft.client.audio.MusicTicker#tick
     * @see net.minecraft.client.audio.MusicTicker#func_239539_a_
     */
    public static boolean handleTick(@Nullable ISound current) {
        if (mc.world == null) return false;
        AmbientMusicCondition ambient = getAmbientMusicType();

        if (currentMusic != null) {
//            if (!ambient.getSound().getName().equals(currentMusic.getSoundLocation())) {
//                mc.getSoundHandler().stop(currentMusic);
//                currentMusic = null;
//                timeUntilNextMusic = MathHelper.nextInt(random, 0, ambient.getMinDelay() / 2);
//            }

            if (!WorldHelper.isDimension(mc.world, dim)) {
                forceStop();
            }

            if (!mc.getSoundHandler().isPlaying(currentMusic)) {
                currentMusic = null;
                int max = AmbientMusicImprovements.maxDelayOverride == 0 ? ambient.getMaxDelay() : AmbientMusicImprovements.maxDelayOverride;
                timeUntilNextMusic = Math.min(MathHelper.nextInt(random, ambient.getMinDelay(), max), timeUntilNextMusic);
            }
        }

        timeUntilNextMusic = Math.min(timeUntilNextMusic, ambient.getMaxDelay());

        if (currentMusic == null && timeUntilNextMusic-- <= 0) {
            dim = WorldHelper.getDimension(mc.world);
            currentMusic = SimpleSound.music(ambient.getSound());

            if (currentMusic.getSound() != SoundHandler.MISSING_SOUND) {
                mc.getSoundHandler().play(currentMusic);
                timeUntilNextMusic = Integer.MAX_VALUE;
            }
        }

        return true;
    }

    public static boolean handleStop() {
        if (currentMusic != null) {
            mc.getSoundHandler().stop(currentMusic);
            currentMusic = null;
            timeUntilNextMusic = 0;
        }
        return true;
    }

    public static boolean handlePlaying(BackgroundMusicSelector music) {
        return currentMusic != null && music.func_232661_a_().getName().equals(currentMusic.getSoundLocation());
    }

    public static void forceStop() {
        mc.getSoundHandler().stop(currentMusic);
        currentMusic = null;
        timeUntilNextMusic = 3600;
    }

    public static AmbientMusicCondition getAmbientMusicType() {
        AmbientMusicCondition condition = null;

        if (conditions != null) {
            for (AmbientMusicCondition c : conditions) {
                if (c.handle()) {
                    condition = c;
                    break;
                }
            }
        }

        if (condition == null) {
            condition = new AmbientMusicCondition(Minecraft.getInstance().func_238178_U_());
        }

        return condition;
    }

    public static class AmbientMusicCondition {
        private net.minecraft.util.SoundEvent sound;
        private int minDelay;
        private int maxDelay;
        private Predicate<Minecraft> condition;
        private static final Minecraft mc;

        public AmbientMusicCondition(net.minecraft.util.SoundEvent sound, int minDelay, int maxDelay, Predicate<Minecraft> condition) {
            this.sound = sound;
            this.minDelay = minDelay;
            this.maxDelay = maxDelay;
            this.condition = condition;
        }

        public AmbientMusicCondition(BackgroundMusicSelector music) {
            this.sound = music.func_232661_a_();
            this.minDelay = music.func_232664_b_();
            this.maxDelay = music.func_232666_c_();
        }

        public boolean handle() {
            if (condition == null) return false;
            return condition.test(mc);
        }

        public net.minecraft.util.SoundEvent getSound() {
            return sound;
        }

        public int getMaxDelay() {
            return maxDelay;
        }

        public int getMinDelay() {
            return minDelay;
        }

        static {
            mc = Minecraft.getInstance();
        }
    }

    static {
        random = new Random();
        mc = Minecraft.getInstance();
    }
}
