package ca.savinetwork.challenge.wheresobama.sorting.secondary;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import ca.savinetwork.challenge.wheresobama.io.IntPair;

public class GroupComparator extends WritableComparator {
	
	protected GroupComparator() {
		super(IntPair.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		IntPair ip1 = (IntPair) w1;
		IntPair ip2 = (IntPair) w2;
		
		return IntPair.compare(ip1.getFirst(), ip2.getFirst());
	}
}
