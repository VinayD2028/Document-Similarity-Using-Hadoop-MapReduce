[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18028251&assignment_repo_type=AssignmentRepo)
### **📌 Document Similarity Using Hadoop MapReduce**  
This project computes the Jaccard Similarity between multiple text documents using Hadoop MapReduce. The Jaccard Similarity is defined as: Jaccard Similarity (A, B) = |A ∩ B| / |A ∪ B| 
The goal of this assignment is to compute the **Jaccard Similarity** between pairs of documents using **MapReduce in Hadoop**. You will implement a MapReduce job that:  
1. Extracts words from multiple text documents.  
2. Identifies which words appear in multiple documents.  
3. Computes the **Jaccard Similarity** between document pairs.  
4. Outputs document pairs with similarity **above 50%**.  

---
## Approach and implementation
1. Mapper: DocumentSimilarityMapper.java: Reads each document and extracts words, Normalizes words by converting them to lowercase and removing punctuation and Emits (word, document_name) pairs.
2. Reducer: DocumentSimilarityReducer.java: Receives all documents containing a particular word, Forms document pairs and tracks shared words. In the cleanup phase, calculates the Jaccard Similarity for each document pair and outputs the result.

---
## Setup and Execution

### 1. **Start the Hadoop Cluster**

Run the following command to start the Hadoop cluster:

```bash
docker compose up -d
```

### 2. **Build the Code**

Build the code using Maven:

```bash
mvn clean package
```

### 4. **Copy JAR to Docker Container**

Copy the JAR file to the Hadoop ResourceManager container:

```bash
docker cp target/DocumentSimilarity-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 5. **Move Dataset to Docker Container**

Copy the dataset to the Hadoop ResourceManager container:

```bash
docker cp input/doc1.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
docker cp input/doc2.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
docker cp input/doc3.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 6. **Connect to Docker Container**

Access the Hadoop ResourceManager container:

```bash
docker exec -it resourcemanager /bin/bash
```

Navigate to the Hadoop directory:

```bash
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 7. **Set Up HDFS**

Create a folder in HDFS for the input dataset:

```bash
hadoop fs -mkdir -p /input
```

Copy the input dataset to the HDFS folder:

```bash
hadoop fs -put doc1.txt /input
hadoop fs -put doc2.txt /input
hadoop fs -put doc3.txt /input
```

### 8. **Execute the MapReduce Job**

Run your MapReduce job using the following command

```bash
 hadoop jar DocumentSimilarity-0.0.1-SNAPSHOT.jar com.example.controller.DocumentSimilarityDriver /input /output
```

### 9. **View the Output**

To view the output of your MapReduce job, use:

```bash
hadoop fs -ls /output
```
9.2 View the Output File foe the Task
Task 1: Jacard Similarity among the documents
```bash
hadoop fs -cat /output/part-r-00000
```

This command will display the result for the analysis task.

### 10. **Copy Output from HDFS to Local OS**
To copy the output from HDFS to your local machine:
1. Use the following command to copy from HDFS:
    ```bash
    hadoop fs -get /output /opt/hadoop-3.2.1/share/hadoop/mapreduce/
    ```
2. use Docker to copy from the container to your local machine:
   ```bash
   exit 
   ```
    ```bash
    docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output5/ ./output_final/
    ```
3. Commit and push to your repo so that we can able to see your output

---
## Challenges faced: 
1. Handling Edge Cases: Initially, empty lines caused errors; handled them by checking if(line.isEmpty()).
2. Correct Union Computation: The union set was sometimes incorrect. Fixed by ensuring all words per document are collected correctly.
3. Formatting Issues: Initial output formatting was inconsistent; resolved using String.format("%.2f%%", value).
---

### **📥 Sample Input**  
You will be given multiple text documents. Each document will contain several words. Your task is to compute the **Jaccard Similarity** between all pairs of documents based on the set of words they contain.  
#### **Example Documents**  
##### **doc1.txt**  
```
hadoop is a distributed system
```
##### **doc2.txt**  
```
hadoop is used for big data processing
```
##### **doc3.txt**  
```
big data is important for analysis
```

---

### **📤 Expected Output**  

The output should show the Jaccard Similarity between document pairs in the following format:  
```
(doc1, doc2) -> 20%  
(doc2, doc3) -> 44.44%  
```

---
