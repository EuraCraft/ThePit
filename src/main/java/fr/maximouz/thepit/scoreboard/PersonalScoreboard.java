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

/*
 * This file is part of SamaGamesAPI.
 *
 * SamaGamesAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamaGamesAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamaGamesAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        euraScoreboard.setObjectiveName("§6§lEura§f§lCraft");

        int nb = 0;

        euraScoreboard.setLine(nb++, "§1");

        if (bank.getPrestige() != Prestige.ZERO) {
            euraScoreboard.setLine(nb++, "§7► §fPréstige: " + bank.getPrestige().getName());
        }

        String prestigeColor = bank.getPrestige().getColor();
        euraScoreboard.setLine(nb++, "§7► §fNiveau: " + prestigeColor + "[" + bank.getLevel().getLevel() + prestigeColor + "]");
        euraScoreboard.setLine(nb++, "§7► §b" + Format.format(bank.getNextLevelExperience() - bank.getExperience()) + " XP" + ChatColor.WHITE + " requise");

        euraScoreboard.setLine(nb++, "§3");

        euraScoreboard.setLine(nb++, "§7► §fGold: §6" + Format.format(bank.getBalance()) + "g");

        euraScoreboard.setLine(nb++, "§4");

        long fightingTimeLeft = TimeUnit.MILLISECONDS.toSeconds(playerStatistic.getFightingTimeLeft());
        euraScoreboard.setLine(nb++, "§7► §fStatus: " + playerStatistic.getStatus().toString() + (playerStatistic.getStatus() == PlayerStatus.FIGHTING && fightingTimeLeft <= 5 && fightingTimeLeft > 0 ? " §7§o(" + fightingTimeLeft + ")" : ""));
        if (playerStatistic.getKillStreak() > 0)
            euraScoreboard.setLine(nb++, "§7► §fSérie: §d" + (playerStatistic.getKillStreak()));

        euraScoreboard.setLine(nb++, "§2§1§1§3");
        euraScoreboard.setLine(nb, "§6play.euracraft.fr");
    }

    public IEuraScoreboard getEuraScoreboard() {
        return euraScoreboard;
    }
}