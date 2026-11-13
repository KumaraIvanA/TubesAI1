package com.firestationga.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Kelas yang merepresentasikan lingkungan masalah firestation
 * 
 * @author Natasya Keisya Hartono
 */
public class Map {
	private char[][] map;
	private int[][] distanceMatrix;
	private List<Point> houseCoords = new ArrayList<Point>();
	private List<Point> emptyCoords = new ArrayList<Point>();
	private List<Integer> emptyCells = new ArrayList<Integer>();
	private int rows;
	private int cols;
	private int cells;

	public Map(char[][] map) {
		this.map = map;
		this.rows = map.length;
		this.cols = map[0].length;
		this.cells = rows * cols;

		int cellCounter = 0;
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				if (map[r][c] == 'H') {
					houseCoords.add(new Point(r, c));
				} else if (map[r][c] == '.') {
					emptyCoords.add(new Point(r, c));
					emptyCells.add(cellCounter);
				}
				cellCounter++;
			}
		}

		this.distanceMatrix = DistanceMatrix.computeDistanceMatrix(map, houseCoords, emptyCoords);
	}

	public char[][] getMap() {
		return map;
	}

	public int[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public int getNCells() {
		return cells;
	}

	public List<Integer> getEmptyCells() {
		return emptyCells;
	}

	public int getNEmptyCells() {
		return emptyCoords.size();
	}

	public char getCellAt(int row, int col) {
		return map[row][col];
	}

	public int getRowOf(int cellIndex) {
		return cellIndex / rows;
	}

	public int getColOf(int cellIndex) {
		return cellIndex % rows;
	}

	public char getCellAt(int cellIndex) {
		return getCellAt(getRowOf(cellIndex), getColOf(cellIndex));
	}

	public Point cellToPoint(int cellIndex) {
		return new Point(getRowOf(cellIndex), getColOf(cellIndex));
	}
}
