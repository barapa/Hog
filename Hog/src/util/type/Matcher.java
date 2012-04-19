package util.type;

import util.type.Types.Type;



public interface Matcher<T> {
    T instantiate(T matched);

    boolean visit(Type t);
   
    //Name name();
    String signature();

    /** Key used for cache lookups, or null if cannot be cached. */
    Object key();
}