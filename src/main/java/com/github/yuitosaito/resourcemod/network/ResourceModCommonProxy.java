package com.github.yuitosaito.resourcemod.network;

import com.github.yuitosaito.resourcemod.blocks.TileEntityBlockImage;
import cpw.mods.fml.common.registry.GameRegistry;

public class ResourceModCommonProxy {

    public void registerTileEntity() {
        GameRegistry.registerTileEntity(TileEntityBlockImage.class, "blockSoundBlockTile");
    }
}