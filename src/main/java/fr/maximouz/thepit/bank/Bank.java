package fr.maximouz.thepit.bank;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Bank {

    private final Player owner;
    private double balance;
    private double experience;
    private double previousExperience;

    private Level level;
    private double nextLevelExperience;
    private Prestige prestige;

    public Bank(Player owner, double balance, double experience, double previousExperience, Level level, Prestige prestige) {
        this.owner = owner;
        this.balance = balance;
        this.experience = experience;
        this.previousExperience = previousExperience;

        setNextLevelExperience(experience + Level.getLevel(level.level + 1).expRequired);
        setLevel(level);
        setPrestige(prestige);
        updateExpBar();
    }

    /**
     * Récupérer le possesseur de ce compte
     * @return Player owning the account
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Récupérer l'argent du joueur
     * @return double
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Définir l'argent du joueur
     * @param balance New balance
     */
    public void setBalance(double balance) {
        this.balance = BigDecimal.valueOf(balance).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Ajouter de l'argent
     * @param amount Amount to add in account
     */
    public void pay(double amount) {
        this.balance = Math.min(balance + amount, Double.MAX_VALUE);
    }

    /**
     * Retirer de l'argent
     * @param amount Amount to withdraw in account
     */
    public void withdraw(double amount) {
        this.balance = Math.max(balance - amount, 0);
    }

    /**
     * Récupérer l'xp du joueur
     * @return Double
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Définir l'xp du joueur
     * @param experience New xp
     */
    public void setExperience(double experience) {
        this.previousExperience = this.experience;
        this.experience = experience;
        if (experience >= nextLevelExperience) {
            setLevel(Level.getLevel(getLevel().level + 1));
        }
        updateExpBar();
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
        if (this.level != null && this.level != level) {
            setNextLevelExperience(experience + Level.getLevel(getLevel().level + 1).expRequired);
            if (this.level.level < level.level) {
                owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            }
        }
        this.level = level;
    }

    /**
     * Récuperer l'xp à atteindre pour passer au niveau suivant
     * @return double
     */
    public double getNextLevelExperience() {
        return nextLevelExperience;
    }

    /**
     * Définir l'xp à atteindre pour passer au niveau suivant
     */
    private void setNextLevelExperience(double nextLevelExperience) {
        this.nextLevelExperience = nextLevelExperience;
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

    public void updateExpBar() {
        owner.setLevel(level.level);
        owner.setExp((float) ((experience - previousExperience) / (nextLevelExperience - previousExperience)));
    }
}
