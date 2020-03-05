/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.stream.Stream;

/**
 *
 * @author arturs vitolins
 */
public class HuffmanUtils {

    public static Map<String, Integer> buildFrequencyTable(String filePath) {

        // @refactor
        StringBuilder contentBuilder = new StringBuilder();
        FileUtils.readFileIntoStringBuild(contentBuilder, filePath);
        
        String fullFileContent = contentBuilder.toString();
        Map<String, Integer> frequencyTable = new HashMap<>();

        for (char character : fullFileContent.toCharArray()) {
            String tempString = String.valueOf(character);
            Integer frequency = frequencyTable.get(tempString);

            if (frequency == null) {
                frequency = 1;
            } else {
                frequency++;
            }

            frequencyTable.put(tempString, frequency);
        }

        return HuffmanUtils.sortByValue(frequencyTable);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        list.forEach((entry) -> {
            result.put(entry.getKey(), entry.getValue());
        });

        return result;
    }

    public static void printCode(HuffmanNode root, String s) {

        // new line and space check
        if (Character.isLetter(root.character) || root.character == ' ' || root.character == '\n') {
            System.out.println(root.character + ":" + root.frequency + ": " + s);
        }

        if (root.zero != null) {
            printCode(root.zero, s + "0");
        }

        if (root.one != null) {
            printCode(root.one, s + "1");
        }
    }

    public static PriorityQueue<HuffmanNode> buildPriorityQueue(Map<String, Integer> table) {
        PriorityQueue<HuffmanNode> queue = new PriorityQueue(table.size(), new HuffmanComperator());

        table.entrySet().forEach((entry) -> {
            char charater = entry.getKey().toCharArray()[0];
            Integer frequency = entry.getValue();

            queue.add(new HuffmanNode(charater, frequency));
        });

        return queue;
    }

    public static HuffmanNode buildEncodingTree(PriorityQueue<HuffmanNode> queue) {
        HuffmanNode root = null;

        while (queue.size() > 1) {

            HuffmanNode x = queue.peek();
            queue.poll();

            HuffmanNode y = queue.peek();
            queue.poll();

            HuffmanNode f = new HuffmanNode();

            f.frequency = x.frequency + y.frequency;
            f.character = '-';

            f.zero = x;
            f.one = y;

            root = f;

            queue.add(f);
        }

        return root;
    }

    static void buildEncodingMap(HuffmanNode root, String s, Map<Character,String> codeMap) {
                
        if (Character.isLetter(root.character) || root.character == ' ' || root.character == '\n') {
            codeMap.put(root.character, s);
        }

        if (root.zero != null) {
            buildEncodingMap(root.zero, s + "0", codeMap);
        }

        if (root.one != null) {
            buildEncodingMap(root.one, s + "1", codeMap);
        }
        
    }
}
