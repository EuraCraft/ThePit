package fr.maximouz.thepit.bank;

import fr.maximouz.thepit.events.EarnExperienceEvent;
import fr.maximouz.thepit.events.EarnGoldEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class Bank {

    private final UUID owner;
    private BigDecimal balance;
    private BigDecimal experience;
    private BigDecimal renown;
    private BigDecimal previousExperience;

    private Level level;
    private BigDecimal nextLevelExperience;
    private Prestige prestige;

    public Bank(UUID owner, BigDecimal balance, BigDecimal experience, BigDecimal renown, BigDecimal previousExperience, BigDecimal nextLevelExperience, Level level, Prestige prestige) {
        this.owner = owner;
        this.balance = balance;
        this.experience = experience;
        this.renown = renown;
        this.previousExperience = previousExperience;
        this.nextLevelExperience = nextLevelExperience;

        setLevel(level);
        setPrestige(prestige);
        updateExpBar(getPlayer());
    }

    public Bank(UUID owner, double balance, double experience, double renown, double previousExperience, double nextLevelExperience, Level level, Prestige prestige) {
        this(owner, BigDecimal.valueOf(balance), BigDecimal.valueOf(experience), BigDecimal.valueOf(renown), BigDecimal.valueOf(previousExperience), BigDecimal.valueOf(nextLevelExperience), level, prestige);
    }

    /**
     * Récupérer le possesseur de ce compte
     * @return Player owning the account
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * Récupérer l'objet Player
     * @return Player owning the account
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(owner);
    }

    /**
     * Récupérer l'argent du joueur
     * @return double
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Définir l'argent du joueur
     * @param balance New balance
     */
    public void setBalance(BigDecimal balance) {
        Bukkit.getPluginManager().callEvent(new EarnGoldEvent(this, balance.subtract(this.balance)));
        System.out.print(getPlayer().getName() + "'s bank balance change : " + balance.subtract(this.balance));
        this.balance = balance;
    }

    /**
     * Ajouter de l'argent
     * @param amount Amount to add in account
     */
    public void pay(BigDecimal amount) {
        setBalance(balance.add(amount));
    }

    /**
     * Retirer de l'argent
     * @param amount Amount to withdraw in account
     */
    public void withdraw(BigDecimal amount) {
        setBalance(balance.subtract(amount));
    }

    /**
     * Retirer de l'argent
     * @param amount Amount to withdraw in account
     */
    public void withdraw(double amount) {
        withdraw(BigDecimal.valueOf(amount));
    }

    /**
     * Récupérer l'xp du joueur
     * @return player's experience
     */
    public BigDecimal getExperience() {
        return experience;
    }

    /**
     * Définir l'xp du joueur
     * @param experience New xp
     */
    private void setExperience(BigDecimal experience) {
        this.experience = experience;
    }

    /**
     * Ajouter de l'xp au joueur
     * @param experience xp to add
     */
    public void addExperience(BigDecimal experience) {
        addExperience(experience, experience, 0);
    }

    private void addExperience(BigDecimal currentExp, BigDecimal totalExp, int levelPassed) {
        this.previousExperience = this.experience;

        if (level != Level.ONE_HUNDRED_AND_TWENTY && this.experience.add(currentExp).compareTo(nextLevelExperience) >= 0) {

            BigDecimal experienceToAdd = nextLevelExperience.subtract(this.experience);
            Level nextLevel = level.getNextLevel();
            setLevel(nextLevel);

            setExperience(this.experience.add(experienceToAdd));
            setNextLevelExperience(this.experience.add(nextLevel.expRequired));

            BigDecimal rest = currentExp.subtract(experienceToAdd);

            if (rest.compareTo(BigDecimal.ZERO) > 0)
                addExperience(rest, totalExp, levelPassed + 1);
            else
                Bukkit.getPluginManager().callEvent(new EarnExperienceEvent(this, totalExp, levelPassed + 1));

        } else {

            setExperience(this.experience.add(currentExp));
            Bukkit.getPluginManager().callEvent(new EarnExperienceEvent(this, totalExp, levelPassed));

        }

    }

    /**
     * Récupérer le niveau du joueur
     * @return int
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Définir le niveau du jouuer
     * @param level nouveau niveau
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Récuperer l'xp à atteindre pour passer au niveau suivant
     * @return BigDecimal
     */
    public BigDecimal getNextLevelExperience() {
        return nextLevelExperience;
    }

    /**
     * Définir l'xp à atteindre pour passer au niveau suivant
     */
    private void setNextLevelExperience(BigDecimal nextLevelExperience) {
        this.nextLevelExperience = nextLevelExperience.multiply(BigDecimal.valueOf(prestige.getXpNeededMultiplier()));
    }

    /**
     * Récupérer le prestige du joueur
     * @return Prestige
     */
    public Prestige getPrestige() {
        return prestige;
    }

    /**
     * Définir le prestige du joueur
     * @param prestige new Prestige
     */
    public void setPrestige(Prestige prestige) {
        this.prestige = prestige;
    }

    public void updateExpBar(Player player) {
        player.setLevel(level.level);
        float exp = experience.subtract(previousExperience).divide(nextLevelExperience.subtract(previousExperience), 2, RoundingMode.HALF_UP).floatValue();
        player.setExp(Math.min(1f, exp));
    }
}
