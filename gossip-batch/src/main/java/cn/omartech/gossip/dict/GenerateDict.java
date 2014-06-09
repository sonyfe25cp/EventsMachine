package cn.omartech.gossip.dict;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

public class GenerateDict {

    public static void main(String[] args) {
        GenerateDict gd = new GenerateDict();
        try {
            gd.readFromDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ConcurrentHashMap<String, Integer> wordsCount = new ConcurrentHashMap<>();

    void readFromDB() throws IOException {
        JobService jobService = new JobService();
        int limit = 5000;
        int max = 0;
        int MAX = 750123;
//        int MAX= 10000;
        int cpu = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor excutor = new ThreadPoolExecutor(cpu, cpu, 1, TimeUnit.HOURS,
                new ArrayBlockingQueue<Runnable>(cpu * 5),
                new ThreadPoolExecutor.CallerRunsPolicy());

        while (max < MAX) {
            List<Job> jobs = jobService.findJobs(max, limit);
            for (Job job : jobs) {
                excutor.submit(new Worker(job));
                max = Math.max(max, job.id);
            }
        }
        excutor.shutdown();
        try {
            excutor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Entry<String, Integer>> countList = new ArrayList<>(wordsCount.entrySet());
        Collections.sort(countList, new Comparator<Entry<String, Integer>>() {

            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                int c1 = o1.getValue();
                int c2 = o2.getValue();
                if (c1 < c2) {
                    return 1;
                } else if (c1 == c2) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("/tmp/words.txt")));
        for (Entry<String, Integer> entry : countList) {
            String key = entry.getKey();
            int c = entry.getValue();
            try {
                bw.write(key + "\t" + c + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bw.flush();
        bw.close();
    }

    class Worker implements Runnable {

        Job job;

        Worker(Job job) {
            this.job = job;
        }

        public void run() {
            String str = job.desc;
            List<Term> terms = ToAnalysis.parse(str);
            for (Term term : terms) {
                String word = term.getName();
                if(word.length() == 1){
                    continue;
                }
                Integer count = wordsCount.get(word);
                if (count == null) {
                    count = 0;
                }
                count++;
                wordsCount.put(word, count);
            }
        }
    }
}
