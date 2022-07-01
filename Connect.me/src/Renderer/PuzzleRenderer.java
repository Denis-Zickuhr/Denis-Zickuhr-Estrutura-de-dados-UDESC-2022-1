package Renderer;

import Assets.Puzzles;
import Model.BlockEntity;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import Process.MatchWatcher;

/**
 *
 * @author Denis
 */
public class PuzzleRenderer extends JDialog {
    private JPanel jp_contentPane;
    private JPanel jp_grid;
    private JPanel jp_buttons;
    private JPanel jp_block;
    private JButton btn_load;
    private JButton btn_width;
    private JButton btn_depth;
    private JButton btn_about;
    private JLabel description;
    private int sample = 0;
    private boolean redraw = false;
    private static JButton lastBtnClk;
    private static int lastBtnClkNumber;
    private static BlockEntity[] array;
    private MatchWatcher mt;


    public PuzzleRenderer() throws Exception {

        // jp_contentPane defs
        jp_contentPane = new JPanel();
        BorderLayout layout = new BorderLayout();
        jp_contentPane.setLayout(layout);
        setContentPane(jp_contentPane);
        setTitle("App");
        setLocation(0,0);
        setModal(true);

        // getRootPane().setDefaultButton(btn_load);

        loadGrid();
        loadButtons();
        drawGrid(new BlockEntity[16]);

        description = new JLabel("Time elapsed: 0ms Solution depth: 0");
        jp_contentPane.add(BorderLayout.NORTH, description);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        // call onCancel() on ESCAPE
        jp_grid.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


    }

    private void loadGrid(){
        jp_grid = new JPanel(new GridLayout(4,4,10,10));
        jp_grid.setSize(800, 800);
        jp_contentPane.add(BorderLayout.CENTER,jp_grid);
    }

    private void loadButtons(){
        jp_buttons = new JPanel();
        btn_load = new JButton("Load");
        btn_width = new JButton("Width");
        btn_depth = new JButton("Depth");
        btn_about = new JButton("About");
        jp_buttons.add(btn_load);
        jp_buttons.add(btn_width);
        jp_buttons.add(btn_depth);
        jp_buttons.add(btn_about);
        jp_contentPane.add(BorderLayout.SOUTH, jp_buttons);
        loadEvents();
    }

    public void loadEvents(){
        btn_load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onLoad();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        btn_width.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onWidth();
            }
        });
        btn_depth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDepth();
            }
        });

        btn_about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAbout();
            }
        });
    }

    public void drawGrid(BlockEntity[] outerArray) throws Exception {

        array = outerArray;
        if (mt == null)
            mt = new MatchWatcher(array.clone());
        else
        mt.watch(array.clone());



        for(int i=0;i<array.length;i++) {

            jp_block = new JPanel();
            BorderLayout layout = new BorderLayout();
            jp_block.setLayout(layout);

            if (array[i] != null){

                ImagePanel block = getBlockImage(array[i].getType());
                block.setPreferredSize(new Dimension(100, 100));
                jp_block.add(BorderLayout.CENTER, block);

                ImagePanel bar_north = getBar((int)array[i].getTubes().first(), 'v');
                array[i].getTubes().rotate();
                ImagePanel bar_east = getBar((int)array[i].getTubes().first(), 'h');
                array[i].getTubes().rotate();
                ImagePanel bar_south = getBar((int)array[i].getTubes().first(), 'v');
                array[i].getTubes().rotate();
                ImagePanel bar_west = getBar((int)array[i].getTubes().first(), 'h');
                array[i].getTubes().rotate();

                jp_block.add(BorderLayout.SOUTH, bar_south);
                jp_block.add(BorderLayout.NORTH, bar_north);
                jp_block.add(BorderLayout.EAST, bar_east);
                jp_block.add(BorderLayout.WEST, bar_west);

                int finalI = i;

                block.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mousePressed(MouseEvent e)
                    {
                        //System.out.println(array[finalI].getTubes());
                        array[finalI].RotateBlock(array[finalI].getType());
                        try {
                            drawGrid(array);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        lastBtnClkNumber = finalI;
                    }
                });

            }else{
                ImagePanel block = new ImagePanel("src/Assets/null.png");
                block.setPreferredSize(new Dimension(120, 120));
                block.setBackground(Color.white);

                JPanel bar_south = new JPanel();
                bar_south.setBackground(Color.white);

                JPanel bar_north = new JPanel();
                bar_north.setBackground(Color.white);

                JPanel bar_east = new JPanel();
                bar_east.setBackground(Color.white);

                JPanel bar_west = new JPanel();
                bar_west.setBackground(Color.white);

                jp_block.add(BorderLayout.CENTER, block);
                jp_block.add(BorderLayout.SOUTH, bar_south);
                jp_block.add(BorderLayout.NORTH, bar_north);
                jp_block.add(BorderLayout.EAST, bar_east);
                jp_block.add(BorderLayout.WEST, bar_west);

                int finalI = i;

                block.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mousePressed(MouseEvent e)
                    {
                        if (lastBtnClkNumber != 999) {
                            try {
                                moveBlock(lastBtnClkNumber, finalI);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });

            }

            if(redraw){
                jp_grid.remove(jp_grid.getComponent(i));
            }
            jp_grid.add(jp_block, i);
        }

        if(!redraw){
            redraw = true;
        }

        jp_grid.updateUI();

    }

    private ImagePanel getBlockImage(String type){
        return new ImagePanel("src/Assets/" + type + ".png");
    }

    private ImagePanel getBar(int i, char dir) throws Exception {

        int h = 0, w = 0;
        ImagePanel tubes = new ImagePanel("src/Assets/tubes"+i+dir+"u.png");

        switch (dir){
            case 'h' : {
                h = 20 ;w = 100;
                break;
            }

            case 'v' : {
                h = 100 ;w = 20;
                break;
            }
            default: throw new Exception("Invalid orientation");
        }

        tubes.setPreferredSize(new Dimension(h, w));

        return tubes;

    }


    class Block extends JLabel{
        @Override
        public void setText(String text) {
            super.setText(text);
            super.setFont(new Font("Arial",Font.BOLD,60));
            super.setBorder(new BevelBorder(1));
        }
    }

    private void onLoad() throws Exception {
        //LoadRenderer loadRenderer = new LoadRenderer(this);
        FileDialog fileDialog = new FileDialog((Frame) null);
        fileDialog.setVisible(true);
        String path = fileDialog.getDirectory() + fileDialog.getFile();
        Puzzles puzzle = new Puzzles();
        this.drawGrid(puzzle.returnPuzzle(path));
    }

    private void onWidth() {
        System.out.println("Width");
    }

    private void onDepth(){
        System.out.println("Depth");
    }

    private void onAbout() {
        AboutRenderer aboutRenderer = new AboutRenderer(this);
    }

    private void onClose(){
        dispose();
    }

    private String moveBlock(int blockPosition, int newPosition) throws Exception {
        if (array[newPosition] == null){
            BlockEntity s = array[blockPosition];
            BlockEntity f = array[newPosition];
            array[blockPosition] = f;
            array[newPosition] = s;
            drawGrid(array);
            lastBtnClkNumber = 999;
            return "block successfully moved!";
        }else{
            if (array[newPosition].getType().equals("red") | array[newPosition].getType().equals("blue")){
                return array[newPosition].getType() + " block cant be moved to!";
            }else{
                if(array[blockPosition].getType().equals("red") | array[blockPosition].getType().equals("blue")){
                    return array[newPosition].getType() + " block cant be moved!";
                }else{
                    BlockEntity s = array[blockPosition];
                    BlockEntity f = array[newPosition];
                    array[blockPosition] = f;
                    array[newPosition] = s;
                    drawGrid(array);
                    lastBtnClkNumber = 999;
                    return "block successfully moved!";
                }
            }
        }
    }
}
