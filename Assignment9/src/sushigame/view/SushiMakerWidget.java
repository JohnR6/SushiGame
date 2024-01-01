package sushigame.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import comp401.sushi.*;
import sushigame.model.Chef;
import sushigame.game.*;
import sushigame.view.*;

public class SushiMakerWidget extends JPanel implements ActionListener {
	
	private JPanel display;
	enum SushiType {NIGIRI, SASHIMI, ROLL}
	SushiType s;
	enum MeatType {TUNA, SALMON, EEL, CRAB, SHRIMP}
	enum VegType {AVOCADO, RICE, SEAWEED}
	private Plate sushi;
	private JSlider posSlider;
	private JSlider priceSlider;
	JTextField avocado_field, rice_field, seaweed_field, tuna_field, salmon_field,
				eel_field, crab_field, shrimp_field;
	Ingredient[] roll2 = {new Avocado(), new Rice(), new Seaweed(), new Tuna(), 
						new Salmon(), new Eel(), new Crab(), new Shrimp()};
	double total;
	double[] ings = new double[8];
	Plate.Color color = null;
	MeatType meat = null;
	PlayerChefView temp;
	IngredientPortion[] roll;

	public SushiMakerWidget(SushiType s, PlayerChefView p) {

		setLayout(new BorderLayout());
		this.temp = p;
		display = new JPanel();
		display.setLayout(new GridLayout(2,1));
		this.s = s;

		switch(s) {
		case NIGIRI:
			sushi = makeNotRoll(s);
			break;
		case SASHIMI:
			sushi = makeNotRoll(s);
			break;
		case ROLL:
			sushi = makeNotRoll(s);
			break;
		}
	}

	public Plate makeNotRoll(SushiType s) {
		JPanel colorPanel = new JPanel();
		Plate out = null;
		boolean finished = false;
		JPanel center_panel = new JPanel();
		center_panel.setLayout(new GridLayout(0,1));
		JButton red_button = new JButton("Red");
		red_button.addActionListener(this);
		red_button.setActionCommand("red_plate");
		JPanel color_panel = new JPanel();
		color_panel.add(new JLabel("Color: "));
		color_panel.add(red_button);

		JButton green_button = new JButton("Green");
		green_button.setActionCommand("green_plate");
		green_button.addActionListener(this);
		color_panel.add(green_button);

		JButton blue_button = new JButton("Blue");
		blue_button.setActionCommand("blue_plate");
		blue_button.addActionListener(this);
		color_panel.add(blue_button);

		JButton gold_button = new JButton("Gold");
		gold_button.setActionCommand("gold_plate");
		gold_button.addActionListener(this);
		color_panel.add(gold_button);
		center_panel.add(color_panel);


		posSlider = new JSlider(0, 19, 0);
		posSlider.setPaintTicks(true);
		posSlider.setSnapToTicks(true);
		posSlider.setMajorTickSpacing(1);
		center_panel.add(new JLabel("Position: "));
		center_panel.add(posSlider);

		priceSlider = new JSlider(5, 15, 5);
		priceSlider.setPaintTicks(true);
		priceSlider.setSnapToTicks(true);
		priceSlider.setPaintLabels(true);
		priceSlider.setMajorTickSpacing(1);
		center_panel.add(new JLabel("Price (Gold only): "));
		center_panel.add(priceSlider);

		if(!s.equals(SushiType.ROLL)) {
			JButton tuna_button = new JButton("Tuna");
			tuna_button.addActionListener(this);
			tuna_button.setActionCommand("tuna_plate");
			JPanel meat_panel = new JPanel();
			meat_panel.add(new JLabel("Meat: "));
			meat_panel.add(tuna_button);

			JButton salmon_button = new JButton("Salmon");
			salmon_button.addActionListener(this);
			salmon_button.setActionCommand("salmon_plate");
			meat_panel.add(salmon_button);

			JButton eel_button = new JButton("Eel");
			eel_button.addActionListener(this);
			eel_button.setActionCommand("eel_plate");
			meat_panel.add(eel_button);

			JButton crab_button = new JButton("Crab");
			crab_button.addActionListener(this);
			crab_button.setActionCommand("crab_plate");
			meat_panel.add(crab_button);

			JButton shrimp_button = new JButton("Shrimp");
			shrimp_button.addActionListener(this);
			shrimp_button.setActionCommand("shrimp_plate");
			meat_panel.add(shrimp_button);
			center_panel.add(meat_panel);
		} else if(s.equals(SushiType.ROLL)) {
			tuna_field = new JTextField(5);
			JPanel roll_panel = new JPanel();
			roll_panel.add(new JLabel("Tuna: "));	
			roll_panel.add(tuna_field);
			
			salmon_field = new JTextField(5);
			roll_panel.add(new JLabel("Salmon: "));	
			roll_panel.add(salmon_field);
			
			eel_field = new JTextField(5);
			roll_panel.add(new JLabel("Eel: "));	
			roll_panel.add(eel_field);
			
			crab_field = new JTextField(5);
			roll_panel.add(new JLabel("Crab: "));	
			roll_panel.add(crab_field);
			
			shrimp_field = new JTextField(5);
			roll_panel.add(new JLabel("Shrimp: "));	
			roll_panel.add(shrimp_field);
			center_panel.add(roll_panel);
			
			avocado_field = new JTextField(5);
			JPanel veg_panel = new JPanel();
			veg_panel.add(new JLabel("Avocado: "));	
			veg_panel.add(avocado_field);
			
			rice_field = new JTextField(5);
			veg_panel.add(new JLabel("Rice: "));	
			veg_panel.add(rice_field);
			
			seaweed_field = new JTextField(5);
			veg_panel.add(new JLabel("Seaweed: "));	
			veg_panel.add(seaweed_field);
			center_panel.add(veg_panel);
		}
		
		add(new JScrollPane(center_panel), BorderLayout.CENTER);
		
		JButton done_button = new JButton("Done");
		done_button.setActionCommand("done");
		done_button.addActionListener(this);
		JPanel done_panel = new JPanel();
		done_panel.add(done_button);
		add(done_panel, BorderLayout.SOUTH);
		
		return out;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		int pos = posSlider.getValue();
		int price = priceSlider.getValue();
		if(s.equals(SushiType.ROLL)) {
			if(avocado_field.getText() != null && avocado_field.getText().length() > 0) {ings[0] = Double.parseDouble(avocado_field.getText());}
			if(rice_field.getText() != null && rice_field.getText().length() > 0) {ings[1] = Double.parseDouble(rice_field.getText());}
			if(seaweed_field.getText() != null && seaweed_field.getText().length() > 0) {ings[2] = Double.parseDouble(seaweed_field.getText());}
			if(tuna_field.getText() != null && tuna_field.getText().length() > 0) {ings[3] = Double.parseDouble(tuna_field.getText());}
			if(salmon_field.getText() != null && salmon_field.getText().length() > 0) {ings[4] = Double.parseDouble(salmon_field.getText());}
			if(eel_field.getText() != null && eel_field.getText().length() > 0) {ings[5] = Double.parseDouble(eel_field.getText());}
			if(crab_field.getText() != null && crab_field.getText().length() > 0) {ings[6] = Double.parseDouble(crab_field.getText());}
			if(shrimp_field.getText() != null && shrimp_field.getText().length() > 0) {ings[7] = Double.parseDouble(shrimp_field.getText());}
			
			total = ings[0] + ings[1] + ings[2] + ings[3] + ings[4] + ings[5] + ings[6] + ings[7];
		}
		switch(cmd) {
		case "red_plate":
			color = Plate.Color.RED;
			break;
		case "green_plate":
			color = Plate.Color.GREEN;
			break;
		case "blue_plate":
			color = Plate.Color.BLUE;
			break;
		case "gold_plate":
			color = Plate.Color.GOLD;
			break;
		case "tuna_plate":
			meat = MeatType.TUNA;
			break;
		case "salmon_plate":
			meat = MeatType.SALMON;
			break;
		case "eel_plate":
			meat = MeatType.EEL;
			break;
		case "crab_plate":
			meat = MeatType.CRAB;
			break;
		case "shrimp_plate":
			meat = MeatType.SHRIMP;
			break;
		case "done":
			if(color != null && meat != null) {
				switch(color) {
				case RED:
					if(this.s == SushiType.SASHIMI) {
						Sushi su = new Sashimi(Sashimi.SashimiType.values()[meat.ordinal()]);
						temp.makeRedPlateRequest(su, pos);
					} else if(this.s == SushiType.NIGIRI) {
						Sushi su = new Nigiri(Nigiri.NigiriType.values()[meat.ordinal()]);
						temp.makeRedPlateRequest(su, pos);
					} else if(this.s == SushiType.ROLL) {
						
					}
					break;
				case BLUE:
					if(this.s == SushiType.SASHIMI) {
						Sushi su = new Sashimi(Sashimi.SashimiType.values()[meat.ordinal()]);
						temp.makeBluePlateRequest(su, pos);
					}else if(this.s == SushiType.NIGIRI) {
						Sushi su = new Nigiri(Nigiri.NigiriType.values()[meat.ordinal()]);
						temp.makeBluePlateRequest(su, pos);
					}
					break;
				case GREEN:
					if(this.s == SushiType.SASHIMI) {
						Sushi su = new Sashimi(Sashimi.SashimiType.values()[meat.ordinal()]);
						temp.makeGreenPlateRequest(su, pos);
					}else if(this.s == SushiType.NIGIRI) {
						Sushi su = new Nigiri(Nigiri.NigiriType.values()[meat.ordinal()]);
						temp.makeGreenPlateRequest(su, pos);
					}
					break;
				case GOLD:
					if(this.s == SushiType.SASHIMI) {
						Sushi su = new Sashimi(Sashimi.SashimiType.values()[meat.ordinal()]);
						temp.makeGoldPlateRequest(su, pos, (double)price);
					}else if(this.s == SushiType.NIGIRI) {
						Sushi su = new Nigiri(Nigiri.NigiriType.values()[meat.ordinal()]);
						temp.makeGoldPlateRequest(su, pos, price);
					}
					break;
				
				}
			} else if(color != null && total <= 1.5 && this.s == SushiType.ROLL) {
				int counter = 0, counter2 = 0;
				for(int i = 0; i < 8; i++) {
					if(ings[i] > 0) {
						counter++;
						roll2[i] = null;
					}
				}
				roll = new IngredientPortion[counter];
				for(int i = 0; i < 8; i++) {
					if(roll2[i] != null) {
						//roll[counter2] = new IngredientPortion()
					}
				}
				if(ings[0] > 0 && counter > counter2) {
					roll[counter2] = new AvocadoPortion(ings[0]);
					counter2++;
				}
				if(ings[1] > 0 && counter > counter2) {
					roll[counter2] = new RicePortion(ings[1]);
					counter2++;
				}
				if(ings[2] > 0 && counter > counter2) {
					roll[counter2] = new SeaweedPortion(ings[2]);
					counter2++;
				}
				if(ings[3] > 0 && counter > counter2) {
					roll[counter2] = new TunaPortion(ings[3]);
					counter2++;
				}
				if(ings[4] > 0 && counter > counter2) {
					roll[counter2] = new SalmonPortion(ings[4]);
					counter2++;
				}
				if(ings[5] > 0 && counter > counter2) {
					roll[counter2] = new EelPortion(ings[5]);
					counter2++;
				}
				if(ings[6] > 0 && counter > counter2) {
					roll[counter2] = new CrabPortion(ings[6]);
					counter2++;
				}
				if(ings[7] > 0 && counter > counter2) {
					roll[counter2] = new ShrimpPortion(ings[7]);
					counter2++;
				}
				Sushi su = new Roll("Custom Roll", roll);
				
				switch(color) {
				case RED:
					temp.makeRedPlateRequest(su, pos);
					break;
				case BLUE:
					temp.makeBluePlateRequest(su, pos);
					break;
				case GREEN:
					temp.makeGreenPlateRequest(su, pos);
					break;
				case GOLD:
					temp.makeGoldPlateRequest(su, pos, price);
					break;
				
				}
			}
		}

	}

}