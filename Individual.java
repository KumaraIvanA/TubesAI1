import java.util.Arrays;
import java.util.Random;

public class Individual implements Comparable<Individual> {
    public static final int CROSSOVER_ONE_POINT = 0;
    public static final int CROSSOVER_UNIFORM = 1;
    public static final int MUTATION_RANDOM = 0;
    public static final int MUTATION_LOCAL = 1; // geser satu FS ke sel kosong yang bertetangga

    public BitStringChromosome chromosome; // kromosom: objek BitStringChromosome (true = sel kosong nomor k berisi FS)
    public double cost; // rata-rata jarak rumah ke FS terdekat (makin kecil makin baik)
    public double fitness; // nilai fitness
    public Random MyRand;
    public double parentProbability;// probabilitas terpilih sbg parent
    public int p; // jumlah FS yang harus ada

    // membuat individu acak: tepat p bit bernilai true
    public Individual(Random MyRand, int numCells, int p) {
        this.MyRand = MyRand;
        this.p = p;
        boolean[] bits = new boolean[numCells]; // semua false
        int placed = 0; // jumlah FS yang sudah ditaruh
        while (placed < p) { // ulangi sampai p FS
            int k = MyRand.nextInt(numCells); // sel kosong acak
            if (!bits[k]) { // hanya jika sel itu belum berisi FS
                bits[k] = true; // taruh FS
                placed++; // hitung
            }
        }
        this.chromosome = new BitStringChromosome(bits); // bungkus bit menjadi kromosom
    }

    // membuat individu baru berdasarkan kromosom dari luar
    public Individual(Random MyRand, BitStringChromosome chromosome, int p) {
        this.MyRand = MyRand;
        this.chromosome = chromosome;
        this.p = p;
    }

    // menghitung cost dan fitness
    public double setFitness(int[][] distMatrix, int numHouses) {
        boolean[] bits = chromosome.getBitString(); // ambil bit dari kromosom
        int[] cells = new int[p]; // daftar sel yang berisi FS
        int k = 0; // posisi ngisi
        for (int i = 0; i < bits.length; i++) { // baca setiap bit
            if (bits[i]) { // bit true berarti ada FS
                cells[k] = i; // catat nomor selnya
                k++; // geser posisi
            }
        }
        this.cost = DistanceMatrix.calculateCost(cells, distMatrix, numHouses);
        this.fitness = 1.0 / (1.0 + this.cost); // cost kecil -> fitness besar
        return this.fitness;
    }

    public double getFitness() {
        return this.fitness;
    }
    
    // Tepat satu FS berpindah, bit di kromosom ini diubah langsung
    public void doMutation(int type, int[][] neighbors) {
        if (type == MUTATION_RANDOM) {
            mutateRandom(); // pindah ke sel kosong mana saja
        } else if (type == MUTATION_LOCAL) {
            mutateLocal(neighbors); // geser ke sel tetangga
        } else {
            throw new IllegalArgumentException("jenis mutasi tidak dikenal");
        }
    }

    // mutasi 1: pindahkan satu FS ke sel kosong mana saja yang belum berisi FS
    private void mutateRandom() {
        boolean[] bits = chromosome.getBitString(); // array yang sama dengan milik kromosom (bukan salinan)
        if (bits.length == p) {
            return; // semua sel sudah berisi FS: tidak ada tempat pindah
        }
        int from; // sel asal
        do {
            from = MyRand.nextInt(bits.length); // coba sel acak
        } while (!bits[from]); // ulangi sampai ketemu sel yang berisi FS
        int to; // sel tujuan
        do {
            to = MyRand.nextInt(bits.length); // coba sel acak
        } while (bits[to]); // ulangi sampai ketemu sel yang masih kosong
        bits[from] = false; // lepas FS dari sel asal
        bits[to] = true; // pasang FS di sel tujuan
    }

    // mutasi 2: geser satu FS ke sel kosong yang bersebelahan
    // (atas/bawah/kiri/kanan)
    private void mutateLocal(int[][] neighbors) {
        if (neighbors == null) {
            throw new IllegalArgumentException("mutasi lokal butuh tabel tetangga (buildNeighbors)");
        }
        boolean[] bits = chromosome.getBitString(); // bit kromosom
        if (bits.length == p) {
            return; // tidak ada tempat pindah
        }
        int[] cells = new int[p]; // daftar sel yang sekarang berisi FS
        int k = 0;
        for (int i = 0; i < bits.length; i++) {
            if (bits[i]) {
                cells[k] = i;
                k++;
            }
        }
        int start = MyRand.nextInt(p); // mulai dari FS acak
        for (int t = 0; t < p; t++) { // coba tiap FS sampai ada yang punya tetangga bebas
            int from = cells[(start + t) % p]; // FS yang dicoba
            int[] free = new int[neighbors[from].length]; // tetangga yang belum berisi FS
            int nFree = 0; // jumlahnya
            for (int nb : neighbors[from]) { // periksa tiap tetangga
                if (!bits[nb]) { // belum berisi FS
                    free[nFree] = nb;
                    nFree++;
                }
            }
            if (nFree > 0) { // ada tempat
                int to = free[MyRand.nextInt(nFree)]; // pilih satu tetangga acak
                bits[from] = false; // lepas FS dari sel lama
                bits[to] = true; // pasang di sel tetangga
                return; // selesai
            }
        }
        mutateRandom(); // tidak ada FS yang bisa digeser: pindah acak saja
    }

    public Individual doCrossover(Individual other, int type) {
        boolean[] a = this.chromosome.getBitString(); // bit induk ini
        boolean[] b = other.chromosome.getBitString(); // bit induk lain
        boolean[] childBits = new boolean[a.length]; // bit anak
        if (type == CROSSOVER_ONE_POINT) {
            int pos = MyRand.nextInt(a.length - 1) + 1; // titik potong 1..panjang-1
            for (int i = 0; i < pos; i++) { // bagian depan dari induk ini
                childBits[i] = a[i];
            }
            for (int i = pos; i < a.length; i++) { // bagian belakang dari induk lain
                childBits[i] = b[i];
            }
        } else if (type == CROSSOVER_UNIFORM) {
            for (int i = 0; i < a.length; i++) { // putuskan tiap bit
                if (a[i] == b[i]) {
                    childBits[i] = a[i]; // kedua induk sepakat: diwariskan
                } else {
                    childBits[i] = MyRand.nextBoolean(); // induk berbeda: pilih salah satu secara acak
                }
            }
        } else {
            throw new IllegalArgumentException("jenis crossover tidak dikenal");
        }
        repair(childBits); // jumlah FS bisa tidak sama dengan p
        return new Individual(this.MyRand, new BitStringChromosome(childBits), p);
    }

    // samakan jumlah FS dengan p (buang yang kelebihan / tambah yang kurang secara
    // acak)
    private void repair(boolean[] bits) {
        int ones = 0; // jumlah FS saat ini
        for (boolean bit : bits) { // hitung bit true
            if (bit) {
                ones++;
            }
        }
        while (ones > p) { // kelebihan FS
            int k = MyRand.nextInt(bits.length); // posisi acak
            if (bits[k]) { // hanya jika memang berisi FS
                bits[k] = false; // buang
                ones--;
            }
        }
        while (ones < p) { // kekurangan FS
            int k = MyRand.nextInt(bits.length); // posisi acak
            if (!bits[k]) { // hanya jika masih kosong
                bits[k] = true; // tambah
                ones++;
            }
        }
    }

    // tabel tetangga sel kosong: neighbors[k] = nomor sel kosong yang bersebelahan
    // (4 arah) dengan sel nomor k
    public static int[][] buildNeighbors(char[][] grid, int m, int n) {
        int[][] id = new int[m][n]; // nomor sel kosong tiap petak, -1 jika bukan sel kosong
        int count = 0; // jumlah sel kosong
        for (int i = 0; i < m; i++) { // telusuri baris demi baris
            for (int j = 0; j < n; j++) { // lalu kolom
                if (grid[i][j] == '.') {
                    id[i][j] = count; // beri nomor urut pada sel kosong
                    count++;
                } else {
                    id[i][j] = -1; // rumah atau pohon
                }
            }
        }
        int[] dr = { -1, 1, 0, 0 }; // 4 arah: atas, bawah, kiri, kanan
        int[] dc = { 0, 0, -1, 1 };
        int[][] result = new int[count][]; // hasil: tetangga per sel kosong
        for (int i = 0; i < m; i++) { // telusuri lagi
            for (int j = 0; j < n; j++) {
                if (id[i][j] < 0) {
                    continue; // lewati rumah dan pohon
                }
                int[] tmp = new int[4]; // maksimal 4 tetangga
                int k = 0; // jumlah ditemukan
                for (int d = 0; d < 4; d++) { // cek 4 arah
                    int r = i + dr[d]; // koordinat tetangga
                    int c = j + dc[d];
                    if (r >= 0 && r < m && c >= 0 && c < n && id[r][c] >= 0) { // di dalam peta dan sel kosong
                        tmp[k] = id[r][c];
                        k++;
                    }
                }
                result[id[i][j]] = Arrays.copyOf(tmp, k); // simpan sebesar jumlah ditemukan
            }
        }
        return result; // tabel tetangga
    }

    @Override
    public int compareTo(Individual other) { // fitness terbesar di depan
        if (this.fitness > other.fitness) {
            return -1;
        } else if (this.fitness < other.fitness) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        boolean[] bits = chromosome.getBitString(); // bit kromosom
        StringBuilder sb = new StringBuilder("cost=" + cost + " cells=");
        for (int i = 0; i < bits.length; i++) { // tulis nomor sel yang berisi FS
            if (bits[i]) {
                sb.append(i).append(' ');
            }
        }
        return sb.toString();
    }
}