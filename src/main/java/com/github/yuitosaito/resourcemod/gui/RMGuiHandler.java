package com.github.yuitosaito.resourcemod.gui;

import com.github.yuitosaito.resourcemod.ResourceMod;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class RMGuiHandler implements IGuiHandler {
    /*サーバー側の処理*/
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ResourceMod.BLOCK_IMAGE_GUI_ID) {
            return new BlockImageContainer(x, y, z);
        }
        return null;
    }

    /*クライアント側の処理*/
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ResourceMod.BLOCK_IMAGE_GUI_ID) {
            return new BlockImageGuiContainer(world.getTileEntity(x, y, z), world);
        }
        return null;
    }
}