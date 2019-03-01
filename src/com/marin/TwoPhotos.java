package com.marin;

import java.util.Collections;
import java.util.HashSet;

public class TwoPhotos extends Photo {
	
	public Photo a, b;
	
	public TwoPhotos(Photo a, Photo b){
		this.a = a;
		this.b = b;
		this.index = a.index;
		HashSet<Integer> set = new HashSet<>();
		set.addAll(a.tags);
		set.addAll(b.tags);
		this.tags.addAll(set);
		Collections.sort(this.tags);
	} 
	
	public String printIndex() {
		return "" + a.index + " " + b.index;
	}
}