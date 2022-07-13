package Shared.NovoProblema;

import Shared.Buscas.Estado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ConnectMeEstadoENode implements Estado {

	private String[][] movimentosValidos;
	private final String[][] puzzle;
	private String ordemBlocos;
	private final List<String> blockOrders = new ArrayList<>();
	private final boolean firstGen;
	private int next;
	private String generator;
	private int meta;
	private List<String> blockPool;
	private int rotations;

	public ConnectMeEstadoENode(String[][] puzzle, String[][] movimentosValidos, String ordemBlocos){
		this.puzzle = puzzle;
		this.movimentosValidos = movimentosValidos;
		this.ordemBlocos = ordemBlocos;
		firstGen = true;
		next = 0;
		this.generator =  "pai";
		meta = 0;
		this.blockPool = new ArrayList<String>(16);
		this.rotations = 0;
	}

	private ConnectMeEstadoENode(String[][] puzzle, int prox, String generator, int meta, List<String> blockPool, int rotations){
		this.puzzle = puzzle;
		this.generator = generator;
		firstGen = false;
		this.next = prox;
		this.meta = meta;
		this.blockPool = blockPool;
		this.rotations = rotations;
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

	@Override
	public List<Estado> sucessores() {

		List<Estado> suc = new LinkedList<>();

		if(this.firstGen) {

			List<Integer> movementMask = new ArrayList<>();
			List<String> blockPool = new ArrayList<>();
			List<Integer> permute = new ArrayList<>();

			int movableBlocks = 1, iAxis = -1, jAxis, r = 0;
			double averageStructure = 0;
			StringBuilder preStr = new StringBuilder();

			for (String[] s : movimentosValidos
			) {
				iAxis++;
				jAxis = 0;
				for (String s1 : s
				) {

					boolean blockCanSwirl = Integer.parseInt(s1) >= 2 & Integer.parseInt(s1) <= 3;
					boolean validBlock = Integer.parseInt(s1) > 0;
					boolean blockMoves = Integer.parseInt(s1) >= 3;
					boolean blockCantMove = Integer.parseInt(s1) <= 2;

					if (!puzzle[iAxis][jAxis].equals("0000")) {
						if (blockCanSwirl) {
							blockPool.add(puzzle[iAxis][jAxis] + "#");
						} else {
							blockPool.add(puzzle[iAxis][jAxis]);
						}

					}
					jAxis++;
					if (blockCanSwirl) {
						r++;
					}
					if (validBlock) {
						if (blockMoves) {
							permute.add(movableBlocks);
							preStr.append(movableBlocks);
						}
						if (blockCantMove) {
							movementMask.add(movableBlocks);
							preStr.append(movableBlocks);
						} else {
							movementMask.add(0);
							if (!(blockMoves))
								preStr.append("0");
						}
						movableBlocks++;
					} else {
						movementMask.add(0);
						preStr.append("0");
					}
				}
			}

			this.blockPool = blockPool;
			this.generator = preStr.toString();

			int[] permuteIntFormat;
			if (permute.size() == 0) {
				permuteIntFormat = new int[1];
				permuteIntFormat[0] = 1;
			} else {
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
			int maxMovements = (int) Math.pow(2, 16);
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

						// Gera uma nova movimentação

						if (validOrder) {
							String[][] newPuzzle = createBlock(blockPool, newOrder, "0000");
							if (puzzleIsConexo(newPuzzle)) {
								suc.add(new ConnectMeEstadoENode(newPuzzle, next, newOrder, meta(newPuzzle), this.blockPool, r));
							} else {
								break;
							}
						}
					}
				}
			}
			return suc;
		}else{
			int maxRotations = (int)Math.pow(rotations,4);
			if (maxRotations < 2){
				if (maxRotations == 1)
					maxRotations = 4;
				else
					maxRotations = 1;
			}
			int aux = Integer.MIN_VALUE;
			while(this.meta >= aux){
				String rotationState = padLeft(Integer.toString(Integer.parseInt(Integer.toString(next), 10), 4), rotations);
				String[][] newPuzzle = createBlock(blockPool, this.generator, rotationState);
				aux = meta(newPuzzle);
				if(aux > this.meta){
					suc.add(new ConnectMeEstadoENode(newPuzzle, this.next+1, this.generator, meta(newPuzzle), this.blockPool, this.rotations));
				}else{
					this.next++;
					if(next == maxRotations){
						return suc;
					}
				}
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