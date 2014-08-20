package ca.savinetwork.challenge.wheresobama;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.log4j.Logger;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.video.Video;
import org.openimaj.video.xuggle.XuggleVideo;

import ca.savinetwork.challenge.wheresobama.io.VideoPathAndFrame;
import ca.savinetwork.challenge.wheresobama.util.Util;

public class VideosToImageSeqFiles {

	private static Logger logger = Logger
			.getLogger(VideosToImageSeqFiles.class);

	static class VideosToImageSeqFilesMapper
			extends
			Mapper<NullWritable, BytesWritable, VideoPathAndFrame, BytesWritable> {

		private Text filenameKey;

		@Override
		protected void setup(Context context) throws IOException,
				InterruptedException {
			InputSplit split = context.getInputSplit();
			Path path = ((FileSplit) split).getPath();
			filenameKey = new Text(path.toString());
		}

		@Override
		protected void map(NullWritable key, BytesWritable value,
				Context context) throws IOException, InterruptedException {
			logger.info("map method called...");

			// store locally the video
			String localFilePath = allocateTempFile();
			Util.fromBytesToFile(value.getBytes(), localFilePath);
			File videoFile = new File(localFilePath);

			Video<MBFImage> video;
			video = new XuggleVideo(videoFile);

			int frameNumber = 0;
			for (MBFImage image : video) {
				// make a local copy of the image
				String imgFilePath = allocateTempFile();
				File imgFile = new File(imgFilePath);
				ImageUtilities.write(image, "JPG", imgFile);

				// get image into bytes
				byte[] imgBytes = Util.fromFileToBytes(imgFilePath);

				context.write(new VideoPathAndFrame(filenameKey.toString(),
						++frameNumber), new BytesWritable(imgBytes));

				// delete local copy of the image
				imgFile.delete();
			}

			// emit a value to store number of frames of video

			byte[] bi = intToByteArray(frameNumber);

			context.write(new VideoPathAndFrame(filenameKey.toString(), -1),
					new BytesWritable(bi));

			videoFile.delete();
		}

		private byte[] intToByteArray(int value) {
			return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
					(byte) (value >>> 8), (byte) value };

		}

		private String allocateTempFile() {
			File tempFile;
			String tempFilePath;

			do {
				tempFilePath = Util.getRandomString(5, 10);
				tempFile = new File(tempFilePath);
			} while (tempFile.exists() || tempFile.isDirectory());

			return tempFilePath;
		}

	}
}