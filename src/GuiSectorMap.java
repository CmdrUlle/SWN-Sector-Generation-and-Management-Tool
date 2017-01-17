import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

public class GuiSectorMap extends JPanel implements MouseListener {
	// private JButton button1 = new JButton("Refresh Sector Map");
	private DatenbankMain db;
	private Map<String, String> savedSectors;
	private String hightlightSector = "";
	public String highlightHex = "";
	private Map<Polygon, String> arrPolygon;

	private int map_width;
	private int map_height;

	public GuiSectorMap() {
		super();
		db = new DatenbankMain();

		refreshSectorData();
		// for (Map.Entry<String, String> entry : this.savedSectors.entrySet()) {
		// System.out.println(entry.getKey() + " " + entry.getValue());
		// }
		setPreferredSize(new Dimension(530, 400));
		init();
		this.repaint();
	}

	private void init() {
		this.setLayout(null);
		// GridBagConstraints c = new GridBagConstraints();

		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.anchor = GridBagConstraints.PAGE_END;

	}

	private void refreshSectorData() {
		map_width = 0;
		map_height = 0;
		int width = 0;
		int height = 0;

		this.savedSectors = new HashMap<String, String>();
		String[][] results = db.searchSectors("");
		if (results != null && results.length != 0) {
			for (String[] helper : results) {
				this.savedSectors.put(helper[1], helper[0]);
				System.out.println(helper[0]);
				width = Integer.parseInt(helper[1].substring(0, 2));
				System.out.println("widht: " + width);
				height = Integer.parseInt(helper[1].substring(2, 4));
				System.out.println("height: " + height);
				
				map_width = (width > map_width) ? width : map_width;
				map_height = (height > map_height) ? height : map_height;
			}
		}
		map_width++; 
		map_height++; 
		
		this.repaint();

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);

		int offset = 0;

		//map_width = 13;
		//map_height = 18;
		// Tested Values
		// 9-12:4.5
		// 10-13:4
		// 11-15:3.5
		// 13-18:3
		// 16-21:2.5
		// 20-27:2
		// 26-36:1.5
		//double scaleFactor = 1.5;
		double scaleFactorWidth = 0.0102793 * map_width * map_width - 0.522113 * map_width + 8.18405;
		double scaleFactorHeight = 0.00542356 * map_height * map_height - 0.375689 * map_height + 8.04261;
		
		double scaleFactor = (scaleFactorWidth < scaleFactorHeight)?scaleFactorWidth:scaleFactorHeight;
		// 0.0102793 x^2-0.522113 x+8.18405 width
		// height

		int finalOffset = (int) (7 * scaleFactor);
		int finalYOffset = (int) (7 * scaleFactor * 2);
		int finalXOffset = (int) (12 * scaleFactor);
		String curHex;
		arrPolygon = new HashMap<Polygon, String>();

		for (int ix = 0; ix < map_width; ix++) {
			for (int iy = 0; iy < map_height; iy++) {
				if (iy < 10) {
					if (ix < 10) {
						curHex = "0" + ix + "0" + iy;
					} else {
						curHex = ix + "0" + iy;
					}
				} else {
					if (ix < 10) {
						curHex = "0" + ix + iy;
					} else {
						curHex = ix + "" + iy;
					}
				}
				if (ix % 2 == 1) {
					offset = finalOffset;
					// g.setColor(Color.RED);
				} else {
					offset = 0;
					// g.setColor(Color.BLACK);
				}

				Polygon polygon = this.drawHoneycombExplicit(ix * finalXOffset + 50, iy * finalYOffset + 50 + offset, scaleFactor);
				arrPolygon.put(polygon, curHex);

				if (this.hightlightSector.equals(curHex)) {
					g.setColor(Color.WHITE);
					g.fillPolygon(polygon);
				}
				g.setColor(Color.BLACK);
				g.drawPolygon(polygon);
				if (this.savedSectors.containsKey(curHex)) {
					String string = this.savedSectors.get(curHex);
					g.setColor(Color.BLUE);
					// g.drawString(this.savedSectors.get(curHex), ix*finalXOffset+50+13, iy*finalYOffset+50+offset+10);
					int stringLen = (int) g.getFontMetrics().getStringBounds(string, g).getWidth();
					g.drawString(string, ix * finalXOffset + 50 - stringLen / 2, iy * finalYOffset + 50 + offset + 10);
				}

				g.setColor(Color.GRAY);
				g.drawString(curHex, ix * finalXOffset + 50 - 13, iy * finalYOffset + 50 + offset - 10);

			}
		}
	}

	// private Polygon drawHoneycomb(int posX, int posY, int width, int height, double ratio) {
	// Polygon hexagon = new Polygon();
	// for (int i = 0; i < 6; i++) {
	// int x = width / 2 + (int) ((width - 2) / 2 * Math.cos(i * 2 * Math.PI / 6) * ratio);
	// int y = height / 2 + (int) ((height - 2) / 2 * Math.sin(i * 2 * Math.PI / 6) * ratio);
	// hexagon.addPoint((x + posX), (y + posY));
	// }
	// return hexagon;
	// }

	private Polygon drawHoneycombExplicit(int posX, int posY, double sf) {
		Polygon hexagon = new Polygon();
		hexagon.addPoint((int) (4 * sf + posX), (int) (7 * sf + posY));
		hexagon.addPoint((int) (8 * sf + posX), (int) (posY));
		hexagon.addPoint((int) (4 * sf + posX), (int) (-7 * sf + posY));
		hexagon.addPoint((int) (-4 * sf + posX), (int) (-7 * sf + posY));
		hexagon.addPoint((int) (-8 * sf + posX), (int) (posY));
		hexagon.addPoint((int) (-4 * sf + posX), (int) (7 * sf + posY));
		return hexagon;
	}

	public void highlightSector(String hex) {
		this.hightlightSector = hex;
		this.repaint();
	}

	private String findHex(Point point) {
		for (Entry<Polygon, String> entry : this.arrPolygon.entrySet()) {
			if (entry.getKey().contains(point)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		this.highlightHex = this.findHex(evt.getPoint());
		System.out.println("HexHexhex:" + highlightHex);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
