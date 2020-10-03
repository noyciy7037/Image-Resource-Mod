package com.github.yuitosaito.resourcemod.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageImageBlock implements IMessage {

    public int type;
    public int x, y, z;
    public String url;
    public int mode;

    @SuppressWarnings("unused")
    public MessageImageBlock() {
    }

    public MessageImageBlock(int type, int x, int y, int z, String url, int mode) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.url = url;
        this.mode = mode;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = buf.readInt();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.mode = buf.readInt();
        this.url = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.type);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.mode);
        ByteBufUtils.writeUTF8String(buf, this.url);
    }
}