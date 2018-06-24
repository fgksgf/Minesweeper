import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class Minesweeper extends JFrame {
    // 扫雷面板的行数和列数
    private int row = 9;
    private int col = 9;

    private Font font = new Font("黑体", Font.PLAIN, 23);
    static final ResourceBundle def = ResourceBundle.getBundle("content");

    // 获取电脑屏幕的宽度和高度
    private int pw = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int ph = Toolkit.getDefaultToolkit().getScreenSize().height;

    private JRadioButtonMenuItem[] levelItems = new JRadioButtonMenuItem[3];
    private String[] levelName = {
            def.getString("newbie"),
            def.getString("midbie"),
            def.getString("highbie")};

    private MyPanel panel;

    public Minesweeper() {
        setTitle(def.getString("title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initBar();

        resize();
        panel = new MyPanel(row, col);
        panel.startGame();
        this.add(panel);

        setVisible(true);
    }

    // 窗口大小随着行数和列数变化
    private void resize() {
        // 方格的边长
        int d = 51;

        // 游戏窗口的宽度和高度
        int width = col * d + 4;
        int height = row * d + 140;

        setBounds((pw - width) / 2, (ph - height) / 2, width, height);
    }

    // 开始一个新游戏
    private void newGame() {
        this.remove(panel);
        resize();
        panel = new MyPanel(row, col);
        panel.startGame();
        this.add(panel);
        this.setVisible(true);
    }

    // 初始化菜单栏
    private void initBar() {
        // 创建游戏菜单并设置字体
        JMenu gameMenu = new JMenu(def.getString("game"));
        gameMenu.setFont(font);

        // 创建菜单选项new并添加响应动作
        JMenuItem newItem = new JMenuItem(def.getString("new"));
        newItem.setFont(font);
        gameMenu.add(newItem);
        newItem.addActionListener(
                e -> newGame()
        );

        gameMenu.addSeparator();
        for (int i = 0; i < levelItems.length; i++) {
            levelItems[i] = new JRadioButtonMenuItem(levelName[i]);
            levelItems[i].setFont(font);
            gameMenu.add(levelItems[i]);
        }

        // 初始默认难度为初级
        levelItems[0].setSelected(true);

        levelItems[0].addActionListener(
                e -> {
                    // 设置为初级难度
                    levelItems[0].setSelected(true);
                    levelItems[1].setSelected(false);
                    levelItems[2].setSelected(false);
                    row = 9;
                    col = 9;
                    newGame();
                }
        );

        levelItems[1].addActionListener(
                e -> {
                    // 设置为中级难度
                    levelItems[1].setSelected(true);
                    levelItems[0].setSelected(false);
                    levelItems[2].setSelected(false);
                    row = 16;
                    col = 16;
                    newGame();
                }
        );

        levelItems[2].addActionListener(
                e -> {
                    // 设置为高级难度
                    levelItems[2].setSelected(true);
                    levelItems[0].setSelected(false);
                    levelItems[1].setSelected(false);
                    row = 16;
                    col = 30;
                    newGame();
                }
        );

        gameMenu.addSeparator();

        // 创建菜单选项about并添加响应动作
        JMenuItem aboutItem = new JMenuItem(def.getString("about"));
        aboutItem.setFont(font);
        gameMenu.add(aboutItem);
        aboutItem.addActionListener(
                e -> JOptionPane.showMessageDialog(this,
                        "<html><h1>扫雷</h1><h2>版本：V 2.1</h2></html>",
                        "关于", JOptionPane.PLAIN_MESSAGE)
        );

        // 创建菜单选项exit并添加响应动作
        JMenuItem exitItem = new JMenuItem(def.getString("exit"));
        exitItem.setFont(font);
        gameMenu.add(exitItem);
        exitItem.addActionListener(
                e -> System.exit(0)
        );

        // 创建工具栏
        JMenuBar bar = new JMenuBar();
        bar.add(gameMenu);
        this.setJMenuBar(bar);
    }

    public static void main(String[] args) {
        new Minesweeper();
    }
}
