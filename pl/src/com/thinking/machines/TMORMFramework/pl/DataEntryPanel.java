package com.thinking.machines.TMORMFramework.pl;
import javax.swing.*;
import java.awt.*;
import com.google.gson.*;
import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.math.*;
import javax.swing.filechooser.*;
import java.io.*;
public class DataEntryPanel extends JPanel implements ActionListener
{
private JLabel moduleTitle;
private JLabel connectionURLCaptionLabel;
private JTextField connectionURLTextField;
private JLabel connectionURLLabel;
private JLabel usernameCaptionLabel;
private JTextField usernameTextField;
private JLabel usernameLabel;
private JLabel passwordCaptionLabel;
private JTextField passwordTextField;
private JLabel passwordLabel;
private JButton saveButton;
private TMORMFrame frame;
private TMORMPanel tmOrmPanel;
public DataEntryPanel(TMORMFrame frame,TMORMPanel tmOrmPanel)
{
this.tmOrmPanel=tmOrmPanel;
this.frame=frame;
initComponents();
setAppearance();
addListeners();
}
private void initComponents()
{
moduleTitle=new JLabel("Database Information");
connectionURLCaptionLabel=new JLabel("Connection URL: ");
connectionURLTextField=new JTextField();
connectionURLLabel=new JLabel("");

usernameCaptionLabel=new JLabel("Username: ");
usernameTextField=new JTextField();
usernameLabel=new JLabel("");

passwordCaptionLabel=new JLabel("Password: ");
passwordTextField=new JTextField();
passwordLabel=new JLabel("");

saveButton=new JButton("save");
setLayout(null);
int lm=5;
int tm=5;
moduleTitle.setBounds(lm+5,tm,400,50);
connectionURLCaptionLabel.setBounds(lm+5,tm+55,100,30);
connectionURLTextField.setBounds(lm+5+100,tm+55,300,30);

usernameCaptionLabel.setBounds(lm+5,tm+55+40,100,30);
usernameTextField.setBounds(lm+5+100,tm+55+40,300,30);

passwordCaptionLabel.setBounds(lm+5,tm+55+80,100,30);
passwordTextField.setBounds(lm+5+100,tm+55+80,300,30);

saveButton.setBounds(lm+5+100,tm+55+40+80,90,30);
add(moduleTitle);
add(connectionURLCaptionLabel);
add(connectionURLTextField);
add(usernameCaptionLabel);
add(usernameTextField);
add(passwordCaptionLabel);
add(passwordTextField);
add(saveButton);
}
private void setAppearance()
{
Font moduleTitleFont=new Font("Verdana",Font.BOLD,20);
moduleTitle.setFont(moduleTitleFont);
Font font=new Font("Verdana",Font.PLAIN,16);
setBorder(BorderFactory.createLineBorder(Color.gray));
connectionURLCaptionLabel.setFont(font);
connectionURLTextField.setFont(font);
usernameCaptionLabel.setFont(font);
usernameTextField.setFont(font);
passwordCaptionLabel.setFont(font);
passwordTextField.setFont(font);
}
private void addListeners()
{
saveButton.addActionListener(this);
}
public void actionPerformed(ActionEvent ev)
{
try
{
if(ev.getSource()==saveButton)
{
String connectionURL=connectionURLTextField.getText().trim();
if(connectionURL.length()==0)
{
JOptionPane.showMessageDialog(this,"Connection URL required");
connectionURLTextField.requestFocus();
return;
}
String username=usernameTextField.getText().trim();
if(username.length()==0)
{
JOptionPane.showMessageDialog(this,"username required");
usernameTextField.requestFocus();
return;
}
String password=passwordTextField.getText().trim();
if(password.length()==0)
{
JOptionPane.showMessageDialog(this,"password required");
passwordTextField.requestFocus();
return;
}
DBData dbdata=new DBData();
dbdata.setConnectionURL(connectionURL);
dbdata.setUsername(username);
dbdata.setPassword(password);
Gson gson=new Gson();
String jsonString=gson.toJson(dbdata);
RandomAccessFile raf=new RandomAccessFile(new File("c:/TMORMFramework/dbconf.json"),"rw");
raf.writeBytes(jsonString);
raf.close();
JOptionPane.showMessageDialog(this,"Settings saved.Chnages will be visible when you restart the app");
setVisible(false);
if(this.tmOrmPanel!=null)
{
tmOrmPanel.setVisible(true);
}
}
}catch(Exception e)
{
}
}
}