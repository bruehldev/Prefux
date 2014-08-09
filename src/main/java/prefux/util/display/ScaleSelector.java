/*  
 * Copyright (c) 2004-2013 Regents of the University of California.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3.  Neither the name of the University nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * Copyright (c) 2014 Martin Stockhammer
 */
package prefux.util.display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Swing widget which displays a preview image and helps select the
 * scale for an exported image.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ScaleSelector extends JComponent implements ChangeListener {

    private final static int MAX_SIZE = 135;
    
    private ImagePanel preview;
    private JLabel     value;
    private JLabel     size;
    private JSlider    slider;
    private Image      image;
    private int width, height;
    
    /**
     * Create a new ScaleSelector.
     */
    public ScaleSelector() {
        slider = new JSlider(1,10,1);
        value  = new JLabel("x1");
        size = new JLabel("   ");
        preview = new ImagePanel();
        
        value.setPreferredSize(new Dimension(25,10));
        size.setHorizontalAlignment(JLabel.CENTER);
        slider.setMajorTickSpacing(1);
        slider.setSnapToTicks(true);
        slider.addChangeListener(this);
        
        setLayout(new BorderLayout());
        
        Box b1 = new Box(BoxLayout.X_AXIS);
        b1.add(Box.createHorizontalStrut(5));
        b1.add(Box.createHorizontalGlue());
        b1.add(preview);
        b1.add(Box.createHorizontalGlue());
        b1.add(Box.createHorizontalStrut(5));
        add(b1, BorderLayout.CENTER);
        
        Box b2 = new Box(BoxLayout.X_AXIS);
        b2.add(slider);
        b2.add(Box.createHorizontalStrut(5));
        b2.add(value);
        
        Box b3 = new Box(BoxLayout.X_AXIS);
        b3.add(Box.createHorizontalStrut(5));
        b3.add(Box.createHorizontalGlue());
        b3.add(size);
        b3.add(Box.createHorizontalGlue());
        b3.add(Box.createHorizontalStrut(5));
        
        Box b4 = new Box(BoxLayout.Y_AXIS);
        b4.add(b2);
        b4.add(b3);
        add(b4, BorderLayout.SOUTH);
    }

    /**
     * Set the preview image.
     * @param img the preview image
     */
    public void setImage(Image img) {
        image = getScaledImage(img);
        stateChanged(null);
    }
    
    /**
     * Get a scaled version of the input image.
     * @param img the input image
     * @return a scaled version of the image
     */
    private Image getScaledImage(Image img) {
        int w = width = img.getWidth(null);
        int h = height = img.getHeight(null);
        double ar = ((double)w)/h;
        
        int nw = MAX_SIZE, nh = MAX_SIZE;
        if ( w > h ) {
            nh = (int)Math.round(nw/ar);
        } else {
            nw = (int)Math.round(nh*ar);
        }
        return img.getScaledInstance(nw,nh,Image.SCALE_SMOOTH);
    }
    
    /**
     * Monitor changes to the scale slider.
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent evt) {
        int scale = slider.getValue();
        value.setText("x"+String.valueOf(scale));
        size.setText("Image Size: "+(width*scale)+" x "+(height*scale)+" pixels");
        preview.repaint();
    }
    
    /**
     * Get the current image scale reported by the slider.
     * @return the image scale to use
     */
    public double getScale() {
        return slider.getValue();
    }
    
    /**
     * Swing component that draws an image scaled to the current
     * scale factor.
     */
    public class ImagePanel extends JComponent {
        Dimension d = new Dimension(MAX_SIZE, MAX_SIZE);
        public ImagePanel() {
            this.setPreferredSize(d);
            this.setMinimumSize(d);
            this.setMaximumSize(d);
        }
        public void paintComponent(Graphics g) {
            double scale = 0.4+(0.06*getScale());
            int w = (int)Math.round(scale*image.getWidth(null));
            int h = (int)Math.round(scale*image.getHeight(null));
            Image img = (scale == 1.0 ? image : 
                image.getScaledInstance(w,h,Image.SCALE_DEFAULT));
            int x = (MAX_SIZE-w)/2;
            int y = (MAX_SIZE-h)/2;
            g.drawImage(img,x,y,null);
        }
    }
    
} // end of class ScaleSelector
