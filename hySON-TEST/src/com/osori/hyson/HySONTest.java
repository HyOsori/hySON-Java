package com.osori.hyson;

import static org.junit.Assert.*;

import org.junit.Test;

public class HySONTest {

	/** String Array Parsing test **/
	
	@Test
	public void testParseEmptyStringArray() {
		String jsonString = "[]";
		HySON hySon = new HySON();
		hySon.parse(jsonString);
		
		String[] results = hySon.getArrayString(jsonString);
		assertEquals(0, results.length);
	}
	
	@Test
	public void testParseStringArray() {
		String jsonString = "[\"a\", \"b\", \"c\"]}";
		HySON hySon = new HySON();
		hySon.parse(jsonString);
		
		String[] results = null;

		results = hySon.getArrayString(jsonString);
		
		assertEquals(3, results.length);
		assertEquals("a", results[0]);
		assertEquals("b", results[1]);
		assertEquals("c", results[2]);
	}
	
	@Test
	public void testParseWithConstructor() {
		String jsonString = "[\"a\", \"b\", \"c\"]}";
		HySON hySon = new HySON(jsonString);
		
		String[] results = null;

		results = hySon.getArrayString(jsonString);
		
		assertEquals(3, results.length);
		assertEquals("a", results[0]);
		assertEquals("b", results[1]);
		assertEquals("c", results[2]);
	}

	/** Boolean Array Parsing test **/
	
	@Test
	public void testParseEmptBooleanArray() {
		String jsonString = "[]";
		HySON hySon = new HySON();
		hySon.parse(jsonString);
		
		Boolean[] results = hySon.getArrayBoolean(jsonString);
		assertEquals(0, results.length);
	}
	
	@Test
	public void testParseBooleanArray() {
		String jsonString = "[\"true\", \"true\", \"false\"]}";
		HySON hySon = new HySON();
		hySon.parse(jsonString);
		
		Boolean[] results = null;

		results = hySon.getArrayBoolean(jsonString);
		
		assertEquals(3, results.length);
		assertTrue(results[0]);
		assertTrue(results[1]);
		assertFalse(results[2]);
	}
	
	@Test
	public void testParseBooleanWithConstructor() {
		String jsonString = "[\"true\", \"true\", \"false\"]}";
		HySON hySon = new HySON(jsonString);
		
		Boolean[] results = null;

		results = hySon.getArrayBoolean(jsonString);
		
		assertEquals(3, results.length);
		assertTrue(results[0]);
		assertTrue(results[1]);
		assertFalse(results[2]);
	}
	
	/** integer Array Parsing Test **/
	@Test
	public void testParseEmptIntArray() {
		String jsonString = "[]";
		HySON hySon = new HySON();
		hySon.parse(jsonString);
		
		int[] results = hySon.getArrayInt(jsonString);
		assertEquals(0, results.length);
	}
		
	@Test
	public void testParseIntArray() {
		String jsonString = "[\"1\", \"2\", \"3\"]}";
		HySON hySon = new HySON();
		hySon.parse(jsonString);
		
		int[] results = null;

		results = hySon.getArrayInt(jsonString);
		
		assertEquals(3, results.length);
		assertEquals(1, results[0]);
		assertEquals(2, results[1]);
		assertEquals(3, results[2]);
	}
	
	@Test
	public void testParseIntWithConstructor() {
		String jsonString = "[\"10\", \"20\", \"30\"]}";
		HySON hySon = new HySON(jsonString);
		
		int[] results = null;

		results = hySon.getArrayInt(jsonString);
		
		assertEquals(3, results.length);
		assertEquals(10, results[0]);
		assertEquals(20, results[1]);
		assertEquals(30, results[2]);
	}
	
	/* -- */	
	public void test1(){
		
		HySON hyson = new HySON("{ \"value\" : 10 } ");
		Temp t = hyson.parse(Temp.class);
		
		assertEquals(10, t);
		
	}
	
	public void test2(){
		
		HySON hyson = new HySON("{ \"object\" : { \"value\" : 10 } } ");
		Temp t = hyson.parse("object", Temp.class);
		
		assertEquals(10, t);
		
	}
	
	public void test3(){
		
		HySON hyson = new HySON("{ \"temp\" : { \"value\" : 10 } } ");
		Temp2 t = hyson.parse(Temp2.class);
		
		assertEquals(10, t);
		
	}
	
	public void test4(){
		
		public Object[] parseArray(Class class) {
		}


		HySON hyson = new HySON("[{\"value\": 0}]");
		Temp[] t = hyson.parseArray(Temp.class);
		//Temp[] t = (Temp[])hyson.parseArray(Temp.class);
		
		assertEquals(10, t);
		
	}
	
	public void test5(){

		HySON hyson = new HySON("{\"temps\" :[{\"value\": 0}]}");
		Temp3 t = hyson.parse(Temp3.class);
		
		assertEquals(10, t);
		
	}
	
	public void test6(){
		
		HySON hyson = new HySON("{\"member1\" : [ {\"temps\" :[{\"value\": 0}]}, {\"temps\" :[{\"value\": 1} ]}");
		Temp4 t = hyson.parse(Temp4.class);
		
		assertEquals(10, t);
		
	}
	
	public void test7(){

		HySON hyson = new HySON("{\"a\": 1, \"b\" : \"횟횟\", \"c\" : [2, 3, 4], \"d\": [5, 6], \"temps\":[{\"value\": 0}]}");
		ManyMembers many = hyson.parse(ManyMembers.class);
		
		assertEquals(10, t);
		
	}

}

