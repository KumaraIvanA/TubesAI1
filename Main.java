
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int npm_ketua = 24045;
        Random rand = new Random(npm_ketua);
        int[] seed = new int[30];
        int batas = 1 << 16; // 2^16 = 65536
        for (int i = 0; i < seed.length; i++) {
            seed[i] = rand.nextInt(batas + 1); //simpan agar bisa diulang
        }
        double total = 0;
        
        int chromosomeType = Individual.CHROMOSOME_BITSTRING; //nilai default
        int crossoverType = Individual.CROSSOVER_ONE_POINT; // nilai default
        int mutationType = Individual.MUTATION_LOCAL; // nilai default

        try {
            if (args.length > 0) crossoverType  = Integer.parseInt(args[0]);
            if (args.length > 1) mutationType   = Integer.parseInt(args[1]);
            if (args.length > 2) chromosomeType = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Argumen harus berupa angka. Contoh: java Main 1 0 0");
            return;
        }

        if (chromosomeType < 0 || chromosomeType > 1) {
            System.out.println("Kromosom tidak valid (0 = tipe A, 1 = tipe B)");
            return;
        }
        if (crossoverType < 0 || crossoverType > 1) {
            System.out.println("Crossover tidak valid (0 = one point, 1 = uniform)");
            return;
        }
        if (mutationType < 0 || mutationType > 1) {
            System.out.println("Mutation tidak valid (0 = random, 1 = local)");
            return;
        }

        //READ INPUT
        String path = "Test Cases/n20_acak_01.txt"; //path untuk test case input
        int baris = 0, kolom = 0, fStation = 0; //inisialisasi variabel baris,kolom,dan fStation 
        char[][] peta = new char[baris][kolom]; //inisialisasi array peta
        char character;
        String barisKarakter;

        try (Scanner sc = new Scanner(new File(path))) { //membaca file
            baris = sc.nextInt(); //membaca banyaknya baris
            kolom = sc.nextInt(); //membaca banyaknya kolom
            fStation = sc.nextInt(); //membaca banyaknya fStation yang dibutuhkan

            peta = new char[baris][kolom]; //peta sebesar baris x kolom

            for (int i = 0; i < peta[0].length; i++) {
                barisKarakter = sc.next(); //membaca karakter tiap baris
                for (int j = 0; j < peta.length; j++) {
                    character = barisKarakter.charAt(j); //membaca satu per satu karakter dalam baris dapat berupa . , H , T
                    peta[i][j] = character; //masukkan karakter . , H , T ke dalam array
                }
            }
        } catch (FileNotFoundException e) {
            //exception saat file tidak ditemukan
            System.out.println("File tidak ditemukan");
            e.printStackTrace();
        }

        //Panggil Map untuk mendapatkan isi map 
        Map map = new Map(baris, kolom, fStation, peta);

        //Ini untuk loop 30x, untuk setiap model/GA dgn tes kasus, genetik operator, dan hyper-parameter tertentu harus dilakukan percobaan sebanyak 30x
        for (int j = 0; j < 30; j++) { //banyaknya percobaan
            Random rnd = new Random(seed[j]);
            //jalankan algoritma dengan rnd sbg Random Number Generator
            //gen (random generator) dikirim ke algogen, jadi hanya menggunakan satu generator untuk keseluruhan algo
            int elitism = 0, maxPopulation = 0, crossoverRate = 0, mutationRate = 0, totalGen = 0;

            GenerativeAlgorithm ga = new GenerativeAlgorithm(rnd, map, elitism, maxPopulation, crossoverRate, mutationRate, totalGen);
            Individual res = ga.run();	//ambil yg terbaik
            double fit = res.getFitness(); //fungsi fitness untuk cari tahu seberapa bagus fitnesnya
            total = total + fit; //hitung total fitness seluruhnya 
        }
    }
}
