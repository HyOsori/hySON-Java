package com.osori.hyson.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.osori.hyson.HySON;
import com.osori.hyson.Test7Class;
import com.osori.hyson.test.data.StringValue;

public class ErrorHandleTest {
	@Test
	public void testEmptyJsonError() {
		String jsonString = "{}";
		StringValue result = HySON.parse(jsonString, StringValue.class);
		
		assertNotNull(result);
		assertNull(result.value);
	}
	
	@Test
	public void testStringError() {
		String jsonString = "{\"v\" : \"error\"}";
		StringValue result = HySON.parse(jsonString, StringValue.class);
		
		assertNotNull(result);
		assertNull(result.value);
	}
	
	@Test
	public void test7Error1() {
		String jsonString = "{\"a\": 10, \"c\" : [2, 3, 4], \"temps\":[{\"value\": 20}], \"e\": 2}";
		Test7Class t = HySON.parse(jsonString, Test7Class.class);
		
		assertNotNull(t);
		assertNull(t.b);
		assertEquals(10, t.a);
		
		assertNotNull(t.c);
		assertEquals(3, t.c.size());
		assertEquals(2, t.c.get(0).intValue());
		assertEquals(3, t.c.get(1).intValue());
		assertEquals(4, t.c.get(2).intValue());
		
		assertNull(t.d);
		
		assertNotNull(t.temps);
		assertEquals(1, t.temps.size());
		assertNotNull(t.temps.get(0));
		assertEquals(20, t.temps.get(0).value);
	}
}
