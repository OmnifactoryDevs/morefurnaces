package dan.morefurnaces.lib;

import dan.morefurnaces.gui.Textures;

public class GuiTextures {
    private GuiTextures() {
    }

    public static final GuiTexture BG = new GuiTexture(Textures.BG, 256, 256, true);
    public static final GuiTexture MC_WIDGETS = new GuiTexture(Textures.MC_WIDGETS, 256, 256, false);

    static {
        MC_WIDGETS.addPart("button_disabled", 0, 46, 200, 20);
        MC_WIDGETS.addPart("button_default", 0, 66, 200, 20);
        MC_WIDGETS.addPart("button_hover", 0, 86, 200, 20);
    }
}
