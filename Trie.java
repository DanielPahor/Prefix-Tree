import java.util.*;

public class Trie implements PrefixMap {

	// Trie variables
	private int size;
	private Node root;
	
	private class Node {
		
		// Node variables
		private Node parent;
		private Node[] children;
		private String value;
		
		// constructors
		public Node(){
			children = new Node[4];
		}
		
		public Node(String val){
			children = new Node[4];
			value = val;
		}
		
		// getters
		private Node getParent(){
			return parent;
		}
		
		private String getValue(){
			return value;
		}
		
		private Node[] getChildren(){
			return children;
		}
		
		private Node getChild(int index){
			return children[index];
		}
		
		// setters
		private void setParent(Node parent){
			this.parent = parent;
		}
		
		private void setValue(String value){
			this.value = value;
		}
		
		private void setChild(int index, Node children){
			this.children[index] = children;
		}
		
		/*
		 * node helper methods
		 */

		private boolean hasValue(){
			if(this.value != null){
				return true;
			}
			return false;
		}
		
		private Node setChild(char c, Node newNode){
			this.children[index(c)] = newNode;
			return newNode;
		}
		
		private boolean hasChildren(){
			for(int i = 0; i <= this.children.length-1; i++){
				if(children[i] != null){
					return true;
				}
			}
			return false;
		}
		
	}
	
	// Trie constructor
	public Trie() {
		root = new Node();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		if(size == 0){
			return true;
		}
		return false;
	}
	
	public Node getRoot() {
		return this.root;
	}

	@Override
	public String get(String key) {
		
		// check valid parameters 
		if(key == null){
			throw new IllegalArgumentException();
		}
		if(!isValidKey(key)){
			throw new MalformedKeyException();
		}
		
		// start at root
		Node current = this.root;
		
		// find node at end of the key (if it exists)
		for(char k : key.toCharArray()){
			int index = index(k);
			if(current.children[index] != null){
				current = current.getChild(index);
			}else{
				return null;
			}
		}
		
		// return the value of the end node
		return current.getValue();	
	}

	@Override
	public String put(String key, String value) {
		
		// check valid parameters 
		if(key == null || value == null){
			throw new IllegalArgumentException();
		}
		if(!isValidKey(key)){
			throw new MalformedKeyException();
		}
		
		// if key is empty
		if(key == ""){
			// if root has value, update with value and return
			if(root.hasValue()){
				String old = root.getValue();
				root.setValue(value);
				return old;
			}
			// if root does not have value, update, increment, and return
			root.setValue(value);
			this.size++;
			return null;
		}
		
		// start at root
		Node current = root;
		
		// find node at end of key (if it exists)
		for(char k : key.toCharArray()){
			int index = index(k);
			if(current.getChild(index) != null){
				current = current.getChild(index);
			}else{
				// build new node if path doesn't exist
				Node newNode = new Node();
				newNode.setParent(current);
				current = current.setChild(k, newNode);
			}
		}
		
		// if the node has a value, update it and return
		if(current.hasValue()){
			String old = current.value;
			current.value = value;
			return old;
		}
		
		// if the node does not have a value, set value, increment, and return
		current.value = value;
		this.size++;
		return null;
	}

	@Override
	public String remove(String key) {
		
		// check valid parameters 
		if(key == null){
			throw new IllegalArgumentException();
		}
		if(!isValidKey(key)){
			throw new MalformedKeyException();
		}
		
		// start at root
		Node current = root;
	
		// find node at end of the key (if it exists)
		for(char k : key.toCharArray()){
			int index = index(k);
			if(current.getChild(index) != null){
				current = current.getChild(index);
			}else{
				return null;
			}
		}
		
		// if root node, remove key, decrement, and return
		if(current == root && current.hasValue()){
			String old = root.value;
			root.setValue(null);
			this.size--;
			return old;
		}
		
		// otherwise, if the node has a value
		if(current.hasValue()){
			
			String old = current.getValue();

			// remove the key and decrement size
			int index = key.length()-1;
			current.setValue(null);
			this.size--; 
			
			// travel back up the tree and remove any 
			// nodes that have no value and no children
			while (!current.hasChildren() && !current.hasValue() && index >= 0){
				
				// remove the node
				Node parent = current.getParent();
				current.setParent(null);
				parent.setChild(index(key.charAt(index)), null);
				current = parent;
				
				index--;
			}
			return old;
		}
		return null;
	}

	@Override
	public int countKeysMatchingPrefix(String prefix) {
		
		// check valid parameters 
		if(prefix == null){
			throw new IllegalArgumentException();
		}
		if(!isValidKey(prefix)){
			throw new MalformedKeyException();
		}
		
		// start at root
		Node current = root;
		
		// find node at end of the prefix (if it exists)
		for(char k : prefix.toCharArray()){
			int index = index(k);
			if(current.children[index] != null){
				current = current.getChild(index);
			}else{
				return 0;
			}
		}
		
		// count the amount of keys that
		// come from this node
		return countKeysMatchingPrefix(current);
	}
	
	private int countKeysMatchingPrefix(Node node) {
		
		if(node == null){
			return 0;
		}
		
		int count = 0;
		
		// increment if key is found
		if(node.hasValue()){
			count++;
		}
		
		// search all children
		for(Node n: node.getChildren()){
			// add the result of the search
			// to the counter
			count += countKeysMatchingPrefix(n);
		}
		
		// return amount of keys found
		return count;
	}
	
	@Override
	public List<String> getKeysMatchingPrefix(String prefix) {
		
		// check valid parameters 
		if(prefix == null){
			throw new IllegalArgumentException();
		}
		if(!isValidKey(prefix)){
			throw new MalformedKeyException();
		}
		
		List<String> keys = new ArrayList<String>();
		
		// start at root
		Node current = root;
		
		// find node at end of the prefix (if it exists)
		for(char k : prefix.toCharArray()){
			int index = index(k);
			if(current.getChild(index) != null){
				current = current.getChild(index);
			}else{
				return keys;
			}
		}
		
		// make new list
		List<String> vals = new ArrayList<String>();
		
		// get all the values of keys that descend from this node
		vals = getAllValuesFromNode(current, vals);
		
		// convert values to array
		String[] valuesArr = new String[vals.size()];
		valuesArr = vals.toArray(valuesArr);
		
		// loop through values
		for(String s : valuesArr){
			String key = prefix;
			// find the corresponding key and add it to the list
			if(getSingleKeyMatchingPrefix(current, key, s) != null){
				keys.add(getSingleKeyMatchingPrefix(current, key, s));
			}
		}
		return keys;
	}
	
	/*
	 * helper methods for getKeysMatchingPrefix
	 */
	
	// gets all values that descend from a node
	public List<String> getAllValuesFromNode(Node node, List<String> values) {
		
		// null check
		if(node == null){
			return values;
		}
		
		// if node has a value, add it to the list
		if(node.hasValue()){
			values.add(node.getValue());
		}
		
		// search all children
		for(Node n: node.getChildren()){
			getAllValuesFromNode(n, values);
		}
		return values;
	}
	
	// gets the key corresponding to a value from a prefix
	public String getSingleKeyMatchingPrefix(Node node, String key, String value) {
		
		if(node.getValue() == value){
			return key;
		}
		
		// traverse from end of prefix to key
		for(int i = 0; i < 4; i++){
			Node n = node.getChildren()[i];
			if(n != null){
				// avoid path if it does not contain the value
				if(!searchPathInvalid(n, value)){
					key += index(i);
					
					return getSingleKeyMatchingPrefix(n, key, value);
				}
			}
		}
		return key;
	}
	
	// returns false if value lies in path
	public boolean searchPathInvalid(Node node, String val){
		
		boolean valCheck = true;
		
		// if value is found, set check to false 
		// (as the search path is no longer null)
		if(node.getValue() == val){
			valCheck = false;
		} 
		
		// test if children contain valid paths
		for(Node n : node.getChildren()){
			if(n!=null){
				valCheck =  valCheck && searchPathInvalid(n, val);
			}
		}
		return valCheck;
	}
	
	@Override
	public int countPrefixes() {
		
		// get all keys
		List<String> keys = getKeysMatchingPrefix("");
		
		int total = 0;
		List<String> found = new ArrayList<String>();
		
		// possibly unnecessary, could put the 'find at end of key'
		// into a separete function
		
		// loop through keys
		for(int i = 0; i < keys.size(); i++){
			String key = keys.get(i);
			// loop through each character of a key
			for(int j = 0; j < key.length(); j++){
				String substr = key.substring(0,j+1);
				// if prefix is unique
				if(!found.contains(substr)){
					// add it to the list
					found.add(substr);
				}
			}
		}
		
		// count the total length of all prefixes found
		for(int i = 0; i < found.size(); i++){
			total++;
		}
		
		return total;
	}

	@Override
	public int sumKeyLengths() {
		
		// get all keys
		List<String> keys = getKeysMatchingPrefix("");
				
		int total = 0;
		
		// loop through keys
		for(int i = 0; i < keys.size(); i++){
			total += keys.get(i).length();
		}
		
		return total;
	}
	
	/*
	 * general helper methods 
	 */
	
	public boolean isValidKey(String key){
		for(char k : key.toCharArray()){
			if(k != 'A' && k != 'C' && k != 'G' && k != 'T'){
				return false;
			}
		}
		return true;
	}
	
	public Integer index(char k){
		if(k == 'A'){
			return 0;
		}else if (k == 'C'){
			return 1;
		}else if (k == 'G'){
			return 2;
		}else if (k == 'T'){
			return 3;
		}
		return null;
	}
	
	public String index(int i){
		if(i == 0){
			return "A";
		}else if (i == 1){
			return "C";
		}else if (i == 2){
			return "G";
		}else if (i == 3){
			return "T";
		} return null;
	}
	
	// find at end of key 
	public Node findEndOfKeyNode(String prefix){
		
		Node current = root; 
		
		for(Char c : prefix.toCharArray()){
			if(c != null){
				// find child 
			}
		}
		
		return current; 
	}
}