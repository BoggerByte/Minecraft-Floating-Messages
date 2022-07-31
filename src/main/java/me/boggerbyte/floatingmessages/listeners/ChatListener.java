package me.boggerbyte.floatingmessages.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.boggerbyte.floatingmessages.Main;
import me.boggerbyte.floatingmessages.floating_message.FloatingMessageFactory;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ChatListener implements Listener {
    private final Plugin plugin = Main.getInstance();
    private final FloatingMessageFactory floatingMessageFactory;

    public ChatListener(FloatingMessageFactory floatingMessageFactory) {
        this.floatingMessageFactory = floatingMessageFactory;
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent event) {
        var player = event.getPlayer();
        var chatMessage = (TextComponent) event.message();

        // running task in sync mode because event is asynchronous
        plugin.getServer().getScheduler()
                .runTask(plugin, () -> floatingMessageFactory.spawnOn(player, chatMessage));
    }
}
