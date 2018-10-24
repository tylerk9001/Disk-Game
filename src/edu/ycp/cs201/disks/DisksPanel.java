package edu.ycp.cs201.disks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class DisksPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 400;
	public static final int HEIGHT = 300;
	
	
	// TODO: add any other fields you need to represent the state of the game
	// Declare a variable for the timer
	private Timer timer;
	private double seconds;
	
	// Initialize the x,y coordinates and radius of the disk.
	private double x = 200.0;
	private double y = 150.0;
	private double radius = 0.0;
	
	// An array of disks
	private Disk[] disks = new Disk[500];
	
	// A counter to count the number of disks placed in the array
	private int diskCount = 0;
	
	// Variables for randomizing the color and radius of the disks
	private Random rand;
	private int randColor;
	private Color color;
	
	// Game over variable
	private boolean gameOver;
	
	
	public DisksPanel() {
		
		// Initialize the radius between 10 and 44
		rand = new Random();
		radius = rand.nextInt(34) + 10;
		
		// Initialize the time to 5 seconds
		seconds = 5;
		
		// Declaring that the game is not over
		gameOver = false;
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.GRAY);
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseClick(e);
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				handleMouseMove(e);
			}
		});
		
		// Schedule timer events to fire every 100 milliseconds (0.1 seconds)
		this.timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleTimerEvent(e);
			}
		});
		
		// Start the timer when the game is loaded
		timer.start();
	}

	// You can call this method to convert a DiskColor value into
	// a java.awt.Color object.
	public Color colorOf(DiskColor dc) {
		return new Color(dc.red(), dc.green(), dc.blue());
	}

	// This method is called whenever the mouse is moved
	protected void handleMouseMove(MouseEvent e) {
		// TODO
		// Get the x and y coordinate of the mouse
		x = e.getX();
		y = e.getY();
		
		// Repaint the screen if the game is not over.
		if (gameOver == false) {
			repaint();
		}
	}
	
	// This method is called whenever the mouse is clicked
	protected void handleMouseClick(MouseEvent e) {
		// TODO
		if (gameOver == false) {
			// Adding a new disk to the array each time mouse is clicked
			diskCount = diskCount + 1;
			randColor = rand.nextInt(15);
			Disk newDisk = new Disk(x, y, radius, DiskColor.values()[randColor]);
			disks[diskCount] = newDisk;
					
		
			
			// Testing to check if the disk is placed out of bounds
			if (disks[diskCount].isOutOfBounds(WIDTH, HEIGHT) == true) {
				// Take the current disk out of the array so that it isnt painted.
				disks[diskCount] = null;
				// Take the disk out of the array if out of bounds.
				diskCount = diskCount - 1;
				// End the game
				gameOver = true;
				// Stop the timer
				timer.stop();
				repaint();
			}
			
			// Testing to check if the disk is overlapping with another disk.
			for (int i = 1; i < diskCount; i++) {
				if (disks[diskCount].overlaps(disks[i]) == true) {
					// Take the current disk out of the array so that it isnt painted.
					disks[diskCount] = null;
					// Take the disk out of the array if overlapped.
					diskCount = diskCount - 1;
					// End the game
					gameOver = true;
					// Stop the timer
					timer.stop();
					
					repaint();
				}
			}
			
			
			// If the game is not over, keep repainting.
			if (gameOver == false) {
				// Generate a new radius
				radius = rand.nextInt(35) + 10;
				// Changes the amount of time 
				seconds = 5 - (0.1 * diskCount);
				repaint();
			}
		}
		
	}

	// This method is called whenever a timer event fires
	protected void handleTimerEvent(ActionEvent e) {
		// TODO
		
		// Changes the time each time by a tenth of second
		seconds = seconds - 0.1;
		
		// If time expires, the game is over
		if (seconds <= 0) {
			gameOver = true;
		}	
		// Repaint to update the timer bar
		repaint();
	}
	
	// Font for the disk counter
	private static final Font FONT = new Font("Dialog", Font.BOLD, 24);
	
	// Disk for the game over screen
	private static final Font gameOverFONT = new Font("Dialog", Font.BOLD, 36);
	
	// This method is called automatically whenever the contents of
	// the window need to be redrawn.
	@Override
	public void paintComponent(Graphics g) {
		// Paint the window background
		super.paintComponent(g);
		// TODO: draw everything that needs to be drawn
		
		// Set outline disk that follows the mouse to black
		g.setColor(Color.BLACK);
		// Fixed disk outline
		if (gameOver == false) {
			g.drawOval((int) (x - radius), (int) (y - radius), (int) (radius * 2), (int) (radius * 2));
		}
		
		
	
		// Drawing and storing each disk in the array.
			for (int i = 0; i < disks.length; i++) {
				if (disks[i] != null && (seconds != 0 || seconds > 0)) {
					double storedRadius = disks[i].getRadius();
					// Setting random color for each disk
					color = colorOf(disks[i].getColor());
					g.setColor(color);
					// Print each disk on the player field
					g.fillOval((int) (disks[i].getX() - storedRadius), (int) (disks[i].getY() - storedRadius), (int) (storedRadius * 2), (int) (storedRadius * 2));
				}
				
				
			// Print the amount of disks placed on the screen (bottom right corner)
			g.setColor(Color.BLACK);
			g.setFont(FONT);
			String counter = Integer.toString(diskCount);
			g.drawString(counter, 325, 240);
				
			// Print the timer bar
			Color barColor = new Color(255, 23, 23, 2);
			g.setColor(barColor);
			g.fillRect(0, 260,(int) (WIDTH * (seconds / 5.0)) , 20);
	
			// Print game over if the game ends
			if (gameOver == true) {
				g.setColor(Color.BLACK);
				g.setFont(gameOverFONT);
				String gameOverMessage = "Game Over!";
				g.drawString(gameOverMessage, 80, 150);
				
			}
			
		}
		
	}
	
}
