package alchemyplusplus.items;

import alchemyplusplus.blocks.BlockRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class ItemExtractor extends APPItem {

    public ItemExtractor(int id, String iconName) {
        super(id, iconName);
        this.maxStackSize = 64;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world,
            int x, int y, int z, int par7, float par8, float par9, float par10) {
        if (world.isBlockSolidOnSide(x, y, z, ForgeDirection.UP)
                && world.isAirBlock(x, y + 1, z)) {
            world.setBlock(x, y + 1, z, BlockRegistry.appBlockExtractor.blockID);
            stack.stackSize--;
            return true;
        } else
            return false;

    }

}
