package sushigame.view;

import java.util.Comparator;

import sushigame.model.Chef;

public class LowToHighSpoiledComparator implements Comparator<Chef> {

	@Override
	public int compare(Chef a, Chef b) {
		// We do b - a because we want largest to smallest
		return (int) (Math.round(a.getSpoiled()) - Math.round(b.getSpoiled()));
	}			
}
