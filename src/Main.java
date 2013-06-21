import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	final static int SCREEN_SIZE = 500;
	static Vector<Point2D> boundarySet = new Vector<Point2D>();
	static Vector<Point2D> pointSet = new Vector<Point2D>();
	static CircleDouble circle = new CircleDouble(0, 0, 0);
	static Point2D.Double testArray[] = { new Point2D.Double(90.37377482652592, 464.61414240308363),
			new Point2D.Double(171.59870451429614, 104.96950450247822), new Point2D.Double(10.4172244332896202, 393.31807326959074),
			new Point2D.Double(485.6295633967431, 273.60459813672486) };

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		JFrame frame = new JFrame("Welzl's Algorithm");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// panel
		final JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
		frame.add(panel);

		JPanel buttonPanel = new JPanel();
		frame.add(buttonPanel, BorderLayout.SOUTH);
		// add point button
		JButton addButton = new JButton("add point");
		addButton.addActionListener(new ActionListener() {
			Random random = new Random();
			int index = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				// Point2D point = testArray[index];
				// index++;
				Point2D point = new Point2D.Double(random.nextDouble() * SCREEN_SIZE, random.nextDouble() * SCREEN_SIZE);
				panel.getGraphics().clearRect(0, 0, SCREEN_SIZE, SCREEN_SIZE);
				panel.getGraphics().setColor(Color.BLACK);
				pointSet.add(point);
				for (Point2D point1 : pointSet) {
					panel.getGraphics().fillRect((int) point1.getX() - 2, (int) point1.getY() - 2, 4, 4);
				}
				circle = mec(pointSet, pointSet.size() - 1);
				panel.getGraphics().drawOval((int) (circle.getCenterX() - circle.getRadius()),
						(int) (circle.getCenterY() - circle.getRadius()), 2 * (int) circle.getRadius(), 2 * (int) circle.getRadius());

				System.out.println("new Point: " + point);
				// System.out.println(boundarySet);
				for (Point2D point1 : boundarySet) {
					System.out.println(point1 + " contains?: " + circle.contains(point1));
					panel.getGraphics().fillRect((int) point1.getX() - 2, (int) point1.getY() - 2, 4, 4);
				}
				System.out.println("----------------------");
			}

			private CircleDouble mec(Vector<Point2D> pointSet, int maxId) {
				CircleDouble retCircle = null;
				if (maxId < 0) {
					retCircle = new CircleDouble(0, 0, 0);
				} else {
					Point2D point = pointSet.elementAt(maxId);
					retCircle = mec(pointSet, maxId - 1);
					if (!retCircle.contains(point)) {
						Vector<Point2D> boundarySet = new Vector<Point2D>();
						boundarySet.add(point);
						retCircle = bMec(pointSet, maxId - 1, boundarySet);
					}
				}
				return retCircle;
			}

			private CircleDouble bMec(Vector<Point2D> pointSet, int maxId, Vector<Point2D> boundarySet) {
				CircleDouble retCircle = new CircleDouble(0, 0, 0);
				if (maxId < 0 || boundarySet.size() == 3) {
					if (boundarySet.size() == 1) {
						retCircle = new CircleDouble(boundarySet.elementAt(0));
					} else {
						if (boundarySet.size() == 2) {
							retCircle = new CircleDouble(boundarySet.elementAt(0), boundarySet.elementAt(1));
						} else {
							if (boundarySet.size() == 3) {
								retCircle = new CircleDouble(boundarySet.elementAt(0), boundarySet.elementAt(1), boundarySet.elementAt(2));
							}
						}
					}
				} else {
					Point2D point = pointSet.elementAt(maxId);
					retCircle = bMec(pointSet, maxId - 1, boundarySet);
					if (!retCircle.contains(point)) {
						boundarySet.add(point);
						retCircle = bMec(pointSet, maxId - 1, boundarySet);
						boundarySet.remove(point);
					}
				}
				return retCircle;
			}
		});
		buttonPanel.add(addButton);

		// add point button
		JButton resetButton = new JButton("reset");
		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.repaint();
				pointSet.clear();
				boundarySet.clear();
			}

		});
		buttonPanel.add(resetButton);

		frame.pack();
		frame.show();
	}
}
