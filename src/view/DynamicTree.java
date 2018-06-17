package view;

/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Enumeration;
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
import model.file;

public class DynamicTree extends JPanel {

    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private file root_file;

    public DynamicTree() {
        super(new GridLayout(1, 0));
        root_file = new file("root", "root", "");
        rootNode = new DefaultMutableTreeNode(root_file);
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());
        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
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

    public void create(String name) {
        root_file = new file(name, "root", "");
        rootNode.setUserObject(root_file);
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
                parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
            }
            user = parentNode.getUserObject();
            file_aux = (file) user;
            if (file_aux.getType().equals("dir") || file_aux.getType().equals("root")) {
                return addObject(parentNode, child, true);
            }
        } else {
            if (parentPath == null) {
                parentNode = rootNode;
            } else {
                parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
            }
            user = parentNode.getUserObject();
            file_aux = (file) user;
            if (file_aux.getType().equals("dir") || file_aux.getType().equals("root")) {
                return addObject(parentNode, child, true);
            }
        }
        return null;
    }

    /**
     * Remove the currently selected node.
     */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
            if (parent != null) {
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
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());

            file archivo = (file) currentNode.getUserObject();
            archivo.setConten(new_file_content);
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
