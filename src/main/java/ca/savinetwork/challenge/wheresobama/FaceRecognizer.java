package ca.savinetwork.challenge.wheresobama;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.keypoints.KEDetectedFace;
import org.openimaj.image.processing.face.recognition.FaceRecognitionEngine;
import org.openimaj.ml.annotation.ScoredAnnotation;
import org.openimaj.util.pair.IndependentPair;

import ca.savinetwork.challenge.wheresobama.io.VideoPathAndFrame;

public class FaceRecognizer {

	private static Logger logger = Logger.getLogger(FaceRecognizer.class);
	private static String DELIMITER = "@";

	// public static class FaceRecognizerMapper extends
	// Mapper<VideoPathAndFrame, BytesWritable, Text, Text> {
	public static class FaceRecognizerMapper extends
			Mapper<VideoPathAndFrame, BytesWritable, Text, Text> {

		private FaceRecognitionEngine<KEDetectedFace, String> faceEngine;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			File file = new File("obama.train");
			if (file.exists()) {
				file.delete();
			}

			file = new File("obama.train");
			OutputStream os = new FileOutputStream(file);

			String uri = "hdfs://localhost/user/cloudera/obama/obama.train";
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(URI.create(uri), conf);
			FSDataInputStream in = null;
			try {
				in = fs.open(new Path(uri));
				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = in.read(bytes)) != -1) {
					os.write(bytes, 0, read);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeStream(in);
				os.close();
			}

			faceEngine = FaceRecognitionEngine.load(file);
		}

		@Override
		// public void map(VideoPathAndFrame key, BytesWritable value, Context
		// context) throws IOException, InterruptedException {
		public void map(VideoPathAndFrame key, BytesWritable value, Context context)
				throws IOException, InterruptedException {
			logger.info("map method called...");

			FImage fimg = ImageUtilities.readF(new ByteArrayInputStream(value
					.getBytes()));

			List<KEDetectedFace> faces = faceEngine.getDetector().detectFaces(
					fimg);

			// Go through detected faces
			for (KEDetectedFace face : faces) {

				// Find existing person for this face
				String person = null;
				float confidence = 0;

				try {
					List<IndependentPair<KEDetectedFace, ScoredAnnotation<String>>> rfaces = faceEngine
							.recogniseBest(face.getFacePatch());

					if (rfaces.size() > 0) {
						ScoredAnnotation<String> score = rfaces.get(0)
								.getSecondObject();
						if (score != null) {
							person = score.annotation;
							confidence = score.confidence;
						} else {
							person = "n/a";
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// a person from the database has been detected
				if (person != null) {
					//context.write(key.getFilename(), new Text(person));
					context.write(new Text(key.getFilename()+"-"+key.getFrame()), new Text(person));
				}
			}
		}
	}

	public static class FaceRecognizerReducer extends
			Reducer<Text, Text, Text, IntWritable> {

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			logger.info("reduce method called...");

			int obamaCounter = 0;

			// count times a person is detected in a video
			for (Text face : values) {
				if (face.toString().equals("obama")) {
					obamaCounter++;
				}
			}

			context.write(key, new IntWritable(obamaCounter));
		}
	}
}
