package com.holub.database;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;


class XMLImporterTest {
    final String orinal_fname = "people.xml";

    // test table name
    @Test
    void loadTableName() throws IOException {
        Table.Importer importer = getImporter();

        final String temp = "people";
        String tableName = importer.loadTableName();
        assertEquals(temp, tableName);
    }
    
    // test table width
    @Test
    void loadWidth() throws IOException {
        Table.Importer importer = getImporter();

        final int temp = 2;
        int tableWidth = importer.loadWidth();
        assertEquals(temp, tableWidth);
    }

    // test column names
    @Test
    void loadColumnNames() throws IOException {
        Table.Importer importer = getImporter();

        final String[] temps = {"First", "Last"};
        String[] columnNames = new String[importer.loadWidth()];
        Iterator columns = importer.loadColumnNames();
        for (int i = 0; columns.hasNext();) {
            columnNames[i++] = columns.next().toString();
        }

        int i = 0;
        for(String temp : temps){
            assertEquals(temp, columnNames[i++]);
        }
    }

    // test row values
    @Test
    void loadRow() throws IOException {
        Table.Importer importer = getImporter();

        int width = importer.loadWidth();
        Iterator columns = importer.loadColumnNames();

        String[] columnNames = new String[width];
        int i = 0;
        while(columns.hasNext()){
            columnNames[i++] = columns.next().toString();
        }

        Object[] current = new Object[width];
        while ((columns = importer.loadRow()) != null) {
            i = 0;
            while(columns.hasNext()){
                current[i++] = columns.next().toString();
            }
        }
        
        // check last row
        assertEquals("Goldie", current[0].toString());
        assertEquals("dj", current[1].toString());
    }

    // return importer
    private Table.Importer getImporter() throws IOException {
        Table.Importer importer = new XMLImporter("people.xml");
        importer.startTable();
        return importer;
    }
}