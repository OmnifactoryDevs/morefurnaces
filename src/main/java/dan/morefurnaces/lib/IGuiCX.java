package dan.morefurnaces.lib;

import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;

public interface IGuiCX
{
    void renderTheToolTip(@Nonnull ItemStack stack, int x, int y);

    void drawTheHoveringText(List<String> textLines, int x, int y);

    void drawTheDefaultBackground();

    int getTheWidth();

    int getTheHeight();
}
