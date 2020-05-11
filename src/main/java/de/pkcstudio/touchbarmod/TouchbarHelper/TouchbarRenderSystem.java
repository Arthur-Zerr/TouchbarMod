package de.pkcstudio.touchbarmod.TouchbarHelper;

import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.extensions.IForgeBakedModel;

public class TouchbarRenderSystem {
    protected static boolean hasLight;
    protected static boolean hasDepthTest;
    protected static boolean hasLight0;
    protected static boolean hasLight1;
    protected static boolean hasRescaleNormal;
    protected static boolean hasColorMaterial;
    protected static boolean depthMask;
    protected static int depthFunc;

    private static final Minecraft CLIENT = Minecraft.getInstance();

    public static void saveGLState() {
        hasLight = GL11.glGetBoolean(GL11.GL_LIGHTING);
        hasLight0 = GL11.glGetBoolean(GL11.GL_LIGHT0);
        hasLight1 = GL11.glGetBoolean(GL11.GL_LIGHT1);
        hasDepthTest = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        hasRescaleNormal = GL11.glGetBoolean(GL12.GL_RESCALE_NORMAL);
        hasColorMaterial = GL11.glGetBoolean(GL11.GL_COLOR_MATERIAL);
        depthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
        depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT); // Leave me alone :(
    }

    public static void loadGLState() {
        RenderSystem.depthMask(depthMask);
        RenderSystem.depthFunc(depthFunc);
        if (hasLight)
            RenderSystem.enableLighting();
        else
            RenderSystem.disableLighting();

        /*
        if (hasLight0)
            //GlStateManager.enableLight(0);
        else
            //GlStateManager.disableLight(0);

        if (hasLight1)
            GlStateManager.enableLight(1);
        else
            //GlStateManager.disableLight(1);
        */

        if (hasDepthTest)
            RenderSystem.enableDepthTest();
        else
            RenderSystem.disableDepthTest();
        if (hasRescaleNormal)
            RenderSystem.enableRescaleNormal();
        else
            RenderSystem.disableRescaleNormal();
        if (hasColorMaterial)
            RenderSystem.enableColorMaterial();
        else
            RenderSystem.disableColorMaterial();

        RenderSystem.popAttributes();
    }
    public static void renderStack(int x, int y, ItemStack stack) {
        enable3DRender();
        try {
            CLIENT.getItemRenderer().renderItemIntoGUI(stack, x, y);
            ItemStack overlayRender = stack.copy();
            overlayRender.setCount(1);
            CLIENT.getItemRenderer().renderItemOverlayIntoGUI(CLIENT.fontRenderer, overlayRender, x, y, "");
        } catch (Exception e) {
            String stackStr = stack != null ? stack.toString() : "NullStack";
        }
        enable2DRender();
    }

    public static void enable3DRender() {
        RenderSystem.enableLighting();
        RenderSystem.enableDepthTest();
    }

    public static void enable2DRender() {
        RenderSystem.disableLighting();
        RenderSystem.disableDepthTest();
    }

}