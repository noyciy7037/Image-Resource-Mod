package com.github.yuitosaito.resourcemod.blocks;

import com.github.yuitosaito.resourcemod.ResourceMod;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;


public class RenderTileEntityBlockImage extends TileEntitySpecialRenderer {
    ResourceLocation texture;

    public RenderTileEntityBlockImage() {
        texture = new ResourceLocation(ResourceMod.MOD_ID, "textures/blocks/NoImage.png");
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double posX, double posY, double posZ, float timeSinceLastTick) {
        TileEntityBlockImage te2 = (TileEntityBlockImage) te;
        if (!te2.getIsMainBlock()) return;
        int sizeH = te2.heightSize;
        int sizeW = te2.widthSize;
        Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) posX, (float) posY, (float) posZ);
        switch (te2.getMetadata()) {
            case 0:
                GL11.glTranslatef(sizeW, 0, 0.0625F);
                break;
            case 1:
                GL11.glRotatef(270, 0, 1, 0);
                GL11.glTranslatef(sizeW, 0, -1 + 0.0625F);
                break;
            case 2:
                GL11.glRotatef(180, 0, 1, 0);
                GL11.glTranslatef(sizeW - 1, 0, -1 + 0.0625F);
                break;
            case 3:
                GL11.glRotatef(90, 0, 1, 0);
                GL11.glTranslatef(sizeW - 1, 0, 0.0625F);
                break;
        }
        GL11.glRotatef(180, 0, 1, 0);
        if (te2.texture_dy != null) {
            bindTexture(te2.texture_dy);
        } else if (te2.texture_dys != null) {
            if (te2.delay >= te2.delays[te2.frame]) {
                te2.delay = 0;
                if (te2.texture_dys.length - 1 > te2.frame) {
                    ++te2.frame;
                } else {
                    te2.frame = 0;
                }
            }
            bindTexture(te2.texture_dys[te2.frame]);
        } else
            bindTexture(texture);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.setBrightness(15728880);
        tessellator.addVertexWithUV(0, 0, 0, 1.0D, 1.0D);
        tessellator.addVertexWithUV(0, sizeH, 0, 1.0D, 0.0D);
        tessellator.addVertexWithUV(sizeW, sizeH, 0, 0.0D, 0.0D);
        tessellator.addVertexWithUV(sizeW, 0, 0, 0.0D, 1.0D);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glColor3f(0, 0, 0);
        GL11.glTranslatef((float) posX, (float) posY, (float) posZ);
        switch (te2.getMetadata()) {
            case 1:
                GL11.glRotatef(270, 0, 1, 0);
                GL11.glTranslatef(0, 0, -1);
                break;
            case 2:
                GL11.glRotatef(180, 0, 1, 0);
                GL11.glTranslatef(-1, 0, -1);
                break;
            case 3:
                GL11.glRotatef(90, 0, 1, 0);
                GL11.glTranslatef(-1, 0, 0);
                break;
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, GL11.GL_3D_COLOR_TEXTURE);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0, 0, 0, 1.0D, 1.0D);
        tessellator.addVertexWithUV(0, sizeH, 0, 1.0D, 0.0D);
        tessellator.addVertexWithUV(sizeW, sizeH, 0, 0.0D, 0.0D);
        tessellator.addVertexWithUV(sizeW, 0, 0, 0.0D, 1.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0, 0, 0, 1.0D, 1.0D);
        tessellator.addVertexWithUV(0, 0, 0.0625, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0, sizeH, 0.0625, 0.0D, 0.0D);
        tessellator.addVertexWithUV(0, sizeH, 0, 0.0D, 1.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0, sizeH, 0, 1.0D, 1.0D);
        tessellator.addVertexWithUV(0, sizeH, 0.0625, 1.0D, 0.0D);
        tessellator.addVertexWithUV(sizeW, sizeH, 0.0625, 0.0D, 0.0D);
        tessellator.addVertexWithUV(sizeW, sizeH, 0, 0.0D, 1.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(sizeW, 0, 0, 1.0D, 1.0D);
        tessellator.addVertexWithUV(sizeW, sizeH, 0, 1.0D, 0.0D);
        tessellator.addVertexWithUV(sizeW, sizeH, 0.0625, 0.0D, 0.0D);
        tessellator.addVertexWithUV(sizeW, 0, 0.0625, 0.0D, 1.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0, 0, 0, 1.0D, 1.0D);
        tessellator.addVertexWithUV(sizeW, 0, 0, 1.0D, 0.0D);
        tessellator.addVertexWithUV(sizeW, 0, 0.0625, 0.0D, 0.0D);
        tessellator.addVertexWithUV(0, 0, 0.0625, 0.0D, 1.0D);
        tessellator.draw();
        GL11.glPopMatrix();
        te2.delay += timeSinceLastTick * 50;
    }

    public static BufferedImage resizeImage(BufferedImage bufferedImage, int layoutWidth, int layoutHeight) {
        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();
        BufferedImage scaledBI = new BufferedImage(layoutWidth, layoutHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaledBI.createGraphics();
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, layoutWidth, layoutHeight);
        int x = (layoutWidth - imageWidth) / 2;
        int y = (layoutHeight - imageHeight) / 2;
        g.drawImage(bufferedImage, x, y, imageWidth, imageHeight, null);
        g.dispose();
        return scaledBI;
    }
}