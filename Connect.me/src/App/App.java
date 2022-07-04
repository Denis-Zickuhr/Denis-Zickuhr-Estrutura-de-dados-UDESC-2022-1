package App;

import Renderer.PuzzleRenderer;

public class App {

    public static void main(String[] args) throws Exception {
        PuzzleRenderer dialog = new PuzzleRenderer();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }

}
