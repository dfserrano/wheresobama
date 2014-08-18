package ca.savinetwork.challenge.wheresobama;

import java.io.File;
import java.io.FileInputStream;
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

		private Random random = new Random(System.currentTimeMillis());

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

			String localFilePath;
			File f;
			do {
				localFilePath = Util.getRandomString(random.nextInt(5) + 5);
				f = new File(localFilePath);
			} while (f.exists() || f.isDirectory());

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
				String imgFilePath;
				File imgFile;
				do {
					imgFilePath = Util.getRandomString(random.nextInt(5) + 5);
					imgFile = new File(imgFilePath);
				} while (imgFile.exists() || imgFile.isDirectory());
				
				ImageUtilities.write(image, "JPG", imgFile);
				
				byte[] bytes = new byte[(int) imgFile.length()];
				
				FileInputStream fis = new FileInputStream(imgFile);
				fis.read(bytes);
				fis.close();
				
				context.write(new VideoPathAndFrame(filenameKey.toString(), ++frameNumber), new BytesWritable(bytes));
			}

			f.delete();
		}
	}
}
