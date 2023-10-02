package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class WelcomeScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	public WelcomeScreen() {
		
        // Set up the JFrame properties
        setTitle("15 Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,300);
        setLocationRelativeTo(null); // Center the window
        
        // Load the background image
        ImageIcon backgroundImage = new ImageIcon(this.getClass().getResource("/resource/image3.png"));

        // Create a custom panel for the background image
        JPanel backgroundPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        }; 

        // Set layout for the background panel
        backgroundPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20,20));
        
        // Create buttons
        JButton playButton = new JButton("Play Game"); 
        JButton createButton = new JButton("Custom your board");
        
        // Button listeners
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle "Play Game" button click
            	 dispose(); // Close the current welcome screen
                 
                 // Create and show the game frame
                 SwingUtilities.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                         new NonCustomizedPuzzle(); // Create and display the game frame
                     }
                 });
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle "Create Your Own Board" button click
            	// Handle "Play Game" button click
           	 	dispose(); // Close the current welcome screen
                
                // Create and show the game frame
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new CustomizedPuzzle(); // Create and display the game frame
                    }
                });
            }
        });

        // Add buttons to the panel
        backgroundPanel.add(playButton);
        backgroundPanel.add(createButton);

        // Set layout for the main frame
        setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER); // Add the background panel to the center
        
        setVisible(true);
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WelcomeScreen();
            }
        });
    }

}
