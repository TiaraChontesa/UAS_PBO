import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// MATERI CLASS, OBJECT, & STATIC
class KonfigurasiKafe {
    public static final String NAMA_KAFE  = "Chatting Cafe & Dimsum 5.0";
    public static final String ALAMAT     = "Jl. Merdeka No. 12, Pontianak";
    public static final String WIFI_PASS  = "chattinglaris";
    public static final double PAJAK      = 0.10;
    
    // PERUBAHAN: Mengubah modifier dari public menjadi private
    private static final String SANDI_KASIR = "kasir123";
    private static final String SANDI_ADMIN = "admin456";

    // Method jembatan untuk mengecek sandi kasir
    public static boolean cekSandiKasir(String sandi) {
        return SANDI_KASIR.equals(sandi);
    }

    // Method jembatan untuk mengecek sandi admin
    public static boolean cekSandiAdmin(String sandi) {
        return SANDI_ADMIN.equals(sandi);
    }

    public static void garis(char c) {
        System.out.println(String.valueOf(c).repeat(50));
    }

    // MATERI INNER CLASS
    public static class Info {
        public String alamat = ALAMAT;
    }
}

// MATERI ABSTRAKSI
interface Tampilan {
    void tampilkanMenu();
}

// MATERI ENKAPSULASI
class Menu implements Tampilan {
    private String kode, nama;
    private double harga, diskon;

    public Menu() { this("M00", "Default", 0, 0); }

    public Menu(String k, String n, double h, double d) {
        kode = k; nama = n;
        setHarga(h); setDiskon(d);
    }

    public String getKode()   { return kode; }
    public String getNama()   { return nama; }
    public double getHarga()  { return harga; }
    public double getDiskon() { return diskon; }

    public void setHarga(double h)  { harga  = h < 0 ? 0 : h; }
    public void setDiskon(double d) { diskon = (d < 0 || d > 100) ? 0 : d; }

    public double getHargaSetelahDiskon() {
        return harga - (harga * diskon / 100);
    }

    public String getTipe()   { return "MENU"; }
    public String getEkstra() { return ""; }

    // MATERI POLYMORPHISM 
    @Override
    public void tampilkanMenu() {
        if (diskon > 0)
            System.out.printf("  [%-4s] %-22s Rp%,-9.0f (Diskon %.0f%%)\n", kode, nama, harga, diskon);
        else
            System.out.printf("  [%-4s] %-22s Rp%,.0f\n", kode, nama, harga);
    }
}

// MATERI INHERITANCE
class MinumanPanas extends Menu {
    private int suhu;

    public MinumanPanas(String k, String n, double h, double d, int s) {
        super(k, n, h, d);
        suhu = s;
    }

    @Override public String getTipe()   { return "PANAS"; }
    @Override public String getEkstra() { return String.valueOf(suhu); }

    @Override
    public void tampilkanMenu() {
        System.out.printf("  [%-4s] %-22s Rp%,-9.0f (Panas %d\u00b0C)\n",
            getKode(), getNama(), getHarga(), suhu);
    }
}

class MinumanDingin extends Menu {
    private String levelEs;

    public MinumanDingin(String k, String n, double h, double d, String e) {
        super(k, n, h, d);
        levelEs = e;
    }

    @Override public String getTipe()   { return "DINGIN"; }
    @Override public String getEkstra() { return levelEs; }

    @Override
    public void tampilkanMenu() {
        System.out.printf("  [%-4s] %-22s Rp%,-9.0f (Es: %s)\n",
            getKode(), getNama(), getHarga(), levelEs);
    }
}

// MATERI ARRAY 1D
class Transaksi {
    public String[] item = new String[20];
    public double[] hS   = new double[20];
    public double[] hD   = new double[20];
    public double[] pD   = new double[20];
    public int[]    qty  = new int[20];
    public int      jml  = 0;

    public void tambah(String nm, double hs, double hd, double d, int q) {
        if (jml < 20) {
            item[jml] = nm; hS[jml] = hs;
            hD[jml]   = hd; pD[jml] = d;
            qty[jml]  = q;  jml++;
        }
    }

    public boolean batalItem(int nomor) {
        int idx = nomor - 1;
        if (idx < 0 || idx >= jml) return false;
        for (int i = idx; i < jml - 1; i++) {
            item[i] = item[i+1]; hS[i] = hS[i+1];
            hD[i]   = hD[i+1];  pD[i] = pD[i+1];
            qty[i]  = qty[i+1];
        }
        jml--;
        return true;
    }

    public double getSub() {
        double s = 0;
        for (int i = 0; i < jml; i++) s += hD[i] * qty[i];
        return s;
    }

    public void reset() { jml = 0; }

    public void tampilPesanan() {
        if (jml == 0) { System.out.println("  (Belum ada pesanan)"); return; }
        KonfigurasiKafe.garis('-');
        System.out.println("  NO  NAMA MENU                QTY   SUBTOTAL");
        KonfigurasiKafe.garis('-');
        for (int i = 0; i < jml; i++) {
            System.out.printf("  %-3d %-24s x%-4d Rp%,.0f\n",
                (i+1), item[i], qty[i], hD[i] * qty[i]);
            if (pD[i] > 0)
                System.out.printf("      (Diskon %.0f%% dari Rp%,.0f/pcs)\n", pD[i], hS[i]);
        }
        KonfigurasiKafe.garis('-');
        System.out.printf("  Subtotal sementara : Rp%,.0f\n", getSub());
        KonfigurasiKafe.garis('-');
    }
}

// MATERI OVERLOADING 
class Pembayaran extends Transaksi {

    public double hitungTotal(double s) {
        return s * (1 + KonfigurasiKafe.PAJAK);
    }

    public double hitungTotal(double s, double disk) {
        double r = s - disk;
        if (r < 0) r = 0;
        return r * (1 + KonfigurasiKafe.PAJAK);
    }

    public void cetakStruk(int meja, double bayar, String kasir, String pelanggan) {
        KonfigurasiKafe.garis('=');
        System.out.println("       " + KonfigurasiKafe.NAMA_KAFE);
        System.out.println("  "      + KonfigurasiKafe.ALAMAT);
        KonfigurasiKafe.garis('=');

        LocalDateTime now = LocalDateTime.now();
        System.out.println("Penjualan : SCCD" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        System.out.println("Tanggal   : "     + now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")));
        System.out.println("Pelanggan : "     + pelanggan);
        System.out.println("No Meja   : "     + (meja == 0 ? "Takeaway" : "Meja " + meja));
        System.out.println("Kasir     : "     + kasir.toUpperCase());
        KonfigurasiKafe.garis('-');

        int tQty = 0;
        double sub = getSub();
        for (int i = 0; i < jml; i++) {
            System.out.println(item[i]);
            if (pD[i] > 0) {
                double totalItem = hD[i] * qty[i];
                double hemat     = (hS[i] - hD[i]) * qty[i];
                System.out.printf("  %dx @%.0f%30s%.0f\n",     qty[i], hD[i], "", totalItem);
                System.out.printf("  (Diskon %.0f%% dari %.0f/pcs)\n", pD[i], hS[i]);
                System.out.printf("  Hemat: -Rp%.0f\n",        hemat);
            } else {
                System.out.printf("  %dx @%.0f%30s%.0f\n",
                    qty[i], hS[i], "", hD[i] * qty[i]);
            }
            tQty += qty[i];
        }

        KonfigurasiKafe.garis('-');
        double pb1 = sub * KonfigurasiKafe.PAJAK;
        double gt  = sub + pb1;
        System.out.printf("%d item\n\n", tQty);
        System.out.printf("Subtotal      :%28.0f\n", sub);
        System.out.printf("PB 1          :%28.0f\n", pb1);
        KonfigurasiKafe.garis('-');
        System.out.printf("Grand Total   :%28.0f\n", gt);
        System.out.printf("CASH          :%28.0f\n", bayar);
        KonfigurasiKafe.garis('-');
        System.out.printf("Kembalian     :%28.0f\n", bayar - gt);
        KonfigurasiKafe.garis('-');
        System.out.println("\n              -Thank You-");
        System.out.println("         Wifi : Chatting");
        System.out.println("    Password  : " + KonfigurasiKafe.WIFI_PASS);
        KonfigurasiKafe.garis('=');
    }
}

// MAIN CLASS
public class MainPOS {
    static final String FILE_MENU = "menu.txt";

    static void simpan(Menu m) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_MENU, true))) {
            bw.write(m.getTipe() + "|" + m.getKode() + "|" + m.getNama() + "|"
                   + (int)m.getHarga() + "|" + (int)m.getDiskon() + "|" + m.getEkstra());
            bw.newLine();
        } catch (IOException e) { System.out.println("[ERROR] Gagal simpan."); }
    }

    static void hapusDariFile(String kode) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_MENU));
            lines.removeIf(l -> l.split("\\|")[1].equalsIgnoreCase(kode));
            Files.write(Paths.get(FILE_MENU), lines);
        } catch (IOException e) { System.out.println("[ERROR] Gagal hapus."); }
    }

    static int muatMenu(Menu[] db) {
        int total = 0;
        if (!new File(FILE_MENU).exists()) {
            try {
                Files.write(Paths.get(FILE_MENU), Arrays.asList(
                    "MENU|D1|Dimsum Ayam|18000|0|",   "MENU|D2|Dimsum Udang|22000|0|",
                    "MENU|D3|Lumpia|16000|38|",        "MENU|D4|Money Bag|16000|38|",
                    "MENU|M1|Chicken Wings|25000|0|",  "MENU|M2|Burger|18000|0|",
                    "PANAS|K1|Kopi Hitam|12000|0|85",  "PANAS|K2|Teh Tarik|14000|0|80",
                    "DINGIN|T1|Es Teh|10000|0|Sedang", "DINGIN|T2|Matcha Latte|28000|0|Banyak",
                    "DINGIN|T3|Lemon Tea|15000|0|Sedang"
                ));
            } catch (IOException e) { System.out.println("[ERROR] Gagal buat default."); }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_MENU))) {
            String b;
            while ((b = br.readLine()) != null && total < db.length) {
                if (b.trim().isEmpty()) continue;
                String[] p  = b.split("\\|", -1);
                double   hg = Double.parseDouble(p[3]);
                double   dk = Double.parseDouble(p[4]);
                if      (p[0].equals("PANAS"))  db[total++] = new MinumanPanas(p[1], p[2], hg, dk, p[5].isEmpty() ? 80    : Integer.parseInt(p[5]));
                else if (p[0].equals("DINGIN")) db[total++] = new MinumanDingin(p[1], p[2], hg, dk, p[5].isEmpty() ? "Sedang" : p[5]);
                else                            db[total++] = new Menu(p[1], p[2], hg, dk);
            }
        } catch (Exception e) { System.out.println("[ERROR] Gagal baca file."); }
        return total;
    }

    static void tampilMenu(Menu[] db, int total) {
        KonfigurasiKafe.garis('=');
        System.out.println("  DAFTAR MENU — " + KonfigurasiKafe.NAMA_KAFE);
        KonfigurasiKafe.garis('=');
        String[] judul = {"MAKANAN", "MINUMAN PANAS", "MINUMAN DINGIN"};
        for (int t = 1; t <= 3; t++) {
            System.out.println("  === " + judul[t-1] + " ===");
            boolean ada = false;
            for (int i = 0; i < total; i++) {
                if (db[i] == null) continue;
                boolean ok = (t == 1 && !(db[i] instanceof MinumanPanas) && !(db[i] instanceof MinumanDingin))
                          || (t == 2 && db[i] instanceof MinumanPanas)
                          || (t == 3 && db[i] instanceof MinumanDingin);
                if (ok) { db[i].tampilkanMenu(); ada = true; } 
            }
            if (!ada) System.out.println("  (Tidak ada)");
        }
        KonfigurasiKafe.garis('=');
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Menu[]  db = new Menu[50];
        int totalMenu = muatMenu(db);
        System.out.println("Sistem: " + totalMenu + " menu dimuat.\n");

        // MATERI ARRAY 2D
        String[][] mejaTabel = new String[5][3];
        for (int i = 0; i < 5; i++) Arrays.fill(mejaTabel[i], "Kosong");

        // MATERI JAGGED ARRAY
        String[][] histori       = new String[100][];
        int        totalTrx      = 0;
        int        totalTerjual  = 0;
        double     totalPendapatan = 0;

        Pembayaran          trx  = new Pembayaran();
        KonfigurasiKafe.Info info = new KonfigurasiKafe.Info();

        while (true) {
            KonfigurasiKafe.garis('=');
            System.out.println("  SISTEM POS — " + KonfigurasiKafe.NAMA_KAFE);
            System.out.println("  " + info.alamat);
            KonfigurasiKafe.garis('=');
            System.out.print("  1. Login KASIR\n  2. Login ADMIN\n  3. Keluar\nPilih: ");

            // MATERI EXCEPTION HANDLING
            try {
                int pil = sc.nextInt(); sc.nextLine();

                if (pil == 3) { System.out.println("Sistem: Keluar."); break; }
                if (pil != 1 && pil != 2) { System.out.println("Pilihan tidak valid."); continue; }

                System.out.print("Sandi " + (pil == 1 ? "KASIR" : "ADMIN") + ": ");
                String sandi = sc.nextLine();
                
                // PERUBAHAN: Memanggil method jembatan verifikasi karena variabel sandi sudah private
                boolean sandiBenar = (pil == 1) ? KonfigurasiKafe.cekSandiKasir(sandi) : KonfigurasiKafe.cekSandiAdmin(sandi);
                if (!sandiBenar)
                    throw new IllegalAccessException("[ERROR] Sandi salah! Akses ditolak.");

                if (pil == 1) {
                    System.out.print("Nama Kasir: ");
                    String namaKasir = sc.nextLine();
                    boolean sesiKasir = true;

                    while (sesiKasir) {
                        tampilMenu(db, totalMenu);
                        System.out.print(
                            "  1. Tambah Pesanan\n" +
                            "  2. Lihat Pesanan\n"  +
                            "  3. Batalkan Item\n"  +
                            "  4. Hitung Pesanan\n" +
                            "  5. Cetak Struk\n"    +
                            "  6. Status Meja\n"    +
                            "  7. Kembali\nPilih: "
                        );
                        int pilK = sc.nextInt(); sc.nextLine();

                        switch (pilK) {
                            case 1:
                                System.out.print("Nama Pelanggan: ");
                                String namaPelanggan = sc.nextLine();
                                System.out.print("No Meja (1-5, 0=Takeaway): ");
                                int noMeja = sc.nextInt(); sc.nextLine();
                                if (noMeja < 0 || noMeja > 5) {
                                    System.out.println("[ERROR] Meja tidak valid."); break;
                                }
                                trx.reset();
                                boolean lagi = true;
                                while (lagi) {
                                    // PERUBAHAN: Menambahkan try-catch local untuk exception penanganan menu dihapus
                                    try {
                                        System.out.print("Kode Menu: ");
                                        String kd = sc.nextLine();
                                        Menu mFound = null;
                                        for (int i = 0; i < totalMenu; i++) {
                                            if (db[i] != null && db[i].getKode().equalsIgnoreCase(kd)) { 
                                                mFound = db[i]; 
                                                break; 
                                            }
                                        }
                                        
                                        // TRIGGER EXCEPTION: Jika mFound null, diasumsikan produk tidak ada/sudah dihapus Admin
                                        if (mFound == null) {
                                            throw new NoSuchElementException("[EXCEPTION] Produk '" + kd + "' tidak tersedia! Menu tersebut telah dihapus oleh Admin atau tidak terdaftar.");
                                        }

                                        System.out.print("Qty: ");
                                        int q = sc.nextInt(); sc.nextLine();
                                        if (q < 1) { System.out.println("[ERROR] Qty tidak valid."); continue; }
                                        
                                        trx.tambah(mFound.getNama(), mFound.getHarga(),
                                                   mFound.getHargaSetelahDiskon(), mFound.getDiskon(), q);
                                        if (noMeja >= 1)
                                            for (int s = 0; s < 3; s++)
                                                if (mejaTabel[noMeja-1][s].equals("Kosong")) {
                                                    mejaTabel[noMeja-1][s] = mFound.getNama() + "(x" + q + ")"; break;
                                                }
                                        System.out.println("Ditambahkan: " + mFound.getNama() + " x" + q);
                                        
                                    } catch (NoSuchElementException e) {
                                        // Menangkap dan menampilkan exception di bagian kasir sesuai permintaan kamu
                                        System.out.println(e.getMessage());
                                    }
                                    
                                    System.out.print("Tambah lagi? (y/n): ");
                                    lagi = sc.nextLine().equalsIgnoreCase("y");
                                }
                                System.out.println("Sistem: Pesanan " + namaPelanggan
                                    + " (" + (noMeja == 0 ? "Takeaway" : "Meja " + noMeja) + ") tersimpan.");
                                break;

                            case 2:
                                System.out.println("\n--- DAFTAR PESANAN SAAT INI ---");
                                trx.tampilPesanan();
                                break;

                            case 3:
                                if (trx.jml == 0) { System.out.println("Belum ada pesanan."); break; }
                                System.out.println("\n--- BATALKAN ITEM ---");
                                trx.tampilPesanan();
                                System.out.print("Nomor item yang dibatalkan: ");
                                int noBatal = sc.nextInt(); sc.nextLine();
                                String namaItemBatal = (noBatal >= 1 && noBatal <= trx.jml) ? trx.item[noBatal-1] : "";
                                if (trx.batalItem(noBatal))
                                    System.out.println("Sistem: '" + namaItemBatal + "' berhasil dibatalkan.");
                                else
                                    throw new IllegalArgumentException("[ERROR] Nomor item tidak valid.");
                                break;

                            case 4:
                                if (trx.jml == 0) { System.out.println("Belum ada pesanan."); break; }
                                double sub = trx.getSub();
                                double gt  = trx.hitungTotal(sub);
                                KonfigurasiKafe.garis('-');
                                System.out.printf("Subtotal   : Rp%,.0f\nPB1(10%%)  : Rp%,.0f\nGrand Total: Rp%,.0f\n",
                                    sub, sub * KonfigurasiKafe.PAJAK, gt);
                                KonfigurasiKafe.garis('-');
                                System.out.print("Uang Bayar: Rp");
                                double bayar = sc.nextDouble(); sc.nextLine();
                                if (bayar < gt) throw new ArithmeticException("[ERROR] Uang pembayaran tidak mencukupi.");
                                System.out.printf("Kembalian  : Rp%,.0f\nSistem: Bayar diterima.\n", bayar - gt);
                                break;

                            case 5:
                                if (trx.jml == 0) { System.out.println("Belum ada pesanan."); break; }
                                System.out.print("Nama Pelanggan: "); String pConf = sc.nextLine();
                                System.out.print("No Meja: ");        int mConf    = sc.nextInt(); sc.nextLine();
                                System.out.print("Uang Bayar: Rp");   double bConf = sc.nextDouble(); sc.nextLine();
                                double gtC = trx.hitungTotal(trx.getSub());
                                if (bConf < gtC) throw new ArithmeticException("[ERROR] Uang pembayaran tidak mencukupi.");
                                trx.cetakStruk(mConf, bConf, namaKasir, pConf);
                                histori[totalTrx] = new String[trx.jml];
                                for (int i = 0; i < trx.jml; i++) {
                                    histori[totalTrx][i] = trx.item[i] + " x" + trx.qty[i];
                                    totalTerjual += trx.qty[i];
                                }
                                totalTrx++; totalPendapatan += gtC; trx.reset();
                                break;

                            case 6:
                                System.out.println("\n--- STATUS MEJA (ARRAY 2D) ---");
                                for (int i = 0; i < 5; i++)
                                    System.out.println("Meja " + (i+1) + " : " + Arrays.toString(mejaTabel[i]));
                                break;

                            case 7: sesiKasir = false; break;
                            default: System.out.println("Pilihan tidak valid.");
                        }
                    }

                } else {
                    boolean sesiAdmin = true;
                    while (sesiAdmin) {
                        KonfigurasiKafe.garis('=');
                        System.out.println("  PANEL ADMIN");
                        KonfigurasiKafe.garis('=');
                        System.out.print(
                            "  1. Tambah Produk\n" +
                            "  2. Lihat Produk\n"  +
                            "  3. Hapus Produk\n"  +
                            "  4. Laporan\n"        +
                            "  5. Histori\n"        +
                            "  6. Kembali\nPilih: "
                        );
                        int pilA = sc.nextInt(); sc.nextLine();

                        switch (pilA) {
                            case 1:
                                System.out.print("Kode: "); String kd = sc.nextLine();
                                boolean dup = false;
                                for (int i = 0; i < totalMenu; i++) {
                                    if (db[i] != null && db[i].getKode().equalsIgnoreCase(kd)) dup = true;
                                }
                                if (dup) { System.out.println("[ERROR] Kode sudah ada!"); break; }
                                System.out.print("Nama: ");       String nm  = sc.nextLine();
                                System.out.print("Harga: ");      double hg  = sc.nextDouble();
                                System.out.print("Diskon(%): ");  double dk  = sc.nextDouble();
                                System.out.print("Kategori (1.Makanan  2.Panas  3.Dingin): ");
                                int kat = sc.nextInt(); sc.nextLine();
                                Menu mBaru;
                                if (kat == 2) {
                                    System.out.print("Suhu(C): ");
                                    mBaru = new MinumanPanas(kd, nm, hg, dk, sc.nextInt()); sc.nextLine();
                                } else if (kat == 3) {
                                    System.out.print("Level Es: ");
                                    mBaru = new MinumanDingin(kd, nm, hg, dk, sc.nextLine());
                                } else {
                                    mBaru = new Menu(kd, nm, hg, dk);
                                }
                                db[totalMenu++] = mBaru;
                                simpan(mBaru);
                                System.out.println("Sistem: Tersimpan permanen.");
                                break;

                            case 2:
                                tampilMenu(db, totalMenu); break;

                            case 3:
                                for (int i = 0; i < totalMenu; i++) {
                                    if (db[i] == null) continue;
                                    System.out.printf("  %2d. ", (i+1)); db[i].tampilkanMenu();
                                }
                                System.out.print("Kode dihapus: "); String kH = sc.nextLine();
                                int idx = -1;
                                for (int i = 0; i < totalMenu; i++) {
                                    if (db[i] != null && db[i].getKode().equalsIgnoreCase(kH)) idx = i;
                                }
                                if (idx == -1) throw new IllegalArgumentException("[ERROR] Produk tidak ditemukan.");
                                System.out.print("1. Hapus Sementara\n2. Hapus Permanen\nPilih: ");
                                int pH = sc.nextInt(); sc.nextLine();
                                for (int i = idx; i < totalMenu - 1; i++) db[i] = db[i+1];
                                db[--totalMenu] = null;
                                if (pH == 2) { hapusDariFile(kH); System.out.println("Sistem: Dihapus permanen."); }
                                else           System.out.println("Sistem: Dihapus sementara.");
                                break;

                            case 4:
                                KonfigurasiKafe.garis('-');
                                System.out.printf(
                                    "  Penerimaan : Rp%,.0f\n  Pajak PB1  : Rp%,.0f\n  Terjual    : %d item\n  Transaksi  : %d\n",
                                    totalPendapatan, totalPendapatan - (totalPendapatan / 1.1),
                                    totalTerjual, totalTrx);
                                KonfigurasiKafe.garis('-'); break;

                            case 5:
                                if (totalTrx == 0) { System.out.println("Belum ada transaksi."); break; }
                                System.out.println("\n--- HISTORI (JAGGED ARRAY) ---");
                                for (int i = 0; i < totalTrx; i++)
                                    System.out.println("Trx #" + (i+1) + " -> " + Arrays.toString(histori[i]));
                                break;

                            case 6: sesiAdmin = false; break;
                            default: System.out.println("Pilihan tidak valid.");
                        }
                    }
                }

            } catch (InputMismatchException e) {
                System.out.println("\n[ERROR] Masukkan ANGKA."); sc.nextLine();
            } catch (Exception e) {
                System.out.println("\n" + e.getMessage());
            }
        }
        sc.close();
    }
}