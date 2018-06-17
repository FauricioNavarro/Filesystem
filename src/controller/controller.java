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
/**
 *
 * @author Fauricio
 */
public class controller {
    private static controller instance = null;
    private String line = "==============================================="+'\n';
    private static String path = "C:\\Users\\Fauricio\\Desktop\\Filesystem\\data\\";
    
    private controller() {                        
    }
    
    public static controller getInstance() {
        if(instance == null) {
         instance = new controller();
      }
      return instance;
    }         
    
    public void file_system(String name,String sectores,String tamaño){
        BufferedWriter writer = null;        
        try {
	     File file = new File(path+name+".txt");	     
             boolean fvar = file.createNewFile();
	     if (fvar){
	          System.out.println("File has been created successfully");                  
                  writer = new BufferedWriter(new FileWriter(file));                  
                  writer.write(name);
                  writer.write(line);
                  int num_sector = Integer.parseInt(sectores);
                  int tam = Integer.parseInt(tamaño);
                  for(int i=0;i<num_sector;i++){
                      int break_line = 0;
                      for(int j=0;j<tam;j++){
                          if(break_line < 50){
                              writer.write((char)254);
                              break_line++;
                          }else{
                              break_line=0;                              
                              writer.write(System.getProperty("line.separator"));
                              writer.write((char)254);
                          }
                      }
                      writer.write(line);                      
                  }
                  writer.close();
	     }else{
	          System.out.println("File already present at the specified location");
	     }
    	} catch (IOException e) {
    		System.out.println("Exception Occurred:");
	        e.printStackTrace();
	}
    }    
}
