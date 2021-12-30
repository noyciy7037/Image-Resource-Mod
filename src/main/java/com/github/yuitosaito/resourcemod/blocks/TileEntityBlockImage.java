package com.github.yuitosaito.resourcemod.blocks;

import com.github.yuitosaito.resourcemod.image.ImageLibrary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;

import static java.lang.Thread.State.TERMINATED;

public class TileEntityBlockImage extends TileEntity {
    public int heightSize = 1;
    public int widthSize = 1;
    public int heightSizeBuffer = 1;
    public int widthSizeBuffer = 1;
    public boolean isMainBlock = false;
    public int mainX = 0;
    public int mainY = 0;
    public int mainZ = 0;
    public String url = "";
    public String urlBuffer = "NONE";
    public int mode = 0;
    public int modeBuffer = -1;
    @SideOnly(Side.CLIENT)
    public BufferedImage image;
    @SideOnly(Side.CLIENT)
    public BufferedImage[] images;
    @SideOnly(Side.CLIENT)
    public ResourceLocation texture_dy;
    @SideOnly(Side.CLIENT)
    public int texture_glID;
    @SideOnly(Side.CLIENT)
    public ResourceLocation[] texture_dys;
    @SideOnly(Side.CLIENT)
    public int[] texture_glIDs;
    @SideOnly(Side.CLIENT)
    public int[] delays;
    @SideOnly(Side.CLIENT)
    public int delay;
    @SideOnly(Side.CLIENT)
    public int frame;
    private GetImage gi;

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.heightSize = nbt.getInteger("heightSize");
        this.widthSize = nbt.getInteger("widthSize");
        this.isMainBlock = nbt.getBoolean("isMainBlock");
        this.mainX = nbt.getInteger("mainX");
        this.mainY = nbt.getInteger("mainY");
        this.mainZ = nbt.getInteger("mainZ");
        this.url = nbt.getString("url");
        this.mode = nbt.getInteger("mode");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateEntity() {
        if (this.isMainBlock && worldObj.isRemote && (!url.equals(urlBuffer) || mode != modeBuffer || heightSizeBuffer != heightSize || widthSizeBuffer != widthSize)) {
            frame = 0;
            updateImage();
        }
        if (worldObj.isRemote && gi != null) {
            if (gi.getState().compareTo(TERMINATED) == 0) {
                frame = 0;
                if (texture_dy != null) {
                    GL11.glDeleteTextures(texture_glID);
                    texture_dy = null;
                    texture_glID = -1;
                }
                if (texture_dys != null) {
                    for (ResourceLocation textureDy : texture_dys) GL11.glDeleteTextures(textureDy.hashCode());
                    texture_dys = null;
                    texture_glIDs = null;
                }
                if (image != null) {
                    DynamicTexture dynamicTexture = new DynamicTexture(image);
                    texture_dy = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("image", dynamicTexture);
                    texture_glID = dynamicTexture.getGlTextureId();
                    System.out.println(texture_dy.getResourceDomain());
                } else if (images != null) {
                    texture_dys = new ResourceLocation[images.length];
                    texture_glIDs = new int[images.length];
                    for (int i = 0; i < images.length; ++i) {
                        DynamicTexture dynamicTexture = new DynamicTexture(images[i]);
                        texture_dys[i] = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("image", dynamicTexture);
                        texture_glIDs[i] = dynamicTexture.getGlTextureId();
                    }
                }
                image = null;
                images = null;
                gi = null;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateImage() {
        gi = new GetImage(this, url, mode);
        gi.start();
        urlBuffer = url;
        modeBuffer = mode;
        widthSizeBuffer = widthSize;
        heightSizeBuffer = heightSize;
    }

    @Override
    public void onChunkUnload() {
        if (worldObj.isRemote) {
            if (texture_dy != null) {
                GL11.glDeleteTextures(texture_glID);
                texture_dy = null;
                texture_glID = -1;
            }
            if (texture_dys != null) {
                for (int textureGLID : texture_glIDs) GL11.glDeleteTextures(textureGLID);
                texture_dys = null;
                texture_glIDs = null;
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("heightSize", this.heightSize);
        nbt.setInteger("widthSize", this.widthSize);
        nbt.setBoolean("isMainBlock", this.isMainBlock);
        nbt.setInteger("mainX", this.mainX);
        nbt.setInteger("mainY", this.mainY);
        nbt.setInteger("mainZ", this.mainZ);
        nbt.setString("url", this.url);
        nbt.setInteger("mode", this.mode);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    public boolean getIsMainBlock() {
        return this.isMainBlock;
    }

    public void setIsMainBlock(boolean isMainBlock) {
        this.isMainBlock = isMainBlock;
    }

    public int getMetadata() {
        return this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        if (!isMainBlock)
            return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1);
        float[] bb = BlockImage.getDirection(getMetadata(), this.heightSize, this.widthSize);
        return AxisAlignedBB.getBoundingBox(this.xCoord + bb[0], this.yCoord + bb[1], this.zCoord + bb[2], this.xCoord + bb[3], this.yCoord + bb[4], this.zCoord + bb[5]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        if (!isMainBlock)
            return 1;
        return Double.POSITIVE_INFINITY;
    }

    public static class GetImage extends Thread {
        TileEntityBlockImage te;
        String url;
        int mode;

        public static final int RESIZE_MODE_DO_NOT_RESIZE = 0;
        public static final int RESIZE_MODE_WIDTH_PRIORITY = 1;
        public static final int RESIZE_MODE_HEIGHT_PRIORITY = 2;

        public GetImage(TileEntityBlockImage te, String url, int mode) {
            this.te = te;
            this.url = url;
            this.mode = mode;
        }

        @Override
        public void run() {
            try {
                BufferedImage image = null;
                BufferedImage[] images = null;
                int[] delays = null;

                ImageLibrary.ImageDataBase imageDataBase = ImageLibrary.bufferedImageMap.get(url);
                if (imageDataBase == null) {
                    ImageLibrary.GetOriginalImage thread = ImageLibrary.getOrStartOriginalImageGetter(url);
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    imageDataBase = ImageLibrary.bufferedImageMap.get(url);
                }

                if (imageDataBase instanceof ImageLibrary.ImageDataSingle) {
                    image = ((ImageLibrary.ImageDataSingle) imageDataBase).image;
                } else if (imageDataBase instanceof ImageLibrary.ImageDataMulti) {
                    ImageLibrary.ImageDataMulti imageDataMulti = ((ImageLibrary.ImageDataMulti) imageDataBase);
                    images = imageDataMulti.images;
                    delays = imageDataMulti.delays;
                }

                if (image != null) {
                    te.images = null;
                    if (mode == RESIZE_MODE_HEIGHT_PRIORITY || mode == RESIZE_MODE_WIDTH_PRIORITY) {
                        int width = mode == RESIZE_MODE_HEIGHT_PRIORITY ? image.getHeight() * te.widthSize / te.heightSize : image.getWidth();
                        int height = mode == RESIZE_MODE_HEIGHT_PRIORITY ? image.getHeight() : image.getWidth() * te.heightSize / te.widthSize;
                        te.image = RenderTileEntityBlockImage.resizeImage(image, width, height);
                    } else {
                        te.image = RenderTileEntityBlockImage.resizeImage(image, image.getWidth(), image.getHeight());
                    }
                } else if (images != null && delays != null) {
                    for (int i = 0; i < images.length; ++i) {
                        BufferedImage ib = images[i];
                        if (mode == RESIZE_MODE_HEIGHT_PRIORITY || mode == RESIZE_MODE_WIDTH_PRIORITY) {
                            int width = mode == RESIZE_MODE_HEIGHT_PRIORITY ? ib.getHeight() * te.widthSize / te.heightSize : ib.getWidth();
                            int height = mode == RESIZE_MODE_HEIGHT_PRIORITY ? ib.getHeight() : ib.getWidth() * te.heightSize / te.widthSize;
                            images[i] = RenderTileEntityBlockImage.resizeImage(ib, width, height);
                        } else {
                            images[i] = RenderTileEntityBlockImage.resizeImage(ib, ib.getWidth(), ib.getHeight());
                        }
                    }
                    te.delays = delays;
                    te.images = images;
                }
            } catch (MalformedURLException | NullPointerException e) {
                e.printStackTrace();
                te.image = null;
                te.images = null;
                te.texture_dys = null;
                te.texture_glIDs = null;
                te.texture_dy = null;
                te.texture_glID = -1;
                te.delays = null;
                te.delay = 0;
            }
        }
    }
}
