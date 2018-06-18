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
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import controller.controller;
import java.util.Date;
import javax.swing.tree.DefaultMutableTreeNode;

public class FS_Tree extends JPanel
        implements ActionListener {

    private JLabel jlTitle;
    private int newNodeSuffix = 1;
    private static String CRT_COMMAND = "CRT";
    private static String FLE_COMMAND = "FLE";
    private static String MKDIR_COMMAND = "MKDIR";
    private static String MFLE_COMMAND = "MFLE";
    private static String PPT_COMMAND = "PPT";
    private static String VIEW_COMMAND = "VIEW";
    private static String COPY_COMMAND = "COPY";
    private static String MOV_COMMAND = "MOV";
    private static String REM_COMMAND = "REM";
    private static String FIND_COMMAND = "FIND";
    private static String CLEAR_COMMAND = "clear";
    private static String NAME = "name";

    private DynamicTree treePanel;

    public FS_Tree() {
        super(new BorderLayout());

        //Create the components.        
        treePanel = new DynamicTree(this);

        JButton crtButton = new JButton("CRT");
        crtButton.setActionCommand(CRT_COMMAND);
        crtButton.addActionListener(this);

        JButton addButton = new JButton("MKDIR");
        addButton.setActionCommand(MKDIR_COMMAND);
        addButton.addActionListener(this);

        JButton fileButton = new JButton("FLE");
        fileButton.setActionCommand(FLE_COMMAND);
        fileButton.addActionListener(this);

        JButton removeButton = new JButton("REM");
        removeButton.setActionCommand(REM_COMMAND);
        removeButton.addActionListener(this);

        JButton clearButton = new JButton("Clear");
        clearButton.setActionCommand(CLEAR_COMMAND);
        clearButton.addActionListener(this);

        JButton modButton = new JButton("MFLE");
        modButton.setActionCommand(MFLE_COMMAND);
        modButton.addActionListener(this);

        JButton viewButton = new JButton("VIEW");
        viewButton.setActionCommand(VIEW_COMMAND);
        viewButton.addActionListener(this);
        
        JButton findButton = new JButton("FIND");
        findButton.setActionCommand(FIND_COMMAND);
        findButton.addActionListener(this);

        //Lay everything out.
        treePanel.setPreferredSize(new Dimension(500, 350));
        add(treePanel, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(crtButton);
        panel.add(addButton);
        panel.add(fileButton);
        panel.add(removeButton);        
        panel.add(modButton);
        panel.add(viewButton);
        panel.add(findButton);
        panel.add(clearButton);
        add(panel, BorderLayout.WEST);

        JPanel panel2 = new JPanel(new GridLayout(8, 0));
        JLabel jlVisor = new JLabel("    Contenido del archivo seleccionado:    ");
        jlTitle = new JLabel();
        panel2.add(jlVisor);
        panel2.add(jlTitle);
        add(panel2, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (MKDIR_COMMAND.equals(command)) {
            //Add button clicked                                           
            String name_aux = JOptionPane.showInputDialog("Digite el nombre del directorio.");
            Date create_date = new Date();
            file new_root = new file(name_aux,"dir","",create_date,create_date,0); 
            if(name_aux.equals("")){
                JOptionPane.showMessageDialog(
                                this,
                                "Espacio en blanco.",
                                "¡Error!",
                                JOptionPane.ERROR_MESSAGE
                        );
            }else{
                boolean band = controller.getInstance().add_disco(new_root);
                if(band){                    
                    DefaultMutableTreeNode parentNode = treePanel.mkdir(new_root);  
                    String Mypath = treePanel.create_path(parentNode);
                    System.out.println(Mypath);
                    controller.getInstance().actualizar_path(name_aux, Mypath);                                        
                    controller.getInstance().print_disco();
                    controller.getInstance().reescribir_disco();
                }else{
                    JOptionPane.showMessageDialog(
                                this,
                                "No hay espacio en el File system",
                                "¡Error!",
                                JOptionPane.ERROR_MESSAGE
                        );
                }
                
            }            
        }else if (FLE_COMMAND.equals(command)) {
            // Remove button clicked

            if (treePanel.dirSelected()) {
                String input = JOptionPane.showInputDialog("Nombre y extensión del archivo:");
                
                // to do: Reescribir para contemplar caso hola.mundo.txt
                String[] splitted_input = input.split("\\.");
                if (splitted_input.length == 2) {
                    String file_name = splitted_input[0];
                    String file_extension = splitted_input[1];
                    Date create_date = new Date();
                    file new_root = new file(file_name, file_extension, "",create_date,create_date,0);
                    if (treePanel.existsFile(new_root)) {
                        JOptionPane.showMessageDialog(
                                this,
                                "No pueden haber 2 archivos con el mismo nombre.",
                                "¡Error!",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        String file_content = JOptionPane.showInputDialog(new JTextArea(), "Contenido del archivo:");
                        if (file_content == null) {
                            file_content = "";
                        }
                        
                        new_root.setConten(file_content);                        
                        boolean band = controller.getInstance().add_disco(new_root);
                        if(band){                    
                            DefaultMutableTreeNode parentNode = treePanel.mkdir(new_root);
                            String Mypath = treePanel.create_path(parentNode);
                            System.out.println(Mypath);
                            controller.getInstance().actualizar_path(file_name, Mypath);                                        
                            controller.getInstance().print_disco();
                            controller.getInstance().reescribir_disco();
                        }else{
                            JOptionPane.showMessageDialog(
                                        this,
                                        "No hay espacio en el File system",
                                        "¡Error!",
                                        JOptionPane.ERROR_MESSAGE
                                );
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Ingrese un nombre y extensión para el archivo.",
                            "Error en el formato",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else
                JOptionPane.showMessageDialog(
                    this,
                    "Solo se pueden crear archivos en un directorio.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
        } else if (REM_COMMAND.equals(command)) {                    
            //Remove button clicked
            treePanel.removeCurrentNode();                        
            controller.getInstance().reescribir_disco();
        } else if (CLEAR_COMMAND.equals(command)) {
            //Clear button clicked.
            treePanel.clear();
        } else if (CRT_COMMAND.equals(command)) {
            String name_temp = JOptionPane.showInputDialog("Nombre del directoria raíz.");
            String num_sect = JOptionPane.showInputDialog("Cantidad de sectores");
            String tam_sect = JOptionPane.showInputDialog("Tamaño del sector");
            controller.getInstance().file_system(name_temp, num_sect, tam_sect);
            treePanel.create(name_temp);
            controller.getInstance().reescribir_disco();
        } else if (MFLE_COMMAND.equals(command)) {
            String file_content = JOptionPane.showInputDialog(new JTextArea(), "Nuevo contenido del archivo:");
            if (file_content == null) {
                file_content = "";
            }
            treePanel.modifyCurrentFileNode(file_content);
            controller.getInstance().print_disco();
        } else if (VIEW_COMMAND.equals(command)) {
            String file_content = treePanel.getCurrentFileContent();
            jlTitle.setText(file_content);
        } else if (FIND_COMMAND.equals(command)){
            String input = JOptionPane.showInputDialog("Nombre y extensión del archivo:");
                
            // to do: Reescribir para contemplar caso hola.mundo.txt
            String[] splitted_input = input.split("\\.");
            String aux = "";
            if (splitted_input.length == 2) {
                String file_name = splitted_input[0];
                String file_extension = splitted_input[1];                
                aux = treePanel.findTree(file_name, file_extension);
            }else{
                String file_name = splitted_input[0];
                aux = treePanel.findTree(file_name, "");
            }
            System.out.println(aux);
            JOptionPane.showMessageDialog(this, "+++++++ Rutas +++++++"+'\n'+aux);
        }
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
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
