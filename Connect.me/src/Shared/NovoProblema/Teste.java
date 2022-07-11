package Shared.NovoProblema;

import Shared.Buscas.*;

import java.util.*;

public class Teste implements Estado {

	private String[][] movimentosValidos;
	private String[][] puzzleOriginal;
	private String[][] puzzleCopia;
	private String ordemBlocos;
	private List list = new ArrayList();

	private static String[][] movimentosValidosTeste =
			{
					{"2","0","3","0"},
					{"1","0","0","0"},
					{"0","2","3","0"},
					{"0","0","4","0"}
			};

	private static String[][] puzzleTeste =
			{
					{"3300","0000","1242","0000"},
					{"3400","0000","0000","0000"},
					{"0000","0020","0010","0000"},
					{"0000","0000","0024","0000"}
			};

	private static String ordemBlocosTeste = "0010000000100010";

	public Teste(String[][] puzzle, String[][] movimentosValidos, String ordemBlocos, String[][] puzzleOriginal){
		this.puzzleCopia = puzzle;
		this.puzzleOriginal = puzzleOriginal;
		this.movimentosValidos = movimentosValidos;
		this.ordemBlocos = ordemBlocos;
	}

	@Override
	public String getDescricao() {
		return null;
	}

	@Override
	public boolean ehMeta() {
		return win();
	}

	@Override
	public int custo() {
		return 1;
	}

	@Override
	public List<Estado> sucessores() {

		List<Estado> suc = new ArrayList<Estado>();

		Collection<Integer> a = List.of(1,2,3,4);
		int blocosMoveis = 1, iAxis = -1, jAxis, r = 0;
		List permute = new ArrayList();
		List movementMask = new ArrayList();
		List blockPool = new ArrayList();
		StringBuilder pieceCount = new StringBuilder();

		for (String[] s: movimentosValidos
			 ) {
			iAxis++;
			jAxis = 0;
			for (String s1: s
			) {

				if(!puzzleOriginal[iAxis][jAxis].equals("0000")){
					blockPool.add(puzzleOriginal[iAxis][jAxis]);
				}
				jAxis++;
				if (Integer.parseInt(s1) >= 2 & Integer.parseInt(s1) <= 3){
					r++;
				}
				if (Integer.parseInt(s1) > 0){
					if (Integer.parseInt(s1) >= 3){
						permute.add(blocosMoveis);
						pieceCount.append(blocosMoveis);
					}
					if(Integer.parseInt(s1) <= 2){
						movementMask.add(blocosMoveis);
						pieceCount.append(blocosMoveis);
					}else{
						movementMask.add(0);
						if (!(Integer.parseInt(s1) >= 3))
							pieceCount.append("0");
					}
					blocosMoveis++;
				}else{
					movementMask.add(0);
					pieceCount.append("0");
				}
			}
		}

		System.out.println(blockPool);

		int [] permuteIntFormat = new int[permute.size()];
		for (int i = 0; i < permute.size(); i++) {
			permuteIntFormat[i] = (int) permute.get(i);
		}

		OptionalDouble mediaDeBlocos = pieceCount.chars().average();
		permute(permuteIntFormat);

		CharSequence cs;
		for (int i = 0; i <  Math.pow(2, 16); i++) {
			cs = padLeft(Integer.toBinaryString(i),16, '0');
			if (ordemBlocos.chars().average().equals(cs.chars().average())){
				String str = cs.toString().replaceAll("1", "#");

				for (int last = 0; last < list.size(); last++) {
					String strCopy = str;
					for (int j = 0; j < list.get(last).toString().length(); j++) {
						strCopy = strCopy.replaceFirst("#", list.get(last).toString().charAt(j) + "");
					}

					String[][] movimentosValidosCopy = puzzleCopy(this.movimentosValidos); // Precisa ser copiado manualmente!

					strCopy = applyMask(strCopy, movementMask);
					if(!mediaDeBlocos.equals(strCopy.chars().average()))
						strCopy = "ERRO";

					String[] ordemBlocosCopy = strCopy.split("");
					if (!strCopy.equals("ERRO")){
						String[][] puzzleCopy = createPuzzle(blockPool, strCopy);
						if(puzzleIsConexo(puzzleCopy)){
							Teste teste = new Teste(puzzleCopy, movimentosValidos, strCopy, puzzleOriginal);
							suc.add(teste);
							System.out.println(Arrays.deepToString(puzzleCopy));
						}
					}
				}
			}
		}

		System.out.println("finish");

		return suc;
	}

	private String[][] createPuzzle(List blocks, String order){
		String[][] puzzle = new String[4][4];
		String[] orderArray = order.split("");
		int countTotal = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (orderArray[countTotal].equals("0")){
					puzzle[i][j] = "0000";
				}else{
					puzzle[i][j] = blocks.get(Integer.parseInt(orderArray[countTotal])-1).toString();
				}
				countTotal++;
			}
		}
		return puzzle;
	}

	private String applyMask(String s,List l){
		StringBuilder aux = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '0')
				aux.append(s.charAt(i));
			else
				aux.append(l.get(i));

		}
		return aux.toString();
	}

	public boolean puzzleIsConexo(String[][] puzzle){
		int upperBound = 3, lowerBound = 0;
		if (puzzle != null){
			for (int i = 0; i < 4; i++){
				for (int j = 0; j < 4; j++) {

					int solidoes = 0;
					int adjacencias = 0;

					int right = 1 + j, left = -1 + j, up = -1 + i, down = 1 + i;
					if(!puzzle[i][j].equals("0000")){
						if (up >= lowerBound) {
							adjacencias++;
							if (puzzle[up][j].equals("0000")) {
								solidoes++;
							}
						}
						if (right <= upperBound) {
							adjacencias++;
							if (puzzle[i][right].equals("0000")) {
								solidoes++;
							}
						}
						if (down <= upperBound) {
							adjacencias++;
							if (puzzle[down][j].equals("0000")) {
								solidoes++;
							}
						}
						if (left >= lowerBound) {
							adjacencias++;
							if (puzzle[i][left].equals("0000")) {
								solidoes++;
							}
						}
					}
					if(adjacencias == solidoes & solidoes != 0){return false;}
				}
			}
		}
		return true;
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

	public String rotateBlock(String puzzleString, int giros){
		for (int i = 0; i < giros; i++){
			puzzleString = puzzleString.substring(1,4) + puzzleString.charAt(0);
		}
		return puzzleString;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		String text = "";
		for (String [] s: puzzleCopia
			 ) {
			text += "[";
			for (String si: s
			) {
				text += si + ",";
			}
			text = text.substring(0,  text.length()-1) + "],";
		}
		return text.substring(0,  text.length()-1);
	}

	public boolean win(){
		int upperBound = 3, lowerBound = 0;
		if (this.puzzleCopia != null){
			for (int i = 0; i < 4; i++){
				for (int j = 0; j < 4; j++) {


					int right = 1 + j, left = -1 + j, up = -1 + i, down = 1 + i;

					// Verificação axial 1, valida peças a direita e acima
					if (axialVerification(
							i,
							j,
							up >= lowerBound,
							right <= upperBound,
							right,
							up,
							new int[]{0, 2, 2, 4}))
						return false;

					// Verificação axial 2, valida peças a esquerda e abaixo
					if (axialVerification(
							i,
							j,
							down <= upperBound,
							left >= lowerBound,
							left,
							down,
							new int[]{2, 0, 4, 2}))
						return false;
				}
			}
		}
		System.out.println("Solução: ");
		System.out.println(toString());
		return true;
	}

	private boolean axialVerification(int i, int j, boolean goUpperBound, boolean goLowerBound,
									  int horizontal, int vertical, int[] directionSet) {
		int block;
		int pairBlock;

		// Verifica o eixo Vertical
		block = Integer.parseInt(puzzleCopia[i][j].substring(directionSet[0], directionSet[0]+1));
		if (goUpperBound & block != 0){
			pairBlock = Integer.parseInt(puzzleCopia[vertical][j].substring(directionSet[1], directionSet[1]+1));
			if (block != pairBlock){
				return true;
			}
		}else if (block != 0){
			return true;
		}

		// Verifica o eixo Horizontal
		block = Integer.parseInt(puzzleCopia[i][j].substring(directionSet[0]+1, directionSet[2]));
		if (goLowerBound & block != 0){
			pairBlock = Integer.parseInt(puzzleCopia[i][horizontal].substring(directionSet[1]+1, directionSet[3]));
			if (block != pairBlock){
				return true;
			}
		}else if (block != 0){
			return true;
		}
		return false;
	}
	private String[][] puzzleCopy(String[][] collections){
		String[][] copyCollection =  new String[4][4];
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				copyCollection[i][j] = collections[i][j];
			}
		}
		return copyCollection;
	}

	public void permute(
			int[] elements) {

		String[] p = new String[factorial(elements.length)];

		if(elements.length == 1) {
			addToList(elements);
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
			addToList(elements);
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

	public int factorial(int value){
		int output = 1;
		for (int i = 2; i <= value; i++) {
			output *= i;
		}
		return output;
	}

	private void swap(int[] input, int a, int b) {
		int tmp = input[a];
		input[a] = input[b];
		input[b] = tmp;
	}

	private void swap(String[][] input, String[][] movimentosValidosCopy, String str) {

	}

	private void addToList(int[] input) {
		String value = "";
		for(int i = 0; i < input.length; i++) {
			value += input[i];
		}
		list.add(value);
	}

	public static void main(String[] args) throws Exception {

		Teste t = new Teste(puzzleTeste, movimentosValidosTeste, ordemBlocosTeste, puzzleTeste);

		//System.out.println(Arrays.deepToString(puzzleTeste));
		//System.out.println(Arrays.deepToString(t.swap(puzzleTeste, 16, 0)));

		t.sucessores();

		//Busca busca = new BuscaProfundidade(new MostraStatusConsole());
		//busca.busca(t);
	}



}