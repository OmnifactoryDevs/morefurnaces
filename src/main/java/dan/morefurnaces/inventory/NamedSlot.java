package dan.morefurnaces.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class NamedSlot extends Slot implements INamedSlot {

    private final String name;

    public NamedSlot(String name, IInventory inv, int index) {
        super(inv, index, -2000, -2000);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
