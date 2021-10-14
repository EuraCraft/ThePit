package fr.maximouz.thepit.prime;

import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PrimeManager {

    private static final PrimeManager INSTANCE = new PrimeManager();

    private final List<Prime> primes;

    public PrimeManager() {
        this.primes = new ArrayList<>();
    }

    public static PrimeManager getInstance() {
        return INSTANCE;
    }

    public List<Prime> getPrimes() {
        return primes;
    }

    public void createPrime(Prime prime) {
        primes.add(prime);
    }

    public void createPrime(Player player, BigDecimal defaultGold) {
        createPrime(new Prime(player, defaultGold));
    }

    public void deletePrime(Prime prime) {
        primes.remove(prime);
    }

    public Prime getPrime(Player player) {
        return primes.stream().filter(prime -> prime.getPlayer().getUniqueId() == player.getUniqueId()).findFirst().orElse(null);
    }

    public boolean hasBounty(Player player) {
        return primes.stream().anyMatch(prime -> prime.getPlayer().getUniqueId() == player.getUniqueId());
    }

}
