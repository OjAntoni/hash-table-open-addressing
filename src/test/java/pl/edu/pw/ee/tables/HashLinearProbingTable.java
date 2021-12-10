package pl.edu.pw.ee.tables;

import org.junit.Test;
import pl.edu.pw.ee.HashLinearProbing;
import pl.edu.pw.ee.services.HashTable;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class HashLinearProbingTable {

    @Test
    public void createTableForHash() {
        List<String> words = initializeWordsList();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("linearTable.txt"))) {
            bw.write(String.format("%-3s %-22s %-11s %s\n", "№", "Początkowy rozmiar hasza", "Średni czas wstawiania 100000 elementów", "Średni czas wyszukania 100000 elementów"));
            int hashSize = 512;
            for (int i = 0; i < 10; i++) {
                List<Long> putTimeArray = new ArrayList<>();
                List<Long> getTimeArray = new ArrayList<>();

                for(int j = 0; j < 30; j++){
                    HashTable<String> hashTable = getInstance(hashSize);

                    long start1 = System.nanoTime();
                    for (String word : words) {
                        hashTable.put(word);
                    }
                    long finish1 = System.nanoTime();
                    long putTime = finish1 - start1;

                    long start2 = System.nanoTime();
                    for (String word : words) {
                        hashTable.get(word);
                    }
                    long finish2 = System.nanoTime();
                    long getTime = finish2 - start2;

                    putTimeArray.add(putTime);
                    getTimeArray.add(getTime);
                }

                double putTime = calcAverage(putTimeArray.subList(10, 20));
                double getTime = calcAverage(getTimeArray.subList(10, 20));

                bw.write(String.format("%-3s %-25s %-38.0f %.0f\n", i+1, hashSize, putTime, getTime));

                hashSize*=2;
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private HashTable<String> getInstance(int size) {
        return new HashLinearProbing<>(size);
    }

    private List<String> initializeWordsList() {
        List<String> words = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new FileReader("words.txt"))) {
            String word;
            while ((word = bf.readLine()) != null) {
                words.add(word);
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return words;
    }

    private double calcAverage(Collection<Long> collection){
        double sum = 0;
        for (long value : collection){
            sum += value;
        }
        return sum/collection.size();
    }

}
