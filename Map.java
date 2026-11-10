public class Map {
    public int baris;
    public int kolom;
    public char[][] isiMap;
    public int fStation;
	public int[] pohon;
	public int[] rumah;

	public Map (int baris, int kolom, int fStation, char[][] isiMap) {
        this.baris = baris;
        this.kolom = kolom;
        this.fStation = fStation; 
        this.isiMap = isiMap;
	}

	public char get(int index) {
		int baris = index / isiMap.length;
		int kolom = index % isiMap[0].length;

		return isiMap[baris][kolom];
	}
}
