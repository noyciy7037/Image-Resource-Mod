package com.github.yuitosaito.resourcemod.blocks;

import com.github.yuitosaito.resourcemod.ResourceMod;
import com.github.yuitosaito.resourcemod.network.ResourceModClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockImage extends BlockContainer {

    public BlockImage() {
        super(Material.iron);
        this.setStepSound(Block.soundTypeMetal);
        this.setBlockTextureName(ResourceMod.MOD_ID + ":image");
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        player.openGui(ResourceMod.INSTANCE, ResourceMod.BLOCK_IMAGE_GUI_ID, world, x, y, z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int a) {
        return new TileEntityBlockImage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return ResourceModClientProxy.blockImageRenderID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        float[] fa = getBlockBounds(meta);
        return AxisAlignedBB.getBoundingBox(x + fa[0], y + fa[1], z + fa[2], x + fa[3], y + fa[4], z + fa[5]);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess block, int x, int y, int z) {
        float[] f = getBlockBounds(block.getBlockMetadata(x, y, z));
        this.setBlockBounds(f[0], f[1], f[2], f[3], f[4], f[5]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        TileEntityBlockImage te = (TileEntityBlockImage) world.getTileEntity(x, y, z);
        float[] fa = getDirection(meta, te.heightSize, te.widthSize);
        if (te.isMainBlock)
            return AxisAlignedBB.getBoundingBox(x + fa[0], y + fa[1], z + fa[2], x + fa[3], y + fa[4], z + fa[5]);
        else
            return AxisAlignedBB.getBoundingBox(te.mainX + fa[0], te.mainY + fa[1], te.mainZ + fa[2], te.mainX + fa[3], te.mainY + fa[4], te.mainZ + fa[5]);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        int l = MathHelper.floor_double((double) (entityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
        TileEntityBlockImage te = (TileEntityBlockImage) world.getTileEntity(x, y, z);
        te.setIsMainBlock(true);
        world.markBlockForUpdate(x, y, z);
    }

    public void setSize(World world, int x, int y, int z, int sizeHeight, int sizeWidth) {
        TileEntityBlockImage te = (TileEntityBlockImage) world.getTileEntity(x, y, z);
        int beforeSizeHeight = te.heightSize;
        int beforeSizeWidth = te.widthSize;
        te.heightSize = 1;
        te.widthSize = 1;
        int meta = world.getBlockMetadata(x, y, z);
        int[] direction = getBlockDirection(meta);
        for (int w = 0; w < beforeSizeWidth; ++w) {
            for (int h = 0; h < beforeSizeHeight; ++h) {
                int x1 = x + direction[0] * w;
                int y1 = y + direction[1] * h;
                int z1 = z + direction[2] * w;
                if (x1 != x || y1 != y || z1 != z) {
                    world.setBlock(x1, y1, z1, Blocks.air);
                }
            }
        }
        for (int w = 0; w < sizeWidth; ++w) {
            for (int h = 0; h < sizeHeight; ++h) {
                int x1 = x + direction[0] * w;
                int y1 = y + direction[1] * h;
                int z1 = z + direction[2] * w;
                if (!this.canPlaceBlockAt(world, x1, y1, z1)) {
                    if (x1 != x || y1 != y || z1 != z) {
                        return;
                    }
                }
            }
        }
        te.widthSize = sizeWidth;
        te.heightSize = sizeHeight;
        for (int w = 0; w < te.widthSize; ++w) {
            for (int h = 0; h < te.heightSize; ++h) {
                int x1 = x + direction[0] * w;
                int y1 = y + direction[1] * h;
                int z1 = z + direction[2] * w;
                if (x1 == x && y1 == y && z1 == z) continue;
                world.setBlock(x1, y1, z1, this, meta, 2);
                TileEntityBlockImage te1 = (TileEntityBlockImage) world.getTileEntity(x1, y1, z1);
                te1.widthSize = te.widthSize;
                te1.heightSize = te.heightSize;
                te1.isMainBlock = false;
                te1.mainX = x;
                te1.mainY = y;
                te1.mainZ = z;
            }
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        TileEntityBlockImage te = (TileEntityBlockImage) world.getTileEntity(x, y, z);
        int mainX, mainY, mainZ;
        if (!te.isMainBlock) {
            mainX = te.mainX;
            mainY = te.mainY;
            mainZ = te.mainZ;
            TileEntity mainTeTe = world.getTileEntity(mainX, mainY, mainZ);
            if (mainTeTe instanceof TileEntityBlockImage) {
                TileEntityBlockImage mainTe = (TileEntityBlockImage) mainTeTe;
                if (mainTe.heightSize == te.heightSize && mainTe.widthSize == te.widthSize)
                    world.func_147480_a(mainX, mainY, mainZ, false);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        TileEntityBlockImage te = (TileEntityBlockImage) world.getTileEntity(x, y, z);
        if (!te.isMainBlock) {
            if (!world.getBlock(te.mainX, te.mainY, te.mainZ).equals(this)) world.func_147480_a(x, y, z, false);
        }
    }

    public static float[] getDirection(int meta, int sizeHeight, int sizeWidth) {
        float[] fa;
        switch (meta) {
            case 1:
                fa = new float[]{0.9375F, 0.0F, 0.0F, 1.0F, 1.0F * sizeHeight, 1.0F * sizeWidth};
                break;
            case 2:
                fa = new float[]{-1.0F * (sizeWidth - 1), 0.0F, 0.9375F, 1.0F, 1.0F * sizeHeight, 1.0F};
                break;
            case 3:
                fa = new float[]{0.0F, 0.0F, -1.0F * (sizeWidth - 1), 0.0625F, 1.0F * sizeHeight, 1.0F};
                break;
            default:
                fa = new float[]{0.0F, 0.0F, 0.0F, 1.0F * sizeWidth, 1.0F * sizeHeight, 0.0625F};
                break;
        }
        return fa;
    }

    public static float[] getBlockBounds(int meta) {
        float[] fa;
        switch (meta) {
            case 1:
                fa = new float[]{0.9375F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F};
                break;
            case 2:
                fa = new float[]{0.0F, 0.0F, 0.9375F, 1.0F, 1.0F, 1.0F};
                break;
            case 3:
                fa = new float[]{0.0F, 0.0F, 0.0F, 0.0625F, 1.0F, 1.0F};
                break;
            default:
                fa = new float[]{0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F};
                break;
        }
        return fa;
    }

    public static int[] getBlockDirection(int meta) {
        int[] fa;
        switch (meta) {
            case 1:
                fa = new int[]{0, 1, 1};
                break;
            case 2:
                fa = new int[]{-1, 1, 0};
                break;
            case 3:
                fa = new int[]{0, 1, -1};
                break;
            default:
                fa = new int[]{1, 1, 0};
                break;
        }
        return fa;
    }
}