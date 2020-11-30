package com.holub.database;

import java.io.*;	
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
 

public class XMLExporter implements Table.Exporter {
	private 	  int	 width;
	private Document document;
	private DocumentBuilder docBuilder;
	private DocumentBuilderFactory docFactory;
	private Element table;
	private Element column;
	private Element row;
	private boolean isColumn = true;
	private int row_cnt = 1;
	
	public XMLExporter() {
		docFactory = DocumentBuilderFactory.newInstance();
		
        try {
			docBuilder = docFactory.newDocumentBuilder();
			document = docBuilder.newDocument();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void startTable() throws IOException {}

	public void storeMetadata( String tableName,
							   int width,
							   int height,
							   Iterator columnNames ) throws IOException
	{	
		// root 생성
		table = document.createElement("table");
		// root 속성 설정
		if(tableName == null) table.setAttribute("name", "Anonymous");
		else table.setAttribute("name", tableName);
		document.appendChild(table);
		
		column = document.createElement("column");
		table.appendChild(column);
		storeRow( columnNames); // comma separated list of columns ids
		
}

	public void storeRow(Iterator data) throws IOException
	{	
		Element row_element = document.createElement("row");
		row_element.setAttribute("num", Integer.toString(row_cnt));
		int col_cnt = 1;
		while(data.hasNext()) {
			Object datum = data.next();
			if(datum!=null) {
				if(datum!=null && isColumn) {
					// column 엘리먼트
		            Element col_name = document.createElement("column_name");
		            col_name.setAttribute("value", datum.toString());
		            column.appendChild(col_name);
				}
				else {
					//row element
					Element row_value = document.createElement("column"+col_cnt);
					col_cnt++;
					row_value.setAttribute("value", datum.toString());
					row_element.appendChild(row_value);
				}
			}
		}
		isColumn = false;
		if(row_element.hasChildNodes()) {
			row_cnt++;
			table.appendChild(row_element);
		}
		
	}
	
	public void endTable() throws IOException {
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		 
        Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(new File("people.xml")));

        try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
	}
	public static class Test
	{ 	public static void main( String[] args ) throws IOException
		{	
			Table people = TableFactory.create( "people",
						   new String[]{ "First", "Last"		} );
			people.insert( new String[]{ "Allen",	"Holub" 	} );
			people.insert( new String[]{ "Ichabod",	"Crane" 	} );
			people.insert( new String[]{ "Rip",		"VanWinkle" } );
			people.insert( new String[]{ "Goldie",	"Locks" 	} );
		    
			XMLExporter tableBuilder = new XMLExporter();
			
            people.export( tableBuilder );
			
		}
	}
}
