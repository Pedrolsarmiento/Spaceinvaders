package spaceinvaders1;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EventListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class SpaceInvaders1 {
    private JFrame ventana;
    private JPanel contenedor;
    private ContenedorEnemigo espacioEnemigos;
    private Nave jugador = new Nave();
    //private Scanner entrada;
    private Timer movimientoJugador;
    private Timer movimientoEnemigo;
    private Timer disparosEnemigos;
    private JLabel tiempo;
    private Timer tiempoDeJuego;
    private int segundos = 0;
    private int minutos = 0;
    public static Font f = new Font(Font.SANS_SERIF, Font.BOLD, 18);
    
    private ActionListener moverEnemigos = new ActionListener() {//Listener para mover enemigos
            int posicion = 0;
            boolean cambioDireccion = true;
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cambioDireccion){//se utiliza para saber cuando llega al final de la ventana
                    posicion +=2;
                    cambioDireccion = (espacioEnemigos.getX()+espacioEnemigos.getWidth())<contenedor.getWidth();
                }else{
                    posicion -=2;
                    cambioDireccion = espacioEnemigos.getX()==0;
                }
                espacioEnemigos.setBounds(posicion, espacioEnemigos.getY(), espacioEnemigos.getWidth(), espacioEnemigos.getHeight());
                espacioEnemigos.actualizaArea();//actualiza el area de las naves para el impacto con el disparo
            }
        };
    
    private ActionListener dispararEnemigos = new ActionListener() {//listener para crear los disparos del enemigo
        int contadorDisparo = 0;
        @Override
        public void actionPerformed(ActionEvent e) {
            coordenada c = espacioEnemigos.posicionDisparo();//coordenada pasa el punto justo debajo de los enemigos donde debe salir el disparo
            if(c!=null){//cuando no quedan enemigos en una columna, el metodo "posicionDisparo" envia coordenadas nulas.
                Disparo d = new Disparo();
                d.setBounds(c.x-(d.getWidth()/2), c.y, d.getWidth(), d.getHeight());

                ActionListener moverDisparo = new ActionListener() {
                    int pos = d.getY();
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        pos += 3;
                        d.setBounds(d.getX(), pos, d.getWidth(), d.getHeight());
                        d.actualizaArea();//actualiza el area de cada disparo para saber si hay colision
                        if(d.getY()>ventana.getHeight()){
                            ((Timer)e.getSource()).stop();
                            contadorDisparo--;
                            contenedor.remove(d);
                        }
                        
                        jugador.actualizaArea();//actualiza el area del jugador para saber si hay colision
                            if(jugador.getArea().seTocan(d.getArea())){//si se toca el area del jugador con el disparo
                                //jugador = null; el programa explota aqui
                                jugador.setVisible(false);
                                jugador.setBounds(0, 0, 0, 0);
                                contenedor.remove(jugador);
                                
                                
                                JFrame v = new JFrame("perderdor");//ventana de si se pierde
                                v.setBounds(0, 0, 400, 300);
                                JPanel c = new JPanel();
                                c.setLayout(null);
                                JButton nuevoJuego = new JButton("Nuevo Juego?");
                                nuevoJuego.setBounds(130, 160, 120, 30);
                                JButton salir = new JButton("Salir :(");
                                salir.setBounds(150, 200, 80, 30);
                                salir.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {//salir

                                        System.exit(0);
                                    }
                                });
                                nuevoJuego.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {//nuevo juego
                                    v.setVisible(false);
                                    v.setVisible(false);
                                    ventana.setVisible(false);
                                    ventana.dispose();//elimina la ventana anterior
                                    ventana.removeAll();
                                    SpaceInvaders1 j = new SpaceInvaders1();//crea un nuevo juego

                                }
                            });
                                JLabel perdedor = new JLabel("PERDEDOR");
                                perdedor.setBounds(50, 0, 400, 100);
                                perdedor.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
                                c.add(perdedor);
                                c.add(nuevoJuego);
                                c.add(salir);
                                v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                v.add(c);
                                v.setLocationRelativeTo(null);
                                v.setVisible(true);

                            }
                    }
                };

                contenedor.add(d);
                contenedor.setComponentZOrder(d,contadorDisparo);
                contenedor.setComponentZOrder(espacioEnemigos, contenedor.getComponentCount()-1);
                contadorDisparo++;
                Timer m = new Timer(10,moverDisparo);//el timer encargado de mover los disparos enemigos
                m.start();
                /* intentar parar el timer pero no funciona
                while(m.isRunning()){
                    if(d.getY()>ventana.getHeight())
                        m.stop();
                }*/
            }
        }
    };
    
    
        private KeyListener entrada = new KeyListener() {//las teclas del jugador
            int posx = jugador.getX();
            int posy = jugador.getY();
            boolean derecha = false;
            boolean izquierda = false;
            boolean espacio = false;

            ActionListener moverIzquierda = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(posx>10){
                        posx-=8;
                    }
                    jugador.setBounds(posx, jugador.getY(), jugador.getAcho(), jugador.getAlto());
                    jugador.actualizaArea();                }
                };
            ActionListener moverDerecha = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(posx<ventana.getWidth()-jugador.getAcho()-10){
                        posx+=8;
                    }
                    jugador.setBounds(posx, jugador.getY(), jugador.getAcho(), jugador.getAlto());
                    jugador.actualizaArea();
                }
            };

            public Timer izq = new Timer(20,moverIzquierda);
            public Timer der = new Timer(20,moverDerecha);
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 37:
                        izquierda = true;
                        break;
                    case 39:
                        derecha = true;
                        break;
                    case 32:
                        espacio = true;
                        break;
                    default:
                        break;
                }
                
                //System.out.println(e.getKeyCode());
                if(izquierda) {//tecla izquierda es 37
                    izq.start();
                }
                if(derecha) {//tecla derecha es 39
                    der.start();
                }
                
                
                if(espacio) {//tecla 32 es espacio
                    int contadorDisparo = 0;
        
                    coordenada c = new coordenada(jugador.getX()+jugador.getWidth()/2, jugador.getY());//ubica la coordenada de salida del disparo del jugador
                    Disparo d = new Disparo();
                    d.setBackground(Color.green);
                    d.setBounds(c.x-(d.getWidth()/2), c.y, d.getWidth(), d.getHeight());
                    contenedor.add(d);
                    final Timer m;
                    m = new Timer(10, new ActionListener() {//timer que mueve el disparo hacia arriba
                        int pos = d.getY();
                        boolean impacto=false;
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //int i=0;int j=0;
                            pos -= 3;
                            d.setBounds(d.getX(), pos, d.getWidth(), d.getHeight());
                            d.actualizaArea();
                            if(espacioEnemigos.hayImpacto(d.getArea())){//si hay colision entre el disparo y las naves enemigas
                                //d.setVisible(false);
                                //d=null; //no me deja eliminar un disparo cuando hay colision
                                ((Timer)e.getSource()).stop();
                                contenedor.remove(d);
                                //m.stop();//no me deja terminar el evento y hacer que deje de mover el disparo
                                //pos=-1;
                                
                                
                                //*****************
                                //los disparos que destruyen a un enemigo, no se eliminan ni se detienen; solo se posicionan fuera de la ventana para que suban infinitamente!!!!!!!
                                //*****************
                            }
                         }
                    });
                    contenedor.setComponentZOrder(d,contadorDisparo);//para que el disparo pueda pasar encima del contenedor enemigo y se vea
                    contenedor.setComponentZOrder(espacioEnemigos, contenedor.getComponentCount()-1);
                    contadorDisparo++;

                    m.start();
                }
            }
            
            
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 37:
                        izquierda = false;
                        izq.stop();
                        break;
                    case 39:
                        derecha = false;
                        der.stop();
                        break;
                    case 32:
                        espacio = false;
                        break;
                    default:
                        break;
                }
            }
                        
    };

    private ActionListener cronometro = new ActionListener() {//listener para ir midiendo el tiempo de juego
        @Override
        public void actionPerformed(ActionEvent e) {
            if(segundos<60){
                segundos++;
            }else{
                segundos = 0;
                minutos++;
            }
            if(segundos<10)
                tiempo.setText("Tiempo: "+minutos+":0"+segundos);
            else
                tiempo.setText("Tiempo: "+minutos+":"+segundos);
        }
    };
    
    public SpaceInvaders1(){//esto hace todo
        ventana = new JFrame("Space Invaders 1");
        ventana.setBounds(0, 0, 600, 700);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        contenedor = new JPanel();
        contenedor.setLayout(null);//para ubicar libremente los enemigos,disparos y jugador
        contenedor.setBackground(Color.BLACK);
        espacioEnemigos = new ContenedorEnemigo();
        movimientoEnemigo = new Timer(20,moverEnemigos);//se activa el movimiento de enemigos
        movimientoEnemigo.start();
        jugador = new Nave();
        //this.movimientoJugador = new Timer(10,accionJugador);
        disparosEnemigos = new Timer(120 ,dispararEnemigos);//el randome para disparar de los enemigos
        disparosEnemigos.start();
        tiempo = new JLabel("Tiempo: "+minutos+":0"+segundos);
        tiempo.setBounds(420, 30, 150, 50);
        tiempo.setFont(f);
        tiempo.setForeground(Color.WHITE);
        contenedor.add(tiempo);
        tiempoDeJuego = new Timer(1000,cronometro);//esto cronometra el tiempo
        tiempoDeJuego.start();
        ventana.addKeyListener(entrada);
        espacioEnemigos.addContainerListener(new ContainerListener() {//esto verifica cuando no quedan enemigos vivos (ganar)
            String mejorTiempo;
            @Override
            public void componentAdded(ContainerEvent e) {
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                
                movimientoEnemigo.stop();
                disparosEnemigos.stop();
                tiempoDeJuego.stop();
                
                ventana.setEnabled(false);
                if(!espacioEnemigos.hayEnemigos()){//verifica que no hayan enemigos y sale ventana de victoria
                    JFrame v = new JFrame();
                    v.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    v.setBounds(0, 0, 400, 140);
                    v.setLocationRelativeTo(null);
                    JPanel despues = new JPanel();
                    JLabel felicidad = new JLabel("Felicidades!\n Escribe tu nombre:");
                    felicidad.setFont(f);
                    despues.add(felicidad);
                    JButton boton = new JButton("Aceptar");
                    JTextField texto = new JTextField("", 20);
                    boton.addActionListener(new ActionListener() {//al hacer click en aceptar, toma tu nombre y lo guarda en un fichero con tu tiempo
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String nombre = texto.getText();
                            if(nombre.equals(""))
                                nombre="cacahuete";//si no pones nombre, eres un cacahuete
                            
                            Record record = new Record(nombre+"--->"+minutos+":"+((segundos<10)?"0":"")+segundos);//llama a la ventana record con la informacion de victoria
                            nombre = nombre+"("+minutos+":"+((segundos<10)?"0":"")+segundos;//la informacion a guardar en el fichero
                            try {
                                FileWriter escritor = new FileWriter("../SpaceInvaders1/src/record.txt",false);//guarda la informacion en el fichero
                                for(int i=0;i<nombre.length();i++){
                                    escritor.write(nombre.charAt(i));
                                }
                                escritor.close();
                            } catch (IOException ex) {
                                
                            }
                            record.nuevoJuego.addActionListener(new ActionListener() {//para comenzar un juego nuevo
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    v.setVisible(false);
                                    record.setVisible(false);
                                    ventana.setVisible(false);
                                    ventana.dispose(); //se usa para liberar los recursos que uso el juego anterior
                                    ventana.removeAll();
                                    SpaceInvaders1 j = new SpaceInvaders1();//se crea un nuevo juego

                                }
                            });
                        }
                    });
                despues.add(texto);
                despues.add(boton);
                v.add(despues);
                v.setVisible(true);
                }
            }
        });
        contenedor.add(espacioEnemigos);
        contenedor.add(jugador);
        ventana.add(contenedor);
        ventana.setVisible(true);
        //fin de constructor
    }

    
    public static void main(String[] args) {
        SpaceInvaders1 juego = new SpaceInvaders1();//para arrancar el juego
               
    }
    
}

class ContenedorEnemigo extends JPanel{
    int filas = 3;//filas de enemigos
    int columnas = 8 ;//columnas de enemigos
    private Enemigo [][] enemigos = new Enemigo[filas][columnas];//tabla para posicion de enemigos
    private Enemigo [] paraDisparar = new Enemigo[columnas];//array para saber cuales pueden disparar (que no tengan enemigos delante)
    private Area posicion = new Area(0,0,0,0);//se utiliza para calcular el impacto con los disparos
    
    public ContenedorEnemigo(){
        super();
        this.setLayout(new GridLayout(filas,columnas));
        this.setBounds(0, 80, columnas*60, filas*50);
        this.setBackground(Color.BLACK);
        this.actualizaPosicion();
        //this.setBorder(BorderFactory.createLineBorder(Color.white));
        
        for(int i=0 ; i<enemigos.length ; i++){//este for lo llena de enemigos
            for(int j=0 ; j<enemigos[i].length ; j++){
                if(i==enemigos.length-1){
                    enemigos[i][j]= new Enemigo(true,i,j);
                    paraDisparar[j] = enemigos[i][j];
                }else{
                    enemigos[i][j]= new Enemigo(false,i,j);
                }
                this.add(enemigos[i][j]);
            }
        }
    }  
    
    public Enemigo[][] getEnemigos(){
        return enemigos;
    }

    public void actualizaPosicion(){
        posicion.setArea(this.getX(),(this.getX()+this.getWidth()),this.getY(),(this.getY()+this.getHeight()));//actualiza la posicion solamente del contenedor
    }
    
    public coordenada posicionDisparo(){//devuelve la posicion de los enemgios incluyendo la posicion del contenedor
        int x=0,y=0,a=(int)(Math.random()*columnas);
                    coordenada c = new coordenada(x,y);
        try{
            x = this.getX() + this.paraDisparar[a].getX() + (this.paraDisparar[a].getWidth()/2);
            y = this.getY() + this.paraDisparar[a].getY() + this.paraDisparar[a].getHeight();
            c.x=x;
            c.y=y;
        }catch(NullPointerException e){
            c = null;
        }
        return c;
    }

    public void actualizaArea() {//actualiza la posicion de cada uno de los enemigos, incluyendo el espacio que ocupe el contenedor de los enemigos
        for(int i=0 ; i<enemigos.length ; i++){
            for(int j=0 ; j<enemigos[i].length ; j++){
                if(this.enemigos[i][j]!=null)
                    this.enemigos[i][j].actualizaArea();
            }
        }
    }
    
    public void destruirEnemigo(int i, int j){//hace invisible al enemigo que sufra el impacto
        if(i>=0 && j>=0){
            enemigos[i][j].setVisible(false);
            //this.remove(enemigos[i][j]);
            enemigos[i][j]= null;
            try{
                paraDisparar[j] = enemigos[i-1][j];//actualiza cuales naves pueden disparar
            }catch(IndexOutOfBoundsException e){
                paraDisparar[j] = null;//si se acaba la columna, queda vacio el array
            }
        }
        if(!this.hayEnemigos()){//cuando todos los enemigos son golpeados, quita todas las naves
            this.remove(0);//al quitar los objetos, activa el evento de ganar
        }
    }
    
    public boolean hayImpacto(Area a){//verificaba si hay impacto con un enemigo recibe el area del disparo
        this.actualizaPosicion();
        boolean impacto = false;
        int i=0,j=0;
        while(!impacto && i<enemigos.length ){//verifica todos los enemigos
            while(!impacto && j<enemigos[i].length){
                try{
                    impacto = this.posicion.sumate(enemigos[i][j].getArea()).seTocan(a);//el impacto se da cuando se tocan las areas del enemigo de la tabla y el disparo
                }catch(NullPointerException ex){

                }
                j++;
            }
            if(!impacto)
                j=0;
            i++;
        }
        if(impacto){//si hay impacto, destruye al enemigo que recibio.
            this.destruirEnemigo(--i, --j);
        }
        return impacto;//regresa un booleano de si hubo o no impacto
    }
    
    public boolean hayEnemigos(){//verifica si queda aunque sea un enemigo
        boolean hayEnemigo = false;
        int i=0,j=0;
        while(!hayEnemigo && i<enemigos.length ){
            while(!hayEnemigo && j<enemigos[i].length){
                hayEnemigo = enemigos[i][j] != null;
                j++;
            }
            j=0;
            i++;
        }
        return hayEnemigo;
    }
}

class Nave extends JLabel{
    private int ancho = 60;
    private int alto = 60;
    private Area a;
    
    
    public Nave(){
        super();
        this.setBounds(300-ancho/2, 650-alto, ancho, alto);
        //this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        a = new Area(this.getX()-10,this.getX()+ancho+10,this.getY(),this.getY()+alto);
        ImageIcon i = new ImageIcon("../SpaceInvaders1/src/spaceinvaders1/nave.png");
        ImageIcon iconoEscala = new ImageIcon(i.getImage().getScaledInstance(ancho, alto, java.awt.Image.SCALE_DEFAULT));
        this.setIcon(iconoEscala);
    }
    
    public void actualizaArea(){
        a.setArea(this.getX()-5,this.getX()+ancho+5,this.getY(),this.getY()+alto);
    }
    public Area getArea(){
        return a;
    }
    public int getAcho(){
        return ancho;
    }
    public int getAlto(){
        return alto;
    }
}


class Enemigo extends JLabel{
    private int ancho = 60;
    private int alto = 50;
    private int fila;
    private int columna;
    private boolean puedoDisparar;
    private Area a;
    //Imagen de la nave enemiga
    
    
    public Enemigo(boolean puedoDisparar,int fila,int columna){//recibe donde esta ubicado dentro de la tabla
        super();
        this.puedoDisparar = puedoDisparar;//nunca se uso
        this.fila=fila;
        this.columna=columna;
        this.setSize(ancho, alto);
        ImageIcon i = new ImageIcon("../SpaceInvaders1/src/spaceinvaders1/Enemigo.png");
        Image transicion = i.getImage();
        Image nuevaI = transicion.getScaledInstance(ancho, alto,  java.awt.Image.SCALE_SMOOTH); 
        i = new ImageIcon(nuevaI);
        this.setIcon(i);
        a = new Area(this.getX(),this.getX()+ancho,this.getY(),this.getY()+alto);
    }
    
    public void actualizaArea(){
        a.setArea(this.getX(),this.getX()+ancho,this.getY(),this.getY()+alto);
    }
    public Area getArea(){
        return a;
    }
    
    public void setPuedoDisparar(boolean b){
        this.puedoDisparar=b;
    }
    public boolean getPuedoDisparar(){
        return this.puedoDisparar;
    }
    public int getFila(){
        return this.fila;
    }
    public int getColumna(){
        return this.columna;
    }
    public int getAncho(){
        return this.ancho;
    }
    public int getAlto(){
        return this.alto;
    }
}

class Record extends JFrame{//ventana que aparece cuando se gana el juego
    FileReader lector;
    String record="";
    JButton nuevoJuego;
    JButton salir;
    JPanel c;
    public Record(String nuevoRecord){
        super("Record");
        this.setBounds(0, 0, 400, 300);
        c = new JPanel();
        c.setLayout(null);
        try {
            lector = new FileReader("../SpaceInvaders1/src/record.txt");//lee el record del ultimo juego que se gano
            int i=0;
            while(i!=-1){
                i = lector.read();
                if(i!=-1){
                    char c = (char)i;
                    if(c!='(')  
                        record+= c;
                    else
                        record+="--->";
                }
            }
        } catch (IOException ex) {
            
        }
        JLabel ultimo = new JLabel("Ultimo Record: "+record);//enseña el ultimo record
        ultimo.setFont(SpaceInvaders1.f);
        ultimo.setBounds(30, 10, 330, 50);
        JLabel tuyo = new JLabel("Tu Record: "+ nuevoRecord);//enseña tu record
        tuyo.setFont(SpaceInvaders1.f);
        tuyo.setBounds(30, 40, 330, 50);

        nuevoJuego = new JButton("Nuevo Juego?");
        nuevoJuego.setBounds(110, 160, 160, 30);
        salir = new JButton("Salir :(");
        salir.setBounds(150, 200, 80, 30);
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//sale del juego
                
                System.exit(0);
            }
        });
        c.add(ultimo);
        c.add(tuyo);
        c.add(nuevoJuego);
        c.add(salir);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(c);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }
}

class Disparo extends JButton implements EventListener{
    private int ancho = 10;
    private int alto = 30;
    private Area a;
    
    public Disparo(){
        super();
        this.setBackground(Color.red);
        this.setSize(ancho, alto);
        a = new Area(this.getX(),this.getX()+ancho,this.getY(),this.getY()+alto);
    }
    
    public void actualizaArea(){
        a.setArea(this.getX(),this.getX()+ancho,this.getY(),this.getY()+alto);
    }
    public Area getArea(){
        return a;
    }
    public boolean impacto(Area a2){
        return this.a.seTocan(a2);
    }
}

    
class Area{//clase utilizada para evualuar los impactos
    public int x1;
    public int x2;
    public int y1;
    public int y2;
    
    public Area(int x1,int x2,int y1,int y2){//tiene las posiciones de las cuatro esquinas del objeto
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
    }
    public boolean seTocan(Area a){//metodo para verificar impacto
        return (( (y1<a.y1&&y2>a.y2) || (y2>a.y1&&y2<a.y2) ) && (x1<a.x1&&x2>a.x2));
    }
    public void setArea(int x1,int x2,int y1,int y2){
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
    }
    
    public Area sumate(Area a2){//metodo para sumar dos areas(usado entre el contenedor de enemigos y los enemigos)
        Area resultado = new Area(this.x1+a2.x1, this.x1+a2.x2, this.y1+a2.y1, this.y1+a2.y2);
        return resultado;
    }
}
class coordenada{//clase para ubicar el punto de salida de los disparos
    public int x;
    public int y;
    
    public coordenada(int x, int y){
        this.x=x;
        this.y=y;
    }
}
