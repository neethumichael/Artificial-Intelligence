package util;

import java.util.HashMap;
import util.Feature;

public class Node {
	private Nodetype type;
	private Feature feature;
	private HashMap<String, Node> children;
	private String leafLabel;
	public enum Nodetype { root, leaf};

	public Node(Feature feature) {
		type = Nodetype.root;
		this.feature = feature;
		children = new HashMap<String, Node>();
	}

	public Node(String nodeLabel) {
		type = Nodetype.leaf;
		this.leafLabel = leafLabel;
	}
	public Feature getAttribute() {
		return feature;
	}

	public void addChild(String Name, Node child) {
		children.put(Name, child);
	}
	public HashMap<String, Node> getChildren() {
		return children;
	}

	public String getleafLabel() {
		return leafLabel;
	}

	public void setleafLabel(String leafLabel) {
		this.leafLabel = leafLabel;
	}

	public Nodetype getType() {
		return type;
	}

	public void setType(Nodetype type) {
		this.type = type;
	}
}