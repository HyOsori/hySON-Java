package com.osori.hyson;

import java.util.ArrayList;
import java.util.Date;

public class ManyMembers {
	public Integer a;
	public String b;
	@Member(Integer.class)
	public ArrayList<Integer> c;
	public Integer[] d;
	@Member(Temp1.class)
	public ArrayList<Temp1> temps;
	public Date date;
}
