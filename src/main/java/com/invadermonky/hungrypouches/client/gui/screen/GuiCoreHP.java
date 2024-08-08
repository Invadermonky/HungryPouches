package com.invadermonky.hungrypouches.client.gui.screen;

import com.invadermonky.hungrypouches.handlers.PouchHandler;
import com.invadermonky.hungrypouches.inventory.containers.ContainerCoreHP;
import com.invadermonky.hungrypouches.network.PacketHandlerHP;
import com.invadermonky.hungrypouches.util.SpriteResourceLocationHP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiCoreHP extends GuiContainer {
    protected ItemStack pouch;
    protected final SpriteResourceLocationHP texture;

    public GuiCoreHP(ContainerCoreHP inventorySlotsIn) {
        super(inventorySlotsIn);
        this.pouch = inventorySlotsIn.getPouch();
        this.texture = PouchHandler.getPouchGuiTexture(pouch);
        this.xSize = this.texture.getWidth();
        this.ySize = this.texture.getHeight();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        this.mc.renderEngine.bindTexture(this.texture);
        if(this.xSize <= 256 && this.ySize <= 256) {
            this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        } else {
            this.drawSizedTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize, 512.0F, 512.0F);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) guiLeft, (float) this.guiTop, 0);
        GlStateManager.popMatrix();
    }

    public void drawSizedTexturedModalRect(int x, int y, int u, int v, int width, int height, float texW, float texH) {
        float texU = 1.0F / texW;
        float texV = 1.0F / texH;
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);

        buffer.pos((x),         (y + height), this.zLevel).tex(((u)         * texU), ((v + height) * texV)).endVertex();
        buffer.pos((x + width), (y + height), this.zLevel).tex(((u + width) * texU), ((v + height) * texV)).endVertex();
        buffer.pos((x + width), (y),          this.zLevel).tex(((u + width) * texU), ((v)          * texV)).endVertex();
        buffer.pos((x),         (y),          this.zLevel).tex(((u)         * texU), ((v)          * texV)).endVertex();

        Tessellator.getInstance().draw();
    }

    public void sendMessageToServer(IMessage message) {
        INetHandler handler = FMLClientHandler.instance().getClientPlayHandler();
        if(handler instanceof NetHandlerPlayClient) {
            PacketHandlerHP.instance.sendToServer(message);
        } else {
            if(this.mc.getConnection() != null) {
                this.mc.getConnection().sendPacket(PacketHandlerHP.instance.getPacketFrom(message));
            }
        }
    }
}
