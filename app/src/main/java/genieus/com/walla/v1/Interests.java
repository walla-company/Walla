package genieus.com.walla.v1;

/**
 * Created by Anesu on 9/3/2016.
 */
public class Interests {
    private int img;
    private String name;

    public Interests(String name, int img){
        this.name = name;
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}