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
    public String character;
    public int count;
    public HuffmanNode zero;
    public HuffmanNode one;
    
    public HuffmanNode() {
        
    }
    
    public void add(String charater, int count) {
        if( this.zero == null) {
            zero = new HuffmanNode();
            zero.character = charater;
            zero.count = count;
        } else {
            one = new HuffmanNode();
            one.character = charater;
            one.count = count;
        }
    }
    
}
