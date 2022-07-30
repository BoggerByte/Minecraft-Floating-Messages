package me.boggerbyte.floatingmessages.floating_message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.WordUtils;

import java.util.Arrays;
import java.util.List;

public class FloatingMessageFormatter {
    private final static String ellipsis = "...";

    private final int maxLinesWidth;
    private final int maxLinesAmount;

    public FloatingMessageFormatter(int maxLinesWidth, int maxLinesAmount) {
        this.maxLinesWidth = maxLinesWidth;
        this.maxLinesAmount = maxLinesAmount;
    }

    public List<TextComponent> format(TextComponent chatMessage) {
        String content = chatMessage.content();

        var contentLines = WordUtils.wrap(content, maxLinesWidth).split("\n");
        if (contentLines.length == 1 && contentLines[0].length() > maxLinesWidth) {
            contentLines[0] += " " + ellipsis;
        }
        if (contentLines.length > maxLinesAmount) {
            contentLines = Arrays.copyOfRange(contentLines, 0, maxLinesAmount);
            contentLines[maxLinesAmount - 1] += " " + ellipsis;
        }

        return Arrays.stream(contentLines).map(Component::text).toList();
    }

}
