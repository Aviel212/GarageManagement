import javax.swing.JFrame;

public class TestGarage {

	public static void main(String[] args) {
		GarageFrame frame=new GarageFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 650);
		//frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

	}

}
