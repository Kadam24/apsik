import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

/**
 * Board with Points that may be expanded (with automatic change of cell
 * number) with mouse event listener
 */

public class Board extends JComponent implements MouseInputListener, ComponentListener {
	//private static final long serialVersionUID = 1L;
	private Point[][] points;
	private int size = 5;

	public Board(int length, int height) {
		addMouseListener(this);
		addComponentListener(this);
		addMouseMotionListener(this);
		setBackground(Color.WHITE);
		setOpaque(true);
	}

	// single iteration
	public void iteration() {
		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y)
				points[x][y].calculateNewState();

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y)
				points[x][y].changeState();
		this.repaint();
	}

	// clearing board
	public void clear() {
		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y) {
				points[x][y].setState(0);
			}
		this.repaint();
	}

	private void initialize(int length, int height) {
		points = new Point[length][height];
		boolean tmp = false;
		int num1 = 0;
		int num2 = 0;

		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y) {
				points[x][y] = new Point();
				//System.out.println(x + " " + y);
			}

		for (int x = 0; x < points.length; ++x) {
			for (int y = 0; y < points[x].length; ++y) {
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						if ((x + i > 0 && x + i < points.length - 1) && (y + j > 0 && y + j < points[x].length - 1) && !(i == 0 && j == 0) && (i == 0 || j == 0))
							points[x][y].addNeighbor(points[x + i][y + j]);
					}
				}
			}
		}
		//int liczniczek = 0;
		points[50][30].setState(2);
		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y) {
				while (points[x][y].getCountDistant() < 1) {
					do {
						Random random = new Random();
						num1 = random.nextInt(points.length);
						Random random2 = new Random();
						num2 = random2.nextInt(points[x].length);
						tmp = points[num1][num2].addDistantFriend(points[x][y]);
						//System.out.println(x + " " + y);
					} while (tmp == false);
					points[x][y].addDistantFriend(points[num1][num2]);
				}
				//liczniczek ++;
				//System.out.println(liczniczek);
			}


		for (int x = 0; x < points.length; ++x)
			for (int y = 0; y < points[x].length; ++y) {
				while (points[x][y].getCountClose() < 2) {
					do {
						do {
							Random random = new Random();
							num1 = x + random.nextInt(8) - 4;
							Random random2 = new Random();
							num2 = y + random2.nextInt(8) - 4;
						} while (num1 < 0 || num1 > points.length - 1 || num2 < 0 || num2 > points[x].length - 1);
						tmp = points[num1][num2].addCloseFriend(points[x][y]);
						System.out.println(x + " " + y);
					} while (tmp == false);
					points[x][y].addCloseFriend(points[num1][num2]);
				}
				//liczniczek ++;
				//System.out.println(liczniczek);
			}
	}

	//paint background and separators between cells
	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		g.setColor(Color.GRAY);
		drawNetting(g, size);
	}

	// draws the background netting
	private void drawNetting(Graphics g, int gridSpace) {
		Insets insets = getInsets();
		int firstX = insets.left;
		int firstY = insets.top;
		int lastX = this.getWidth() - insets.right;
		int lastY = this.getHeight() - insets.bottom;

		int x = firstX;
		while (x < lastX) {
			g.drawLine(x, firstY, x, lastY);
			x += gridSpace;
		}

		int y = firstY;
		while (y < lastY) {
			g.drawLine(firstX, y, lastX, y);
			y += gridSpace;
		}

		for (x = 0; x < points.length; ++x) {
			for (y = 0; y < points[x].length; ++y) {
				//if (points[x][y].getState() != 0) {
				switch (points[x][y].getState()) {
					case 0:
						g.setColor(new Color(0x3c9a50));
						if (points[x][y].getAge() < 2) g.setColor(new Color(0x6ff299));
						else if (points[x][y].getAge() > 2) g.setColor(new Color(0x2f6936));
						break;
					case 1:
						g.setColor(new Color(0xff1e1e));
						if (points[x][y].getAge() < 2) g.setColor(new Color(0xff8181));
						else if (points[x][y].getAge() > 2) g.setColor(new Color(0x78191F));
						break;
					case 2:
						g.setColor(new Color(0xff));
						if (points[x][y].getAge() < 2) g.setColor(new Color(0x00ffff));
						else if (points[x][y].getAge() > 2) g.setColor(new Color(0x0f0f44));
						break;
					case 3:
						g.setColor(new Color(0x3c9a50));
						if (points[x][y].getAge() < 2) g.setColor(new Color(0x6ff299));
						else if (points[x][y].getAge() > 2) g.setColor(new Color(0x2f6936));
						break;
				}
				g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
			}
		}
	}


	public void mouseClicked(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			points[x][y].clicked();
			this.repaint();
		}
	}

	public void componentResized(ComponentEvent e) {
		int dlugosc = (this.getWidth() / size) + 1;
		int wysokosc = (this.getHeight() / size) + 1;
		initialize(dlugosc, wysokosc);
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX() / size;
		int y = e.getY() / size;
		if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
			points[x][y].setState(1);
			this.repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

}
