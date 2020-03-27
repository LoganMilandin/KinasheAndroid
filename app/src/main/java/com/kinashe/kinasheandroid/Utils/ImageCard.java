package com.kinashe.kinasheandroid.Utils;

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
