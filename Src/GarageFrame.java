import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GarageFrame extends JFrame implements ActionListener, MouseListener {
	JButton[] buttons = new JButton[3];
	String[] buttonsNames = {"Add", "Remove", "Save"};
	JTextField[] carDataFields = new JTextField[6];
	String[] carDataFieldsLabels = {
			"Num","Company","Model","Year","License Number", "Color", "Price"};
	JTable dailyDataTable;
	DefaultTableModel model;
	int[] tableColSizes= {40,100,80,60,135,80,86};
	DayList entireList;
	int carsCounter;
	JMenuItem[] modifyItems = new JMenuItem[6];
	JPopupMenu popupMenu;
	JMenu modifyMenu;
	
	GarageFrame(){
		super("Mosah Zion");
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//deal with textfields and labels
		JPanel pDataFields = new JPanel(new GridLayout(6,2,5,5));
		for(int i=0;i<6;i++) {
			carDataFields[i]=new JTextField(15);
			JLabel label = new JLabel(carDataFieldsLabels[i+1]);
			pDataFields.add(label);
			pDataFields.add(carDataFields[i]);
		}
		c.fill=GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		c.ipadx=0;
		c.ipady=35;
		c.weightx=1.0;
		c.weighty=1.0;
		c.gridy=0;
		c.gridx=0;
		this.add(pDataFields,c);
		
		//deal with buttons
		int i=0;
		//deal with buttons line 1
		JPanel cButtons1 = new JPanel(new FlowLayout());
		while(i<2) {
			buttons[i]=new JButton(buttonsNames[i]);
			buttons[i].addActionListener(this);
			cButtons1.add(buttons[i]);
			i++;
		}
		c.fill=GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.ipadx=0;
		c.ipady=0;
		c.weightx=1.0;
		c.weighty=1.0;
		c.gridy=1;
		c.gridx=0;
		this.add(cButtons1,c);
		
		//deal with buttons line 2
		JPanel cButtons2=new JPanel(new FlowLayout());
		while(i<buttons.length) {
			buttons[i]=new JButton(buttonsNames[i]);
			buttons[i].addActionListener(this);
			cButtons2.add(buttons[i]);
			i++;
		}
		c.fill=GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx=1.0;
		c.weighty=1.0;
		c.gridy=2;
		c.gridx=0;
		this.add(cButtons2,c);
		
		//deal with the table
		model=new DefaultTableModel(null, carDataFieldsLabels);
		dailyDataTable=new JTable(model);
		dailyDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for(int j=0;j<tableColSizes.length;j++) {
			dailyDataTable.getColumnModel().getColumn(j).setMinWidth(tableColSizes[j]);
			dailyDataTable.getColumnModel().getColumn(j).setMaxWidth(tableColSizes[j]);
			dailyDataTable.getColumnModel().getColumn(j).setPreferredWidth(tableColSizes[j]);
		}
		dailyDataTable.getTableHeader().setReorderingAllowed(false);
		dailyDataTable.setSelectionBackground(Color.cyan);
		popupMenu = new JPopupMenu();
		modifyMenu = new JMenu("Modify");
		for(int j=0;j<modifyItems.length;j++) {
			modifyItems[j]=new JMenuItem(carDataFieldsLabels[j+1]);
			modifyItems[j].addActionListener(this);
			modifyMenu.add(modifyItems[j]);
		}
		popupMenu.add(modifyMenu);
		dailyDataTable.setComponentPopupMenu(popupMenu);
		JScrollPane scrollPane = new JScrollPane(dailyDataTable);
		c.fill=GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_END;
		c.weightx=1.0;
		c.weighty=1.0;
		c.ipady=300;
		c.gridy=3;
		c.gridx=0;
		this.add(scrollPane,c);
		
		entireList = new DayList();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int indexSelected = dailyDataTable.getSelectedRow();
		
		//add
		if(e.getSource()==buttons[0]) {
			for(int i=0;i<carDataFields.length-1;i++) {
				String temp=carDataFields[i].getText();
				if(temp=="") {
					JOptionPane.showMessageDialog(this, "at least 1 field is blank" ,
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			String company=carDataFields[0].getText();
			String companyRE="[A-z]+";
			company=this.checkRE(company, companyRE, carDataFieldsLabels[1]);
			
			String carmodel=carDataFields[1].getText();
			String modelRE="([A-z]+([-]?[0-9]+)?)|([1-9][0-9]*)";
			carmodel=this.checkRE(carmodel, modelRE, carDataFieldsLabels[2]);
			
			String year=carDataFields[2].getText(); 
			String yearRE="[19|20][0-9][0-9]"; //1900-2099 (not perfect)
			year=this.checkRE(year, yearRE, carDataFieldsLabels[3]);
			
			//needs some RE checkings on the next 3 Strings
			String color=carDataFields[4].getText(); 
			String licenseNum=carDataFields[3].getText();
			String price=carDataFields[5].getText();
			
			Car car = new Car(company, carmodel, year, color, licenseNum);
			entireList.cars.add(car);
			model.addRow(new Object[] {
					++carsCounter,
					car.company,
					car.model,
					car.yearCreated,
					car.licenseNumber,
					car.color,
					price});
			clean();
		}
	
		//remove
		else if(e.getSource()==buttons[1]) {
			if(indexSelected==-1) {
				JOptionPane.showMessageDialog(this, "no object was selected to remove\\modify" ,
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			else{
				entireList.cars.remove(indexSelected);
				model.removeRow(indexSelected);
			}
		}
		
		//save to file
		else if(e.getSource()==buttons[2]) {
			try {
				String fileName="today.csv";
				String pathToExportTo="C:\\Users\\Aviel\\Desktop\\java projects\\Garage\\Data";
		        FileWriter csv = new FileWriter(new File(pathToExportTo,fileName));

		        for (int i = 0; i < model.getColumnCount(); i++) {
		            csv.write(model.getColumnName(i) + ",");
		        }

		        csv.write("\n");

		        for (int i = 0; i < model.getRowCount(); i++) {
		            for (int j = 0; j < model.getColumnCount(); j++) {
		                csv.write(model.getValueAt(i, j).toString() + ",");
		            }
		            csv.write("\n");
		        }

		        csv.close();
		    } catch (IOException e1) {
		        e1.printStackTrace();
		    }
			//JOptionPane.sho
		}
		
		//modify menu item - "Company"
		else if(e.getSource()==modifyItems[0]) {
			String newData=JOptionPane.showInputDialog(this, "Company: ", "modifying", JOptionPane.PLAIN_MESSAGE);
			//model.setValueAt(newData, indexSelected, 1);
			entireList.cars.get(indexSelected).company=newData;
		}
		//modify menu item - "Model"
		else if(e.getSource()==modifyItems[1]) {
			String newData=JOptionPane.showInputDialog(this, "Model: ", "modifying", JOptionPane.PLAIN_MESSAGE);
			model.setValueAt(newData, indexSelected, 2);
			entireList.cars.get(indexSelected).model=newData;
		}
		//modify menu item - "Year"
		else if(e.getSource()==modifyItems[2]) {
			String newData=JOptionPane.showInputDialog(this, "Year: ", "modifying", JOptionPane.PLAIN_MESSAGE);
			model.setValueAt(newData, indexSelected, 3);
			entireList.cars.get(indexSelected).yearCreated=newData;
		}
		//modify menu item - "License Number"
		else if(e.getSource()==modifyItems[3]) {
			String newData=JOptionPane.showInputDialog(this, "License Number: ", "modifying", JOptionPane.PLAIN_MESSAGE);
			model.setValueAt(newData, indexSelected, 4);
			entireList.cars.get(indexSelected).licenseNumber=newData;
		}
		//modify menu item - "Color"
		else if(e.getSource()==modifyItems[4]) {
			String newData=JOptionPane.showInputDialog(this, "Color: ", "modifying", JOptionPane.PLAIN_MESSAGE);
			model.setValueAt(newData, indexSelected, 5);
			entireList.cars.get(indexSelected).color=newData;
		}
		//modify menu item - "Price"
		else if(e.getSource()==modifyItems[5]) {
			String newData=JOptionPane.showInputDialog(this, "Price: ", "modifying", JOptionPane.PLAIN_MESSAGE);
			model.setValueAt(newData, indexSelected, 6);
			//entireList.cars.get(indexSelected).price=newData;
		}
	}

	public void clean() {
		for(JTextField temp:carDataFields) {
			temp.setText(null);
		}
	}
	
	public String checkRE(String customStr, String customRE, String datafieldLabel) {
		if(!customStr.matches(customRE)) {
			customStr=JOptionPane.showInputDialog(this, "Illegal "+datafieldLabel+", try again:",
					datafieldLabel+" not Legit", JOptionPane.ERROR_MESSAGE);
			while(!customStr.matches(customRE)) {
				customStr=JOptionPane.showInputDialog(this, "Illegal "+datafieldLabel+", try again:",
						datafieldLabel+" not Legit", JOptionPane.ERROR_MESSAGE);
			}
		}
		return customStr;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
