package alexndr.SimpleOres.core.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ProxyClient extends ProxyCommon
{
	/**
	 * Adds the armor 'names' to the RenderingRegistry. This is so that the armor texture will show up properly when you wear it. The names are given by the main mod class.
	 * ie. copper, mythril, adamantium.
	 * 
	 * This is in ProxyClient because only the client will render the armor texture, thus it only needs to be client-side.
	 */
    public int addArmor(String armor)
    {
        return RenderingRegistry.addNewArmourRendererPrefix(armor);
    }
    
    /**
     * Some very messy code to determine the zooming of the bows when you draw them back. Most of this is copy paste so I won't attempt to explain it. 
     * It is fairly straight forward if you actually look at it though.
     */
    public float fovModifierHand = 0F;  
    protected float speedOnGround = 0.1F;
    
    @Override
    public void onBowUse(ItemStack stack, EntityPlayer player) 
    {	    	
    	float f = 1.0F;

        if (player.capabilities.isFlying)
        {
            f *= 1.1F;
        }
        
        float speedOnGround = 0.1F;
        int i = player.getItemInUseDuration();
        float f1 = (float)i / 20.0F;

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }
        
        else
        {
            f1 *= f1;
        }

        f *= 1.0F - f1 * TickHelper.zoomAmount;     
        fovModifierHand = fovModifierHand > 0.001F ? fovModifierHand : (Float)ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, Minecraft.getMinecraft().entityRenderer, "fovModifierHand", "field_78507_R");
        fovModifierHand += (f - fovModifierHand) * 0.5F;

        if (fovModifierHand > 1.5F)
        {
            fovModifierHand = 1.5F;
        }

        if (fovModifierHand < 0.1F)
        {
            fovModifierHand = 0.1F;
        }
        
        ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, Minecraft.getMinecraft().entityRenderer, fovModifierHand, "fovModifierHand", "field_78507_R");
    }
    
    @Override
    public void resetSavedFOV() 
    {
     this.fovModifierHand = 0F;
    }
    
	 @Override
	 public void registerClientTickHandler()
	 {
		 TickRegistry.registerTickHandler(new TickHelper(), Side.CLIENT);
	 }
	 
	 public void addModderCapes()
	 {

		 String capeURL = "https://dl.dropboxusercontent.com/u/66466201/SimpleOres/SimpleOres%20Cape.png";
		 String[] devs = {"AleXndrTheGr8st"};

		 ThreadDownloadImageData image = new ThreadDownloadImageData(capeURL, null, null);

		 for (String username : devs) 
		 {
			 Minecraft.getMinecraft().renderEngine.loadTexture(new ResourceLocation("cloaks/" + username), (TextureObject) image);
		 }
	}
}
