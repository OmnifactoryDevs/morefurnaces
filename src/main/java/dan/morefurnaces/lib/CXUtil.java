package dan.morefurnaces.lib;

import java.lang.reflect.Array;

public class CXUtil {

    public static <T extends Cloneable<T>> T[] deepClone(T[] array, Class<T> clazz) {
        T[] ret = (T[]) Array.newInstance(clazz, array.length);
        for (int i = 0; i < ret.length; i++) {
            ret[i] = array[i].clone();
        }
        return ret;
    }
}
