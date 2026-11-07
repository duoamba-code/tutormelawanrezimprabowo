# Kirin Voxel Bridge, tahap 1 (Fabric 1.21.11, klien saja)

BELUM DIKOMPILASI atau DIJALANKAN oleh penulisnya (tidak ada JDK/Gradle/Minecraft di lingkungan pembuatan). Kode ditulis dengan nama Mojang
mappings untuk 1.21.11. Bila ada error kompilasi, laporkan teks errornya apa adanya.

## Yang dilakukan tahap 1
Tidak merender apa pun dan belum menyentuh Iris. Mod hanya:
1. Menjaga grid 96^3 sel di sekitar pemain (asal dipatok ke kelipatan 16 = sama dengan batas chunk). Tiap sel menyimpan id bentuk.
2. Id bentuk berasal dari `BlockState.getShape(...)` milik game sendiri, diubah ke daftar kotak (satuan 1/16 blok, maks 16 kotak). Blok mod ikut otomatis.
3. Memindai bergulir: tiap tick N unit 16^3 dibaca ulang (bawaan 4 unit = ~16 ribu sel/tick). Ini pengganti sementara untuk event perubahan blok.
4. Melacak sampai 64 entity terdekat sebagai kotak pembatas (`getBoundingBox`), tiap tick.
5. Melaporkan biaya: ms per unit, jumlah bentuk unik, ukuran tabel bila dikirim ke shader, memori.

## Cara memasang (membangun)
1. Pasang JDK 21 dan IntelliJ IDEA.
2. Paling aman: buat proyek dari generator resmi https://fabricmc.net/develop/template (Minecraft 1.21.11, Mojang mappings, tanpa data generation).
   Lalu timpa `src/main/java/net/kirin/voxelbridge/*` dan `src/main/resources/fabric.mod.json` dengan isi zip ini.
   (File build.gradle / gradle.properties / settings.gradle di zip ini hanya contoh; wrapper Gradle tidak disertakan.)
3. Jalankan `./gradlew build`; jar ada di `build/libs/` (ambil yang tanpa `-sources`).
4. Taruh jar di folder `mods` bersama Fabric API. Iris tidak diperlukan di tahap ini.

## Cara memakai (di dalam game)
Masuk ke dunia, tunggu beberapa detik, lalu ketik di chat:
- `/kirinvx` atau `/kirinvx stats`: laporan (lihat di atas).
- `/kirinvx dump`: tulis seluruh tabel bentuk ke `config/kirinvx-shapes.txt` (id | blockstate | kotak). Periksa blok mod Anda di sini.
- `/kirinvx speed <1..216>`: unit per tick. Naikkan bila ingin grid cepat penuh, turunkan bila FPS turun.
- `/kirinvx rescan`: mulai ulang. `/kirinvx toggle`: hidup/mati.

## Cara membaca hasilnya
- "ms/tick" pemindaian harus kecil (patokan kasar: di bawah 1 ms). Bila besar di CPU Anda, turunkan `speed`.
- "melebihi 16 kotak" > 0: ada bentuk mod yang terlalu rumit dan dipotong. Lihat `dump`.
- "sel berubah di putaran terakhir" tinggi terus saat Anda diam = ada yang tidak stabil (mis. getShape bergantung posisi); laporkan.

## Batas yang sudah diketahui
- `getShape` itu bentuk outline/interaksi, bukan model visual. Daun, lectern, dan blok bermodel khusus bisa sedikit berbeda dari tampilannya.
- Offset acak tanaman (+-0.25) tidak dimodelkan.
- Bentuk di-cache per BlockState; blok yang bentuknya bergantung pada dunia/posisi di luar blockstate dihitung dari posisi pertama kali terlihat.
- Chunk yang belum dimuat klien terbaca sebagai kosong.

## Tahap berikutnya (belum ada)
2. Hubungkan ke Iris: kirim grid + tabel bentuk + kotak entity sebagai tekstur ke shader pack (cara resminya belum ada, perlu mixin).
3. Ubah `vx.glsl` supaya membaca tabel kotak, bukan bentuk yang ditulis tangan per tipe.
Tahap 2 dan 3 sengaja belum ditulis: nama kelas internal Iris/Sodium 1.21.11 harus dibaca dulu dari kodenya.

## Bila gagal dikompilasi (titik paling mungkin)
- `level.hasChunk(x, z)` di VoxelWorld: ganti dengan `level.getChunkSource().hasChunk(x, z)`.
- `level.getEntities((Entity) null, area, predicate)`: bila namanya beda, cari metode setara di `ClientLevel`/`EntityGetter`.
- `ClientCommandManager` / `sendFeedback(Component)`: ini nama untuk 1.21.11; di Minecraft 26.x kelasnya berganti jadi `ClientCommands`.
