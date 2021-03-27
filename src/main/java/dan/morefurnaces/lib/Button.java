package dan.morefurnaces.lib;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;

public class Button extends Control {
    public boolean playSound = true;
    protected boolean hover;
    protected String text = "";

    public Button(String text, Anchor anchor, ControlContainer parent) {
        super(anchor, parent);
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button, boolean intoControl) {
        if (button == 0 && playSound && intoControl) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }

    protected String getHoverState(boolean mouseOver) {
        int i = 1;

        if (!isEnabled()) {
            return "button_disabled";
        } else if (mouseOver) {
            return "button_hover";
        }

        return "button_default";
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        hover = isMouseOverControl(mouseX, mouseY);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GuiTextures.MC_WIDGETS.drawPartSliced(getHoverState(hover), getBounds());
        drawContent(mouseX, mouseY, partialTicks);
    }

    protected void drawContent(int mouseX, int mouseY, float partialTicks) {
        int l = 0xe0e0e0;

        if (!isEnabled()) {
            l = -0x5f5f60;
        } else if (hover) {
            l = 0xffffa0;
        }


        drawCenteredString(mc.fontRenderer, text, getX() + getWidth() / 2, getY() + (getHeight() - 8) / 2, l);
    }

    public static class Builder extends ControlBuilder<Button> {
        private final String text;

        public Builder(String text, GuiData data, String name, ControlContainer parent) {
            super(data, name, parent);
            this.text = text;
        }

        @Override
        protected Button createInstance() {
            return new Button(text, anchor, parent);
        }
    }
}
