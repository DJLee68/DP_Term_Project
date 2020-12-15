package com.holub.database;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.holub.database.Table.Importer;

public class XMLImporter implements Importer {
	private Document document;
	private DocumentBuilder docbuilder;
	private DocumentBuilderFactory docfactory;
	private List<String> columnNames = new ArrayList<String>();
	private String          tableName;
	private Element table;
	private NodeList nodes;
	private int row_cnt = 0;
	
	public XMLImporter(String filename) {
		docfactory = DocumentBuilderFactory.newInstance();
		try {
			docbuilder = docfactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			document = docbuilder.parse(filename);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table = document.getDocumentElement();
		nodes = table.getChildNodes();

	}
	@Override
	public void startTable() throws IOException {
		// TODO Auto-generated method stub
		
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){ // 해당 노드의 종류 판정(Element일 때)
				Element ele = (Element)node;
				String nodeName = ele.getNodeName();				
				if(nodeName.equals("column")){
					NodeList column = ele.getElementsByTagName("column_name");
					for(int j=0; j<column.getLength(); j++) {
						Element tmp = (Element) column.item(j);
						columnNames.add(tmp.getAttribute("value"));
					}
				}
			}
		}
	}

	@Override
	public String loadTableName() throws IOException {
		// TODO Auto-generated method stub
		tableName = table.getAttribute("name");
		return tableName;
	}

	@Override
	public int loadWidth() throws IOException {
		// TODO Auto-generated method stub
		System.out.println(columnNames.size());
		return columnNames.size();
	}

	@Override
	public Iterator loadColumnNames() throws IOException {
//		System.out.println(columnNames);
		// TODO Auto-generated method stub
		return columnNames.iterator();
	}

	@Override
	public Iterator loadRow() throws IOException {
		List<String> rows = new ArrayList<String>();
//		System.out.println("a"+nodes.getLength()+"a");
		for(; row_cnt < nodes.getLength(); row_cnt++){
//			System.out.println("cnt"+row_cnt);
			Node node = nodes.item(row_cnt);
			if(node.getNodeType() == Node.ELEMENT_NODE){ // 해당 노드의 종류 판정(Element일 때)
//				System.out.println("a");
				Element ele = (Element)node;
				String nodeName = ele.getNodeName();
				if(nodeName.equals("row")){
//					System.out.println("b");
					NodeList childeren2 = ele.getChildNodes();
//					System.out.println(childeren2.getLength()+"a");
					for(int a = 0; a < childeren2.getLength(); a++){
//						System.out.println("c");
						Node node2 = childeren2.item(a);
						
						if(node2.getNodeType() == Node.ELEMENT_NODE){
		
							Element ele2 = (Element)node2;
							String nodeName2 = ele2.getNodeName();
							rows.add(ele2.getAttribute("value"));
		//					System.out.println("node name2: " + nodeName2);
		//					System.out.println("node attribute2: " + ele2.getAttribute("value"));
						}
					}
					row_cnt++;
					break;
				}
			}
//			System.out.println(row_cnt);
			
		}
		// TODO Auto-generated method stub
//		System.out.println(rows);
		if(rows.size()==0) return null;
		else {
			System.out.println(rows);
			return rows.iterator();
		}
	}

	@Override
	public void endTable() throws IOException {
		// TODO Auto-generated method stub

	}
	public static class Test
	{ 	public static void main( String[] args ) throws IOException
		{	
			
			Table t = TableFactory.create(new XMLImporter("people.xml"));
			
			//Junit Test(직접 만든 테이블과 비교?)
						
		}
	}

}
