package ca.savinetwork.challenge.wheresobama;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.log4j.Logger;

public class VideosToImageSeqFiles {

	private static Logger logger = Logger
			.getLogger(VideosToImageSeqFiles.class);

	static class VideosToImageSeqFilesMapper extends
			Mapper<NullWritable, BytesWritable, Text, BytesWritable> {

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
			context.write(filenameKey, value);
		}
	}
}
