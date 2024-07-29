/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.invoicingapp.tools;

import java.io.InputStream;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author frede
 */
public class ButtonFeatures {
    
    private static final int imgSize=100;
    
    public static void makeButtonFocusable(Button button,String title,String pathImgUnfocused,String pathImgFocused){
        setImageButton(button, pathImgUnfocused);
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                switchState(button,title,pathImgFocused);
            });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                switchState(button,"",pathImgUnfocused);
            });
    }
    
    private static void switchState(Button button,String title,String pathImg){
        setImageButton(button, pathImg);
        button.setText(title);
    }
    
    private static void setImageButton(Button button, String path){
        InputStream inputStream=ButtonFeatures.class.getResourceAsStream(path);
        Image img=new Image(inputStream, imgSize, imgSize, true, true);
        button.setGraphic(new ImageView(img));
    }
    
    
    
}
