package ca.savinetwork.challenge.wheresobama.sorting.secondary;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

import ca.savinetwork.challenge.wheresobama.io.IntPair;
import ca.savinetwork.challenge.wheresobama.io.VideoPathAndFrame;

public class VideoPathPartitioner extends Partitioner<VideoPathAndFrame, NullWritable> {
	@Override
	public int getPartition(VideoPathAndFrame key, NullWritable value, int numPartitions) {
		// multiply by 127 to perform some mixing
		return Math.abs(key.getFilename().hashCode() * 127) % numPartitions;
	}
}
