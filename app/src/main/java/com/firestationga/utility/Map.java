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

	/**
	 * Method untuk menbuat objek Map jika diberikan array karakter 2D sebagai
	 * tempat penyimpanan peta sesungguhnya.
	 * Dibuat oleh: Natasya Keisya Hartono
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 *
	 * @param map peta sesungguhnya
	 */
	public Map(char[][] map) {
		this.map = map;
		this.rows = map.length;
		this.cols = map[0].length;
		this.cells = rows * cols;

		// Peroleh koordinat setiap rumah dan sel kosong.
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

		// Hitung distance matrix peta
		this.distanceMatrix = DistanceMatrix.computeDistanceMatrix(map, houseCoords, emptyCoords);
	}

	/**
	 * Method untuk memperoleh peta sebenarnya
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return peta sebenarnya dalam representasi array karakter 2D
	 */
	public char[][] getMap() {
		return map;
	}

	/**
	 * Method untuk memperoleh distance matrix
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return distance matrix peta
	 */
	public int[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	/**
	 * Method untuk memperoleh jumlah baris
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return jumlah baris
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Method untuk memperoleh jumlah kolom
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return jumlah kolom
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Method untuk memperoleh jumlah sel
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return jumlah sel
	 */
	public int getNCells() {
		return cells;
	}

	/**
	 * Method untuk memperoleh daftar sel kosong
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return daftar sel kosong
	 */
	public List<Integer> getEmptyCells() {
		return emptyCells;
	}

	/**
	 * Method untuk memperoleh jumlah sel kosong
	 * Ditulis oleh: Kumara Ivan Anuraga
	 * 
	 * @return jumlah sel kosong
	 */
	public int getNEmptyCells() {
		return emptyCoords.size();
	}

	/**
	 * Method untuk memperoleh karakter pada koordinat tertentu
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param row baris
	 * @param col kolom
	 * @return karakter pada koordinat tersebut
	 */
	public char getCellAt(int row, int col) {
		return map[row][col];
	}

	/**
	 * Method untuk memperoleh baris pada untuk sebuah index sel
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param cellIndex sel index
	 * @return baris untuk index sel tersebut
	 */
	public int getRowOf(int cellIndex) {
		return cellIndex / rows;
	}

	/**
	 * Method untuk memperoleh kolom pada untuk sebuah index sel
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param cellIndex sel index
	 * @return kolom untuk index sel tersebut
	 */
	public int getColOf(int cellIndex) {
		return cellIndex % rows;
	}

	/**
	 * Method untuk memperoleh karakter pada index kolom tertentu
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param cellIndex sel index
	 * @return karakter pada koordinat tersebut
	 */
	public char getCellAt(int cellIndex) {
		return getCellAt(getRowOf(cellIndex), getColOf(cellIndex));
	}

	/**
	 * Method untuk mengubah sel menjadi sebuah titik
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param cellIndex sel index
	 * @return representasi titik index sel tersebut
	 */
	public Point cellToPoint(int cellIndex) {
		return new Point(getRowOf(cellIndex), getColOf(cellIndex));
	}
}
