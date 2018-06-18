/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Fauricio
 */
public class file {
    private String name;
    private String type; //Extensión
    private String content;
    private Date create_date;
    private Date update;
    private int size_file;        

    public file(String name, String type, String conten, Date create_date, Date update, int size_file) {
        this.name = name;
        this.type = type;
        this.content = conten;
        this.create_date = create_date;
        this.update = update;
        this.size_file = size_file;
    }
        
    public file(String name, String type, String conten) {
        this.name = name;
        this.type = type;
        this.content = conten;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public int getSize_file() {
        return size_file;
    }

    public void setSize_file(int size_file) {
        this.size_file = size_file;
    }
       
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConten() {
        return content;
    }

    public void setConten(String conten) {
        this.content = conten;
    }

    public String get_all(){
        return name + type + create_date + update + content + '|';
    }
    @Override
    public String toString() {
        return "Name= " + name + ",Extension= " + type + ",create_date= " + create_date + '\n' +"Content= "+content + '\n';
    }

    public String getFullName() {
        return this.name + this.type;
    }
    
    
    
}
