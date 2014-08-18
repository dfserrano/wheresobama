package ca.savinetwork.challenge.wheresobama.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class VideoPathAndFrame implements WritableComparable<VideoPathAndFrame> {
	private Text filename;
	private IntWritable frame;

	public VideoPathAndFrame() {
		set(new Text(), new IntWritable());
	}

	public VideoPathAndFrame(String filename, int frame) {
		set(new Text(filename), new IntWritable(frame));
	}

	public VideoPathAndFrame(Text filename, IntWritable frame) {
		set(filename, frame);
	}

	public void set(Text filename, IntWritable frame) {
		this.filename = filename;
		this.frame = frame;
	}

	public Text getFilename() {
		return filename;
	}

	public IntWritable getFrame() {
		return frame;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		filename.write(out);
		frame.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		filename.readFields(in);
		frame.readFields(in);
	}

	@Override
	public int hashCode() {
		return filename.hashCode() * 163 + frame.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof VideoPathAndFrame) {
			VideoPathAndFrame ip = (VideoPathAndFrame) o;
			return filename.equals(ip.filename) && frame.equals(ip.frame);
		}
		return false;
	}

	@Override
	public String toString() {
		return filename + "\t" + frame;
	}

	@Override
	public int compareTo(VideoPathAndFrame ip) {
		int cmp = filename.compareTo(ip.filename);
		if (cmp != 0) {
			return cmp;
		}
		return frame.compareTo(ip.frame);
	}
	
	public static int compare(IntWritable a, IntWritable b) {
		return a.compareTo(b);
	}
}