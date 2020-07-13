package com.thinking.machines.TMORMFramework.pl;
import com.thinking.machines.TMORMFramework.*;
import javax.swing.*;
import java.io.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.math.*;
import javax.swing.filechooser.*;
import java.io.*;
import javax.swing.tree.*;
import java.sql.*;
public class TMORMPanel extends JPanel implements TreeSelectionListener
{
private JLabel moduleTitle;
private JTable table;
private JScrollPane jsp;
private TMDB tmdb;
private JTree tablesTree;
private JTree viewsTree;
private JScrollPane viewsTreeScrollPane;
private JLabel tablesTitle;
private JLabel viewsTitle;
private TableDetails tableDetails;
public static java.util.Map<String,Tables> tablesMap=new java.util.HashMap<>();
public static java.util.Map<String,Tables> viewsMap=new java.util.HashMap<>();
//private TMORMModel tmORMModel;
public TMORMPanel()
{
//tmORMModel=new TMORMModel();
tmdb=TMDB.getInstance();
for(Tables t:tmdb.tablesList)
{
tablesMap.put(t.tableName.toLowerCase().trim(),t);
}
initComponents();
setAppearance();
addListeners();
}
private void initComponents()
{
setLayout(null);
int lm=5;
int tm=5;
moduleTitle=new JLabel("TMORM Framework");
DefaultMutableTreeNode tables=new DefaultMutableTreeNode("Tables");
DefaultMutableTreeNode tableName=null;
DefaultMutableTreeNode column=null;
for( Tables tableData :tmdb.tablesList)
{
if(tableData.tableName.equalsIgnoreCase("sys_config"))continue;
tableName=new DefaultMutableTreeNode(tableData.tableName);
for(Attribute attribute:tableData.attributes)
{
column=new DefaultMutableTreeNode(attribute.columnName);
tableName.add(column);
}
tables.add(tableName);
}

tablesTitle=new JLabel("Tables:");
tablesTitle.setBounds(2,50,250,40);
tablesTree=new JTree(tables);
tablesTree.setRootVisible(false);
tableDetails=new TableDetails();
tableDetails.setBounds(250,80,700,610);
jsp=new JScrollPane(tablesTree);
moduleTitle.setBounds(lm+350,0,400,100);
jsp.setBounds(1,80,250,290);
viewsTitle=new JLabel("Views:");
viewsTitle.setBounds(2,360,250,40);

tables=new DefaultMutableTreeNode("Views");
for( Tables tableData :tmdb.viewsList)
{
viewsMap.put(tableData.tableName,tableData);
if(tableData.tableName.equalsIgnoreCase("sys_config"))continue;
tableName=new DefaultMutableTreeNode(tableData.tableName);
for(Attribute attribute:tableData.attributes)
{
column=new DefaultMutableTreeNode(attribute.columnName);
tableName.add(column);
}
tables.add(tableName);
}


viewsTree=new JTree(tables);
viewsTree.setRootVisible(false);
viewsTreeScrollPane=new JScrollPane(viewsTree);
viewsTreeScrollPane.setBounds(1,390,250,290);
add(moduleTitle);
add(jsp);
add(viewsTreeScrollPane);
add(tablesTitle);
add(viewsTitle);
add(tableDetails);
}
private void setAppearance()
{
viewsTreeScrollPane.setBorder(BorderFactory.createLineBorder(new Color(112,112,112)));
jsp.setBorder(BorderFactory.createLineBorder(new Color(112,112,112)));
Font moduleTitleFont=new Font("Verdana",Font.BOLD,24);
moduleTitle.setFont(moduleTitleFont);
Font font=new Font("Verdana",Font.BOLD,16);
tablesTitle.setFont(font);
viewsTitle.setFont(font);
font=new Font("Verdana",Font.PLAIN,14);
tablesTree.setFont(font);
viewsTree.setFont(font);
//table.setRowHeight(30);
//table.setFont(font);
Font tableTitleFont=new Font("Verdana",Font.BOLD,16);
/*table.getTableHeader().setFont(tableTitleFont);
table.getColumnModel().getColumn(0).setPreferredWidth(100);
table.getColumnModel().getColumn(1).setPreferredWidth(560);
table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
table.getTableHeader().setResizingAllowed(false);
table.getTableHeader().setReorderingAllowed(false);*/
}
private void addListeners()
{
tablesTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener(){
public void valueChanged(TreeSelectionEvent e)
{
Object obj=e.getNewLeadSelectionPath().getLastPathComponent();
TMORMPanel.this.tableDetails.setTableName(obj.toString());
TMORMPanel.this.tableDetails.setActionPanelEnabled(true);
}
});
viewsTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener(){
public void valueChanged(TreeSelectionEvent e)
{
Object obj=e.getNewLeadSelectionPath().getLastPathComponent();
TMORMPanel.this.tableDetails.setViewName(obj.toString());
TMORMPanel.this.tableDetails.setActionPanelEnabled(false);
}
});
}
public void valueChanged(TreeSelectionEvent e)
{
}
//inner class

class TableDetails extends JPanel implements ActionListener
{
public final int  VIEW_MODE=1;
public final int  EDIT_MODE=2;
public final int  ADD_MODE=3;
public final int GAC_MODE=4;
public final int DELETE_MODE=5;
public final int VIEW_DISPLAY_MODE=6;
private JTable  table;
private JLabel label;
private JPanel noDataPanel;
private JScrollPane scrollPane;
private DefaultTableModel dtm;
private JPanel actionButtonPanel;
private JButton addButton;
private JButton editButton;
private JButton deleteButton;
private JButton generateAnnotationClassButton;
private JButton cancelButton;
private Tables tableData;
private Tables viewData;
private JPanel actionPanel;
private int currentMode=0;
private String rows[][];
private String columns[];
private JTextArea textArea;
private JScrollPane textAreaScrollPane;
public TableDetails()
{
tableData=null;
initComponents();
setAppearance();
addListeners();
}
private void initComponents()
{
//size 700*610
setLayout(null);

table=new JTable();

scrollPane=new JScrollPane(table);
scrollPane.setBounds(10,1,690,500);
add(scrollPane);

textArea=new JTextArea();
textAreaScrollPane=new JScrollPane(textArea);
textAreaScrollPane.setBounds(10,1,690,500);
add(textAreaScrollPane);


noDataPanel=new JPanel();
noDataPanel.setBounds(10,1,690,500);
noDataPanel.setLayout(null);
label=new JLabel("No Data to Display");
label.setBounds(250,200,400,50);
noDataPanel.add(label);
add(noDataPanel);


actionPanel=new JPanel();
actionPanel.setLayout(null);
actionPanel.setBounds(50,520,610,70);

addButton=new JButton("Add");
addButton.setBounds(10,5,110,60);
actionPanel.add(addButton);

editButton=new JButton("Edit");
editButton.setBounds(130,5,110,60);
actionPanel.add(editButton);

deleteButton=new JButton("Delete");
deleteButton.setBounds(250,5,110,60);
actionPanel.add(deleteButton);

cancelButton=new JButton("X");
cancelButton.setBounds(370,5,110,60);
actionPanel.add(cancelButton);

generateAnnotationClassButton=new JButton("GAC");
generateAnnotationClassButton.setBounds(490,5,110,60);
actionPanel.add(generateAnnotationClassButton);

add(actionPanel);

setViewMode();
setNoDataToDisplay();
}
private void setAppearance()
{
noDataPanel.setBackground(Color.white);
noDataPanel.setBorder(BorderFactory.createLineBorder(new Color(112,112,112)));

Font noDataToDisplayFont=new Font("Verdana",Font.BOLD,16);
label.setFont(noDataToDisplayFont);


actionPanel.setBackground(Color.white);
actionPanel.setBorder(BorderFactory.createLineBorder(new Color(112,112,112)));



scrollPane.setBackground(Color.white);
scrollPane.setBorder(BorderFactory.createLineBorder(new Color(112,112,112)));

Font font=new Font("Verdana",Font.PLAIN,16);

textArea.setFont(font);
textAreaScrollPane.setBorder(BorderFactory.createLineBorder(new Color(112,112,112)));

table.setFont(font);
table.setBackground(Color.white);
table.setRowHeight(30);
Font tableTitleFont=new Font("Verdana",Font.BOLD,16);
table.getTableHeader().setFont(tableTitleFont);

//table.getColumnModel().getColumn(0).setPreferredWidth(100);
//table.getColumnModel().getColumn(1).setPreferredWidth(560);
table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
table.getTableHeader().setResizingAllowed(false);
table.getTableHeader().setReorderingAllowed(false);






}
public void addListeners()
{
addButton.addActionListener(this);
editButton.addActionListener(this);
deleteButton.addActionListener(this);
cancelButton.addActionListener(this);
generateAnnotationClassButton.addActionListener(this);
}
public void setTableName(String name)
{
if(name==null || name.equalsIgnoreCase("Tables"))
{
setNoDataToDisplay();
}
else
{
setViewMode();
setDataInTable(TMORMPanel.tablesMap.get(name.toLowerCase().trim()));
}
}
public void setViewName(String name)
{
if(name==null || name.equalsIgnoreCase("Tables"))
{
setNoDataToDisplay();
}
else
{
this.viewData=TMORMPanel.viewsMap.get(name);
setDataInTable(this.viewData);
}

}
private void setDataInTable(Tables tableData)
{
if(tableData==null)return;
this.tableData=tableData;
noDataPanel.setVisible(false);
scrollPane.setVisible(true);
this.columns=new String[tableData.attributes.size()];

//code to fetch data related to table

String select=tableData.select;
java.util.List<String[]> dataList=new java.util.LinkedList<>();
String data[]=new String[tableData.attributes.size()];
try
{
PreparedStatement ps=TMDB.connection.prepareStatement(select);
ResultSet rs=ps.executeQuery();
int i=0;
i=0;
for(Attribute a:tableData.attributes)
{
this.columns[i]=a.columnName;
i++;
}
while(rs.next())
{
data=new String[tableData.attributes.size()];
i=0;
for(Attribute a:tableData.attributes)
{
data[i]=rs.getString(a.columnName);
i++;
}
dataList.add(data);
}
this.rows=new String[dataList.size()][tableData.attributes.size()];
for(int j=0;j<dataList.size();j++)
{
this.rows[j]=dataList.get(j);
}
dtm=new DefaultTableModel(this.rows,this.columns);
table.setModel(dtm);
}catch(Exception e)
{
e.printStackTrace();
System.out.println(e.getMessage());
}
//code to fetch data related to table ends here
}

public void actionPerformed(ActionEvent e)
{
if(tableData==null && viewData==null)
{
JOptionPane.showMessageDialog(this,"Please select a table or view first");
return;
}
if(e.getSource()==generateAnnotationClassButton)
{
JFileChooser j=new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
 j.setAcceptAllFileFilterUsed(false); 
j.setDialogTitle("Select a .txt file"); 
FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .java file", "java"); 
j.addChoosableFileFilter(restrict); 
int option=j.showOpenDialog(null);
String path="";
if(option==JFileChooser.APPROVE_OPTION)
{
path=j.getSelectedFile().getAbsolutePath();
if(!path.endsWith(".java"))
{
path=null;
}
}
else
{
path=null;
}
System.out.println(path);
if(path==null)
{
JOptionPane.showMessageDialog(this,"select java file");
return;
}
else
{
if(this.currentMode==VIEW_DISPLAY_MODE)
{
generateViewClass(path,j.getSelectedFile().getName());
}
else
{
generateClass(path,j.getSelectedFile().getName());
}
}
}
if(e.getSource()==addButton)
{
if(this.currentMode!=this.ADD_MODE)
{
setAddMode();
}
else
{
String insertString=textArea.getText().trim();
if(insertString==null || insertString.length()==0)
{
textArea.setForeground(Color.red);
textArea.setText("Please enter proper query");
}
try
{
PreparedStatement ps=TMDB.connection.prepareStatement(insertString);
ps.executeUpdate();
setTableName(this.tableData.tableName);
}catch(Exception ee)
{
textArea.setForeground(Color.red);
textArea.setText("Malformed query");
}
}
}
if(e.getSource()==deleteButton)
{
if(this.currentMode!=DELETE_MODE)
{
setDeleteMode();
}
else
{
String deleteString=textArea.getText().trim();
if(deleteString==null || deleteString.length()==0)
{
textArea.setForeground(Color.red);
textArea.setText("Please enter proper query");
}
try
{
PreparedStatement ps=TMDB.connection.prepareStatement(deleteString);
ps.executeUpdate();
setTableName(this.tableData.tableName);
}catch(Exception ee)
{
textArea.setForeground(Color.red);
textArea.setText("Malformed query");
}

}
}
if(e.getSource()==cancelButton)
{
setViewMode();
}
if(e.getSource()==editButton)
{
if(this.currentMode!=EDIT_MODE)setEditMode();
else
{
String updateString=textArea.getText().trim();
if(updateString==null || updateString.length()==0)
{
textArea.setForeground(Color.red);
textArea.setText("Please enter proper query");
}
try
{
PreparedStatement ps=TMDB.connection.prepareStatement(updateString);
ps.executeUpdate();
setTableName(this.tableData.tableName);
}catch(Exception ee)
{
textArea.setForeground(Color.red);
textArea.setText("Malformed query");
}

}
}
}
public void generateClass(String path,String className)
{
File f=new File(path);
if(f.exists())
{
f.delete();
}
className=className.substring(0,className.length()-5);
System.out.println(this.tableData.tableName);
try
{
RandomAccessFile raf=new RandomAccessFile(new File(path),"rw");
raf.writeBytes("import com.thinking.machines.TORMFramework.*;\n");
raf.writeBytes("import com.thinking.machines.TORMFramework.exceptions.*;\n");
raf.writeBytes("import com.thinking.machines.TORMFramework.annotations.*;\n");
raf.writeBytes("@Table(\""+tableData.tableName+"\")\n");
raf.writeBytes("public class "+className+"\n");
raf.writeBytes("{\n");
java.util.Map<String,String> dataTypeMap=new java.util.HashMap<>();
String dataType="";
for(Attribute a:tableData.attributes)
{
if(a.columnType.equalsIgnoreCase("INT"))
{
dataType="int";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("VARCHAR"))
{
dataType="String";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("CHAR"))
{
dataType="String";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("BIGINT"))
{
dataType="long";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("DATE"))
{
dataType="java.sql.Date";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("DATETIME"))
{
dataType="java.sql.Date";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("SMALLINT"))
{
dataType="short";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("TINYINT"))
{
dataType="byte";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("BOOLEAN"))
{
dataType="boolean";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("DECIMAL"))
{
dataType="double";
dataTypeMap.put(a.columnName,dataType);
}
raf.writeBytes("@Column(\""+a.columnName+"\")\n");
raf.writeBytes("private "+dataType+" "+a.columnName+";\n");
}
for(String fieldName:dataTypeMap.keySet())
{
dataType=dataTypeMap.get(fieldName);
raf.writeBytes("public void set"+(fieldName.substring(0,1).toUpperCase()+fieldName.substring(1))+"("+dataType+" "+fieldName+")\n");
raf.writeBytes("{\n");
raf.writeBytes("this."+fieldName+"="+fieldName+";\n");
raf.writeBytes("}\n");

raf.writeBytes("public "+dataType+" get"+(fieldName.substring(0,1).toUpperCase()+fieldName.substring(1))+"()\n");
raf.writeBytes("{\n");
raf.writeBytes("return this."+fieldName+";\n");
raf.writeBytes("}\n");
}
raf.writeBytes("}\n");
raf.close();
JOptionPane.showMessageDialog(this,"class "+className+" generated at "+path);
}catch(Exception e)
{
e.printStackTrace();
}
}
public void generateViewClass(String path,String className)
{
File f=new File(path);
if(f.exists())
{
f.delete();
}
className=className.substring(0,className.length()-5);
System.out.println(this.viewData.tableName);
try
{
RandomAccessFile raf=new RandomAccessFile(new File(path),"rw");
raf.writeBytes("import com.thinking.machines.TORMFramework.*;\n");
raf.writeBytes("import com.thinking.machines.TORMFramework.exceptions.*;\n");
raf.writeBytes("import com.thinking.machines.TORMFramework.annotations.*;\n");
raf.writeBytes("@View(\""+tableData.tableName+"\")\n");
raf.writeBytes("public class "+className+"\n");
raf.writeBytes("{\n");
java.util.Map<String,String> dataTypeMap=new java.util.HashMap<>();
String dataType="";
for(Attribute a:tableData.attributes)
{
if(a.columnType.equalsIgnoreCase("INT"))
{
dataType="int";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("VARCHAR"))
{
dataType="String";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("CHAR"))
{
dataType="String";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("BIGINT"))
{
dataType="long";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("DATE"))
{
dataType="java.sql.Date";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("DATETIME"))
{
dataType="java.sql.Date";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("SMALLINT"))
{
dataType="short";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("TINYINT"))
{
dataType="byte";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("BOOLEAN"))
{
dataType="boolean";
dataTypeMap.put(a.columnName,dataType);
}
if(a.columnType.equalsIgnoreCase("DECIMAL"))
{
dataType="double";
dataTypeMap.put(a.columnName,dataType);
}
raf.writeBytes("@Column(\""+a.columnName+"\")\n");
raf.writeBytes("private "+dataType+" "+a.columnName+";\n");
}
for(String fieldName:dataTypeMap.keySet())
{
dataType=dataTypeMap.get(fieldName);
raf.writeBytes("public void set"+(fieldName.substring(0,1).toUpperCase()+fieldName.substring(1))+"("+dataType+" "+fieldName+")\n");
raf.writeBytes("{\n");
raf.writeBytes("this."+fieldName+"="+fieldName+";\n");
raf.writeBytes("}\n");

raf.writeBytes("public "+dataType+" get"+(fieldName.substring(0,1).toUpperCase()+fieldName.substring(1))+"()\n");
raf.writeBytes("{\n");
raf.writeBytes("return this."+fieldName+";\n");
raf.writeBytes("}\n");
}
raf.writeBytes("}\n");
raf.close();
JOptionPane.showMessageDialog(this,"class "+className+" generated at "+path);
}catch(Exception e)
{
e.printStackTrace();
}
}

public void setViewMode()
{
this.currentMode=this.VIEW_MODE;
cancelButton.setEnabled(false);
scrollPane.setVisible(true);
noDataPanel.setVisible(false);
textAreaScrollPane.setVisible(false);
editButton.setText("Edit");
editButton.setEnabled(true);
deleteButton.setEnabled(true);
addButton.setText("Add");
addButton.setEnabled(true);
generateAnnotationClassButton.setEnabled(true);
TMORMPanel.this.tablesTree.setEnabled(true);
}
public void setAddMode()
{
if(this.currentMode==ADD_MODE)return;
this.currentMode=this.ADD_MODE;
editButton.setEnabled(false);
deleteButton.setEnabled(false);
generateAnnotationClassButton.setEnabled(false);
cancelButton.setEnabled(true);
textArea.setForeground(Color.black);
textArea.setText("\n"+this.tableData.insert);
textAreaScrollPane.setVisible(true);
scrollPane.setVisible(false);
noDataPanel.setVisible(false);
addButton.setText("save");
TMORMPanel.this.tablesTree.setEnabled(false);
}
public void setEditMode()
{
if(this.currentMode==EDIT_MODE)return;
this.currentMode=this.EDIT_MODE;
editButton.setText("save");
editButton.setEnabled(true);
deleteButton.setEnabled(false);
generateAnnotationClassButton.setEnabled(false);
cancelButton.setEnabled(true);
textArea.setForeground(Color.black);
textArea.setText("\n"+this.tableData.update);
textAreaScrollPane.setVisible(true);
scrollPane.setVisible(false);
noDataPanel.setVisible(false);
addButton.setEnabled(false);
TMORMPanel.this.tablesTree.setEnabled(false);
}
public void setDeleteMode()
{
if(this.currentMode==DELETE_MODE)return;
this.currentMode=this.DELETE_MODE;
editButton.setEnabled(false);
deleteButton.setEnabled(true);
generateAnnotationClassButton.setEnabled(false);
cancelButton.setEnabled(true);
textArea.setForeground(Color.black);
textArea.setText("\n"+this.tableData.delete);
textAreaScrollPane.setVisible(true);
scrollPane.setVisible(false);
noDataPanel.setVisible(false);
addButton.setEnabled(false);
TMORMPanel.this.tablesTree.setEnabled(false);
}

public void setNoDataToDisplay()
{
textAreaScrollPane.setVisible(false);
noDataPanel.setVisible(true);
scrollPane.setVisible(false);
}
public void setActionPanelEnabled(boolean flag)
{
if(flag==false)
{
addButton.setEnabled(false);
cancelButton.setEnabled(false);
editButton.setEnabled(false);
deleteButton.setEnabled(false);
this.currentMode=VIEW_DISPLAY_MODE;
}
else
{
setViewMode();
}
}
}//inner class ends
}
