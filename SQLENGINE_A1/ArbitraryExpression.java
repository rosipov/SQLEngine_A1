package sqlengine_a1;
import java.util.*;
import sqlengine_a1.parser.*;

class Row {
	public Data getValue(String columnName) {
		return new Int(4);
	}
}

public class ArbitraryExpression {
	private ASTNode rootNode;
	
	public ArbitraryExpression(ASTNode node) {
		this.rootNode = node;
	}
	
	public Data evaluate(Row row) {
		return evalNode(rootNode, row);
	}
	
	private Data evalNode(ASTNode node, Row row) {
		if (node.type == ASTNode.Type.INTEGER)
			return new Int(node.intValue);
		/*else if (node.type == ASTNode.Type.FLOAT)
			return new Number(node.floatValue);
		else if (node.type == ASTNode.Type.STRING)
			return new Char(node.stringValue);
		else if (node.type == ASTNode.Type.NAME)
			return row.getValue(node.stringValue);*/
		else if (node.type == ASTNode.Type.LT_COMPARISON)
			return new Int(evalNode(node.subnodes.get("lhs"), row).compareTo(evalNode(node.subnodes.get("rhs"), row)) < 0? 1 : 0);
		else if (node.type == ASTNode.Type.GT_COMPARISON)
			return new Int(evalNode(node.subnodes.get("lhs"), row).compareTo(evalNode(node.subnodes.get("rhs"), row)) > 0? 1 : 0);
		else if (node.type == ASTNode.Type.EQ_COMPARISON)
			return new Int(evalNode(node.subnodes.get("lhs"), row).compareTo(evalNode(node.subnodes.get("rhs"), row)) == 0? 1 : 0);
		else if (node.type == ASTNode.Type.NE_COMPARISON)
			return new Int(evalNode(node.subnodes.get("lhs"), row).compareTo(evalNode(node.subnodes.get("rhs"), row)) != 0? 1 : 0);
		else if (node.type == ASTNode.Type.GE_COMPARISON)
			return new Int(evalNode(node.subnodes.get("lhs"), row).compareTo(evalNode(node.subnodes.get("rhs"), row)) >= 0? 1 : 0);
		else if (node.type == ASTNode.Type.LE_COMPARISON)
			return new Int(evalNode(node.subnodes.get("lhs"), row).compareTo(evalNode(node.subnodes.get("rhs"), row)) <= 0? 1 : 0);
		else
			return null;
	}
}