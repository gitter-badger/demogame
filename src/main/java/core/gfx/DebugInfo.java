package core.gfx;

import core.object.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Debug object to draw debug information on a specific GameObject.
 *
 * @author Frédéric Delorme <frederic.delorme@gmail.com>
 * @since 2019
 */
public class DebugInfo {

    /**
     * display information to an information panel on right of GameObject
     *
     * @param g
     * @param go
     */
    public static void display(Graphics2D g, GameObject go) {
        Font debugFont = g.getFont().deriveFont(8f);
        g.setFont(debugFont);
        FontMetrics fm = g.getFontMetrics(debugFont);

        int maxWidth = 60;
        int maxLinePerColumn = 5;
        int fontHeight = fm.getHeight();
        float offsetX = go.x + go.width + 2;
        float offsetY = go.y;

        java.util.List<String> debugInfo = prepareDebugInfo(go);
        int width = (debugInfo.size() % maxLinePerColumn) * (maxWidth);
        int height = (debugInfo.size() - 1) * (fontHeight - 3);

        drawBackgroundPanel(g,
                offsetX, offsetY - fontHeight,
                width, height,
                Color.DARK_GRAY,
                new Color(0.3f, 0.3f, 0.3f, 0.6f));

        drawAttributesText(
                g, debugInfo,
                offsetX, offsetY,
                maxWidth, maxLinePerColumn, fontHeight,
                Color.WHITE);
    }

    private static List<String> prepareDebugInfo(GameObject go) {
        List<String> debugInfo = new ArrayList<>();
        debugInfo.add(String.format("name:%s", go.name));
        debugInfo.add(String.format("pos:(%03.1f,%03.1f)", go.x, go.y));
        debugInfo.add(String.format("vel:(%03.1f,%03.1f)", go.dx, go.dy));
        debugInfo.add(String.format("debug:%d", go.debugLevel));
        debugInfo.add(String.format("action:%s", go.action.toString()));

        for (Map.Entry<String, Object> e : go.attributes.entrySet()) {
            String debugInfoLine = String.format("%s:%s", e.getKey(), e.getValue().toString());
            debugInfo.add(debugInfoLine);
        }
        return debugInfo;
    }

    private static void drawAttributesText(
            Graphics2D g, List<String> debugInfo,
            float offsetX, float offsetY,
            int maxWidth, int maxLinePerColumn,
            int fontHeight,
            Color textColor) {
        g.setColor(textColor);
        int x = 0, y = 0;
        for (String line : debugInfo) {
            g.drawString(String.format("%s", line),
                    (x + offsetX),
                    (y * (fontHeight + 2)) + offsetY + 4);
            y += 1;
            if (y > maxLinePerColumn) {
                y = 0;
                x += maxWidth + 2;
            }
        }
    }

    private static void drawBackgroundPanel(
            Graphics2D g,
            float offsetX, float offsetY,
            int width, int height,
            Color borderColor, Color backgroundColor) {
        g.setColor(backgroundColor);
        g.fillRect(
                (int) offsetX - 4, (int) (offsetY),
                width, height
        );
        g.setColor(borderColor);
        g.drawRect(
                (int) offsetX - 4,
                (int) (offsetY),
                width,
                height
        );
    }

}