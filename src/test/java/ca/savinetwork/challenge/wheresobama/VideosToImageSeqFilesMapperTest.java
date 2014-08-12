package ca.savinetwork.challenge.wheresobama;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;

import ca.savinetwork.challenge.wheresobama.VideosToImageSeqFiles.VideosToImageSeqFilesMapper;

public class VideosToImageSeqFilesMapperTest {

	MapDriver<NullWritable, BytesWritable, Text, BytesWritable> mapDriver;
	
	@Before
	public void setUp() {
		VideosToImageSeqFilesMapper mapper = new VideosToImageSeqFilesMapper();
		mapDriver = MapDriver.newMapDriver(mapper);
	}
	
	@Test
	public void testMapper() throws IOException {
		mapDriver.withInput(NullWritable.get(), new BytesWritable("Diego".getBytes()));
		mapDriver.withOutput(new Text("somefile"), new BytesWritable("Diego".getBytes()));
		mapDriver.runTest();
	}
}
