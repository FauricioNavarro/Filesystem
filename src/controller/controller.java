/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import model.file;
/**
 *
 * @author Fauricio
 */
public class controller {
    private JTree tree;
    private DefaultMutableTreeNode root;
    private static controller instance = null;
    
    private controller() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        root.add(new DefaultMutableTreeNode("dir0"));
        System.out.printf("Inicio");
        
    }
    
    public static controller getInstance() {
        if(instance == null) {
         instance = new controller();
      }
      return instance;
    }

    public DefaultMutableTreeNode getRoot() {
        return root;
    }       

    public JTree getTree() {
        return tree;
    }
    
    private static class controllerHolder {

        private static final controller INSTANCE = new controller();
    }
    
    public void create_directory(String name){
        file new_file = new file(name,"dir","");
        DefaultMutableTreeNode newDirectory = new DefaultMutableTreeNode(new_file);
        this.root.add(newDirectory);
        //this.tree = new JTree(root);
        String mjs = root.getFirstChild().toString();
        System.out.print(mjs);
    }
    
}
