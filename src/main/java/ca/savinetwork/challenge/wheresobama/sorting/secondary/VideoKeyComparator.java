package ca.savinetwork.challenge.wheresobama.sorting.secondary;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import ca.savinetwork.challenge.wheresobama.io.IntPair;
import ca.savinetwork.challenge.wheresobama.io.VideoPathAndFrame;

public class VideoKeyComparator extends WritableComparator {
	protected VideoKeyComparator() {
		super(VideoPathAndFrame.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		VideoPathAndFrame ip1 = (VideoPathAndFrame) w1;
		VideoPathAndFrame ip2 = (VideoPathAndFrame) w2;
		
		int cmp = VideoPathAndFrame.compare(ip1.getFilename(), ip2.getFilename());
		
		if (cmp != 0) {
			return cmp;
		}
		return VideoPathAndFrame.compare(ip1.getFrame(), ip2.getFrame());
	}
}
