package pl.edu.pw.ee.tables;

import org.junit.Test;
import pl.edu.pw.ee.HashQuadraticProbing;
import pl.edu.pw.ee.services.HashTable;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.fail;

public class HashQuadraticProbingTable {
    @Test
    public void createTableForHash() {
        List<String> words = initializeWordsList();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("quadraticTable.txt"))) {
            bw.write(String.format("%-3s %-12s %-42s %-82s\n", "№", "Początkowy rozmiar hasza", "Średni czas wstawiania 100000 elementów", "Średni czas wyszukania 100000 elementów"));
            int hashSize = 512;
            double[] aValues = new double[10];
            double[] bValues = new double[10];

            for(int k = 0; k < 10; k++){
                aValues[k] = Math.sqrt(k+0.5);
                bValues[k] = Math.sqrt(k + 1);
            }
            bw.write("\t\t\t");
            for(int j = 0; j < 2; j++){
                for(int i = 0; i < 10; i++){
                    bw.write(String.format("a=%.2f ",aValues[i]));
                }
            }
            bw.write("\n");
            bw.write("\t\t\t");
            for(int j = 0; j < 2; j++){
                for(int i = 0; i < 10; i++){
                    bw.write(String.format("b=%.2f ",bValues[i]));
                }
            }
            bw.write("\n");


            for (int i = 0; i < 10; i++) {
                List<Double> rowPutTimeList = new ArrayList<>(10);
                List<Double> rowGetTimeList = new ArrayList<>(10);

                for(int k = 0 ; k < 10; k++){
                    double a = aValues[k];
                    double b = bValues[k];

                    List<Long> tmpPutTimeArray = new ArrayList<>();
                    List<Long> tmpGetTimeArray = new ArrayList<>();

                    for(int j = 0; j < 30; j++){
                        HashTable<String> hashTable = getInstance(hashSize, a, b);

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

                        tmpPutTimeArray.add(putTime);
                        tmpGetTimeArray.add(getTime);
                    }

                    double putTime = calcAverage(tmpPutTimeArray.subList(10, 20));
                    double getTime = calcAverage(tmpGetTimeArray.subList(10, 20));

                    rowPutTimeList.add(k, putTime);
                    rowGetTimeList.add(k, getTime);
                }
                StringBuilder sb = new StringBuilder();
                sb.append(i).append("   ").append(hashSize).append("   ");
                for (Double item : rowPutTimeList) {
                    sb.append(String.format("%.2f  ", item));
                }
                for (Double item : rowGetTimeList) {
                    sb.append(String.format("%.2f  ", item));
                }
                sb.append("\n");
                bw.write(sb.toString());
                hashSize*=2;
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private HashTable<String> getInstance(int size, double a, double b) {
        return new HashQuadraticProbing<>(size, a, b);
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
