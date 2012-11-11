/*
 * (c) Tensegrity Software 2005. All rights reserved.
 */
package org.palo.api.demos;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.palo.api.Connection;
import org.palo.api.ConnectionFactory;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Element;

/**
 * <code>JTableDemo</code>
 * 
 * <p>
 * This is a simple swing demo for JPalo. This demo will retrieve
 * a rectangular section of data from the Demo Datbase's Sales-Cube
 * and display it in a jtable.
 * </p>
 * 
 * To run this demo launch like this:
 * <pre>
 * java -classpath paloapi.jar -Djava.library.path=PATH_TO_PALO_DLLS org.palo.api.demos.JTableDemo
 * </pre>
 * 
 * <i>PATH_TO_PALO_DLLS</i> should point to a directory holding both libpalo.dll and palojava.dll. On 
 * unix systems the shared libraries will be named *.so rather than *.dll. 
 * </p>
 * 
 * @author Stepan Rutz
 * @version $ID$
 */
public class PaloAPIDemo
{
    // server configuration
    static final String PALO_SERVER = "localhost";
    static final String PALO_SERVICE = "7921";
    static final String PALO_USER = "admin";
    static final String PALO_PASS = "admin";
    
    // demo configuration, specifies database, cube and coordinates
    static final String DATABASE_NAME = "Demo";
    static final String CUBE_NAME = "Sales";

    // connection to Palo Server
    private static Connection connection;
    
    public static void main(final String[] args)
    {
        // connect to palo server on localhost with default credentials.
        connection = ConnectionFactory.getInstance().newConnection(
            PALO_SERVER,
            PALO_SERVICE,
            PALO_USER,
            PALO_PASS);

        Database database = connection.getDatabaseByName("System");
        
        Cube cube = database.getCubeByName("#_USER_GROUP");

        // construct a jtable
        JTable table = setupTable(cube);
        JScrollPane scrollpane = new JScrollPane(table);
        
        // construct frame and show it
        JFrame f = new JFrame("JTable Demo");

        f.addWindowListener(new WindowAdapter() 
	        {
	            public void windowClosed(WindowEvent e)
	            {
	                // disconnect from palo.
	                connection.disconnect();
	                System.exit(0);
	            }
	        });
        
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().add(scrollpane, BorderLayout.CENTER);
        java.awt.Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        f.setBounds(100, 100, screensize.width - 222, screensize.height - 200);
        f.setVisible(true);
    }
    
    private static JTable setupTable(Cube cube)
    {
        Element rowElements[] = cube.getDimensionAt(0).getElementsInOrder();
        Element columnElements[] = cube.getDimensionAt(1).getElementsInOrder();
        
        Object cubedata[] = cube.getDataArray(new Element[][] {
            rowElements,
            columnElements//,
            //new Element[] { cube.getDimensionAt(2).getElementAt(0) },
            //new Element[] { cube.getDimensionAt(3).getElementAt(0) },
            //new Element[] { cube.getDimensionAt(4).getElementAt(0) },
            //new Element[] { cube.getDimensionAt(5).getElementAt(0) },
        });
        
        int rowcount = rowElements.length;
        int columncount = columnElements.length;
        
        Object data[][] = new Object[rowcount][];
        for (int i = 0; i < rowcount; ++i)
        {
            data[i] = new Object[columncount + 1];
            data[i][0] = rowElements[i].getName();
            System.arraycopy(cubedata, i * columncount, data[i], 1, columncount);
        }

        Object columnnames[] = new String[columncount + 1];
        for (int i = 0; i < columnnames.length; ++i)
        {
            if (i == 0)
                columnnames[i] = "-";
            else
                columnnames[i] = columnElements[i - 1].getName();
        }
                
        DefaultTableModel model = new DefaultTableModel(data, columnnames); 

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        return table;
    }
}
