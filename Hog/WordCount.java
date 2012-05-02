/* This is a program written in Java+Hadoop, to give
 * a sample of what our code generation 
 * 
 * derived from the WordCount program at http://wiki.apache.org/hadoop/WordCount
 * 
 * @author paul, kurry 
 * 
 *
 */



import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.*;
    

public class WordCount {
	
	public static class Functions {
	     public static int fib(int n){
	    	 if (n == 0){
	    		 return 0;
	    	 }
	    	 else if (n == 1){
	    		 return 1;
	    	 }
	    	 
	    	 else {
	    		 return fib(n-1) + fib(n-2);
	    	 }
	     }
	     
	     public static int factorial(int n){
	    	 if (n==0 || n == 1){
	    		 return 1;
	    	 }
	    	 else {
	    		 return n * factorial(n-1);
	    	 }
	     }
	}
	
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		Functions f = new Functions();
		
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			String line = value.toString();
			int n =   f.factorial(3);
			line = line + n;
			StringTokenizer tokenizer = new StringTokenizer(line);
			while (tokenizer.hasMoreTokens()) {
				word.set(tokenizer.nextToken());
				output.collect(word, one);
			}
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
		
	    public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			int sum = 0;
			while(values.hasNext()) {
			    sum += values.next().get();
			}
			output.collect(key, new IntWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception {
	    JobConf conf = new JobConf(WordCount.class);
	    conf.setJobName("wordcount");
			
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
	      

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
		
		
		
		
		/*
		JobConf conf = new JobConf(WordCount.class);
		conf.setJobName("wordcount");
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		conf.setMapperClass(Map.class);
		conf.setReducerClass(Reduce.class);
		conf.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		FileInputFormat.addInputPath(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		JobClient.runJob(conf);
		*/
	}

}