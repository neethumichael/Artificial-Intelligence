package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import util.Node;
import java.awt.*;
import java.util.*;

import javax.swing.JPanel;
public class DisplayTree extends JPanel{
	protected NodeDisp tree;
	// The font for the tree nodes
	protected Font font = new Font("Tahoma", Font.BOLD, 20);
	String target ;
	// The max. height of tree
	protected int maxHeight;
	public void printTree(Node rootTree, String targetLabel) {
		this.target = targetLabel;
		NodeDisp root=null;
		if (rootTree.getType().equals("leaf")) {
			NodeDisp newNode = new NodeDisp(rootTree.getleafLabel(),null,null);
			tree = newNode;
		} else {
			tree = createTree(rootTree);
		}						
		DisplayTree dp = new DisplayTree(tree,1000,1000);
		//treeDisplay td = new treeDisplay(tree);
	}

	public NodeDisp createTree(Node root) {
		if(!root.getType().contains("leaf")) {
			HashMap<String,Node> children = root.getChildren();
			Node left=null;
			Node right=null;
			for(Map.Entry<String, Node> child : children.entrySet()) {
				String identify = child.getKey();
				if(identify.contains("less")){
					left = child.getValue();
				}
				else {
					right = child.getValue();
				}
			}
			NodeDisp newNode = new NodeDisp(root.getFeature(),createTree(left),createTree(right));
			return newNode;
		}else {
			String label = root.getleafLabel();
			if(root.getleafLabel().equals("")) {
				label = "No "+target;
			}
			NodeDisp newNode = new NodeDisp(label,null,null);
			return newNode;
		}
	}

	public DisplayTree(){
	}
	/* 
	 * Create a new window with the given width and height 
	 * that is showing the given tree.
	 */
	public DisplayTree(NodeDisp tree, int width, int height) {

		//Initialize drawing colors, border, opacity.
		setBackground(Color.white);
		setForeground(Color.black);

		// Create window and make it so hitting the close icon
		// will terminate the program
		JFrame f = new JFrame("BinaryTree View");
		JFrame frame = new JFrame();

		// add the panel to a JScrollPane
		JScrollPane jScrollPane = new JScrollPane(this);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// adding the jScrollPane to your frame
		frame.getContentPane().add(jScrollPane);

		// listener for closing the window
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		f.getContentPane().add(this, BorderLayout.CENTER);
		f.setSize(new Dimension(width, height));
		f.setVisible(true);

		// initial tree.
		setTree(tree);
	}

	// Set the display to show the given tree. 
	public void setTree(NodeDisp t) {
		tree = t;
		maxHeight = heightOfTree(tree);
	}

	public void refresh() {
		paintImmediately(0,0, getWidth(), getHeight());
	}

	// draws the tree between minX and maxX
	protected void drawTree(Graphics g, int minX, int maxX, 
			int y, int yStep, NodeDisp tree) {

		String s = (String)tree.data;

		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int width = fm.stringWidth(s);
		int height = fm.getHeight();

		int xSep = Math.min((maxX - minX)/8, 10);

		g.drawString(s, (minX + maxX)/2 - width/2, y + yStep/2);

		if (tree.left!=null) {
			// recursively draw the tree
			g.drawLine((minX + maxX)/2 - xSep, y + yStep/2 + 5,
					(minX + (minX + maxX)/2) / 2, 
					y + yStep + yStep/2 - height);
			drawTree(g, minX, (minX + maxX)/2, y + yStep, yStep, tree.left);
		}
		if (tree.right!=null) {
			// same as above
			g.drawLine((minX + maxX)/2 + xSep, y + yStep/2 + 5,
					(maxX + (minX + maxX)/2) / 2, 
					y + yStep + yStep/2 - height);
			drawTree(g, (minX + maxX)/2, maxX, y + yStep, yStep, tree.right);
		}
	}

	//calls drawTree whenever the window needs to be repainted.
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);      //clears the background
		int width = getWidth();
		int height = getHeight();
		maxHeight = Math.max(heightOfTree(tree), maxHeight);
		int treeHeight = maxHeight;
		drawTree(g, 0, width, 0, height / (treeHeight + 1), tree);
	} 
	
	// return the height of tree with given root node
	public int heightOfTree(NodeDisp node)
	{
		if (node == null)		{
			return 0;
		}
		else		{
			return 1 +
					Math.max(heightOfTree(node.left),
							heightOfTree(node.right));
		}
	}
}

// data structure for node
class NodeDisp {  
	Object data;
	NodeDisp left;
	NodeDisp right; 

	NodeDisp(String x, NodeDisp l, NodeDisp r) {
		left = l;
		right = r;
		data = (Object) x;
	}
}
