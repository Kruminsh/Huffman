/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author kruminsh
 */
public class HuffmanCoding {
    
    public String outputMessage = "";
    
    HuffmanCoding() {
        
    }
    
    public static Map<String, Integer> buildFrequencyTable(String content) {
        Map<String, Integer> frequencyTable = new HashMap<>();

        for (char character : content.toCharArray()) {
            String tempString = String.valueOf(character);
            Integer frequency = frequencyTable.get(tempString);

            if (frequency == null) {
                frequency = 1;
            } else {
                frequency++;
            }

            frequencyTable.put(tempString, frequency);
        }
        return new TreeMap<String, Integer>(frequencyTable);
    }
    
    public static Map<String, Integer> parseToFrequencyTable(String table) {
        String characterEncodingArray[] = table.split(",");     
        Map<String, Integer> freqTable = new HashMap<>();
        
        for (String keyValuePair : characterEncodingArray) {
            String[] keyValue = keyValuePair.split("=");
            int asciiCode = Integer.valueOf(keyValue[0]);
            freqTable.put(String.valueOf((char) asciiCode), Integer.valueOf(keyValue[1]));
        }
        
        return new TreeMap<String, Integer>(freqTable);
    }
    
    public static PriorityQueue<HuffmanNode> buildPriorityQueue(Map<String, Integer> table) {
        PriorityQueue<HuffmanNode> queue = new PriorityQueue(table.size(), new HuffmanComperator());

        table.entrySet().forEach((entry) -> {
            char charater = entry.getKey().charAt(0);
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
            f.character = Character.MIN_VALUE;

            f.zero = x;
            f.one = y;

            root = f;

            queue.add(f);
        }

        return root;
    }
     
    static void buildEncodingMap(HuffmanNode root, String s, Map<Character, String> codeMap) {

        if (root.character != Character.MIN_VALUE) {
            codeMap.put(root.character, s);
        }

        if (root.zero != null) {
            buildEncodingMap(root.zero, s + "0", codeMap);
        }

        if (root.one != null) {
            buildEncodingMap(root.one, s + "1", codeMap);
        }

    }
    
    public static String encodeFileContentByCodeMap(String fileContent, Map<Character, String> codeMap) {
        StringBuilder encodedContent = new StringBuilder();
        for (char c : fileContent.toCharArray()) {
            encodedContent.append(codeMap.get(c));
        }

        return encodedContent.toString();
    }
    
    public static void writeCodeMapToFile(FileWriter myWriter, Map<String, Integer> freqTable) throws IOException {
        myWriter.write("{");
        int mapCounter = 0;
        int mapLength = freqTable.size();
        for (Map.Entry<String, Integer> entry : freqTable.entrySet()) {
            mapCounter++;

            myWriter.write((int)entry.getKey().charAt(0) + "=" + entry.getValue() + (mapCounter != mapLength ? "," : ""));
        }
        myWriter.write("}");
    }

    static void writeEncodedStringToFile(FileWriter myWriter, String encodedString) throws IOException {
        int spot = 0;
        byte[] bytes = new byte[256];
        
        System.out.println("encoding");
        for (int i = 0; encodedString.length() > 7; i++) {
            String temp = encodedString.substring(0, 7);
            encodedString = encodedString.substring(7, encodedString.length());
            bytes[spot] = Byte.parseByte(temp, 2);
            spot++;
        }
        System.out.println("encoding-done");

        for (int i = 0; i <= spot; i++) {
            myWriter.write(bytes[i]);
        }
    }
    
    public Boolean encode(File file) {
        StringBuilder fileContentBuilder = new StringBuilder();
        FileUtils.readFileIntoStringBuild(fileContentBuilder, file.getPath());
        
        Map<String, Integer> freqTable = buildFrequencyTable(fileContentBuilder.toString());
        
        System.out.println(freqTable);

        PriorityQueue<HuffmanNode> queue = buildPriorityQueue(freqTable);

        HuffmanNode root = buildEncodingTree(queue);

        Map<Character, String> codeMap = new HashMap<>();
        buildEncodingMap(root, "", codeMap);

        System.out.println(codeMap);

        File encodedFile = new File("./file-encoded.huf");
        try (FileWriter myWriter = new FileWriter(encodedFile)) {
            String encodedString = encodeFileContentByCodeMap(fileContentBuilder.toString(), codeMap);
            
            writeCodeMapToFile(myWriter, freqTable);
            writeEncodedStringToFile(myWriter, encodedString);
            
            this.outputMessage = "ENCODED FILE SUCCESSFULLY\n" + encodedFile.getCanonicalPath();
            return true;
            
        } catch (IOException e) {
            e.printStackTrace();
            this.outputMessage = "Oops, unexpected error!\n" + e.getMessage();
            return false;
        }
    }
    
    public static String decodeFileContent(String encodedContent, Map<String, Character> encodingMap) {
        StringBuilder decodedTextBuilder = new StringBuilder();
        StringBuilder encodedByteBuilder = new StringBuilder();
        for (char encodedChar : encodedContent.toCharArray()) {
            encodedByteBuilder.append(encodedChar);

            if (encodingMap.containsKey(encodedByteBuilder.toString())) {
                decodedTextBuilder.append(encodingMap.get(encodedByteBuilder.toString()));
                encodedByteBuilder = new StringBuilder();
            }
        }
        String decodedText = decodedTextBuilder.toString();
        return decodedText.substring(0, decodedText.length() - 1);
    }
    
    public static String byteContentToStringBytes(byte[] encodedContent, Map<String,Character> encodingMap, int freqByteLength) {
        StringBuilder byteStringBuilder = new StringBuilder();
        for (int i = freqByteLength; i < encodedContent.length; i++) {
            byte single = encodedContent[i];
            String formatedStingByte = String.format("%7s", Integer.toBinaryString(single & 0xFF)).replace(' ', '0');
            byteStringBuilder.append(formatedStingByte);
        }

        return byteStringBuilder.toString();
    }
    
    public Boolean decode(File file) {
        StringBuilder encodedFileContentBuilder = new StringBuilder();
        FileUtils.readFileIntoStringBuild(encodedFileContentBuilder, file.getPath());
        String encodedFileContent = encodedFileContentBuilder.toString();
        
        int substringStartOffset = 1;
        int indexOfEncodingTableStart = encodedFileContent.indexOf("{");
        int indexOfEncodingTableEnd = encodedFileContent.indexOf("}");
        
        String encodingTableString = encodedFileContent.substring(indexOfEncodingTableStart+ substringStartOffset, indexOfEncodingTableEnd);
        Map<String, Integer> freqTable = parseToFrequencyTable(encodingTableString);
        
        PriorityQueue<HuffmanNode> queue = buildPriorityQueue(freqTable);
        
        HuffmanNode root = buildEncodingTree(queue);

        Map<Character, String> codeMap = new HashMap<>();
        buildEncodingMap(root, "", codeMap);
        
        System.out.println(codeMap.toString());
        Map<String, Character> encodingMap = codeMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());           
            byte[] freqTableBytes = ("{" + encodingTableString + "}").getBytes(); //Bytes that take up frequency table in the file
            
            String encodedContent = byteContentToStringBytes(fileContent, encodingMap, freqTableBytes.length);
            
            System.out.println("decoding-start");
            String decodedText = decodeFileContent(encodedContent, encodingMap);
            System.out.println("decoding-end");
            
            File decodedFile = new File("./file-decoded.txt");
            try ( FileWriter myWriter = new FileWriter(decodedFile)) {

                myWriter.write(decodedText);
                this.outputMessage = "CREATED DECODED FILE SUCCESFULLY\n" + decodedFile.getCanonicalPath();
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                this.outputMessage = "Oops, unexpected error!\n" + ex.getMessage();
                return false;
            }
        } catch (Exception ex){
            ex.printStackTrace();
            this.outputMessage = "Oops, unexpected error!\n" + ex.getMessage();
            return false;
        }
    }
}
