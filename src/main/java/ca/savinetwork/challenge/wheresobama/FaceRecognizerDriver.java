package ca.savinetwork.challenge.wheresobama;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import ca.savinetwork.challenge.wheresobama.FaceRecognizer.FaceRecognizerMapper;
import ca.savinetwork.challenge.wheresobama.FaceRecognizer.FaceRecognizerReducer;

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
		
		return job.waitForCompletion(true) ? 0 : 1;
	}
	
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new FaceRecognizerDriver(), args);
		System.exit(exitCode);
	}

}
