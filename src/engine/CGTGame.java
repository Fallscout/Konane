package engine;

import java.util.ArrayList;
import java.util.List;

public class CGTGame {
	private List<CGTGame> leftOptions;
	private List<CGTGame> rightOptions;

	public CGTGame() {
		this.leftOptions = new ArrayList<CGTGame>();
		this.rightOptions = new ArrayList<CGTGame>();
	}
	
	public CGTGame(List<CGTGame> leftOptions, List<CGTGame> rightOptions) {
		this.leftOptions = leftOptions;
		this.rightOptions = rightOptions;
	}
	
	public List<CGTGame> getLeftOptions() {
		return this.leftOptions;
	}
	
	public List<CGTGame> getRightOptions() {
		return this.rightOptions;
	}
}
