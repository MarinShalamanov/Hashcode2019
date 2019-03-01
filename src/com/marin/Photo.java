package com.marin;

import java.util.ArrayList;
import java.util.List;

public class Photo {
	public int index;
	public boolean horizontal;
	public List<Integer> tags = new ArrayList<>();
	
	@Override
	public String toString() {
		return "Photo [horizontal=" + horizontal + ", tags=" + tags + "]";
	}
	
	public String printIndex() {
		return "" + index;
	}
	
}


