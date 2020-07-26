package svenhjol.charm.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.iface.IStorageMaterial;

public class CrateBlock extends MesonBlock {
    protected IStorageMaterial type;

    public CrateBlock(MesonModule module, IStorageMaterial type) {
        super(module, type.toString() + "_crate", Block.Properties
            .create(Material.WOOD)
            .sound(SoundType.WOOD)
            .hardnessAndResistance(1.5F));

        this.type = type;
    }

}
