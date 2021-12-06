// Import any package as required
import java.util.*;

public class HuffmanSubmit implements Huffman {
  
	// Feel free to add more methods and variables as required. 
	class Node implements Comparable<Node> {
		private int value;
		private char element;
		private Node rightChild;
		private Node leftChild;

		Node(int v, char e) {value = v; rightChild = null; leftChild = null; element = e;}
		Node(int v) {value = v; rightChild = null; leftChild = null; element = '\0';}

		//sets the left child
		void setRightChild(Node rc) {
			rightChild = rc;
		}

		//sets the right child
		void setLeftChild(Node lc) {
			leftChild = lc;
		}

		//returns the left child
		Node returnLeftChild() {
			return leftChild;
		}

		//returns the right child
		Node returnRightChild() {
			return rightChild;
		}

		//returns the char (if available)
		int returnValue() {
			return value;
		}

		public int compareTo(Node other) {
			return this.value - other.returnValue();
		}

		//determines the huffman code (for encoding)
		void huffmanCode(Dictionary<Character, String> dict, char input, String s) {
			if (leftChild == null && rightChild == null && element == input) {
				dict.put(input, s);
				return;
			} else {
				if (leftChild != null) {
					leftChild.huffmanCode(dict, input, s+"0");
				}

				if (rightChild != null) {
					rightChild.huffmanCode(dict, input, s+"1");
				}
			}
		}

		//determines the huffman code (for decoding)
		void huffmanDecode(Dictionary<String, Character> dict, char input, String s) {
			if (leftChild == null && rightChild == null && element == input) {
				dict.put(s, input);
				return;
			} else {
				if (leftChild != null) {
					leftChild.huffmanDecode(dict, input, s+"0");
				}

				if (rightChild != null) {
					rightChild.huffmanDecode(dict, input, s+"1");
				}
			}
		}
	}
 
	public void encode(String inputFile, String outputFile, String freqFile){
		BinaryIn input = new BinaryIn(inputFile);
		
		
		Dictionary<Character, Integer> freqDict = new Hashtable<Character, Integer>();
		char currentValue = '\0';
		PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();

		//finds the frequency of chars in the inputted source (or reads every 8 bits)
		//and is put into a dictionary (freqDict)
		while (true) {
			try {
				currentValue = input.readChar();
				if (freqDict.get(currentValue) != null) {
					freqDict.put(currentValue, freqDict.get(currentValue)+1);
				} else {
					freqDict.put(currentValue, 1);
				}
			} catch (NoSuchElementException e) { //when the file has been fully read, breaks out of the while loop
				break;
			}
		}

		//writes each frequency onto the frequency file (traversing through the dictionary)
		//also adds frequencies into a priority queue for the huffman tree
		BinaryOut freqOut = new BinaryOut(freqFile);
		Enumeration<Character> u = freqDict.keys();
		for (Enumeration<Integer> v = freqDict.elements(); v.hasMoreElements();) {
			int intTemp = (int)v.nextElement();
			char charTemp = u.nextElement();
			priorityQueue.add(new Node(intTemp, charTemp));

			freqOut.write(Integer.toBinaryString(charTemp)+":"+intTemp+"\n");
        }
		freqOut.close();

		//this creates the huffman tree
		Node tmp1, tmp2, tmp3 = null;
		while (priorityQueue.size() > 1) {
			tmp1 = priorityQueue.poll();
			tmp2 = priorityQueue.poll();
			tmp3 = new Node(tmp1.returnValue()+tmp2.returnValue());
			tmp3.setLeftChild(tmp1);
			tmp3.setRightChild(tmp2);
			priorityQueue.add(tmp3);
		}
		Node huffmanTree = priorityQueue.poll();

		Dictionary<Character, String> huffmanValues = new Hashtable<Character, String>();

		//finds the huffman codes for every char
		for (u = freqDict.keys(); u.hasMoreElements();) {
			char charTemp = u.nextElement();
			huffmanTree.huffmanCode(huffmanValues, charTemp, "");
		}

		BinaryOut encodeOut = new BinaryOut(outputFile);
		input = new BinaryIn(inputFile);

		//reads the input file again, and for every char, writes the huffman value 
		//onto the output file
		while (true) {
			try {
				currentValue = input.readChar();
				String tempString = huffmanValues.get(currentValue);
				for (int i=0; i<tempString.length(); i++) {
					if (tempString.charAt(i) == '0') {
						encodeOut.write(false);
					} else {
						encodeOut.write(true);
					}
				}
			} catch (Exception NoSuchElementException) {
				break;
			}
		}
		encodeOut.close();

    }

   	public void decode(String inputFile, String outputFile, String freqFile){
		BinaryIn freqInfo = new BinaryIn(freqFile);
		String tempString = "";
		int tempInt = 0;
		char tempChar = '\0';
		char dictChar = '\0';
		int dictInt = 0;
		Dictionary<Character, Integer> freqDict = new Hashtable<Character, Integer>();
		PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();

		//reads the frequency file and places the values into a dictionary (freqDict)
		while (true) {
			try {
				tempString = Character.toString(freqInfo.readChar());
				tempChar = freqInfo.readChar();
				while (tempChar != ':')  {
					tempString += tempChar;
					tempChar =  freqInfo.readChar();
				}
				tempInt = Integer.parseInt(tempString, 2);
				dictChar = (char)tempInt;

				tempChar =  freqInfo.readChar();
				tempString = "";
				while (tempChar != '\n')  {
					tempString += tempChar;
					tempChar =  freqInfo.readChar();
				}
				dictInt = Integer.parseInt(tempString);

				freqDict.put(dictChar, dictInt);
			} catch (Exception NoSuchElementException) {
				break;
			}
		}

		//adds all the frequencies into a priority queue
		Enumeration<Character> u = freqDict.keys();
		for (Enumeration<Integer> v = freqDict.elements(); v.hasMoreElements();) {
			int intTemp = (int)v.nextElement();
			char charTemp = u.nextElement();
			priorityQueue.add(new Node(intTemp, charTemp));
        }

		//creates the huffman tree
		Node tmp1, tmp2, tmp3 = null;
		while (priorityQueue.size() > 1) {
			tmp1 = priorityQueue.poll();
			tmp2 = priorityQueue.poll();
			tmp3 = new Node(tmp1.returnValue()+tmp2.returnValue());
			tmp3.setLeftChild(tmp1);
			tmp3.setRightChild(tmp2);
			priorityQueue.add(tmp3);
		}
		Node huffmanTree = priorityQueue.poll();

		Dictionary<String, Character> huffmanValues = new Hashtable<String, Character>();

		//creates the huffman values for all chars 
		for (u = freqDict.keys(); u.hasMoreElements();) {
			char charTemp = u.nextElement();
			huffmanTree.huffmanDecode(huffmanValues, charTemp, "");
		}

		BinaryIn input = new BinaryIn(inputFile);
		BinaryOut output = new BinaryOut(outputFile);

		//reads the encoded file, and everytime it comes across a valid huffman code,
		//writes this character into the output file
		while (true) {
			try {
				if (input.readBoolean() == true) {
					tempString = "1";
				} else {
					tempString = "0";
				}
				while (huffmanValues.get(tempString) == null) {
					if (input.readBoolean() == true) {
						tempString += "1";
					} else {
						tempString += "0";
					}
				}
				output.write(Character.toString(huffmanValues.get(tempString)));
				output.flush();
			} catch (Exception NoSuchElementException) {
				output.flush();
				break;
			}
		}
		output.close();
   	}




   	public static void main(String[] args) {
      	Huffman  huffman = new HuffmanSubmit();
		huffman.encode("alice30.txt", "ur.enc", "freq.txt");

		huffman.decode("ur.enc", "alice30_dec.txt", "freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   	}
}
