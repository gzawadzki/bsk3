package sample;

import java.util.Arrays;
import static sample.desTables.*;

public class desAlgorithm {

    private int[] keyMoves = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private int[][] subkeys = new int[16][48];

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
     przesunięcia w lewo, każdy bit jest przesuwany w lewo,
     najstarszy bit (pierwszy od lewej) przesuwany jest
     na ostatnie miejsce(skrajnie po prawej)
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
     * Metoda która dopełnia do 4 bitów liczby zamienione na bity wzięte z tabel Sn.
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


}
