import java.io.File;
import java.util.Random;
import java.util.Scanner;
import java.io.FileNotFoundException;


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

        
        //READ INPUT
        String path = "Test Cases/n20_acak_01.txt"; //path untuk test case input
        int baris = 0,kolom = 0,fStation = 0; //inisialisasi variabel baris,kolom,dan fStation 
        char [][] peta = new char[baris][kolom]; //inisialisasi array peta
        char character; 
        String barisKarakter;

        try (Scanner sc = new Scanner(new File(path))) { //membaca file
            baris = sc.nextInt(); //membaca banyaknya baris
            kolom = sc.nextInt(); //membaca banyaknya kolom
            fStation = sc.nextInt(); //membaca banyaknya fStation yang dibutuhkan

            peta  = new char[baris][kolom]; //peta sebesar baris x kolom


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
            int elitism = 0,maxPopulation = 0,crossoverRate =0,mutationRate=0,totalGen=0;

            GenerativeAlgorithm ga = new GenerativeAlgorithm(rnd, map, elitism,maxPopulation, crossoverRate, mutationRate, totalGen); 
            Individual res = ga.run();	//ambil yg terbaik
            double fit = 1.0; //benerin ini fungisnya. fungsi fitness untuk cari tahu seberapa bagus fitnesnya
            total = total + fit; //hitung total fitness seluruhnya 
        }
    }    
}