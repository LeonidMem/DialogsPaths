package ru.leonidm.dialogs.paths;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.leonidm.dialogs.api.events.DialogAddEvent;
import ru.leonidm.dialogs.api.events.DialogRemoveEvent;
import ru.leonidm.dialogs.api.events.DialogsReloadEvent;
import ru.leonidm.dialogs.api.events.QuestStartEvent;
import ru.leonidm.dialogs.paths.entities.ConfigurablePath;

public class EventsHandler implements Listener {

    @EventHandler
    public void onReload(DialogsReloadEvent e) {
        DialogsPaths.getInstance().reloadConfig();
        FilesLoader.reload();
    }

    @EventHandler
    public void onDialogAdd(DialogAddEvent e) {
        check(e.getOfflinePlayer());
    }

    @EventHandler
    public void onDialogRemove(DialogRemoveEvent e) {
        check(e.getOfflinePlayer());
    }

    @EventHandler
    public void onQuestAdd(DialogAddEvent e) {
        check(e.getOfflinePlayer());
    }

    @EventHandler
    public void onQuestRemove(DialogRemoveEvent e) {
        check(e.getOfflinePlayer());
    }

    @EventHandler
    public void onQuestStart(QuestStartEvent e) {
        check(e.getOfflinePlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {
        check(e.getPlayer());
    }

    private void check(OfflinePlayer player) {
        if(player.getPlayer() != null) ConfigurablePath.checkPlayer(player.getPlayer());
    }
}
