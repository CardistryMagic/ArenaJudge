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
	private Mat currentWebcamFrame;
	private Queue<BufferedImage> frameBuffer;
	private VisionProcessor visionProcessor = new VisionProcessor();
	private boolean isCalibrated = false;
	private boolean isCurrentlyCalibrating = false;
	private BufferedImage recordingOverlay;
	private int numberOfFramesInQueue = 1;
	private double[] rgbThresholdRed = {80, 255.0};
	private double[] rgbThresholdGreen = {0.0, 110};
	private double[] rgbThresholdBlue = {0.0, 110};
	private static final long serialVersionUID = 1L;
	private boolean showCalibrationFrame = false;
	private VideoCapture webcamCapture;
	
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
    	currentWebcamFrame = new Mat();
    	
    	// initialize video camera capture
        webcamCapture = new VideoCapture(0);
        
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
		BufferedImage temporaryWebcamFrame = null;
		String formatted = String.format("%03d", currentFrameNumber);
		
		// update GUI
		repaint();
		
		if(webcamCapture.isOpened())
		{
			// read image from webcam
			webcamCapture.read(currentWebcamFrame);
			
			if (!currentWebcamFrame.empty())
			{
				// convert current frame from mat to buffered image
				temporaryWebcamFrame = matToBufferedImage(currentWebcamFrame);
				
				// process current frame
				videoProcessor.process(webcamImage, this.getRedRGBThreshold(), , this.getBlueRGBThreshold(), this.getGreenRGBThreshold());
				
				// add current frame to frame buffer
				frameBuffer.add(temporaryWebcamFrame);
			} // end of if (!currentWebcamFrame.empty())
		}
		else
		{
			System.out.println(" --(!) No captured frame -- Break!");   
		} // end of if(webcamCapture.isOpened())
		
		// update current frame number
		currentFrameBufferID = (currentFrameBufferID + 1) % numberOfFramesInQueue;
		
		BufferedImage currentDisplayFrame = frameBuffer.remove();
		
		setCurrentFrame(currentDisplayFrame);
	}
	
	/* accessors */
	
	
	/* mutators */

	private BufferedImage matToBufferedImage(Mat matrix) {  
		int cols = matrix.cols();  
		int rows = matrix.rows();  
		int elemSize = (int)matrix.elemSize();  
		byte[] data = new byte[cols * rows * elemSize];  
		int type;  
		matrix.get(0, 0, data);  
		switch (matrix.channels()) {  
		case 1:  
			type = BufferedImage.TYPE_BYTE_GRAY;  
			break;  
		case 3:  
			type = BufferedImage.TYPE_3BYTE_BGR;  
			// bgr to rgb  
			byte b;  
			for(int i=0; i<data.length; i=i+3) {  
				b = data[i];  
				data[i] = data[i+2];  
				data[i+2] = b;  
			}  
			break;  
		default:  
			return null;  
		}  
		BufferedImage image2 = new BufferedImage(cols, rows, type);  
		image2.getRaster().setDataElements(0, 0, cols, rows, data);  
		return image2;  
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
	
	/* utility */
	
	@Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        // draw current frame
        g.drawImage(currentFrame, 10, 10, currentFrame.getWidth()+10, currentFrame.getHeight()+10, 0, 0, 
        		currentFrame.getWidth(), currentFrame.getHeight(), null);
        // draw recording frame
        g.drawImage(recordingFrame, 10, 10, currentFrame.getWidth()+10, currentFrame.getHeight()+10, 0, 0, 
        		currentFrame.getWidth(), currentFrame.getHeight(), null);
        // draw 
//        this.showCalibrationFrame = !this.showCalibrationFrame;
        if (this.isCurrentlyCalibrating()) {
        	g.drawImage(calibratingFrame, 10, 10, currentFrame.getWidth()+10, currentFrame.getHeight()+10, 0, 0, 
            		currentFrame.getWidth(), currentFrame.getHeight(), null);
        }
    }
	
	
	
	
}
