package fr.maximouz.thepit.upgrade.perk.killstreaks.perks;

import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import fr.maximouz.thepit.utils.Reflection;
import org.bukkit.entity.Player;

public class SpongeBobKillStreakPerk extends KillStreakPerk {

    private static final Class<?> humanClass = Reflection.getNMSClass("EntityHuman");

    public SpongeBobKillStreakPerk(KillStreakPerkType type) {
        super(type);
    }

    @Override
    public void trigger(Player player) {
        Object human = humanClass.cast(Reflection.getHandle(player));
        try {
            float absorption = Reflection.callMethod(Reflection.getMethod(humanClass, "getAbsorptionHearts"), player);
            Reflection.callMethod(Reflection.getMethod(humanClass, "setAbsorptionHearts", float.class), human, absorption + 30.0f);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
