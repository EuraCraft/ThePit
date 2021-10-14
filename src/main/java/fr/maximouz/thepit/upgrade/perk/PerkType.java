package fr.maximouz.thepit.upgrade.perk;

import fr.maximouz.thepit.upgrade.perk.perks.*;
import fr.maximouz.thepit.utils.Reflection;

public enum PerkType {

    GOLDEN_HEADS(GoldenHeadsPerk.class),
    FISHING_ROD(FishingRodPerk.class),
    LAVA_BUCKET(LavaBucketPerk.class),
    STRENGTH_CHAINING(StrengthChainingPerk.class),
    ENDLESS_QUIVER(EndlessQuiverPerk.class),
    MINE_MAN(MineManPerk.class),
    SECURITY_FIRST(SecurityFirstPerk.class),
    TRICKLE_DOWN(TrickleDownPerk.class),
    LUCKY_DIAMOND(LuckyDiamondPerk.class),
    SPAMMER(SpammerPerk.class),
    BOUNTY_HUNTER(BountyHunterPerk.class),
    STREAKER(StreakerPerk.class),
    GLADIATOR(GladiatorPerk.class),
    VAMPIRE(VampirePerk.class);

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
