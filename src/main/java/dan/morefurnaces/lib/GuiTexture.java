package dan.morefurnaces.lib;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.util.Rectangle;

import java.lang.reflect.Type;
import java.util.Map;

public class GuiTexture
{
    private final ResourceLocation location;
    private final int texWidth;
    private final int texHeight;

    private Map<String, Rectangle> parts = Maps.newHashMap();

    public GuiTexture(ResourceLocation location, int width, int height, boolean hasPartJson)
    {
        this.location = location;
        texWidth = width;
        texHeight = height;

        if (hasPartJson)
            loadTextureParts();
    }

    private void loadTextureParts()
    {
        String json = ClientUtil.readResource(new ResourceLocation(location.toString().replace(".png", ".parts.json")));
        if (json == null) return;

        Type type = new TypeToken<Map<String, Rectangle>>() {}.getType();
        Map<String, Rectangle> map = ClientUtil.gson.fromJson(json, type);
        if (map == null) return;

        for (Map.Entry<String, Rectangle> entry : map.entrySet())
        {
            addPart(entry.getKey(), entry.getValue());
        }
    }

    public Rectangle getPart(String key)
    {
        return parts.get(key);
    }

    public void addPart(String key, int x, int y, int width, int height)
    {
        parts.put(key, new Rectangle(x, y, width, height));
    }

    public void addPart(String key, Rectangle rect)
    {
        parts.put(key, rect);
    }

    public void draw(int x, int y, int u, int v, int width, int height)
    {
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(location);
        Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, texWidth, texHeight);
    }

    public void draw(int x, int y, int u, int v, int maxWidth, int maxHeight, float width, float height)
    {
        int w = (int) (maxWidth * width);
        int h = (int) (maxHeight * height);

        if (width < 0)
        {
            w = maxWidth + w;
            u += maxWidth - h;
            x += maxWidth - w;
        }
        if (height < 0)
        {
            h = maxHeight + h;
            v += maxHeight - h;
            y += maxHeight - h;
        }

        draw(x, y, u, v, w, h);
    }

    public void drawPart(String key, int x, int y, float width, float height)
    {
        Rectangle rect = parts.get(key);
        draw(x, y, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), width, height);
    }

    public void drawPart(String key, int x, int y)
    {
        Rectangle rect = parts.get(key);
        draw(x, y, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void drawPart(String key, int x, int y, int width, int height)
    {
        Rectangle rect = parts.get(key);
        draw(x, y, rect.getX(), rect.getY(), width, height);
    }

    public void drawPartSliced(String key, Rectangle rect)
    {
        drawPartSliced(key, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void drawPartSliced(String key, int x, int y, int width, int height)
    {
        Rectangle rect = parts.get(key);
        drawSliced(x, y, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), width, height);
    }

    public void drawSliced(int _x, int _y, int u, int v, int uWidth, int uHeight, int width, int height)
    {
        int heightChange = height % 2 != 0 ? 1 : 0;
        int widthChange = width % 2 != 0 ? 1 : 0;

        int x = _x;
        int y = _y;
        int cH = width / 2;
        int cV = height / 2;

        // Top Left
        draw(x, y, u, v, cH + widthChange, cV + heightChange);
        // Top Right
        draw(x + cH + widthChange, y, u + uWidth - cH, v, cH, cV + heightChange);
        // Bottom Left
        draw(x, y + cV + heightChange, u, v + uHeight - cV, cH + widthChange, cV);
        // Bottom Right
        draw(x + cH + widthChange, y + cV + heightChange, u + uWidth - cH, v + uHeight - cV, cH, cV);
    }
}
