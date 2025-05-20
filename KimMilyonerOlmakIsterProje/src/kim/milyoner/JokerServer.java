package kim.milyoner;

import java.io.*;
import java.net.*;
import java.util.*;

public class JokerServer {
    public static void main(String[] args) {
        final int PORT = 4338; // Joker sunucusunun dinleyeceği port

        try {
            // Sunucu soketi oluşturuluyor
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Joker Server başlatıldı. Port: " + PORT);

            // Program sunucusundan gelen bağlantı kabul ediliyor
            Socket clientSocket = serverSocket.accept();
            System.out.println("Program sunucusu bağlandı: " + clientSocket.getInetAddress());

            // Giriş ve çıkış akışları oluşturuluyor
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            Random rand = new Random();
            String gelenMesaj;

            // Program sunucusundan gelen komutları işle
            while ((gelenMesaj = in.readLine()) != null) {
                System.out.println("Gelen Mesaj: " + gelenMesaj);

                // Mesajı parçala → örnek: "SEYIRCI|B"
                String[] parcalar = gelenMesaj.split("\\|");
                if (parcalar.length != 2) {
                    out.println("Hatalı format: Doğru cevap belirtilmemiş.");
                    continue;
                }

                String komut = parcalar[0];           // SEYIRCI veya YARI
                char dogruCevap = parcalar[1].charAt(0); // Örn: A, B, C, D

                if (komut.equalsIgnoreCase("SEYIRCI")) {
                    // Seyirci jokeri: Doğru cevaba yüksek oran ver
                    Map<Character, Integer> oylar = new LinkedHashMap<>();
                    oylar.put('A', 0);
                    oylar.put('B', 0);
                    oylar.put('C', 0);
                    oylar.put('D', 0);

                    int dogruOy = 60 + rand.nextInt(31); // %60–90
                    int kalan = 100 - dogruOy;

                    // Geri kalan %’yi yanlış şıklara dağıt
                    List<Character> yanlislar = new ArrayList<>(oylar.keySet());
                    yanlislar.remove((Character) dogruCevap);
                    Collections.shuffle(yanlislar);

                    int ilk = rand.nextInt(kalan + 1);
                    int ikinci = rand.nextInt(kalan - ilk + 1);
                    int ucuncu = kalan - ilk - ikinci;

                    oylar.put(dogruCevap, dogruOy);
                    oylar.put(yanlislar.get(0), ilk);
                    oylar.put(yanlislar.get(1), ikinci);
                    oylar.put(yanlislar.get(2), ucuncu);

                    // Cevabı oluştur
                    StringBuilder sonuc = new StringBuilder();
                    for (Map.Entry<Character, Integer> entry : oylar.entrySet()) {
                        sonuc.append(entry.getKey()).append(": %").append(entry.getValue()).append("  ");
                    }

                    out.println(sonuc.toString().trim());

                } else if (komut.equalsIgnoreCase("YARI")) {
                    // Yarı yarıya jokeri,2 yanlış şık elenecek
                    List<Character> tumSiklar = Arrays.asList('A', 'B', 'C', 'D');
                    List<Character> yanlislar = new ArrayList<>(tumSiklar);
                    yanlislar.remove((Character) dogruCevap); // doğruyu çıkar
                    Collections.shuffle(yanlislar);

                    char elenen1 = yanlislar.get(0);
                    char elenen2 = yanlislar.get(1);

                    out.println(elenen1 + " ve " + elenen2 + " elendi.");
                } else {
                    out.println("Geçersiz joker komutu: " + komut);
                }
            }

            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            System.err.println("Hata: " + e.getMessage());
        }
    }
}



