package ca.savinetwork.challenge.wheresobama;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.log4j.Logger;
import org.openimaj.image.MBFImage;
import org.openimaj.video.Video;
import org.openimaj.video.xuggle.XuggleVideo;

public class VideosToImageSeqFiles {

	private static Logger logger = Logger
			.getLogger(VideosToImageSeqFiles.class);

	static class VideosToImageSeqFilesMapper extends
			Mapper<NullWritable, BytesWritable, Text, BytesWritable> {

		private Text filenameKey;

		private static final String letters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		private static Random random = new Random(System.currentTimeMillis());

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
			
			Text mapKey = new Text();
			
			String localFilePath;
			File f;
			do {
				localFilePath = getRandomString(random.nextInt(5) + 5);
				f = new File(localFilePath);
			} while(f.exists() && f.isDirectory());
			
			FileOutputStream stream = new FileOutputStream(localFilePath);
			try {
			    stream.write(value.getBytes());
			} finally {
			    stream.close();
			}

			Video<MBFImage> video;
			video = new XuggleVideo(new File(localFilePath));

			int frameNumber = 0;
			for (MBFImage image : video) {
				mapKey.set(filenameKey + "@" + ++frameNumber);
				context.write(filenameKey, new BytesWritable(image.toByteImage()));
			}
			
			f.delete();
		}

		private String getRandomString(int len) {
			StringBuilder sb = new StringBuilder(len);

			for (int i = 0; i < len; i++) {
				sb.append(letters.charAt(random.nextInt(letters.length())));
			}

			return sb.toString();
		}
	}
}
