package kim.milyoner;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ProgramSunucu {

    //5 adet sorudan oluşan  bir liste oluşturalım
    private static ArrayList<Question> soruListesi() {
        ArrayList<Question> sorular = new ArrayList<>();

        sorular.add(new Question("Java dili ilk ne zaman piyasaya çıktı?",
                new String[]{"1995", "2000", "1991", "1989"}, 'A'));

        sorular.add(new Question("TCP/IP modeli kaç katmandan oluşur?",
                new String[]{"7", "4", "5", "6"}, 'B'));

        sorular.add(new Question("Socket sınıfı hangi pakette yer alır?",
                new String[]{"java.net", "java.io", "java.util", "java.lang"}, 'A'));

        sorular.add(new Question("IP adresi “192.168.1.1” hangi IP adres sınıfına aittir?",
                new String[]{"Sınıf A", "Sınıf B", "Sınıf C", "Sınıf D"}, 'C'));

        sorular.add(new Question("Hangi protokol bağlantısız (connectionless) veri iletiminde kullanılır?",
                new String[]{"TCP", "UDP", "FTP", "SMTP"}, 'B'));

        return sorular;
    }

    public static void main(String[] args) {
        final int PORT_YARISMACI = 4337; //Yarışmacının dinlendiği port
        final String JOKER_IP = "127.0.0.1"; //Joker sunucusunun IP adresi
        final int PORT_JOKER = 4338; //Jokerin dinlediği port

        PrintWriter yarismaciOut = null; // Yarışmacıya veri göndermek için yazıcı
        BufferedReader yarismaciIn = null; // Yarışmacıdan veri okumak için okuyucu

        boolean jokerKullanildiS = false; // Seyirci jokeri kullanıldı mı?
        boolean jokerKullanildiY = false; // Yarı yarıya jokeri kullanıldı mı?

        try {
            // Joker sunucusuna bağlan
            Socket jokerSocket = new Socket(JOKER_IP, PORT_JOKER);
            PrintWriter jokerOut = new PrintWriter(jokerSocket.getOutputStream(), true);
            BufferedReader jokerIn = new BufferedReader(new InputStreamReader(jokerSocket.getInputStream()));
            System.out.println("Joker sunucusuna bağlanıldı.");

            // Yarışmacı için sunucu oluştur
            ServerSocket serverSocket = new ServerSocket(PORT_YARISMACI);
            System.out.println("Yarışmacı bekleniyor...");

            // Yarışmacı bağlantısı kabul ediliyor
            Socket yarismaciSocket = serverSocket.accept();
            System.out.println("Yarışmacı bağlandı: " + yarismaciSocket.getInetAddress());

            // Giriş-çıkış akışları atanıyor
            yarismaciIn = new BufferedReader(new InputStreamReader(yarismaciSocket.getInputStream()));
            yarismaciOut = new PrintWriter(yarismaciSocket.getOutputStream(), true);

            // Yarışmacıdan ilk mesaj alınır
            String gelenMesaj = yarismaciIn.readLine();
            System.out.println("Yarışmacıdan gelen mesaj: " + gelenMesaj);
            yarismaciOut.println("Merhaba Yarışmacı, bağlantınız başarıyla kuruldu!");

            // Sorular ve ödül mesajları
            ArrayList<Question> sorular = soruListesi();
            String[] odulMesajlari = {
                    "Linç Yükleniyor",
                    "Önemli olan katılmaktı",
                    "İki birden büyüktür",
                    "Buralara kolay gelmedik",
                    "Sen bu işi biliyorsun",
                    "Harikasın"
            };

            int dogruSayisi = 0; // Doğru cevap sayısı

            for (int i = 0; i < sorular.size(); i++) {
                Question soru = sorular.get(i);

                // Soru + joker metni tek mesajda gönderiliyor
                StringBuilder mesaj = new StringBuilder();
                mesaj.append((i + 1) + ". Soru: " + soru.questionText + "\n");
                mesaj.append("Şıklar: A) " + soru.options[0] + " B) " + soru.options[1] +
                        " C) " + soru.options[2] + " D) " + soru.options[3] + "\n");
                mesaj.append("Jokerler: Seyirciye Sorma (S), Yarı Yarıya (Y)\n");
                mesaj.append("Cevabınızı girin (A, B, C, D) veya Joker kullanın (S, Y):");
                yarismaciOut.println(mesaj.toString());

                String girdi = yarismaciIn.readLine();
                if (girdi == null) break;
                girdi = girdi.trim().toUpperCase();

                // Giriş kontrol döngüsü
                while (!girdi.matches("[ABCDSY]")) {
                    yarismaciOut.println("Geçersiz giriş. Lütfen A, B, C, D, S veya Y girin:");
                    girdi = yarismaciIn.readLine();
                    if (girdi == null) break;
                    girdi = girdi.trim().toUpperCase();
                }

                // Joker kontrolleri
                if (girdi.equals("S")) {
                    if (jokerKullanildiS) {
                        yarismaciOut.println("Seyirci jokeri zaten kullanıldı. Lütfen başka bir giriş yapın.");
                        i--;
                        continue;
                    } else {
                        jokerOut.println("SEYIRCI|" + soru.correctAnswer);
                        String seyirciSonucu = jokerIn.readLine();
                        yarismaciOut.println("Seyirci cevabı: " + seyirciSonucu);
                        jokerKullanildiS = true;
                        i--;
                        continue;
                    }
                } else if (girdi.equals("Y")) {
                    if (jokerKullanildiY) {
                        yarismaciOut.println("Yarı Yarıya jokeri zaten kullanıldı. Lütfen başka bir giriş yapın.");
                        i--;
                        continue;
                    } else {
                        jokerOut.println("YARI|" + soru.correctAnswer);
                        String elenen = jokerIn.readLine();
                        yarismaciOut.println("Yarı Yarıya Jokeri: Elenen şıklar: " + elenen);
                        jokerKullanildiY = true;
                        i--;
                        continue;
                    }
                } else if (girdi.equals(String.valueOf(soru.correctAnswer))) {
                    dogruSayisi++;
                    yarismaciOut.println("Tebrikler! Doğru cevap.");
                } else {
                    yarismaciOut.println("Yanlış cevap. Yarışma sona erdi.");
                    yarismaciOut.println("Kazanılan ödül: " + odulMesajlari[dogruSayisi]);
                    break;
                }
            }

            // Tüm sorular doğruysa
            if (dogruSayisi == sorular.size()) {
                yarismaciOut.println("Tüm soruları doğru cevapladınız!");
                yarismaciOut.println("Ödül: " + odulMesajlari[5]);
            }

            // Bağlantılar kapatılıyor
            yarismaciIn.close();
            yarismaciOut.close();
            jokerIn.close();
            jokerOut.close();
            yarismaciSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            System.err.println("Bağlantı hatası: " + e.getMessage());
        }
    }
}
