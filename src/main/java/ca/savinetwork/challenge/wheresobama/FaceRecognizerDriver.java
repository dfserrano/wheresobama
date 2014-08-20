package ca.savinetwork.challenge.wheresobama;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import ca.savinetwork.challenge.wheresobama.FaceRecognizer.FaceRecognizerMapper;
import ca.savinetwork.challenge.wheresobama.FaceRecognizer.FaceRecognizerReducer;
import ca.savinetwork.challenge.wheresobama.sorting.secondary.VideoGroupComparator;
import ca.savinetwork.challenge.wheresobama.sorting.secondary.VideoKeyComparator;
import ca.savinetwork.challenge.wheresobama.sorting.secondary.VideoPathPartitioner;

public class FaceRecognizerDriver extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		Job job = new Job(getConf());
		job.setJarByClass(FaceRecognizerDriver.class);
		job.setJobName("FaceRecognizer");
		
        job.setInputFormatClass(SequenceFileInputFormat.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setMapperClass(FaceRecognizerMapper.class);
		job.setReducerClass(FaceRecognizerReducer.class);
		
		String[] otherArgs = args;
		if (otherArgs.length != 2) {
			System.err
					.println("Usage: FaceRecognizerDriver <in Path for url file> <out pat for sequence file>");
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
		int exitCode = ToolRunner.run(new FaceRecognizerDriver(), args);
		System.exit(exitCode);
	}

}
