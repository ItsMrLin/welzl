import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	final static int SCREEN_SIZE = 500;
	static Vector<Point2D> boundarySet = new Vector<Point2D>();
	static CircleDouble circle = new CircleDouble(0, 0, 0);
	static Point2D.Double testArray[] = {new Point2D.Double(409.68237674047157, 198.5028016392349),
		new Point2D.Double(108.368466910509, 6.1412859291253845),
		new Point2D.Double(156.73257862031997, 483.06707383975737)};
	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		JFrame frame = new JFrame("Welzl's Algorithm");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// panel
		final JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
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
				Point2D point = new Point2D.Double(random.nextDouble() * SCREEN_SIZE, random.nextDouble() * SCREEN_SIZE);
//				Point2D point = testArray[index];
//				index++;
				panel.getGraphics().setColor(Color.BLACK);
				panel.getGraphics().fillRect((int) point.getX() - 2, (int) point.getY() - 2, 4, 4);
				if (boundarySet.isEmpty()) {
					boundarySet.add(point);
					circle.setCenter(point);
					circle.setRadius(0);
				} else {
					if (boundarySet.size() == 1) {
						circle = new CircleDouble(boundarySet.get(0), point);
						boundarySet.add(point);
					} else {
						if (boundarySet.size() == 2) {
							circle = updatedTwo(boundarySet, point);
						} else {
							if (boundarySet.size() == 3) {
								circle = updatedThree(boundarySet, point);
							} else {
								System.out.println("CASE 4!");
							}
						}
					}
				}
				panel.getGraphics().drawOval((int) (circle.getCenterX() - circle.getRadius()),
						(int) (circle.getCenterY() - circle.getRadius()), 2 * (int) circle.getRadius(), 2 * (int) circle.getRadius());
				panel.getGraphics().setColor(Color.RED);
				//System.out.println(boundarySet);
				for (Point2D point1 : boundarySet) {
					System.out.println(point1+" contains?: "+circle.contains(point1));
					panel.getGraphics().fillRect((int) point1.getX() - 2, (int) point1.getY() - 2, 4, 4);
				}
				System.out.println("----------------------");
			}

		});
		buttonPanel.add(addButton);

		// add point button
		JButton resetButton = new JButton("reset");
		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.repaint();
				boundarySet.clear();
			}

		});
		buttonPanel.add(resetButton);

		frame.pack();
		frame.show();
	}

	// deal with the case where supportSet.size() == 2
	private static CircleDouble updatedTwo(Vector<Point2D> supportSet, Point2D p) {

		Point2D.Double p0 = (Point2D.Double) supportSet.elementAt(0);
		Point2D.Double p1 = (Point2D.Double) supportSet.elementAt(1);
		CircleDouble[] circles = new CircleDouble[2];
		CircleDouble mCir;
		double minRadius = Double.MAX_VALUE;
		int exclusiveIndex = -1;

		// use p-p1 as diameter and check if p0 is inside
		circles[0] = new CircleDouble(p, p1);
		if (circles[0].contains(p0)) {
			minRadius = circles[0].getRadius();
			// p0 is not on the perimeter anymore
			exclusiveIndex = 0;
		}

		// use p-p0 as diameter and check if p1 is inside
		circles[1] = new CircleDouble(p, p0);
		if (circles[1].contains(p1) && circles[1].getRadius() < minRadius) {
			minRadius = circles[1].getRadius();
			// p1 is not on the perimeter anymore
			exclusiveIndex = 1;
		}

		if (exclusiveIndex != -1) {
			mCir = circles[exclusiveIndex];
			// one point in supportSet is replaced by p
			supportSet.set(exclusiveIndex, p);
		} else {
			mCir = new CircleDouble(p0, p1, p);
			// p is on the perimeter, then add p to supportSet
			supportSet.add(p);
		}
		return mCir;
	}

	// deal with the case where supportSet.size() == 3
	private static CircleDouble updatedThree(Vector<Point2D> supportSet, Point2D p) {
		Point2D p0 = supportSet.elementAt(0);
		Point2D p1 = supportSet.elementAt(1);
		Point2D p2 = supportSet.elementAt(2);
		CircleDouble[] circles = new CircleDouble[6];
		CircleDouble mCir;
		double minRadius = Double.MAX_VALUE;
		int exclusiveIndex = -1;

		// construct circle with p-p1-p2 and check if p0 is inside
		circles[0] = new CircleDouble(p, p1, p2);
		if (circles[0].contains(p0)) {
			minRadius = circles[0].getRadius();
			// p0 is not on the perimeter anymore
			exclusiveIndex = 0;
		}

		// construct circle with p-p0-p2 and check if p1 is inside
		circles[1] = new CircleDouble(p, p0, p2);
		if (circles[1].contains(p1) && (circles[1].getRadius() < minRadius)) {
			minRadius = circles[1].getRadius();
			// p1 is not on the perimeter anymore
			exclusiveIndex = 1;
		}

		// construct circle with p-p0-p1 and check if p2 is inside
		circles[2] = new CircleDouble(p, p0, p1);
		if (circles[2].contains(p2) && (circles[2].getRadius() < minRadius)) {
			minRadius = circles[2].getRadius();
			// p2 is not on the perimeter anymore
			exclusiveIndex = 2;
		}

		// use p-p2 as diameter and check if p0 and p1 is inside
		circles[3] = new CircleDouble(p, p2);
		if (circles[3].contains(p0) && circles[3].contains(p1) && (circles[3].getRadius() < minRadius)) {
			minRadius = circles[3].getRadius();
			// p0, p1 are not on the perimeter anymore
			exclusiveIndex = 3;
		}

		// use p-p1 as diameter and check if p0 and p2 is inside
		circles[4] = new CircleDouble(p, p1);
		if (circles[4].contains(p0) && circles[4].contains(p2) && (circles[4].getRadius() < minRadius)) {
			minRadius = circles[4].getRadius();
			// p0, p2 are not on the perimeter anymore
			exclusiveIndex = 4;
		}

		// use p-p0 as diameter and check if p1 and p2 is inside
		circles[5] = new CircleDouble(p, p0);
		if (circles[5].contains(p1) && circles[5].contains(p2) && (circles[5].getRadius() < minRadius)) {
			minRadius = circles[5].getRadius();
			// p1, p2 are not on the perimeter anymore
			exclusiveIndex = 5;
		}

		if (exclusiveIndex != -1) {
			mCir = circles[exclusiveIndex];
		} else {
			mCir = circles[0];
			for (CircleDouble c : circles) {
				if (c.getRadius() > mCir.getRadius()) {
					mCir = c;
				}
			}
		}
		Point2D point;
		switch (exclusiveIndex) {
		case 0:
			supportSet.set(exclusiveIndex, p);
			break;
		case 1:
			supportSet.set(exclusiveIndex, p);
			break;
		case 2:
			supportSet.set(exclusiveIndex, p);
			break;
		case 3:
			// discard the p0 and p1
			point = supportSet.elementAt(2);
			supportSet.setSize(2);
			supportSet.set(0, point);
			supportSet.set(1, p);
			break;
		case 4:
			// discard the p0 and p2
			supportSet.setSize(2);
			supportSet.set(0, p);
			break;
		case 5:
			// discard the p1 and p2
			supportSet.setSize(2);
			supportSet.set(1, p);
			break;
		}

		return mCir;
	}
}
