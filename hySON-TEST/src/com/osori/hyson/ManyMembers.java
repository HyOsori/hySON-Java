package com.osori.hyson;

import java.util.ArrayList;

public class ManyMembers {
	public int a;
	public String b;
	@Member(Integer.class)
	public ArrayList<Integer> c;
	public int[] d;
	@Member(Temp1.class)
	public ArrayList<Temp1> temps;

}
