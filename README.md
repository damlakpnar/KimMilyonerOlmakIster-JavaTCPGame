# 🎮 Kim Milyoner Olmak İster – Bilgisayar Ağları Dersi Projesi

Bu proje, üniversitedeki **Bilgisayar Ağları** dersi kapsamında geliştirdiğim bir dönem projesidir. Amaç, istemci-sunucu mimarisini kullanarak çok oyunculu bir bilgi yarışması oyunu oluşturmaktı. Yarışma formatı olarak hepimizin bildiği **"Kim Milyoner Olmak İster?"** programını seçtim.

Uygulama tamamen **Java** ile yazıldı ve **terminal (komut satırı)** üzerinden çalışıyor. Her yarışmacı ayrı bir istemci olarak sunucuya bağlanıyor. Sunucu, yarışmanın kontrolünü elinde tutuyor: sırayla soruları soruyor, cevapları alıyor, doğru mu yanlış mı olduğunu kontrol ediyor ve yarışmanın akışını yönetiyor.

İstemciler sadece terminalden gelen soruları görüyor ve yine terminal üzerinden cevap veriyor. Birden fazla kişi aynı anda bağlanıp yarışmaya katılabiliyor. Oyuncuların **joker** kullanma hakkı da var, yani yarışma mantığını olabildiğince gerçek formata yakın tutmaya çalıştım. 

Teknik olarak **TCP soket programlama** kullanıldı. `ServerSocket` ile sunucu tarafında bağlantılar dinleniyor, `Socket` ile istemciler bağlanıyor. Her istemci için ayrı bir thread açılıyor, böylece herkes aynı anda sistemde kalabiliyor. Veri alışverişi `BufferedReader` ve `PrintWriter` ile yapılıyor.

Sonuç olarak bu proje, hem Java'da socket programlamayı hem de çok oyunculu bir oyun mantığını anlamak açısından benim için oldukça öğretici oldu. Kod yapısı elimden geldiğince okunabilir ve sade olacak şekilde düzenlendi. Eğlenceli ve öğretici bir deneyimdi
