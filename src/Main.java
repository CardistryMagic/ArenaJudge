import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * An object to overlook the operations of the arena judge
 * 
 * @author thedi
 *
 */
public class Main implements ActionListener 
{
	/* instance fields */
	private int numberOfFramesInQueue;
	private GraphicDisplay graphicDisplay;
	private final BufferedImage loadingFrame;
	// create structure
    private final JFrame frame;
    private final JPanel panel;
	
	/* constructors */
	
	/**
	 * Creates a main object with default characteristics.
	 */
	public Main()
	{
		numberOfFramesInQueue = 1;
		graphicDisplay = new GraphicDisplay();
		frame = new JFrame();
	    panel = new JPanel();
	} // end of class Main()
	
	/**
	 * Creates a main object to overlook the arena judge with the specified number of frames in the queue.
	 * 
	 * @param numberOfFramesInQueue number of frames to be in the queue at any given time; must be greater than 1
	 */
	public Main(int numberOfFramesInQueue)
	{
		if(numberOfFramesInQueue >= 1)
		{
			this.numberOfFramesInQueue = numberOfFramesInQueue;
		}
		else
		{
			this.numberOfFramesInQueue = 1;
		} // end of if(numberOfFramesInQueue >= 1)
		
		graphicDisplay = new GraphicDisplay();
		frame = new JFrame();
	    panel = new JPanel();
	} // end of constructor Main(int numberOfFramesInQueue)
	
	/**
	 * Creates a main object to overlook the arena judge with the specified graphic display.
	 * 
	 * @param graphicDisplay graphic display; cannot be null
	 */
	public Main(GraphicDisplay graphicDisplay)
	{
		if (graphicDisplay != null)
		{
			this.graphicDisplay = graphicDisplay;
		}
		else
		{
			this.graphicDisplay = new GraphicDisplay();
		} // end of (graphicDisplay != null)
		
		numberOfFramesInQueue = 1;
		frame = new JFrame();
	    panel = new JPanel();
	} // end of Main(GraphicDisplay graphicDisplay)
	
	/**
	 * Creates a main object to overlook the arena judge with the specified characteristics.
	 * 
	 * @param numberOfFramesInQueue number of frames in the queue at any given moment; must be greater than 1
	 * @param graphicDisplay graphic display
	 */
	public Main(int numberOfFramesInQueue, GraphicDisplay graphicDisplay)
	{
		if (graphicDisplay != null)
		{
			this.graphicDisplay = graphicDisplay;
		}
		else
		{
			this.graphicDisplay = new GraphicDisplay();
		} // end of (graphicDisplay != null)
		
		if(numberOfFramesInQueue >= 1)
		{
			this.numberOfFramesInQueue = numberOfFramesInQueue;
		}
		else
		{
			this.numberOfFramesInQueue = 1;
		} // end of if(numberOfFramesInQueue >= 1)
		
		frame = new JFrame();
	    panel = new JPanel();
	} // end of Main(int numberOfFramesInQueue, GraphicDisplay graphicDisplay)
	
	/* accessors */
	
	/**
	 * Returns the number of frames in the frame buffer at any given moment in time.
	 * 
	 * @return numberOfFramesInQueue number of frames in the frame buffer at any given moment in time
	 */
	public int getNumberOfBufferFrames()
	{
		return numberOfFramesInQueue;
	} // end of method getNumberOfBufferFrames()
	
	/**
	 * Returns the graphic display.
	 * 
	 * @return graphicDisplay graphic display
	 */
	public GraphicDisplay getGraphicDisplay()
	{
		return graphicDisplay;
	} // end of getGraphicDisplay()

	/* mutators */
	private void createGUI()
	{
		try 
		{
        	// read image
			loadingFrame = ImageIO.read(
        			new File("C:/Users/thedi/Dropbox/VisionTracking2/src/Images/WIN_20180301_08_22_05_Pro.jpg"));
        	
        }
		catch (IOException e)
		{
			loadingFrame = null;
        } // end of try
		
		Queue<BufferedImage> frameBuffer = new LinkedList<BufferedImage>();
		
		for(int i = 0; i < numberOfFramesInQueue; i++)
		{
			frameBuffer.add(loadingFrame);
		} // end of for(int i = 0; i < numberOfFramesInQueue; i++)
		
		// initialize GUI
		panel.setLayout(new BorderLayout());
      	graphicDisplay = new GraphicsDisplay(img.getWidth(), img.getHeight(), queue);
        panel.add(graphicDisplay, BorderLayout.CENTER);
        
        // initialize mouse adapter
        MouseAdapter mouseAdapter = new MouseAdapter()
        {
        	public void mouseClicked(MouseEvent e)
        	{
            	if (graphicDisplay.isCurrentlyCalibrating())
            	{
            		int currentMouseX = e.getX();
            		int currentMouseY = e.getY();
            		
            		if(!graphicDisplay.isCoordOneSet())
            		{
            			
            			int[] mouseCoordinates = {currentMouseX, currentMouseY};
            			graphicDisplay.setCoordOne(currentMouseY);
            		} // end of if(!graphicDisplay.isCoordOneSet())
            		else if (!graphicDisplay.isCoordTwoSet()) 
            		{
            			int[] arr = {x,y};
            			graphicDisplay.setCoordTwo(arr);
            			graphicDisplay.stopCalibration();
            		} // end of if(!graphicDisplay.isCoordTwoSet())
            	} // end of if (graphicDisplay.isCurrentlyCalibrating())
            } // end of method mouseClicked(MouseEvent e)
        };
        
        // add mouse listener to graphics panel
        panel.addMouseListener(mouseAdapter);

        // initialize calibrate button characteristics
        JButton calibrationButton = new JButton("Calibration");
        calibrationButton.setActionCommand("calibrate");
        calibrationButton.addActionListener(this);
        panel.add(calibrationButton, BorderLayout.SOUTH);
        
        // add all components to graphics frame
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
	}
	
	
	/* utility */
	@Override
	public void actionPerformed(ActionEvent e) {
		int[] placeholderMouseCoordinates = {-1, -1};
		graphicDisplay.setCoordOne(placeholderMouseCoordinates);
		graphicDisplay.setCoordTwo(placeholderMouseCoordinates);
		graphicDisplay.calibrate();
	} // end of method actionPerformed(ActionEvent e)
	
	public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Main ArenaJudgerGUI = new Main();
                ArenaJudgerGUI.createGUI();
            }
        });
    }
} // end of class Main
