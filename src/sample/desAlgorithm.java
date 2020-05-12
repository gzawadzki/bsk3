package sample;

public class desAlgorithm {


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
}
