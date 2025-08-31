package org.testingisdocumenting.znai.jupyter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JupyterCellFilter {
    public static List<JupyterCell> fromSection(List<JupyterCell> cells, String sectionName) {
        return fromSection(cells, sectionName, false);
    }
    
    public static List<JupyterCell> fromSection(List<JupyterCell> cells, String sectionName, boolean excludeSectionTitle) {
        int startIdx = -1;
        int endIdx = cells.size();
        
        for (int i = 0; i < cells.size(); i++) {
            JupyterCell cell = cells.get(i);
            if (cell.getType().equals(JupyterCell.MARKDOWN_TYPE) && 
                cell.getInput() != null) {
                if (firstLineTrimmed(cell.getInput()).matches("^#+\\s+" + java.util.regex.Pattern.quote(sectionName))) {
                    startIdx = i;
                    break;
                }
            }
        }
        
        if (startIdx == -1) {
            return new ArrayList<>();
        }
        
        for (int i = startIdx + 1; i < cells.size(); i++) {
            JupyterCell cell = cells.get(i);
            if (cell.getType().equals(JupyterCell.MARKDOWN_TYPE) && 
                cell.getInput() != null) {
                if (firstLineTrimmed(cell.getInput()).matches("^#+ .*")) {
                    endIdx = i;
                    break;
                }
            }
        }
        
        List<JupyterCell> sectionCells = new ArrayList<>();
        for (int i = startIdx; i < endIdx; i++) {
            sectionCells.add(cells.get(i));
        }

        return excludeSectionTitle ?
                excludeSectionHeader(sectionCells):
                sectionCells;
    }

    private static String firstLineTrimmed(String input) {
        return input.trim().contains("\n") ? input.substring(0, input.indexOf("\n")) : input;
    }

    private static String stripFirstSectionHeader(String content) {
        String[] lines = content.split("\\n");
        int firstMatchIdx = 0;
        for (String line : lines) {
            if (line.strip().startsWith("#")) {
                break;
            }

            firstMatchIdx++;
        }

        return Arrays.stream(lines).skip(firstMatchIdx + 1).collect(Collectors.joining("\n")).trim();
    }
    
    private static List<JupyterCell> excludeSectionHeader(List<JupyterCell> cells) {
        if (cells.isEmpty()) {
            return cells;
        }

        List<JupyterCell> result = new ArrayList<>();
        JupyterCell firstCell = cells.get(0);
        
        if (firstCell.getType().equals(JupyterCell.MARKDOWN_TYPE) && firstCell.getInput() != null) {
            String markdown = firstCell.getInput();
            String stripped = stripFirstSectionHeader(markdown);
            if (!stripped.isEmpty()) {
                result.add(new JupyterCell(firstCell.getType(), stripped, firstCell.getOutputs()));
            }
        } else {
            result.add(firstCell);
        }

        result.addAll(cells.subList(1, cells.size()));
        return result;
    }
}
