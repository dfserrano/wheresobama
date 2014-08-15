package ca.savinetwork.challenge.wheresobama;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import ca.savinetwork.challenge.wheresobama.sorting.secondary.FirstPartitioner;
import ca.savinetwork.challenge.wheresobama.sorting.secondary.GroupComparator;
import ca.savinetwork.challenge.wheresobama.sorting.secondary.IntPair;
import ca.savinetwork.challenge.wheresobama.sorting.secondary.KeyComparator;
import ca.savinetwork.challenge.wheresobama.util.JobBuilder;

public class GroupedVideoResultsDriver extends Configured implements Tool {
	static class MaxTemperatureMapper extends
			Mapper<LongWritable, Text, IntPair, NullWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			context.write(
					new IntPair(2002, 30),
					NullWritable.get());

		}
	}

	@Override
	public int run(String[] args) throws Exception {
		Job job = JobBuilder.parseInputAndOutput(this, getConf(), args);
		if (job == null) {
			return -1;
		}

		job.setMapperClass(MaxTemperatureMapper.class);
		
		job.setPartitionerClass(FirstPartitioner.class);
		job.setSortComparatorClass(KeyComparator.class);
		job.setGroupingComparatorClass(GroupComparator.class);
		
		//job.setReducerClass(MaxTemperatureReducer.class);
		job.setOutputKeyClass(IntPair.class);
		job.setOutputValueClass(NullWritable.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new GroupedVideoResultsDriver(), args);
		System.exit(exitCode);
	}
}
