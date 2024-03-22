//Piotr Kondziolka - 7
import java.util.Scanner;

class Source {

    public static Scanner scanner = new Scanner(System.in); //tworzymy obiekt scanner

    public void maxSum(int input[][]){

        int rows = input.length;        //liczba wierszy
        int cols = input[0].length;     //liczba kolumn


        //sprawdzenie czy tablica ma tylko ujemne elementy
        boolean flag = true;
        for(int i = 0; i < rows; i++) {

            for(int j = 0; j < cols; j++) {

                if(input[i][j] >= 0){
                    flag = false;
                    break;
                }
            }
        }

        //tablica pomocnicza o rozmiarze rows
        int temp[] = new int[rows];
        //w tej tablicy beda przechowywane sumy wierszy przy odpowiednich ograniczeniach LEFT i RIGHT

        Result result = new Result();   //tworzymy obiekt Result

                                            //idea algorytmu//
        //przesuwamy ograniczniki left i right po kolumnach tak zeby rozpatrzyc kazdy spojny podciag
        //indeksow [0..cols], dla kazdego takiego podciagu indeksow do tablicy temp wpisujemy sume
        //kazdego wiersza ogarniczonego ogranicznikami left i right nastepnie korzystajac z algorytmu Kadane
        //dla tablicy temp szukamy w niej maksymalnej spojnej podtablicy
        //indeksy konca i poczatku tej podtablicy to odpowiednio ograrniczenie dolne i gorne maksynalnej
        //podtablicy 2D ktorej szukamy, natomiast ogarniczenie lewe i prawe dla podtablicy 2D to ograniczniki left i right
        //wyniki aktualizujemy w kolejnych iteracjach


        for(int left = 0; left < cols; left++){

            //zerowanie tablicy pomocniczej przy kazdym przesunieciu left
            for(int i = 0; i < rows; i++){
                temp[i] = 0;
            }

            for(int right = left; right < cols; right++){

                //aktualizowanie tablicy pomocniczej dla nowego ograniczenia right
                for(int i = 0; i < rows; i++){
                    temp[i] += input[i][right];
                }

                //korzystamu z algorytmu Kadane dla tablicy pomocniczej
                KadaneResult kadaneResult = kadane(temp);

                //jesli algorytm kadane dal nam wieksza sume to aktualizujemy wyniki
                if( kadaneResult.maxSum > result.maxSum ){

                    result.maxSum = kadaneResult.maxSum;
                    result.maxLeft = left;
                    result.maxRight = right;
                    result.maxUp = kadaneResult.start;
                    result.maxDown = kadaneResult.end;
                    result.size =  ((result.maxDown-result.maxUp + 1) * (result.maxRight-result.maxLeft + 1));
                }

                //przypadek w ktorym sumy w podtablicach sa rowne
                //wtedy zgodnie z poleceniem interesuje nas podtablica o mniejszym rozmiarze
                if( kadaneResult.maxSum == result.maxSum
                && ( (right-left + 1)*(kadaneResult.end - kadaneResult.start + 1) <  result.size ) ){

                        result.maxLeft = left;
                        result.maxRight = right;
                        result.maxUp = kadaneResult.start;
                        result.maxDown = kadaneResult.end;
                        result.size = ((result.maxDown - result.maxUp + 1) * (result.maxRight - result.maxLeft + 1));
                }

                //przypadek w ktorym rozmiary podtablic i ich sumy sa rowne
                //wtedy interesuje nas podtablica ktorej indeksy tworza ciag leksykograficznie najmniejszy
                if( kadaneResult.maxSum == result.maxSum
                        && ( (right-left + 1)*(kadaneResult.end - kadaneResult.start + 1) ==  result.size ) ){


                    //definicja pomocniczych indeksow s(i,j,k,l) dla obu tablic
                    int i1 = kadaneResult.start;
                    int j1 = kadaneResult.end;
                    int k1 = left;
                    int l1 = right;
                    int i2 = result.maxUp;
                    int j2 = result.maxDown;
                    int k2 = result.maxLeft;
                    int l2 = result.maxRight;

                    boolean flaga = false;

                    //sprawdzenie porzadku leksykograficznego
                    if(i1 < i2){
                        flaga = true;   //jesli indeksi i1 jest mniejszy niz i2 to juz wiemy ze bedziemy dokonywac zmian
                    }
                    else if(j1 < j2){  //...a jesli nie to sprawdzamy kolejny itd..
                        flaga = true;
                    }
                    else if(k1 < k2){
                        flaga = true;
                    }
                    else if(l1 < l2){
                        flaga = true;
                    }
                    if(flaga == true) {

                        result.maxLeft = left;
                        result.maxRight = right;
                        result.maxUp = kadaneResult.start;
                        result.maxDown = kadaneResult.end;
                        result.size = ((result.maxDown - result.maxUp + 1) * (result.maxRight - result.maxLeft + 1));
                    }
                }
            }
        }


        if(flag == false)
            result.displayresult();
        else
            System.out.println("s = 0, mst is empty");
    }

    //w obiekcie typu Result przechowujemy dane aktualnie najlepszej podtablicy
    class Result{
        long maxSum;
        int maxLeft;
        int maxRight;
        int maxUp;
        int maxDown;
        int size;

        public void displayresult(){

            System.out.println("s = " + maxSum + ", mst = a[" + maxUp + ".." + maxDown + "]" + "[" + maxLeft + ".." + maxRight + "]");

        }
    }

    //przechwujmey wyniki wykonania algorytmu Kadane
    class KadaneResult{
        long maxSum;
        int start;
        int end;

        public KadaneResult(long MAXSUM, int START, int END) {   //konstruktor
            maxSum = MAXSUM;
            start = START;
            end = END;
        }
    }

    //algorytm Kadane
    public KadaneResult kadane(int arr[]){

        long max = Integer.MIN_VALUE;
        int maxStart = 0;
        int maxEnd = 0;
        int currentStart = 0;
        int maxSoFar = 0;

        for(int i = 0; i < arr.length; i++){

            maxSoFar += arr[i];

            if(max < maxSoFar){
                maxStart = currentStart;
                maxEnd = i;
                max = maxSoFar;
            }

            if(maxSoFar <= 0){
                maxSoFar = 0;
                currentStart = i+1;
            }
        }
        return new KadaneResult(max, maxStart, maxEnd); //tworzymy nowy obiekt typu KadaneResult
    }

    public static void main(String[] args) {

        int ile = 0;
        int nz = 0;
        char dwukropek = ':';
        int n = 0;
        int m = 0;

        int lb_zestawow = scanner.nextInt();

        while(ile < lb_zestawow){

            Source wynik = new Source();

            //wejscie
            nz = scanner.nextInt();                 //numer zestawu
            dwukropek = scanner.next().charAt(0);   //znak dwukropka
            n = scanner.nextInt();                  //liczba wierszy
            m = scanner.nextInt();                  //liczba kolumn

            int tablica[][] = new int[n][m];        //definiowanie tablicy

            //wypelnienie tablicy
            for(int i=0; i<n; i++){
                for(int j=0; j<m; j++){

                    tablica[i][j] = scanner.nextInt();

                }
            }
            //osobno rozwazam tablice 1D i 2D
            if(n!=1 && m!=1) {
                System.out.print(nz + ": " + "n = " + n + " m = " + m + ", ");
                wynik.maxSum(tablica);
            }
            else if(n==1){

                //sprawdzenie czy tablica ma tylko ujemne elementy
                boolean flag = true;
                for(int i = 0; i < m; i++) {
                        if(tablica[0][i] >= 0){
                            flag = false;
                            break;
                        }
                }
                //algorytm Kadane z jednym dodatkowym warunkiem
                long max = Integer.MIN_VALUE;
                int maxStart = 0;
                int maxEnd = 0;
                int currentStart = 0;
                int maxSoFar = 0;

                for(int i = 0; i < m; i++){

                    maxSoFar += tablica[0][i];

                    if(max < maxSoFar){
                        maxStart = currentStart;
                        maxEnd = i;
                        max = maxSoFar;
                    }

                    //jesli suma aktualnie rozwazanej podtablicy i najlepszej sa rowne to interesuje nas podtablica
                    //o mniejszym rozmiarze
                    if(max == maxSoFar){

                        if( (i - currentStart +1) < (maxEnd - maxStart +1) ){
                            maxStart = currentStart;
                            maxEnd = i;
                            max = maxSoFar;
                        }


                    }

                    if(maxSoFar <= 0){
                        maxSoFar = 0;
                        currentStart = i+1;
                    }
                }

                if(flag == false) {
                    System.out.print(nz + ": " + "n = " + n + " m = " + m + ", ");
                    System.out.println("s = " + max + ", mst = a[" + 0 + ".." + 0 + "]" + "[" + maxStart + ".." + maxEnd + "]");
                }
                else
                    System.out.println(nz + ": " + "n = " + n + " m = " + m + ", s = 0, mst is empty");
            }
            else if(m==1){

                //sprawdzenie czy tablica ma tylko ujemne elementy
                boolean flag = true;
                for(int i = 0; i < n; i++) {
                    if(tablica[i][0] >= 0){
                        flag = false;
                        break;
                    }
                }

                //algorytm Kadane z jednym dodatkowym warunkiem
                long max = Integer.MIN_VALUE;
                int maxStart = 0;
                int maxEnd = 0;
                int currentStart = 0;
                int maxSoFar = 0;

                for(int i = 0; i < n; i++){

                    maxSoFar += tablica[i][0];

                    if(max < maxSoFar){
                        maxStart = currentStart;
                        maxEnd = i;
                        max = maxSoFar;
                    }

                    //jesli suma aktualnie rozwazanej podtablicy i najlepszej sa rowne to interesuje nas podtablica
                    //o mniejszym rozmiarze
                    if(max == maxSoFar){

                        if( (i - currentStart +1) < (maxEnd - maxStart +1) ){
                            maxStart = currentStart;
                            maxEnd = i;
                            max = maxSoFar;
                        }

                    }

                    if(maxSoFar <= 0){
                        maxSoFar = 0;
                        currentStart = i+1;
                    }
                }

                if(flag == false) {
                    System.out.print(nz + ": " + "n = " + n + " m = " + m + ", ");
                    System.out.println("s = " + max + ", mst = a[" + maxStart + ".." + maxEnd + "]" + "[" + 0 + ".." + 0 + "]");
                }
                else
                    System.out.println(nz + ": " + "n = " + n + " m = " + m + ", s = 0, mst is empty");
            }

            ile++;
        }
    }
}