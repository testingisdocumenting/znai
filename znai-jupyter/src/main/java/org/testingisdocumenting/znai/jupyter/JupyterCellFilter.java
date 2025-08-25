package org.testingisdocumenting.znai.jupyter;

import java.util.ArrayList;
import java.util.List;

public class JupyterCellFilter {
    public static List<JupyterCell> fromSection(List<JupyterCell> cells, String sectionName) {
        int startIdx = -1;
        int endIdx = cells.size();
        
        for (int i = 0; i < cells.size(); i++) {
            JupyterCell cell = cells.get(i);
            if (cell.getType().equals(JupyterCell.MARKDOWN_TYPE) && 
                cell.getInput() != null && 
                cell.getInput().trim().matches("^#+\\s+" + java.util.regex.Pattern.quote(sectionName) + ".*")) {
                startIdx = i;
                break;
            }
        }
        
        if (startIdx == -1) {
            return new ArrayList<>();
        }
        
        for (int i = startIdx + 1; i < cells.size(); i++) {
            JupyterCell cell = cells.get(i);
            if (cell.getType().equals(JupyterCell.MARKDOWN_TYPE) && 
                cell.getInput() != null && 
                cell.getInput().trim().matches("^#+ .*")) {
                endIdx = i;
                break;
            }
        }
        
        List<JupyterCell> sectionCells = new ArrayList<>();
        for (int i = startIdx; i < endIdx; i++) {
            sectionCells.add(cells.get(i));
        }
        
        return sectionCells;
    }
}
