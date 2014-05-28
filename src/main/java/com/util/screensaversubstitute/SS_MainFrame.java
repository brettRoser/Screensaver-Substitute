package screensaversubstitute;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class SS_MainFrame extends JFrame implements ActionListener, Runnable {	
  private JButton startMouseMovementButton = 
    new JButton("Start Mouse Movement");
  private JButton stopMouseMovementButton = 
    new JButton("Stop Mouse Movement");
  private JButton exitButton = new JButton("Exit");
	
  private JMenuBar menubar = new JMenuBar();
  private JMenu fileMenu = new JMenu("File");
  private JMenu optionsMenu = new JMenu("Options");
  private JMenuItem setTimerMenuItem = new JMenuItem("Set Timer");
  private JMenuItem exitMenuItem = new JMenuItem("Exit");
	
  private SS_SetTimerFrame timerFrame = new SS_SetTimerFrame();

  private static Dimension screenDimensions =
    Toolkit.getDefaultToolkit().getScreenSize();
  private int buttonCode;
  private Thread actionThread = null;

  public static void main(String[] args) {			
    SS_MainFrame mainFrame = new SS_MainFrame();
		
    int w = mainFrame.getSize().width;
    int h = mainFrame.getSize().height;

    int x = (screenDimensions.width - w) / 2;
    int y = (screenDimensions.height - h) / 4;

    mainFrame.setLocation(x, y);
    mainFrame.setVisible(true);
    mainFrame.setResizable(false);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
	
  public SS_MainFrame() {
    super("Screensaver Substitute v2.0");
    final int FRAME_WIDTH = 440;
    final int FRAME_HEIGHT = 88;
    setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.GREEN.darker());
    buttonPanel.setLayout(new FlowLayout());
		
    buildMenuBar();
    addItemsToActionListener();
		
    buttonPanel.add(startMouseMovementButton);
    buttonPanel.add(stopMouseMovementButton);
    buttonPanel.add(exitButton);

    add(buttonPanel, BorderLayout.CENTER);
  }
	
  /*
   * This method builds the actual menu bar which allows the user to launch
   * the SS_SetTimerFrame class and close the program. The SS_SetTimerFrame
   * class allows the user to enter a value which changes the pause interval
   * which determines how often the mouse will move.
   */
  private void buildMenuBar() {
    exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
      KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		
    fileMenu.add(exitMenuItem);
    optionsMenu.add(setTimerMenuItem);
    menubar.add(fileMenu);
    menubar.add(optionsMenu);
        
    setJMenuBar(menubar);
  }
	
  /*
   * This method adds the various items to the Action Listener.
   */
  private void addItemsToActionListener() {
    exitMenuItem.addActionListener(this);
    setTimerMenuItem.addActionListener(this);
    startMouseMovementButton.addActionListener(this);
    stopMouseMovementButton.addActionListener(this);
    exitButton.addActionListener(this);
  }
	
  /*
   * This method is the required run method of the Runnable interface and
   * contains the code which moves the mouse and fetches the pause interval.
   * This run method is invoked by calling Thread.start().
   */
  @Override
  public void run() {		
    try {
      Robot robot = new Robot();
      while(buttonCode > 0) {
        robot.mouseMove(300, 200);
        Thread.sleep(timerFrame.getPauseValue());
        if(buttonCode == 0) 
          break;
        robot.mouseMove(300, 500);
        Thread.sleep(timerFrame.getPauseValue());
        if(buttonCode == 0) 
          break;
        robot.mouseMove(800, 500);
        Thread.sleep(timerFrame.getPauseValue());
        if(buttonCode == 0) 
          break;
        robot.mouseMove(800, 200);
        Thread.sleep(timerFrame.getPauseValue());
      }
    } catch (InterruptedException ex) {
      Logger.getLogger(SS_MainFrame.class.getName())
        .log(Level.SEVERE, null, ex);
    } catch (AWTException ex) {
      Logger.getLogger(SS_MainFrame.class.getName())
        .log(Level.SEVERE, null, ex);
    }
  }

  /*
   * This method responds to button clicks and performs the appropriate 
   * logic when button clicks are detected.  
   */
  public void actionPerformed(ActionEvent evt) {
    if(evt.getSource().equals(startMouseMovementButton)) {
      actionThread = new Thread(this, "myThread");
      actionThread.start();		
      buttonCode = 1;
    } else if(evt.getSource().equals(stopMouseMovementButton)) {
      buttonCode = 0;
      Thread.currentThread().interrupt();
    } else if(evt.getSource().equals(exitButton)|| 
      evt.getSource().equals(exitMenuItem)) {
      System.exit(0);
    } else if(evt.getSource().equals(setTimerMenuItem)) {			
      int w = timerFrame.getSize().width; 
      int h = timerFrame.getSize().height;

      int x = (screenDimensions.width - w) / 2;
      int y = (screenDimensions.height - h) / 4;

      timerFrame.setLocation(x, y);
      timerFrame.setVisible(true);
      timerFrame.setResizable(false);
      timerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    } else {
      JOptionPane.showMessageDialog(null, "Incorrect Entry! "
        + "Please try again", "Alert", JOptionPane.ERROR_MESSAGE);
    }	
  } // end of actionPerformed method
} // end of SSMainFrame class

