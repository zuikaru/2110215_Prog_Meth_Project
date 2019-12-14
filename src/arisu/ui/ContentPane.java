package arisu.ui;

import java.util.HashMap;
import java.util.Map;

import arisu.Arisu;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class ContentPane extends StackPane{
	private Map<String, Node> contents;
	private String current;
	public ContentPane(Arisu app) {
		contents = new HashMap<>();
	}
	
	public void add(String link, Node node) {
		contents.put(link, node);
	}
	
	public void setCurrent(String name) {
		if(current != null && current.equals(name)) return;
		current = name;
		if(contents.containsKey(current)) {
			Node node = contents.get(current);
			if(node != null) {
				if(getChildren().isEmpty())
					getChildren().add(node);
				else
					getChildren().set(0, node);
			}
		}
	}
	
	public Node getCurrentNode() {
		return contents.get(current);
	}
	
	public String getCurrent() {
		return current;
	}
}
