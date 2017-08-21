package genieus.com.walla.v2.datatypes;

/**
 * Created by Anesu on 12/13/2016.
 */
public class Interest {

    private String name;
    private int img;
    private boolean selected;

    public Interest(String name, int img, boolean selected){
        this.name = name;
        this.img = img;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public int getImg() {
        return img;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
