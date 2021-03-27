package dan.morefurnaces.lib;

import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.lang.reflect.Array;

public class CXUtil
{
    public static <T extends IMessage> boolean checkThreadAndEnqueue(final T message, final IMessageHandler<T, IMessage> handler, final MessageContext ctx, IThreadListener listener)
    {
        if (!listener.isCallingFromMinecraftThread())
        {
            listener.addScheduledTask(new Runnable()
            {
                @Override
                public void run()
                {
                    handler.onMessage(message, ctx);
                }
            });
            return true;
        }

        return false;
    }

    public static <T extends Cloneable<T>> T[] deepClone(T[] array, Class<T> clazz)
    {
        T[] ret = (T[]) Array.newInstance(clazz, array.length);
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = array[i].clone();
        }
        return ret;
    }
}
