package Process;

import DataStructure.CircularlyLinkedList;
import Model.BlockEntity;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Packer {

    private ArrayList<BlockEntity[]> packData(ArrayList<String[]> list){
        BlockEntity[] sample = new BlockEntity[16];
        for (int i = 0; i < list.size(); i++) {
            String[] dataItem = list.get(i);
            if(dataItem != null) {
                CircularlyLinkedList<Integer> tubes = new CircularlyLinkedList<>();
                tubes.addFirst(Integer.parseInt(dataItem[3]));
                tubes.addFirst(Integer.parseInt(dataItem[2]));
                tubes.addFirst(Integer.parseInt(dataItem[1]));
                tubes.addFirst(Integer.parseInt(dataItem[0]));
                sample[i] = new BlockEntity(tubes, dataItem[4]);
            }else{
                sample[i] = null;
            }
        }
        ArrayList<BlockEntity[]> result =  new ArrayList<>();
        result.add(sample);
        return result;
    }

    public BlockEntity[] loadPuzzle(String path) throws Exception {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            String result = "empty";
            if (line != null) {
                result = line;
                sb.append(line);
                sb.append(System.lineSeparator());
            }

            return packData(getData(result)).get(0);

        } catch (IOException e) {
            throw new Exception("Arquivo não selecionado pelo usuário");
        }
    }

    private ArrayList<String[]> getData(String result) throws IOException {
        ArrayList<String[]> data = new ArrayList<>();
        result = result.substring(1, result.length() - 1);
        String[] objList = result.split(",");


        for(int j = 0; j < objList.length; j++){
            if (objList[j].equalsIgnoreCase("null")){
                data.add(null);
            }else{
                String[] iol = new String[5];
                iol = objList[j].split(";");
                data.add(new String[]{
                        (String)iol[0].substring(1, iol[0].length()),
                        (String)iol[1],
                        (String)iol[2],
                        (String)iol[3],
                        (String)iol[4].substring(0, iol[4].length()-1)});
            }

        }
        return data;
    }

    public BlockEntity[] getPuzzle(String result) throws IOException {
        return packData(getData(result)).get(0);
    }

}
