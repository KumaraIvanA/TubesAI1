import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


public class Main {
    public static void main(String[] args) {
        
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

        // Map map = new Map(baris,kolom,fStation,peta); //contoh panggil map
    }
}