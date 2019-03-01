package com.marin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
	
	static String files[] = new String[] {
			"/hashcode/a.txt",
			"/hashcode/b.txt",
			"/hashcode/c.txt",
			"/hashcode/d.txt",
			"/hashcode/e.txt"
	};
	
	public static List<Photo> readFile(String filename) throws FileNotFoundException {
		System.out.println("reding");
		Scanner in = new Scanner(new File(filename));
		int n = in.nextInt();
		
		List<Photo> photos = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			Photo ph = new Photo();
			ph.index = i;	
			ph.horizontal = in.next().charAt(0) == 'H';
			int m = in.nextInt();
			for (int j = 0; j < m; j++) {
				ph.tags.add(in.next().hashCode());
			}
			Collections.sort(ph.tags);
			photos.add(ph);
		}
		
		in.close();
		return photos;
	}
	
	public static int getUnionOfTags(List<Photo> photos) {
		HashSet<Integer> s = new HashSet<>();
		for (Photo p : photos) {
			s.addAll(p.tags);
		}
		return s.size();
	}
	
	public static int score(List<Integer> tags1, List<Integer> tags2) {
        int common = 0;
        int diff1 = 0, diff2 = 0;
        HashSet<Integer> tagsSet1 = new HashSet<>();
        HashSet<Integer> tagsSet2 = new HashSet<>();
        for (Integer tag1: tags1) {
            tagsSet1.add(tag1);
        }
        for (Integer tag2: tags2) {
            tagsSet2.add(tag2);
        }
        for (Integer tag1: tags1) {
            if (tagsSet2.contains(tag1)) common++;
            else diff1++;
        }
        diff2 = tags1.size()+tags2.size()-common-common-diff1;
//        System.out.println(common +" " + diff1 + " " + diff2);
        return Math.min(common, Math.min(diff1, diff2));
    }
	
	
	// THIS CHANGES THE ORDER 
	public static void randomizedGreedy(List<Photo> photos) 
	{
		System.out.println("randomized greedy");
		Collections.shuffle(photos);
		Random r = new Random(41);
		
		for (int idx = 1; idx < photos.size(); idx++) {
			int NUM_SAMPLES = Math.min(photos.size() - idx, 2000);
			if (FILE == 2) NUM_SAMPLES = photos.size() - idx;
			
			int bestIdx = -1;
			int bestScore = -1;
			
			if (idx % 500 == 0) System.out.println(idx);
			
			for (int j = 0; j < NUM_SAMPLES; j++) {
				int next = idx + r.nextInt(photos.size() - idx);
				Photo np = photos.get(next);
				int sc = score(photos.get(idx-1).tags, np.tags);
				if (sc > bestScore) {
					bestScore = sc;
					bestIdx = next;
				}
			}
			
			Collections.swap(photos, idx, bestIdx);
		}
	}
	
	public static List<ArrayList<Integer>> readScores() throws Exception {
		List<ArrayList<Integer>> scores = new ArrayList<>();
		char filec = (char) ('a' + FILE);
		String filename = "/hashcode/" + filec + "_scores.txt";
		//Scanner in = new Scanner(new File(filename));
		BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
		int i = 0;
		
		while(true) {
			String line = in.readLine();
			if(line == null) break;
			String[] splits = line.split(" ");
			
			if (Integer.parseInt(splits[0]) != i) System.out.println("ERROR");
			ArrayList<Integer> list = new ArrayList<>();
			for (int j = 1; j < Math.min(1000, splits.length); j++) {
				if (!splits[j].isEmpty())
					list.add(Integer.parseInt(splits[j]));
			}
			
			scores.add(list);
			
			if( i % 5000 == 0) System.out.print(i + " ");
			i++;
		}
		
		in.close();
		return scores;
	}
	
	// THIS CHANGES THE ORDER 
	public static List<Photo> randomizedGreedyScores(List<Photo> photos, List<Photo> original) throws Exception 
	{
		
		System.out.println("randomized greedy");
		System.out.println("orig size: " + original.size());
		
		List<Photo> sol = new ArrayList<>();
		sol.add(photos.get(0));
		
		List<ArrayList<Integer>> scores = readScores();
		System.out.println("score size" + 	scores.size());
		HashSet<Integer> notUsed = new HashSet<>();
		for(Photo p : photos) notUsed.add(p.index);
		
		notUsed.remove(photos.get(0).index);
		
		for (int idx = 1; idx < photos.size(); idx++) {
			
			int index = sol.get(idx-1).index;
			ArrayList<Integer> others = scores.get(index);
			
			if (idx % 500 == 0) System.out.println(idx);
			
			boolean done = false;
			for (int j = 0; j < others.size(); j++) {
				int otherIdx = others.get(j);
				Photo other = original.get(otherIdx);
				if (notUsed.contains(otherIdx)) {
					
					sol.add(other);
					notUsed.remove(otherIdx);
					done = true;
					break;
				}
			}
			
			if(!done) {
				Integer next = notUsed.iterator().next();
				Photo nextPhoto = original.get(next);
				sol.add(nextPhoto);
				notUsed.remove(next);
			}
			
			
		}	
		
		return sol;
	}
	
	public static void hillClimb(List<Photo> slideshow) {
		List<Integer> scores = new ArrayList<>();
		for (int i = 1; i < slideshow.size(); i++) {
			scores.add(score(slideshow.get(i).tags, slideshow.get(i-1).tags));
		}
		
		Random r = new Random(42);
		
		for(int k = 0; k < 100; k++) {
			int min_val = Integer.MAX_VALUE, min_pos;
			for (int i = 0; i < scores.size(); i++) {
				if(scores.get(i) < min_val) {
					min_val = scores.get(i);
					min_pos = i;
				}
			}
			
			final int tries = 100;
			int best_idx, best_val = 0;
			for (int i = 0; i < tries; i++) {
				int idx = r.nextInt(slideshow.size());
				slideshow.get(idx);
			}
			// TODO
		}
	}
	
	
	
	public static void writeSolution(List<Photo> slideshow) throws FileNotFoundException {
		Date date = new Date();
		char filec = (char) ('A' + FILE);
		String filename = "/hashcode/out_" +  date.getHours() + "_" + date.getMinutes() + "_" + filec + ".txt";
		File file = new File(filename);
		PrintWriter pw = new PrintWriter(file);
		pw.write(Integer.toString(slideshow.size()) + "\n");
		for(Photo p : slideshow) {
			pw.write(p.printIndex() + "\n");
		}
		pw.close();
	}
	
	public static List<Photo> mergeVerticalsInteresting (List<Photo> photos) {
		System.out.println("mergin verticals interesting");
		List<Photo> result = new ArrayList<>();
		
		for(Photo p : photos) {
			if(p.horizontal) {
				result.add(p);
			}
		}
		
		List<Photo> vertical = new ArrayList<>();
		for(Photo p : photos) {
			if(!p.horizontal) {
				vertical.add(p);
			}
		}
		
		randomizedGreedy(vertical);
		
		for(int i = 1; i < vertical.size(); i+=2) {
			result.add(new TwoPhotos(vertical.get(i-1), vertical.get(i)));
		}
		
		return result;
	}
	
	public static List<Photo> getVertical(List<Photo> photos) {
		List<Photo> vertical = new ArrayList<>();
		for(Photo p : photos) {
			if(!p.horizontal) {
				vertical.add(p);
			}
		}
		return vertical;
	}
	
	public static List<Photo> getHorizontal(List<Photo> photos) {
		List<Photo> vertical = new ArrayList<>();
		for(Photo p : photos) {
			if(p.horizontal) {
				vertical.add(p);
			}
		}
		return vertical;
	}
	
	public static List<Photo> mergeVerticals (List<Photo> photos) {
		System.out.println("mergin verticals");
		List<Photo> result = new ArrayList<>();
		
		for(Photo p : photos) {
			if(p.horizontal) {
				result.add(p);
			}
		}
		
		List<Photo> vertical = new ArrayList<>();
		for(Photo p : photos) {
			if(!p.horizontal) {
				vertical.add(p);
			}
		}
		
		for(int i = 1; i < vertical.size(); i+=2) {
			result.add(new TwoPhotos(vertical.get(i-1), vertical.get(i)));
		}
		
		return result;
	}
	
	public static List<Photo> mergeVerticalsFrontBack (List<Photo> photos) {
		System.out.println("mergin verticals");
		List<Photo> result = new ArrayList<>();
		
		for(Photo p : photos) {
			if(p.horizontal) {
				result.add(p);
			}
		}
		
		List<Photo> vertical = new ArrayList<>();
		for(Photo p : photos) {
			if(!p.horizontal) {
				vertical.add(p);
			}
		}
		
		if(vertical.size() % 2 == 1) {
			vertical.remove(vertical.size()-1);
		}
		
		for(int i = 1; i < vertical.size()/2; i++) {
			result.add(new TwoPhotos(vertical.get(i), 
					vertical.get(vertical.size()-1-i)));
		}
		
		return result;
	}
	
	public static List<Photo> mergeVerticalsRandom (List<Photo> photos) {
		System.out.println("mergin verticals");
		List<Photo> result = new ArrayList<>();
		
		for(Photo p : photos) {
			if(p.horizontal) {
				result.add(p);
			}
		}
		
		List<Photo> vertical = new ArrayList<>();
		for(Photo p : photos) {
			if(!p.horizontal) {
				vertical.add(p);
			}
		}
		
		Collections.shuffle(vertical);
		
		for(int i = 1; i < vertical.size(); i+=2) {
			result.add(new TwoPhotos(vertical.get(i-1), vertical.get(i)));
		}
		
		return result;
	}
	
	
	
	static void solveWithScores() throws Exception {
		List<Photo> p = readFile(files[FILE]);
		
		List<Photo> horizontal = getHorizontal(p);
		horizontal = randomizedGreedyScores(horizontal, p);
		
		List<Photo> vertical = getVertical(p);
		
		vertical = randomizedGreedyScores(vertical, p);
		// vertical.remove(vertical.size()-1);
		
		if (FILE != 1) {
			vertical = mergeVerticalsFrontBack(vertical);
		}
		
		List<Photo> sol = new ArrayList<>();
		sol.addAll(vertical);
		sol.addAll(horizontal);
		

		writeSolution(sol);
	}
	
	static void solveOnlyHorizontalScores() throws Exception {
		List<Photo> p = readFile(files[FILE]);
		
		List<Photo> horizontal = getHorizontal(p);
		horizontal = randomizedGreedyScores(horizontal, p);
		
		List<Photo> vertical = getVertical(p);
		vertical = mergeVerticalsRandom(vertical);
		randomizedGreedy(vertical);
		
		List<Photo> sol = new ArrayList<>();
		sol.addAll(vertical);
		sol.addAll(horizontal);
		
		writeSolution(sol);
	}
	
	static void solveNoScores() throws Exception {
		List<Photo> p = readFile(files[FILE]);
		
		if (FILE != 1)
			p = mergeVerticalsRandom(p);
		
		randomizedGreedy(p);
		
		writeSolution(p);
	}
	
	static int FILE = 3;	
	
	public static void main(String[] args) throws Exception  {
		solveOnlyHorizontalScores();
		
		
	}
}
