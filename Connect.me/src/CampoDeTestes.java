import java.util.*;

public class CampoDeTestes {

    static int atual = 0;
    static int[] s = new int[]{1,1,1};
    static boolean ida = true;
    static List list = new ArrayList();

    public static void main(String[] args) {

//        for (int i = 0; i < 10; i++){
//            analiseCombinatorial(s);
//            System.out.println(s[0] +" "+ s[1] +" "+ s[2] + " ");
//        }


        //[[3;3;0;0;blue],null,[1;2;4;2;orange],null,[3;4;0;0;red],null,null,null,null,[0;0;2;0;blue],[0;0;1;0;orange],null,null,null,[0;0;2;3;green],null]

        // String[] blocos = new String[]     {"2","0","3","0","1","0","0","0","0","2","3","0","0","0","4","0"};

        // 0 = Não tem bloco
        // 1 = Tem bloco
        // 2 = Tem bloco e ele gira
        // 3 = Tem bloco, ele gira e move
        // 4 = Tem bloco e ele move

        // r = 2 e 3 quantidade


        //product(List.of(3,2,1,0), 2);

        // printAllRecursive(3, s, ',');
//
        String blocos = "0000000000001110";

        int[] ordem = new int[]{1,2,3};


        CampoDeTestes campoDeTestes = new CampoDeTestes();
        //campoDeTestes.permute(ordem);

        //System.out.println(list);

        //System.out.println(campoDeTestes.binarySelection(blocos, 16).length);

        //campoDeTestes.merge(campoDeTestes.binarySelection(blocos, 8), list);

//        int r = 4;
//        for (int i = 0; i < Math.pow(r, r); i++) {
//            System.out.println(padLeftStatic(Integer.toString(Integer.parseInt(Integer.toString(i), 10), 4), r, '0'));
//        }

    }

    /*

    Produto

    */

    public static List<Collection<Integer>> product(Collection<Integer> a, int r) {
        List<Collection<Integer>> result = Collections.nCopies(1, Collections.emptyList());
        for (Collection<Integer> pool : Collections.nCopies(r, new LinkedHashSet<>(a))) {
            List<Collection<Integer>> temp = new ArrayList<>();
            for (Collection<Integer> x : result) {
                for (Integer y : pool) {
                    Collection<Integer> z = new ArrayList<>(x);
                    z.add(y);
                    temp.add(z);
                    if(z.size() == r)
                        System.out.println(z);
                }
            }
            result = temp;
        }
        System.out.println(result.size());
        return result;
    }

    /*

    Permutação de elementos

    */

    public void permute(
            int[] elements) {

        String[] p = new String[factorial(elements.length)];

        if(elements.length == 1) {
            printArray(elements);
        } else {
            for(int i = 0; i < elements.length-1; i++) {
                permute(elements.length - 1, elements);
                if(elements.length % 2 == 0) {
                    swap(elements, i, elements.length-1);
                } else {
                    swap(elements, 0, elements.length-1);
                }
            }
            permute(elements.length - 1, elements);
        }
    }

    public void permute(
            int n, int[] elements) {

        if(n == 1) {
            printArray(elements);
        } else {
            for(int i = 0; i < n-1; i++) {
                permute(n - 1, elements);
                if(n % 2 == 0) {
                    swap(elements, i, n-1);
                } else {
                    swap(elements, 0, n-1);
                }
            }
            permute(n - 1, elements);
        }
    }

    public static int factorial(int value){
        int output = 1;
        for (int i = 2; i <= value; i++) {
            output *= i;
        }
        return output;
    }

    private static void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    private static void printArray(int[] input) {
       String value = "";
        for(int i = 0; i < input.length; i++) {
            value += input[i];
        }
        list.add(value);
    }

    /*

     Seleção binária

     */

    public Object[] binarySelection(String string, int bits){

        ArrayList temp = new ArrayList();
        CharSequence cs;

        for (int i = 0; i < Math.pow(2, bits); i++) {
            cs = padLeft(Integer.toBinaryString(i),bits, '0');
            if (string.chars().average().equals(cs.chars().average())){
                String str = cs.toString().replaceAll("1", "#");

//                for (int last = 0; last < list.size(); last++) {
//                    String strCopy = str;
//                    for (int j = 0; j < list.get(last).toString().length(); j++) {
//                        strCopy = strCopy.replaceFirst("#", list.get(last).toString().charAt(j) + "");
//                    }
//                    temp.add(strCopy);
//                }
            }
        }
        return temp.toArray();
    }

    public String padLeft(String string, int length, char character) {
        if (string.length() >= length) {
            return string;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - string.length()) {
            sb.append(character);
        }
        sb.append(string);

        return sb.toString();
    }

    public static String padLeftStatic(String string, int length, char character) {
        if (string.length() >= length) {
            return string;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - string.length()) {
            sb.append(character);
        }
        sb.append(string);

        return sb.toString();
    }

    /*

    Merge

    */

}
