package Process;

import Shared.Buscas.Estado;

import java.util.List;

public class WidthProcess implements Estado {
    /**
     * retorna uma descricao do problema que esta representacao
     * de estado resolve
     */
    @Override
    public String getDescricao() {
        return null;
    }

    /**
     * verifica se o estado e meta
     */
    @Override
    public boolean ehMeta() {
        return false;
    }

    /**
     * Custo para geracao deste estado
     * (nao e o custo acumulado --- g)
     */
    @Override
    public int custo() {
        return 0;
    }

    /**
     * gera uma lista de sucessores do nodo.
     */
    @Override
    public <E extends Estado> List<E> sucessores() {
        return null;
    }

    // Os dois metodos sao usados em conjunto

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
}

