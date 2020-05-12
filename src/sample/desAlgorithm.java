package sample;

public class desAlgorithm {

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
     * @param block -> blok do permutacji
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
}
