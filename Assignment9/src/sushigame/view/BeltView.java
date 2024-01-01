package sushigame.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import comp401.sushi.*;
import sushigame.model.Belt;
import sushigame.model.BeltEvent;
import sushigame.model.BeltObserver;

public class BeltView extends JPanel implements BeltObserver,ActionListener {

	private Belt belt;
	private JLabel[] belt_labels;
	private JButton help;
	private JFrame frame;
	private int counter;
	
	public BeltView(Belt b) {
		this.belt = b;
		belt.registerBeltObserver(this);
		setLayout(new GridLayout(belt.getSize(), 2));
		belt_labels = new JLabel[belt.getSize()];
		for (int i = 0; i < belt.getSize(); i++) {
			JLabel plabel = new JLabel("");
			plabel.setMinimumSize(new Dimension(300, 20));
			plabel.setPreferredSize(new Dimension(300, 20));
			plabel.setOpaque(true);
			plabel.setBackground(Color.GRAY);
			add(plabel);
			belt_labels[i] = plabel;
			
			help = new JButton("?");
			help.setActionCommand("get_help" + Integer.toString(i));
			help.addActionListener(this);
			add(help);
		}
		refresh();
	}

	@Override
	public void handleBeltEvent(BeltEvent e) {	
		refresh();
	}

	private void refresh() {
		for (int i=0; i<belt.getSize(); i++) {
			Plate p = belt.getPlateAtPosition(i);
			JLabel plabel = belt_labels[i];

			if (p == null) {
				plabel.setText("");
				plabel.setBackground(Color.GRAY);
			} else {
				//prints the Chef's name, whether the sushi is Nigiri/Sashimi/Roll, and the age
				plabel.setText(p.getChef().getName() + " | " + p.getContents().getName() 
								+ " | " + belt.getAgeOfPlateAtPosition(i));
				
				switch (p.getColor()) {
				case RED:
					plabel.setBackground(Color.RED); break;
				case GREEN:
					plabel.setBackground(Color.GREEN); break;
				case BLUE:
					plabel.setBackground(Color.BLUE); break;
				case GOLD:
					plabel.setBackground(Color.YELLOW); break;
				}
			}
			
			
			
		}
	}

	private String printRoll(IngredientPortion[] temp) {
		String out = "";
		for(int i = 0; i < temp.length; i++) {
			out += temp[i].getName() + "  " + temp[i].getAmount() + "\n";
		}
		return out;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < belt.getSize(); i++) {
			if(e.getActionCommand().equals("get_help" + Integer.toString(i))) {
				if(belt.getPlateAtPosition(i).getContents() instanceof Roll) {
					JOptionPane.showMessageDialog(frame, 
							belt.getPlateAtPosition(i).getColor() + " Plate\n" +
							belt.getPlateAtPosition(i).getContents().getName() + "\n" +
							"Made By " + belt.getPlateAtPosition(i).getChef().getName() + "\n" +
							"Age: " + belt.getAgeOfPlateAtPosition(i) + "\n" +
							this.printRoll(belt.getPlateAtPosition(i).getContents().getIngredients()));
				}
				else {
					JOptionPane.showMessageDialog(frame, 
							belt.getPlateAtPosition(i).getColor() + " Plate\n" +
							belt.getPlateAtPosition(i).getContents().getName() + "\n" +
							"Made By " + belt.getPlateAtPosition(i).getChef().getName() + "\n" +
							"Age: " + belt.getAgeOfPlateAtPosition(i));
				}
				
			}
		}
	}
	
	
}
