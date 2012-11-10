package sqlengine_a1;
import java.util.*;
import sqlengine_a1.parser.*;

public class SqlConsole {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);
		while (true) {
			String line = stdin.nextLine();
			if (line.equalsIgnoreCase("quit"))
				break;
			SqlTokenizer st = new SqlTokenizer(line);
			Token token;
			try {
				while ((token = st.nextToken()) != null)
					System.out.println(token.toString());
			}
			catch (ParseError e) {
				System.err.println("Parse error: " + e);
			}
        }
    }
}
