package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

public class DocumentSimilarityMapper extends Mapper<Object, Text, Text, Text> {
    
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.isEmpty()) {
            System.out.println("DEBUG: Empty line encountered");
            return; // Skip empty lines
        }

        String[] parts = ((org.apache.hadoop.mapreduce.lib.input.FileSplit) context.getInputSplit()).getPath().toString().split("/");
        String fileName = parts[parts.length - 1]; // Extract filename
        
        System.out.println("DEBUG: Processing file - " + fileName);

        HashSet<String> words = new HashSet<>();
        StringTokenizer tokenizer = new StringTokenizer(line);
        
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken().toLowerCase().replaceAll("[^a-zA-Z]", ""); // Normalize words
            if (!word.isEmpty()) {
                words.add(word);
            }
        }

        System.out.println("DEBUG: " + fileName + " contains words: " + words);

        for (String word : words) {
            context.write(new Text(word), new Text(fileName)); // Emit (word, filename)
        }
    }
}
