Dokumentasi & Panduan Lengkap Sistem POS Chatting Cafe & Dimsum 5.0

Sistem Point of Sales (POS) berbasis **Java Console App** ini dirancang khusus untuk mengautomatisasi operasional kasir secara *real-time*, manajemen menu produk oleh admin secara permanen, hingga perekaman laporan keuangan harian. Proyek ini dibangun dengan menerapkan seluruh pilar utama **Pemrograman Berorientasi Objek (OOP)** secara mendalam.

---

## Anggota Kelompok
1. **Tiara Chontesa** — NIM: `D1041231070` 
2. **Tania Yuniar** — NIM: `D1041231010` 
3. **Freya Martines** — NIM: `D1041231078`
4. **Nabila Rahma Aulia** — NIM: `D1041231030` 

---

## Review Singkat Proyek & Pemetaan Materi OOP

Aplikasi ini memodernisasi sistem pencatatan kafe lewat struktur data terintegrasi. Berdasarkan kode sumber `MainPOS.java`, berikut adalah bukti implementasi materi kuliah yang tertanam di dalam sistem:

1. **Class, Object, & Static**: Diimplementasikan pada kelas `KonfigurasiKafe`. Atribut global seperti `NAMA_KAFE`, `ALAMAT`, `WIFI_PASS`, dan `PAJAK` menggunakan modifier `public static final` agar dapat diakses langsung oleh kelas lain tanpa instansiasi objek. Kredensial masuk (`SANDI_KASIR`, `SANDI_ADMIN`) diproteksi di tingkat kelas demi keamanan operasional.
2. **Inner Class**: Kelas `Info` dideklarasikan di dalam kelas `KonfigurasiKafe` (*Nested/Inner Class*) untuk mengelompokkan metadata fisik kedai (alamat).
3. **Abstraksi (Interface)**: Menggunakan `interface Tampilan` dengan cetak biru method `void tampilkanMenu()`. Ini memaksakan standarisasi visual bagi seluruh kelas produk yang diturunkan.
4. **Enkapsulasi**: Atribut pada kelas `Menu` (`kode`, `nama`, `harga`, `diskon`) dilindungi dengan akses modifier `private`. Data tersebut hanya dapat diakses secara aman melalui metode *Getter* dan *Setter*. Logika proteksi disematkan di dalam setter agar harga atau diskon yang diinput bernilai minus otomatis di-reset ke angka `0`.
5. **Inheritance (Pewarisan)**: Kelas `Menu` bertindak sebagai *Superclass* (Parent) yang mewariskan semua karakteristik dasarnya kepada *Subclass* (Child) yaitu `MinumanPanas` dan `MinumanDingin` melalui kata kunci `extends`.
6. **Polymorphism**: 
   * *Method Overriding*: Metode `tampilkanMenu()` di-override pada kelas `MinumanPanas` (untuk mencetak spesifikasi suhu derajat Celcius) dan `MinumanDingin` (untuk mencetak level es).
   * *Method Overloading*: Kelas `Pembayaran` mengimplementasikan overloading pada metode `hitungTotal()`. Versi pertama mengalkulasi total murni, sedangkan versi kedua menerima parameter tambahan untuk memotong diskon member khusus.
7. **Struktur Data Array Kompleks**:
   * *Array 1D*: Menampung data antrean item keranjang belanja aktif (`namaItem`, `hargaItem`, `qtyItem`).
   * *Array 2D*: Variabel `mejaTabel` bertipe array $5 \times 3$ digunakan untuk memetakan status keterisian menu pesanan di 5 meja kafe secara langsung.
   * *Jagged Array*: Matriks `histori` menampung catatan transaksi harian secara dinamis. Panjang baris tiap transaksi bersifat tidak beraturan (*jagged*) karena disesuaikan secara dinamis dengan jumlah variasi item produk yang dibeli pelanggan.
8. **File I/O (Sinkronisasi Permanen)**: Menggunakan objek `BufferedReader` dan `BufferedWriter` yang terikat dengan berkas fisik `menu.txt`. Perubahan data menu oleh Admin langsung ditulis kembali ke dalam file teks sehingga data tidak hilang saat aplikasi dimatikan.
9. **Exception Handling**: Sistem dibentengi dari crash lewat mekanisme `try-catch` terpusat untuk menangkap kesalahan tipe input data (`InputMismatchException`), kesalahan autentikasi (`IllegalAccessException`), kegagalan kalkulasi uang tunai (`ArithmeticException`), serta pengaman transaksi kasir ketika sebuah kode menu mendadak dihapus oleh Admin (`NoSuchElementException`).

---

##  Instruksi Kompilasi & Penggunaan

### 1. Prasyarat Sistem
* Pastikan komputer Anda telah terinstal **Java Development Kit (JDK)** versi 11 atau yang lebih baru.
* Pastikan file `MainPOS.java` dan file database lokal `menu.txt` berada di dalam satu direktori/folder kerja yang sama.

### 2. Cara Menjalankan Aplikasi
Buka terminal (Command Prompt/PowerShell/Terminal Linux) tepat pada folder lokasi file tersebut, kemudian jalankan perintah berikut secara berurutan:

```bash
# Kompilasi berkas Java menjadi Bytecode
javac MainPOS.java

# Jalankan aplikasi utama
java MainPOS
