package pl.edu.pw.ee;

public class HashQuadraticProbing<T extends Comparable<T>> extends HashOpenAddressing<T> {
    private final double a;
    private final double b;

    public HashQuadraticProbing() {
        super();
        a = Math.sqrt(5);
        b = Math.sqrt(2);
    }

    public HashQuadraticProbing(int size, double a, double b) {
        super(size);
        this.a = a;
        this.b = b;
    }

    @Override
    int hashFunc(int key, int i) {
        int m = getSize();
        int hash = ((int) (key + a*i + b*i*i)) % m;
        hash = hash < 0 ? -hash : hash;
        return hash;
    }
}
