package cn.omartech.gossip.dict;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.IOUtils;

public class VectorPrepare {

    static Map<String, Integer> wordMap = new ConcurrentHashMap<>();
    static {
        InputStream is = VectorPrepare.class.getClassLoader().getResourceAsStream("words.txt");
        try {
            List<String> lines = IOUtils.readLines(is);
            int i = 0;
            for (String line : lines) {
                String[] tmps = line.split("\t");
                int parseInt = Integer.parseInt(tmps[1]);
                wordMap.put(tmps[0], i);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String parseVector(String text) {
        List<Term> terms = ToAnalysis.parse(text);
        Map<Integer, Integer> posCount = new HashMap<>();
        for (Term term : terms) {
            Integer num = wordMap.get(term.getName());
            if (num != null) {
                Integer c = posCount.get(num);
                if (c == null) {
                    c = 0;
                }
                c++;
                posCount.put(num, c);
            }
        }
        List<Entry<Integer, Integer>> numbers = new ArrayList<>(posCount.entrySet());
        Collections.sort(numbers, new Comparator<Entry<Integer, Integer>>() {

            @Override
            public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
                if (o1.getKey() > o2.getKey()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        StringBuilder sb = new StringBuilder();
        for (Entry<Integer, Integer> entry : numbers) {
            sb.append(entry.getKey() + ":" + entry.getValue());
            sb.append(";");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String text = "根据公司销售战略，协助制定公司年度区域渠道开发策略和执行计划开发";
        String res = parseVector(text);
        System.out.println(res);
    }

    
    
}
