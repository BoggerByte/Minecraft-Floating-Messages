package me.boggerbyte.floatingmessages.floating_message;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;

public class FloatingMessageSpawner {
    private final static String floatingMessageTag = "floating_message_line_entity_tag";

    private final FloatingMessageFormatter messageFormatter;
    private final int minDuration;
    private final int maxDuration;
    private final int readSpeed;

    public FloatingMessageSpawner(
            FloatingMessageFormatter messageFormatter,
            int minDuration,
            int maxDuration,
            int readSpeed
    ) {
        this.messageFormatter = messageFormatter;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.readSpeed = readSpeed;
    }

    public void mountOn(Entity entity, TextComponent chatMessage) {
        var currentMounts = entity.getPassengers().stream()
                .filter(e -> e.getScoreboardTags().contains(floatingMessageTag))
                .toList();
        var currentMount = currentMounts.isEmpty() ? null : (AreaEffectCloud) currentMounts.get(0);
        var currentMountDuration = currentMount == null ? 0 : currentMount.getDuration() - currentMount.getTicksLived();

        var computedDuration = chatMessage.content().length() * readSpeed;
        var clampedDuration = Math.max(minDuration, Math.min(maxDuration, computedDuration));
        var duration = Math.max(clampedDuration, currentMountDuration);

        var lines = new ArrayList<Entity>();
        var messageLines = messageFormatter.format(chatMessage);
        for (TextComponent messageLine : messageLines) {
            var location = entity.getLocation().add(0, 1, 0);
            var particle = entity.getWorld().spawn(location, AreaEffectCloud.class);
            particle.setRadius(0);
            particle.setWaitTime(0);
            particle.setDuration(duration);
            particle.setCustomNameVisible(true);
            particle.customName(messageLine);
            particle.addScoreboardTag(floatingMessageTag);
            lines.add(particle);
        }

        Collections.reverse(lines);

        Entity mount = entity;
        for (Entity line : lines) {
            mount.addPassenger(line);
            mount = line;
        }

        if (currentMount != null) {
            var lastLine = lines.get(lines.size() - 1);
            lastLine.addPassenger(currentMount);
        }
    }

    public void despawnAll() {
        Bukkit.getWorlds().forEach(w -> w.getEntities().forEach(e -> {
            if (e.getScoreboardTags().contains(floatingMessageTag)) e.remove();
        }));
    }
}
