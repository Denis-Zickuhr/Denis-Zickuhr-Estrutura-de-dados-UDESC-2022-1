package Renderer;

import Process.Packer;
import Model.BlockEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import Process.MatchWatcher;
import Shared.Buscas.*;
import Process.ConnectMeEstadoENode;

/**
 *
 * @author Denis
 */
public class PuzzleRenderer extends JFrame {
    private final JPanel jp_contentPane;
    private JPanel jp_grid;
    private JButton btn_load;
    private JButton btn_width;
    private JButton btn_depth;
    private JButton btn_about;
    private final JLabel description;
    private boolean redraw = false;
    private static int lastBtnClkNumber;
    private static BlockEntity[] array;
    private MatchWatcher mt;
    private final Packer dataPacker = new Packer();

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public static void setArray(BlockEntity[] array) {
        PuzzleRenderer.array = array;
    }

    public PuzzleRenderer() throws Exception {

        // jp_contentPane defs
        jp_contentPane = new JPanel();
        BorderLayout layout = new BorderLayout();
        jp_contentPane.setLayout(layout);
        setContentPane(jp_contentPane);
        setTitle("App");
        setLocation(0,0);
        //setVisible(true);

        // getRootPane().setDefaultButton(btn_load);

        loadGrid();
        loadButtons();
        drawGrid(new BlockEntity[16]);

        description = new JLabel("Time elapsed: 0ms, Solution depth: 0, Nodes visited: ");
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
        jp_grid = new JPanel(new GridLayout(4,4,0,0));
        jp_grid.setBackground(Color.white);
        jp_grid.setSize(800, 800);
        jp_contentPane.add(BorderLayout.CENTER,jp_grid);
    }

    private void loadButtons(){
        JPanel jp_buttons = new JPanel();
        btn_load = new JButton("Carregar");
        btn_width = new JButton("Largura");
        btn_depth = new JButton("Profundidade");
        btn_about = new JButton("Sobre");
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
                try {
                    onWidth();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btn_depth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onDepth();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

            JPanel jp_block = new JPanel();
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
                ImagePanel block = new ImagePanel("Assets/null.png");
                block.setPreferredSize(new Dimension(120, 120));
               //block.setBackground(Color.gray);

                JPanel bar_south = new JPanel();
                //bar_south.setBackground(Color.white);

                JPanel bar_north = new JPanel();
                //bar_north.setBackground(Color.white);

                JPanel bar_east = new JPanel();
                //bar_east.setBackground(Color.white);

                JPanel bar_west = new JPanel();
                //bar_west.setBackground(Color.white);

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
        return new ImagePanel("Assets/" + type + ".png");
    }

    private ImagePanel getBar(int i, char dir) throws Exception {

        int h = 0, w = 0;
        ImagePanel tubes = new ImagePanel("Assets/tubes"+i+dir+"u.png");

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

    private void onLoad() throws Exception {
        //LoadRenderer loadRenderer = new LoadRenderer(this);
        FileDialog fileDialog = new FileDialog((Frame) null);
        fileDialog.setVisible(true);
        String path = fileDialog.getDirectory() + fileDialog.getFile();
        this.drawGrid(this.dataPacker.loadPuzzle(path));
    }

    private void onWidth() throws Exception {

        ConnectMeEstadoENode estadoInicial = new ConnectMeEstadoENode(mt.getPuzzle(), mt.getMovimentosValidos(), mt.getOrdem());

        Busca<ConnectMeEstadoENode> busca = new BuscaLargura<>(new MostraStatusConsole());
        ConnectMeEstadoENode estadoFinal = (ConnectMeEstadoENode)busca.busca(estadoInicial).getEstado();
        setDescription(
                "Time elapsed: "+busca.getStatus().getTempoDecorrido()
                        +"ms, Solution depth: "+busca.getStatus().getProfundidade()
                        +", Nodes visited: " + busca.getStatus().getVisitados());


        if (!estadoFinal.getGenerator().equals("init")){
            String result = mt.getAsSolution(estadoFinal.getPuzzle(), estadoFinal.getGenerator());
            drawGrid(this.dataPacker.getPuzzle(result));
        }else{
            drawGrid(array);
        }


    }

    private void onDepth() throws Exception {

        ConnectMeEstadoENode estadoInicial = new ConnectMeEstadoENode(mt.getPuzzle(), mt.getMovimentosValidos(), mt.getOrdem());

        ConnectMeEstadoENode estadoFinal;

        Busca<ConnectMeEstadoENode> busca = new BuscaProfundidade<>(mt.getDepth(), new MostraStatusConsole());
        estadoFinal = (ConnectMeEstadoENode) busca.busca(estadoInicial).getEstado();
        setDescription(
                "Time elapsed: "+busca.getStatus().getTempoDecorrido()
                        +"ms, Solution depth: "+busca.getStatus().getProfundidade()
                        +", Nodes visited: " + busca.getStatus().getVisitados());

        if (!estadoFinal.getGenerator().equals("init")){
            String result = mt.getAsSolution(estadoFinal.getPuzzle(), estadoFinal.getGenerator());
            drawGrid(this.dataPacker.getPuzzle(result));
        }else{
            drawGrid(array);
        }

    }

    private void onAbout() {
        AboutRenderer aboutRenderer = new AboutRenderer(this);
    }

    private void onClose(){
        dispose();
    }

    private void moveBlock(int blockPosition, int newPosition) throws Exception {
        if (array[newPosition] == null){
            BlockEntity s = array[blockPosition];
            BlockEntity f = array[newPosition];
            array[blockPosition] = f;
            array[newPosition] = s;
            drawGrid(array);
            lastBtnClkNumber = 999;
        }else{
            if (array[newPosition].getType().equals("red") | array[newPosition].getType().equals("blue")){
            }else{
                if(array[blockPosition].getType().equals("red") | array[blockPosition].getType().equals("blue")){
                }else{
                    BlockEntity s = array[blockPosition];
                    BlockEntity f = array[newPosition];
                    array[blockPosition] = f;
                    array[newPosition] = s;
                    drawGrid(array);
                    lastBtnClkNumber = 999;
                }
            }
        }
    }
}
