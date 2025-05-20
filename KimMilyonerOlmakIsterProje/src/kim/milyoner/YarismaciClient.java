package kim.milyoner;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class YarismaciClient {
    public static void main(String[] args) {
        final String SERVER_IP = "127.0.0.1";   // Program Sunucusu IP adresi (localhost)
        final int SERVER_PORT = 4337;           // Program Sunucusu portu

        try {
            // Program Sunucusuna bağlan
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Program Sunucusuna bağlanıldı.");

            // Sunucudan gelen mesajları okumak için
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Sunucuya mesaj göndermek için
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Kullanıcıdan veri almak için
            Scanner scanner = new Scanner(System.in);

            // Bağlantının başında tanıtıcı mesaj gönder
            out.println("Merhaba Program Sunucusu, ben Yarışmacıyım!");

            // Sunucudan gelen mesajları döngüyle sürekli oku
            while (true) {
                String mesaj = in.readLine();  // Program sunucusundan bir mesaj oku
                if (mesaj == null) break;      // Bağlantı kapandıysa çık

                System.out.println(mesaj);     // Gelen mesajı ekrana yaz

                // Eğer bu mesaj bir soru ise, cevap iste
                if (mesaj.contains("Cevabınızı girin")) {
                    System.out.print("Cevabınız: ");  // Kullanıcıya sor
                    String cevap = scanner.nextLine();  // Kullanıcıdan cevap al
                    out.println(cevap);  // Cevabı sunucuya gönder
                }
            }

            // Bağlantı sonlandırılıyor
            socket.close();

        } catch (IOException e) {
            System.err.println("Bağlantı hatası: " + e.getMessage());
        }
    }
}


