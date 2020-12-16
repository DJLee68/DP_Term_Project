package com.holub.database;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.junit.jupiter.api.Test;

class XMLExporterTest {
    public String temp_fname = "people_temp.xml";
    public String original_fname = "people.xml";

    @Test
    void testExport() throws IOException {
        // 기존의 XML 파일을 테이블로 변환 
        Table table = readTable(original_fname);

        // 변환된 테이블에서 다른 XML 파일로 Export 
        table.export(new XMLExporter(temp_fname));

        // XML 파일을 테이블로 변환 
        Table createdTable = readTable(temp_fname);
        
        // 같은지 비교 
        assertEquals(table.toString(), createdTable.toString());
    }

    private Table readTable(String name) throws IOException {
        Table table = new ConcreteTable(new XMLImporter(name));
        return table;
    }
}