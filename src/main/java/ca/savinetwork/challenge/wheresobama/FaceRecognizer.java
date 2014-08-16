package ca.savinetwork.challenge.wheresobama;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

public class FaceRecognizer {
	
	private static Logger logger = Logger
			.getLogger(FaceRecognizer.class);
	
	public static class FaceRecognizerMapper extends Mapper<Text, BytesWritable, Text, Text>{

		public void map(Text key, BytesWritable value, Context context) throws IOException,InterruptedException {
			logger.info("map method called...");
			// TO-DO: Code for recognizer
			// write filename and name of person detected
	        //context.write(key, value);
		}	    
	}

	public static class FaceRecognizerReducer extends Reducer<Text,Text,Text,Text> {

		public void reduce(Text key, Iterable<Text> values, Context context)
							throws IOException, InterruptedException {
			logger.info("reduce method called...");
			// count times a person is detected in a video
			for (Text face : values) {
				
			}
			
			//context.write(key, value);
		}
	}
}
