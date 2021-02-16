package com.github.yuitosaito.resourcemod;

import com.github.yuitosaito.resourcemod.blocks.BlockImage;
import com.github.yuitosaito.resourcemod.gui.RMGuiHandler;
import com.github.yuitosaito.resourcemod.network.PacketHandler;
import com.github.yuitosaito.resourcemod.network.ResourceModCommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = ResourceMod.MOD_ID, name = "ResourceMod", version = "1.0.1"/*Constants.version*/)
public class ResourceMod {
    public static final String MOD_ID = "resourcemod";
    @Mod.Instance("resourcemod")
    public static ResourceMod INSTANCE;

    /*private static final String[] libs = {"batik-anim-1.13.jar", "batik-awt-util-1.13.jar", "batik-bridge-1.13.jar",
            "batik-constants-1.13.jar", "batik-css-1.13.jar", "batik-dom-1.13.jar", "batik-ext-1.13.jar",
            "batik-gvt-1.13.jar", "batik-i18n-1.13.jar", "batik-parser-1.13.jar", "batik-script-1.13.jar"
            , "batik-shared-resources-1.13.jar", "batik-svg-dom-1.13.jar", "batik-svggen-1.13.jar",
            "batik-transcoder-1.13.jar", "batik-util-1.13.jar", "batik-xml-1.13.jar", "commons-io-1.3.1.jar",
            "commons-logging-1.0.4.jar", "serializer-2.7.2.jar", "xalan-2.7.2.jar", "xml-apis-1.4.01.jar",
            "xml-apis-ext-1.3.04.jar", "xmlgraphics-commons-2.4.jar"};*/

    public static CreativeTabs tabResource = new ResourceTab("ResourceTab");

    public static final int BLOCK_IMAGE_GUI_ID = 0;

    @SidedProxy(clientSide = "com.github.yuitosaito.resourcemod.network.ResourceModClientProxy", serverSide = "com.github.yuitosaito.resourcemod.network.ResourceModCommonProxy")
    public static ResourceModCommonProxy proxy;

    public static Block blockImage;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerTileEntity();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new RMGuiHandler());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        blockImage = new BlockImage().setBlockName("resourceBlockImage").setCreativeTab(tabResource);
        GameRegistry.registerBlock(blockImage, "resourceBlockImage");
        PacketHandler.init();
    }
}