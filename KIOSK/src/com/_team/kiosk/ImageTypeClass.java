package com._team.kiosk;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageTypeClass {
    @SuppressWarnings("unused")
	public static void main(String[] args) {
        boolean isok = isImage("images/here.jpg");
    }
    
    //이미지 깨짐여부 확인
    public static boolean isImage(String filepath){
        boolean result = false;
        File f = new File(filepath);
        try {
            BufferedImage buf = ImageIO.read(f);
            if(buf == null){
                result = false;
            } else {
                result = true;	
            }
        } catch (Exception e) {
            e.printStackTrace();
        }		
        return result;
    }
}