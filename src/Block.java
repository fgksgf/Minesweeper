import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class Block {
    // 方块坐标
    private int x;
    private int y;

    // 方块是否被翻开
    boolean flip = false;
    // 游戏是否结束
    boolean over = false;

    // 方块被标记的类型, 0为无标记，1为扫除标记，2为问号标记
    int flag = 0;

    // 方块类型：0-8数字，9表示是地雷
    int category = 0;

    private static Image nums[] = new Image[9];
    private static Image bombs[] = new Image[2];
    private static Image flags[] = new Image[3];

    static {
        try {
            // 读取数字图片
            for (int i = 0; i < 9; i++) {
                nums[i] = ImageIO.read(new File("images/" + i + ".jpg"));
            }

            for (int i = 0; i < 2; i++) {
                // 读取地雷图片
                bombs[i] = ImageIO.read(new File("images/bomb" + i + ".jpg"));
            }

            for (int i = 0; i < 3; i++) {
                // 读取标记图片
                flags[i] = ImageIO.read(new File("images/flag" + i + ".jpg"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 右键未翻开的方块时，更新标记
    void updateFlag() {
        flag++;
        flag = flag % 3;
    }

    // 画方块
    void drawBlock(Graphics g) {
        if (flip) {
            if (over) {
                if (category == 9 && flag == 1) {
                    // 如果地雷被扫到
                    g.drawImage(bombs[1], x, y, null);
                } else if (category == 9) {
                    g.drawImage(bombs[0], x, y, null);
                }
            } else {
                if (category == 9) {
                    // 被翻开是地雷
                    g.drawImage(bombs[0], x, y, null);
                }
            }

            if (category >= 0 && category <= 8) {
                // 被翻开且是数字
                g.drawImage(nums[category], x, y, null);
            }
        } else {
            // 未被翻开
            g.drawImage(flags[flag], x, y, null);
        }
    }
}
