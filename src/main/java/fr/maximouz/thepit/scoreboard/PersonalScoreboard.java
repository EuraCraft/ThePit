package fr.maximouz.thepit.scoreboard;

import fr.euracraft.api.EuraAPI;
import fr.euracraft.api.exception.EuraScoreboardException;
import fr.euracraft.api.net.IEuraScoreboard;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.BankManager;
import fr.maximouz.thepit.bank.Prestige;
import fr.maximouz.thepit.statistic.PlayerStatistic;
import fr.maximouz.thepit.statistic.PlayerStatisticsManager;
import fr.maximouz.thepit.statistic.PlayerStatus;
import fr.maximouz.thepit.utils.Format;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PersonalScoreboard {

    private final Bank bank;
    private final PlayerStatistic playerStatistic;

    private IEuraScoreboard euraScoreboard;

    public PersonalScoreboard(Player player) {
        this.bank = BankManager.getInstance().getBank(player);
        this.playerStatistic = PlayerStatisticsManager.getInstance().getPlayerStatistic(player);
        try {
            this.euraScoreboard = EuraAPI.getInstance().getEuraPlayer(player.getUniqueId()).getScoreboard();
        } catch (EuraScoreboardException ignored) {

        }
    }

    public void updateScoreBoard() {
        euraScoreboard.setObjectiveName(ChatColor.GOLD +""+ ChatColor.BOLD + "Eura" + ChatColor.WHITE +""+ ChatColor.BOLD + "Craft");

        int nb = 0;

        euraScoreboard.setLine(nb++, ChatColor.BLUE + "");

        if (bank.getPrestige() != Prestige.ZERO) {
            euraScoreboard.setLine(nb++, ChatColor.GRAY + "► " + ChatColor.WHITE + "Préstige: " + bank.getPrestige().getName());
        }

        String prestigeColor = bank.getPrestige().getColor();
        euraScoreboard.setLine(nb++, ChatColor.GRAY + "► " + ChatColor.WHITE + "Niveau: " + prestigeColor + "[" + bank.getLevel().getLevel() + prestigeColor + "]");
        euraScoreboard.setLine(nb++, ChatColor.GRAY + "► " + ChatColor.AQUA + Format.format(bank.getNextLevelExperience() - bank.getExperience()) + " XP" + ChatColor.WHITE + " requises");

        euraScoreboard.setLine(nb++, ChatColor.AQUA + "");

        euraScoreboard.setLine(nb++, ChatColor.GRAY + "► " + ChatColor.WHITE + "Gold: " + ChatColor.GOLD + Format.format(bank.getBalance()) + "g");

        euraScoreboard.setLine(nb++, ChatColor.GREEN + "");

        long fightingTimeLeft = TimeUnit.MILLISECONDS.toSeconds(playerStatistic.getFightingTimeLeft());
        euraScoreboard.setLine(nb++, ChatColor.GRAY + "► §fStatus: " + playerStatistic.getStatus().toString() + (playerStatistic.getStatus() == PlayerStatus.FIGHTING && fightingTimeLeft <= 5 && fightingTimeLeft > 0 ? " " + ChatColor.GRAY +""+ ChatColor.ITALIC + "(" + fightingTimeLeft + ")" : ""));
        if (playerStatistic.getKillStreak() > 0)
            euraScoreboard.setLine(nb++, ChatColor.GRAY + "► " + ChatColor.WHITE + "Série: " + ChatColor.LIGHT_PURPLE + (playerStatistic.getKillStreak()));

        euraScoreboard.setLine(nb++, ChatColor.RED + "");
        euraScoreboard.setLine(nb, ChatColor.GOLD + "play.euracraft.fr");
    }

    public IEuraScoreboard getEuraScoreboard() {
        return euraScoreboard;
    }
}