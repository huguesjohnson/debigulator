/*
SplashScreen.java - simple splash screen
Copyright  (C) 2000-2007 Hugues Johnson

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software 
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/

package com.huguesjohnson.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

/** 
 * SplashScreen loads a borderless window containing a single image.
 * Useful for displaying a splash screen while a program is loading.
 * @author Hugues Johnson
 */
public class SplashScreen extends Window{
	/* Determines if a de-serialized file is compatible with this class. See <a href="http://java.sun.com/j2se/1.5.0/docs/guide/serialization/spec/class.html">Java Object Serialization Specifications</a>. */
	private static final long serialVersionUID=1886110606212664845L;
	/* the image to display */
     private Image splashImage;
     
     /** 
      * Constructor for SplashSceen creates a borderless window and paints a specified image.
      * @param parent The parent window.
      * @param splashImagePath The full path of the image to load.
      */
     public SplashScreen(Frame parent,String splashImagePath){
          super(parent);
          /* load splash image */
          this.splashImage=Toolkit.getDefaultToolkit().getImage(splashImagePath);
          this.waitForImage();
          this.resizeAndShow();
     }

     /** 
      * Constructor for SplashSceen creates a borderless window and paints a specified image.
      * @param parent The parent window.
      * @param splashImage The Image to use.
      */
     public SplashScreen(Frame parent,Image splashImage){
          super(parent);
          /* load splash image */
          this.splashImage=splashImage;
          this.waitForImage();
          this.resizeAndShow();
     }
     
     /* resize the frame to fit the image and show */
     private void resizeAndShow(){
         /* set the size of the window to the size of the image */
         int height=this.splashImage.getHeight(this);
         int width=this.splashImage.getWidth(this);
         this.setSize(width,height);
         /* center the window */
         Dimension screenDimension=Toolkit.getDefaultToolkit().getScreenSize();
         Rectangle windowDimension=this.getBounds();
         this.setLocation((screenDimension.width-windowDimension.width)/2,(screenDimension.height-windowDimension.height)/2);
         /* show the window */
         this.setVisible(true);
     }
     
     /* wait for image to load */
     private void waitForImage(){
         /* use MediaTracker to make sure image loads before trying to display */
         MediaTracker mediaTracker=new MediaTracker(this);
         /* add image to media tracker */
         mediaTracker.addImage(this.splashImage,0);
         /* wait for image to load */
         try{
              mediaTracker.waitForID(0);
         } catch(InterruptedException x){
              x.printStackTrace();
         }
     }
     
     /** 
      * Draws the image passed to the constructor onto the splash window.
      * @param g The Graphics object to paint.
      */
     public void paint(Graphics g){
          if(this.splashImage!=null){
               g.drawImage(this.splashImage,0,0,this);
          }
     }
}