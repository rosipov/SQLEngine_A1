package sqlengine_a1;

import java.util.*;

public class Table
{
    protected List<ColumnDefinition> columns;
    private List<Row> rows;

    public Table(List<ColumnDefinition> columns)
    {
	this.columns = columns;
    }

    public List<ColumnDefinition> getColumns()
    {
	return columns;
    }

    public void insertRow(Row row)
    {
	// TODO validate
	rows.add(row);
    }

    public int columnIndex(String columnName)
    {
	for (int i = 0; i < columns.size(); i++)
	    if (columns.get(i).getName().equals(columnName))
		return i;
	return -1;
    }

    // SELECT statement
    public DbResult select(List<String> columnNames, ArbitraryExpression where) throws SqlException
    {
	////////////////////////
	//SELECT * FROM table;//
	////////////////////////
	// if specific columns are not specified (if it is a SELECT *)
	if (columnNames == null)
	{
	    // get all of the column names
	    columnNames = new ArrayList<String>();
	    for (ColumnDefinition col : columns)
		columnNames.add(col.getName());

	    // get all of the rows for the table
	    List<Row> returnedRows = new ArrayList<Row>();
	    for (int i = 0; i < rows.size(); i++)
	    {
		returnedRows.add(rows.get(i));
	    }

	    return new DbResult(columnNames, returnedRows);
	}

	//////////////////////////////////////////////
	//SELECT col0[, col1, col2, ...] FROM table;//
	//////////////////////////////////////////////
	// if columns are specified (if it is SELECT colName0, colName1, ...)
	// else
	// {
	// //check to see if all of the requested columns are valid
	// boolean success = true;
	//
	// for(int i = 0; i < columnNames.size() && success == true; i++)
	// {
	// //if it does not contain the column name, stop and do not do the rest
	// of the query
	// if(!this.contains(columnNames.get(i)))
	// {
	// success = false;
	// }
	// }
	//
	// //only do the rest if every column is correct
	// if(success)
	// {
	// columnNames = new ArrayList<String>();
	// rows = new ArrayList<Row>();
	//
	// for(String col : columnNames)
	// {
	// columnNames.add(col.getName()); //TODO: How do we keep each row type
	// separate? ~Joey
	// //find out where this column is in the Table's column list
	//
	// }
	// }
	//
	// //fail
	// else
	// {
	// throw new SqlException e;
	// }
	// }
	// TODO: get rid of this and do it properly ~Joey
	return new DbResult(new ArrayList(), new ArrayList());
    }

    public DbResult insert(List<String> columns, List<List<Data>> rows) throws SqlException 
    {
        ////////////////////////////////////////////////////////
        //INSERT INTO table VALUES (literal [, literal ] ...);//
        ////////////////////////////////////////////////////////
	//if field values are not passed in
	if(columns == null)
	{
	    
	}
	
        ///////////////////////////////////////////////////////////////////////////////
        //INSERT INTO table [( field [,field]...)] VALUES (literal [, literal ] ...);//
        ///////////////////////////////////////////////////////////////////////////////
	//if field values (columns) are passed in
	else
	{
	    
	}
	
	//TODO: get rid of me
	return null;
    }

    public DbResult delete(ArbitraryExpression where) throws SqlException
    {
        //////////////////////
        //DELETE FROM table;//
        //////////////////////
	// if there is no where clause (if it is just DELETE)
	if (where == null)
	{
	    // remove all of the rows
	    rows.removeAll(rows);

	    // return the effective number of rows left (should be 0)
	    return new DbResult(rows.size());
	}

        ////////////////////////////////////////
        //DELETE FROM table [WHERE condition];//
        ////////////////////////////////////////
	// else, if it is has a where clause (if it is Delete Where condition)
	else
	{
	    // evaluate each row
	    for (Row thisRow : rows)
	    {
		// if the row meets the Where condition
		if (where.evaluate(thisRow).isTrue())
		{
		    // remove it
		    rows.remove(thisRow);
		}
	    }
	    return new DbResult(rows.size());
	}
    }

    public DbResult update(List<String> columnNames,
	    List<ArbitraryExpression> values, ArbitraryExpression where)
	    throws SqlException
    {
	
	////////////////////////////////////////////////////////////////////////////////////
	//UPDATE table_name  SET field_name = expression [[, field_name = expression]...];//  
	////////////////////////////////////////////////////////////////////////////////////
	//if there is no Where condition
	if(where == null)
	{
	    //check to see if the number of field names mathes the number of expressions
	    if(columnNames.size() == values.size())
	    {
		//for each column name passed in
		for(int i = 0; i < columnNames.size(); i++)
		{
		    //find the column number affected 
		    int index = columnIndex(columnNames.get(i));				//TODO:ask about validation ~Joey
		    
		    //for each row in the table
		    for(Row row : rows)
		    {
			//update its value to the new "values" variable passed in            //TODO:ask about this ~Joey
			row.setData(index, values.get(i).evaluate(row));
		    }
		  
		}
		
	    }
	    
	    //otherwise, its an error
	    else
	    {
		throw new SqlException("Error: The number of column names listed after 'SET' must match the number of values");
	    }
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//UPDATE table_name  SET field_name = expression [[, field_name = expression]...][WHERE condition];//  
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//if there is a Where condition
	else
	{
	    
	}
	return null;
    }

    // tests if the Table contains a specific type of column based off of its
    // name (case insensative)
    public boolean contains(String col)
    {
	boolean found = false;

	for (int i = 0; i < columns.size() && !found; i++)
	{
	    // if you find a matching column name
	    if (columns.get(i).getName().equals(col))
	    {
		found = true;
	    }
	}

	return found;
    }

    public int getRowCount()
    {
	return rows.size();
    }
}
