package sushigame.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import sushigame.model.Belt;
import sushigame.model.BeltEvent;
import sushigame.model.BeltObserver;
import sushigame.model.Chef;
import sushigame.model.SushiGameModel;

public class ScoreboardWidget extends JPanel implements BeltObserver, ActionListener {

	private SushiGameModel game_model;
	private JLabel display;
	private JPanel buttonDisplay;
	private int order;
	private boolean first;
	
	public ScoreboardWidget(SushiGameModel gm) {
		game_model = gm;
		game_model.getBelt().registerBeltObserver(this);
		order = 0;
		first = true;
		
		display = new JLabel();
		display.setVerticalAlignment(SwingConstants.TOP);
		buttonDisplay = new JPanel();
		setLayout(new BorderLayout());
		add(display, BorderLayout.NORTH);
		add(buttonDisplay, BorderLayout.CENTER);
		display.setText(makeScoreboardHTML());
	}

	private String makeScoreboardHTML() {
		String sb_html = "<html>";
		sb_html += "<h1>Scoreboard</h1>";

		// Create an array of all chefs and sort by balance.
		Chef[] opponent_chefs= game_model.getOpponentChefs();
		Chef[] chefs = new Chef[opponent_chefs.length+1];
		chefs[0] = game_model.getPlayerChef();
		for (int i=1; i<chefs.length; i++) {
			chefs[i] = opponent_chefs[i-1];
		}
		switch(order) {
		case 0:
			Arrays.sort(chefs, new HighToLowBalanceComparator());
			break;
		case 1:
			Arrays.sort(chefs, new HighToLowSoldComparator());
			break;
		case 2:
			Arrays.sort(chefs, new LowToHighSpoiledComparator());
			break;
		}
		
		for (Chef c : chefs) {
			sb_html += c.getName() + " ($" + Math.round(c.getBalance()*100.0)/100.0 + " | Sold: "
					+ c.getSold() + " | Spoiled: " + c.getSpoiled() +  ") <br>";
		}
		
		if(first) {
			JButton balance_button = new JButton("Balance");
			balance_button.addActionListener(this);
			balance_button.setActionCommand("balance");
			buttonDisplay.add(balance_button);

			JButton sold_button = new JButton("Sold");
			sold_button.addActionListener(this);
			sold_button.setActionCommand("sold");
			buttonDisplay.add(sold_button);

			JButton spoiled_button = new JButton("Spoiled");
			spoiled_button.addActionListener(this);
			spoiled_button.setActionCommand("spoiled");
			buttonDisplay.add(spoiled_button);
			
			first = false;
		}
		
		return sb_html;
	}

	public void refresh() {
		display.setText(makeScoreboardHTML());		
	}
	
	@Override
	public void handleBeltEvent(BeltEvent e) {
		if (e.getType() == BeltEvent.EventType.ROTATE) {
			refresh();
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		switch(cmd) {
		case "balance":
			order = 0;
			break;
		case "sold":
			order = 1;
			break;
		case "spoiled":
			order = 2;
			break;
		}
		
		this.refresh();
	}

}
