/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testt;

/**
 *
 * @author thanglongsp
 */
import java.awt.*;
import static java.awt.Color.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Testt extends Panel {
    
    protected Connection conn;
    private int last_x, last_y;
    public Color currentColor = Color.black;


        public void setMyColor(Color c) {
            currentColor= c;
        }
    
        public void connectDB() throws SQLException, ClassNotFoundException{
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/md", "root", "");
                Statement st = conn.createStatement();
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Error in connectDB()");
            }  
        }
    
    Testt() throws SQLException,  ClassNotFoundException{
        connectDB();
        setBackground(Color.white);
        setSize(400,400);
       
        
        addMouseListener( new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
        Graphics g = getGraphics();
        float kc[] = new float[1000];
        String[] cl = new String[1000];
        float d;
        int j=0,i = 0;
        
        try {
            connectDB();
        } catch (SQLException ex) {
            Logger.getLogger(Testt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Testt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        // -------- draw from db ----------------------------------------------------
            Statement stm = null;
            try {
                stm = conn.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(Testt.class.getName()).log(Level.SEVERE, null, ex);
            }
            ResultSet rs = null;
            try {
                rs = stm.executeQuery("SELECT * FROM xy ");
            } catch (SQLException ex) {
                Logger.getLogger(Testt.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                while (rs.next()) {
                    if(rs.getString("color").equals("red")){
                        g.setColor(Color.red);
                        g.fillRect(rs.getInt("tdx"),rs.getInt("tdy"), 5,5 );
                    }else{
                        g.setColor(Color.blue);
                        g.fillRect(rs.getInt("tdx"),rs.getInt("tdy"), 5,5 );
                    }
                last_x = e.getX(); last_y = e.getY();
                d = (float) Math.sqrt((last_x - rs.getInt("tdx"))*(last_x - rs.getInt("tdx")) + (last_y-rs.getInt("tdy"))*(last_y-rs.getInt("tdy")));    
                kc[i] = d;
                cl[i] = rs.getString("color");
                i ++ ;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Testt.class.getName()).log(Level.SEVERE, null, ex);
            }
            //--- 0k ----
            
            float tem;
            int leng = 0;
            i = 0;
            while(kc[i] != 0.0)
            { 
                leng +=1;
                i++;
            }
            String string;  
            for(i = 0; i < leng;i++){
                for(j = 0;j<= i ; j ++){
                    if(kc[j] > kc[i]){
                        tem = kc[j];
                        kc[j] = kc[i];
                        kc[i] = tem;
                        
                        string = cl[j];
                        cl[j] = cl[i];
                        cl[i] = string;
                    }
                }
            }
            // ---- 
            int dem = 0;
            int r1 = 0;
            for(i = 0; i < 3 ;i++){
                if(cl[i].equals("red")) dem +=1;
                System.out.println(kc[i]);
                System.out.println(cl[i]);
                
            }

            if(dem >= 2){
                try {
                    r1 = stm.executeUpdate("INSERT INTO xy VALUES ('" +last_x+ "','" +last_y+ "','red')");
                } catch (SQLException ex) {
                    Logger.getLogger(Testt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(dem <= 1){
                try {
                    r1 = stm.executeUpdate("INSERT INTO xy VALUES ('" +last_x+ "','" +last_y+ "','blue')");
                } catch (SQLException ex) {
                    Logger.getLogger(Testt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }         
        }
            
        });  
        }
        
    
        
    static public void main(String args[]) throws ClassNotFoundException, SQLException {
        
        Frame f = new Frame("KNN in spatial data :))");
        f.setLayout(new BorderLayout());
        f.setSize(450,500);
        f.setBackground(Color.black);
        final Testt cb = new Testt();
        f.add(cb, BorderLayout.CENTER);
        cb.setLocation(25,50);
        
        f.add(new Panel(), BorderLayout.EAST);
        f.add(new Panel(), BorderLayout.NORTH);
        f.add(new Panel(), BorderLayout.WEST);
        
        Panel pan= new Panel();
        pan.setLayout(new GridLayout());
        f.add(pan, BorderLayout.SOUTH);
        
        Panel p1= new Panel();
        p1.setLayout(new FlowLayout());
        Panel p2= new Panel();
        p2.setLayout(new FlowLayout());
        pan.add(p1);
        pan.add(p2);
        
        
        //-------
        Button b = new Button("Quit");
        //b.setBounds(100, 100, 200, 200);
        p2.add(b);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Quit");
                System.exit(0);
            }
        }
        );
        
        f.setVisible(true);
        }

}