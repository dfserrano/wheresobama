package ca.savinetwork.challenge.wheresobama.sorting.secondary;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class FirstPartitioner extends Partitioner<IntPair, NullWritable> {
	@Override
	public int getPartition(IntPair key, NullWritable value, int numPartitions) {
		// multiply by 127 to perform some mixing
		return Math.abs(key.getFirst().get() * 127) % numPartitions;
	}
}
