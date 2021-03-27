package dan.morefurnaces.lib;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ClientUtil
{
    private static final Map<ResourceLocation, GuiData> guiCache = Maps.newHashMap();

    public static String readResource(ResourceLocation location)
    {
        String ret = null;

        InputStream is = null;
        try
        {
            is = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
            ret = IOUtils.toString(is, Charsets.UTF_8);

        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            IOUtils.closeQuietly(is);
        }

        return ret;
    }

    public static GuiData loadGuiData(ResourceLocation location, boolean useCache)
    {
        if (useCache && guiCache.containsKey(location))
        {
            return guiCache.get(location);
        }

        String json = ClientUtil.readResource(location);
        GuiData data = gson.fromJson(json, GuiData.class);
        guiCache.put(location, data);
        return data;
    }

    private static final JsonDeserializer<Map<String, SlotData[]>> SlotDataArrayMapDeserializer = (json, typeOfT, context) ->
    {
        Map<String, SlotData[]> map = Maps.newHashMap();
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
        {
            map.put(entry.getKey(), context.deserialize(entry.getValue(), new TypeToken<SlotData[]>() {}.getType()));
        }
        return map;
    };

    private static final JsonDeserializer<Map<String, ControlData>> ControlDataMapDeserializer = (json, typeOfT, context) ->
    {
        Map<String, ControlData> map = Maps.newHashMap();
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
        {
            map.put(entry.getKey(), context.deserialize(entry.getValue(), new TypeToken<ControlData>() {}.getType()));
        }
        return map;
    };

    public static final Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(new TypeToken<Map<String, SlotData[]>>() {}.getType(), SlotDataArrayMapDeserializer)
            .registerTypeAdapter(new TypeToken<Map<String, ControlData>>() {}.getType(), ControlDataMapDeserializer)
            .create();
}
