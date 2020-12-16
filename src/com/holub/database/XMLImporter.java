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
	
	// 생성자가 불릴 때 XML 파일 형식을 위한 세팅을 함 
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
		
		// XML의 각 컬럼 정보를 세팅함 
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
		return columnNames.size();
	}

	@Override
	public Iterator loadColumnNames() throws IOException {
		// TODO Auto-generated method stub
		return columnNames.iterator();
	}

	@Override
	public Iterator loadRow() throws IOException {
		List<String> rows = new ArrayList<String>();
		// 노드의 개수만큼 순회 
		for(; row_cnt < nodes.getLength(); row_cnt++){
			Node node = nodes.item(row_cnt);
			if(node.getNodeType() == Node.ELEMENT_NODE){ // 해당 노드의 종류 판정(Element일 때)
				Element ele = (Element)node;
				String nodeName = ele.getNodeName();
				if(nodeName.equals("row")){ //해당 노드의 이름이 'row'일 때
					NodeList childeren2 = ele.getChildNodes();
					for(int a = 0; a < childeren2.getLength(); a++){ // 각 row의 컬럼들의 값을 순회 
						Node node2 = childeren2.item(a);
						if(node2.getNodeType() == Node.ELEMENT_NODE){
							Element ele2 = (Element)node2;
							String nodeName2 = ele2.getNodeName();
							rows.add(ele2.getAttribute("value")); // 각 row의 각 컬럼들의 값을 추가 
						}
					}
					row_cnt++;
					break;
				}
			}
		}
		// TODO Auto-generated method stub
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
