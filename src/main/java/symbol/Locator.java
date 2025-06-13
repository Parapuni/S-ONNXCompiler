package symbol;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Locator {
    public HashMap<String, String> inLocator;
    public HashMap<String, String> outLocator;

    public Locator() {
        this.inLocator = new HashMap<>();
        this.outLocator = new HashMap<>();
    }

    public void addLocation(String symbolicName, String scope, String location) {
        if("input".equals(scope))
            inLocator.put(symbolicName, location);
        else
            outLocator.put(symbolicName, location);
    }

    public boolean contains(String symbolicName, String scope) {
        if("input".equals(scope))
            return inLocator.containsKey(symbolicName);
        else if("output".equals(scope))
            return outLocator.containsKey(symbolicName);
        else
            return false;
    }

    public Set<Map.Entry<String, String>> entry(String scope){
        if("input".equals(scope))
            return inLocator.entrySet();
        else if("output".equals(scope))
            return outLocator.entrySet();
        else
            return null;
    }
}
