package de.pkcstudio.touchbarmod.TouchbarHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.extensions.IForgeBakedModel;

public class ImageConvert{
	
    private static final Logger LOGGER = LogManager.getLogger();

    public static byte[] GetTextureOld(ItemStack item) {
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

	public static byte[] GetTextureOldOld(ItemStack item){
		if(item.getDisplayName().getString().equals("Air")) return null;
		RenderSystem.pushMatrix();

		IForgeBakedModel itemModel = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(item);

		TextureAtlasSprite sprite = itemModel.getParticleTexture(null);
	
		AtlasTexture texture = sprite.func_229241_m_();

		try {
			texture.loadTexture(Minecraft.getInstance().getResourceManager());
			
		} catch (Exception e) {
			LOGGER.catching(e);
			RenderSystem.popMatrix();
			return null;
		}

		int width = sprite.getWidth();
		int height = sprite.getHeight();
		int size = width * height;

		BufferedImage bufferedimage = new BufferedImage(width, height, 2);
		IntBuffer buffer = BufferUtils.createIntBuffer(16*16);
		int[] data = new int[size];

		RenderSystem.enableTexture();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlTextureId());
		LOGGER.info("Some shit Info: " + texture.getGlTextureId());

		GL11.glReadPixels((int)sprite.getMinU(), (int)sprite.getMinV(), sprite.getWidth(), sprite.getWidth(), GL11.GL_RGBA, GL11.GL_INT, buffer);
		//GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_INT, buffer);

		buffer.get(data);
		bufferedimage.setRGB(0, 0, width, height, data, 0, width);

		RenderSystem.disableTexture();
		RenderSystem.popMatrix();

		return getImgBytes(bufferedimage);
	}

	public static byte[] GetTexture(ItemStack item){
		IForgeBakedModel itemModel = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(item);
		TextureAtlasSprite sprite = itemModel.getParticleTexture(null);

		int width = sprite.getWidth();
		int height = sprite.getHeight();

		Framebuffer spriteFrame = new Framebuffer(width, height, true, true);
		spriteFrame.setFramebufferColor(0, 0, 0, 1);

		RenderSystem.pushMatrix();
		spriteFrame.bindFramebuffer(true);
		TouchbarRenderSystem.saveGLState();
		RenderSystem.clear(16640, true);
		RenderSystem.clearColor(0, 0, 0, 1);

		RenderSystem.enableTexture();
		RenderSystem.scalef(5,1, 1);

		Minecraft.getInstance().getItemRenderer().renderItemOverlays(Minecraft.getInstance().fontRenderer, item, 0, 0);
		TouchbarRenderSystem.renderStack(0, 0, item);

		TouchbarRenderSystem.loadGLState();
		RenderSystem.popMatrix();

		spriteFrame.bindFramebufferTexture();
		IntBuffer pixels = BufferUtils.createIntBuffer(width * height);

		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixels);

		int[] data = new int[width * height];
		pixels.get(data);

		BufferedImage bufferedimage = new BufferedImage(width, height, 2);
		bufferedimage.setRGB(0, 0, width, height, data, 0, width);

		spriteFrame.unbindFramebuffer();
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