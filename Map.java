public class Map {
    public int baris;
    public int kolom;
    public char[][] isiMap;
    public int fStation;

    public Map (int baris, int kolom,int fStation ,char[][] isiMap) {
        this.baris = baris;
        this.kolom = kolom;
        this.fStation = fStation; 
        this.isiMap = isiMap;
    }
}
