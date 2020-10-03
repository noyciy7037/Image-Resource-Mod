package com.github.yuitosaito.resourcemod.gui;

import com.github.yuitosaito.resourcemod.blocks.TileEntityBlockImage;
import com.github.yuitosaito.resourcemod.network.MessageImageBlock;
import com.github.yuitosaito.resourcemod.network.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class BlockImageGuiContainer extends GuiContainer {
    private TileEntityBlockImage te;
    private GuiTextField textURL;
    private GuiTextField textWidthSize;
    private GuiTextField textHeightSize;
    private int mode;

    public BlockImageGuiContainer(TileEntity te, World world) {
        super(new BlockImageContainer(te.xCoord, te.yCoord, te.zCoord));
        this.te = (TileEntityBlockImage) te;
        if (!this.te.isMainBlock)
            this.te = (TileEntityBlockImage) world.getTileEntity(this.te.mainX, this.te.mainY, this.te.mainZ);
        this.xSize = 350;
        this.ySize = 200;
        this.mode = this.te.mode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = width - xSize >> 1;
        int j = height - ySize >> 1;
        buttonList.add(new GuiButton(0, i + 230, j + 170, 100, 20, StatCollector.translateToLocal("gui.done")));
        buttonList.add(new GuiButton(1, i + 120, j + 170, 100, 20, StatCollector.translateToLocal("gui.cancel")));
        buttonList.add(new GuiButton(2, i + 230, j + 110, 100, 20, StatCollector.translateToLocal("resourcemod.gui.ImageBlock.mode." + mode)));

        this.textURL = new GuiTextField(this.fontRendererObj, i + 16, j + 35, 300, 20);
        this.textURL.setMaxStringLength(200);
        if (te.url != null)
            this.textURL.setText(te.url);
        this.textURL.setFocused(true);

        this.textWidthSize = new GuiTextField(this.fontRendererObj, i + 16, j + 75, 50, 20);
        this.textWidthSize.setMaxStringLength(2);
        this.textWidthSize.setText("" + te.widthSize);

        this.textHeightSize = new GuiTextField(this.fontRendererObj, i + 16, j + 115, 50, 20);
        this.textHeightSize.setMaxStringLength(2);
        this.textHeightSize.setText("" + te.heightSize);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (!(this.textURL.textboxKeyTyped(par1, par2) || this.textWidthSize.textboxKeyTyped(par1, par2) || this.textHeightSize.textboxKeyTyped(par1, par2))) {
            super.keyTyped(par1, par2);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.textURL.updateCursorCounter();
        this.textWidthSize.updateCursorCounter();
        this.textHeightSize.updateCursorCounter();
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        this.textURL.drawTextBox();
        this.textWidthSize.drawTextBox();
        this.textHeightSize.drawTextBox();
    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {
        super.mouseClicked(x, y, btn);
        this.textURL.mouseClicked(x, y, btn);
        this.textWidthSize.mouseClicked(x, y, btn);
        this.textHeightSize.mouseClicked(x, y, btn);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int xMouse, int yMouse) {
        int color = new Color(255, 255, 255).getRGB();
        this.fontRendererObj.drawString(StatCollector.translateToLocal("resourcemod.gui.ImageBlock"), 8, 5, color);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("resourcemod.gui.URL"), 15, 20, color);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("resourcemod.gui.WidthSize"), 15, 60, color);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("resourcemod.gui.HeightSize"), 15, 100, color);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int xMouse, int yMouse) {
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, GL11.GL_3D_COLOR_TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.enabled)
            if (guiButton.id == 0) {
                if (te.mode != mode || !te.url.equals(textURL.getText()))
                    PacketHandler.INSTANCE.sendToServer(new MessageImageBlock(0, te.xCoord, te.yCoord, te.zCoord, textURL.getText(), mode));
                if (isInteger(textWidthSize.getText()) && isInteger(textHeightSize.getText()))
                    if (Integer.parseInt(textWidthSize.getText()) != te.widthSize || Integer.parseInt(textHeightSize.getText()) != te.heightSize)
                        if (Integer.parseInt(textWidthSize.getText()) <= 20 || Integer.parseInt(textHeightSize.getText()) <= 20)
                            PacketHandler.INSTANCE.sendToServer(new MessageImageBlock(1, te.xCoord, te.yCoord, te.zCoord, textWidthSize.getText(), Integer.parseInt(textHeightSize.getText())));
                this.mc.thePlayer.closeScreen();
            } else if (guiButton.id == 1) {
                this.onGuiClosed();
                this.mc.thePlayer.closeScreen();
            } else if (guiButton.id == 2) {
                switch (mode) {
                    case TileEntityBlockImage.GetImage.RESIZE_MODE_WIDTH_PRIORITY:
                        mode = TileEntityBlockImage.GetImage.RESIZE_MODE_HEIGHT_PRIORITY;
                        break;
                    case TileEntityBlockImage.GetImage.RESIZE_MODE_HEIGHT_PRIORITY:
                        mode = TileEntityBlockImage.GetImage.RESIZE_MODE_DO_NOT_RESIZE;
                        break;
                    default:
                        mode = TileEntityBlockImage.GetImage.RESIZE_MODE_WIDTH_PRIORITY;
                        break;
                }
                guiButton.displayString = StatCollector.translateToLocal("resourcemod.gui.ImageBlock.mode." + mode);
            }
    }

    public static boolean isInteger(String str) {
        if (str == null
                || str.isEmpty()
                || str.equals("+")
                || str.equals("-")) {
            return false;
        }

        char first = str.charAt(0);
        int i = (first == '+' || first == '-') ? 1 : 0;
        int sign = (first == '-') ? -1 : 1;
        int len = str.length();
        long integer = 0;

        while (i < len) {
            char ch = str.charAt(i++);
            int digit = Character.digit(ch, 10);
            if (digit == -1) {
                return false;
            }

            integer = integer * 10 + sign * digit;
            if (integer < Integer.MIN_VALUE || Integer.MAX_VALUE < integer) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
}