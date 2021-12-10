package pl.edu.pw.ee;

public class HashDoubleHashing<T extends Comparable<T>> extends HashOpenAddressing<T> {

    public HashDoubleHashing() {
        super();
    }

    public HashDoubleHashing(int size) {
        super(size);
    }

    @Override
    int hashFunc(int key, int i) {
        int m = getSize();

        if(m == 0) throw new IllegalArgumentException("You couldn't get a hash if size is zero");

        int hashFuncF = key % m;
        int hashFuncG = 1 + key % (m - 3);
        int hash = (hashFuncF + i * hashFuncG) % m;

        hash = hash < 0 ? -hash : hash;

        return hash;
    }



}
