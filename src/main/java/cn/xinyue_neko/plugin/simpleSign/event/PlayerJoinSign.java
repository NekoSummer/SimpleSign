package cn.xinyue_neko.plugin.simpleSign.event;

import cn.xinyue_neko.plugin.simpleSign.SimpleSign;
import cn.xinyue_neko.plugin.simpleSign.config.Config;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PlayerJoinSign implements Listener {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern(Config.DATE_TIME_FORMAT);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (SimpleSign.getEconomy() == null) return;

        String uuid = player.getUniqueId().toString();
        String today = LocalDate.now().format(DATE);
        String lastSign = SimpleSign.getData().getString(uuid);

        if (today.equals(lastSign)) {
            player.sendMessage(Config.MESSAGE_ALREADY
                    .replace('&', ChatColor.COLOR_CHAR)
            );
            return;
        }

        EconomyResponse response = SimpleSign.getEconomy().depositPlayer(player, Config.SIGN_MONEYS);
        if (response.transactionSuccess()) {
            SimpleSign.getData().set(uuid, today);
            SimpleSign.saveData();
            player.sendMessage(Config.MESSAGES_SUCCESS
                    .replace("{money}", (Config.formatMoney(Config.SIGN_MONEYS)))
                    .replace('&', ChatColor.COLOR_CHAR)
            );
        } else {
            player.sendMessage(Config.MESSAGE_ERROR.replace("{error}", response.errorMessage));
        }
    }
}
