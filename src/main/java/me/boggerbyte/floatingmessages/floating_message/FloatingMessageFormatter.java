package me.boggerbyte.floatingmessages.floating_message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.WordUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FloatingMessageFormatter {
    private final int maxLinesWidth;
    private final int maxLinesAmount;

    public FloatingMessageFormatter(int maxLinesWidth, int maxLinesAmount) {
        this.maxLinesWidth = maxLinesWidth;
        this.maxLinesAmount = maxLinesAmount;
    }

    public List<TextComponent> format(TextComponent chatMessage) {
        String content = chatMessage.content();

        var wrapped = WordUtils.wrap(content, maxLinesWidth, null, true);
        var wrappedLines = wrapped.split(System.lineSeparator());
        if (wrappedLines.length > maxLinesAmount) {
            wrappedLines = Arrays.copyOfRange(wrappedLines, 0, maxLinesAmount);
            wrappedLines[maxLinesAmount - 1] += " ...";
        }

        return Arrays.stream(wrappedLines).map(Component::text).collect(Collectors.toList());
    }

}
