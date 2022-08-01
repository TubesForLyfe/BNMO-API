# BNMO-API

## Cara Menjalankan

- Siapkan file `application.properties` dengan mengkopi template dari file `application.properties.example` dan value `apilayer.apikey` diisi dengan apikey yang didapat <a href="https://apilayer.com/account">di sini</a>. (Apabila ingin menjalankan aplikasi tanpa menggunakan database Docker, silahkan mengganti value setiap `spring.datasource`).

- Untuk mendapatkan file .jar dari aplikasi

`> ./mvnw clean package`

- Jika menggunakan Docker, jalankan perintah berikut pada terminal.

Untuk melakukan build dan menjalankan container

`> docker-compose up --build`

Untuk menjalankan container saja

`> docker-compose up`

- Jika tanpa menggunakan Docker, jalankan perintah berikut pada terminal.

Untuk menjalankan aplikasi secara langsung

`> ./mvnw spring-boot:run`

Untuk menjalankan aplikasi dari jar

`> java -jar ./target/bnmo-api-0.0.1-SNAPSHOT.jar`

## Design Pattern yang digunakan

- Adapter

Adapter adalah design pattern struktural yang memungkinkan objek dengan interface yang tidak kompatibel untuk berkolaborasi. Pada aplikasi ini, Adapter diimplementasikan untuk mengkoordinasikan objek token yang terdapat pada file `Token.java` dengan interface hak mengakses API yang terdapat pada file `RolePermissionInterface.java`. Hasil koordinasi antara objek dan interface ini tertuang dalam file `Role.java`.

- Abstract Factory

Abstract Factory adalah dessign patern creational yang menghasilkan family dari objek terkait tanpa menentukan kelas konkretnya. Pada aplikasi ini, Abstract Factory diimplementasikan dalam family dari SQL dengan menggunakan interface untuk menghasilkan string dari SQL Command (insert, select, update, dan delete) dari masing-masing objek SQL. Family dari SQL terdapat dalam direktori `classes/sql` dan interface dari family SQL terdapat pada file `SQLInterface.java`.

## Tech Stack yang digunakan

Tech Stack yang digunakan adalah <strong>Java Spring Boot dan PostgreSQL</strong> dengan versi Java 17 dan Spring Boot 2.7.2.

## Cara Menggunakan Sistem

- Registrasi dan login dapat dilakukan tanpa menggunakan <strong>authorization bearer token</strong>.
- Setelah login sukses, token yang dikirim sebagai response dapat digunakan sebagai <strong>authorization bearer token</strong> dalam pemanggilan API selain registrasi dan login.

## Endpoint yang dibuat

- Admin
1. Melakukan login
2. Melakukan verifikasi akun customer
3. Melakukan verifikasi request penambahan/pengurangan saldo oleh customer
4. Melakukan pencarian data customer

- Customer
1. Melakukan registrasi dan login
2. Melakukan request penambahan/pengurangan saldo
3. Melihat informasi profil dan saldo
4. Melakukan transfer saldo ke rekening lain
5. Melihat riwayat transaksi

## Fitur Tambahan

- Customer

Melihat riwayat request penambahan/pengurangan saldo beserta status dan waktu verifikasi dari request tersebut