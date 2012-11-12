package sqlengine_a1;
import java.util.*;
import sqlengine_a1.parser.*;

public class SqlConsole {
	private static Database db;
	
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
				else if (astRoot.type == ASTNode.Type.EVAL_STATEMENT) {
					ArbitraryExpression expr = new ArbitraryExpression(astRoot.subnodes.get("expression"));
					System.out.println(expr.evaluate(null).toString());
				}
				else {
					DbResult result = execStatement(astRoot);
					if (result == null)
						System.out.println("No result returned.");
					else if (result.getReturnedRows() == null)
						System.out.println(result.getAffectedRowCount() + " rows affected.");
					else {
						for (String col : result.getColumnNames()) {
							System.out.print(col + "  ");
						}
						for (Row row : result.getReturnedRows()) {
							for (String col : result.getColumnNames()) {
								System.out.print(row.getData(col) + "  ");
							}
						}
					}
				}
			}
			catch (ParseError e) {
				System.err.println("Parse error: " + e);
			}
		}
	}
	
	public static DbResult execStatement(ASTNode rootNode) {
		if (rootNode.type == ASTNode.Type.CREATE_TABLE_STATEMENT) {
			String name = rootNode.sub("tableName").stringValue;
			List<ColumnDefinition> cols = new ArrayList<ColumnDefinition>();
			ASTNode thisNode = rootNode.sub("columns");
			while (true) {
				ASTNode here = thisNode.type == ASTNode.Type.FIELD_DEF_LIST? thisNode.sub("this") : thisNode;
				
				String colName = here.sub("columnName").stringValue;
				String type = here.sub("type").stringValue;
				int length = -1;
				if (here.subnodes.containsKey("length"))
					length = here.sub("length").sub("length").intValue;
				boolean nullable = true;
				if (here.subnodes.containsKey("nullity"))
					nullable = here.sub("nullity").type == ASTNode.Type.COLUMN_NULLABLE;
				
				cols.add(new ColumnDefinition(colName, type, length, nullable));
				
				if (thisNode.type == ASTNode.Type.FIELD_DEF_LIST)
					thisNode = thisNode.sub("next");
				else
					break;
			}
			
			db.createTable(name, cols);
			return null;
		}
		else if (rootNode.type == ASTNode.Type.LOAD_STATEMENT) {
			String name = rootNode.sub("dbName").stringValue;
			db = Database.load(name);
			return null;
		}
		else return null;
	}
}
