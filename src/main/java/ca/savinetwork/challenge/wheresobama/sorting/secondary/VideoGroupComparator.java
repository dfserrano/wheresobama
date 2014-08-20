package ca.savinetwork.challenge.wheresobama.sorting.secondary;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import ca.savinetwork.challenge.wheresobama.io.IntPair;
import ca.savinetwork.challenge.wheresobama.io.VideoPathAndFrame;

public class VideoGroupComparator extends WritableComparator {
	
	protected VideoGroupComparator() {
		super(VideoPathAndFrame.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		VideoPathAndFrame ip1 = (VideoPathAndFrame) w1;
		VideoPathAndFrame ip2 = (VideoPathAndFrame) w2;
		
		return VideoPathAndFrame.compare(ip1.getFilename(), ip2.getFilename());
	}
}
