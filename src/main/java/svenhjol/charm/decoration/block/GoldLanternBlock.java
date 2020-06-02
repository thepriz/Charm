package svenhjol.charm.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import svenhjol.meson.MesonModule;

public class GoldLanternBlock extends BaseLanternBlock {
    public GoldLanternBlock(MesonModule module) {
        super(module, "gold_lantern", Block.Properties
            .create(Material.IRON)
            .hardnessAndResistance(3.5F)
            .sound(SoundType.LANTERN)
            .lightValue(15));
    }
}
