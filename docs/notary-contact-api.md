# Update email notaris berdasarkan nomor telepon

Endpoint ini mencari notaris dari **Telp No** (`PHONE_NO`), lalu mengganti **Email** (`EMAIL`). Nomor telepon adalah kunci pencarian dan tidak diubah. HP (`MOBILE_NO`), dokumen, dan status pengajuan juga tidak diubah.

## Alamat

```
POST http://localhost:8060/compliance/api/notary/contact
```

`8060` adalah port Jetty di `pom.xml`. Context aplikasi adalah `/compliance`.

## Header

| Header | Nilai |
| --- | --- |
| `Authorization` | `Bearer 8c1f0a7e5b2d49c6a3e87104f6d9b258` |
| `Content-Type` | `application/json` |
| `Accept` | `application/json` |

Token default ada di `src/main/java/api.properties`, property `notary.api.bearer.token`.

Environment variable `NOTARY_API_BEARER_TOKEN` menimpa nilai file itu. Kalau token kosong, setiap request ditolak dengan HTTP 401.

Ganti token ini sebelum API dipakai di luar uji lokal.

## Body

```json
{
  "phoneNo": "0215550101",
  "email": "notaris@example.com"
}
```

| Field | Wajib | Aturan |
| --- | --- | --- |
| `phoneNo` | ya | Kunci pencarian. Harus sama persis dengan Telp No di data notaris, paling panjang 20 karakter, dan memuat minimal satu angka. |
| `email` | ya | Email baru. Paling panjang 100 karakter, tanpa spasi. |

`phoneNo` harus sama dengan nilai yang tersimpan, termasuk tanda hubung atau spasi. Contoh: data `021-5550101` tidak cocok dengan request `0215550101`.

## Menjalankan aplikasi

Dari root project:

```bash
mvn jetty:run
```

Tunggu sampai Jetty siap di port 8060. Aplikasi ini memakai database Oracle yang diatur di `src/main/java/login.properties`. Nomor telepon harus milik satu notaris yang masih aktif (`ENABLED_FLAG = Y`, `DEL_ID` kosong atau `0`).

## Postman

1. Buka Postman, pilih **New**, lalu **HTTP Request**.
2. Method: **POST**.
3. URL: `http://localhost:8060/compliance/api/notary/contact`
4. Tab **Authorization**:
   - Type: **Bearer Token**
   - Token: `8c1f0a7e5b2d49c6a3e87104f6d9b258`
5. Tab **Headers**:
   - `Content-Type` = `application/json`
   - `Accept` = `application/json`
6. Tab **Body**: pilih **raw** dan **JSON**, lalu isi contoh body di atas. `phoneNo` diisi Telp No yang sudah ada di database.
7. Klik **Send**.

Collection siap import ada di `docs/notary-contact-api.postman_collection.json`. Di Postman pilih **Import**, lalu file itu. Isi variable `phoneNo` dengan nomor yang nyata sebelum menekan Send.

## curl

```bash
curl -i -X POST "http://localhost:8060/compliance/api/notary/contact" \
  -H "Authorization: Bearer 8c1f0a7e5b2d49c6a3e87104f6d9b258" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d "{\"phoneNo\":\"0215550101\",\"email\":\"notaris@example.com\"}"
```

Di Windows PowerShell, pakai `curl.exe` supaya PowerShell tidak mengganti `curl` dengan `Invoke-WebRequest`.

## Response sukses (HTTP 200)

```json
{
  "notaryId": 15,
  "notaryName": "Notaris Contoh",
  "phoneNo": "0215550101",
  "email": "notaris@example.com",
  "lastUpdateBy": "API",
  "lastUpdateDate": "2026-10-14T14:30:00+07:00"
}
```

`notaryId` dan `notaryName` hanya dikembalikan supaya baris yang terpilih bisa dicek. `lastUpdateBy` diisi `API`. `lastUpdateDate` memakai zona waktu server.

## Response gagal

Semua error memakai bentuk yang sama:

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Bearer token is missing or invalid"
}
```

| HTTP | Penyebab |
| --- | --- |
| 401 | Header `Authorization` tidak ada, bukan `Bearer`, atau token salah. |
| 400 | JSON rusak, `phoneNo` kosong atau lebih dari 20 karakter, email kosong atau formatnya salah. |
| 404 | Tidak ada notaris aktif dengan Telp No itu. |
| 409 | Lebih dari satu notaris aktif memakai Telp No yang sama, jadi tidak ada baris yang diubah. |
| 405 | Method bukan POST atau PUT. |
| 415 | `Content-Type` bukan `application/json`. |
| 500 | Penyimpanan ke database gagal. Detail error hanya tertulis di log server. |

## Cek hasil di halaman

Buka data notaris yang sama di halaman Notary. Field **Email** mengikuti nilai yang baru dikirim. **Telp No** dan **HP No** tetap seperti semula.
