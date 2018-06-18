/*
* Basado en https://coderanch.com/t/346509/java/JTree-drag-drop-tree-Java
* de Craig Wood
 */
package view;

import java.awt.datatransfer.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.tree.*;
import model.file;

class TreeTransferHandler extends TransferHandler {

    DataFlavor nodesFlavor;
    DataFlavor[] flavors = new DataFlavor[1];
    DefaultMutableTreeNode[] nodesToRemove;
    private FS_Tree fsTree;
    private boolean rename;

    public TreeTransferHandler(FS_Tree fsTree) {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=\""
                    + javax.swing.tree.DefaultMutableTreeNode[].class.getName()
                    + "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
            this.fsTree = fsTree;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }
        support.setShowDropLocation(true);
        if (!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }

        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl
                = (JTree.DropLocation) support.getDropLocation();
        JTree tree = (JTree) support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        // Unless it is just one row!
        if (selRows.length > 1) {
            for (int i = 0; i < selRows.length; i++) {
                if (selRows[i] == dropRow) {
                    return false;
                }
            }
        }

        // Do not allow MOVE-action drops if a non-leaf node is
        // selected unless all of its children are also selected.
        int action = support.getDropAction();
        if (action == MOVE) {
            return haveCompleteNode(tree);
        }
        // Do not allow a non-leaf node to be copied to a level
        // which is less than its source level.
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode target
                = (DefaultMutableTreeNode) dest.getLastPathComponent();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode firstNode
                = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (firstNode.getChildCount() > 0
                && target.getLevel() < firstNode.getLevel()) {
            return false;
        }

        return true;
    }

    private boolean haveCompleteNode(JTree tree) {
        int[] selRows = tree.getSelectionRows();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode first
                = (DefaultMutableTreeNode) path.getLastPathComponent();
        int childCount = first.getChildCount();
        // first has children and no children are selected.
        if (childCount > 0 && selRows.length == 1) {
            return false;
        }
        // first may have children.
        for (int i = 1; i < selRows.length; i++) {
            path = tree.getPathForRow(selRows[i]);
            DefaultMutableTreeNode next
                    = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (first.isNodeChild(next)) {
                // Found a child of first.
                if (childCount > selRows.length - 1) {
                    // Not all children of first are selected.
                    return false;
                }
            }
        }
        return true;
    }

    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree) c;
        TreePath[] paths = tree.getSelectionPaths();
        if (paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes that will be removed in
            // exportDone after a successful drop.
            List<DefaultMutableTreeNode> copies
                    = new ArrayList<DefaultMutableTreeNode>();
            List<DefaultMutableTreeNode> toRemove
                    = new ArrayList<DefaultMutableTreeNode>();
            DefaultMutableTreeNode node
                    = (DefaultMutableTreeNode) paths[0].getLastPathComponent();
            DefaultMutableTreeNode copy = copy(node);
            copies.add(copy);
            toRemove.add(node);
            for (int i = 1; i < paths.length; i++) {
                DefaultMutableTreeNode next
                        = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                // Do not allow higher level nodes to be added to list.
                if (next.getLevel() < node.getLevel()) {
                    break;
                } else if (next.getLevel() > node.getLevel()) {  // child node
                    copy.add(copy(next));
                    // node already contains child
                } else {                                        // sibling
                    copies.add(copy(next));
                    toRemove.add(next);
                }
            }
            DefaultMutableTreeNode[] nodes
                    = copies.toArray(new DefaultMutableTreeNode[copies.size()]);
            nodesToRemove
                    = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }

    /**
     * Defensive copy used in createTransferable.
     */
    private DefaultMutableTreeNode copy(TreeNode node) {
        return new DefaultMutableTreeNode(node);
    }

    protected void exportDone(JComponent source, Transferable data, int action) {
        if (!this.rename) {
            if ((action & MOVE) == MOVE) {
                JTree tree = (JTree) source;
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                // Remove nodes saved in nodesToRemove in createTransferable.
                for (int i = 0; i < nodesToRemove.length; i++) {
                    model.removeNodeFromParent(nodesToRemove[i]);
                }
            }
        } else
            this.rename = false;
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        // Extract transfer data.
        DefaultMutableTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor);
        } catch (UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch (java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }

        // Get drop location info.
        JTree.DropLocation dl
                = (JTree.DropLocation) support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode parent
                = (DefaultMutableTreeNode) dest.getLastPathComponent();
        JTree tree = (JTree) support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

        // Determine whether the action is a move or a rename.
        // It is assured there is only one file in "nodes" in the case
        // of a rename becuase drops over an element are only allowed
        // for one element.
        file target_file = (file) parent.getUserObject();
        DefaultMutableTreeNode tmp_node =
                (DefaultMutableTreeNode) nodes[0].getUserObject();
        file mov_file = (file) tmp_node.getUserObject();
        
        if (target_file.getFullName().equals(mov_file.getFullName())) {
            if (!target_file.getType().equals("root")) {
                String new_name = JOptionPane.showInputDialog("Nuevo nombre:");
                target_file.setName(new_name);
                this.rename = true;
            }
        } else {
            // Just allow target nodes of type "dir"/"root"
            if (!target_file.getType().equals("dir")
                    && !target_file.getType().equals("root")) {
                return false;
            }

            // Configure for drop mode.
            int index = childIndex;    // DropMode.INSERT
            if (childIndex == -1) {     // DropMode.ON
                index = parent.getChildCount();
            }
            
            DefaultMutableTreeNode tmp_node2;
            // Check for file uniqueness
            for (int i = 0; i < nodes.length; i++) {
                tmp_node = (DefaultMutableTreeNode) nodes[i].getUserObject();
                mov_file = (file) tmp_node.getUserObject();
                Enumeration children = parent.children();
                while (children.hasMoreElements()) {
                    tmp_node2 = (DefaultMutableTreeNode) children.nextElement();
                    target_file = (file) tmp_node2.getUserObject();
                    if (mov_file.getFullName().equals(
                            target_file.getFullName())) {
                        JOptionPane.showMessageDialog(
                            fsTree,
                            "El nombre de los archivos debe ser único.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                        return false;
                    }
                }
            }
            
            // Add data to model.
            for (int i = 0; i < nodes.length; i++) {
                model.insertNodeInto(nodes[i], parent, index++);
            }
        }

        return true;
    }

    public String toString() {
        return getClass().getName();
    }

    public class NodesTransferable implements Transferable {

        DefaultMutableTreeNode[] nodes;

        public NodesTransferable(DefaultMutableTreeNode[] nodes) {
            this.nodes = nodes;
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return nodes;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}
