package com.github.yuitosaito.resourcemod.network;

import com.github.yuitosaito.resourcemod.blocks.RenderTileEntityBlockImage;
import com.github.yuitosaito.resourcemod.blocks.TileEntityBlockImage;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ResourceModClientProxy extends ResourceModCommonProxy {

    public static int blockImageRenderID;

    @Override
    public void registerTileEntity() {
        GameRegistry.registerTileEntity(TileEntityBlockImage.class, "blockSoundBlockTile");
        blockImageRenderID = RenderingRegistry.getNextAvailableRenderId();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlockImage.class, new RenderTileEntityBlockImage());
    }
}