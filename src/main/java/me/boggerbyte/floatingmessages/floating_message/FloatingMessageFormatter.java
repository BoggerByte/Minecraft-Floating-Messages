package me.boggerbyte.floatingmessages.floating_message;

import org.apache.commons.lang.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloatingMessageFormatter {
    private final int maxLinesWidth;
    private final int maxLinesAmount;

    public FloatingMessageFormatter(int maxLinesWidth, int maxLinesAmount) {
        this.maxLinesWidth = maxLinesWidth;
        this.maxLinesAmount = maxLinesAmount;
    }

    public List<String> format(String chatMessage) {
        var wrapped = WordUtils.wrap(chatMessage, maxLinesWidth, null, true);
        var wrappedLines = wrapped.split(System.lineSeparator());
        if (wrappedLines.length > maxLinesAmount) {
            wrappedLines = Arrays.copyOfRange(wrappedLines, 0, maxLinesAmount);
            wrappedLines[maxLinesAmount - 1] += " ...";
        }

        return new ArrayList<>(Arrays.asList(wrappedLines));
    }

}
