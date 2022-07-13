package Shared.NovoProblema;

import Shared.Buscas.*;

import java.util.*;

public class ConnectMeEstadoConn implements Estado {

	private String[][] movimentosValidos;
	private final String[][] puzzle;
	private String ordemBlocos;
	private final List<String> blockOrders = new ArrayList<>();
	private List<Solution[]> solutionsList;
	private final boolean firstGen;
	private Solution[] solutions;
	private int next;
	private String generator;
	private int meta;

	public ConnectMeEstadoConn(String[][] puzzle, String[][] movimentosValidos, String ordemBlocos){
		this.puzzle = puzzle;
		this.movimentosValidos = movimentosValidos;
		this.ordemBlocos = ordemBlocos;
		firstGen = true;
		next = 0;
		this.generator =  "pai";
		this.solutionsList = null;
		meta = 0;
	}

	private ConnectMeEstadoConn(String[][] puzzle, Solution[] solutions, int prox, String generator, int meta){
		this.puzzle = puzzle;
		this.solutions = solutions;
		this.generator = generator;
		firstGen = false;
		this.next = prox;
		this.meta = meta;
	}

	public String[][] getPuzzle() {
		return puzzle;
	}

	public String getGenerator() {
		return generator;
	}

	@Override
	public String getDescricao() {
		return "Connect.me, o Objetivo é conectar todos os tubos!";
	}

	@Override
	public boolean ehMeta() {
		return win();
	}

	@Override
	public int custo() {
		return 1;
	}

	private static class Solution {
		private final String[][] solution;
		private final String blockReference;
		Solution(String [][] solution, String blockReference){
			this.solution = solution;
			this.blockReference = blockReference;
		}

		public String[][] getSolution(){
			return this.solution;
		}

		public String getBlockReference() {
			return this.blockReference;
		}
	}


	private List<Solution[]> geraEstados() {

		List<Solution[]> solutions = new LinkedList<>();

		List<Integer> movementMask = new ArrayList<>();
		List<String> blockPool = new ArrayList<>();
		List<Integer> permute = new ArrayList<>();

		int movableBlocks = 1, iAxis = -1, jAxis, r = 0;
		double averageStructure = 0;
		StringBuilder preStr = new StringBuilder();


		for (String[] s: movimentosValidos
		) {
			iAxis++;
			jAxis = 0;
			for (String s1: s
			) {

				boolean blocoGira = Integer.parseInt(s1) >= 2 & Integer.parseInt(s1) <= 3;
				boolean ehUmBloco = Integer.parseInt(s1) > 0;
				boolean blocoSeMovimenta = Integer.parseInt(s1) >= 3;
				boolean blocoEstatico = Integer.parseInt(s1) <= 2;

				if(!puzzle[iAxis][jAxis].equals("0000")){
					if (blocoGira){
						blockPool.add(puzzle[iAxis][jAxis] + "#");
					}else{
						blockPool.add(puzzle[iAxis][jAxis]);
					}

				}
				jAxis++;
				if (blocoGira){
					r++;
				}
				if (ehUmBloco){
					if (blocoSeMovimenta){
						permute.add(movableBlocks);
						preStr.append(movableBlocks);
					}
					if(blocoEstatico){
						movementMask.add(movableBlocks);
						preStr.append(movableBlocks);
					}else{
						movementMask.add(0);
						if (!(blocoSeMovimenta))
							preStr.append("0");
					}
					movableBlocks++;
				}else{
					movementMask.add(0);
					preStr.append("0");
				}
			}
		}

		this.generator = preStr.toString();

		int [] permuteIntFormat;
		if (permute.size() == 0){
			permuteIntFormat = new int[1];
			permuteIntFormat[0] = 1;
		}else{
			permuteIntFormat = new int[permute.size()];
		}

		for (int i = 0; i < permute.size(); i++) {
			permuteIntFormat[i] = permute.get(i);
		}

		permute.clear();
		if (preStr.chars().average().isPresent())
			averageStructure = preStr.chars().average().getAsDouble();
		permute(permuteIntFormat);

		CharSequence cs;
		int maxRotations = (int)Math.pow(r,4);
		if (maxRotations < 2){
			if (maxRotations == 1)
				maxRotations = 4;
			else
				maxRotations = 1;
		}
		int maxMovements = (int)Math.pow(2, 16);
		for (int i = maxMovements; i >= 0; i--) {
			cs = padLeft(Integer.toBinaryString(i), 16);

			boolean movimentoValido = ordemBlocos.chars().average().equals(cs.chars().average());
			if (movimentoValido) {
				String order = cs.toString().replaceAll("1", "#");
				for (Object o : blockOrders) {
					String newOrder = order;
					for (int j = 0; j < o.toString().length(); j++) {
						newOrder = newOrder.replaceFirst("#", o.toString().charAt(j) + "");
					}

					newOrder = applyMask(newOrder, movementMask);
					boolean validOrder = false;
					if (newOrder.chars().average().isPresent())
						validOrder = averageStructure == newOrder.chars().average().getAsDouble();

					// Cria um bloco novo, com a nova rotação

					if (validOrder) {
						Solution[] sol = new Solution[maxRotations];
						boolean solValid = false;
						for (int j = 0; j < maxRotations; j++) {
							String rotationState = padLeft(Integer.toString(Integer.parseInt(Integer.toString(j), 10), 4), r);
							String[][] puzzleNovo = createBlock(blockPool, newOrder, rotationState);
							if (puzzleIsConexo(puzzleNovo)) {
								sol[j] = new Solution(puzzleNovo, newOrder);
							} else {
								solValid = true;
								break;
							}
						}
						if (!solValid)
							solutions.add(sol);
					}
				}
			}
		}
		return solutions;
	}

	@Override
	public List<Estado> sucessores() {

		List<Estado> suc = new LinkedList<>();

		if(firstGen){
			this.solutionsList = geraEstados();
			for (Solution[] value : solutionsList) {
				for (int j = 0; j < value.length; j++) {
					if (value[j] != null) {
						String[][] newPuzzle = value[j].getSolution();
						String newGenerator = value[j].getBlockReference();
						value[j] = null;
						ConnectMeEstadoConn sucessor = new ConnectMeEstadoConn(newPuzzle, value, 1, newGenerator, meta(newPuzzle));
						suc.add(sucessor);
						break;
					}
				}
			}
		}else{
			try {
				int aux = Integer.MIN_VALUE;
				while(this.meta >= aux){ //10 > 0; 10 > 9
					String[][] newPuzzle = this.solutions[next].getSolution();
					aux = meta(newPuzzle);
					if(aux > this.meta){
						String newGenerator = this.solutions[next].getBlockReference();
						ConnectMeEstadoConn sucessor = new ConnectMeEstadoConn(newPuzzle, this.solutions, next + 1, newGenerator, aux);
						suc.add(sucessor);
					}else{
						this.next++;
						if(next == this.solutions.length){
							return suc;
						}
					}
				}
			}catch(Exception e){
				// Fim do nó deste sucessor
			}
		}
		return suc;
	}

	private String[][] createBlock(List<String> blocks, String order, String giros){
		String[] orderArray = order.split("");
		String[][] puzzleLocal = puzzleCopy(this.puzzle);
		int countTotal = 0;
		int countBlocos = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (orderArray[countTotal].equals("0")){
					if (!puzzleLocal[i][j].equals("0000"))
						puzzleLocal[i][j] = "0000";
				}else{
					String bloco = blocks.get(Integer.parseInt(orderArray[countTotal])-1);
					if (bloco.length() > 4){
						bloco = bloco.replace("#", "");
						puzzleLocal[i][j] = rotateBlock(bloco, giros.charAt(countBlocos));
						countBlocos++;
					}else{
						puzzleLocal[i][j] = bloco;
					}
				}
				countTotal++;
			}
		}
		return puzzleLocal;
	}

	private String applyMask(String s,List<Integer> l){
		StringBuilder aux = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '0')
				aux.append(s.charAt(i));
			else
				aux.append(l.get(i));
		}
		return aux.toString();
	}

	private boolean puzzleIsConexo(String[][] puzzle){
		int upperBound = 3, lowerBound = 0;
		if (puzzle != null){
			for (int i = 0; i < 4; i++){
				for (int j = 0; j < 4; j++) {

					int connections = 0;
					int connectors = 0;

					int right = 1 + j, left = -1 + j, up = -1 + i, down = 1 + i;
					if(!puzzle[i][j].equals("0000")){
						if (up >= lowerBound) {
							connectors++;
							if (puzzle[up][j].equals("0000")) {
								connections++;
							}
						}
						if (right <= upperBound) {
							connectors++;
							if (puzzle[i][right].equals("0000")) {
								connections++;
							}
						}
						if (down <= upperBound) {
							connectors++;
							if (puzzle[down][j].equals("0000")) {
								connections++;
							}
						}
						if (left >= lowerBound) {
							connectors++;
							if (puzzle[i][left].equals("0000")) {
								connections++;
							}
						}
					}
					if(connectors == connections & connections != 0){return false;}
				}
			}
		}
		return true;
	}

	private String padLeft(String string, int length) {
		if (string.length() >= length) {
			return string;
		}
		StringBuilder sb = new StringBuilder();
		while (sb.length() < length - string.length()) {
			sb.append('0');
		}
		sb.append(string);

		return sb.toString();
	}

	private String rotateBlock(String puzzleString, char giros){
		for (int i = 0; i < Integer.parseInt(String.valueOf(giros)); i++){
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
		return (Arrays.deepToString(puzzle)).hashCode();
	}
	
	@Override
	public String toString() {
		return Arrays.deepToString(puzzle);
	}

	private boolean win(){
		int upperBound = 3, lowerBound = 0;
		if (this.puzzle != null){
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
		return true;
	}

	private int meta(String[][] newPuzzle){
		int meta = 0;
		int upperBound = 3, lowerBound = 0;
		if (newPuzzle != null){
			for (int i = 0; i < 4; i++){
				for (int j = 0; j < 4; j++) {
					int right = 1 + j, left = -1 + j, up = -1 + i, down = 1 + i;

					meta++;

					// Verificação axial 1, valida peças a direita e acima
					if (axialVerification(
							i,
							j,
							up >= lowerBound,
							right <= upperBound,
							right,
							up,
							new int[]{0, 2, 2, 4},
							newPuzzle))
						return meta;

					// Verificação axial 2, valida peças a esquerda e abaixo
					if (axialVerification(
							i,
							j,
							down <= upperBound,
							left >= lowerBound,
							left,
							down,
							new int[]{2, 0, 4, 2},
							newPuzzle))
						return meta;
				}
			}
		}
		return meta;
	}

	private boolean axialVerification(int i, int j, boolean goUpperBound, boolean goLowerBound,
									  int horizontal, int vertical, int[] directionSet) {
		int block;
		int pairBlock;

		// Verifica o eixo Vertical
		block = Integer.parseInt(puzzle[i][j].substring(directionSet[0], directionSet[0]+1));
		if (goUpperBound & block != 0){
			pairBlock = Integer.parseInt(puzzle[vertical][j].substring(directionSet[1], directionSet[1]+1));
			if (block != pairBlock){
				return true;
			}
		}else if (block != 0){
			return true;
		}

		// Verifica o eixo Horizontal
		block = Integer.parseInt(puzzle[i][j].substring(directionSet[0]+1, directionSet[2]));
		if (goLowerBound & block != 0){
			pairBlock = Integer.parseInt(puzzle[i][horizontal].substring(directionSet[1]+1, directionSet[3]));
			return block != pairBlock;
		}else return block != 0;
	}

	private boolean axialVerification(int i, int j, boolean goUpperBound, boolean goLowerBound,
									  int horizontal, int vertical, int[] directionSet, String[][] newPuzzle) {
		int block;
		int pairBlock;

		// Verifica o eixo Vertical
		block = Integer.parseInt(newPuzzle[i][j].substring(directionSet[0], directionSet[0]+1));
		if (goUpperBound & block != 0){
			pairBlock = Integer.parseInt(newPuzzle[vertical][j].substring(directionSet[1], directionSet[1]+1));
			if (block != pairBlock){
				return true;
			}
		}else if (block != 0){
			return true;
		}

		// Verifica o eixo Horizontal
		block = Integer.parseInt(newPuzzle[i][j].substring(directionSet[0]+1, directionSet[2]));
		if (goLowerBound & block != 0){
			pairBlock = Integer.parseInt(newPuzzle[i][horizontal].substring(directionSet[1]+1, directionSet[3]));
			return block != pairBlock;
		}else return block != 0;
	}

	private String[][] puzzleCopy(String[][] collections){
		String[][] copyCollection =  new String[4][4];
		for (int i = 0; i < 4; i++){
			System.arraycopy(collections[i], 0, copyCollection[i], 0, 4);
		}
		return copyCollection;
	}

	private void permute(int[] elements) {
		if(elements.length == 1) {
			addOrder(elements);
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

	private void permute(int n, int[] elements) {
		if(n == 1) {
			addOrder(elements);
		}else{
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

	private void swap(int[] elements, int i, int j) {
		int tmp = elements[i];
		elements[i] = elements[j];
		elements[j] = tmp;
	}

	private void addOrder(int[] input) {
		StringBuilder value = new StringBuilder();
		for (int j : input) {
			value.append(j);
		}
		blockOrders.add(value.toString());
	}

}