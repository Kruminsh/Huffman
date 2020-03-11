/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

/*
 *  Huffman Node class
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
}
