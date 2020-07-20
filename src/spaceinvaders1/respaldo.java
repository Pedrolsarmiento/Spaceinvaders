/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Timer;

/**
 *
 * @author pedro
 */
public class respaldo {/*
    private MouseListener dispararJugador = new MouseListener() {
        Timer m;
        Disparo d;
        int contadorDisparo = 0;
        @Override
        public void mouseClicked(MouseEvent e) {
            coordenada c = new coordenada(200, 400);
            d = new Disparo();
            d.setBounds(c.x-(d.getWidth()/2), c.y, d.getWidth(), d.getHeight());
            contenedor.add(d);
            //Timer m;
            contenedor.setComponentZOrder(d,contadorDisparo);
            contenedor.setComponentZOrder(espacioEnemigos, contenedor.getComponentCount()-1);
            contadorDisparo++;
            ActionListener moverDisparo = new ActionListener() {
                int pos = d.getY();
                boolean impacto=false;
                @Override
                public void actionPerformed(ActionEvent e) {
                    //int i=0;int j=0;
                    pos -= 1;
                    d.setBounds(d.getX(), pos, d.getWidth(), d.getHeight());
                    d.actualizaArea();
                    if(espacioEnemigos.hayImpacto(d.getArea())){
                        d.setVisible(false);
                        contenedor.remove(d);
                        d=null;
                        m.stop();
                        
                    }
                }
            };
            m = new Timer(10,moverDisparo);
            m.start();
            
        }

        
        

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    };*/
    
    
    
    /***************************N2
     * private MouseListener dispararJugador = new MouseListener() {
        //Timer m;
        //Disparo d;
        int contadorDisparo = 0;
        @Override
        public void mouseClicked(MouseEvent e) {
            coordenada c = new coordenada(200, 400);
            Disparo d = new Disparo();
            d.setBackground(Color.green);
            d.setBounds(c.x-(d.getWidth()/2), c.y, d.getWidth(), d.getHeight());
            contenedor.add(d);
            final Timer m = new Timer(10,new ActionListener() {
                int pos = d.getY();
                boolean impacto=false;
                @Override
                public void actionPerformed(ActionEvent e) {
                    //int i=0;int j=0;
                    pos -= 3;
                    d.setBounds(d.getX(), pos, d.getWidth(), d.getHeight());
                    d.actualizaArea();
                    if(espacioEnemigos.hayImpacto(d.getArea())){
                        d.setVisible(false);
                        contenedor.remove(d); 
                        pos=-1;
                        
                    }
                }
            });
            contenedor.setComponentZOrder(d,contadorDisparo);
            contenedor.setComponentZOrder(espacioEnemigos, contenedor.getComponentCount()-1);
            contadorDisparo++;
            
            m.start();
            
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    };

     * 
     * 
     */
}
