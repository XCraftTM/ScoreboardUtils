package de.xcrafttm.scoreboardUtils.listener;

import de.xcrafttm.scoreboardUtils.ScoreboardUtils;
import fr.mrmicky.fastboard.adventure.FastBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static de.xcrafttm.scoreboardUtils.tools.Utils.mm;

public class PlayerHandler implements Listener {

    private final ScoreboardUtils plugin;

    public PlayerHandler(ScoreboardUtils plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(plugin.boards.containsKey(player.getUniqueId())) return;
        FastBoard board = new FastBoard(player);
        board.updateTitle(mm(plugin.config.getTitle(), player));
        board.updateLines(plugin.config.getLines().stream().map(line -> mm(line, player)).toList());
        plugin.boards.put(player.getUniqueId(), board);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FastBoard board = plugin.boards.remove(player.getUniqueId());
        if (board != null) board.delete();
    }
}
