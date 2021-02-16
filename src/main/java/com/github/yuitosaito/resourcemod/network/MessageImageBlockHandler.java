package com.github.yuitosaito.resourcemod.network;

import com.github.yuitosaito.resourcemod.blocks.BlockImage;
import com.github.yuitosaito.resourcemod.blocks.TileEntityBlockImage;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class MessageImageBlockHandler implements IMessageHandler<MessageImageBlock, IMessage> {

    @Override
    public IMessage onMessage(MessageImageBlock message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            if (message.type == 0) {
                TileEntityBlockImage te = (TileEntityBlockImage) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);
                te.url = message.url;
                te.mode = message.mode;
                ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
            } else if (message.type == 1) {
                BlockImage block = (BlockImage) ctx.getServerHandler().playerEntity.worldObj.getBlock(message.x, message.y, message.z);
                block.setSize(ctx.getServerHandler().playerEntity.worldObj, message.x, message.y, message.z, message.mode, Integer.parseInt(message.url));
                ctx.getServerHandler().playerEntity.worldObj.markBlockForUpdate(message.x, message.y, message.z);
            }
        }
        return null;
    }
}