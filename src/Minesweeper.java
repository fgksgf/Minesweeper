import javax.swing.*;
import java.awt.*;

public class Minesweeper extends JFrame {
    // 扫雷面板的行数和列数
    int row = 9;
    int col = 9;

    // 窗口的宽度和高度
    int width = 0;
    int height = 0;

    Font font = new Font("黑体", Font.PLAIN, 23);

    // 方格的边长
    final int d = 51;
    int pw = Toolkit.getDefaultToolkit().getScreenSize().width;
    int ph = Toolkit.getDefaultToolkit().getScreenSize().height;

    //    JCheckBoxMenuItem[] levelItems = new JCheckBoxMenuItem[3];
    JRadioButtonMenuItem[] levelItems = new JRadioButtonMenuItem[3];
    String[] levelName = {"初级", "中级", "高级"};

    MyPanel panel;

    public Minesweeper() {
        setTitle("扫雷");
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
    public void resize() {
        width = col * d + 4;
        height = row * d + 140;
        setBounds((pw - width) / 2, (ph - height) / 2, width, height);
    }

    // 初始化菜单栏
    public void initBar() {
        // 创建游戏菜单并设置字体
        JMenu gameMenu = new JMenu("游戏");
        gameMenu.setFont(font);

        // 创建菜单选项new并添加响应动作
        JMenuItem newItem = new JMenuItem("新游戏");
        newItem.setFont(font);
        gameMenu.add(newItem);
        newItem.addActionListener(
                e -> {
                    // 开始一个新游戏
                    this.remove(panel);
                    resize();
                    panel = new MyPanel(row, col);
                    panel.startGame();
                    this.add(panel);
                    this.setVisible(true);
                }
        );

        gameMenu.addSeparator();
        for (int i = 0; i < levelItems.length; i++) {
//            levelItems[i] = new JCheckBoxMenuItem(levelName[i]);
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
                }
        );

        gameMenu.addSeparator();

        // 创建菜单选项about并添加响应动作
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.setFont(font);
        gameMenu.add(aboutItem);
        aboutItem.addActionListener(
                e -> JOptionPane.showMessageDialog(this,
                        "扫雷\nfgksgf@yahoo.com\n版本：V 2.0",
                        "关于", JOptionPane.PLAIN_MESSAGE)
        );

        // 创建菜单选项exit并添加响应动作
        JMenuItem exitItem = new JMenuItem("退出");
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
