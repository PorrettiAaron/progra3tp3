package view;

import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import org.openstreetmap.gui.jmapviewer.Coordinate;

public class MapMarkerDotWithLabel extends MapMarkerDot {
    private String label;

    public MapMarkerDotWithLabel(Coordinate coord, String label) {
        super(null, null, coord);
        this.label = label;
    }

    @Override
    public void paint(Graphics g, Point position, int radio) {
        super.paint(g, position, radio);
        if (label != null) {
            Rectangle rectangle = g.getFontMetrics().getStringBounds(label, g).getBounds();
            int labelX = position.x - rectangle.width / 2;
            int labelY = position.y - radio - 5;
            g.drawString(label, labelX, labelY);
        }
    }
}
