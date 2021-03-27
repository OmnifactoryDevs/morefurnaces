package dan.morefurnaces.lib;

import net.minecraft.inventory.Slot;

public class SlotControl extends Control {
    private final Slot slot;

    public SlotControl(Slot slot, Anchor anchor, ControlContainer parent) {
        super(anchor, parent);
        this.slot = slot;
    }

    @Override
    public void updateBounds() {
        super.updateBounds();

        slot.xPos = getX() - parent.getX();
        slot.yPos = getY() - parent.getY();
    }

    public static class Builder extends ControlBuilder<SlotControl> {
        public final Slot slot;

        public Builder(Slot slot, GuiData data, String name, ControlContainer parent) {
            super(data, name, parent);
            this.slot = slot;
            anchor.size(16, 16);
        }

        @Override
        protected SlotControl createInstance() {
            return new SlotControl(slot, anchor, parent);
        }
    }
}
