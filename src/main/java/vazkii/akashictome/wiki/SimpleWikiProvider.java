package vazkii.akashictome.wiki;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.apache.commons.lang3.text.WordUtils;

public class SimpleWikiProvider implements IWikiProvider {

    final String name, urlBase, replacement;
    final boolean lowercase;

    public SimpleWikiProvider(String name, String urlBase) {
        this(name, urlBase, "%20");
    }

    public SimpleWikiProvider(String name, String urlBase, boolean lowercase) {
        this(name, urlBase, "%20", lowercase);
    }

    public SimpleWikiProvider(String name, String urlBase, String replacement) {
        this.name = name;
        this.urlBase = urlBase;
        this.replacement = replacement;
        lowercase = false;
    }

    public SimpleWikiProvider(String name, String urlBase, String replacement, boolean lowercase) {
        this.name = name;
        this.urlBase = urlBase;
        this.replacement = replacement;
        this.lowercase = lowercase;
    }

    @Override
    public String getBlockName(World world, MovingObjectPosition pos) {
        int x = pos.blockX;
        int y = pos.blockY;
        int z = pos.blockZ;

        Block block = world.getBlock(x, y, z);
        if (block == null) return null;

        ItemStack stack = block.getPickBlock(pos, world, x, y, z);

        if (stack == null || stack.getItem() == null) stack = new ItemStack(block, 1, world.getBlockMetadata(x, y, z));

        if (stack.getItem() == null) return null;

        String name = stack.getDisplayName();
        if (name == null || name.isEmpty()) return null;

        return name;
    }

    @Override
    public String getWikiURL(World world, MovingObjectPosition pos) {
        String name = getBlockName(world, pos);
        if (name == null) return null;

        if (lowercase) {
            return String.format(urlBase, name.toLowerCase().replaceAll(" ", replacement));
        } else {
            return String.format(urlBase, WordUtils.capitalizeFully(name).replaceAll(" ", replacement));
        }
    }

    @Override
    public String getWikiName(World world, MovingObjectPosition pos) {
        return name;
    }

}
