package pl.glmc.core.bungee.cmd.economy;

import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang.StringUtils;
import pl.glmc.core.bungee.GlmcCoreBungee;
import pl.glmc.core.bungee.api.economy.ApiEconomyProvider;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class BaltopCommand extends Command {
    private final static int PER_PAGE = 10;
    private final static DateTimeFormatter LAST_REFRESHED_FORMATTER = DateTimeFormatter.ofPattern("d/M/uu H:mm:ss");

    private final GlmcCoreBungee plugin;

    private LocalDateTime lastRefresh;
    private LinkedHashMap<UUID, BigDecimal> topBalance;
    private int pages;

    public BaltopCommand(final GlmcCoreBungee plugin) {
        super("baltop", "economy.baltop", "balancetop");

        this.plugin = plugin;

        this.lastRefresh = new Timestamp(new Date().getTime()).toLocalDateTime();
        this.topBalance = new LinkedHashMap<>();

        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, this);

        this.calculateBalanceTop();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
        boolean refresh = lastRefresh.plusMinutes(1).isBefore(currentTimestamp.toLocalDateTime());

        if (args.length > 0 && sender.hasPermission("economy.baltop.force")
                && Arrays.stream(args).anyMatch(arg -> arg.equalsIgnoreCase("--force"))) {
            refresh = true;
        }

        if (refresh) {
            this.calculateBalanceTop()
                    .thenAccept(success -> {
                        if (success) {
                            this.sendBaltop(sender, args);
                        } else {
                            TextComponent errorResponse = new TextComponent();
                            errorResponse.addExtra(ChatColor.RED + "Wystąpił błąd podczas wczytywania listy najbogatszych graczy!");
                        }
                    });
        } else {
            this.sendBaltop(sender, args);
        }
    }

    private void sendBaltop(CommandSender sender, String[] args) {
        int page;
        if (args.length > 0 && StringUtils.isNumeric(args[0]) && sender.hasPermission("economy.baltop.all")) {
            page = Integer.parseInt(args[0]) - 1;

            if (page < 0) {
                TextComponent errorResponse = new TextComponent();
                errorResponse.addExtra(ChatColor.RED + "Number strony nie może być mniejszy niż 1!");

                sender.sendMessage(errorResponse);

                return;
            } else if (page > this.pages) {
                TextComponent errorResponse = new TextComponent();
                errorResponse.addExtra(ChatColor.RED + "Number strony nie może być większy niż " + this.pages + "!");

                sender.sendMessage(errorResponse);

                return;
            }
        } else {
            page = 0;
        }

        String lastRefreshTime = lastRefresh.format(LAST_REFRESHED_FORMATTER);

        TextComponent baltopResponse = new TextComponent();
        baltopResponse.addExtra(ChatColor.GOLD + "Najbogatsi gracze na serwerze: " + ChatColor.GRAY + "(" + lastRefreshTime + ")");

        int position = 0;

        for (Map.Entry<UUID, BigDecimal> sortedPosition : topBalance.entrySet()) {
            position++;

            if (position <= PER_PAGE * page) continue;
            else if (position > PER_PAGE * page + PER_PAGE) break;

            ProxiedPlayer player = this.plugin.getProxy().getPlayer(sortedPosition.getKey());

            String playerName;
            if (player == null) {
                //todo replace with own user cache for better performance
                User user = this.plugin.getApiProvider().getLuckPermsHook().getLuckPerms().getUserManager().loadUser(sortedPosition.getKey()).join();

                if (user.getUsername() == null) {
                    playerName = sortedPosition.getKey().toString();
                } else {
                    playerName = user.getUsername();
                }
            } else {
                playerName = player.getName();
            }

            baltopResponse.addExtra("\n" + ChatColor.GRAY + position + ". " + ChatColor.WHITE + playerName + ": " + ChatColor.YELLOW + NumberFormat.getNumberInstance().format(sortedPosition.getValue()));
        }
        if (sender.hasPermission("economy.baltop.all")) {
            baltopResponse.addExtra("\n" + ChatColor.GOLD + "Strona " + ChatColor.YELLOW + (page + 1) + ChatColor.GRAY + "/" + ChatColor.YELLOW + pages + ChatColor.GOLD + ", aby przejść na kolejną użyj komendy /baltop " + (page + 2));
        }

        sender.sendMessage(baltopResponse);
    }

    private CompletableFuture<Boolean> calculateBalanceTop() {
        CompletableFuture<Boolean> callback = new CompletableFuture<>();

        this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
            ApiEconomyProvider bankEconomyProvider = (ApiEconomyProvider) this.plugin.getApiProvider().getPlayerBankEconomy();
            ApiEconomyProvider cashEconomyProvider = (ApiEconomyProvider) this.plugin.getApiProvider().getPlayerCashEconomy();

            HashMap<UUID, BigDecimal> combined = new HashMap<>(bankEconomyProvider.getRegisteredAccounts());

            cashEconomyProvider.getRegisteredAccounts().forEach((uniqueId, balance) -> {
                combined.compute(uniqueId, (currentUniqueId, currentBalance) -> {
                    if (currentBalance == null) {
                        return balance;
                    } else {
                        return currentBalance.add(balance);
                    }
                });
            });

            this.topBalance = combined.entrySet().stream()
                    .sorted(Map.Entry.<UUID, BigDecimal>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (key, value) -> key, LinkedHashMap::new));

            this.lastRefresh = new Timestamp(new Date().getTime()).toLocalDateTime();

            int entries = topBalance.size();;

            this.pages = entries % PER_PAGE == 0 ? entries / PER_PAGE : (entries - entries % PER_PAGE) / PER_PAGE + 1;

            callback.complete(true);
        });

        return callback;
    }
}
