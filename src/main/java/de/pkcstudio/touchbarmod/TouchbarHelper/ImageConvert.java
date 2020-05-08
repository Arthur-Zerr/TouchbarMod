package de.pkcstudio.touchbarmod.TouchbarHelper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.extensions.IForgeBakedModel;

public class ImageConvert {
    
    public static byte[] GetTexture(ItemStack item) {
		IForgeBakedModel itemModel = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(item);

		TextureAtlasSprite sprite = itemModel.getParticleTexture(null);
		TextureManager textureManager = Minecraft.getInstance().getTextureManager();
		if (item.getItem() == Items.AIR)
			return null;

		if(Block.getBlockFromItem(item.getItem()) != null)
			textureManager.bindTexture(new ResourceLocation("textures/block/" + item.getItem().getRegistryName().getPath() + ".png"));
		else
			textureManager.bindTexture(new
		ResourceLocation(item.getItem().getRegistryName().getPath()));

		//textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		//textureManager.func_229267_b_(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
		
		int width = sprite.getWidth();
		int height = sprite.getHeight();
		int size = width * height;

		BufferedImage bufferedimage = new BufferedImage(width, height, 2);
		int[] data = new int[size];

		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, data);
		// buffer.get(data);
		bufferedimage.setRGB(0, 0, width, height, data, 0, width);

		return getImgBytes(bufferedimage);
	}

	public static byte[] getImgBytes(BufferedImage image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "PNG", baos);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return baos.toByteArray();
	}
}