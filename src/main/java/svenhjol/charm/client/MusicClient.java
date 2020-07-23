package svenhjol.charm.client;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.module.MusicImprovements;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.SoundHelper;
import svenhjol.meson.helper.WorldHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicClient {
    private MesonModule module;
    private ISound musicToStop = null;
    private int ticksBeforeStop = 0;
    private static ISound currentMusic;
    private static ResourceLocation currentDim = null;
    private static int timeUntilNextMusic = 100;
    private static final List<MusicCondition> musicConditions = new ArrayList<>();
    public static boolean enabled;

    public MusicClient(MesonModule module, FMLClientSetupEvent event) {
        this.module = module;

        // set statically so hooks can check this is enabled
        enabled = module.enabled;

        if (MusicImprovements.playCreativeMusic) {
            addCreativeMusicCondition();
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCanceled()) {
            stopRecord(event.getEntity(), event.getPos(), event.getItemStack());
        }
    }

    @SubscribeEvent
    public void onSoundSource(SoundEvent.SoundSourceEvent event) {
        if (!event.isCanceled()) {
            checkBackgroundMusic(event.getSound());
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

    public void addCreativeMusicCondition() {
        musicConditions.add(new MusicCondition(
            SoundEvents.MUSIC_CREATIVE, 1200, 3600, mc -> mc.player != null
                && (!mc.player.isCreative() || !mc.player.isSpectator())
                && WorldHelper.isDimension(mc.player.world, new ResourceLocation("overworld"))
                && new Random().nextFloat() < 0.25F
        ));
    }

    public void stopRecord(Entity entity, BlockPos pos, ItemStack stack) {
        if (entity.world.isRemote
            && entity instanceof PlayerEntity
            && stack.getItem() instanceof MusicDiscItem
        ) {
            BlockState state = entity.world.getBlockState(pos);
            if (state.getBlock() == Blocks.JUKEBOX && !state.get(JukeboxBlock.HAS_RECORD))
                SoundHelper.getSoundHandler().stop(null, SoundCategory.MUSIC);
        }
    }

    public void checkBackgroundMusic(ISound sound) {
        if (sound.getCategory() == SoundCategory.MUSIC) {

            // check if there are any records playing
            SoundHelper.getPlayingSounds().forEach((category, s) -> {
                if (category == SoundCategory.RECORDS) {
                    musicToStop = s;
                    Meson.LOG.debug("Triggered background music while record playing");
                }
            });
        }
    }

    /**
     * @see net.minecraft.client.audio.MusicTicker#tick
     * @see net.minecraft.client.audio.MusicTicker#func_239539_a_
     */
    public static boolean handleTick(@Nullable ISound current) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.world == null) return false;
        MusicCondition ambient = getMusicCondition();

        if (currentMusic != null) {
            if (!WorldHelper.isDimension(mc.world, currentDim))
                forceStop();

            if (!mc.getSoundHandler().isPlaying(currentMusic)) {
                currentMusic = null;
                timeUntilNextMusic = Math.min(MathHelper.nextInt(new Random(), ambient.getMinDelay(), 3600), timeUntilNextMusic);
            }
        }

        timeUntilNextMusic = Math.min(timeUntilNextMusic, ambient.getMaxDelay());

        if (currentMusic == null && timeUntilNextMusic-- <= 0) {
            currentDim = WorldHelper.getDimension(mc.world);
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
            Minecraft.getInstance().getSoundHandler().stop(currentMusic);
            currentMusic = null;
            timeUntilNextMusic = 0;
        }
        return true;
    }

    public static boolean handlePlaying(BackgroundMusicSelector music) {
        return currentMusic != null && music.func_232661_a_().getName().equals(currentMusic.getSoundLocation());
    }

    public static void forceStop() {
        Minecraft.getInstance().getSoundHandler().stop(currentMusic);
        currentMusic = null;
        timeUntilNextMusic = 3600;
    }

    public static MusicCondition getMusicCondition() {
        MusicCondition condition = null;

        // select an available condition from the pool of conditions
        for (MusicCondition c : musicConditions) {
            if (c.handle()) {
                condition = c;
                break;
            }
        }

        // if none available, just play a default background track
        if (condition == null)
            condition = new MusicCondition(Minecraft.getInstance().func_238178_U_());

        return condition;
    }
}
