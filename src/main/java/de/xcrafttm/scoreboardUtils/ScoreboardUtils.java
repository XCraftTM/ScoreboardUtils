package de.xcrafttm.scoreboardUtils;

import de.xcrafttm.scoreboardUtils.configs.ScoreboardConfig;
import de.xcrafttm.scoreboardUtils.listener.PlayerHandler;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import fr.mrmicky.fastboard.adventure.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

import static de.xcrafttm.scoreboardUtils.tools.Utils.mm;

public final class ScoreboardUtils extends JavaPlugin {

    public static final String PREFIX = "<gray>[<aqua>ScoreboardUtils<gray>] <white>";
    private static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public ScoreboardConfig config;
    public final Map<UUID, FastBoard> boards = new HashMap<>();

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIPaperConfig(this).setNamespace("scoreboardutils").silentLogs(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        console.sendMessage(mm(PREFIX + "<green>Plugin was enabled successfully!"));

        new PlayerHandler(this);

        this.config = ConfigManager.create(ScoreboardConfig.class, (it) -> {
            it.configure(opt -> {
                opt.configurer(new YamlBukkitConfigurer(), new SerdesBukkit());
                opt.bindFile(new File(this.getDataFolder(), "scoreboard.yml"));
                opt.removeOrphans(true);
            });
            it.saveDefaults();
            it.load(true);
        });

        Bukkit.getOnlinePlayers().forEach(player -> {
            FastBoard board = new FastBoard(player);
            board.updateTitle(mm(config.getTitle(), player));
            board.updateLines(config.getLines().stream().map(line -> mm(line, player)).toList());
            this.boards.put(player.getUniqueId(), board);
        });

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : boards.values()) {
                updateBoard(board);
            }
        }, 0L, 20L);

        new CommandAPICommand("scoreboardreload")
                .withAliases("sbreload")
                .withPermission("voxoria.scoreboard.reload")
                .withShortDescription("Reloads the scoreboard configuration")
                .executesPlayer((player, args) -> {
                    config.load(true);
                    boards.values().forEach(this::updateBoard);
                    player.sendMessage(mm(PREFIX + "<green>Scoreboard configuration reloaded successfully!"));
                })
                .register();
    }

    @Override
    public void onDisable() {
        console.sendMessage(mm(PREFIX + "<red>Plugin was disabled!"));
        try {
            CommandAPI.unregister("vxcore");
        } catch (Exception e) {
            console.sendMessage(PREFIX + "§cError while unregistering /vxcore command: " + e.getMessage());
        }

        CommandAPI.onDisable();
    }

    private void updateBoard(FastBoard board) {
        Player player = board.getPlayer();
        if (player == null || !player.isOnline()) return;

        board.updateTitle(mm(config.getTitle(), player));

        List<String> lines = config.getLines();
        for (int i = 0; i < lines.size(); i++) {
            board.updateLine(i, mm(lines.get(i), player));
        }

        // Remove extra lines if the new entry has fewer lines than before
        board.updateLines(lines.stream()
                .map(line -> mm(line, player))
                .toList());
    }
}
