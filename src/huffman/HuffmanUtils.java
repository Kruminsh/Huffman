/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 *
 * @author arturs vitolins
 */
public class HuffmanUtils {

    public static Map<String, Integer> buildFrequencyTable(String filePath) {

        // @refactor
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String fullFileContent = contentBuilder.toString();
        Map<String, Integer> frequencyTable = new HashMap<>();

        for (char character : fullFileContent.toCharArray()) {
            String tempString = String.valueOf(character);
            Integer frequency = frequencyTable.get(tempString);
            
            if(frequency == null) {
                frequency = 1;
            } else {
                frequency++;
            }
            
            frequencyTable.put(tempString, frequency );
        }

//        for (int i = 0; i < fullFileContent.length(); i++) {
//
//        }
//
//        System.out.println(frequencyTable);

        return HuffmanUtils.sortByValue(frequencyTable);
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
    
    public static void buildEncodingTree(Map<String, Integer> frequencyTable ) {
        
        HuffmanNode root = new HuffmanNode();
        
        for (Entry<String, Integer> entry : frequencyTable.entrySet()) {
            String charater = entry.getKey();
            Integer frequency = entry.getValue();
            
            root.add(charater, frequency);
        }
    }
}
