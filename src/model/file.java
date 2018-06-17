/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author Fauricio
 */
public class file {
    private String name;
    private String type; //Extensión
    private String conten;
    private Date create_date;
    private Date update;
    private int size_file;        

    public file(String name, String type, String conten, Date create_date, Date update, int size_file) {
        this.name = name;
        this.type = type;
        this.conten = conten;
        this.create_date = create_date;
        this.update = update;
        this.size_file = size_file;
    }
        
    public file(String name, String type, String conten) {
        this.name = name;
        this.type = type;
        this.conten = conten;
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
        return conten;
    }

    public void setConten(String conten) {
        this.conten = conten;
    }

    @Override
    public String toString() {
        return "file{" + "name=" + name + ", type=" + type + ", conten=" + conten + '}';
    }
    
    
}
