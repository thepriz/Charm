package svenhjol.charm.building.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

public class SmoothGlowstoneBlock extends MesonBlock {
    public SmoothGlowstoneBlock(MesonModule module) {
        super(module, "smooth_glowstone", Block.Properties
            .create(Material.GLASS)
            .setLightLevel((p_235447_0_) -> 15)
            .sound(SoundType.GLASS)
            .hardnessAndResistance(0.3F)
        );
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.BUILDING_BLOCKS;
    }
}
