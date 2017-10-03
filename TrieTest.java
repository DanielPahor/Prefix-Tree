import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

import java.util.*;

public class TrieTest {
	
	// GET TESTS
	@Test
	public void testGetGeneralExample() {
		PrefixMap trie = new Trie();
		trie.put("T", "1");
		
		assertTrue(trie.get("T").equals("1"));
	}
	
	@Test
	public void testGetLongKey() {
		PrefixMap trie = new Trie();
		trie.put("TTTTTTTTTTTTTTTAAAAACCCCCCCGGGTACG", "1");
		
		assertTrue(trie.get("TTTTTTTTTTTTTTTAAAAACCCCCCCGGGTACG").equals("1"));
	}
	
	@Test
	public void testGetManyKeys(){
		PrefixMap trie = new Trie();
		trie.put("TT", "1");
		trie.put("TAGG", "2");
		trie.put("TAGGTTA", "3");
		trie.put("TAGGTTACCCA", "4");
		trie.put("AAAA", "5");
		trie.put("CAAGA", "6");
		trie.put("CGGA", "7");
		trie.put("GGA", "8");
		trie.put("GGCC", "9");
		trie.put("TT", "11");
		
		assertTrue(trie.get("TT") == "11");
		assertTrue(trie.get("TAGG") == "2");
		assertTrue(trie.get("TAGGTTA") == "3");
		assertTrue(trie.get("TAGGTTACCCA") == "4");
		assertTrue(trie.get("AAAA") == "5");
		assertTrue(trie.get("CAAGA") == "6");
		assertTrue(trie.get("CGGA") == "7");
		assertTrue(trie.get("GGA") == "8");
		assertTrue(trie.get("GGCC") == "9");
		assertTrue(trie.size() == 9);
	}
	
	@Test
	public void testGetRoot(){
		PrefixMap trie = new Trie();
		trie.put("", "1");
		trie.put("ATCG", "2");
		trie.put("TCGA", "3");
		
		assertTrue(trie.get("") == "1");
		assertTrue(trie.get("ATCG") == "2");
		assertTrue(trie.get("TCGA") == "3");
	}
	
	// PUT TESTS
	@Test 
	public void testPutUpdate(){
		PrefixMap trie = new Trie();
		trie.put("A", "1");
		trie.put("A", "2");
		
		assertTrue(trie.size() == 1 && trie.get("A") == "2");
		
		trie.put("CCCGGAA", "3");
		trie.put("CCCGGAA", "4");
		trie.put("CCCGGAA", "5");
		
		assertTrue(trie.get("CCCGGAA") == "5");
	}
	
	@Test
	public void testPutKeyCount(){
		PrefixMap trie = new Trie();
		trie.put("A", "1");
		trie.put("AA", "2");
		trie.put("T", "2");
		trie.put("T", "2");
		
		System.out.println(trie.size());
		
		assertTrue(trie.size() == 3);
		trie.put("A", "1");
		trie.put("AA", "2");
		trie.put("T", "2");
		trie.put("T", "2");
	}
	
	@Test
	public void testPutRootTwiceDoesntEffectKeys(){
		PrefixMap trie = new Trie();
		trie.put("AAAA", "1");
		trie.put("CCCC", "2");
		trie.put("", "3");
		trie.put("", "4");
		
		assertTrue(trie.get("AAAA") == "1");
		assertTrue(trie.get("CCCC") == "2");
	}
	
	// REMOVE TESTS
	@Test
	public void testRemoveGeneralExample(){
		PrefixMap trie = new Trie();
		trie.put("ACC","1");
		
		assertTrue(trie.remove("ACC") == "1");
	}
	
	@Test
	public void testRemoveRoot(){
		PrefixMap trie = new Trie();
		trie.put("ACCAAAATTTGGGAAA","1");
		trie.put("C","5");
		trie.put("", "3");
		
		assertTrue(trie.remove("C") == "5");
		assertTrue(trie.remove("ACCAAAATTTGGGAAA") == "1");
		assertTrue(trie.remove("") == "3");
	}
	
	@Test
	public void testRemoveTwice(){
		PrefixMap trie = new Trie();
		trie.put("AAT", "1");
		
		assertTrue(trie.remove("AAT") == "1");
		assertNull(trie.remove("AAT"));
	}
	
	@Test 
	public void testRemoveVeryLongKey(){
		PrefixMap trie = new Trie();
		trie.put("AAAAAATTTTTTTTTTTTTCCCCCCATATATAGGGGGCGCGATAGATATAGACGAGA", "1");
		assertTrue(trie.remove("AAAAAATTTTTTTTTTTTTCCCCCCATATATAGGGGGCGCGATAGATATAGACGAGA") == "1");
	}
	
	// COUNT KEYS MATCHING PREFIX TESTS
	@Test
	public void testCountKeysMatchPrefixGeneralExample() {
		
		PrefixMap trie = new Trie();
		
		trie.put("AA", "1");
		trie.put("AAAATA", "2");
		trie.put("AAAATAA", "3");
		trie.put("CAAAATAA", "4");
		trie.put("GCAAAATAA", "5");
		
		assertTrue(trie.countKeysMatchingPrefix("AA") == 3);
	}
	
	@Test
	public void testCountKeysMatchPrefixRoot() {
		
		PrefixMap trie = new Trie();
		
		assertTrue(trie.countKeysMatchingPrefix("") == 0);
		trie.put("", "1");
		assertTrue(trie.countKeysMatchingPrefix("") == 1);
	}
	
	@Test
	public void testCountKeysMatchPrefixMany() {
		
		PrefixMap trie = new Trie();
		
		trie.put("A", "1");
		trie.put("C", "2");
		trie.put("G", "3");
		trie.put("T", "4");
		trie.put("AT", "5");
		trie.put("CA", "5");
		trie.put("TT", "5");
		trie.put("GTA", "5");
		
		assertTrue(trie.countKeysMatchingPrefix("") == 8);
		assertTrue(trie.countKeysMatchingPrefix("A") == 2);
		assertTrue(trie.countKeysMatchingPrefix("C") == 2);
		assertTrue(trie.countKeysMatchingPrefix("G") == 2);
		assertTrue(trie.countKeysMatchingPrefix("GT") == 1);
	}
	
	// GET KEYS MATCHING PREFIX
	@Test 
	public void testGetKeysMatchingPrefixGeneralExample() {
		
		PrefixMap trie = new Trie();
		
		trie.put("AAAAATTCG","o");
		
		List<String> keys = trie.getKeysMatchingPrefix("AA");
		
		assertTrue(keys.get(0).equals("AAAAATTCG"));
	}
	
	@Test
	public void testGetKeysMatchingPrefixRoot(){
		
		Trie trie = new Trie();
		
		List<String> keysIn = new ArrayList<String>();
		keysIn.add("AAAA");
		keysIn.add("AACAT");
		
		trie.put("AAAA", "1");
		trie.put("AACAT", "2");
		
		List<String> keysOut = new ArrayList<String>();
		keysOut = trie.getKeysMatchingPrefix("");
		
		Collections.sort(keysIn);
		Collections.sort(keysOut);
				
		assertEquals(keysIn, keysOut);
	}
	
	@Test
	public void testGetKeysMatchPrefixEmpty(){
		
		Trie trie = new Trie();
		List<String> empty = new ArrayList<String>();
		
		assertEquals(trie.getKeysMatchingPrefix("AAA"), empty);
	}
	
	// COUNT PREFIXES TEST
	@Test
	public void testCountPrefixesGeneralExample() {
		
		PrefixMap trie = new Trie();
		
		trie.put("AAAA", "1");
		trie.put("AATA", "2");
		
		assertTrue(trie.countPrefixes() == 6);
	}
	
	@Test 
	public void testCountPrefixesEmpty() {
		
		PrefixMap trie = new Trie();
		
		assertTrue(trie.countPrefixes() == 0);
	}
	
	// SUM KEY LENGTH TESTS
	@Test
	public void testSumKeyLengthsGeneralExample() {
		
		PrefixMap trie = new Trie();
		
		trie.put("AA", "1");
		trie.put("AACT", "2");
		trie.put("TCAG", "2");
		
		assertTrue(trie.sumKeyLengths() == 10);
	}
	
	@Test
	public void testSumKeyLengthSimilarKeys() {
		PrefixMap trie = new Trie();
		
		trie.put("AAA", "1");
		trie.put("AAAA", "2");
		
		assertTrue(trie.sumKeyLengths() == 7);
	}
}
