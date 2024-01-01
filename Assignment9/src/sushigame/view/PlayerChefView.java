package sushigame.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import comp401.sushi.*;

public class PlayerChefView extends JPanel implements ActionListener {

	private static List<ChefViewListener> listeners;
	private Sushi new_roll;
	private Sushi new_sashimi;
	private Sushi new_nigiri;
	private int belt_size;
	
	public PlayerChefView(int belt_size) {
		this.belt_size = belt_size;
		listeners = new ArrayList<ChefViewListener>();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JButton sashimi_button = new JButton("Make plate of sashimi");
		sashimi_button.setActionCommand("new_sashimi");
		sashimi_button.addActionListener(this);
		add(sashimi_button);

		JButton nigiri_button = new JButton("Make plate of nigiri");
		nigiri_button.setActionCommand("new_nigiri");
		nigiri_button.addActionListener(this);
		add(nigiri_button);

		JButton roll_button = new JButton("Make roll");
		roll_button.setActionCommand("new_roll");
		roll_button.addActionListener(this);
		add(roll_button);

//		kmp_roll = new Roll("KMP Roll", new IngredientPortion[] {new EelPortion(1.0), new AvocadoPortion(0.5), new SeaweedPortion(0.2)});
//		crab_sashimi = new Sashimi(Sashimi.SashimiType.CRAB);
//		eel_nigiri = new Nigiri(Nigiri.NigiriType.EEL);
	}

	public void registerChefListener(ChefViewListener cl) {
		listeners.add(cl);
	}
	
	private void makeNotRollRequest(SushiMakerWidget.SushiType s) {
		JFrame sashimiMaker = new JFrame();
		sashimiMaker.setTitle("Make New Sashimi");
		sashimiMaker.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		SushiMakerWidget sushiMakerView = new SushiMakerWidget(s, this);
		sashimiMaker.setContentPane(sushiMakerView);
		
		sashimiMaker.pack();
		sashimiMaker.setVisible(true);
	}
//	
//	private void makeRollRequest() {
//		JFrame rollMaker = new JFrame();
//		rollMaker.setTitle("Make New Roll");
//		rollMaker.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		
//		SushiMakerWidget rollMakerView = new rollMakerWidget(this);
//		rollMaker.setContentPane(rollMakerView);
//		
//		rollMaker.pack();
//		rollMaker.setVisible(true);
//	}

	
	void makeRedPlateRequest(Sushi plate_sushi, int plate_position) {
		for (ChefViewListener l : listeners) {
			l.handleRedPlateRequest(plate_sushi, plate_position);
		}
	}

	void makeGreenPlateRequest(Sushi plate_sushi, int plate_position) {
		for (ChefViewListener l : listeners) {
			l.handleGreenPlateRequest(plate_sushi, plate_position);
		}
	}

	void makeBluePlateRequest(Sushi plate_sushi, int plate_position) {
		for (ChefViewListener l : listeners) {
			l.handleBluePlateRequest(plate_sushi, plate_position);
		}
	}
	
	void makeGoldPlateRequest(Sushi plate_sushi, int plate_position, double price) {
		for (ChefViewListener l : listeners) {
			l.handleGoldPlateRequest(plate_sushi, plate_position, price);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "new_sashimi":
			makeNotRollRequest(SushiMakerWidget.SushiType.SASHIMI);
			break;
		case "new_nigiri":
			makeNotRollRequest(SushiMakerWidget.SushiType.NIGIRI);
			break;
		case "new_roll":
			makeNotRollRequest(SushiMakerWidget.SushiType.ROLL);
		}
	}
}
