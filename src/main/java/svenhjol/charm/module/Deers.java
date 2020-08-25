package svenhjol.charm.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import svenhjol.charm.entity.DeerEntity;
import svenhjol.charm.entity.DeerEntity;

public class Deers {

    ENTITY = EntityType.Builder.
    <DeerEntity>create(DeerEntity::new, EntityClassification.MISC)
            .setTrackingRange(80)
            .setUpdateInterval(10)
            .setShouldReceiveVelocityUpdates(false)
            .size(2.0F, 2.0F)
            .build(ID.getPath());

}
