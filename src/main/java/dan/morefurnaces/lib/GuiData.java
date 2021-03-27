package dan.morefurnaces.lib;

import com.google.common.collect.Maps;
import dan.morefurnaces.inventory.INamedSlot;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class GuiData {
    private String parent;
    private Map<String, ControlData> controls;
    private Map<String, SlotData[]> slots;

    private transient GuiData parentData;

    public Anchor apply(String name, ControlContainer parent, IControlProvider provider) {
        if (controls != null) {
            loadParent();

            ControlData data = controls.get(name);
            if (data != null) {
                Anchor anchor = new Anchor();
                data.apply(anchor, parent, provider);
                return anchor;
            }
        }

        return new Anchor();
    }

    public SlotControl.Builder apply(SlotControl.Builder builder) {
        if (slots != null) {
            loadParent();

            if (builder.slot instanceof INamedSlot) {
                String name = ((INamedSlot) builder.slot).getName();

                SlotData[] slotDatas = slots.get(name);
                for (SlotData slotData : slotDatas) {
                    if (slotData.apply(builder))
                        return builder;
                }
            }
        }
        return builder;
    }

    private void loadParent() {
        if (parent != null && parentData == null) {
            parentData = ClientUtil.loadGuiData(new ResourceLocation(parent), true);
            parentData.loadParent();

            if (controls == null) controls = Maps.newLinkedHashMap();
            if (slots == null) slots = Maps.newHashMap();

            Map<String, ControlData> parentControls = parentData.cloneControls();
            for (Map.Entry<String, ControlData> entry : controls.entrySet()) {
                if (parentControls.containsKey(entry.getKey())) {
                    ControlData toOverride = parentControls.get(entry.getKey());
                    toOverride.claimChildValues(entry.getValue());
                } else {
                    parentControls.put(entry.getKey(), entry.getValue());
                }
            }
            controls.clear();
            controls.putAll(parentControls);

            Map<String, SlotData[]> parentSlots = parentData.cloneSlots();
            for (Map.Entry<String, SlotData[]> entry : slots.entrySet()) {
                parentSlots.put(entry.getKey(), entry.getValue());
            }
            slots.clear();
            slots.putAll(parentSlots);
        }
    }

    private Map<String, SlotData[]> cloneSlots() {
        Map<String, SlotData[]> ret = Maps.newLinkedHashMap();

        if (slots != null) {
            for (Map.Entry<String, SlotData[]> entry : slots.entrySet()) {
                ret.put(entry.getKey(), CXUtil.deepClone(entry.getValue(), SlotData.class));
            }
        }

        return ret;
    }

    private Map<String, ControlData> cloneControls() {
        Map<String, ControlData> ret = Maps.newLinkedHashMap();

        if (controls != null) {
            for (Map.Entry<String, ControlData> entry : controls.entrySet()) {
                ret.put(entry.getKey(), entry.getValue().clone());
            }
        }

        return ret;
    }
}
