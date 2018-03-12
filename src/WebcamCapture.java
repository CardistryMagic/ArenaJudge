import java.awt.image.BufferedImage;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.videoio.VideoCapture;

public class WebcamCapture {
	/* private fields */
	private int webcamID;
	private VideoCapture videoCapture;
	
	private BufferedImage sampleWebcamInput;
	
	/* constructors */
	
	/**
	 * Creates a new video capture.
	 */
	public WebcamCapture()
	{
		webcamID = 0;
		videoCapture = new VideoCapture(webcamID);
	} // end of VideoCapture()
	
	/**
	 * Creates a new video capture with the specified webcam ID.
	 * 
	 * @param webcamID webcam ID
	 */
	public WebcamCapture(int webcamID)
	{
		this.webcamID = webcamID;
		videoCapture = new VideoCapture(this.webcamID);
		videoCapture.set(3, 1280);
		videoCapture.set(4, 960);
	} // end of VideoCapture(int webcamID)
	
	/* accessors */
	
	/**
	 * Returns the webcamID.
	 * 
	 * @return webcam ID
	 */
	public int getWebcamID()
	{
		return webcamID;
	} // end of method getWebcamID()
	
	/**
	 * Returns the current frame as a buffered image.
	 * 
	 * @return currentWebcamFrame current frame from webcam capture as a buffered image
	 */
	public BufferedImage getCurrentFrameBufferedImage()
	{
		return matToBufferedImage(getCurrentFrameMat());
	}
	
	/**
	 * Returns the current frame as a mat.
	 * 
	 * @return current frame from webcam capture as a mat
	 */
	public Mat getCurrentFrameMat()
	{
		Mat currentWebcamMat = new Mat();
		if(videoCapture.isOpened())
		{
			// read image from webcam
			videoCapture.read(currentWebcamMat);
		}
		else
		{
			System.out.println("Error: No captured frame --> videoCapture not opened");   
		} // end of if(webcamCapture.isOpened())
		
		return currentWebcamMat;
	} // end of method getCurrentFrameMat()
	
	/* mutators */
	
	
	/* utility */
	
	private BufferedImage matToBufferedImage(Mat matrix)
	{
		if (matrix != null)
		{
			int cols = matrix.cols();  
			int rows = matrix.rows();  
			int elemSize = (int)matrix.elemSize();  
			byte[] data = new byte[cols * rows * elemSize];  
			int type;  
			matrix.get(0, 0, data);  
			switch (matrix.channels()) 
			{  
			case 1:  
				type = BufferedImage.TYPE_BYTE_GRAY;  
				break;  
			case 3:  
				type = BufferedImage.TYPE_3BYTE_BGR;  
				// bgr to rgb  
				byte b;  
				for (int i=0; i<data.length; i=i+3) 
				{  
					b = data[i];  
					data[i] = data[i+2];  
					data[i+2] = b;  
				} // end of for (int i=0; i<data.length; i=i+3)
				break;  
			default:  
				return null;  
			} // end of switch (matrix.channels()) 
			BufferedImage image2 = new BufferedImage(cols, rows, type);  
			image2.getRaster().setDataElements(0, 0, cols, rows, data);  
			return image2;  
		}
		else
		{
			return null;
		} // end of if (matrix != null)
	} // end of matToBufferedImage(Mat matrix)
} // end of class WebcamCapture
