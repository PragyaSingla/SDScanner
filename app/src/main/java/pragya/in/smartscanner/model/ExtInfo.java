package pragya.in.smartscanner.model;
/**
 * Created by Pragya on 02-05-2018.
 */
public class ExtInfo implements Comparable<ExtInfo> {
    private final int frequence;
    private final String name;

    public ExtInfo(String name, int frequence) {
        this.name = name;
        this.frequence = frequence;
    }

    public String getName() {
        return this.name;
    }

    public int getFrequence() {
        return this.frequence;
    }

    public int compareTo(ExtInfo another) {
        return another.getFrequence() - this.frequence;
    }

    public String toString() {
        return String.format("%s - %d", this.name, this.frequence);
    }
}
