/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedWriter;
import model.file;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.sector;
/**
 *
 * @author Fauricio
 */
public class controller {
    private static controller instance = null;
    private String line = '\n'+"==============================================="+'\n';
    private static String path = "C:\\Users\\Fauricio\\Desktop\\Filesystem\\data\\";
    private int cant_sectores=0;
    private int tamaño_sector=0;
    private String name_file;
    private ArrayList<sector> disco_virtual;
    
    private controller() {                        
    }
    
    public static controller getInstance() {
        if(instance == null) {
         instance = new controller();
      }
      return instance;
    }     
        
    public void inicializa_disco(){
        disco_virtual  = new ArrayList<>();
        for(int i=0;i<cant_sectores;i++){
            sector sector_n = new sector("", i, "", true, "");
            disco_virtual.add(sector_n);
        }
    }
    
    public void print_disco(){
        for(sector s : disco_virtual){
            System.out.println(s.toString());
            System.out.println(line);
        }
    }
    
    public int sectores_disponibles(){
        int sectores = 0;
        for(sector s : disco_virtual){
            boolean estado = s.getEmpty();
            if(estado)
                sectores++;            
        }
        return sectores;
    }
    
    public int sectores_ocupado(String path){
        int sectores = 0;
        for(sector s : disco_virtual){            
            if(s.getRuta().equals(path))
                sectores++;            
        }
        return sectores;
    }
    
    public int sectores_necesarios(int tamaño){
        int tam = 0;
        double sec_necesitados = tamaño / tamaño_sector;
        int sec_necesitados_aux = tamaño / tamaño_sector;
        double res = sec_necesitados - sec_necesitados_aux;
        if(res == 0){
            tam = sec_necesitados_aux;
        }else{
            tam = sec_necesitados_aux+1;
        }
        return tam;
    }
    
    public void actualizar_path(String nombre,String my_path){
        for(sector s:disco_virtual){
            if(s.getNombre().equals(nombre)){
                if(s.getRuta().isEmpty() || s.getRuta().equals(nombre)){
                    s.setRuta(my_path);
                }
            }
        }
    }
    
    public boolean actualizar_contenido(file new_file,String my_path){
        int linea = new_file.get_all().length();
        System.out.println("largo:"+linea);
        int sec_necesitados = sectores_necesarios(linea);
        int sec_act = sectores_ocupado(my_path);
        int sec = sec_necesitados - sec_act;
        int sec_fin;
        boolean bool = true;
        System.out.println("sectores:"+sec_necesitados);
        if(sec == 0){
            sec_fin = sec;
        }else if(sec > 0){
            //pedir un nuevo sector
            if(sec < sectores_disponibles()){
                sec_fin = sec_act + sec;
            }else{
                bool = false;
            }
        }else{
            sec_fin = sec_act + sec;
        }
        if(bool){
            int i = 0;
            for(sector s:disco_virtual){
                if(s.getRuta().equals(my_path) && i<=sec_necesitados){
                    s.setNombre(new_file.getName());
                    s.setIsEmpty(false);       
                    int begin,end;
                    if(i==sec_necesitados){
                        begin = i*tamaño_sector;
                        end = linea;                                                
                    }else{
                        begin = i*tamaño_sector;
                        end = ((i*tamaño_sector)+tamaño_sector)-1;                                                
                    }                    
                    System.out.println(i+"|"+begin+"-"+end);
                    String content_aux = new_file.get_all().substring(begin, end);
                    s.setContenido(content_aux);          
                    i++;
                }else if(s.getEmpty()==true && i<=sec_necesitados){
                    s.setNombre(new_file.getName());
                    s.setIsEmpty(false);       
                    int begin,end;
                    if(i==sec_necesitados){
                        begin = i*tamaño_sector;
                        end = linea;                                                
                    }else{
                        begin = i*tamaño_sector;
                        end = ((i*tamaño_sector)+tamaño_sector)-1;                                                
                    }                    
                    System.out.println(i+"|"+begin+"-"+end);
                    String content_aux = new_file.get_all().substring(begin, end);
                    s.setContenido(content_aux);          
                    i++;
                }
            }
        }        
        return bool;
    }
    
    public void remove_disco(String path){
        for(sector s:disco_virtual){
            if(s.getRuta().equals(path)){
                s.setIsEmpty(true);
                s.setRuta("");
            }
        }
    }
    
    public boolean add_disco(file new_file){
        boolean band = false;
        int linea = new_file.get_all().length();
        System.out.println("largo:"+linea);
        int sec_necesitados = sectores_necesarios(linea);
        System.out.println("sectores:"+sec_necesitados);
        if(sec_necesitados < sectores_disponibles()){
            int i=0;
            for(sector s:disco_virtual){
                if(s.getEmpty()==true && i<=sec_necesitados){
                    s.setNombre(new_file.getName());
                    s.setIsEmpty(false);       
                    int begin,end;
                    if(i==sec_necesitados){
                        begin = i*tamaño_sector;
                        end = linea;                                                
                    }else{
                        begin = i*tamaño_sector;
                        end = ((i*tamaño_sector)+tamaño_sector)-1;                                                
                    }                    
                    System.out.println(i+"|"+begin+"-"+end);
                    String content_aux = new_file.get_all().substring(begin, end);
                    s.setContenido(content_aux);          
                    i++;
                }
            }            
            band = true;
        }
        return band;
    }
            
    public void file_system(String name,String sectores,String tamaño){
        BufferedWriter writer = null;              
        try {
            name_file = path+name+".txt";
	     File file = new File(path+name+".txt");	     
             boolean fvar = file.createNewFile();
	     //if (fvar){
	     //     System.out.println("File has been created successfully");                  
                  writer = new BufferedWriter(new FileWriter(file));                  
                  writer.write(name);
                  writer.write(System.getProperty("line.separator"));
                  writer.write(line);                      
                  writer.write(System.getProperty("line.separator"));
                  int num_sector = Integer.parseInt(sectores);
                  int tam = Integer.parseInt(tamaño);                  
                  tamaño_sector = tam;
                  cant_sectores = num_sector;
                  inicializa_disco();
                  for(int i=0;i<num_sector;i++){
                      int break_line = 0;
                      for(int j=0;j<tam;j++){
                          if(break_line < 50){
                              writer.write((char)254);
                              break_line++;
                          }else{
                              break_line=0;                              
                              writer.write(System.getProperty("line.separator"));                              
                          }
                      }
                      writer.write(System.getProperty("line.separator"));
                      writer.write(line);                      
                      writer.write(System.getProperty("line.separator"));
                  }
                  writer.close();
	     //}else{
	     //     System.out.println("File already present at the specified location");
	     //}
    	} catch (IOException e) {
    		System.out.println("Exception Occurred:");
	        e.printStackTrace();
	}
    }   
    
    public void reescribir_disco(){
        BufferedWriter writer = null;              
        try {            
	    File file = new File(name_file);	 
            writer = new BufferedWriter(new FileWriter(file));              
	    for(sector s:disco_virtual){                
                int break_line = 0;
                int fragmentacion = 0;
                String text = s.getContenido();
                int len_text = text.length();
                if(s.getEmpty()==false){
                    for(int j=0;j<tamaño_sector;j++){
                        if(break_line < 50){
                            if(j<len_text){
                                writer.write(text.charAt(j));
                            }else{
                                writer.write((char)254);
                            }
                            break_line++;
                        }else{
                            break_line=0;                              
                            writer.write(System.getProperty("line.separator"));
                            if(j<len_text){
                                writer.write(text.charAt(j));
                            }else{
                                writer.write((char)254);
                            }
                        }                    
                    }
                    writer.write(System.getProperty("line.separator"));
                    writer.write(line);                      
                    writer.write(System.getProperty("line.separator"));
                }else{
                    for(int j=0;j<tamaño_sector;j++){
                          if(break_line < 50){
                              writer.write((char)254);
                              break_line++;
                          }else{
                              break_line=0;                              
                              writer.write(System.getProperty("line.separator"));                              
                          }
                      }
                      writer.write(System.getProperty("line.separator"));
                      writer.write(line);                      
                      writer.write(System.getProperty("line.separator"));
                }                
            }
            writer.close();
     //}else{
     //     System.out.println("File already present at the specified location");
     //}
    	} catch (IOException e) {
    		System.out.println("Exception Occurred:");
	        e.printStackTrace();
	}
    }
}
