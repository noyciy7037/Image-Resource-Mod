package com.github.yuitosaito.resourcemod.network;

import com.github.yuitosaito.resourcemod.ResourceMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ResourceMod.MOD_ID);

    public static void init() {
        INSTANCE.registerMessage(MessageImageBlockHandler.class, MessageImageBlock.class, 0, Side.SERVER);
    }
}