package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.*;

public class DocumentSimilarityReducer extends Reducer<Text, Text, Text, Text> {

    private Map<String, Set<String>> documentWordMap = new HashMap<>();
    private Map<String, Set<String>> allWordsPerDoc = new HashMap<>();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Set<String> docSet = new HashSet<>();

        for (Text val : values) {
            String docName = val.toString();
            docSet.add(docName);
            allWordsPerDoc.computeIfAbsent(docName, k -> new HashSet<>()).add(key.toString());
        }

        List<String> docList = new ArrayList<>(docSet);
        for (int i = 0; i < docList.size(); i++) {
            for (int j = i + 1; j < docList.size(); j++) {
                String pair = docList.get(i) + "," + docList.get(j);
                documentWordMap.computeIfAbsent(pair, k -> new HashSet<>()).add(key.toString());
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for (Map.Entry<String, Set<String>> entry : documentWordMap.entrySet()) {
            String[] docs = entry.getKey().split(",");
            String doc1 = docs[0];
            String doc2 = docs[1];

            Set<String> wordsInDoc1 = allWordsPerDoc.getOrDefault(doc1, new HashSet<>());
            Set<String> wordsInDoc2 = allWordsPerDoc.getOrDefault(doc2, new HashSet<>());

            Set<String> unionSet = new HashSet<>(wordsInDoc1);
            unionSet.addAll(wordsInDoc2);

            Set<String> intersectionSet = entry.getValue();

            if (unionSet.isEmpty()) {
                System.out.println("DEBUG: Empty union set for pair (" + doc1 + ", " + doc2 + ")");
                continue;
            }

            double jaccardSimilarity = (double) intersectionSet.size() / unionSet.size();
            context.write(new Text("(" + doc1 + ", " + doc2 + ")"), new Text(String.format("%.2f%%", jaccardSimilarity * 100)));
        }
    }
}