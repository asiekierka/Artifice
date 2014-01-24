package shukaro.artifice.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import shukaro.artifice.ArtificeCore;
import shukaro.artifice.render.connectedtexture.ConnectedTextures;
import shukaro.artifice.util.BlockCoord;

import java.util.Locale;

public class TextureHandler
{
    public static void updateTexture(BlockCoord c)
    {
        World world = Minecraft.getMinecraft().theWorld;

        if (world != null && c.getBlock(world) != null && !c.getBlock(world).isAirBlock(world, c.x, c.y, c.z))
        {
            Block block = c.getBlock(world);
            int meta = c.getMeta(world);
            boolean updated = false;
            int[] indices = new int[6];

            for (int i = 0; i < 6; i++)
            {
                ConnectedTextures t = getConnectedTexture(block.getIcon(i, meta));
                if (t != null)
                {
                    if (ArtificeCore.textureCache.get(c) == null)
                        updated = true;
                    else if (ArtificeCore.textureCache.get(c)[i] != t.renderer.getTextureIndex(world, c.x, c.y, c.z, i))
                        updated = true;
                    indices[i] = t.renderer.getTextureIndex(world, c.x, c.y, c.z, i);
                }
            }

            if (updated)
            {
                ArtificeCore.textureCache.put(c, indices);
                world.markBlockForRenderUpdate(c.x, c.y, c.z);
            }
        }
    }

    public static ConnectedTextures getConnectedTexture(Icon icon)
    {
        String s = icon.getIconName();
        for (ConnectedTextures t : ConnectedTextures.values())
        {
            if (s.startsWith(ArtificeCore.modID.toLowerCase(Locale.ENGLISH) + ":" + t.name.replace('_', '/')))
            {
                return t;
            }
        }
        return null;
    }
}