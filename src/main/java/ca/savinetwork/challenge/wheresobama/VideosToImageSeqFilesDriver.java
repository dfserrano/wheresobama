package ca.savinetwork.challenge.wheresobama;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import ca.savinetwork.challenge.wheresobama.VideosToImageSeqFiles.VideosToImageSeqFilesMapper;
import ca.savinetwork.challenge.wheresobama.io.VideoPathAndFrame;
import ca.savinetwork.challenge.wheresobama.io.WholeFileInputFormat;

public class VideosToImageSeqFilesDriver extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Job job = new Job(getConf());
		job.setJarByClass(VideosToImageSeqFilesDriver.class);
		job.setJobName("VideosToImageSeqFiles");

		job.setInputFormatClass(WholeFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		job.setOutputKeyClass(VideoPathAndFrame.class);
		job.setOutputValueClass(BytesWritable.class);

		job.setMapperClass(VideosToImageSeqFilesMapper.class);

		//String[] otherArgs = new GenericOptionsParser(args).getRemainingArgs();
		String[] otherArgs = args;
		if (otherArgs.length != 2) {
			System.err
					.println("Usage: VideosToImageSeqFilesDriver <in Path for url file> <out pat for sequence file>");
			for (int i = 0; i < otherArgs.length; i++) {
				System.out.println(otherArgs[i]);
			}
			System.exit(2);
		}

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new VideosToImageSeqFilesDriver(), args);
		System.exit(exitCode);
	}

}
