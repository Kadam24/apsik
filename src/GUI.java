import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class containing GUI: board + buttons
 */
public class GUI extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private Board board;
	private JButton start;
	private JButton clear;
	private JFrame frame;
	private int iterNum = 0;
	private final int maxDelay = 500;
	private final int initDelay = 5;
	private boolean running = false;

	public GUI(Program jf) {
		frame = jf;
		timer = new Timer(initDelay, this);
		timer.stop();
	}

	/**
	 * @param container to which GUI and board is added
	 */
	public void initialize(Container container) {
		container.setLayout(new BorderLayout());
		container.setSize(new Dimension(1000, 700));

		JPanel buttonPanel = new JPanel();

		start = new JButton("Start");
		start.setActionCommand("Start");
		start.addActionListener(this);

		clear = new JButton("Clear");
		clear.setActionCommand("clear");
		clear.addActionListener(this);

		//pred.setMinimum(0);
		//pred.setMaximum(maxDelay);
		//pred.addChangeListener(this);
		//pred.setValue(maxDelay - timer.getDelay());

		buttonPanel.add(start);
		buttonPanel.add(clear);
		//buttonPanel.add(pred);

		board = new Board(1000, 700 - buttonPanel.getHeight());
		container.add(board, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * handles clicking on each button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		int iterPrint;
		String pora;
		if (e.getSource().equals(timer)) {
			iterNum++;
			iterPrint = (int) Math.floor(iterNum/10);
			if (iterPrint % 365 < 31+28) {
				pora = "zima";
			} else if (iterPrint % 365 < 59+92) {
				pora = "wiosna";
			} else if (iterPrint % 365 < 151+92) {
				pora = "lato";
			} else if (iterPrint % 365 < 243+91) {
				pora = "jesień";
			} else {
				pora = "zima";
			}
			frame.setTitle(pora + ": (" + Integer.toString(iterPrint+1) + " iteration)");
			board.iteration();
		} else {
			String command = e.getActionCommand();
			if (command.equals("Start")) {
				if (!running) {
					timer.start();
					start.setText("Pause");
				} else {
					timer.stop();
					start.setText("Start");
				}
				running = !running;
				clear.setEnabled(true);

			}
			else if (command.equals("clear")) {
				iterNum = 0;
				timer.stop();
				start.setEnabled(true);
				board.clear();
				frame.setTitle("Cellular Automata Toolbox");
			}

		}
	}

	/**
	 * slider to control simulation speed
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		//timer.setDelay(maxDelay - pred.getValue());
	}
}
