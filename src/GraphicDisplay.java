import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Queue;
import java.awt.image.DataBufferByte;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.Core;
import org.opencv.core.CvType;

/**
 * An object to display the output from the arena judge.
 * 
 * @author thedi
 *
 */
public class GraphicDisplay extends JComponent implements ActionListener 
{
	/* instance fields */
	private int animationDelay = 30;
	private Timer animationTimer;
	private BufferedImage calibrationOverlay;
	private int calibrationX1;
	private int calibrationX2;
	private int calibrationY1;
	private int calibrationY2;
	private int currentFrameBufferID = 0;
	private BufferedImage currentVideoCaptureFrame;
	private Queue<BufferedImage> frameBuffer;
//	private VisionProcessor visionProcessor = new VisionProcessor();
	private boolean isCalibrated = false;
	private boolean isCurrentlyCalibrating = false;
	private BufferedImage recordingOverlay;
	private int numberOfFramesInQueue = 1;
	private double[] rgbThresholdRed = {80, 255.0};
	private double[] rgbThresholdGreen = {0.0, 110};
	private double[] rgbThresholdBlue = {0.0, 110};
	private static final long serialVersionUID = 1L;
	private boolean showCalibrationFrame = false;
	private WebcamCapture webcamCapture;
	
	/* constructors */
	
	/**
	 * Creates a graphic display with specified characteristics.
	 * 
	 * @param frameWidth width of the webcam frames
	 * @param frameHeight height of the webcam frames
	 * @param frameBuffer initial frame buffer 
	 */
	public GraphicDisplay(int frameWidth, int frameHeight, Queue<BufferedImage> frameBuffer)
	{
		// load open cv libraries
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        this.frameBuffer = frameBuffer;
        
        // initialize current frame
    	currentVideoCaptureFrame = this.frameBuffer.remove();
    	
    	webcamCapture = new WebcamCapture(0);
        
        try {
        	// read recording frame image
        	recordingOverlay = ImageIO.read(new File("C:/Users/thedi/Dropbox/VisionTracking2/src/Images/RecordingFrame.png"));
        	
        } catch (IOException e) {
        }
        
        try {
        	// read recording frame image
        	calibrationOverlay = ImageIO.read(new File("C:/Users/thedi/Dropbox/VisionTracking2/src/Images/CalibratingFrame.png"));
        	
        } catch (IOException e) {
        }

        // set preferred size for frame
        setPreferredSize(new Dimension(frameWidth + 20, frameHeight + 20));
        
        // start animation
        startAnimation();
	} // end of GraphicDisplay(int frameWidth, int frameHeight, Queue<BufferedImage> frameBuffer)

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String formatted = String.format("%03d", currentFrameBufferID);
		
		// update GUI
		repaint();
		
		if (webcamCapture.getCurrentFrameMat() != null)
		{
			// process current frame
//			videoProcessor.process(webcamCapture.getCurrentFrameMat(), getRedRGBThreshold(), 
//					getBlueRGBThreshold(), getGreenRGBThreshold());
			
			// add current frame to frame buffer
			frameBuffer.add(webcamCapture.getCurrentFrameBufferedImage());
		}
		
		// update current frame number
		currentFrameBufferID = (currentFrameBufferID + 1) % numberOfFramesInQueue;
		
		BufferedImage currentDisplayFrame = frameBuffer.remove();
		
		setCurrentFrame(currentDisplayFrame);
	}
	
	/* accessors */
	
	/**
	 * 
	 * @return
	 */
	public boolean isCurrentlyCalibrating()
	{
		return this.isCurrentlyCalibrating;
	} // end of method isCurrentlyCalibrating()
	
	/**
	 * Returns the first set of calibration coordinates.
	 * 
	 * @return first set of calibration coordinates
	 */
	public int[] getCoordOne()
	{
		int[] arr = {calibrationX1, calibrationY1};
		return arr;
	} // end of method getCoordOne()
	
	/**
	 * Returns the second set of calibration coordinates.
	 * 
	 * @return second set of calibration coordinates
	 */
	public int[] getCoordTwo()
	{
		int[] arr = {calibrationX2, calibrationY2};
		return arr;
	} // end of method getCoordTwo()
	
	/**
	 * Returns if the first set of calibration coordinates have been initialized.
	 * 
	 * @return if the first set of calibration coordinates have been initialized
	 */
	public boolean isCoordOneSet()
	{
		return calibrationX1 != -1 && calibrationY1 != -1;
	} // end of method isCoordOneSet()
	
	/**
	 * Returns if the second set of calibration coordinates have been initialized.
	 * 
	 * @return if the second set of calibration coordinates have been initialized
	 */
	public boolean isCoordTwoSet()
	{
		return calibrationX2 != -1 && calibrationY2 != -1;
	} // end of method isCoordTwoSet()
	
	/**
	 * Returns the red RGB calibration threshold values.
	 * 
	 * @return red RGB calibration threshold values
	 */
	public double[] getRedRGBThreshold()
	{
		return rgbThresholdRed;
	} // end of method getRedRGBThreshold()
	
	/**
	 * Returns the green RGB calibration threshold values.
	 * 
	 * @return green RGB calibration threshold values
	 */
	public double[] getGreenRGBThreshold()
	{
		return rgbThresholdGreen;
	} // end of method getGreenRGBThreshold()
	
	/**
	 * Returns the blue RGB calibration threshold values.
	 * 
	 * @return blue RGB calibration threshold values
	 */
	public double[] getBlueRGBThreshold()
	{
		return rgbThresholdBlue;
	} // end of method getBlueRGBThreshold()
	
	/* mutators */

	/**
	 * Set first set of calibration coordinates.
	 * 
	 * @param arr first set of calibration coordinates
	 */
	public void setCoordOne(int[] arr)
	{
		calibrationX1 = arr[0];
		calibrationY1 = arr[1];
	} // end of method setCoordOne(int[] arr)
	
	/**
	 * Set second set of calibration coordinates.
	 * 
	 * @param arr second set of calibration coordinates
	 */
	public void setCoordTwo(int[] arr)
	{
		calibrationX2 = arr[0];
		calibrationY2 = arr[1];
	} // end of method setCoordTwo(int[] arr)
	
	/**
	 * Starts the calibration process.
	 */
	public void startCalibration()
	{
		isCurrentlyCalibrating = true;
	} // end of method startCalibration()
	
	/**
	 * Ends the calibration process.
	 */
	public void stopCalibration()
	{
		isCurrentlyCalibrating = false;
		
		Robot robot;
		try
		{
			robot = new Robot();
			BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			screenShot = cropImage(screenShot, this.getCoordOne(), this.getCoordTwo());
			Color avg = averageColor(screenShot);
			
			double[] r = {avg.getRed() - 50, 255};
			double[] g = {0, avg.getGreen() + 50};
			double[] b = {0, avg.getBlue() + 50};
			
			rgbThresholdRed = r;
			rgbThresholdBlue = b;
			rgbThresholdGreen = g;
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		} // end of try
	}
	
	
	
	private void startAnimation() 
	{
	    if (animationTimer == null) 
	    {
	        animationTimer = new Timer(animationDelay, this);
	        animationTimer.start();
	    } else if (!animationTimer.isRunning())
	    {
	    	animationTimer.restart();
	    } // end of if (animationTimer == null) 
	} // end of method startAnimation()
	
	private void setCurrentFrame(BufferedImage currentVideoCaptureFrame)
	{
		this.currentVideoCaptureFrame = currentVideoCaptureFrame;
	}
	
	/* utility */
	
	private Color averageColor(BufferedImage bi) {
	    int x1 = bi.getWidth();
	    int y1 = bi.getHeight();
	    int x0 = 0;
	    int y0 = 0;
	    int num = 0;
	    long sumr = 0, sumg = 0, sumb = 0;
	    for (int x = x0; x < x1; x++) {
	        for (int y = y0; y < y1; y++) {
	            Color pixel = new Color(bi.getRGB(x, y));
	            sumr += pixel.getRed();
	            sumg += pixel.getGreen();
	            sumb += pixel.getBlue();
	            num += 1;
	        }
	    }
	    System.out.println(sumr / num + " " + sumg / num + " " + sumb / num);
	    return new Color((int)sumr / num, (int) sumg / num, (int) sumb / num);
	}
	
	private BufferedImage cropImage(BufferedImage src, int[] coord1, int[] coord2)
	{
		int x1 = coord1[0];
		int y1 = coord1[1];
		
		System.out.println(x1 + " " + y1);
		
		int width = Math.abs(calibrationX2 - x1);
		int height = Math.abs(calibrationY2 - y1);
	    BufferedImage dest = src.getSubimage(x1+8, y1+32, width, height);
	    File outputfile = new File("C:/Users/thedi/Dropbox/VisionTracking2/src/Images/saved.png");
	    try 
	    {
	    	ImageIO.write(dest, "png", outputfile);
		}
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
	    return dest;
	}
	
	@Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        // draw current frame
        g.drawImage(currentVideoCaptureFrame, 10, 10, currentVideoCaptureFrame.getWidth()+10, 
        		currentVideoCaptureFrame.getHeight()+10, 0, 0, currentVideoCaptureFrame.getWidth(), 
        		currentVideoCaptureFrame.getHeight(), null);
        // draw recording frame
        g.drawImage(recordingOverlay, 10, 10, currentVideoCaptureFrame.getWidth()+10, 
        		currentVideoCaptureFrame.getHeight()+10, 0, 0, currentVideoCaptureFrame.getWidth(), 
        		currentVideoCaptureFrame.getHeight(), null);
        // draw calibrating frame
        if (this.isCurrentlyCalibrating()) {
        	g.drawImage(calibrationOverlay, 10, 10, currentVideoCaptureFrame.getWidth()+10, 
        			currentVideoCaptureFrame.getHeight()+10, 0, 0, currentVideoCaptureFrame.getWidth(), 
        			currentVideoCaptureFrame.getHeight(), null);
        }
    }
	
	
	
	
}
