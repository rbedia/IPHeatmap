/**
 * Copyright (C) 2011 Rafael Bedia
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * http://www.gnu.org/copyleft/gpl.html
 */
package org.trillinux.ipheatmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.StringKeyAnalyzer;
import org.ardverk.collection.Trie;

public class IPLoader {

	Trie<String, Integer> trie;
	
	Map<Integer, Trie<String, Integer>> tries;

	public IPLoader() {
		trie = new PatriciaTrie<String, Integer>(StringKeyAnalyzer.BYTE);
		tries = new HashMap<Integer, Trie<String,Integer>>();
		for (int i = 0; i < 256; i++) {
			tries.put(i, new PatriciaTrie<String, Integer>(StringKeyAnalyzer.BYTE));
		}
	}

	public void load(String dir, String file) throws IOException {
		int base = Integer.parseInt(file.substring(0, file.indexOf('.')));
		FileReader fileReader = new FileReader(new File(dir, file));
		BufferedReader reader = new BufferedReader(fileReader);
		String line;

		while ((line = reader.readLine()) != null) {
			long ip = IPUtil.ipToLong(line);
			BitSet bitSet = ipToBitSet((int) ip);
			String key = bitSetToString(bitSet);
//			trie.put(key, (int) ip);
			tries.get(base).put(key, (int) ip);
		}
		reader.close();
		
		System.out.println("size: " + trieSize());
	}

	public int trieSize() {
		int size = 0;
		for (int i = 0; i < 256; i++) {
			size += tries.get(i).size();
		}
		return size;
	}
	
	public SortedMap<String, Integer> find(String prefix) {
//		SortedMap<String, Integer> matches = trie.prefixMap(prefix);
//		System.out.println("count: " + matches.size());
//		return matches;
		
		SortedMap<String, Integer> all = new TreeMap<String, Integer>();
		
		for (int i = 0; i < 256; i++) {
			SortedMap<String, Integer> matches = tries.get(i).prefixMap(prefix);
			all.putAll(matches);
		}
		System.out.println("count: " + all.size());
		return all;
	}
	
	public BitSet ipToBitSet(int value) {
		BitSet bi = new BitSet();
		for (int i = 31; i >= 0; i--)
			if (((1 << i) & value) != 0)
				bi.set(i);
			else
				bi.clear(i);
		return bi;
	}

	public String bitSetToString(BitSet bitSet) {
		StringBuffer buffer = new StringBuffer(32);
		for (int i = 31; i >= 0; i--) {
			buffer.append(bitSet.get(i) ? '1' : '0');
		}
		return buffer.toString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		long now = System.currentTimeMillis();
		
		IPLoader loader = new IPLoader();
		
		for (int i = 0; i < 90; i++) {
			loader.load("/home/rafael/crawler/leaves/", i + ".txt");
		}

		System.out.println((System.currentTimeMillis() - now) / 1000.0);
		
		now = System.currentTimeMillis();
		loader.find("0000011");
		System.out.println((System.currentTimeMillis() - now) / 1000.0);
	}

}
