package fr.maximouz.thepit.upgrade;

import fr.maximouz.thepit.upgrade.upgrades.*;
import fr.maximouz.thepit.utils.Reflection;

public enum UpgradeType {

    XP_BOOST(XpBoostUpgrade.class),
    GOLD_BOOST(GoldBoostUpgrade.class),
    ASSASSIN(AssassinUpgrade.class),
    SNIPER(SniperUpgrade.class),
    TANK(TankUpgrade.class),
    BUILDER(BuilderUpgrade.class),
    GATO(GatoUpgrade.class);

    private final Class<? extends Upgrade> clazz;

    UpgradeType(Class<? extends Upgrade> clazz) {
        this.clazz = clazz;
    }

    public Upgrade getNewInstance() {
        try {
            return (Upgrade) Reflection.callConstructor(Reflection.getConstructor(clazz));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
