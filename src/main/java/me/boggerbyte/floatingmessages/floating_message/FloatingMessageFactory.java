package me.boggerbyte.floatingmessages.floating_message;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;

public class FloatingMessageFactory {
    private final static String floatingMessageTag = "floating_message_line_entity_tag";

    private final FloatingMessageFormatter messageFormatter;
    private final int minDuration;
    private final int maxDuration;
    private final int readSpeed;

    public FloatingMessageFactory(
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

    private Entity getCurrentMount(Entity entity) {
        return entity.getPassengers().stream()
                .filter(e -> e.getScoreboardTags().contains(floatingMessageTag))
                .findFirst().orElse(null);
    }

    private int computeDuration(TextComponent chatMessage) {
        var readingDuration = chatMessage.content().length() * readSpeed;
        return Math.max(minDuration, Math.min(maxDuration, readingDuration));
    }

    public void spawnOn(Entity entity, TextComponent chatMessage) {
        var currentLinesMount = (AreaEffectCloud) getCurrentMount(entity);
        var currentLinesMountDuration = currentLinesMount == null ? 0 :
                currentLinesMount.getDuration() - currentLinesMount.getTicksLived();

        var computedDuration = computeDuration(chatMessage);
        var duration = Math.max(computedDuration, currentLinesMountDuration);

        var messageLines = messageFormatter.format(chatMessage);
        Collections.reverse(messageLines);

        var lines = new ArrayList<Entity>();
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

        if (currentLinesMount != null) lines.add(currentLinesMount);

        Entity linesMount = entity;
        for (Entity line : lines) {
            linesMount.addPassenger(line);
            linesMount = line;
        }
    }

    public void despawnAll() {
        Bukkit.getWorlds()
                .forEach(w -> w.getEntities().stream()
                        .filter(e -> e.getScoreboardTags().contains(floatingMessageTag))
                        .forEach(Entity::remove));
    }
}
