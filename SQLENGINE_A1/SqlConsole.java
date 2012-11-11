package sqlengine_a1;
import java.util.*;
import sqlengine_a1.parser.*;

public class SqlConsole {
	public static void main(String[] args) {
		Scanner stdin = new Scanner(System.in);
		while (true) {
			System.out.print("|> ");
			String line = stdin.nextLine();
			
			try {
				SqlParser sp = new SqlParser(line);
				ASTNode astRoot = sp.parseStatement();
				System.out.println(astRoot.toString());
				
				if (astRoot.type == ASTNode.Type.QUIT_STATEMENT)
					return;
				
				if (astRoot.type == ASTNode.Type.EVAL_STATEMENT) {
					ArbitraryExpression expr = new ArbitraryExpression(astRoot.subnodes.get("expression"));
					System.out.println(expr.evaluate(null).toString());
				}
			}
			catch (ParseError e) {
				System.err.println("Parse error: " + e);
			}
		}
	}

	public static int createTable(string tName, ArrayList<columnNode> inputArrayList) {	
		Table aTable = new Table(tName);
		for(int i = 0; i<inputArrayList.size(); i++) {
			aTable.addColumn(inputArrayList.get(i));
		}				
	}
}

class columnNode {
	private string columnName;
	private string columnType;
	private int columnLength;
	private boolean notNull;
	
	public columnNode(string cName, string cType, int cLength, boolean nNull){
		columnName = cName;
		columnType = cType;
		columnLength = cLength;
		notNull = nNull;
	}
	
	public string getColName(){
		return columnName;
	}
	public string getColType(){
		return columnType;
	}	
	public int getColLength(){
		return columnLength;
	}	
	public boolean getNotNull(){
		return notNull;
	}	
}

class Table {
	private string tableName;
	private static ArrayList<columnNode> columnArray = new ArrayList<columnNode>();
	private static ArrayList<string> rowArray = new ArrayList<string>();
	
	public Table(string tName) {
		tableName = tName;
	}
	
	public addColumn(columnNode node){
		columnArray.add(node);
	}

	public addRow(string data){
		rowArray.add(data);
	}
}
