package view;

/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */
import model.file;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

public class FS_Tree extends JPanel 
                             implements ActionListener {
    private JTextField name; 
    private int newNodeSuffix = 1;
    private static String MKDIR_COMMAND = "MKDIR";
    private static String REMOVE_COMMAND = "remove";
    private static String CLEAR_COMMAND = "clear";
    private static String CRT_COMMAND = "CRT";
    private static String NAME = "name";
    private static String FLE_COMMAND = "FLE";
    
    
    private DynamicTree treePanel;

    public FS_Tree() {
        super(new BorderLayout());
        
        //Create the components.
        file new_root = new file("root","dir","");
        treePanel = new DynamicTree(new_root.getName());
        //populateTree(treePanel);
        
        name = new JTextField(NAME);            
        
        JButton crtButton = new JButton("CRT");
        crtButton.setActionCommand(CRT_COMMAND);
        crtButton.addActionListener(this);
        
        JButton addButton = new JButton("MKDIR");
        addButton.setActionCommand(MKDIR_COMMAND);
        addButton.addActionListener(this);
        
        JButton fileButton = new JButton("FLE");
        fileButton.setActionCommand(FLE_COMMAND);
        fileButton.addActionListener(this);
        
        JButton removeButton = new JButton("Remove");
        removeButton.setActionCommand(REMOVE_COMMAND);
        removeButton.addActionListener(this);
        
        JButton clearButton = new JButton("Clear");
        clearButton.setActionCommand(CLEAR_COMMAND);
        clearButton.addActionListener(this);

        //Lay everything out.
        treePanel.setPreferredSize(new Dimension(500, 350));
        add(treePanel, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(7,0));
        panel.add(name);
        panel.add(crtButton);
        panel.add(addButton);
        panel.add(fileButton);
        panel.add(removeButton); 
        panel.add(clearButton);
	add(panel, BorderLayout.EAST);
    }

    public void populateTree(DynamicTree treePanel) {
        /*
        String p1Name = new String("Parent 1");
        String p2Name = new String("Parent 2");
        String c1Name = new String("Child 1");
        String c2Name = new String("Child 2");

        DefaultMutableTreeNode p1, p2;

        p1 = treePanel.addObject(null, p1Name);
        p2 = treePanel.addObject(null, p2Name);

        treePanel.addObject(p1, c1Name);
        treePanel.addObject(p1, c2Name);

        treePanel.addObject(p2, c1Name);
        treePanel.addObject(p2, c2Name);
        */
    }
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (MKDIR_COMMAND.equals(command)) {
            //Add button clicked
            
            String name_aux = name.getText().toString();
            file new_root = new file(name_aux,"dir","");            
            treePanel.mkdir(new_root);
            
        }else if (FLE_COMMAND.equals(command)) {
            //Remove button clicked            
            String name_aux = name.getText().toString();
            file new_root = new file(name_aux,"txt","");            
            treePanel.mkdir(new_root);           
            treePanel.mkdir(new_root);
        } else if (REMOVE_COMMAND.equals(command)) {
            //Remove button clicked
            treePanel.removeCurrentNode();
        } else if (CLEAR_COMMAND.equals(command)) {
            //Clear button clicked.
            treePanel.clear();
        } else if(CRT_COMMAND.equals(command)){
            String name_aux = name.getText().toString();
            treePanel.create(name_aux);
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("File System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        FS_Tree newContentPane = new FS_Tree();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
