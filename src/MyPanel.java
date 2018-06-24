import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyPanel extends JPanel implements MouseListener, Runnable {
    // 扫雷面板的行数和列数
    private int row;
    private int col;

    // 地雷总数
    private int sum = 10;
    // 剩余地雷数
    private int remain = 10;

    private long startTime;
    private long endTime = 0;

    private static Icon[] icon = new ImageIcon[3];

    private JButton label1 = new JButton(Minesweeper.def.getString("remain") + remain);
    private JButton label2 = new JButton(Minesweeper.def.getString("time") + endTime);
    private JButton face = new JButton(icon[0]);

    private Font font = new Font("黑体", Font.BOLD, 25);

    // 方格的边长
    private final int d = 51;
    private final int h = 70;

    private Block blocks[][];

    // 游戏是否结束
    private boolean over = false;
    // 游戏是否开始
    private boolean begin = false;
    // 玩家是否胜利
    private boolean win = false;

    static {
        for (int i = 0; i < icon.length; i++) {
            icon[i] = new ImageIcon("images/face" + i + ".png");
        }
    }

    MyPanel(int row, int col) {
        this.row = row;
        this.col = col;
        setBackground(Color.BLACK);
        initButtons();
        initBlocks();
        addMouseListener(this);
    }

    // 初始化按钮
    private void initButtons() {
        label1.setFont(font);
        label1.setBackground(Color.BLACK);
        label1.setEnabled(false);
        label1.setBorderPainted(false);
        this.add(label1);

        face.setBackground(Color.BLACK);
        face.setBorder(null);
        face.setSize(40,40);
        face.setBorderPainted(false);
        face.setIcon(icon[0]);
        face.addActionListener(
                e -> {
                    if (over || win || begin) {
                        restart();
                    }
                });
        this.add(face);

        label2.setFont(font);
        label2.setBackground(Color.BLACK);
        label2.setEnabled(false);
        label2.setBorderPainted(false);
        this.add(label2);
    }

    // 开始新游戏
    private void restart() {
        face.setIcon(icon[0]);
        remain = sum;
        startTime = 0;
        endTime = 0;
        win = false;
        over = false;
        begin = false;

        initBlocks();
        startGame();
    }

    // 初始化方块，随机产生地雷
    private void initBlocks() {
        // 已有地雷数
        int count = 0;

        blocks = new Block[row][col];
        if (row == 9 && col == 9) {
            sum = 10;
        } else if (row == 16 && col == 16) {
            sum = 40;
        } else if (row == 16 && col == 30) {
            sum = 99;
        }

        remain = sum;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                blocks[i][j] = new Block(d * j, d * i + h);
            }
        }

        // 随机指定地雷
        while (count < sum) {
            int i = (int) (Math.random() * row);
            int j = (int) (Math.random() * col);
            if (blocks[i][j].category != 9) {
                blocks[i][j].category = 9;
                count++;
            }
        }

        // 计算非地雷方块的数值
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (blocks[i][j].category != 9) {
                    // 左上
                    if (i - 1 >= 0 && j - 1 >= 0 && blocks[i - 1][j - 1].category == 9) {
                        blocks[i][j].category++;
                    }
                    // 上
                    if (i - 1 >= 0 && blocks[i - 1][j].category == 9) {
                        blocks[i][j].category++;
                    }
                    // 右上
                    if (i - 1 >= 0 && j + 1 < col && blocks[i - 1][j + 1].category == 9) {
                        blocks[i][j].category++;
                    }
                    // 左
                    if (j - 1 >= 0 && blocks[i][j - 1].category == 9) {
                        blocks[i][j].category++;
                    }
                    // 右
                    if (j + 1 < col && blocks[i][j + 1].category == 9) {
                        blocks[i][j].category++;
                    }
                    // 左下
                    if (i + 1 < row && j - 1 >= 0 && blocks[i + 1][j - 1].category == 9) {
                        blocks[i][j].category++;
                    }
                    // 下
                    if (i + 1 < row && blocks[i + 1][j].category == 9) {
                        blocks[i][j].category++;
                    }
                    // 右下
                    if (i + 1 < row && j + 1 < col && blocks[i + 1][j + 1].category == 9) {
                        blocks[i][j].category++;
                    }
                }
            }
        }
    }

    void startGame() {
        new Thread(this).start();
    }

    /**
     * 翻开相邻的方块
     * 如果方块为空（category为0），则递归地翻开与空相邻的方块
     */
    private void flipAround(int i, int j) {
        // 左上
        if (i - 1 >= 0 && j - 1 >= 0 && !blocks[i - 1][j - 1].flip && blocks[i - 1][j - 1].flag == 0) {
            blocks[i - 1][j - 1].flip = true;
            if (blocks[i - 1][j - 1].category == 0) {
                flipAround(i - 1, j - 1);
            }
        }
        // 上
        if (i - 1 >= 0 && !blocks[i - 1][j].flip && blocks[i - 1][j].flag == 0) {
            blocks[i - 1][j].flip = true;
            if (blocks[i - 1][j].category == 0) {
                flipAround(i - 1, j);
            }
        }
        // 右上
        if (i - 1 >= 0 && j + 1 < col && !blocks[i - 1][j + 1].flip && blocks[i - 1][j + 1].flag == 0) {
            blocks[i - 1][j + 1].flip = true;
            if (blocks[i - 1][j + 1].category == 0) {
                flipAround(i - 1, j + 1);
            }
        }
        // 左
        if (j - 1 >= 0 && !blocks[i][j - 1].flip && blocks[i][j - 1].flag == 0) {
            blocks[i][j - 1].flip = true;
            if (blocks[i][j - 1].category == 0) {
                flipAround(i, j - 1);
            }
        }
        // 右
        if (j + 1 < col && !blocks[i][j + 1].flip && blocks[i][j + 1].flag == 0) {
            blocks[i][j + 1].flip = true;
            if (blocks[i][j + 1].category == 0) {
                flipAround(i, j + 1);
            }
        }
        // 左下
        if (i + 1 < row && j - 1 >= 0 && !blocks[i + 1][j - 1].flip && blocks[i + 1][j - 1].flag == 0) {
            blocks[i + 1][j - 1].flip = true;
            if (blocks[i + 1][j - 1].category == 0) {
                flipAround(i + 1, j - 1);
            }
        }
        // 下
        if (i + 1 < row && !blocks[i + 1][j].flip && blocks[i + 1][j].flag == 0) {
            blocks[i + 1][j].flip = true;
            if (blocks[i + 1][j].category == 0) {
                flipAround(i + 1, j);
            }
        }
        // 右下
        if (i + 1 < row && j + 1 < col && !blocks[i + 1][j + 1].flip && blocks[i + 1][j + 1].flag == 0) {
            blocks[i + 1][j + 1].flip = true;
            if (blocks[i + 1][j + 1].category == 0) {
                flipAround(i + 1, j + 1);
            }
        }
    }

    /**
     * 当双击方块周围已标记雷数等于该位置数字时，相当于对该方块周围未打开的方块均进行一次左键单击操作
     * 地雷未标记完全时使用双击无效。若数字周围有标错的地雷，则游戏结束
     */
    private void doubleClickFlip(int i, int j) {
        int number = 0;
        boolean wrong = false;
        // 左上
        if (i - 1 >= 0 && j - 1 >= 0 && !blocks[i - 1][j - 1].flip) {
            if (blocks[i - 1][j - 1].category == 9 && blocks[i - 1][j - 1].flag == 1) {
                number++;
            } else if (blocks[i - 1][j - 1].category != 9 && blocks[i - 1][j - 1].flag == 1) {
                wrong = true;
            }
        }
        // 上
        if (i - 1 >= 0 && !blocks[i - 1][j].flip) {
            if (blocks[i - 1][j].category == 9 && blocks[i - 1][j].flag == 1) {
                number++;
            } else if (blocks[i - 1][j].category != 9 && blocks[i - 1][j].flag == 1) {
                wrong = true;
            }
        }
        // 右上
        if (i - 1 >= 0 && j + 1 < col && !blocks[i - 1][j + 1].flip) {
            if (blocks[i - 1][j + 1].category == 9 && blocks[i - 1][j + 1].flag == 1) {
                number++;
            } else if (blocks[i - 1][j + 1].category != 9 && blocks[i - 1][j + 1].flag == 1) {
                wrong = true;
            }
        }
        // 左
        if (j - 1 >= 0 && !blocks[i][j - 1].flip) {
            if (blocks[i][j - 1].category == 9 && blocks[i][j - 1].flag == 1) {
                number++;
            } else if (blocks[i][j - 1].category != 9 && blocks[i][j - 1].flag == 1) {
                wrong = true;
            }
        }
        // 右
        if (j + 1 < col && !blocks[i][j + 1].flip) {
            if (blocks[i][j + 1].category == 9 && blocks[i][j + 1].flag == 1) {
                number++;
            } else if (blocks[i][j + 1].category != 9 && blocks[i][j + 1].flag == 1) {
                wrong = true;
            }
        }
        // 左下
        if (i + 1 < row && j - 1 >= 0 && !blocks[i + 1][j - 1].flip) {
            if (blocks[i + 1][j - 1].category == 9 && blocks[i + 1][j - 1].flag == 1) {
                number++;
            } else if (blocks[i + 1][j - 1].category != 9 && blocks[i + 1][j - 1].flag == 1) {
                wrong = true;
            }
        }
        // 下
        if (i + 1 < row && !blocks[i + 1][j].flip) {
            if (blocks[i + 1][j].category == 9 && blocks[i + 1][j].flag == 1) {
                number++;
            } else if (blocks[i + 1][j].category != 9 && blocks[i + 1][j].flag == 1) {
                wrong = true;
            }
        }
        // 右下
        if (i + 1 < row && j + 1 < col && !blocks[i + 1][j + 1].flip) {
            if (blocks[i + 1][j + 1].category == 9 && blocks[i + 1][j + 1].flag == 1) {
                number++;
            } else if (blocks[i + 1][j + 1].category != 9 && blocks[i + 1][j + 1].flag == 1) {
                wrong = true;
            }
        }

        if (wrong) {
            over = true;
            begin = false;
            face.setIcon(icon[2]);
            flipAll();
            repaint();
            return;
        }

        if (number == blocks[i][j].category) {
            flipAround(i, j);
        }
    }

    // 翻开所有方块
    private void flipAll() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                blocks[i][j].flip = true;
                blocks[i][j].over = true;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        label1.setText(Minesweeper.def.getString("remain") + remain);
        if (begin) {
            endTime = (System.currentTimeMillis() - startTime) / 1000;
        }
        label2.setText(Minesweeper.def.getString("time") + endTime);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                blocks[i][j].drawBlock(g);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int i = (e.getY() - h) / d;
        int j = e.getX() / d;

        if ((!over || !win) && i >= 0 && i < row && j >= 0 && j < col && !blocks[i][j].flip) {
            if (e.getButton() == MouseEvent.BUTTON1 && blocks[i][j].flag == 0) {
                // 第一次单击时开始记时
                if (!begin) {
                    begin = true;
                    startTime = System.currentTimeMillis();
                }

                // 左键点击翻开方块
                blocks[i][j].flip = true;
                if (blocks[i][j].category == 0) {
                    flipAround(i, j);
                } else if (blocks[i][j].category == 9) {
                    // 翻开地雷，游戏结束
                    over = true;
                    begin = false;
                    face.setIcon(icon[2]);
                    flipAll();
                    repaint();
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                // 右键标记方块
                int temp = blocks[i][j].flag;
                blocks[i][j].updateFlag();
                if (blocks[i][j].flag == 1 && remain > 0) {
                    remain--;
                }
                // 如果清除了扫雷标记，则剩余地雷数加一
                if (temp == 1 && blocks[i][j].flag != 1) {
                    remain++;
                }
            }
        } else if ((!over || !win) && i >= 0 && i < row && j >= 0 && j < col && e.getClickCount() == 2) {
            if (blocks[i][j].flip && blocks[i][j].category < 9) {
                doubleClickFlip(i, j);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void run() {
        while (!over && !win) {
            // 判断玩家是否胜利
            if (remain == 0) {
                int count = 0;
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        Block b = blocks[i][j];
                        // 如果所有地雷都被扫除则胜利
                        if (b.category == 9 && b.flag == 1) {
                            count++;
                        }
                    }
                }
                if (count == sum) {
                    win = true;
                    begin = false;
                    face.setIcon(icon[1]);
                    flipAll();
                    repaint();
                }
            }

            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
