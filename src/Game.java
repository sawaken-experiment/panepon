import java.awt.Container;
import javax.swing.JFrame;

public class Game extends JFrame {

	private static final long serialVersionUID = 1L;

	public Game() {
		setTitle("パネルがポン"); //タイトルバーに表示する
		setBounds(100, 50, 100, 100);

		Panel panel_ob = new Panel();

		//継承したJFrameクラス内のgetContentPaneメソッドにより
		//このフレームの contentPane オブジェクトを取得
		Container content_ob = getContentPane();
		//オブジェクトにコンポーネント(表示内容)を追加する
		content_ob.add(panel_ob);

		//windowのサイズをパネルのサイズに合わせる
		pack();
	}

	public static void main(String[] args) {
		Game frame = new Game();
		//×でプロセスの終了
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//window表示ON
		frame.setVisible(true);

	}
}