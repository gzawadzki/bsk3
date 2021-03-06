package sample;

import java.util.ArrayList;
import java.util.Arrays;

import static sample.desTables.*;

public class desAlgorithm {

    private int[] keyMoves = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private int[][] subkeys = new int[16][48];
    public String desCiphering(String s, String k, boolean encode) {

        StringBuilder answer = new StringBuilder(); //sluzy do budowania zaszyfrowanej odpowiedzi

        //uzupełnienie i podzial do pelnych blokow
        ArrayList<String> blocks = prepareWord(s);

        int[] key = hexToBinary(k);

        for (String block : blocks) {
            int[] word = hexToBinary(block);

            answer.append(des(word, key, encode)); //wywolujemy metode StringBuildera(append) i za pomoca des budujemy zaszyfrowaną odpowiedz

        }

        //usuwanie nieznaczących bitow
        if (!encode) {
            int bitsToDelete = hexToDec(answer.charAt(answer.length() - 1));
            answer.delete(answer.length() - bitsToDelete, answer.length());
        }

        return answer.toString();

    }

    //z szesnastkowych na dziesiętne
    private int hexToDec(char c) {

        int result;
        switch (c) {
            case 'a':
                result = 10;
                break;
            case 'b':
                result = 11;
                break;
            case 'c':
                result = 12;
                break;
            case 'd':
                result = 13;
                break;
            case 'e':
                result = 14;
                break;
            case 'f':
                result = 15;
                break;
            default:
                result = Integer.parseInt(String.valueOf(c));
                break;

        }
        return result;

    }

    private ArrayList<String> prepareWord(String word) { //przygotowanie uzyskanych danych

        int remain = word.length();
        int iter = (int) Math.ceil(word.length() / 16.0); //dzielimy uzyskane słowo na 16-bitowe bloki (format hex)
        ArrayList<String> blocks = new ArrayList<>();

        for (int i = 0; i < iter; i++) {
            StringBuilder fullBlock = new StringBuilder();

            if (remain > 16) { //sprawdzenie czy slowo ma ponad 16 znakow
                for (int x = 0; x < 16; x++) {
                    fullBlock.append(word.charAt(i * 16 + x));
                }

                blocks.add(fullBlock.toString());
            } else {
                for (int x = 0; x < remain; x++) {
                    fullBlock.append(word.charAt(i * 16 + x));
                }
                //uzupełnienie zerami (do przedostatniego bitu) w przypadku gdy ktorys blok jest krotszy
                for (int j = remain; j < 15; j++) {
                    fullBlock.append(0);

                }

                //wstawienie na ostatni bit liczby oznaczającej ilosc uzupelnionych miejsc
                fullBlock.append(remainToHex(16-remain));

                blocks.add(fullBlock.toString());
            }

            remain -= 16;
        }
        return blocks;

    }

    //zamiana na szesnastkowe
    private char remainToHex(int remain) {
        char result = String.valueOf(0).charAt(0);
        if (remain < 10) {
            result = String.valueOf(remain).charAt(0);
        } else {
            switch (remain) {
                case 10:
                    result = 'a';
                    break;
                case 11:
                    result = 'b';
                    break;
                case 12:
                    result = 'c';
                    break;
                case 13:
                    result = 'd';
                    break;
                case 14:
                    result = 'e';
                    break;
                case 15:
                    result = 'f';
                    break;
            }

        }
        return result;
    }

    //przeprowadzenie operacji xor na tablicy
    private int[] xorTables(int[] first, int[] second) {
        int[] result = new int[first.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = xor(first[i], second[i]);
        }

        return result;
    }

    // wykonanie operacji xor (w jezyku java za operacje xor odpowiada ^)
    private int xor(int a, int b) {
        return a ^ b;
    }

    /**
     * Metoda która permutuje bloki bitów zgodnie z podanym rozmiarem i używa tabel z klasy desTables, które są niezbędne do algorytmu des
     *
     * @param block     -> blok do permutacji
     * @param permArray -> tabela z desTables
     * @param finalSize -> rozmiar który chcemy osiągnąć np. 48 bitów
     * @return
     */
    private int[] makePermutation(int[] block, int[] permArray, int finalSize) {
        int[] newBlock = new int[finalSize];
        for (int i = 0; i < finalSize; i++) {
            int index = permArray[i] - 1;
            newBlock[i] = block[index];
        }
        return newBlock;

    }

    /**
     * przesunięcia w lewo, każdy bit jest przesuwany w lewo,
     * najstarszy bit (pierwszy od lewej) przesuwany jest
     * na ostatnie miejsce(skrajnie po prawej)
     */
    private int[] moveKeyBits(int[] key, int times) {
        int answer[] = new int[key.length];
        System.arraycopy(key, 0, answer, 0, key.length);
        for (int i = 0; i < times; i++) {
            int temp = answer[0];
            for (int j = 0; j < key.length - 1; j++) {
                answer[j] = answer[j + 1];
            }
            answer[key.length - 1] = temp;
        }
        return answer;
    }


    /**
     * Dwie poniższe metody dzielą dany blok bitów na dwa mniejsze odpowienio na lewą lub prawą część
     *
     * @param block
     * @return
     */
    private int[] getLeftPart(int[] block) {
        return Arrays.copyOfRange(block, 0, block.length / 2);
    }

    private int[] getRightPart(int[] block) {
        return Arrays.copyOfRange(block, block.length / 2, block.length);
    }


    /**
     * Metoda która dopełnia do 4 bitów liczby zamienione na bity wzięte z tabel Sn. Potrzebne jest to do przekształcania kluczy
     * @param bit ciąg bitów
     * @return
     */
    private StringBuilder completeTo4(String bit) {
        StringBuilder result = new StringBuilder();
        if (bit.length() < 4) {
            for (int i = bit.length(); i < 4; i++) {
                result.append("0");
            }
        }
        result.append(bit);


        return result;
    }

    /**
     * Metoda która przekształca klucz do postaci końcowej, zmiejsza ilość bitów oraz jest on permutowany
     *
     * @param key -> klucz
     */
    private void prepareSubkeys(int[] key) {
        for (int i = 1; i <= 16; i++) {

            int[] tmpLeft = new int[28];
            int[] tmpRight = new int[28];

            System.arraycopy(key, 0, tmpLeft, 0, 28);
            System.arraycopy(key, 28, tmpRight, 0, 28);

            int[] movedLeft = moveKeyBits(tmpLeft, keyMoves[i - 1]);
            int[] movedRight = moveKeyBits(tmpRight, keyMoves[i - 1]);


            int[] tmpSubkey = new int[56];
            System.arraycopy(movedLeft, 0, tmpSubkey, 0, 28);
            System.arraycopy(movedRight, 0, tmpSubkey, 28, 28);

            int[] finalSubkey = new int[48];
            finalSubkey = makePermutation(tmpSubkey, keyPC_2, 48);

            for (int j = 0; j < 48; j++) {
                subkeys[i - 1][j] = finalSubkey[j];

            }

            System.arraycopy(tmpSubkey, 0, key, 0, 56);


        }
    }

    /**
     * metoda potrzebna do wypisania liczby szesnastkowej z tablicy bitów
     * @param bits tablica bitów
     */
    private static StringBuilder displayBits(int[] bits) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < bits.length; i += 4) {
            String output = new String();
            for (int j = 0; j < 4; j++) {
                output += bits[i + j];   // składanie czteroznakowych liczb binarnych
            }
            result.append(Integer.toHexString(Integer.parseInt(output, 2))); //zamiana na 16
        }
        return result;
    }
    /**
     *metoda potrzebna do zamiany liczby szesnastkowej(string) na binarna (tablica intów)
     * @param hex liczba szesnastkowa
     * @return liczba binarna w postaci tablicy intów
     */
    private static int[] hexToBinary(String hex) {
        int[] result = new int[hex.length() * 4];
        for (int i = 0; i < hex.length(); i++) {

            String s = Integer.toBinaryString(Integer.parseInt(hex.charAt(i) + "", 16)); //zamiana pojedynczych cyfr na binarny

            while (s.length() < 4) {
                s = "0" + s; //dopełnienie do 4 bitów
            }
            for (int j = 0; j < 4; j++) {
                result[(4 * i) + j] = Integer.parseInt(s.charAt(j) + "");//zapisywanie bitów do tablicy
            }

        }
        return result;


    }
    /**
     * metoda dokonująca transformacji 6 bitowego bloku danych przy pomocy bloków od S1 do S8
     * @param block
     * @return blok po permutacji
     */
    private int[] transformBySBlocks(int[] block) {
        int[] result = new int[32];

        for (int i = 0; i < 8; i++) {
            int[] tmpBlock = new int[6];

            System.arraycopy(block, i * 6, tmpBlock, 0, 6);
            // ustalanie wuersza
            StringBuilder row = new StringBuilder();
            row.append(tmpBlock[0]);
            row.append(tmpBlock[5]);
            //ustalanie kolumny
            StringBuilder column = new StringBuilder();
            column.append(tmpBlock[1]);
            column.append(tmpBlock[2]);
            column.append(tmpBlock[3]);
            column.append(tmpBlock[4]);

            //zamiana indeksów na dziesietny
            int rowDec = Integer.parseInt(String.valueOf(row), 2);
            int columnDec = Integer.parseInt(String.valueOf(column), 2);


            String bit = Integer.toBinaryString(SBlock[rowDec + 4 * i][columnDec]);  // pobranie bitu z SBLock

            StringBuilder binaryBit = completeTo4(bit); //dopełnienie


            for (int j = 0; j < 4; j++) {
                result[j + 4 * i] = Character.getNumericValue(binaryBit.charAt(j));
            }


        }
        System.arraycopy(makePermutation(result, PBlockP, 32), 0, result, 0, 32);//permutacja


        return result;


    }

    //algorytm des, pobiera słowo, klucz oraz encode który odpowiada za to czy szyfrujemy czy odszyfrowujemy wiadomość
    private StringBuilder des(int[] word, int[] key, boolean encode) {

        //inicjująca permutacja
        int[] data = makePermutation(word, initialP, 64);

        // podzielenie na dwie części- lewą i prawą
        int[] leftData = getLeftPart(data);
        int[] rightData = getRightPart(data);

        //key PC-1 permutation
        key = makePermutation(key, keyPC_1, 56);
        prepareSubkeys(key);


        //Feistel operations

        for (int i = 1; i <= 16; i++) {

            int[] tmpKey = new int[48];

            if (encode)
                System.arraycopy(subkeys[i - 1], 0, tmpKey, 0, 48);
            else
                System.arraycopy(subkeys[16 - i], 0, tmpKey, 0, 48);

            //rozszerzająca permutacja
            int[] expandedR = new int[48];
            expandedR = makePermutation(rightData, expandingP, 48);
            int[] newR = new int[48];
            System.arraycopy(expandedR, 0, newR, 0, 48);


            newR = xorTables(expandedR, tmpKey);


            System.arraycopy(transformBySBlocks(newR), 0, newR, 0, 32);


            int[] newRightData = xorTables(leftData, newR);

            //zamiana stron
            leftData = rightData;
            rightData = newRightData;


        }

        //połączenie dwóch części
        System.arraycopy(rightData, 0, data, 0, 32);
        System.arraycopy(leftData, 0, data, 32, 32);


        data = Arrays.copyOf(makePermutation(data, finalP, 64), 64);
        return displayBits(data);


    }
    static String stringToHex(String string) {
        StringBuilder buf = new StringBuilder(200);
        for (char ch: string.toCharArray()) {
            buf.append(String.format("%04x", (int) ch));
        }
        return buf.toString();
    }
    public static String convertHexToStringValue(String hex) {
        StringBuilder stringbuilder = new StringBuilder();
        char[] hexData = hex.toCharArray();
        for (int count = 0; count < hexData.length - 1; count += 2) {
            int firstDigit = Character.digit(hexData[count], 16);
            int lastDigit = Character.digit(hexData[count + 1], 16);
            int decimal = firstDigit * 16 + lastDigit;
            stringbuilder.append((char)decimal);
        }
        return stringbuilder.toString();
    }




}
