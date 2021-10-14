package fr.maximouz.thepit.upgrade;

import fr.maximouz.thepit.upgrade.upgrades.*;
import fr.maximouz.thepit.utils.Reflection;

public enum UpgradeType {

    XP_BOOST(XpBoostUpgrade.class, "Boost XP", "§7Vous gagnez §b+10% d'XP§7 peu importe", "§7la raison du gain."),
    GOLD_BOOST(GoldBoostUpgrade.class, "§7Vous gagnez §e+10% de Gold§7 lorsque vous", "§7réalisez un meurtre, une assistance ou", "§7que vous en ramassez au sol."),
    ASSASSIN(AssassinUpgrade.class, "Assassin", "§7Vous infligez §c+1%§7 de dégâts", "§7au corps à corps."),
    SNIPER(SniperUpgrade.class, "Tireur d'élite", "§7Vous infligez §c+3%§7 de dégâts lorsque", "§7vous utilisez votre arc."),
    TANK(TankUpgrade.class, "Tank", "§7Les joueurs vous infligent §9-1%", "§7de dégâts."),
    BUILDER(BuilderUpgrade.class, "Constructeur", "§7Vos blocs restent en vie §a60%§7 plus", "§7longtemps qu'avant."),
    GATO(GatoUpgrade.class, "Gourmet", "%KILLS%§7 après votre mort", "§7rapportent §6+5g§7 et §b+5 XP§7.");

    private final Class<? extends Upgrade> clazz;
    private final String displayName;
    private final String[] description;

    UpgradeType(Class<? extends Upgrade> clazz, String displayName, String... description) {
        this.clazz = clazz;
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String[] getDescription() {
        return description;
    }

    public Upgrade getNewInstance() {
        try {
            return (Upgrade) Reflection.callConstructor(Reflection.getConstructor(clazz, getClass()), this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
