package com.holub.database;

import java.io.*;	
import java.util.*;

public class HTMLExporter implements Table.Exporter {
	private final Writer out;
	public HTMLExporter(Writer out) {
		this.out = out;
	}
	
	private 	  int	 width;
	
	public void startTable() throws IOException {}

	public void storeMetadata( String tableName,
							   int width,
							   int height,
							   Iterator columnNames ) throws IOException
	{	
		this.width = width;
		// 테이블의 이름 작성 
		out.write(tableName == null ? "<anonymous>" : tableName );
		out.write("<p>");
}

	public void storeRow( Iterator data ) throws IOException
	{	int i = width;
		while( data.hasNext() )
		{	
			// 데이터 작성 
			Object datum = data.next();
			if( datum != null )	{
				out.write( datum.toString() );
			}

			if( --i > 0 ) {
				out.write("&emsp;/&emsp;");
			}
		}
		out.write("<br>");
	}
	
	@Override
	public void endTable() throws IOException {
		// TODO Auto-generated method stub
	}
	public static class Test
	{ 	public static void main( String[] args ) throws IOException
		{	
			// Export할 테이블 생성  
			Table people = TableFactory.create( "people",
						   new String[]{ "First", "Last"		} );
			people.insert( new String[]{ "Allen",	"Holub" 	} );
			people.insert( new String[]{ "Ichabod",	"Crane" 	} );
			people.insert( new String[]{ "Rip",		"VanWinkle" } );
			people.insert( new String[]{ "Goldie",	"Locks" 	} );
			
			Writer out = new FileWriter( "people.html" );
			
			// ConcreteBuilder 부름 
			Table.Exporter tableBuilder = new HTMLExporter( out );
			
			// Build a HTML product
			people.export( tableBuilder );
			
			out.close();
		}
	}
}
