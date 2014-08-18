package ca.savinetwork.challenge.wheresobama;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.io.BytesWritable;
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
import ca.savinetwork.challenge.wheresobama.util.Util;

public class FaceRecognizer {

	private static Logger logger = Logger.getLogger(FaceRecognizer.class);
	private static String DELIMITER = "@";

	public static class FaceRecognizerMapper extends
			Mapper<VideoPathAndFrame, BytesWritable, Text, Text> {

		private FaceRecognitionEngine<KEDetectedFace, String> faceEngine;
		private Random random = new Random(System.currentTimeMillis());

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			faceEngine = FaceRecognitionEngine.load(new File(
					"/home/dfserrano/obama.train"));
		}

		@Override
		public void map(VideoPathAndFrame key, BytesWritable value,
				Context context) throws IOException, InterruptedException {
			logger.info("map method called...");

			String localFilePath;
			File f;
			do {
				localFilePath = Util.getRandomString(random.nextInt(5) + 5);
				f = new File(localFilePath);
			} while (f.exists() && f.isDirectory());

			FileOutputStream fos = new FileOutputStream(f);
			fos.write(value.getBytes());
			fos.flush();
			fos.close();

			FImage fimg = ImageUtilities.readF(new FileInputStream(
					localFilePath));

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
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// a person from the database has been detected
				if (person != null) {
					context.write(key.getFilename(), new Text(person));
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
