package com.thinking.machines.TMORMFramework.pl;
import com.thinking.machines.TMORMFramework.pl.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import com.google.gson.*;
public class TMORMFrame extends JFrame implements ActionListener
{
private JMenuBar mb;
private JMenu menu;
private JMenuItem dbConfig;
private Container container;
private TMORMPanel tmOrmPanel;
private DataEntryPanel dataEntryPanel;
public TMORMFrame()
{
initComponents();
setAppearance();
addListeners();
}
public void initComponents()
{
container=getContentPane();
container.setLayout(null);
File f=new File("c:/TMORMFramework/dbconf.json");
if(f.exists())
{
tmOrmPanel=new TMORMPanel();
tmOrmPanel.setBounds(1,1,1000,750);
container.add(tmOrmPanel);
}
else
{
dataEntryPanel=new DataEntryPanel(this,null);
dataEntryPanel.setBounds(1,1,682,608);
container.add(dataEntryPanel);
}

dbConfig=new JMenuItem("chnage database configuration");
menu=new JMenu("options");
menu.add(dbConfig);
mb=new JMenuBar();
mb.add(menu);
setJMenuBar(mb);



setSize(1000,750);
Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();
setLocation(dimension.width/2-getWidth()/2,dimension.height/2-getHeight()/2);
setDefaultCloseOperation(EXIT_ON_CLOSE);
setVisible(true);
}
public void setAppearance()
{
setTitle("TMORM Framework");
//ImageIcon appIcon=new ImageIcon("app.png");
//setIconImage(appIcon.getImage());
//itemPanel.setBorder(BorderFactory.createLineBorder(new Color(112,112,112)));
}
public void addListeners()
{
dbConfig.addActionListener(this);
}
public void actionPerformed(ActionEvent e)
{
tmOrmPanel.setVisible(false);
dataEntryPanel=new DataEntryPanel(this,tmOrmPanel);
dataEntryPanel.setBounds(1,1,682,608);
container.add(dataEntryPanel);
}
}