package dan.morefurnaces.lib;

public class SlotData implements Cloneable<SlotData> {
    private int from;
    private int to;
    private int rows = 1;
    private int x;
    private int y;

    public boolean apply(SlotControl.Builder builder) {
        int index = builder.slot.getSlotIndex();

        int minIndex = Math.min(from, to);
        int maxIndex = Math.max(from, to);
        if (minIndex > index || maxIndex < index) return false;

        int numSlots = maxIndex - minIndex + 1;
        int cols = numSlots / rows;

        if (cols * rows != numSlots) return false;

        index -= minIndex;
        if (to < from)// bottom-right to top-left order
        {
            index = numSlots - 1 - index;
        }

        int gridX = index % cols;
        int gridY = index / cols;
        if (x >= 0)
            builder.left(gridX * 18 + x);
        else
            builder.right(-gridX * 18 - x - 16);

        if (y >= 0)
            builder.top(gridY * 18 + y);
        else
            builder.bottom(-gridY * 18 - y - 16);

        return true;
    }

    @Override
    public SlotData clone() {
        SlotData data = new SlotData();
        data.from = from;
        data.to = to;
        data.rows = rows;
        data.x = x;
        data.y = y;
        return data;
    }
}
