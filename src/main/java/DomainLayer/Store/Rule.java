package DomainLayer.Store;

public interface Rule<T, U> {
    public boolean checkRule(T user, U products);
}
