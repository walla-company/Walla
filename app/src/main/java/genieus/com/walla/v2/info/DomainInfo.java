package genieus.com.walla.v2.info;

/**
 * Created by anesu on 12/27/16.
 */

public class DomainInfo {
    private String name, domain, fullname;

    public DomainInfo(String name, String domain, String fullname){
        this.name = name;
        this.domain = domain;
        this.fullname = fullname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString(){
        return String.format("%s(%s)", name, fullname);
    }
}
