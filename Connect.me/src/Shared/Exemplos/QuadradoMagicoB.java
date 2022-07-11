package Shared.Exemplos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Shared.Buscas.Aleatorio;
import Shared.Buscas.Estado;
import Shared.Buscas.Nodo;
import Shared.Buscas.SubidaMontanha;

/**
 * Problema do quadrado m�gico (ver getDescricao)
 *
 * Representa um estado do mundo: o nro que est� em cada
 * posi��o do quadro.
 *
 *
 * Nesta solu��o o estado inicial � o quadro com os n�meros
 * distribu�dos aleatoriamente e os sucessores s�o gerados
 * por trocas de posi��o entre n�meros.
 */
public class QuadradoMagicoB extends QuadradoMagico implements Aleatorio {
    
    public String getDescricao() {
        return
        "Um quadrado m�gico de ordem n � um arranjo quadrado de n� inteiros\n"+
        "distintos dispostos de tal maneira que os n�meros de uma linha\n"+
        "qualquer, de uma coluna qualquer ou da diagonal principal t�m mesma\n"+
        "soma, chamada constante m�gica do quadrado. O quadrado � normal se os\n"+
        "n� n�meros que o formam s�o os primeiros n� inteiros positivos.\n\n"+
        "A constante m�gica do quadrado � dada por: n (n� + 1) / 2\n"+
        "Neste exemplo, n = 4 e a constante m�gica=34\n\n"+
        "Nesta vers�o (b), o tabuleiro inicia com os n�meros\n"+
        "aleatoriamente posicionados e cada opera��o\n"+
        "troca dois n�meros de lugar\n"+
        "(tem heur�stica e gera��o de estados aleat�rios implementado)\n";
    }
    
    /**
     *  cria um estado inicial (aleat�rio)
     */
    public QuadradoMagicoB() {
        for (int i=1; i<=tam*tam; i++) {
            // tenta at� achar um posi��o livre
            int l = Math.round( (float)(Math.random()*(tam-1)) );
            int c = Math.round( (float)(Math.random()*(tam-1)) );
            while (tabuleiro[l][c] != 0) {
                l = Math.round( (float)(Math.random()*(tam-1)) );
                c = Math.round( (float)(Math.random()*(tam-1)) );
            }
            tabuleiro[l][c] = i;
        }
        meuNro = tam*tam;
    }
    
    /**
     *  cria um estado inicial a partir de outro (copia)
     */
    QuadradoMagicoB(QuadradoMagico modelo) {
        super(modelo);
    }
    
    public Estado geraAleatorio() {
        return new QuadradoMagicoB();
    }
    
    /**
     * gera uma lista de sucessores do nodo.
     * (troca dois n�meros de posi��o no tabuleiro)
     */
    public List<Estado> sucessores() {
        List<Estado> suc = new ArrayList<Estado>(); // a lista de sucessores
        
        // coloca o seguinte em todas as posi��es livres
        for (int l=0;l<tam-1;l++) {
            for (int c=0;c<tam-1;c++) {
                // troca com o pr�ximo na linha
                QuadradoMagicoB novo = new QuadradoMagicoB(this);
                int temp = novo.tabuleiro[l][c];
                novo.tabuleiro[l][c] = novo.tabuleiro[l+1][c];
                novo.tabuleiro[l+1][c] = temp;
                suc.add(novo);
                
                // troca com o debaixo
                novo = new QuadradoMagicoB(this);
                temp = novo.tabuleiro[l][c];
                novo.tabuleiro[l][c] = novo.tabuleiro[l][c+1];
                novo.tabuleiro[l][c+1] = temp;
                suc.add(novo);
            }
        }
        
        return suc;
    }
    
    public static void main(String[] a) {
        QuadradoMagicoB.tam = 4;
        
        //Estado inicial  = new EstadoSoma34a();
        Estado inicial  = new QuadradoMagicoB();
        Nodo n;
        
        System.out.println("Come�ou em "+new Date());
        
        System.out.println("Estado inicial "+inicial);
        
        //n = Busca.buscaLargura(inicial, null); // (d� "out of memory")
        //n = Busca.buscaProfRec(inicial, null, 18); // (demora muito)
        
        // chama busca em profundidade iterativo
        // (como se sabe que a solu��o esta em 16 n�veis, n�o
        // faz sentido busca em prof. iterativo)
        //n = Busca.buscaProfIterativo(inicial, null);
        
        //n = Busca.buscaHeuristica(inicial, null);
        /*
        if (n == null) {
            System.out.println("sem solu��o!");
        } else {
            System.out.println("solu��o:\n" + n.montaCaminho() + "\n\n");
        }
         */
        
        n = new SubidaMontanha().busca(inicial);
        System.out.println("solu��o:\n" + n.getEstado() + "\n\n");
        System.out.println("Terminou em "+new Date());
    }
}
