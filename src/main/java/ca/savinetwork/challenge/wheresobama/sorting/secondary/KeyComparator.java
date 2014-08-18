package ca.savinetwork.challenge.wheresobama.sorting.secondary;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import ca.savinetwork.challenge.wheresobama.io.IntPair;

public class KeyComparator extends WritableComparator {
	protected KeyComparator() {
		super(IntPair.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		IntPair ip1 = (IntPair) w1;
		IntPair ip2 = (IntPair) w2;
		
		int cmp = IntPair.compare(ip1.getFirst(), ip2.getFirst());
		
		if (cmp != 0) {
			return cmp;
		}
		return IntPair.compare(ip1.getSecond(), ip2.getSecond());
	}
}
