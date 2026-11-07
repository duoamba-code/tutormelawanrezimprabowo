# Kirin Voxel Bridge — build untuk pengguna Windows 7

## Kenapa build lokal gagal?

Gradle baru memuat `gradle-fileevents.dll` yang menggunakan API Windows 10 `ReadDirectoryChangesExW`. API itu tidak tersedia di Windows 7. Menambahkan `--no-watch-fs` tidak cukup, karena DLL sudah dirujuk ketika native services diinisialisasi.

Karena itu, jangan jalankan Gradle 9.x dari Windows 7 untuk proyek ini. Source tetap bisa diedit di Windows 7; proses kompilasi dilakukan di runner Linux GitHub Actions.

## Isi paket

- Source code Tahap 1
- Konfigurasi Fabric 1.21.11
- Fabric Loader 0.18.4
- Fabric API 0.141.3+1.21.11
- Fabric Loom 1.14.10 (plugin remap untuk Minecraft 1.21.11)
- Workflow `.github/workflows/build.yml`

## Cara build tanpa memasang Gradle di Windows 7

1. Ekstrak ZIP ini. Pastikan `.github/workflows/build.yml` ikut terekstrak.
2. Masuk ke GitHub melalui browser dan buat repository baru. Pilih **Private** bila source ingin disimpan privat.
3. Pada repository, pilih **Add file → Upload files**.
4. Unggah isi folder proyek ke root repository. Jangan mengunggah folder induk ZIP sebagai satu subfolder; `build.gradle`, `settings.gradle`, `gradle.properties`, `src/`, dan `.github/` harus berada di root repository.
5. Klik **Commit changes**.
6. Buka tab **Actions**. Pilih workflow **Build Kirin Voxel Bridge**.
7. Klik **Run workflow**, pilih branch `main`, lalu klik tombol hijau **Run workflow**.
8. Setelah run berstatus sukses, buka halaman run tersebut. Di bagian **Artifacts**, unduh `kirinvx-fabric-1.21.11`.
9. Ekstrak artifact ZIP. Ambil `kirinvx-0.1.0.jar` (bukan `-sources.jar`) dan taruh ke folder `mods` pada profil Minecraft Fabric 1.21.11. Fabric API juga harus tersedia di folder `mods`.

## Hasil build

`build/libs/kirinvx-0.1.0.jar`

## Catatan

- Workflow menggunakan JDK 21 dan Gradle 9.5.1 di Ubuntu GitHub-hosted runner.
- JAR ditargetkan untuk Java 21 / Minecraft 1.21.11.
- Tahap 1 hanya memindai voxel/entity dan menyediakan perintah debug; belum mengirim data ke Iris dan belum merender path tracing.
- Build belum dieksekusi dari sesi ini; workflow adalah konfigurasi untuk dijalankan pada GitHub Actions.
