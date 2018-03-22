package prolab2proje1final;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class node{
    public double latitude;
    public double longtitude;
    public double rakim;
    public int plaka;
    public int onceki_plaka;
    public double maliyet= Double.POSITIVE_INFINITY;
    ArrayList<kenar> komsuluk = new ArrayList<kenar>();
}

class kenar{
    public node end;
    public double rakimAcisi;
    public double mesafe;
}

class zeplin {
    node iller[] = new node[81];
    int kisi_sayisi;
    zeplin(int kisi_Sayisi){
    this.kisi_sayisi = kisi_Sayisi;
    for (int i = 0; i < iller.length; i++) {
            //Her node icin nesne olusturuldu
           iller[i] = new node();
        }
}
    
    public static double mesafe(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6372.8* c;
    }
    
    public static double hipotenus(double mesafe ,node ilk_sehir,node ikinci_sehir){
        double hipotenus;
        double rakim_farki;
        rakim_farki = Math.abs(ikinci_sehir.rakim-ilk_sehir.rakim)/1000;
        
        hipotenus=Math.sqrt(Math.pow(mesafe, 2)+Math.pow(rakim_farki, 2));
        return hipotenus;
    }
    
    public static double rakimAcisi(double basil_rakimi,double sonil_rakimi,double mesafe,int deger){
        double rakimAcisi;
        sonil_rakimi = sonil_rakimi+deger;
        double rakimFarki = (sonil_rakimi-basil_rakimi);
        rakimFarki=Math.abs(rakimFarki);
        rakimAcisi = Math.toDegrees(Math.atan(rakimFarki/mesafe));
        return rakimAcisi;
    }
    
    public double kazanc(int i){
       double para = this.kisi_sayisi*100;
       double zeplininYakiti = iller[i].maliyet*10;
       return para-zeplininYakiti;
   }
    public double kazanc1(int i){
       double para = iller[i].maliyet*10+((iller[i].maliyet*10)*50/100);
       return para/i;
   }
    public void harita(int baslangic_Plaka,int bitis_Plaka,BufferedWriter bWriter) throws FileNotFoundException, IOException{
 
       File file = new File("text.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        String line;
        
        for (node iller1 : iller) {
            line = br.readLine();
            String []bol =line.split(",");
            iller1.latitude = Double.parseDouble(bol[0]);
            iller1.longtitude = Double.parseDouble(bol[1]);
            iller1.plaka = Integer.parseInt(bol[2]);
            iller1.rakim = Integer.parseInt(bol[3]);
        }
        File file1 = new File("komsu.txt");
        FileReader fileReader1 = new FileReader(file1);
        BufferedReader br1 = new BufferedReader(fileReader1);
        
        for (node iller1 : iller) {
            line = br1.readLine();
            String dizi[] = line.split(",");
            for (String dizi1 : dizi) {
                kenar e = new kenar();
                iller1.komsuluk.add(e);
            }
            for (int j = 0; j < dizi.length; j++) {
                int k =Integer.parseInt(dizi[j]);
                iller1.komsuluk.get(j).end = iller[k-1]; //81. ilde sıkıntı var çünkü dizi 80de bitiyor.
                //çözüldü
            }
            for (int j = 0; j < dizi.length; j++) {
                iller1.komsuluk.get(j).mesafe = hipotenus(mesafe(iller1.latitude, iller1.longtitude, iller1.komsuluk.get(j).end.latitude, iller1.komsuluk.get(j).end.longtitude), 
                        iller1, iller1.komsuluk.get(j).end);  
            }
            for(int j = 0;j < dizi.length;j++){
                iller1.komsuluk.get(j).rakimAcisi = rakimAcisi(iller1.rakim, iller1.komsuluk.get(j).end.rakim, mesafe(iller1.latitude, iller1.longtitude, iller1.komsuluk.get(j).end.latitude, iller1.komsuluk.get(j).end.longtitude),0);
                    
            }
        }
        
        for(int i=0;i<iller[baslangic_Plaka-1].komsuluk.size();i++){
            iller[baslangic_Plaka-1].komsuluk.get(i).rakimAcisi=rakimAcisi(iller[baslangic_Plaka-1].rakim,iller[baslangic_Plaka-1].komsuluk.get(i).end.rakim, iller[baslangic_Plaka-1].komsuluk.get(i).mesafe, 50);
        }
        
        File file4 = new File("Komsuluklar.txt");
        if (!file4.exists()) {
            file4.createNewFile();
        }

        FileWriter fileWriter1 = new FileWriter(file4, false);
        BufferedWriter bWriter1 = new BufferedWriter(fileWriter1);
        for (int i = 0; i < iller.length; i++) {
            bWriter1.write("Plaka:"+(i+1)+"\t\tLatitude:"+iller[i].latitude+"\t\tLongtitude:"+iller[i].longtitude+"\t\tRakim:"+iller[i].rakim);
            bWriter1.newLine();
            bWriter1.write((i+1)+". İlin Komsulari:");
            for (int j = 0; j < iller[i].komsuluk.size(); j++) {
                
                bWriter1.newLine();
                bWriter1.write("Plaka:"+iller[i].komsuluk.get(j).end.plaka+"\tMesafe:"+iller[i].komsuluk.get(j).mesafe+
                        "\tLatitude:"+iller[i].komsuluk.get(j).end.latitude+"\tLongtitude:"+iller[i].komsuluk.get(j).end.longtitude+
                        "\tRakim:"+iller[i].komsuluk.get(j).end.rakim+"\tRakim Acisi:"+iller[i].komsuluk.get(j).rakimAcisi);
            }
            bWriter1.newLine();
       }
        bWriter1.close();
        
        
        
        int temp;
        for (int i = 0; i < iller.length; i++) {
            temp = iller[i].komsuluk.size();
            for (int j = 0; j < iller[i].komsuluk.size();) {
                if(iller[i].komsuluk.get(j).rakimAcisi>80-kisi_sayisi){
                    iller[i].komsuluk.remove(j);
                    
                }
                if(temp== iller[i].komsuluk.size()){
                    j++;
                }
                else{
                    j=0;
                    temp--;
                }
                
            }
       }
        
        
        
       iller[baslangic_Plaka-1].maliyet=0;
       
       node node = iller[baslangic_Plaka-1];
       int j=0,k=0;
       while(true){
            if( j==81) j=0;//Dizi 81 elemanlı ancak indis 80 de bitiyor
                          //Sifirlanma Sebebi ise birden fazla her ili dönebilmek
            
            for (int i = 0;i<node.komsuluk.size() ; i++) {
                    if(node.komsuluk.get(i).end.maliyet> node.maliyet+node.komsuluk.get(i).mesafe /*&& node.komsuluk.get(i).rakimAcisi<80-kisi_Sayisi*/){
                    node.komsuluk.get(i).end.maliyet = node.maliyet+node.komsuluk.get(i).mesafe;
                    node.komsuluk.get(i).end.onceki_plaka = node.plaka;
                }
            }
            node = iller[j];
            j++;
            k++;
            if(9999==k) break;//While dan çıksın aynı zamanda tüm düğümlerin döndüğü kesin olsun diye çok yüksek değer girildi.
        }

       
       try{
            node = iller[bitis_Plaka-1];
        }
        catch(ArrayIndexOutOfBoundsException hata){
        }
       
       ArrayList<Integer> path = new ArrayList<>();//Gidilen yolu tutan dizi
               
       path.add(bitis_Plaka);
       
       File file3 = new File("problemler.txt");
        if (!file3.exists()) {
            file3.createNewFile();
        }

       
       while(true){
        path.add(node.onceki_plaka);
            
        try{
           node = iller[node.onceki_plaka-1];
        }
        catch(ArrayIndexOutOfBoundsException hata){
            System.out.println("Bu Aci Degeri İcin Yol Yok");
            bWriter.write("Bu Aci Degeri İcin Yol Yok");
            bWriter.newLine();
            break;
        }
            if(node.plaka == baslangic_Plaka) break;
        }
       for (int i = 0; i < path.size(); i++) {
           if(path.get(1)==0){
               continue;
           }
           System.out.print(path.get(i)+" ");
           bWriter.write(path.get(i)+" ");
       }
       System.out.println("");
       bWriter.newLine();
       
       double yuzde_50;
       yuzde_50 = (iller[bitis_Plaka-1].maliyet*10+iller[bitis_Plaka-1].maliyet*10*50/100)/kisi_sayisi;
       
       if(iller[bitis_Plaka-1].maliyet == Double.POSITIVE_INFINITY){           
       }
       
       else{
       System.out.println("Maliyet:"+iller[bitis_Plaka-1].maliyet);
       System.out.println("Kazanç:"+kazanc(bitis_Plaka-1));
       System.out.println("Yüzde 50 Kar İçin Alınması Gerekilen Para:"+yuzde_50);
       bWriter.write("Maliyet:"+iller[bitis_Plaka-1].maliyet);
       bWriter.newLine();
       bWriter.write("Kazanç:"+kazanc(bitis_Plaka-1));
       bWriter.newLine();
       bWriter.write("Yüzde 50 Kar İçin Alınması Gerekilen Para:"+yuzde_50);
       bWriter.newLine();
       
       
   }
  }
    
}

public class ProLab2Proje1Final {

    public static void main(String[] args) throws IOException {
        
        
        zeplin zeplinler[] = new zeplin[46];
        //0. eleman 5 kişi için
        //45. eleman 50 kişi için
        
        Scanner scan = new Scanner(System.in);
        System.out.println("Giris İlini Giriniz.");
        int giris_ili = scan.nextInt();
        System.out.println("Cikis İlini Giriniz.");
        int cikis_ili = scan.nextInt();
        
        
        long baslangic = System.currentTimeMillis();
        
        for (int i = 0; i < zeplinler.length; i++) {
            zeplinler[i] = new zeplin(i+5);
            
        }
        
        

        File file3 = new File("problemler.txt");
        if (!file3.exists()) {
            file3.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file3, false);
        BufferedWriter bWriter = new BufferedWriter(fileWriter);
        
        bWriter.write("1. Problem");
        bWriter.newLine();
        bWriter.newLine();
        
        System.out.println("1. Problem:");
        
        for (int i = 0; i < zeplinler.length; i++) {
            System.out.print("Aci="+(75-i));
            bWriter.write("Aci:"+(75-i));
            bWriter.newLine();
            System.out.println("");
            
            zeplinler[i].harita(giris_ili, cikis_ili,bWriter);
            System.out.println("");
            bWriter.newLine();
            
        }
        
        
        for (int i = 0; i < zeplinler.length; i++) {
            zeplinler[i] = new zeplin(i+5);
            
        }
        
        
        System.out.println("2. Problem");
        bWriter.newLine();
        bWriter.write("2. Problem");
        bWriter.newLine();

        for (int i = 5; i < 50; i+=10) {
            System.out.println("Kişi Sayisi="+(i+5));
            bWriter.write("Kişi Sayisi="+(i+5));
            bWriter.newLine();
            
            zeplinler[i].harita(giris_ili, cikis_ili,bWriter);
            System.out.println("");
            bWriter.newLine();
            
        }
        
        long bitis = System.currentTimeMillis();
        bWriter.newLine();
        bWriter.newLine();
        System.out.println("Calisma Süresi:"+(bitis-baslangic)+"ms");
        bWriter.write("Calisma Süresi:"+(bitis-baslangic)+"ms");
        bWriter.newLine();
        bWriter.close();
    }
    
}
