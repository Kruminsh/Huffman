# Huffman Coding Compression Tool
Class project in Riga Technical University

## Description
This tool is a Java-based application that allows users to compress files using the Huffman coding algorithm. The application features a user-friendly interface where users can upload files for compression, streamlining the data compression process.

## Usage
- Launch the application.
- Use the form to upload the file you wish to compress.
- The program will compress the file using Huffman coding.

## Algorithm
The Huffman coding algorithm is a widely used method for data compression. The process begins by building a frequency table of characters from the input text, which helps in understanding how often each character appears. A priority queue is then created based on these frequencies, leading to the construction of a Huffman tree.

In this tree, each node represents a character and its frequency, with the least frequent characters being deeper in the tree. The tree is used to create a unique binary code for each character, with more frequent characters getting shorter codes. This binary code replaces the original characters in the file, leading to compression.

The encoded data, along with the mapping of characters to their binary codes (the Huffman tree), is then saved to a file. For decompression, the process is reversed, using the Huffman tree to convert the binary codes back into the original characters. This algorithm is efficient for files with repetitive characters, achieving significant compression ratios.