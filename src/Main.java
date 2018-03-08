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
	
	/* constructors */
	
	/**
	 * Creates a main object with default characteristics.
	 */
	public Main()
	{
		numberOfFramesInQueue = 1;
		graphicDisplay = new GraphicDisplay();
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
		BufferedImage img;
		
		try {
        	// read image
        	img = ImageIO.read(
        			new File("C:/Users/thedi/Dropbox/VisionTracking2/src/Images/WIN_20180301_08_22_05_Pro.jpg"));
        	
        	// add image to queue
//        	queue.add(img);
        	
//        	System.out.println(img == null);
        	
        } catch (IOException e) {
        }
	}
	
	
	/* utility */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	} // end of method actionPerformed(ActionEvent e)
	
	
} // end of class Main
