/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

/**
 *
 * @author arturs vitolins
 */
public class HuffmanNode {

    public char character;
    public int frequency;
    public HuffmanNode zero;
    public HuffmanNode one;

    public HuffmanNode() {

    }

    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public void add(char charater, int frequency) {
        if (this.zero == null) {
            zero = new HuffmanNode();
            zero.character = charater;
            zero.frequency = frequency;
        } else {
            one = new HuffmanNode();
            one.character = charater;
            one.frequency = frequency;
        }
    }
}
