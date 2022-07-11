package Shared.NovoProblema;

import Shared.Buscas.Estado;

import java.util.List;

public class NovoProblema implements Estado {

	@Override
	public String getDescricao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean ehMeta() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int custo() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public <E extends Estado> List<E> sucessores() {
		// TODO Auto-generated method stub
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