package dan.morefurnaces.items;

import dan.morefurnaces.FurnaceType;

public enum Upgrades {
    STONE_TO_IRON(null, FurnaceType.IRON),
    STONE_TO_NETHERRACK(null, FurnaceType.NETHERRACK),
    IRON_TO_GOLD(FurnaceType.IRON, FurnaceType.GOLD),
    IRON_TO_OBSIDIAN(FurnaceType.IRON, FurnaceType.OBSIDIAN),
    GOLD_TO_DIAMOND(FurnaceType.GOLD, FurnaceType.DIAMOND),
    COPPER_TO_SILVER(FurnaceType.COPPER, FurnaceType.SILVER),
    IRON_TO_SILVER(FurnaceType.IRON, FurnaceType.SILVER),
    STONE_TO_COPPER(null, FurnaceType.COPPER);

    private final FurnaceType from;
    private final FurnaceType to;

    Upgrades(FurnaceType from, FurnaceType to) {
        this.from = from;
        this.to = to;
    }

    public boolean isVanillaUpgrade() {
        return from == null;
    }

    public boolean canUpgrade(FurnaceType type) {
        return from != null && type == from;
    }

    public FurnaceType getUpgradedType() {
        return to;
    }

    public String getUnlocalizedName() {
        return name().toLowerCase();
    }
}
