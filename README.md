# TubesAI1
---
## Compile JAR
```bash
./gradlew jar
```
---
## Menjalankan JAR
```bash
java -jar fsga.jar [options...] <file>
```
---
## Penggunaan CLI
Untuk melihat semua argumen CLI dan nilai parameter default, jalankan:
```bash
java -jar fsga.jar -h
```
atau
```bash
java -jar fsga.jar --help
```
\
\
Contoh penggunaan CLI:
```bash
java -jar fsga.jar --chromosome bitstring --selection roulette --population 150 input_data.txt
```
---
## Melakukan eksperimen
Contoh penggunaan CLI untuk melakukan eksperimen:
```bash
java -jar fsga.jar --chromosome integer --selection roulette --crossover one_point --mutation local --population 500 --generation 20 --pcrossover 0.25 --pmutation 0.01 --pelitism 0.90 --runs 30 ".\Test Cases\n20_acak_03.txt"    
```
Jika parameter tidak dituliskan maka akan digunakan nilai parameter default 
Contoh penggunaan CLI untuk melakukan eksperimen dengan menggunakan nilai default (pcrossover,pmutation,pelitism menggunakan nilai default) :
```bash
java -jar fsga.jar --chromosome integer --selection roulette --crossover one_point --mutation local --population 500 --generation 20 --runs 30 ".\Test Cases\n20_acak_03.txt"    
```

