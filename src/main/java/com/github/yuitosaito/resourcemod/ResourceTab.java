package com.github.yuitosaito.resourcemod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class ResourceTab extends CreativeTabs {
    public ResourceTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ResourceMod.blockImage, 1);
    }

    @Override
    public Item getTabIconItem() {
        return null;
    }
}