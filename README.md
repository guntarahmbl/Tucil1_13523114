#  IQ Puzzler Pro Solver
# Tugas Kecil 1 Strategi Algoritma IF2211 Algoritma Brute Force
## **Program akan berjalan maksimal menggunakan GUI. Jika menjalankan melalui CLI, pastikan file input bernama input.txt**
## Deskripsi Program
Program ini merupakan solver dari permainan IQ Puzzler Pro yang menuntut pemain untuk menyusun blok-blok atau piece ke dalam suatu bord hingga suatu bord terisi penuh.
Blok-blok dapat diatur sedemikian rupa (diputar, dicerminkan, diganti posisinya) sehingga memenuhi kriteria end game. Pada program ini, digunakan algoritma brute force
untuk mencari kemungkinan solusinya.

##  Struktur Program
Struktur program adalah sebagai berikut:
```sh
/Tucil1_13523114
├── /bin                # Executable file
│   ├── GUI_Tucil1_13523114.jar    
│   ├── CLI_Tucil1_13523114.jar    
├── /doc                # Laporan
├── /output   
│   ├── Block.class   
│   ├── IO.class   
│   ├── GUI.class       
│   ├── Main.class     
│   └── Solver.class         
├── /src                # Source code program
│   ├── Block.java     
│   ├── IO.java   
│   ├── GUI.java     
│   ├── Main.java      
│   └── Solver.java
├── /test               # Kasus uji
└── README.md           # Deskripsi Program
```
![GUI](https://github.com/user-attachments/assets/291cab18-599a-4734-a492-f22657b237c9)
### Requirement
Hal yang harus dipasang terlebih dahulu adalah:
- **Java 9 atau di atasnya**
- **Modul JavaFX yang diunduh dari gluon dan dipasang**

### Cara instalasi (CLI)
1. **Clone repository**

```bash
  git clone https://github.com/guntarahmbl/Tucil1_13523114.git
```

2. **Pergi ke directory /Tucil1_13523114**

```bash
  cd Tucil1_13523114/bin
```

3. **Jalankan program melalui CLI atau GUI**

```bash
  java -jar CLI_Tucil1_13523114.jar 
  java --module-path "C:\javafx\lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -jar GUI_Tucil1_13523114.jar

```

## **Cara Menggunakan**

1. **Jalankan program melalui GUI** .
2. Masukkan file input berupa file txt yang berisi problem yang ingin diselesaikan. Pilih darimana saja.
3. Klik tombol solve untuk mendapatkan solusi pada GUI
4. Anda dapat menyimpan solusi yang diberikan baik dalam bentuk gambar (.png) atau text document (.txt) dengan cara klik tombol simpan di bawah solusi

## **Author**
**Guntara Hambali**

**NIM 13523114**

**Kelas K2**