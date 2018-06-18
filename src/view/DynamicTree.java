package view;

/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */
import controller.controller;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.DropMode;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import model.file;

public class DynamicTree extends JPanel {

    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private file root_file;
    private String str_tree;

    public DynamicTree(FS_Tree fsTre) {
        super(new GridLayout(1, 0));
        root_file = new file("root", "root", "");
        rootNode = new DefaultMutableTreeNode(root_file);
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());
        tree = new JTree(treeModel);
        tree.setEditable(true);
        
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.ON_OR_INSERT);
        tree.setTransferHandler(new TreeTransferHandler(fsTre));
        
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    /**
     * Remove all nodes except the root node.
     */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }
    
    public String create_path(DefaultMutableTreeNode parentNode){                
        TreeNode[] path_list_aux = new TreeNode[WIDTH];        
        path_list_aux = parentNode.getPath();
        String path="";
        for(TreeNode d:path_list_aux){
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) d;
            Object obj = n.getUserObject();
            file f = (file) obj;
            path = path + (f.getName() +"/");            
        }
        return path;        
    }

    public void create(String name) {
        root_file = new file(name, "root", "");
        boolean band = controller.getInstance().add_disco(root_file);
        if(band){
            controller.getInstance().print_disco();
            rootNode.setUserObject(root_file);                    
        }else{
            JOptionPane.showMessageDialog(
                        this,
                        "No hay espacio en el File system",
                        "¡Error!",
                        JOptionPane.ERROR_MESSAGE
                );
        }        
    }

    public DefaultMutableTreeNode mkdir(Object child) {
        DefaultMutableTreeNode parentNode = null;
        file new_file = (file) child;
        file file_aux;
        Object user;
        TreePath parentPath = tree.getSelectionPath();
        if (new_file.getType().equals("dir") || new_file.getType().equals("root")) {
            if (parentPath == null) {
                parentNode = rootNode;
            } else {
                parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());                
            }                                 
            user = parentNode.getUserObject();                                                
            file_aux = (file) user;            
            if((file_aux.getType().equals("dir")||file_aux.getType().equals("root"))
                    && isNameRepit(parentNode, new_file.getName(),new_file.getType())==false){                                
                return addObject(parentNode, child, true);
            }else{
                JOptionPane.showMessageDialog(
                                this,
                                "No pueden haber 2 directorios con el mismo nombre.",
                                "¡Error!",
                                JOptionPane.ERROR_MESSAGE
                        );
            }
        } else {
            if (parentPath == null) {
                parentNode = rootNode;
            } else {
                parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());                
            }                          
            user = parentNode.getUserObject();                                                
            file_aux = (file) user;  
            if((file_aux.getType().equals("dir")||file_aux.getType().equals("root"))
                    && isNameRepit(parentNode, new_file.getName(),new_file.getType())==false){                                
                return addObject(parentNode, child, true);
            }
        }
        return null;
    }
    
    public boolean isNameRepit(DefaultMutableTreeNode parentNode,String name,String ext){
        boolean band = false;
        int lim = parentNode.getChildCount();
        if(lim != 0){
            for(int i=0;i<lim;i++){
            DefaultMutableTreeNode temp = (DefaultMutableTreeNode) parentNode.getChildAt(i);
            Object user = temp.getUserObject();                                                
            file file_aux = (file) user;
                if(file_aux.getName().equals(name)&&file_aux.getType().equals(ext)){
                    band = true;
                }
            }
        }
        return band;
    }
    
    
    public String tree_toString(){
        String res = "";        
        int lim = rootNode.getChildCount();            
        file file_temp = (file) root_file;
        res = file_temp.get_all();        
        for(int i = 0;i<lim;i++){
            DefaultMutableTreeNode next_node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            if(next_node != null){
                res = res + tree_toString_aux(next_node);
            }
        }  
        return res;
    }
    
    public String tree_toString_aux(DefaultMutableTreeNode node){
        String res = "";
        if(node != null){
            int lim = node.getChildCount();
            Object obj = node.getUserObject();
            file file_temp = (file) obj;
            res = res + file_temp.get_all();            
            for(int i = 0;i<lim;i++){
                DefaultMutableTreeNode next_node = (DefaultMutableTreeNode) node.getChildAt(i);                
                res = res + tree_toString_aux(next_node);                                
            }
        }
        return res;
    }
    
    public void printTree(){
        int lim = rootNode.getChildCount();            
        file file_temp = (file) root_file;
        System.out.printf(file_temp.toString());
        System.out.printf("+++++++++++++++++++++++++++++++++++++++"+'\n');
        for(int i = 0;i<lim;i++){
            DefaultMutableTreeNode next_node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            if(next_node != null){
                print_aux(next_node);
            }
        }                      
    }
    
    public void print_aux(DefaultMutableTreeNode node){
        if(node != null){
            int lim = node.getChildCount();
            Object obj = node.getUserObject();
            file file_temp = (file) obj;
            System.out.println(file_temp.toString());
            System.out.printf("====================================="+'\n');
            for(int i = 0;i<lim;i++){
                DefaultMutableTreeNode next_node = (DefaultMutableTreeNode) node.getChildAt(i);                
                print_aux(next_node);                                
            }
        }        
    }
    
    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
            if (parent != null) {
                String path_aux = create_path(currentNode);
                System.out.println("Current delete node:"+path_aux);
                controller.getInstance().remove_disco(path_aux);
                treeModel.removeNodeFromParent(currentNode);                
                return;
            }
        }

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    /**
     * Modifies current selected file's content
     */
    public void modifyCurrentFileNode(String new_file_content) {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());

            file archivo = (file) currentNode.getUserObject();                        
            archivo.setConten(new_file_content);
            controller.getInstance().actualizar_contenido(archivo, create_path(currentNode));
            controller.getInstance().reescribir_disco();
        }

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    /**
     * Add child to the currently selected node.
     */
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());

        }

        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
            Object child) {
        return addObject(parent, child, false);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
            Object child,
            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode
                = new DefaultMutableTreeNode(child);

        if (parent == null) {
            parent = rootNode;
        }

        //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent,
                parent.getChildCount());

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    String getCurrentFileContent() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());

            file archivo = (file) currentNode.getUserObject();
            return archivo.getConten();
        }

        // Either there was no selection, or the root was selected.
        toolkit.beep();
        return "";
    }

    boolean existsFile(file new_root) {
        System.out.println("===");
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());

            Enumeration children = currentNode.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode tmp_node = (DefaultMutableTreeNode) children.nextElement();
                file tmp_file = (file) tmp_node.getUserObject();
                System.out.println(tmp_file.getName());
                if (tmp_file.getFullName().equals(new_root.getFullName())) {
                    return true;
                }
            }

            return false;
        }

        return false;
    }

    boolean dirSelected() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            file file_tmp = (file) currentNode.getUserObject();
            if (file_tmp.getType().equals("dir") ||
                    file_tmp.getType().equals("root"))
                return true;
        }

        return false;
    }

    class MyTreeModelListener implements TreeModelListener {

        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */
            int index = e.getChildIndices()[0];
            node = (DefaultMutableTreeNode) (node.getChildAt(index));

            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }

        public void treeNodesInserted(TreeModelEvent e) {
        }

        public void treeNodesRemoved(TreeModelEvent e) {
        }

        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
}
