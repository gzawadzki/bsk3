package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.StrictMath.abs;

public class Controller implements Initializable {

    public Label result_label;
    public Label klucz_label;
    public Label slowo_label;
    public Label warning;

    public RadioButton radio_railfence;
    public RadioButton radio_macierzeA;
    public RadioButton radio_macierzeB;
    public RadioButton cezar;
    public RadioButton radio_macierzeC;
    public RadioButton radio_vigener;

    public TextField klucz_tf;
    public TextField slowo_tf;
    public TextField result3_tf;
    public Button end_button;
    public Button randomButton;
    public RadioButton radio_des;

    ToggleGroup toggleGroup = new ToggleGroup();
    ArrayList<String> words;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        radio_railfence.setToggleGroup(toggleGroup);
        radio_macierzeB.setToggleGroup(toggleGroup);
        radio_macierzeA.setToggleGroup(toggleGroup);
        radio_macierzeC.setToggleGroup(toggleGroup);
        radio_des.setToggleGroup(toggleGroup);
        cezar.setToggleGroup(toggleGroup);
        radio_vigener.setToggleGroup(toggleGroup);

        words = new ArrayList<>();
        File file = new File("src/sample/file.txt");
        try {
            Scanner in = new Scanner(file);
            while (in.hasNext()) {
                words.add(in.nextLine());
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

    }

    public void endButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) end_button.getScene().getWindow();
        stage.close();
    }

    public void randomButtonPressed(ActionEvent actionEvent) {
        Random variable = new Random();
        int k = variable.nextInt(words.size());

        slowo_tf.setText(words.get(k));
    }

    public void szyfrujButtonClicked(ActionEvent actionEvent) {
        RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();

        if (klucz_tf.getText().isEmpty() || slowo_tf.getText().isEmpty()) {
            warning.setText("Pola 'Klucz' oraz 'Słowo' nie mogą być puste ");
        } else {

            /*Szyfrowanie metodą rail fence*/
            if (radio_railfence.equals(selected)) {
                String word = slowo_tf.getText();
                int key = Integer.parseInt(klucz_tf.getText());

                char[] output = SzyfrowanieRailFence(word.toCharArray(), key);
                result3_tf.setText(String.valueOf(output));

            } /*Szyfrowanie metodą macierzową A*/ else if (radio_macierzeA.equals(selected)) {
                String word = slowo_tf.getText();
                String key = String.valueOf((klucz_tf.getText()));
                int[] keyArr = Stream.of(key.replaceAll("-", "").split("")).mapToInt(Integer::parseInt).toArray();
                String result = SzyfrowanieMacierzoweA(word, keyArr);

                result3_tf.setText(result);


            } /*Szyfrowanie metodą macierzową B*/ else if (radio_macierzeB.equals(selected)) {
                String word = slowo_tf.getText();
                String key = klucz_tf.getText();
                String result = szyfrowanieMacierzB(word, key);
                result3_tf.setText(result);

            } /*Szyfr Cezara*/ else if (cezar.equals(selected)) {
                String word = slowo_tf.getText();
                int key = Integer.parseInt(klucz_tf.getText());
                String result = Cezar(word, key, 0);

                result3_tf.setText(result);

            } /*Szyfrowanie metodą macierzową C*/ else if (radio_macierzeC.equals(selected)) {
                String word = slowo_tf.getText();
                String key = klucz_tf.getText();
                String result = szyfrowanieMacierzC(word, key);
                result3_tf.setText(result);

            } /*Szyfrowanie Vigenere'a*/ else if (radio_vigener.equals(selected)) {
                String word = slowo_tf.getText();
                String key = klucz_tf.getText();
                String result = vigenere(word, key);
                result3_tf.setText(result);

            }/*Szyfrowanie DES*/ else if(radio_des.equals(selected)){
                desAlgorithm des = new desAlgorithm();
                String key = klucz_tf.getText();
                String word = desAlgorithm.stringToHex(slowo_tf.getText());
                String result= des.desCiphering(word,key,true);
                result3_tf.setText(result);


            }

            else {
                warning.setText("Wybierz metodę szyfrowania");
            }

        }
    }

    public void deszyfrujButtonClicked(ActionEvent actionEvent) {
        RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();

        if (klucz_tf.getText().isEmpty() || slowo_tf.getText().isEmpty()) {
            warning.setText("Pola 'Klucz' oraz 'Słowo' nie mogą być puste ");
        } else {
            String word = slowo_tf.getText();


            /*Deszyfrowanie metodą rail fence*/
            if (radio_railfence.equals(selected)) {
                int key = Integer.parseInt(klucz_tf.getText());

                char[] result = DeszyfrowanieRailFence(word.toCharArray(), key);
                result3_tf.setText(String.valueOf(result));

            }
            /*Deszyfrowanie metodą macierzową A*/
            else if (radio_macierzeA.equals(selected)) {
                String key = String.valueOf((klucz_tf.getText()));
                int[] keyArr = Stream.of(key.replaceAll("-", "").split("")).mapToInt(Integer::parseInt).toArray();
                String result = DeszyfrowanieMacierzoweA(word, keyArr);
                result3_tf.setText(result);

            } /*Deszyfrowanie metodą macierzową B*/ else if (radio_macierzeB.equals(selected)) {
                String key = klucz_tf.getText();
                String result = deszyfrowanieMacierzB(word, key);
                result3_tf.setText(result);

            } /*Deszyfrowanie Cezara*/ else if (cezar.equals(selected)) {
                int key = Integer.parseInt(klucz_tf.getText());
                String result = Cezar(word, key, 1);

                result3_tf.setText(result);

            }
            /*Deszyfrowanie metodą macierzową C*/
            else if (radio_macierzeC.equals(selected)) {
                String key = klucz_tf.getText();
                String result = deszyfrowanieMacierzC(word, key);
                result3_tf.setText(result);

            } /*Deszyfrowanie metodą vigener'a*/ else if (radio_vigener.equals(selected)) {
                String key = klucz_tf.getText();
                String result = desVigenere(word, key);
                result3_tf.setText(result);
            }/*Deszyforwanie DES*/ else if(radio_des.equals(selected)){
                desAlgorithm des = new desAlgorithm();
                String key = klucz_tf.getText();
                String s = slowo_tf.getText();
                String result= des.desCiphering(s,key,false);

                result3_tf.setText(desAlgorithm.convertHexToStringValue(result));
            }
            else {
                warning.setText("Wybierz metodę deszyfrowania");
            }
        }
    }


    /* Rail fence poniżej */
    public static void init(char[][] tab, int height, int size) {
        for (char[] row : tab)
            Arrays.fill(row, '0');

    }


    public static char[] result(char[] output, int size, char[][] password, int key) {
        int line = 0;

        for (int i = 0; i < key; i++)
            for (int x = 0; x < size; x++) {
                if (password[i][x] != '0') {
                    output[line] = password[i][x];
                    line++;
                }
            }

        return output;
    }

    public static String SzyfrowanieMacierzoweA(String password, int[] keyArr) {
        double cols = keyArr.length;
        int i = 0;
        int j = 0;
        double rows = Math.ceil(password.length() / cols);

        char[][] cipher = fillMatrixA(cols, rows, password);
        StringBuilder answer = new StringBuilder();
        for (i = 0; i < rows; i++) {
            for (int k : keyArr) {
                char next = cipher[i][k - 1];
                if (next != '0') {
                    answer.append(cipher[i][k - 1]);
                }

            }
        }
        return answer.toString();
    }

    public static String DeszyfrowanieMacierzoweA(String password, int[] keyArr) {
        double cols = keyArr.length;
        double rows = Math.ceil(password.length() / cols);
        char[][] tab = new char[(int) rows][(int) cols];

        //wypełnienie zerami
        init(tab, (int) rows, (int) cols);
        fillMatrixDecode(password.length(), rows, cols, tab, keyArr, password);
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char next = tab[i][j];
                if (next != '0') {
                    answer.append(tab[i][j]);
                }
            }
        }
        System.out.println(answer);
        return answer.toString();
    }

    public static char[][] fillMatrixA(double cols, double rows, String password) {
        char[][] cipher = new char[(int) rows][(int) cols];
        char[] e = password.toCharArray();
        int i = 0;
        int j = 0;
        init(cipher, (int) rows, (int) cols);
        for (i = 0; i < e.length; i++) {
            cipher[j][(int) (i % cols)] = e[i];
            if (i % cols == cols - 1) {
                j++;
            }
        }
        return cipher;
    }

    private static void fillMatrixDecode(int wordLength, double rows, double cols, char[][] matrix, int[] arr, String text) {
        int row = 0;
        int keyIndex = 0;
        int col = arr[keyIndex];
        int remainder = (int) (wordLength % cols);
        System.out.println("reszta" + remainder);

        for (int i = 0; i < wordLength; i++) {

            // check field out of bounds
            while (remainder > 0 && row == rows - 1 && col > remainder) {
                keyIndex++;
                col = arr[keyIndex];
            }
            matrix[row][col - 1] = text.charAt(i);
            keyIndex++;

            if (keyIndex == cols) {
                row++;
                keyIndex = 0;
            }

            col = arr[keyIndex];


        }

    }

    public static char[] SzyfrowanieRailFence(char[] password, int key) {
        char[][] szyfr = new char[key][password.length];
        char[] output = new char[password.length];
        int new_key = 2 * key - 2;
        int j = 0, height = 1;

        init(szyfr, key, password.length);

        for (int i = 0; i < password.length; i++) {
            szyfr[abs(j)][i] = password[i];

            if (i % new_key == 0) {
                height *= 1;
            }
            if ((i + key - 1) % new_key == 0) {
                height *= -1;
            }
            j += height;
        }

        result(output, password.length, szyfr, key);

        return output;
    }

    public static char[] DeszyfrowanieRailFence(char[] password, int key) {
        char[][] szyfr = new char[key][password.length];
        char[] output = new char[password.length];
        int new_key = 2 * key - 2;
        int j = 0, height = -1, n = 0;

        init(szyfr, key, password.length);

        for (int i = 0; i < password.length; i++) {
            szyfr[abs(j)][i] = '*';
            if (i % new_key == 0 || (i + key - 1) % new_key == 0)
                height *= -1;

            j += height;
        }

        for (int y = 0; y < key; y++)
            for (int x = 0; x < password.length; x++) {
                if (szyfr[y][x] == '*') {
                    szyfr[y][x] = password[n];
                    n++;
                }
            }

        j = 0;
        height = -1;

        for (int i = 0; i < password.length; i++) {
            output[i] = szyfr[abs(j)][i];

            if (i % new_key == 0) {
                height *= 1;
            }
            if ((i + key - 1) % new_key == 0) {
                height *= -1;
            }
            j += height;
        }

        return output;
    }


    /* Szyfr Cezara*/

    public static String Cezar(String password, int key, int param) {
        StringBuilder sb = new StringBuilder();

        switch (param) {
            case 0:
                for (int i = 0; i < password.length(); i++) {
                    sb.append(szyfrujZnak(password.substring(i, i + 1), key));
                }
                break;
            case 1:
                for (int i = 0; i < password.length(); i++) {
                    sb.append(deszyfrujZnak(password.substring(i, i + 1), key));
                }
                break;
        }

        return sb.toString();
    }

    public static String szyfrujZnak(String s, int key) {
        int index = 0;
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        for (int i = 0; i < alphabet.length; i++) {
            if (s.equalsIgnoreCase(alphabet[i])) {
                index = (i + key) % (alphabet.length);
                break;
            }
            if (s.equals(" ") || s.equals(",")) {
                return s;
            }
        }

        return alphabet[index];
    }

    public static String deszyfrujZnak(String s, int key) {
        int index = 0;
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        for (int i = 0; i < alphabet.length; i++) {

            if (s.equalsIgnoreCase(alphabet[i])) {
                index = i - key;

                if (index < 0) {
                    index = alphabet.length + index;
                }
                break;
            }
            if (s.equals(" ") || s.equals(",")) {
                return s;
            }
        }

        return alphabet[index];
    }

    /*Szyfrowanie metodą macierzową C*/

    public static String szyfrowanieMacierzC(String s, String k) {

        s = s.replaceAll(" ", "");
        int[] key = kolejnoscKlucza(k);
        String out = "";
        int r = 0;
        int count = 0;
        //ustalenie ile bedzie potrzebnych wierszy do macierzy
        for (int i = 0; i < key.length; i++) {
            count += key[i];
            r++;
            if (count >= s.length())
                break;
        }
        //wypelnienie tablicy zerami
        char[][] tab = new char[r][k.length()];
        for (char[] row : tab)
            Arrays.fill(row, '0');
        //wpisywanie po kolei liter danego slowa do macierzy wierszami(do momentu określonego przez key)
        int x = 0;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < key[i]; j++) {

                if (x == s.length())
                    break;
                tab[i][j] = s.charAt(x);
                x++;
            }
        }
        //odczytywanie kolumnami zaszyfrowanego kodu
        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < r; j++) {
                if (tab[j][key[i] - 1] != '0') {
                    out += tab[j][key[i] - 1];

                }
            }
            out += " ";
        }

        return out;
    }

    public static String deszyfrowanieMacierzC(String s, String k) {
        s = s.replaceAll(" ", "");
        int[] o = kolejnoscKlucza(k);
        int[] key = polozenie(o);
        String out = "";
        int r = 0;
        int count = 0;
        int last = 0;

        for (int i = 0; i < o.length; i++) {
            if (count + o[i] >= s.length()) {
                last = s.length() - count;
                r++; //kontrolujemy liczbe wierszy
                break;
            }
            count += o[i];
            r++; //kontrolujemy liczbe wierszy
        }

        //wypelnienie zerami
        char[][] tab = new char[r][k.length()];
        for (char[] row : tab)
            Arrays.fill(row, '0');

        int x = 0;
        for (int i = 0; i < o.length; i++) {
            int c = o[i] - 1;
            for (int j = 0; j < r; j++) {
                if (x == s.length())
                    break;
                if (o[j] > c) {

                    if (j < r - 1 || (j == (r - 1) && c < last)) {

                        tab[j][c] = s.charAt(x); //wpisywanie liter do pomocniczej macierzy tab
                        x++;
                    }
                }
            }
        }

        //odczyt odszyfrowanej wiadomosci, z pomijaniem pustych pól (0)
        for (int j = 0; j < r; j++) {
            for (int i = 0; i < key.length; i++) {
                if (tab[j][i] != '0')
                    out += tab[j][i];
            }
        }

        return out;
    }

    //zwraca tablice, ktora przechowuje miejsca liter klucza (zgodnie z alfabetem)
    public static int[] polozenie(int[] key) {
        int[] k = new int[key.length];
        for (int i = 0; i < key.length; i++) {
            k[key[i] - 1] = i + 1;
        }
        return k;
    }

    //funkcja do przyporzadkowania kolejności liter klucza co do alfabetu
    private static int[] kolejnoscKlucza(String s) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int[] k = new int[s.length()]; //tablica o rozmiarze równym liczbie kolumn macierzy(liter klucza)
        int x = 0;
        int c;

        for (int j = 0; j < alphabet.length(); j++) {
            c = -1;
            c = s.indexOf(alphabet.charAt(j), c + 1);
            //wchodzimy do while jesli znajdziemy litere z klucza
            while (c >= 0) {
                k[c] = ++x;
                c = s.indexOf(alphabet.charAt(j), c + 1); // jak dana litera wiecej sie nie powtorzy, to wychodzimy z petli (c=-1)
            }
        }

        return polozenie(k);

    }


    /*Szyfrowanie Macierz B*/
    public static String szyfrowanieMacierzB(String s, String k) {
        s = s.replace(" ", "");
        String output = "";
        int pom1 = 0; //zmienna pomocnicza potrzebna do wprowadzenia waidomosci do macierzy
        int wiersze;
        int[] klucz = kolejnoscKlucza(k); //zamiana hasla na ciag liczbowy

        if (s.length() % k.length() == 0) {
            wiersze = s.length() / k.length();              //obliczanie ilosci wierszy
        } else wiersze = wiersze = (s.length() / k.length()) + 1;


        char[][] tab = new char[k.length()][wiersze];

        for (int j = 0; j < wiersze; j++) {
            for (int i = 0; i < k.length(); i++) {  //wypelnienie tablicy zerami
                tab[i][j] = '0';
            }
        }

        for (int j = 0; j < wiersze; j++) {
            for (int i = 0; i < k.length(); i++) {
                if (pom1 >= s.length()) {
                    break;                              //wprowadzenie wiadomosci do tablicy
                } else {
                    tab[i][j] = s.charAt(pom1);
                    pom1++;
                }
            }
        }

        int i = 0;
        for (int p = 0; p < k.length(); p++) {
            for (int j = 0; j < wiersze; j++) {
                if (tab[klucz[i] - 1][j] != '0') {                  //szyfrowanie waidomosci outut przy pomocy klucza
                    output = output + tab[klucz[i] - 1][j];
                } else break;


            }
            output = output + " ";
            i++;
        }
        return output;
    }

    public static String deszyfrowanieMacierzB(String s, String k) {
        String output = "";
        int pom1 = 0;
        int[] klucz = kolejnoscKlucza2(k);
        int kol;

        kol = (s.length() / k.length()) + 1; //okreslenie ilosci kolumn (pomocnicze)

        char[][] tab = new char[kol][k.length()]; // tablica pomocnicza
        char[][] tabOut = new char[kol][k.length()]; //tablica z przestawionymi wierszami

        for (int j = 0; j < k.length(); j++) {
            for (int i = 0; i < kol; i++) {
                tab[i][j] = '0';
            }
        }
        for (int j = 0; j < k.length(); j++) {
            for (int i = 0; i < kol; i++) {
                tabOut[i][j] = '0';
            }
        }

        for (int j = 0; j < k.length(); j++) {
            for (int i = 0; i < kol; i++) {
                if (pom1 >= s.length()) {
                    break;
                }
                if (s.charAt(pom1) != ' ') {
                    tab[i][j] = s.charAt(pom1);   //wypelnienie tablicy poziomo ustawonymi fragmetami zakodowanej wiadomosci
                    pom1++;
                } else {
                    pom1++;

                    break;

                }
            }
        }


        int pom2 = 0;
        for (int j = 0; j < k.length(); j++) {
            for (int i = 0; i < kol; i++) {
                if (pom2 >= k.length()) break;
                tabOut[i][j] = tab[i][klucz[pom2] - 1];         // wypelnianie tablicy Out przestawionymi wierszami tablicy pomocniczej

            }
            pom2++;
        }

        int pom = 0;

        for (int j = 0; j < kol; j++) {
            for (int i = 0; i < k.length(); i++) {
                if (tabOut[j][i] == '0') break;     //odszyfrowywanie waidomosci
                output = output + tabOut[j][i];

            }
        }


        return output;
    }
    /* Szyfr Vigenere*/
    public static String vigenere(String s, String k) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int N = 26; //liczba liter w wierszu/kolumnie
        String out = "";
        for (int i = 0; i < s.length(); i++) {
            out += alphabet.charAt((alphabet.indexOf(s.charAt(i)) + alphabet.indexOf(k.charAt(i%k.length()))) % N);
        }

        return out;
    }

    public static String desVigenere(String s, String k) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int N = 26; //liczba liter w wierszu/kolumnie
        String out = "";
        for (int i = 0; i < s.length(); i++) {
            out += alphabet.charAt((alphabet.indexOf(s.charAt(i)) - alphabet.indexOf(k.charAt(i%k.length())) + N) % N);
        }

        return out;
    }
    private static int[] kolejnoscKlucza2(String s) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int[] k = new int[s.length()]; //tablica o rozmiarze równym liczbie kolumn macierzy(liter klucza)
        int x = 0;
        int c;

        for (int j = 0; j < alphabet.length(); j++) {
            c = -1;
            c = s.indexOf(alphabet.charAt(j), c + 1);
            //wchodzimy do while jesli znajdziemy litere z klucza
            while (c >= 0) {
                k[c] = ++x;
                c = s.indexOf(alphabet.charAt(j), c + 1); // jak dana litera wiecej sie nie powtorzy, to wychodzimy z petli (c=-1)
            }
        }

        return k;

    }
}