package org.jdamico.bc.openpgp.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestString {

	@Test
	public void test() {
		
		String demo = "The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog The qui";
		int slot = 7;
		int size = 20;
		
		List<String> parts = new ArrayList<String>();
		
		int init = 0;
		int end = slot;
		
		for (int i = 0; i < demo.length(); i = i + slot) {
			System.out.println(demo.substring(i, slot));
		}
	}

}
