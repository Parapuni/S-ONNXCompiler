package symbol.throwable;

public class NameConflictException extends Exception {
    public String locationPre;
    public String locationNow;
    public String symbolicName;

    public NameConflictException(String locationPre, String locationNow, String symbolicName) {
        super("Name conflict at " + locationNow + ":\n\tsymbol " + symbolicName + " already defined at " + locationPre);
        this.locationPre = locationPre;
        this.locationNow = locationNow;
        this.symbolicName = symbolicName;
    }
}
