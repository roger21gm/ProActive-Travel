/**
 * @class Pair
 * @brief És un parell d'element.
 * @param <A> És el primer element.
 * @param <B> És el segon element.
 */
public class Pair<A, B> {
    public A primer; ///< Primer element del parell.
    public B segon; ///< Segon element del parell.

    /**
     * @pre cert
     * @post Crea un nou Pair de dos elements.
     * @param primer Primer element.
     * @param segon Segon element.
     */
    public Pair(A primer, B segon) {
        this.primer = primer;
        this.segon = segon;
    }


    public int hashCode() {
        int hashFirst = primer != null ? primer.hashCode() : 0;
        int hashSecond = segon != null ? segon.hashCode() : 0;
        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair otherPair = (Pair) other;
            return
                    ((  this.primer == otherPair.primer ||
                            ( this.primer != null && otherPair.primer != null &&
                                    this.primer.equals(otherPair.primer))) &&
                            (  this.segon == otherPair.segon ||
                                    ( this.segon != null && otherPair.segon != null &&
                                            this.segon.equals(otherPair.segon))) );
        }

        return false;
    }
}