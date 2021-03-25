package fr.maximouz.thepit.upgrade.perk;

import fr.maximouz.thepit.upgrade.perk.perks.GoldenHeadsPerk;
import fr.maximouz.thepit.upgrade.perk.perks.NonePerk;
import fr.maximouz.thepit.utils.Reflection;

public enum PerkType {

    NONE(NonePerk.class),
    GOLDEN_HEADS(GoldenHeadsPerk.class);

    private final Class<? extends Perk> clazz;

    PerkType(Class<? extends Perk> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Perk> getClazz() {
        return clazz;
    }

    public Perk getNewInstance() {
        try {
            return (Perk) Reflection.callConstructor(Reflection.getConstructor(getClazz()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
