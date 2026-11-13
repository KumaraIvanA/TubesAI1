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
