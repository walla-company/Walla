package genieus.com.walla.v2.info;

/**
 * Created by anesu on 12/27/16.
 */

public class DomainInfo {
    private String name, code;

    public DomainInfo(String code, String name){
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString(){
        return String.format("%s(%s)", code, name);
    }
}
