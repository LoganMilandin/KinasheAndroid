package com.kinashe.kinasheandroid.Utils;

/**
 * not really sure this class is super necessary but Avery made it so
 * we'll keep it around lol
 */
public class ImageCard {

    private String Text;
    private int Image;

    public ImageCard(String text, int image) {
        Text = text;
        Image = image;
    }

    public String getText() {
        return Text;
    }

    public int getImage() {
        return Image;
    }
}
