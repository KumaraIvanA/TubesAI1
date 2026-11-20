package com.firestationga.utility;

import java.util.*;

/**
 * Kelas yang menyediakan metode untuk menghitung distance matrix dan menghitung
 * jarak setiap rumah ke <i>fire station</i> terdekat.
 *
 * @author Clara Joycelyne Siauttara
 */
public class DistanceMatrix {

	// Direction arrays untuk bergerak 4 arah: Atas, Bawah, Kiri, Kanan
	private static final int[] dR = { -1, 1, 0, 0 };
	private static final int[] dC = { 0, 0, -1, 1 };

	/**
	 * Menghitung matriks jarak dari setiap rumah ke semua sel kosong.
	 * Ditulis oleh: Clara Joycelyne Siauttara
	 * 
	 * @param grid       Matriks char ukuran m x n (berisi '.', 'H', 'T')
	 * @param houses     List koordinat rumah
	 * @param emptyCells List koordinat sel kosong
	 * @return Matriks 2D int: distMatrix[indexRumah][indexSelKosong]
	 */
	public static int[][] computeDistanceMatrix(char[][] grid, List<Point> houses, List<Point> emptyCells) {
		int m = grid.length;
		int n = grid[0].length;
		int numHouses = houses.size();
		int numEmpty = emptyCells.size();
		int penalty = m * n; // Nilai penalti jika rumah tidak terjangkau (P = m x n)

		// Matriks hasil: baris = indeks rumah, kolom = indeks sel kosong
		int[][] distMatrix = new int[numHouses][numEmpty];

		// Mapping cepat dari (r, c) sel kosong ke indeksnya di emptyCells (0 sampai
		// numEmpty - 1)
		int[][] emptyCellIndexMap = new int[m][n];
		for (int i = 0; i < m; i++) {
			Arrays.fill(emptyCellIndexMap[i], -1);
		}
		for (int idx = 0; idx < numEmpty; idx++) {
			Point p = emptyCells.get(idx);
			emptyCellIndexMap[p.r][p.c] = idx;
		}

		// Lakukan BFS dari SETIAP RUMAH
		for (int hIdx = 0; hIdx < numHouses; hIdx++) {
			Point startHouse = houses.get(hIdx);

			// Inisialisasi matriks jarak dari rumah ini ke seluruh sel di grid
			int[][] distGrid = new int[m][n];
			for (int[] row : distGrid) {
				Arrays.fill(row, -1);
			}

			Queue<Point> queue = new LinkedList<>();

			// Cek 4 tetangga awal dari rumah
			// (Jarak dari rumah ke sel kosong yang persis bersebelahan adalah 1)
			for (int i = 0; i < 4; i++) {
				int nR = startHouse.r + dR[i];
				int nC = startHouse.c + dC[i];

				// Hanya boleh melangkah ke sel yang berada dalam grid dan merupakan sel kosong
				// '.'
				if (nR >= 0 && nR < m && nC >= 0 && nC < n && grid[nR][nC] == '.') {
					distGrid[nR][nC] = 1;
					queue.add(new Point(nR, nC));
				}
			}

			// Jalankan BFS melintasi sel kosong
			while (!queue.isEmpty()) {
				Point curr = queue.poll();
				int currentDist = distGrid[curr.r][curr.c];

				for (int i = 0; i < 4; i++) {
					int nR = curr.r + dR[i];
					int nC = curr.c + dC[i];

					// Validasi batas grid, pastikan sel kosong ('.'), dan belum dikunjungi
					if (nR >= 0 && nR < m && nC >= 0 && nC < n) {
						if (grid[nR][nC] == '.' && distGrid[nR][nC] == -1) {
							distGrid[nR][nC] = currentDist + 1;
							queue.add(new Point(nR, nC));
						}
					}
				}
			}

			// Pindahkan hasil jarak dari distGrid ke distMatrix untuk rumah ke-hIdx
			for (int eIdx = 0; eIdx < numEmpty; eIdx++) {
				Point ep = emptyCells.get(eIdx);
				int d = distGrid[ep.r][ep.c];

				// Jika d == -1 (tidak tercapai), berikan penalti P = m * n
				distMatrix[hIdx][eIdx] = (d != -1) ? d : penalty;
			}
		}

		return distMatrix;
	}

	/**
	 * Method untuk menghitung rata-rata jarak setiap rumah ke
	 * <i>fire station</i> terdekat.
	 * Ditulis oleh: Clara Joycelyne Siauttara
	 *
	 * @param S array yang berisi lokasi <i>fire station</i>
	 * @param distMatrix array yang berupa <i>distance matrix</i> sebuah peta
	 * @return rata-rata jarak setiap rumah ke <i>fire station</i> terdekat
	 */
	public static double calculateCost(int[] S, int[][] distMatrix) {
		int totalDistance = 0;
		int numHouses = distMatrix.length;

		// Lakukan looping untuk setiap rumah
		for (int h = 0; h < numHouses; h++) {
			int minDist = Integer.MAX_VALUE;

			// Peroleh jarak rumah ke fire station terdekat
			for (int fsIdx : S) {
				int d = distMatrix[h][fsIdx];
				if (d < minDist) {
					minDist = d;
				}
			}

			totalDistance += minDist;
		}

		// Hitung cost rata-rata
		return (double) totalDistance / numHouses;
	}
}
