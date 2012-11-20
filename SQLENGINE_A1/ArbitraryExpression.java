package sqlengine_a1;
import java.util.*;
import sqlengine_a1.parser.*;

public class ArbitraryExpression {
	private ASTNode rootNode;
	
	public ArbitraryExpression(ASTNode node) {
		this.rootNode = node;
	}
	
	public Data evaluate(Row row) throws SqlException {
		return evalNode(rootNode, row);
	}
	
	private Data evalNode(ASTNode node, Row row) throws SqlException {
		if (node.type == ASTNode.Type.INTEGER)
			return new Int(node.intValue);
		/*else if (node.type == ASTNode.Type.FLOAT)
			return new Number(node.floatValue);
		else if (node.type == ASTNode.Type.STRING)
			return new Char(node.stringValue);*/
		else if (node.type == ASTNode.Type.NAME)
			return row.getData(node.stringValue);
		else if (node.type == ASTNode.Type.LT_COMPARISON)
			return new Int(evalNode(node.sub("lhs"), row).compareTo(evalNode(node.sub("rhs"), row)) < 0? 1 : 0);
		else if (node.type == ASTNode.Type.GT_COMPARISON)
			return new Int(evalNode(node.sub("lhs"), row).compareTo(evalNode(node.sub("rhs"), row)) > 0? 1 : 0);
		else if (node.type == ASTNode.Type.EQ_COMPARISON)
			return new Int(evalNode(node.sub("lhs"), row).compareTo(evalNode(node.sub("rhs"), row)) == 0? 1 : 0);
		else if (node.type == ASTNode.Type.NE_COMPARISON)
			return new Int(evalNode(node.sub("lhs"), row).compareTo(evalNode(node.sub("rhs"), row)) != 0? 1 : 0);
		else if (node.type == ASTNode.Type.GE_COMPARISON)
			return new Int(evalNode(node.sub("lhs"), row).compareTo(evalNode(node.sub("rhs"), row)) >= 0? 1 : 0);
		else if (node.type == ASTNode.Type.LE_COMPARISON)
			return new Int(evalNode(node.sub("lhs"), row).compareTo(evalNode(node.sub("rhs"), row)) <= 0? 1 : 0);
		else if (node.type == ASTNode.Type.LOGICAL_EXPRESSION) {
			ASTNode.Type op = node.sub("operator").type;
			if (op == ASTNode.Type.AND) {
				Data temp = evalNode(node.sub("lhs"), row);
				if (temp.isTrue())
					return new Int(evalNode(node.sub("rhs"), row).isTrue()? 1 : 0);
				else
					return new Int(0);
			}
			else if (op == ASTNode.Type.OR) {
				Data temp = evalNode(node.sub("lhs"), row);
				if (temp.isTrue())
					return new Int(1);
				else
					return new Int(evalNode(node.sub("rhs"), row).isTrue()? 1 : 0);
			}
			else if (op == ASTNode.Type.XOR) {
				boolean a = evalNode(node.sub("lhs"), row).isTrue();
				boolean b = evalNode(node.sub("rhs"), row).isTrue();
				return new Int(((a || b) && !(a && b))? 1 : 0);
			}
			else {
				throw new SqlException("unknown logical op (" + op + ")...?");
			}
		}
		else if (node.type == ASTNode.Type.NOT_EXPRESSION) {
			return new Int(evalNode(node.sub("operand"), row).isTrue()? 0 : 1);
		}
		else
			return null;
	}
}