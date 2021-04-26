package pl.glmc.core.bungee.cmd.economy;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.protocol.packet.Chat;
import pl.glmc.core.bungee.GlmcCoreBungee;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.UUID;

public class BalanceCommand extends Command {
    private final GlmcCoreBungee plugin;

    public BalanceCommand(final GlmcCoreBungee plugin) {
        super("balance", "", "bal", "money");

        this.plugin = plugin;

        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, this);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID playerUniqueId;
        if (args.length == 0) {
            if (sender instanceof ProxiedPlayer) {
                playerUniqueId = ((ProxiedPlayer) sender).getUniqueId();
            } else {
                return;
            }
        } else if (args.length == 1) {
            if (!sender.hasPermission("economy.balance.others")) {
                TextComponent response = new TextComponent("Nie posiadasz wystarczających uprawnień");
                response.setColor(ChatColor.RED);

                sender.sendMessage(response);

                return;
            }

            ProxiedPlayer player = this.plugin.getProxy().getPlayer(args[0]);

            if (player == null) {
                TextComponent response = new TextComponent("Nie odnaleziono gracza!");
                response.setColor(ChatColor.RED);

                sender.sendMessage(response);

                return;
            } else {
                playerUniqueId = player.getUniqueId();
            }
        } else {
            TextComponent response = new TextComponent("Poprawne użycie tej komendy: /balance");
            response.setColor(ChatColor.RED);

            sender.sendMessage(response);

            return;
        }

        BigDecimal bankBalance = this.plugin.getApiProvider().getPlayerBankEconomy().getCachedBalance(playerUniqueId);
        BigDecimal cashBalance = this.plugin.getApiProvider().getPlayerCashEconomy().getCachedBalance(playerUniqueId);

        if (bankBalance == null || cashBalance == null) {
            TextComponent textComponent = new TextComponent();
            textComponent.addExtra("Nie odnaleziono oczekiwanego konta w ekonomii! \n");
            textComponent.addExtra("Jeżeli uważasz, że to błąd, koniecznie skontaktuj się z administracją poprzez komendę /helpop lub na naszym Discordzie!");
            textComponent.setColor(ChatColor.RED);

            sender.sendMessage(textComponent);

            return;
        }

        TextComponent response = new TextComponent();
        response.addExtra( ChatColor.YELLOW + "Stan konta: " + ChatColor.GOLD + this.plugin.getApiProvider().getPlayerBankEconomy().getDecimalFormat().format(bankBalance) + "\n");
        response.addExtra(ChatColor.YELLOW + "Przy sobie: " + ChatColor.GOLD + this.plugin.getApiProvider().getPlayerCashEconomy().getDecimalFormat().format(cashBalance));

        sender.sendMessage(response);
    }
}
