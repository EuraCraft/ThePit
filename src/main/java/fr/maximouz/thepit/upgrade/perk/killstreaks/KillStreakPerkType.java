package fr.maximouz.thepit.upgrade.perk.killstreaks;

import fr.euracraft.api.util.SymbolUtil;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.perk.killstreaks.perks.*;
import fr.maximouz.thepit.utils.Reflection;
import org.bukkit.Material;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum KillStreakPerkType {

    SECOND_GOLDEN_APPLE(SecondGAppleKillStreakPerk.class, KillStreakPerkEvery.THREE, KillStreakPerkSize.NORMAL, "Deuxième Pomme Dorée", Material.GOLDEN_APPLE, Level.TEN, 1500.0,
            "§7Vous gagnez:",
            "§7 ■ §b+5XP",
            "§7 ■ §e+5g",
            "§7 ■ §7pomme dorée/tête en or supplémentaire."),
    EXPLICIOUS(ExpliciousKillStreakPerk.class, KillStreakPerkEvery.THREE, KillStreakPerkSize.NORMAL, "Sagesse", Material.INK_SACK, (byte) 12, Level.THIRTY, 3000.0,
            "§7Vous gagnez §b+12XP§7."),
    ARQUEBUSIER(ArquebusierKillStreakPerk .class, KillStreakPerkEvery.THREE, KillStreakPerkSize.NORMAL, "Robin des bois", Material.ARROW, Level.TEN, 5000.0,
            "§7Vous gagnez:",
            "§7 ■ §6+7g",
            "§7 ■ §f+16 flêches",
            "§7 ■ §eRapidité I §7(10s)"),

    FIGHTER(FighterKillStreakPerk.class, KillStreakPerkEvery.FIVE, KillStreakPerkSize.NORMAL, "Combattant", Material.FIREBALL, Level.FIFTY, 5000.0,
            "§7Vous gagnez:",
            "§7 ■ §c+20% de dégâts",
            "§7 ■ §eRapidité I§7",
            "§7 ■ §9Résistance I",
            "§7pendant 4 secondes."),
    USAIN_BOLT(UsainBoltKillStreakPerk.class, KillStreakPerkEvery.FIVE, KillStreakPerkSize.NORMAL, "Usain Bolt", Material.GOLD_BOOTS, Level.ONE_HUNDRED, 10000.0,
            "§7Vous gagnez §eRapidité II",
            "§7pendant 4 secondes."),

    FEAST(FeastKillStreakPerk.class, KillStreakPerkEvery.SEVEN, KillStreakPerkSize.NORMAL, "Festin Royal", Material.COOKED_MUTTON, Level.THIRTY, 4000.0,
            "§7Vous obtenez un §6Steak Royal§7 qui vous octroie:",
            "§7 ■ §c+20% de dégâts",
            "§7 ■ §eRapidité I",
            "§7 ■ §9Résistance I",
            "§7pendant 10 secondes.",
            "",
            "§7§oConsommation instantanée."),
    COUNTER_STRIKE(CounterStrikeKillStreakPerk.class, KillStreakPerkEvery.SEVEN, KillStreakPerkSize.NORMAL, "Contre Attaque", Material.DIAMOND_BARDING, Level.FORTY, 5000.0,
            "§7Vous infligez §c+15% de dégâts",
            "§7et bloquez §91" + SymbolUtil.HEARTH + "§7 par coup",
            "§7pendant 8 secondes."),

    SPONGE_BOB(SpongeBobKillStreakPerk.class, KillStreakPerkEvery.TWENTY_FIVE, KillStreakPerkSize.NORMAL, "Bob l'éponge", Material.SPONGE, Level.SEVENTY, 12000.0,
            "§7Vous gagnez §6+15" + SymbolUtil.HEARTH + " d'absorption."),

    OVER_DRIVE(OverDriveKillStreakPerk.class, KillStreakPerkEvery.FIFTY, KillStreakPerkSize.MEGA_STREAK, "Overdrive", Material.BLAZE_POWDER, Level.ONE, 0.0,
            "§7À l'activation:",
            "§a ■ §eRapidité I §7permanent",
            "§a ■ §b+100% XP§7 sur les meurtres",
            "§a ■ §6+50% de gold§7 sur les meurtres",
            "",
            "§7Mais:",
            "§c ■ §7Tous les 5 meurtres vous",
            "§7perdez §c-0.1" + SymbolUtil.HEARTH + "§7 lorsqu'un",
            "§7joueur vous attaque.",
            "§7§o(Accumulation)",
            "",
            "§7À la mort:",
            "§e ■ §7Vous gagnez §b4,000 XP");

    private final Class<? extends KillStreakPerk> clazz;
    private final KillStreakPerkEvery every;
    private final KillStreakPerkSize size;

    private final String displayName;
    private final Material material;
    private final byte data;
    private final Level levelRequired;
    private final BigDecimal price;
    private final String[] description;

    KillStreakPerkType(Class<? extends KillStreakPerk> clazz, KillStreakPerkEvery every, KillStreakPerkSize size, String displayName, Material material, byte data, Level levelRequired, double price, String... description) {
        this.clazz = clazz;
        this.every = every;
        this.size = size;
        this.displayName = displayName;
        this.material = material;
        this.data = data;
        this.levelRequired = levelRequired;
        this.price = BigDecimal.valueOf(price);
        this.description = description;
    }

    KillStreakPerkType(Class<? extends KillStreakPerk> clazz, KillStreakPerkEvery every, KillStreakPerkSize size, String displayName, Material material, Level levelRequired, double price, String... description) {
        this(clazz, every, size, displayName, material, (byte) 0, levelRequired, price, description);
    }

    public Class<? extends KillStreakPerk> getClazz() {
        return clazz;
    }

    public KillStreakPerkEvery getEvery() {
        return every;
    }

    public KillStreakPerkSize getSize() {
        return size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }

    public Level getLevelRequired() {
        return levelRequired;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String[] getDescription() {
        return description;
    }

    public KillStreakPerk getNewInstance() {
        try {
            return (KillStreakPerk) Reflection.callConstructor(Reflection.getConstructor(getClazz(), getClass()), this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<KillStreakPerkType> get(KillStreakPerkEvery every) {
        return Arrays.stream(values()).filter(type -> type.getEvery() == every).collect(Collectors.toList());
    }

    public static List<KillStreakPerkType> get(KillStreakPerkSize size) {
        return Arrays.stream(values()).filter(type -> type.getSize() == size).collect(Collectors.toList());
    }

}
